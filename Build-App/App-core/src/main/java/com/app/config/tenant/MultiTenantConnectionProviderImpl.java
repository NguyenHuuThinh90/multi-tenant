package com.app.config.tenant;

import com.app.config.tenant.master.MasterDataSourceProperties;
import com.app.config.tenant.master.entity.MasterTenant;
import com.app.config.tenant.master.repository.MasterTenantRepository;
import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.*;

@Component
public class MultiTenantConnectionProviderImpl extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl {

    private Map<String, DataSource> dataSourcesApp = new TreeMap<>();
    public static Map<String, LocalContainerEntityManagerFactoryBean> emMap = new TreeMap<>();

    @Autowired
    private MasterTenantRepository masterTenantRepo;
    @Autowired
    private TenantStore tenantStore;
    @Autowired
    private MasterDataSourceProperties masterDataSourceProperties;

    @Override
    protected DataSource selectAnyDataSource() {
        if (dataSourcesApp.isEmpty()) {
            List<MasterTenant> masterTenants = initMasterTenant();
            for (MasterTenant masterTenant : masterTenants) {
                dataSourcesApp.put(masterTenant.getTenantId(), TenantConfig.createDatasource(masterTenant, masterDataSourceProperties));
                // Generate entity manager - temp
                TenantEntityManager.createEntityManager(masterTenant.getTenantId(), dataSourcesApp.get(masterTenant.getTenantId()), masterDataSourceProperties);
            }
        }
        return this.dataSourcesApp.values().iterator().next();
    }

    @Override
    protected DataSource selectDataSource(String tenantIdentifier) {
        tenantIdentifier = initializeTenantIfLost(tenantIdentifier);

        if (!this.dataSourcesApp.containsKey(tenantIdentifier)) {
            List<MasterTenant> masterTenants = initMasterTenant();
            for (MasterTenant masterTenant : masterTenants) {
                dataSourcesApp.put(masterTenant.getTenantId(), TenantConfig.createDatasource(masterTenant, masterDataSourceProperties));
                // Generate entity manager - temp
//                TenantEntityManager.createEntityManager(masterTenant.getTenantId(), dataSourcesApp.get(masterTenant.getTenantId()), masterDataSourceProperties);
            }
        }
        return this.dataSourcesApp.get(tenantIdentifier);
    }

    private String initializeTenantIfLost(String tenantIdentifier) {
        if (tenantIdentifier != tenantStore.getTenantId()) {
            tenantIdentifier = tenantStore.getTenantId();
        }
        return tenantIdentifier;
    }

    private List<MasterTenant> initMasterTenant() {
        List<MasterTenant> masterTenants = new ArrayList<>();
        Iterator<MasterTenant> iterator = masterTenantRepo.findAll().iterator();
        while (iterator.hasNext()) {
            masterTenants.add(iterator.next());
        }
        return masterTenants;
    }
}
