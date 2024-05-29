package com.aniwhere.global.config.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("logging")
public class LoggingUtil {

    public Logger getLogger(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }

    public void logInfo(Class<?> clazz, String message) {
        getLogger(clazz).info(message);
    }

    public void logError(Class<?> clazz, String message, Throwable t) {
        getLogger(clazz).error(message, t);
    }

    public void logDebug(Class<?> clazz, String message) {
        getLogger(clazz).debug(message);
    }

    public void logWarn(Class<?> clazz, String message) {
        getLogger(clazz).warn(message);
    }
}
