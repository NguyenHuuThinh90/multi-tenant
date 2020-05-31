package com.app.config.securiy;

import com.app.auth.Permission;
import com.app.dto.Ro;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;

public class DefaultMethodSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {

    private Object filterObject;
    private Object returnObject;
    private Object target;

    public DefaultMethodSecurityExpressionRoot(Authentication authentication) {
        super(authentication);
    }

    public boolean access(Long id, Permission... ps) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Map<String, Ro> map = (Map<String, Ro>) authentication.getCredentials();
        String pUser = map.get(id.toString()).getId();
        for (Permission p: ps) {
            if (pUser.equals(p.getValue())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void setFilterObject(Object filterObject) {
        this.filterObject = filterObject;
    }

    @Override
    public Object getFilterObject() {
        return this.filterObject;
    }

    @Override
    public void setReturnObject(Object returnObject) {
        this.returnObject = returnObject;
    }

    @Override
    public Object getReturnObject() {
        return this.returnObject;
    }

    @Override
    public Object getThis() {
        return this.target;
    }

    public void setThis(Object target) {
        this.target = target;
    }
}
