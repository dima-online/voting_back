package kz.bsbnb.common.consts;

/**
 * Created by Olzhas.Pazyldayev on 22.08.2017.
 */
public enum Locale {
    kk,
    ru,
    en;

    public static Locale getLocale(String locale) {
        return valueOf(locale);
    }

}
