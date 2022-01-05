package com.leverx.workload.config;

import javax.sql.DataSource;
import liquibase.integration.spring.SpringLiquibase;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("classpath:test.properties")
@AllArgsConstructor
public class LiquibaseTestConfig {
  private final DataSource dataSource;
  private final Environment env;

  @Bean
  public SpringLiquibase liquibase() {
    SpringLiquibase liquibase = new SpringLiquibase();
    liquibase.setChangeLog(env.getRequiredProperty("liquibase.changelog"));
    liquibase.setDataSource(dataSource);
    return liquibase;
  }
}
