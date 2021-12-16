package com.leverx.workload.config;

import java.util.Properties;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@EnableJpaRepositories(basePackages = "com.leverx.workload.repository")
@EnableTransactionManagement
@ComponentScan(basePackages = "com.leverx")
@PropertySource("classpath:app.properties")
public class ApplicationConfig {

  @Value("${jdbc.driverClass}")
  private String driverClass;

  @Value("${jdbc.url}")
  private String jdbcUrl;

  @Value("${jdbc.username}")
  private String jdbcUsername;

  @Value("${jdbc.password}")
  private String jdbcPassword;

  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
    JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
    em.setDataSource(dataSource());
    em.setPackagesToScan("com.leverx.workload.entity");
    em.setJpaVendorAdapter(vendorAdapter);
    em.setJpaProperties(additionalJpaProperties());
    return em;
  }

  Properties additionalJpaProperties() {
    Properties properties = new Properties();
    properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQL10Dialect");
    properties.setProperty("hibernate.show_sql", "true");
    return properties;
  }

  @Bean
  public DataSource dataSource() {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName(driverClass);
    dataSource.setUrl(jdbcUrl);
    dataSource.setUsername(jdbcUsername);
    dataSource.setPassword(jdbcPassword);
    return dataSource;
  }

  @Bean
  public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
    JpaTransactionManager transactionManager = new JpaTransactionManager();
    transactionManager.setEntityManagerFactory(entityManagerFactory);
    return transactionManager;
  }

  @Bean
  public SpringLiquibase liquibase() {
    SpringLiquibase liquibase = new SpringLiquibase();
    liquibase.setChangeLog("classpath:db/changelog/db-changelog-master.xml");
    liquibase.setDataSource(dataSource());
    return liquibase;
  }
}
