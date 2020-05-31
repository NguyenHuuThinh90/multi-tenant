package com.app.config.tenant;

import com.app.config.tenant.master.MasterDataSourceProperties;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

public class TenantEntityManager {

    private static Map<String, LocalContainerEntityManagerFactoryBean> emMap = new TreeMap<>();

    public static void createEntityManager(String tenantId, DataSource dataSource, MasterDataSourceProperties masterDataSourceProperties) {
        LocalContainerEntityManagerFactoryBean emFactoryBean = new LocalContainerEntityManagerFactoryBean();
        //All tenant related entities, repositories and service classes must be scanned
        emFactoryBean.setDataSource(dataSource);
        emFactoryBean.setPackagesToScan(new String[] { "com.app.entity", "com.app.repository" });
        emFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        emFactoryBean.setPersistenceUnitName(tenantId);
        //Setting properties
        Properties properties = new Properties();
        TenantConfig.hibernateProperties(properties, masterDataSourceProperties);
        emFactoryBean.setJpaProperties(properties);
        emFactoryBean.afterPropertiesSet();
        emMap.put(tenantId, emFactoryBean);
    }

    public static EntityManager getEntityManager(String tenantId) {
        return emMap.get(tenantId).getObject().createEntityManager();
    }

    public static EntityManager getDefaultEntityManager() {
        return emMap.values().iterator().next().getObject().createEntityManager();
    }
}
