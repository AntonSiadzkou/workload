package com.leverx.workload.controller;

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
import com.leverx.workload.controller.request.UserRequest;
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
  void getAllUsersFilterByName() throws Exception {
    mvc.perform(get(USER_ENDPOINT + "?firstName=John")).andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$[0].firstName").value("John"))
        .andExpect(jsonPath("$[2].firstName").value("john"))
        .andExpect(jsonPath("$.length()").value("3"));
  }

  @Test
  public void shouldReturnOkIfGetRequest() throws Exception {
    mvc.perform(get(USER_ENDPOINT)).andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  void getUserById() throws Exception {
    mvc.perform(get(USER_ENDPOINT + "/{id}", 4)).andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.firstName").value("Artur"))
        .andExpect(jsonPath("$.email").value("mail4@joy.com"));
  }

  @Test
  void getUserByIdException() throws Exception {
    mvc.perform(get(USER_ENDPOINT + "/{id}", 9999)).andExpect(status().isNotFound())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.statusCode").value("404"))
        .andExpect(jsonPath("$.message").value("User with id=9999 not found"));
  }

  @Test
  void createUser() throws Exception {
    mvc.perform(post(USER_ENDPOINT).contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(new UserRequest("Jane", "Kane", "email11@mail.com", "pass24AS",
            "lead", "IT", "user", true))))
        .andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.firstName").value("Jane"))
        .andExpect(jsonPath("$.email").value("email11@mail.com"));
  }

  @Test
  void createUserEmailException() throws Exception {
    mvc.perform(post(USER_ENDPOINT).contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(new UserRequest("Lowry", "Wayne", "mail4@joy.com", "pass34Wq",
            "junior", "IT", "user", false))))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.statusCode").value("400")).andExpect(jsonPath("$.message")
            .value("Email = mail4@joy.com already exists, email must be unique"));
  }

  @Test
  void updateUser() throws Exception {
    mvc.perform(put(USER_ENDPOINT + "/{id}", 4).contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(new UserRequest("NewUser", "Kane", "newmail@mail.com", "pass24AS",
            "lead", "IT", "user", true))))
        .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.firstName").value("NewUser"))
        .andExpect(jsonPath("$.email").value("newmail@mail.com"));
  }

  @Test
  void updateUserException() throws Exception {
    mvc.perform(put(USER_ENDPOINT + "/{id}", 100).contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(new UserRequest("NewUser", "Kane", "newmail@mail.com", "pass24AS",
            "lead", "IT", "user", true))))
        .andExpect(status().isNotFound())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.statusCode").value("404"))
        .andExpect(jsonPath("$.message").value("Unable to update user. User doesn't exist."));
  }

  private static String asJsonString(final Object obj) {
    try {
      return new ObjectMapper().writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
