package artsploit;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPOutputStream;

public class B64 {
    public static void main(String[] args) throws IOException {
        InputStream in = B64.class.getClassLoader().getResourceAsStream("artsploit/TestInterceptor.class");
        byte[] bytes = new byte[in.available()];
        in.read(bytes);
        // 将字节压缩下
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        GZIPOutputStream gzipOutputStream=new GZIPOutputStream(byteArrayOutputStream);
        gzipOutputStream.write(bytes);
        gzipOutputStream.close();
        System.out.println(java.util.Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray()));
    }
}
