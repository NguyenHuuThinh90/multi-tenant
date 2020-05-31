package com.app.config.security;

import com.app.dto.Ro;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DefaultAuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Check request filter have data
        // if ok set    Security holder context
        // next filter
        Map<String, Ro> map = new HashMap<>();
        map.put("1", new Ro("t1"));
        map.put("2", new Ro("NEW"));
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("thinh", map, null));
        filterChain.doFilter(request, response);
    }
}
