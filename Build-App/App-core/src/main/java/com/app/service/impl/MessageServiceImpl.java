package com.app.service.impl;

import com.app.service.MessageService;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Locale;

/**
 * @author thinhnguyen
 */
@Service
public class MessageServiceImpl implements MessageService {

    @Resource
    private ReloadableResourceBundleMessageSource source;

    /**
     * get message from locale properties
     * @param message message key
     * @return String
     */
    @Override
    public String get(String message) {
        Locale locale = LocaleContextHolder.getLocale();
        return source.getMessage(message, null, locale);
    }

    /**
     * get message from locale properties
     * @param message message key
     * @param locale string(vi, en)
     * @return String
     */
    @Override
    public String get(String message, String locale) {
        Locale loc = new Locale(locale);
        return source.getMessage(message, null, loc);
    }

    /**
     * get message from locale properties
     * @param message message key
     * @param locale locale
     * @return String
     */
    @Override
    public String get(String message, Locale locale) {
        return source.getMessage(message, null, locale);
    }

    /**
     * get message from locale properties
     * @param message
     * @param params
     * @return String
     */
    @Override
    public String get(String message, Object... params) {
        Locale locale = LocaleContextHolder.getLocale();
        return source.getMessage(message, params, locale);
    }
}
