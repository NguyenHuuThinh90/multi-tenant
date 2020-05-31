package com.app.config.tenant;


public class TenantStore {
    private String tenantId;

    public void clear() {
        this.tenantId = null;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
}