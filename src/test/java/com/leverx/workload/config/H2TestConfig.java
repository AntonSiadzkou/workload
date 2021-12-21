package com.leverx.workload.config;

import java.util.Properties;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
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
@PropertySource("classpath:test.properties")
@AllArgsConstructor
public class H2TestConfig {
  private final Environment env;

  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
    JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
    em.setDataSource(dataSource());
    em.setPackagesToScan(env.getRequiredProperty("scan.package"));
    em.setJpaVendorAdapter(vendorAdapter);
    em.setJpaProperties(additionalJpaProperties());
    return em;
  }

  Properties additionalJpaProperties() {
    Properties properties = new Properties();
    properties.setProperty("hibernate.dialect", env.getRequiredProperty("jpa.hibernate.dialect"));
    properties.setProperty("hibernate.show_sql", env.getRequiredProperty("jpa.hibernate.show_sql"));
    return properties;
  }

  @Bean
  public DataSource dataSource() {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName(env.getRequiredProperty("jdbc.driverClass"));
    dataSource.setUrl(env.getRequiredProperty("jdbc.url"));
    dataSource.setUsername(env.getRequiredProperty("jdbc.username"));
    dataSource.setPassword(env.getRequiredProperty("jdbc.password"));
    return dataSource;
  }

  @Bean
  public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
    JpaTransactionManager transactionManager = new JpaTransactionManager();
    transactionManager.setEntityManagerFactory(entityManagerFactory);
    return transactionManager;
  }

  @Bean
  public Validator validator() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    return factory.getValidator();
  }
}
