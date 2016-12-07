package kz.bsbnb.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ruslan on 07.12.16.
 */
public class PasswordUtil {

    private Pattern symbolsPattern;
    private Matcher matcher;

    private static final String SYMBOLS_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,14})" +
            "|((?=.*\\d)(?=.*[а-я])(?=.*[А-Я]).{8,14})";

    /**
     * (            # Начало группы
     * (?=.*\d)     #   должен содержать как минимум одну цифру
     * (?=.*[a-z])  #   должен содержать как минимум букву в нижнем регистре
     * (?=.*[A-Z])  #   должен содержать как минимум букву в верхнем регистре
     * .     любое совпадение с предыдущими условиями
     * {8,14}       #     длина от 8 до 14 символов
     * )            # Конец группы
     * <p/>
     * Аналогичная запись для русского/украинского алфавита
     */

    private static final String REPEATS_PATTERN = "(%s)|(%s)";

    /**
     * Максимальное количество дублирующихся символов
     */
    private static final int MAX_COUNT_DUBLICATED_SYMBOLS = 3;

    /**
     * Длина строки для проверки на предопределенные последовательности
     */
    private static final int MAX_STRING_LENGHT_SEQUENCES = 4;

    /**
     * Длина строки для проверки повтор прямых и отраженных комбинаций
     */
    private static final int MAX_STRING_LENGHT_REPEATS = 2;

    /**
     * Предопределенные последовательности: клавиатуры, алфавиты, цифровые последовательности
     */
    private static String[] predefinedSequences = {
            /**
             * клавиатура ENG
            */
            "qwertyuiopasdfghjklzxcvbnm",
            /**
             * клавиатура RUS
            */
            "йцукенгшщзхъфывапролджэячсмитьбю",
            /**
             * клавиатура UKR
            */
            "йцукенгшщзхїфівапролджєячсмитьбю",
            /**
             * Алфавит ENG
            */
            "abcdefghijklmnopqrstuvwxyz",

            /**
             * Алфавит RUS
            */
            "абвгдеёжзиклмнопрстуфхцчшщьъэюя",

            /**
             * Алфавит UKR
            */
            "абвгдеєжзиіїйклмнопрстуфхцчшщьюя",
            /**
             * Цифры
            */
            "1234567890"
    };


    public PasswordUtil() {
        symbolsPattern = Pattern.compile(SYMBOLS_PATTERN);

    }

    public boolean validate(final String password) {
        return validateSymbols(password);
//        &&
//                validateMatches(password) &&
//                validatePredefinedSequences(password) &&
//                validateDublicatedSymbols(password);
    }

    /**
     * Проверка пароля на существование в нем определенных символов.
     *
     * @param password пароль для валидации
     * @return true если пароль валидный, false - пароль не валидный
     */
    private boolean validateSymbols(final String password) {
        matcher = symbolsPattern.matcher(password);
        return matcher.matches();
    }

    /**
     * Проверка на повтор прямых и отраженных комбинаций
     *
     * @param password пароль
     * @return true если пароль валидный, false - пароль невалидный
     */
    private boolean validateMatches(final String password) {
        int countOfMatches;
        String passwLower = password.toLowerCase();
        for (int i = 0; i < passwLower.length() / 2; ++i) {
            for (int j = i + MAX_STRING_LENGHT_REPEATS;
                 j < passwLower.length() / 2 + MAX_STRING_LENGHT_REPEATS; ++j) {
                //чтобы не выйти за пределы массива
                if (passwLower.length() < passwLower.length() / 2 + MAX_STRING_LENGHT_REPEATS) {
                    break;
                }
                countOfMatches = 0;
                String matchWord = passwLower.substring(i, j);
                String matchWorldReverse = new StringBuffer(matchWord).reverse().toString();
                Pattern pattern = Pattern.compile(String.format(REPEATS_PATTERN, matchWord, matchWorldReverse));
                matcher = pattern.matcher(passwLower);
                while (matcher.find()) {
                    if (++countOfMatches > 1) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Проверка на наличие предопределенных последовательностей символов
     * (алфавитные последовательности, клавиатурные последовательности)
     *
     * @param password
     * @return true если пароль валидный, false - пароль невалидный
     */
    private boolean validatePredefinedSequences(final String password) {
        int coincidences;
        String passwordLower = password.toLowerCase();

        for (int i = 0; i < predefinedSequences.length; ++i) {
            String currentKeyboard = predefinedSequences[i];
            for (int j = 0; j < currentKeyboard.length() - MAX_STRING_LENGHT_SEQUENCES + 1; ++j) {
                coincidences = 0;
                String matchWord = currentKeyboard.substring(j, j + MAX_STRING_LENGHT_SEQUENCES);
                String matchWorldReverse = new StringBuffer(matchWord).reverse().toString();
                Pattern pattern = Pattern.compile(String.format(REPEATS_PATTERN, matchWord, matchWorldReverse));
                matcher = pattern.matcher(passwordLower);
                while (matcher.find()) {
                    if (++coincidences > 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Учет количества одинаковых символов
     *
     * @param password
     * @return true если пароль валидный, false - пароль невалидный
     */
    private boolean validateDublicatedSymbols(final String password) {
        String passwordLower = password.toLowerCase();
        int countOfDublicated;
        char symbol;
        for (int i = 0; i < passwordLower.length() / 2 + 1; ++i) {
            countOfDublicated = 0;
            symbol = password.charAt(i);
            for (int j = i; j < passwordLower.length(); ++j) {
                if (passwordLower.charAt(j) == symbol) {
                    if (++countOfDublicated > MAX_COUNT_DUBLICATED_SYMBOLS) return false;
                }
            }
        }
        return true;
    }

}
