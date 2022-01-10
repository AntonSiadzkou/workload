package com.leverx.workload.userproject.web.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.leverx.workload.config.ApplicationConfig;
import com.leverx.workload.config.H2TestConfig;
import com.leverx.workload.config.MapperConfig;
import com.leverx.workload.config.WebConfig;
import com.leverx.workload.userproject.web.dto.request.UserProjectBodyParams;
import com.leverx.workload.util.GeneralUtils;
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

  @Test
  void getAllAvailableUsers_AllGood_ResponseReceived() throws Exception {
    mvc.perform(get("/projects/users/available")).andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.users.length()").value("1"));
  }

  @Test
  void saveUserProject_AllValid_Created() throws Exception {
    mvc.perform(put("/projects/users").contentType(MediaType.APPLICATION_JSON)
        .content(GeneralUtils.asJsonString(createUserProjectBodyParamsSample())))
        .andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  void deleteUserProject_UserProjectIdValid_UserProjectDeleted() throws Exception {
    mvc.perform(delete("/projects/{id}/users/{id}", 1, 1).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());
  }

  @Test
  void deleteUserProject_UserProjectNotExist_Exception() throws Exception {
    mvc.perform(
        delete("/projects/{id}/users/{id}", 999, 999).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.statusCode").value("404")).andExpect(jsonPath("$.message")
            .value("Unable to delete user with project.There is no such pair of identifiers."));
  }

  private UserProjectBodyParams createUserProjectBodyParamsSample() {
    UserProjectBodyParams params = new UserProjectBodyParams();
    params.setUserId(3L);
    params.setProjectId(3L);
    params.setAssignDate("2022-04-05");
    params.setAssignDate("2022-11-28");
    return params;
  }
}
