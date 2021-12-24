package com.leverx.workload.config;

import java.util.Collections;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@PropertySource("classpath:app.properties")
@AllArgsConstructor
public class SwaggerConfig {

  private final Environment env;

  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
        .apis(RequestHandlerSelectors.basePackage(env.getProperty("scan.package")))
        .paths(PathSelectors.any()).build();
  }

  private ApiInfo apiInfo() {
    return new ApiInfo(env.getProperty("swagger.info.title"),
        env.getProperty("swagger.info.description"), env.getProperty("swagger.info.version"),
        env.getProperty("swagger.info.termsUrl"),
        new Contact(env.getProperty("swagger.info.contact.name"),
            env.getProperty("swagger.info.contact.url"),
            env.getProperty("swagger.info.contact.mail")),
        env.getProperty("swagger.info.license"), env.getProperty("swagger.info.licenseUrl"),
        Collections.emptyList());
  }
}
