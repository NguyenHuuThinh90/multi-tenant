package com.app.service;

import java.util.Locale;

/**
 * @author thinhnguyen
 */
public interface MessageService {
    String get(String message, String locale);
    String get(String message, Locale locale);
    String get(String message);
    String get(String message, Object... params);
}
