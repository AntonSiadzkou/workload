package com.leverx.workload.user.web.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leverx.workload.config.ApplicationConfig;
import com.leverx.workload.config.H2TestConfig;
import com.leverx.workload.config.LiquibaseConfig;
import com.leverx.workload.config.MapperConfig;
import com.leverx.workload.config.WebConfig;
import com.leverx.workload.user.web.dto.request.UserBodyParams;
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
@ContextConfiguration(classes = {ApplicationConfig.class, MapperConfig.class, LiquibaseConfig.class,
    WebConfig.class, H2TestConfig.class})
@WebAppConfiguration
@Sql("classpath:test-data.sql")
class UserControllerTest {
  public static final String USER_ENDPOINT = "/users";
  private final WebApplicationContext webAppContext;
  private MockMvc mvc;

  @Autowired
  public UserControllerTest(WebApplicationContext webAppContext) {
    this.webAppContext = webAppContext;
  }

  @BeforeEach
  void setUp() {
    this.mvc = MockMvcBuilders.webAppContextSetup(this.webAppContext).build();
  }

  @Test
  void getAllUsersFilterByName_AllGood_ResponseReceived() throws Exception {
    mvc.perform(get(USER_ENDPOINT + "?firstName=John")).andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$[0].firstName").value("John"))
        .andExpect(jsonPath("$[2].firstName").value("john"))
        .andExpect(jsonPath("$.length()").value("3"));
  }

  @Test
  public void getRequest_Made_ResponseOk() throws Exception {
    mvc.perform(get(USER_ENDPOINT)).andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  void getUserById_UserExists_ResponseOk() throws Exception {
    mvc.perform(get(USER_ENDPOINT + "/{id}", 4)).andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.firstName").value("Artur"))
        .andExpect(jsonPath("$.email").value("mail4@joy.com"));
  }

  @Test
  void getUserById_UserNotExist_Exception() throws Exception {
    mvc.perform(get(USER_ENDPOINT + "/{id}", 9999)).andExpect(status().isNotFound())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.statusCode").value("404"))
        .andExpect(jsonPath("$.message").value("User with id=9999 not found"));
  }

  @Test
  void createUser_UserValid_Created() throws Exception {
    mvc.perform(post(USER_ENDPOINT).contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(createUserBodyParamsSample()))).andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$").value("12"));
  }

  @Test
  void createUser_DuplicatedEmail_Exception() throws Exception {
    UserBodyParams user = new UserBodyParams();
    user.setEmail("mail4@joy.com");
    mvc.perform(
        post(USER_ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(asJsonString(user)))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.statusCode").value("400")).andExpect(jsonPath("$.message")
            .value("Email = mail4@joy.com already exists, email must be unique"));
  }

  @Test
  void updateUser_UserValid_Updated() throws Exception {
    UserBodyParams user = createUserBodyParamsSample();
    mvc.perform(put(USER_ENDPOINT + "/{id}", 4).contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(user))).andExpect(status().isOk());
  }

  @Test
  void updateUser_UserNotExist_Exception() throws Exception {
    mvc.perform(put(USER_ENDPOINT + "/{id}", 100).contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(createUserBodyParamsSample()))).andExpect(status().isNotFound())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.statusCode").value("404"))
        .andExpect(jsonPath("$.message").value("Unable to update user. User doesn't exist."));
  }

  @Test
  void deleteUser_IdValid_UserDeleted() throws Exception {
    mvc.perform(delete(USER_ENDPOINT + "/{id}", 4).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());
  }

  @Test
  void deleteUser_UserNotExist_Exception() throws Exception {
    mvc.perform(delete(USER_ENDPOINT + "/{id}", 999).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.statusCode").value("404"))
        .andExpect(jsonPath("$.message").value("Unable to delete user. User doesn't exist."));
  }

  private UserBodyParams createUserBodyParamsSample() {
    UserBodyParams user = new UserBodyParams();
    user.setFirstName("Jane");
    user.setLastName("Kane");
    user.setEmail("email11@mail.com");
    user.setPassword("pass24AS");
    user.setPosition("lead");
    user.setDepartment("IT");
    user.setRole("user");
    user.setActive(true);
    return user;
  }

  private static String asJsonString(final Object obj) {
    try {
      return new ObjectMapper().writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
