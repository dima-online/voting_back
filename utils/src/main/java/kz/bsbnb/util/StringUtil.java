package kz.bsbnb.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by ruslan on 19.10.16.
 */
public class StringUtil {

    private static String convertToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();
        for (byte b : data) {
            int halfbyte = (b >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                buf.append((0 <= halfbyte) && (halfbyte <= 9) ? (char) ('0' + halfbyte) : (char) ('a' + (halfbyte - 10)));
                halfbyte = b & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }

    public static String SHA(String text) {
        MessageDigest md = null;
        String result = "";
        try {
            md = MessageDigest.getInstance("SHA-256");
            md.update(text.getBytes("iso-8859-1"), 0, text.length());
            byte[] sha1hash = md.digest();
            result = convertToHex(sha1hash);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            System.out.println("Error======>" + e.getMessage());
        }
        return result;
    }

    public static String RND(int count) {
        String result = "";
        for (int i = 1; i <= count; i++) {
            int n = (int) (Math.random() * 10);
            result = result + String.valueOf(n);
//            result = result + String.valueOf(i); --Простой пароль
        }
        return result;
    }
}
