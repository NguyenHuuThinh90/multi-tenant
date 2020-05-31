package com.app.config.tenant;

import com.app.config.tenant.master.MasterDataSourceProperties;
import com.app.config.tenant.master.entity.MasterTenant;
import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.cfg.AvailableSettings;

import javax.sql.DataSource;
import java.util.Map;

public class TenantConfig {

    public static DataSource defaultDatasource(MasterDataSourceProperties properties) {
        return initDataSource(null, properties);
    }

    public static DataSource createDatasource(MasterTenant masterTenant, MasterDataSourceProperties properties) {
        return initDataSource(masterTenant, properties);
    }

    public static void hibernateProperties(Map<Object, Object> properties, MasterDataSourceProperties masterProperties) {
        properties.put(AvailableSettings.PHYSICAL_NAMING_STRATEGY, "org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy");
        properties.put(AvailableSettings.IMPLICIT_NAMING_STRATEGY, "org.hibernate.boot.model.naming.ImplicitNamingStrategyLegacyHbmImpl");
        properties.put(org.hibernate.cfg.Environment.DIALECT, masterProperties.getDialect());
        properties.put(org.hibernate.cfg.Environment.SHOW_SQL, false);
        properties.put(org.hibernate.cfg.Environment.FORMAT_SQL, true);
        properties.put(org.hibernate.cfg.Environment.HBM2DDL_AUTO, masterProperties.getDdl());
        properties.put("spring.jpa.open-in-view", false);
        properties.put(AvailableSettings.NON_CONTEXTUAL_LOB_CREATION, true);
    }

    private static DataSource initDataSource(MasterTenant masterTenant, MasterDataSourceProperties properties) {

        HikariDataSource ds = new HikariDataSource();
        if (masterTenant == null) {
            ds.setUsername(properties.getUsername());
            ds.setPassword(properties.getPassword());
            ds.setJdbcUrl(properties.getUrl());
            ds.setDriverClassName(properties.getDriver());
            ds.setPoolName(properties.getPoolName());
        } else {
            ds.setUsername(masterTenant.getUsername());
            ds.setPassword(masterTenant.getPassword());
            ds.setJdbcUrl(masterTenant.getUrl());
            ds.setDriverClassName(masterTenant.getDriver());
            ds.setPoolName(masterTenant.getPoolName());

        }
        ds.setMaximumPoolSize(properties.getMaxPoolSize());
        // Minimum number of idle connections in the pool
        ds.setMinimumIdle(properties.getMinIdle());
        // Maximum waiting time for a connection from the pool
        ds.setConnectionTimeout(properties.getConnectionTimeout());
        // Maximum time that a connection is allowed to sit idle in the pool
        ds.setIdleTimeout(properties.getIdleTimeout());
        return ds;
    }
}
