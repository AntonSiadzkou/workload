package com.leverx.workload.userproject.web.controller;

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
class UserProjectControllerTest {

  private final WebApplicationContext webAppContext;
  private MockMvc mvc;

  @Autowired
  public UserProjectControllerTest(WebApplicationContext webAppContext) {
    this.webAppContext = webAppContext;
  }

  @BeforeEach
  void setUp() {
    this.mvc = MockMvcBuilders.webAppContextSetup(this.webAppContext).build();
  }

  @Test
  void getAllUserProjectsByUserId_AllGood_ResponseReceived() throws Exception {
    mvc.perform(get("/users/{id}/projects", 4)).andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.user.firstName").value("Maria"))
        .andExpect(jsonPath("$.projects.length()").value("2"));
  }

  @Test
  void getAllCurrentUserProjectsByUserId_AllGood_ResponseReceived() throws Exception {
    mvc.perform(get("/users/{id}/projects/current", 4)).andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.user.firstName").value("Maria"))
        .andExpect(jsonPath("$.projects.length()").value("1"));
  }

  @Test
  void getAllUserProjectsByProjectId_AllGood_ResponseReceived() throws Exception {
    mvc.perform(get("/projects/{id}/users", 4)).andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.project.name").value("java project"))
        .andExpect(jsonPath("$.users.length()").value("4"));
  }

  @Test
  void getAllCurrentUserProjectsByProjectId_AllGood_ResponseReceived() throws Exception {
    mvc.perform(get("/projects/{id}/users/current", 4)).andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.project.name").value("java project"))
        .andExpect(jsonPath("$.users.length()").value("3"));
  }
}
