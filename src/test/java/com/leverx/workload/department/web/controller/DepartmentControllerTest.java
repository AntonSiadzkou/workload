package com.leverx.workload.department.web.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.leverx.workload.config.ApplicationConfig;
import com.leverx.workload.config.H2TestConfig;
import com.leverx.workload.config.MapperConfig;
import com.leverx.workload.config.WebConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
@ContextConfiguration(
    classes = {ApplicationConfig.class, MapperConfig.class, WebConfig.class, H2TestConfig.class})
@WebAppConfiguration
@Sql("classpath:test-data.sql")
class DepartmentControllerTest {

  public static final String DEPARTMENT_ENDPOINT = "/departments";
  private final WebApplicationContext webAppContext;
  private MockMvc mvc;

  @Autowired
  public DepartmentControllerTest(WebApplicationContext webAppContext) {
    this.webAppContext = webAppContext;
  }

  @BeforeEach
  void setUp() {
    this.mvc = MockMvcBuilders.webAppContextSetup(this.webAppContext).build();
  }

  @Test
  void getAllDepartments_AllGood_ResponseReceived() throws Exception {
    mvc.perform(get(DEPARTMENT_ENDPOINT)).andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$[0].title").value("HR")).andExpect(jsonPath("$[2].title").value("IT"))
        .andExpect(jsonPath("$.length()").value("3"));
  }

  @Test
  void getDepartmentById_DepartmentExists_ResponseOk() throws Exception {
    mvc.perform(get(DEPARTMENT_ENDPOINT + "/{id}", 2)).andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.title").value("PR"));
  }

  @Test
  void getUserById_UserNotExist_Exception() throws Exception {
    mvc.perform(get(DEPARTMENT_ENDPOINT + "/{id}", 9999)).andExpect(status().isNotFound())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.statusCode").value("404"))
        .andExpect(jsonPath("$.message").value("User with id=9999 not found"));
  }
}
