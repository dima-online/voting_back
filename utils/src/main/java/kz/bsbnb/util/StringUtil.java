package kz.bsbnb.util;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Random;

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

    public static String md5(String st) {
        MessageDigest messageDigest = null;
        byte[] digest = new byte[0];

        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(st.getBytes());
            digest = messageDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            // тут можно обработать ошибку
            // возникает она если в передаваемый алгоритм в getInstance(,,,) не существует
            e.printStackTrace();
        }

        BigInteger bigInt = new BigInteger(1, digest);
        String md5Hex = bigInt.toString(16);

        while( md5Hex.length() < 32 ){
            md5Hex = "0" + md5Hex;
        }

        return md5Hex;
    }

    public static String RND(int from, int to) {
//        for (int i = 1; i <= count; i++) {
//            int n = (int) (Math.random() * 10);
//            result = result + String.valueOf(n);
////            result = result + String.valueOf(i); --Простой пароль
//        }
        String result  = "";
        Random r     = new Random();
        int cntchars = from + r.nextInt(to - from + 1);

        for (int i = 0; i < cntchars; ++i) {
            char next = 0;
            int range = 10;

            switch(r.nextInt(3)) {
                case 0: {next = '0'; range = 10;} break;
                case 1: {next = 'a'; range = 26;} break;
                case 2: {next = 'A'; range = 26;} break;
            }

            result += (char)((r.nextInt(range)) + next);
        }

        return result;
    }

    //assertTrue(is_pdf(Files.readAllBytes(Paths.get("output.pdf"));
    public static boolean is_pdf(byte[] data) {
        if (data != null && data.length > 4 &&
                data[0] == 0x25 && // %
                data[1] == 0x50 && // P
                data[2] == 0x44 && // D
                data[3] == 0x46 && // F
                data[4] == 0x2D) { // -

            // version 1.3 file terminator
            if (data[5] == 0x31 && data[6] == 0x2E && data[7] == 0x33 &&
                    data[data.length - 7] == 0x25 && // %
                    data[data.length - 6] == 0x25 && // %
                    data[data.length - 5] == 0x45 && // E
                    data[data.length - 4] == 0x4F && // O
                    data[data.length - 3] == 0x46 && // F
                    data[data.length - 2] == 0x20 && // SPACE
                    data[data.length - 1] == 0x0A) { // EOL
                return true;
            }

            // version 1.3 file terminator
            if (data[5] == 0x31 && data[6] == 0x2E && data[7] == 0x34 &&
                    data[data.length - 6] == 0x25 && // %
                    data[data.length - 5] == 0x25 && // %
                    data[data.length - 4] == 0x45 && // E
                    data[data.length - 3] == 0x4F && // O
                    data[data.length - 2] == 0x46 && // F
                    data[data.length - 1] == 0x0A) { // EOL
                return true;
            }
        }
        return false;
    }

    public static final String DECIMAL_PATTERN = "###,###.###";
    public static final String INTEGER_PATTERN = "###,###";

    public static String parseToStr(Object value) {
        String format = DECIMAL_PATTERN;
        if (value instanceof BigInteger || value instanceof Integer || value instanceof Long) {
            format = INTEGER_PATTERN;
        }
        DecimalFormat myFormatter = new DecimalFormat(format);
        DecimalFormatSymbols formatSymbols = myFormatter.getDecimalFormatSymbols();
        formatSymbols.setGroupingSeparator(' ');
        myFormatter.setDecimalFormatSymbols(formatSymbols);

        String output = myFormatter.format(value);
        return output;
    }
}
