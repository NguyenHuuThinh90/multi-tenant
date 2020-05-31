package com.app.config.tenant;

import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import java.io.IOException;

public class TenantFilter implements Filter {

    @Autowired
    private TenantStore tenantStore;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            this.tenantStore.setTenantId("app");
            chain.doFilter(request, response);
        } finally {
            this.tenantStore.clear();
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }
}
