package com.app.config.tenant;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

public class CurrentTenantIdentifierResolverImpl implements CurrentTenantIdentifierResolver {

    private static final String DEFAULT_TENANT_ID = "app";

    @Autowired
    private TenantStore tenantStore;

    @Override
    public String resolveCurrentTenantIdentifier() {
        String tenant = tenantStore.getTenantId();
        System.out.println("TENANT:" + tenant);
        return !StringUtils.isEmpty(tenant) ? tenant : DEFAULT_TENANT_ID;
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}