package kz.bsbnb.common.util;

import com.google.common.base.CharMatcher;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Olzhas.Pazyldayev on 28.11.2016.
 */
public class MaskValidator {
    private static final String USERNAME_EXPRESSION = "^(?=.{6,20}$)([a-zA-Z]+[a-zA-Z0-9_\\-\\.]+)$";
    private static final String PASSWORD_EXPRESSION = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@!.,_$])(?=\\S+$).{8,}$";
    private static final String EMAIL_EXPRESSION = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
    private static final String PHONE_EXPRESSION = "^([0-9]{10})$";
    private static final String IDN_EXPRESSION = "^(?=.{12}$)([0-9]+)$";
    private static final String CODEWORD_EXPRESSION = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!#$%&*?@^_~])(?=\\S+$).{8,}$";


    public static boolean isUsernameValid(String username) {
        return isValid(username, USERNAME_EXPRESSION, true);
    }

    public static boolean isPasswordValid(String password) {
        return isValid(password, PASSWORD_EXPRESSION, true);
    }

    public static boolean isEmailValid(String email) {
        return isValidCaseInsensitive(email, EMAIL_EXPRESSION, false);
    }

    public static boolean isPhoneValid(String phone) {
        return isValid(phone, PHONE_EXPRESSION, true);
    }

    public static boolean isIdnValid(String idn) {
        return isValid(idn, IDN_EXPRESSION, false) && validateIDN(idn);
    }

    public static boolean isIdnValidExpression(String idn) {
        return isValid(idn, IDN_EXPRESSION, false);
    }

    public static boolean isCodewordValid(String codeword) {
        return isValid(codeword, CODEWORD_EXPRESSION, true);
    }

    private static boolean isValid(String match, String expression, Boolean ascii) {
        if (match == null || match.isEmpty()) {
            return false;
        }
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(match);
        if (ascii) {
            return matcher.matches() && CharMatcher.ascii().matchesAllOf(match);
        } else {
            return matcher.matches();
        }
    }

    public static boolean validateIDN(String idn) {
        if (idn.length() != 12)
            return false;

        if (idn.equals("000000000000"))
            return false;

        int[] a = new int[11];
        int idnDg = 0;

        for (int i = 0; i < 11; i++) {
            try {
                a[i] = Integer.parseInt(idn.substring(i, 1 + i));
            } catch (NumberFormatException ex) {
                return false;
            }
            idnDg += (i + 1) * a[i];
        }

        idnDg = idnDg % 11;

        if (idnDg == 10) {
            idnDg = (3 * a[0] + 4 * a[1] + 5 * a[2] + 6 * a[3] + 7 * a[4] + 8 * a[5] + 9 * a[6] + 10 * a[7] + 11 * a[8] + 1 * a[9] + 2 * a[10]) % 11;
        }

        int lastDigit;
        try {
            lastDigit = Integer.parseInt(idn.substring(11, 12));
        } catch (NumberFormatException ex) {
            return false;
        }

        if (idnDg == lastDigit)
            return true;
        else
            return false;
    }

    public static boolean isValidCaseInsensitive(String match, String expression, Boolean ascii) {
        if (match == null || match.isEmpty()) {
            return false;
        }
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(match);
        if (ascii) {
            return matcher.matches() && CharMatcher.ascii().matchesAllOf(match);
        } else {
            return matcher.matches();
        }
    }


}
