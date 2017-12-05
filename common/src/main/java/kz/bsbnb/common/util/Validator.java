package kz.bsbnb.common.util;


import kz.bsbnb.util.exception.UserShowException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;


/**
 * Created by Olzhas.Pazyldayev on 28.10.2016.
 */
public class Validator {

    public static final String EMPTY = "$EMPTY";

    public static void throwRuntimeException(String errorMessage, Boolean show) {
        if (show) {
            throw new UserShowException(errorMessage);
        } else {
            throw new RuntimeException(errorMessage);
        }
    }

    public static void throwDateTimeException(String errorMessage, Boolean show) {
        if (show) {
            throw new UserShowException(errorMessage);
        } else {
            throw new DateTimeException(errorMessage);
        }
    }

    public static void throwNumberFormatException(String errorMessage, Boolean show) {
        if (show) {
            throw new UserShowException(errorMessage);
        } else {
            throw new NumberFormatException(errorMessage);
        }
    }

    public static String checkStringNotNullOrEmpty(String string, Object errorMessage, Boolean show) {
        if (string == null || string.equals("") || string.equals(EMPTY)) {
            throwRuntimeException(String.valueOf(errorMessage), show);
        } else if (string.isEmpty()) {
            throwRuntimeException(String.valueOf(errorMessage), show);
        }
        return string;
    }

    public static void checkStringNotBlank(String string, Object errorMessage, Boolean show) {
        if (StringUtils.isBlank(string)) {
            throwRuntimeException(String.valueOf(errorMessage), show);
        }
    }

    public static <T> T checkObjectNotNull(T object, Object errorMessage, Boolean show) {
        if (object == null) {
            throwRuntimeException(String.valueOf(errorMessage), show);
            return null;
        } else {
            return object;
        }
    }

    public static <T> T checkObjectNotNullRuntime(T object, Object errorMessage, Boolean show) {
        if (object == null) {
            throwRuntimeException(String.valueOf(errorMessage), show);
            return null;
        } else {
            return object;
        }
    }

    public static void checkObjectIsNull(Object object, Object errorMessage, Boolean show) {
        if (object != null) {
            throwRuntimeException(String.valueOf(errorMessage), show);
        }
    }

    public static Collection checkCollectionNullOrEmpty(Collection collection, Object errorMessage, Boolean show) {
        if (collection == null) {
            throwRuntimeException(String.valueOf(errorMessage), show);
            return null;
        } else if (collection.isEmpty()) {
            throwRuntimeException(String.valueOf(errorMessage), show);
            return null;
        }
        return collection;
    }

    public static void checkIfDateExpired(Date date, String errorMessage, Boolean show) {
        if (LocalDate.now().isAfter(DateUtil.asLocalDate(date))) {
            throwDateTimeException(errorMessage, show);
        }
    }

    public static void checkIfDateNotInPast(Date date, String errorMessage, Boolean show) {
        if (LocalDate.now().isAfter(DateUtil.asLocalDate(date))) {
            if (!DateUtils.isSameDay(new Date(), date)) {
                throwDateTimeException(errorMessage, show);
            }
        }
    }

    public static void checkIfNowTimeIsBetween(LocalTime start, LocalTime end, String errorMessage, Boolean show) {
        LocalTime nowTime = LocalTime.now();
        if (nowTime.isAfter(start) && nowTime.isBefore(end)) {
            throwDateTimeException(errorMessage, show);
        }
    }

    public static void checkIfNowTimeAfterAndBefore(LocalTime start, LocalTime end, String errorMessage, Boolean show) {
        LocalTime nowTime = LocalTime.now();
        if (nowTime.isAfter(start) || nowTime.isBefore(end)) {
            throwDateTimeException(errorMessage, show);
        }
    }


    public static void checkTransferDay(Date lastRateDate, String errorMessage, Boolean show) {
        Date preLast = DateUtil.dayBefore(lastRateDate);
        if (DateUtils.isSameDay(new Date(), preLast)) {
            throwDateTimeException(errorMessage, show);
        }
        if (new Date().after(preLast)) {
            throwDateTimeException(errorMessage, show);
        }
    }

    public static void checkTransferStartTime(Date day, String errorMessage, Boolean show) {
        Calendar firstDay = Calendar.getInstance();
        firstDay.setTime(day);
        firstDay.set(Calendar.HOUR_OF_DAY, 9);
        firstDay.set(Calendar.MINUTE, 0);
        firstDay.set(Calendar.SECOND, 0);
        firstDay.set(Calendar.MILLISECOND, 0);
        if (new Date().before(firstDay.getTime())) {
            throwDateTimeException(errorMessage, show);
        }
    }

    public static void checkChangeUserInfoDay(List<DayOfWeek> daysOfWeekNotAllowed, List<LocalDate> datesOfYearNotAllowed, Boolean show, String... errorMessage) {
        LocalDate nowDate = LocalDate.now();
        if (daysOfWeekNotAllowed.contains(nowDate.getDayOfWeek())) {
            throwDateTimeException(errorMessage[0], show);
        }
        if (datesOfYearNotAllowed.contains(nowDate)) {
            throwDateTimeException(errorMessage[1], show);
        }
    }

    public static void checkLongValBiggerThanVal2(Long value, Long value2, String errorMessage, Boolean show) {
        if (value > value2) {
            throwNumberFormatException(errorMessage, show);
        }
    }

    public static void checkLongEquals(Long value, Long value2, String errorMessage, Boolean show) {
        if (!value.equals(value2)) {
            throwNumberFormatException(errorMessage, show);
        }
    }

    public static void checkObjectsEqual(Object object, Object object1, String errorMessage, Boolean show) {
        if (!object.equals(object1)) {
            throwRuntimeException(errorMessage, show);
        }
    }

    public static void checkTransferBalance(BigDecimal balance, BigDecimal transferAmount, String errorMessage, Boolean show) {
        if (balance.subtract(transferAmount).signum() < 0) {
            throwNumberFormatException(errorMessage, show);
        }
    }

    public static void checkTransferAmount(BigDecimal transferAmount, BigDecimal transferAmountCheck, String errorMessage, Boolean show) {
        if (transferAmount.subtract(transferAmountCheck).abs().compareTo(new BigDecimal(0.5)) == 1) {
            throwNumberFormatException(errorMessage, show);
        }
    }

    public static void checkBiggerThan500MRP(BigDecimal amountEkzt, Double oneMRP, String errorMessage, Boolean show) {
        if (amountEkzt.compareTo(new BigDecimal(oneMRP * 500)) > 0) {
            throwNumberFormatException(errorMessage, show);
        }
    }


    public static void checkStringsEqual(String str1, String str2, String errorMessage, Boolean show) {
        if (!str1.equals(str2)) {
            throwRuntimeException(errorMessage, show);
        }
    }

    public static void checkStringsNotEqual(String str1, String str2, String errorMessage, Boolean show) {
        if (str1.equals(str2)) {
            throwRuntimeException(errorMessage, show);
        }
    }

    public static void checkStringLength(String str, Integer length, String errorMessage, Boolean show) {
        if (str.length() != length) {
            throwRuntimeException(errorMessage, show);
        }
    }

    public static void checkStringContains(String str1, String str2, String errorMessage, Boolean show) {
        if (!str1.contains(str2)) {
            throwRuntimeException(errorMessage, show);
        }
    }


    public static void checkIsUsernameValid(String username, String errorMessage, Boolean show) {
        if (!MaskValidator.isUsernameValid(username)) {
            throwRuntimeException(errorMessage, show);
        }
    }

    public static void checkIsPasswordValid(String password, String errorMessage, Boolean show) {
        if (!MaskValidator.isPasswordValid(password)) {
            throwRuntimeException(errorMessage, show);
        }
    }

    public static void checkIsEmailValid(String email, String errorMessage, Boolean show) {
        if (!MaskValidator.isEmailValid(email)) {
            throwRuntimeException(errorMessage, show);
        }
    }

    public static void checkIsPhoneValid(String phone, String errorMessage, Boolean show) {
        if (!MaskValidator.isPhoneValid(phone)) {
            throwRuntimeException(errorMessage, show);
        }
    }

    public static void checkIsIdnValid(String idn, String errorMessage, Boolean show) {
        if (!MaskValidator.isIdnValid(idn)) {
            throwRuntimeException(errorMessage, show);
        }
    }

    public static void checkIsIdnValidExpression(String idn, String errorMessage, Boolean show) {
        if (!MaskValidator.isIdnValidExpression(idn)) {
            throwRuntimeException(errorMessage, show);
        }
    }

    public static void checkIsCodewordValid(String codeword, String errorMessage, Boolean show) {
        if (!MaskValidator.isCodewordValid(codeword)) {
            throwRuntimeException(errorMessage, show);
        }
    }

    public static void checkIfBirthDateValid(Date birth, String errorMessage, Boolean show) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -18);
        if (birth.getTime() > cal.getTime().getTime()) {
            throwDateTimeException(errorMessage, show);
        }
    }

    public static void checkIfDateAfterNow(Date date, String errorMessage, Boolean show) {
        if (date.after(new Date())) {
            throwDateTimeException(errorMessage, show);
        }
    }

    public static void checkIfDateBeforeNow(Date date, String errorMessage, Boolean show) {
        if (date.before(new Date())) {
            throwDateTimeException(errorMessage, show);
        }
    }

    public static void checkIfDateBeforeNowHours(Date date, Integer hours, String errorMessage, Boolean show) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR, -hours);
        if (date.before(cal.getTime())) {
            throwDateTimeException(errorMessage, show);
        }
    }

    public static void checkIfDateBeforeNowMinutes(Date date, Integer minutes, String errorMessage, Boolean show) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, -minutes);
        if (date.before(cal.getTime())) {
            throwDateTimeException(errorMessage, show);
        }
    }

    public static void checkIfDateAfterNowMinutes(Date date, Integer minutes, String errorMessage, Boolean show) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, minutes);
        if (date.after(cal.getTime())) {
            throwDateTimeException(errorMessage, show);
        }
    }

    public static void checkIfDateAfterDate2(Date date, Date date2, String errorMessage, Boolean show) {
        if (date.after(date2)) {
            throwDateTimeException(errorMessage, show);
        }
    }

    public static void checkIfBirthDateIdnValid(Date date, String idn, String errorMessage, Boolean show) {
        SimpleDateFormat format = new SimpleDateFormat(Constants.DATE_FORMAT_IDN);
        if (!format.format(date).substring(2).equals(idn.substring(0, 6))) {
            throwDateTimeException(errorMessage, show);
        }
    }

    public static void checkAmountBiggerThanZero(BigDecimal amount, String errorMessage, Boolean show) {
        if (amount.signum() < 1) {
            throwNumberFormatException(errorMessage, show);
        }
    }

    public static void checkAmountLessThanZero(BigDecimal amount, BigDecimal amount2, String errorMessage, Boolean show) {
        if (amount.compareTo(amount2) < 1) {
            throwNumberFormatException(errorMessage, show);
        }
    }

    public static void checkAmountLessAmount2(BigDecimal amount, BigDecimal amount2, String errorMessage, Boolean show) {
        if (amount.compareTo(amount2) < 0) {
            throwNumberFormatException(errorMessage, show);
        }
    }

    public static boolean isBeforeThanMinute(Date date, Integer minute) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, -minute);
        if (date.before(cal.getTime())) {
            return true;
        }
        return false;
    }

}