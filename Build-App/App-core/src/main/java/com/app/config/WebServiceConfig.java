package com.app.config;

import com.app.config.tenant.TenantFilter;
import com.app.config.tenant.TenantStore;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.target.ThreadLocalTargetSource;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import javax.servlet.Filter;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Configuration
public class WebServiceConfig extends AcceptHeaderLocaleResolver implements WebMvcConfigurer {

    List<Locale> LOCALES = Arrays.asList(new Locale("en"), new Locale("vi"));
    private final String HEADER_ACCEPT = "Accept-Language";
    private final String UTF8 = "UTF-8";
    private final String BASE_NAME = "localization.messages";

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        String headerLang = request.getHeader(HEADER_ACCEPT);
        return headerLang == null || headerLang.isEmpty()
                ? Locale.getDefault()
                : Locale.lookup(Locale.LanguageRange.parse(headerLang), LOCALES);
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource rs = new ReloadableResourceBundleMessageSource();
        rs.setBasename(BASE_NAME);
        rs.setDefaultEncoding(UTF8);
        rs.setUseCodeAsDefaultMessage(true);
        return rs;
    }

    @Bean(destroyMethod = "destroy")
    public ThreadLocalTargetSource threadLocalTenantStore() {
        ThreadLocalTargetSource result = new ThreadLocalTargetSource();
        result.setTargetBeanName("tenantStore");
        return result;
    }

    @Primary
    @Bean(name = "proxiedThreadLocalTargetSource")
    public ProxyFactoryBean proxiedThreadLocalTargetSource(ThreadLocalTargetSource threadLocalTargetSource) {
        ProxyFactoryBean result = new ProxyFactoryBean();
        result.setTargetSource(threadLocalTargetSource);
        return result;
    }

    @Bean(name = "tenantStore")
    @Scope(scopeName = "prototype")
    public TenantStore tenantStore() {
        return new TenantStore();
    }

    @Bean
    public Filter tenantFilter() {
        return new TenantFilter();
    }

    @Bean
    public FilterRegistrationBean tenantFilterRegistration() {
        FilterRegistrationBean result = new FilterRegistrationBean();
        result.setFilter(this.tenantFilter());
        result.setUrlPatterns(Arrays.asList("/*"));
        result.setName("Tenant Store Filter");
        result.setOrder(1);
        return result;
    }
}
