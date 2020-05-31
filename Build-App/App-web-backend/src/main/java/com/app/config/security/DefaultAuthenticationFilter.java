package com.app.config.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DefaultAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final DefaultAuthenticationFailureHandler failureHandler;
    private final DefaultAuthenticationSuccessHandler successHandler;

    public DefaultAuthenticationFilter(AuthenticationManager authenticationManager,
                                       DefaultAuthenticationFailureHandler failureHandler,
                                       DefaultAuthenticationSuccessHandler successHandler) {
        this.authenticationManager = authenticationManager;
        this.failureHandler = failureHandler;
        this.successHandler = successHandler;
//        setLogin();
        setFilterProcessesUrl("/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = obtainUsername(request);
        String password = obtainPassword(request);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        return this.authenticationManager.authenticate(authenticationToken);
//        return super.attemptAuthentication(request, response);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        this.successHandler.onAuthenticationSuccess(request, response, authResult);
//        super.successfulAuthentication(request, response, chain, authResult);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        this.failureHandler.onAuthenticationFailure(request, response, failed);
//        super.unsuccessfulAuthentication(request, response, failed);
    }

//    private void setLogin() {
//        setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/login", "POST"));
//    }
}
