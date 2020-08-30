
package utility;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashPassword {
     public static String hash(String password) {
         StringBuffer sb = new StringBuffer();
        try {
            // loading md5 algorithm
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte[] b = md.digest();

            // string buffer
            for (byte b1: b) {
                sb.append(Integer.toHexString(b1 & 0xff).toString());
            }
        } catch(NoSuchAlgorithmException e) {
            System.out.println(e);
        }
        
        return sb.toString();
    }
}
