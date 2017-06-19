package com.soccer.ws.configuration;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

/**
 * Created by u0090265 on 09.06.17.
 */
@Configuration
@Profile("default")
public class LocalDataSourceConfig {
    @Bean
    public DataSource dataSource() {
        // no need shutdown, EmbeddedDatabaseFactoryBean will take care of this
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        return builder
                .setType(EmbeddedDatabaseType.H2) //.H2 or .DERBY
                .build();
    }

    @Bean
    public SpringLiquibase liquibase() {
        final SpringLiquibase springLiquibase = new SpringLiquibase();
        springLiquibase.setDataSource(dataSource());
        springLiquibase.setChangeLog("classpath:db/changelog/db.changelog-master.xml");
        return springLiquibase;
    }

    /**
     @Bean public ResourceDatabasePopulator databasePopulator() {
     ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
     populator.setSqlScriptEncoding("UTF-8");
     populator.addScript(new ClassPathResource("db/data.sql"));
     return populator;
     }
     **/
}
