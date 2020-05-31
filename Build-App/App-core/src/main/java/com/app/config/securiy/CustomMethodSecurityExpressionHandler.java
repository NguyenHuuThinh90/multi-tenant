package com.app.config.securiy;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.expression.spel.support.StandardTypeLocator;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;

public class CustomMethodSecurityExpressionHandler extends DefaultMethodSecurityExpressionHandler {

    private AuthenticationTrustResolver trustResolver = new AuthenticationTrustResolverImpl();

    @Override
    protected MethodSecurityExpressionOperations createSecurityExpressionRoot(Authentication authentication, MethodInvocation invocation) {
        DefaultMethodSecurityExpressionRoot root = new DefaultMethodSecurityExpressionRoot(authentication);
        root.setThis(invocation.getThis());
        root.setPermissionEvaluator(getPermissionEvaluator());
        root.setTrustResolver(this.trustResolver);
        root.setRoleHierarchy(getRoleHierarchy());
        root.setDefaultRolePrefix(getDefaultRolePrefix());

        return root;
    }

    @Override
    public StandardEvaluationContext createEvaluationContextInternal(Authentication auth, MethodInvocation mi) {
        final StandardEvaluationContext decoratedStandardEvaluationContext =  super.createEvaluationContextInternal(auth, mi);
        ((StandardTypeLocator) decoratedStandardEvaluationContext.getTypeLocator()).registerImport("com.app.auth");
        return decoratedStandardEvaluationContext;
    }
}
