package org.ujar.userprofilecrud.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.ujar.userprofilecrud.entity.UserProfile;
import org.ujar.userprofilecrud.repository.UserProfileRepository;

@WebMvcTest(value = UserProfileResource.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
class UserProfileResourceTest {
  @MockBean
  private UserProfileRepository userProfileRepository;

  private final MockMvc mockMvc;

  private final ObjectMapper objectMapper;

  @Autowired
  public UserProfileResourceTest(MockMvc mockMvc, ObjectMapper objectMapper) {
    this.mockMvc = mockMvc;
    this.objectMapper = objectMapper;
  }

  @Test
  void shouldCreateUserProfile() throws Exception {
    UserProfile userProfile = new UserProfile(1L, "spring@example.com", true);

    mockMvc.perform(post("/api/v1/user-profiles").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userProfile)))
        .andExpect(status().isCreated())
        .andDo(print());
  }

  @Test
  void shouldReturnUserProfile() throws Exception {
    long id = 1L;
    UserProfile userProfile = new UserProfile(id, "i@example.com", true);

    when(userProfileRepository.findById(id)).thenReturn(Optional.of(userProfile));
    mockMvc.perform(get("/api/v1/user-profiles/{id}", id)).andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(id))
        .andExpect(jsonPath("$.email").value(userProfile.getEmail()))
        .andExpect(jsonPath("$.active").value(userProfile.isActive()))
        .andDo(print());
  }

  @Test
  void shouldReturnNotFoundUserProfile() throws Exception {
    long id = 1L;

    when(userProfileRepository.findById(id)).thenReturn(Optional.empty());
    mockMvc.perform(get("/api/v1/user-profiles/{id}", id))
        .andExpect(status().isNotFound())
        .andDo(print());
  }

  @Test
  void shouldReturnListOfUserProfiles() throws Exception {
    List<UserProfile> userProfiles = new ArrayList<>(
        Arrays.asList(new UserProfile(1L, "spring@example.com 1", true),
            new UserProfile(2L, "spring@example.com 2", true),
            new UserProfile(3L, "spring@example.com 3", true)));

    when(userProfileRepository.findAll()).thenReturn(userProfiles);
    mockMvc.perform(get("/api/v1/user-profiles"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.size()").value(userProfiles.size()))
        .andDo(print());
  }

  @Test
  void shouldReturnListOfUserProfilesWithFilter() throws Exception {
    List<UserProfile> userProfiles = new ArrayList<>(
        Arrays.asList(new UserProfile(1L, "spring@example.com", true),
            new UserProfile(3L, "demo@example.com", true)));

    String email = "example";
    MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
    paramsMap.add("email", email);

    when(userProfileRepository.findByEmailContaining(email)).thenReturn(userProfiles);
    mockMvc.perform(get("/api/v1/user-profiles").params(paramsMap))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.size()").value(userProfiles.size()))
        .andDo(print());
  }

  @Test
  void shouldReturnNoContentWhenFilter() throws Exception {
    String email = "coder@example.com";
    MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
    paramsMap.add("email", email);

    List<UserProfile> userProfiles = Collections.emptyList();

    when(userProfileRepository.findByEmail(email)).thenReturn(userProfiles);
    mockMvc.perform(get("/api/v1/user-profiles").params(paramsMap))
        .andExpect(status().isNoContent())
        .andDo(print());
  }

  @Test
  void shouldUpdateUserProfile() throws Exception {
    long id = 1L;

    UserProfile userProfile = new UserProfile(id, "spring@example.com", false);
    UserProfile updateduserProfile = new UserProfile(id, "updated@example.com", true);

    when(userProfileRepository.findById(id)).thenReturn(Optional.of(userProfile));
    when(userProfileRepository.save(any(UserProfile.class))).thenReturn(updateduserProfile);

    mockMvc.perform(put("/api/v1/user-profiles/{id}", id).contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateduserProfile)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.email").value(updateduserProfile.getEmail()))
        .andExpect(jsonPath("$.active").value(updateduserProfile.isActive()))
        .andDo(print());
  }

  @Test
  void shouldReturnNotFoundUpdateUserProfile() throws Exception {
    long id = 1L;

    UserProfile updateduserProfile = new UserProfile(id, "updated@example.com", true);

    when(userProfileRepository.findById(id)).thenReturn(Optional.empty());
    when(userProfileRepository.save(any(UserProfile.class))).thenReturn(updateduserProfile);

    mockMvc.perform(put("/api/v1/user-profiles/{id}", id).contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateduserProfile)))
        .andExpect(status().isNotFound())
        .andDo(print());
  }

  @Test
  void shouldDeleteUserProfile() throws Exception {
    long id = 1L;

    doNothing().when(userProfileRepository).deleteById(id);
    mockMvc.perform(delete("/api/v1/user-profiles/{id}", id))
        .andExpect(status().isNoContent())
        .andDo(print());
  }

  @Test
  void shouldDeleteAllUserProfiles() throws Exception {
    doNothing().when(userProfileRepository).deleteAll();
    mockMvc.perform(delete("/api/v1/user-profiles"))
        .andExpect(status().isNoContent())
        .andDo(print());
  }
}
