package kz.bsbnb.util;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * Created by ruslan on 05.12.16.
 */
public class CheckUtil {

    private final static int INN_LENGTH = 12;

    // RI - Rnn & Inn
    public static class RIException extends Exception {

        public RIException(String msg) {
            super(msg);
        }
    }

     public static class INNLenException extends RIException {

        public INNLenException(int len) {
            super("ИИН/БИН должен содержать 12 цифр");
        }
    }

    public static class INNNotValidChar extends RIException {

        public INNNotValidChar(String err) {
            super(err);
        }
    }

    public static class INNControlSum10 extends RIException {

        public INNControlSum10() {
            // Если полученное число также равно 10, то данный ИИН не используется.
            super("Контрольная сумма равна 10. Нельзя использовать этот ИИН/БИН");
        }
    }


    /**
     * Проверка номера ИНН
     */
    public static boolean INN(String inn) throws INNLenException, INNNotValidChar, INNControlSum10 {
        // check INN/BIN lenght
        if (inn.length() != INN_LENGTH) {
            throw new INNLenException(inn.length());
        }

        if (inn.split("\\d").length > 0) {
            throw new INNNotValidChar("ИИН/БИН содержит не правильные символы");
        }

        int q = 3;
        int summa = 0;
        int summa2 = 0;

        for (int i = 1; i <= INN_LENGTH - 1; i++) {
            int n = Integer.parseInt(inn.substring(i - 1, i));
            summa = summa + i * n;
            summa2 = summa2 + q * n;
            q = q + 1;
            if (q == INN_LENGTH) {
                q = 1;
            }
        }

        summa = summa % (INN_LENGTH - 1);
        if (summa == 10) {
            summa = summa2 % (INN_LENGTH - 1);
            if (summa == 10) {
                //  Если полученное число также равно 10, то данный ИИН не используется.
                throw new INNControlSum10();
            }
        }

        return (summa == Integer.parseInt(inn.substring(INN_LENGTH - 1, INN_LENGTH)));
    }


}