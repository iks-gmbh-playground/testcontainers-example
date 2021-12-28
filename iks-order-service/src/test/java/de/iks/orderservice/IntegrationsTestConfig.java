package de.iks.orderservice;

import de.iks.orderservice.testcontainers.PostgresDB;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;

@Configuration
@Profile("ittest")
public class IntegrationsTestConfig implements WebMvcConfigurer {

    static final PostgresDB postgres;

    static {
        postgres = new PostgresDB();
        if (!postgres.isRunning()) {
            postgres.withUsername("username")
                    .withPassword("password")
                    .withDatabaseName("orderservice")
                    .start();
        }
    }

    @Bean
    @Primary
    public DataSource datasource() {
        var driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setPassword(postgres.getPassword());
        driverManagerDataSource.setUsername(postgres.getUsername());
        driverManagerDataSource.setUrl(postgres.getJdbcUrl());
        driverManagerDataSource.setDriverClassName(postgres.getDriverClassName());
        return driverManagerDataSource;
    }
}