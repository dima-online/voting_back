package kz.bsbnb.util.processor;

import kz.bsbnb.common.consts.Locale;

/**
 * Created by serik.mukashev on 21.11.2017.
 */
public interface MessageProcessor {
    String getMessage(String name);

    String getMessage(String name, Object... args);

    Locale getCurrentLocale();
}
