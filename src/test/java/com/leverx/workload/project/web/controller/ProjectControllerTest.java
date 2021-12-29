package com.leverx.workload.project.web.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.leverx.workload.config.ApplicationConfig;
import com.leverx.workload.config.H2TestConfig;
import com.leverx.workload.config.MapperConfig;
import com.leverx.workload.config.WebConfig;
import com.leverx.workload.project.web.dto.request.ProjectBodyParams;
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
class ProjectControllerTest {

  public static final String PROJECT_ENDPOINT = "/projects";
  private final WebApplicationContext webAppContext;
  private MockMvc mvc;

  @Autowired
  public ProjectControllerTest(WebApplicationContext webAppContext) {
    this.webAppContext = webAppContext;
  }

  @BeforeEach
  void setUp() {
    this.mvc = MockMvcBuilders.webAppContextSetup(this.webAppContext).build();
  }

  @Test
  void getAllProjectsFilterByStartDate_AllGood_ResponseReceived() throws Exception {
    mvc.perform(get(PROJECT_ENDPOINT + "?startDate=2021-11-01")).andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$[0].name").value("js project"))
        .andExpect(jsonPath("$[0].endDate").value("2023-05-28"))
        .andExpect(jsonPath("$.length()").value("1"));
  }

  @Test
  void getProjectById_ProjectExists_ResponseOk() throws Exception {
    mvc.perform(get(PROJECT_ENDPOINT + "/{id}", 2)).andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.name").value("js project"))
        .andExpect(jsonPath("$.startDate").value("2021-11-09"));
  }

  @Test
  void getProjectById_ProjectNotExist_Exception() throws Exception {
    mvc.perform(get(PROJECT_ENDPOINT + "/{id}", 9999)).andExpect(status().isNotFound())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.statusCode").value("404")).andExpect(
            jsonPath("$.message").value(String.format("Project with id=%s not found", 9999)));
  }

  @Test
  void createProject_ProjectValid_Created() throws Exception {
    mvc.perform(post(PROJECT_ENDPOINT).contentType(MediaType.APPLICATION_JSON)
        .content(GeneralUtils.asJsonString(createProjectBodyParamsSample())))
        .andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$").value("12"));
  }

  @Test
  void updateProject_ProjectValid_Updated() throws Exception {
    ProjectBodyParams params = createProjectBodyParamsSample();
    params.setId(2L);
    mvc.perform(put(PROJECT_ENDPOINT).contentType(MediaType.APPLICATION_JSON)
        .content(GeneralUtils.asJsonString(params))).andExpect(status().isOk());
  }

  @Test
  void updateProject_ProjectNotExist_Exception() throws Exception {
    ProjectBodyParams params = createProjectBodyParamsSample();
    params.setId(999L);
    mvc.perform(put(PROJECT_ENDPOINT).contentType(MediaType.APPLICATION_JSON)
        .content(GeneralUtils.asJsonString(params))).andExpect(status().isNotFound())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.statusCode").value("404"))
        .andExpect(jsonPath("$.message").value("Unable to update project. Project doesn't exist."));
  }

  @Test
  void updateProject_NotValidDates_Exception() throws Exception {
    ProjectBodyParams params = createProjectBodyParamsSample();
    params.setEndDate("2000-12-22");
    mvc.perform(put(PROJECT_ENDPOINT).contentType(MediaType.APPLICATION_JSON)
        .content(GeneralUtils.asJsonString(params))).andExpect(status().isNotFound())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.statusCode").value("400")).andExpect(
            jsonPath("$.message").value("The project end date must be later than the start date."));
  }

  @Test
  void deleteProject_IdValid_ProjectDeleted() throws Exception {
    mvc.perform(delete(PROJECT_ENDPOINT + "/{id}", 2).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());
  }

  @Test
  void deleteProject_ProjectNotExist_Exception() throws Exception {
    mvc.perform(delete(PROJECT_ENDPOINT + "/{id}", 999).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.statusCode").value("404"))
        .andExpect(jsonPath("$.message").value("Unable to delete project. Project doesn't exist."));
  }

  private ProjectBodyParams createProjectBodyParamsSample() {
    ProjectBodyParams project = new ProjectBodyParams();
    project.setId(5);
    project.setName("test project");
    project.setStartDate("2021-12-22");
    project.setEndDate("2023-04-20");
    return project;
  }
}
