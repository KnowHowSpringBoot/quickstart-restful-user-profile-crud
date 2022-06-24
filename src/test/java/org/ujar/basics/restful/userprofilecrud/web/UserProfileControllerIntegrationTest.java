package org.ujar.basics.restful.userprofilecrud.web;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.gson.Gson;
import java.util.ArrayList;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.ujar.basics.restful.userprofilecrud.entity.UserProfile;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class UserProfileControllerIntegrationTest {

  private final MockMvc mvc;

  UserProfileControllerIntegrationTest(@Autowired final MockMvc mvc) {
    this.mvc = mvc;
  }

  @Test
  @SneakyThrows
  void shouldCreateUserProfile() {
    final var profile = new UserProfile();
    profile.setEmail("bazz@example.net");
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
  @SneakyThrows
  void shouldFindProfileById() {
    final var gson = new Gson();
    final var profile = new UserProfile();
    profile.setEmail("bar@example.net");
    profile.setActive(true);

    var responseBody = createNewEntity(profile);

    MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/api/v1/user-profiles/" + responseBody.getId())
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();
    responseBody = gson.fromJson(result.getResponse().getContentAsString(), UserProfile.class);

    assertEntityEquals(profile, responseBody);
    deleteEntity(responseBody.getId());
  }

  @Test
  @SneakyThrows
  void shouldFindAllProfiles() {
    final var userProfile = new UserProfile();
    userProfile.setActive(true);

    final var numberOfRecords = 3;
    final var emails = new String[] {"foo@example.net", "bar@example.net", "bazz@example.net"};
    final var createdProfiles = new ArrayList<UserProfile>();
    for (int i = 0; i < numberOfRecords; i++) {
      userProfile.setEmail(emails[i]);
      createdProfiles.add(createNewEntity(userProfile));
    }

    mvc.perform(MockMvcRequestBuilders.get("/api/v1/user-profiles")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json(
            """
                {
                  "content":[
                    {
                      "active":true
                    },
                    {
                      "active":true
                    },
                    {
                      "active":true
                    }
                  ],
                  "pageable":{
                    "sort":{
                      "empty":true,
                      "unsorted":true,
                      "sorted":false
                    },
                    "offset":0,
                    "pageSize":10,
                    "pageNumber":0,
                    "unpaged":false,
                    "paged":true
                  },
                  "last":true,
                  "totalElements":3,
                  "totalPages":1,
                  "size":10,
                  "number":0,
                  "sort":{
                    "empty":true,
                    "unsorted":true,
                    "sorted":false
                  },
                  "first":true,
                  "numberOfElements":3,
                  "empty":false
                }"""
        ));
    for (UserProfile singleEntity : createdProfiles) {
      deleteEntity(singleEntity.getId());
    }
  }

  @Test
  @SneakyThrows
  void shouldUpdateUserProfile() {
    final var profile = new UserProfile();
    profile.setEmail("foo@example.net");
    profile.setActive(true);

    var responseBody = createNewEntity(profile);

    responseBody = getEntityById(responseBody.getId(), status().isOk());
    responseBody.setActive(false);
    responseBody = updateEntity(responseBody);
    profile.setActive(false);

    assertEntityEquals(profile, responseBody);
    deleteEntity(responseBody.getId());
  }

  @Test
  void shouldDeleteUserProfile() {
    final var profile = new UserProfile();
    profile.setEmail("bar@example.net");
    profile.setActive(true);

    final var responseBody = createNewEntity(profile);

    deleteEntity(responseBody.getId());
    getEntityById(responseBody.getId(), status().isNotFound());
  }

  @SneakyThrows
  private UserProfile createNewEntity(final UserProfile profile) {
    final var gson = new Gson();

    MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/api/v1/user-profiles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(gson.toJson(profile))
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andReturn();

    return gson.fromJson(result.getResponse().getContentAsString(), UserProfile.class);
  }

  @SneakyThrows
  private UserProfile updateEntity(final UserProfile profile) {
    final var gson = new Gson();
    final var id = profile.getId();
    MvcResult result = mvc.perform(MockMvcRequestBuilders.put("/api/v1/user-profiles/" + id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(gson.toJson(profile))
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();

    return gson.fromJson(result.getResponse().getContentAsString(), UserProfile.class);
  }

  @SneakyThrows
  private UserProfile getEntityById(final Long id, final ResultMatcher matcher) {
    final var gson = new Gson();

    final var result = mvc.perform(MockMvcRequestBuilders.get("/api/v1/user-profiles/" + id)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(matcher)
        .andReturn();

    return gson.fromJson(result.getResponse().getContentAsString(), UserProfile.class);
  }

  @SneakyThrows
  private void deleteEntity(final Long id) {
    mvc.perform(MockMvcRequestBuilders.delete("/api/v1/user-profiles/" + id)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();
  }

  private void assertEntityEquals(final UserProfile profile, final UserProfile responseBody) {
    assertNotNull(responseBody);
    assertNotNull(responseBody.getId());
    assertEquals(responseBody.isActive(), profile.isActive());
  }

}
