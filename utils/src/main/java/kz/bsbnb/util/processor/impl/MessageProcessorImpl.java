package kz.bsbnb.util.processor.impl;

import kz.bsbnb.util.processor.MessageProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.Locale;

/**
 * Created by serik.mukashev on 21.11.2017.
 */
@Service
public class MessageProcessorImpl implements MessageProcessor {

    @Autowired
    MessageSource messageSource;

    @Override
    public String getMessage(String name) {
        Locale locale = LocaleContextHolder.getLocale();
        if (locale.equals(Locale.forLanguageTag("kz"))) {
            return messageSource.getMessage(name, null, locale);
        } else {
            return messageSource.getMessage(name, null, Locale.forLanguageTag("RU"));
        }
    }

    @Override
    public String getMessage(String name, Object... args) {
        Locale locale = LocaleContextHolder.getLocale();
        if (locale.equals(Locale.forLanguageTag("kz"))) {
            return messageSource.getMessage(name, args, locale);
        } else {
            return messageSource.getMessage(name, args, Locale.forLanguageTag("RU"));
        }
    }
}
