package com.leverx.workload.config;

import java.util.Properties;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = "com.leverx.workload")
@EnableTransactionManagement
@PropertySource("classpath:app.properties")
public class JpaConfig {
  @Value("${scan.package}")
  private String scanPackage;

  @Value("${jdbc.driverClass}")
  private String driverClass;

  @Value("${jdbc.url}")
  private String jdbcUrl;

  @Value("${jdbc.username}")
  private String jdbcUsername;

  @Value("${jdbc.password}")
  private String jdbcPassword;

  @Value("${jpa.hibernate.dialect}")
  private String hibernateDialect;

  @Value("${jpa.hibernate.show_sql}")
  private String hibernateShowSql;

  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
    JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
    em.setDataSource(dataSource());
    em.setPackagesToScan(scanPackage);
    em.setJpaVendorAdapter(vendorAdapter);
    em.setJpaProperties(additionalJpaProperties());
    return em;
  }

  // todo: change it and delete it
  Properties additionalJpaProperties() {
    Properties properties = new Properties();
    properties.setProperty("hibernate.dialect", hibernateDialect);
    properties.setProperty("hibernate.show_sql", hibernateShowSql);
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
}
