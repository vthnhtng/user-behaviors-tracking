package org.pinebell.app.tracking.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

@Configuration
public class ClickHouseConfig {

    private static final Logger logger = LoggerFactory.getLogger(ClickHouseConfig.class);

    @Value("${clickhouse.datasource.url}")
    private String url;

    @Value("${clickhouse.datasource.username}")
    private String username;

    @Value("${clickhouse.datasource.password:}")
    private String password;

    @Value("${clickhouse.datasource.driver-class-name}")
    private String driverClassName;

    @Value("${clickhouse.datasource.pool-size:10}")
    private int poolSize;

    @Value("${clickhouse.datasource.min-idle:2}")
    private int minIdle;

    @Value("${clickhouse.datasource.connection-timeout:30000}")
    private long connectionTimeout;

    @Value("${clickhouse.datasource.idle-timeout:600000}")
    private long idleTimeout;

    @Value("${clickhouse.datasource.schema-location:}")
    private String schemaLocation;

    @Bean("clickHouseDataSource")
    public DataSource clickHouseDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName(driverClassName);
        config.setMaximumPoolSize(poolSize);
        config.setMinimumIdle(minIdle);
        config.setConnectionTimeout(connectionTimeout);
        config.setIdleTimeout(idleTimeout);
        config.setPoolName("clickhouse-pool");

        HikariDataSource dataSource = new HikariDataSource(config);
        initializeSchema(dataSource);
        return dataSource;
    }

    @Bean("clickHouseJdbcTemplate")
    public JdbcTemplate clickHouseJdbcTemplate() {
        return new JdbcTemplate(clickHouseDataSource());
    }

    @Bean("clickHouseNamedJdbcTemplate")
    public NamedParameterJdbcTemplate clickHouseNamedJdbcTemplate() {
        return new NamedParameterJdbcTemplate(clickHouseDataSource());
    }

    private void initializeSchema(DataSource dataSource) {
        if (schemaLocation == null || schemaLocation.isBlank()) {
            logger.info("ClickHouse schema initialization skipped: no schema-location configured");
            return;
        }

        try {
            ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
            populator.addScript(new ClassPathResource(schemaLocation));
            populator.setSeparator(";");
            populator.setContinueOnError(true);
            populator.execute(dataSource);
            logger.info("ClickHouse schema initialized from: {}", schemaLocation);
        } catch (Exception e) {
            logger.warn("ClickHouse schema initialization skipped: {}", e.getMessage());
        }
    }
}
