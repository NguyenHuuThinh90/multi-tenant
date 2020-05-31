package com.app.annotation;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
@Scope(value="request", proxyMode= ScopedProxyMode.TARGET_CLASS)
public @interface Converter {
    @AliasFor(annotation = Component.class)
    String value() default "";
}
