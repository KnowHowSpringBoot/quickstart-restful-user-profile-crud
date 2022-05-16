package org.ujar.basics.rest.userprofilecrud.web;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.gson.Gson;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.ujar.basics.rest.userprofilecrud.entity.UserProfile;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserProfileControllerTest {

  private final MockMvc mvc;

  public UserProfileControllerTest(@Autowired MockMvc mvc) {
    this.mvc = mvc;
  }

  @Test
  public void create() throws Exception {
    var profile = new UserProfile();
    profile.setActive(true);

    var responseBody = createNewEntity(profile);
    assertEntityEquals(profile, responseBody);
    deleteEntity(responseBody.getId());

    profile.setActive(false);

    responseBody = createNewEntity(profile);
    assertEntityEquals(profile, responseBody);
    deleteEntity(responseBody.getId());
  }

  @Test
  public void findById() throws Exception {
    var gson = new Gson();
    var profile = new UserProfile();
    profile.setActive(true);

    var responseBody = createNewEntity(profile);

    MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/v1/user-profiles/" + responseBody.getId())
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();
    responseBody = gson.fromJson(result.getResponse().getContentAsString(), UserProfile.class);

    assertEntityEquals(profile, responseBody);
    deleteEntity(responseBody.getId());
  }

  @Test
  public void findAll() throws Exception {

    var userProfile = new UserProfile();
    userProfile.setActive(true);

    var numberOfRecords = 3;

    var createdProfiles = new ArrayList<UserProfile>();
    for (int i = 0; i < numberOfRecords; i++) {
      createdProfiles.add(createNewEntity(userProfile));
    }

    mvc.perform(MockMvcRequestBuilders.get("/v1/user-profiles")
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json(
            "{\n"
            + "  \"content\":[\n"
            + "    {\n"
            + "      \"active\":true\n"
            + "    },\n"
            + "    {\n"
            + "      \"active\":true\n"
            + "    },\n"
            + "    {\n"
            + "      \"active\":true\n"
            + "    }\n"
            + "  ],\n"
            + "  \"pageable\":{\n"
            + "    \"sort\":{\n"
            + "      \"empty\":true,\n"
            + "      \"unsorted\":true,\n"
            + "      \"sorted\":false\n"
            + "    },\n"
            + "    \"offset\":0,\n"
            + "    \"pageSize\":10,\n"
            + "    \"pageNumber\":0,\n"
            + "    \"unpaged\":false,\n"
            + "    \"paged\":true\n"
            + "  },\n"
            + "  \"last\":true,\n"
            + "  \"totalElements\":3,\n"
            + "  \"totalPages\":1,\n"
            + "  \"size\":10,\n"
            + "  \"number\":0,\n"
            + "  \"sort\":{\n"
            + "    \"empty\":true,\n"
            + "    \"unsorted\":true,\n"
            + "    \"sorted\":false\n"
            + "  },\n"
            + "  \"first\":true,\n"
            + "  \"numberOfElements\":3,\n"
            + "  \"empty\":false\n"
            + "}"
        ));
    for (UserProfile singleEntity : createdProfiles) {
      deleteEntity(singleEntity.getId());
    }
  }

  @Test
  public void update() throws Exception {
    var profile = new UserProfile();
    profile.setActive(true);

    var responseBody = createNewEntity(profile);

    responseBody = getEntityById(responseBody.getId());
    responseBody.setActive(false);
    responseBody = updateEntity(responseBody);
    profile.setActive(false);
    assertEntityEquals(profile, responseBody);
    deleteEntity(responseBody.getId());
  }

  @Test
  public void delete() throws Exception {
    var profile = new UserProfile();
    profile.setActive(true);

    var responseBody = createNewEntity(profile);

    deleteEntity(responseBody.getId());
    responseBody = getEntityById(responseBody.getId());
    assertNull(responseBody);
  }

  private UserProfile createNewEntity(UserProfile profile) throws Exception {
    var gson = new Gson();

    MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/v1/user-profiles")
        .contentType(MediaType.APPLICATION_JSON)
        .content(gson.toJson(profile))
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andReturn();

    return gson.fromJson(result.getResponse().getContentAsString(), UserProfile.class);
  }

  private UserProfile updateEntity(UserProfile profile) throws Exception {
    var gson = new Gson();
    var id = profile.getId();
    MvcResult result = mvc.perform(MockMvcRequestBuilders.put("/v1/user-profiles/" + id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(gson.toJson(profile))
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();

    return gson.fromJson(result.getResponse().getContentAsString(), UserProfile.class);
  }

  private UserProfile getEntityById(Long id) throws Exception {
    var gson = new Gson();

    var result = mvc.perform(MockMvcRequestBuilders.get("/v1/user-profiles/" + id)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();

    return gson.fromJson(result.getResponse().getContentAsString(), UserProfile.class);
  }

  private void deleteEntity(Long id) throws Exception {
    mvc.perform(MockMvcRequestBuilders.delete("/v1/user-profiles/" + id)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();
  }

  private void assertEntityEquals(UserProfile profile, UserProfile responseBody) {
    assertNotNull(responseBody);
    assertNotNull(responseBody.getId());
    assertEquals(responseBody.isActive(), profile.isActive());
  }

}
