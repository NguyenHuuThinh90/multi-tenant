package com.app.config;

import com.app.annotation.ApiController;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Configuration
public class RestRequestMappingConfig {

    private final String API_REGEX = "api.*[0-9]";

    @Bean
    public WebMvcRegistrations webMvcRegistrationsHandlerMapping() {
        Pattern pattern = Pattern.compile(API_REGEX);

        return new WebMvcRegistrations() {

            @Override
            public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
                return new RequestMappingHandlerMapping() {

                    @Override
                    protected void registerHandlerMethod(Object handler, Method method, RequestMappingInfo mapping) {
                        Class<?> beanType = method.getDeclaringClass();
                        Matcher m = pattern.matcher(beanType.getCanonicalName());
                        String baseApi = "";
                        if (m.find()) {
                            baseApi = m.group(0).replace(".", "/");
                        }

                        ApiController restApiController = beanType.getAnnotation(ApiController.class);
                        if (restApiController != null) {
                            PatternsRequestCondition apiPattern = new PatternsRequestCondition(baseApi)
                                    .combine(mapping.getPatternsCondition());

                            mapping = new RequestMappingInfo(mapping.getName(), apiPattern,
                                    mapping.getMethodsCondition(), mapping.getParamsCondition(),
                                    mapping.getHeadersCondition(), mapping.getConsumesCondition(),
                                    mapping.getProducesCondition(), mapping.getCustomCondition());
                        }

                        super.registerHandlerMethod(handler, method, mapping);
                    }
                };
            }
        };
    }
}
