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
        if (locale.equals(Locale.forLanguageTag(kz.bsbnb.common.consts.Locale.kk.name()))) {
            return messageSource.getMessage(name, null, locale);
        } else {
            return messageSource.getMessage(name, null, Locale.forLanguageTag(kz.bsbnb.common.consts.Locale.ru.name()));
        }
    }

    @Override
    public String getMessage(String name, Object... args) {
        Locale locale = LocaleContextHolder.getLocale();
        if (locale.equals(Locale.forLanguageTag(kz.bsbnb.common.consts.Locale.kk.name()))) {
            return messageSource.getMessage(name, args, locale);
        } else {
            return messageSource.getMessage(name, args, Locale.forLanguageTag(kz.bsbnb.common.consts.Locale.ru.name()));
        }
    }

    public kz.bsbnb.common.consts.Locale getCurrentLocale() {
        Locale locale = LocaleContextHolder.getLocale();
        if (locale.equals(Locale.forLanguageTag(kz.bsbnb.common.consts.Locale.kk.name()))) {
            return kz.bsbnb.common.consts.Locale.kk;
        } else if (locale.equals(Locale.forLanguageTag(kz.bsbnb.common.consts.Locale.en.name()))) {
            return kz.bsbnb.common.consts.Locale.en;
        } else {
            return kz.bsbnb.common.consts.Locale.ru;
        }
    }
}
