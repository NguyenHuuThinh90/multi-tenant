package com.app.config.tenant;

import com.app.config.tenant.master.MasterDataSourceProperties;
import com.app.config.tenant.master.repository.MasterTenantRepository;
import org.hibernate.MultiTenancyStrategy;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = { "com.app.entity", "com.app.repository" },
        entityManagerFactoryRef = "tenantEntityManagerFactory",
        transactionManagerRef = "tenantTransactionManager")
public class TenantDatabaseConfig {

    @Autowired
    private MasterDataSourceProperties masterProperties;
    @Autowired
    private MasterTenantRepository masterTenantRepository;

    @Bean(name = "tenantJpaVendorAdapter")
    public JpaVendorAdapter jpaVendorAdapter() {
        return new HibernateJpaVendorAdapter();
    }

    @Bean(name = "multiTenantConnectionProvider")
    @DependsOn(value = "masterEntityManagerFactory")
    public MultiTenantConnectionProvider multiTenantConnectionProvider() {
        return new MultiTenantConnectionProviderImpl();
    }

    @Bean(name = "currentTenantIdentifierResolver")
    @DependsOn(value = "masterEntityManagerFactory")
    public CurrentTenantIdentifierResolver currentTenantIdentifierResolver() {
        return new CurrentTenantIdentifierResolverImpl();
    }

    @Bean(name = "tenantEntityManagerFactory")
    @DependsOn(value = "multiTenantConnectionProvider")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            @Qualifier("multiTenantConnectionProvider") MultiTenantConnectionProvider connectionProvider,
            @Qualifier("currentTenantIdentifierResolver") CurrentTenantIdentifierResolver tenantResolver) {

        LocalContainerEntityManagerFactoryBean emFactoryBean = new LocalContainerEntityManagerFactoryBean();
        //All tenant related entities, repositories and service classes must be scanned
        emFactoryBean.setPackagesToScan(new String[] { "com.app.entity", "com.app.repository" });
        emFactoryBean.setJpaVendorAdapter(jpaVendorAdapter());
        emFactoryBean.setPersistenceUnitName("tenant-database-persistence-unit");
        //Setting properties
        Properties properties = new Properties();
        properties.put(org.hibernate.cfg.Environment.MULTI_TENANT, MultiTenancyStrategy.DATABASE);
        properties.put(org.hibernate.cfg.Environment.MULTI_TENANT_CONNECTION_PROVIDER, connectionProvider);
        properties.put(org.hibernate.cfg.Environment.MULTI_TENANT_IDENTIFIER_RESOLVER, tenantResolver);
        TenantConfig.hibernateProperties(properties, masterProperties);
        emFactoryBean.setJpaProperties(properties);
        return emFactoryBean;
    }

    @Bean(name = "tenantTransactionManager")
    @DependsOn(value = "tenantEntityManagerFactory")
    public JpaTransactionManager transactionManager(@Qualifier("tenantEntityManagerFactory") EntityManagerFactory tenantEntityManager) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(tenantEntityManager);
        return transactionManager;
    }
}
