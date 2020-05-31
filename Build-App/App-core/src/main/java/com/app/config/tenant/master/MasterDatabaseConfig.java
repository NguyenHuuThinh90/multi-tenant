package com.app.config.tenant.master;

import com.app.config.tenant.TenantConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.*;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@PropertySource(value = "classpath:config/${spring.profiles.active}/database.properties")
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = { "com.app.config.tenant.master.entity",
        "com.app.config.tenant.master.repository" },
        entityManagerFactoryRef = "masterEntityManagerFactory",
        transactionManagerRef = "masterTransactionManager")
public class MasterDatabaseConfig {

    @Resource
    private MasterDataSourceProperties masterProperties;

    @Bean(name = "masterDataSource")
    public DataSource masterDataSource() {
        DataSource ds = TenantConfig.defaultDatasource(masterProperties);
        return ds;
    }

    @Primary
    @Bean(name = "masterEntityManagerFactory")
    @DependsOn(value = "masterDataSource")
    public LocalContainerEntityManagerFactoryBean masterEntityManagerFactory(@Qualifier("masterDataSource") DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();

        // Set the master data source
        em.setDataSource(dataSource);
        em.setPackagesToScan(new String[] { "com.app.config.tenant.master.entity",
                "com.app.config.tenant.master.repository"});
        em.setPersistenceUnitName("master-database-persistence-unit");

        // Setting Hibernate as the JPA provider
        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        // Set the hibernate properties
        Properties properties = new Properties();
        TenantConfig.hibernateProperties(properties, masterProperties);
        em.setJpaProperties(properties);
        return em;
    }

    @Bean(name = "masterTransactionManager")
    @DependsOn(value = "masterEntityManagerFactory")
    public JpaTransactionManager masterTransactionManager(
            @Qualifier("masterEntityManagerFactory") EntityManagerFactory emf) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);
        return transactionManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }
}
