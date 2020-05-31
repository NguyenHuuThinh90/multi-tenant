package com.app.config;

import com.app.config.security.*;
import com.app.config.tenant.TenantFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

@EnableWebSecurity
public class WebApplicationSecurity extends WebSecurityConfigurerAdapter {

    @Resource
    private DefaultUserDetailService userDetailService;
    @Resource
    private TenantFilter tenantFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .authorizeRequests()
                .antMatchers("/login").permitAll()
                .anyRequest().permitAll()
                .and()
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint()).accessDeniedHandler(accessDeniedHandler())
                .and()
                .addFilterBefore(tenantFilter, DefaultAuthenticationFilter.class)
                .addFilter(new DefaultAuthenticationFilter(authenticationManager(), failureHandler(), successHandler()))
                .addFilterAfter(new DefaultAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().headers().xssProtection();

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DefaultAuthenticationFailureHandler failureHandler() {
        return new DefaultAuthenticationFailureHandler();
    }

    @Bean
    public DefaultAuthenticationSuccessHandler successHandler() {
        return new DefaultAuthenticationSuccessHandler();
    }

    @Bean
    public DefaultAuthenticationEntryPoint authenticationEntryPoint() {
        return new DefaultAuthenticationEntryPoint();
    }

    @Bean
    public DefaultAccessDeniedHandler accessDeniedHandler() {
        return new DefaultAccessDeniedHandler();
    }


}
