package com.soccer.ws.configuration;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Objects;

@Configuration
@PropertySource({"classpath:application-realdb.properties"})
@EnableJpaRepositories(
        basePackages = {"com.soccer.ws.migration.persistence"},
        entityManagerFactoryRef = "newEntityManager",
        transactionManagerRef = "newTransactionManager"
)
public class NewDBConfiguration {
    @Autowired
    private Environment env;

    @Bean
    public LocalContainerEntityManagerFactoryBean newEntityManager() {
        LocalContainerEntityManagerFactoryBean em
                = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(newDataSource());
        em.setPackagesToScan(
                new String[]{"com.soccer.ws.migration.model"});

        HibernateJpaVendorAdapter vendorAdapter
                = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.dialect",
                env.getProperty("hibernate.dialect"));
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Bean
    public DataSource newDataSource() {

        DriverManagerDataSource dataSource
                = new DriverManagerDataSource();
        dataSource.setDriverClassName(
                Objects.requireNonNull(env.getProperty("jdbc.datasource.type")));
        dataSource.setUrl(env.getProperty("jdbc.datasource.new.url"));
        dataSource.setUsername(env.getProperty("jdbc.datasource.new.username"));
        dataSource.setPassword(env.getProperty("jdbc.datasource.new.password"));

        return dataSource;
    }

    @Bean
    public PlatformTransactionManager newTransactionManager() {

        JpaTransactionManager transactionManager
                = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(
                newEntityManager().getObject());
        return transactionManager;
    }

    @Bean
    SpringLiquibase newDBspringLiquibase() {
        SpringLiquibase springLiquibase = new SpringLiquibase();
        springLiquibase.setChangeLog("classpath:migrate/db.changelog-master.xml");
        springLiquibase.setDataSource(newDataSource());
        return springLiquibase;
    }
}
