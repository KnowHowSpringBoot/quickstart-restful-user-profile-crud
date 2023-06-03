package org.ujar.userprofilecrud.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.ujar.boot.restful.web.ApiError;
import org.ujar.userprofilecrud.entity.UserProfile;
import org.ujar.userprofilecrud.repository.UserProfileRepository;

@RestController
@Tag(name = "User profiles", description = "API endpoints for managing user profile entity.")
@Validated
@RequestMapping("/api/v1/user-profiles")
@RequiredArgsConstructor
class UserProfileResource {

  private final UserProfileRepository userProfileRepository;

  @PostMapping
  @Operation(
      description = "Create a new user profile.",
      responses = {
          @ApiResponse(responseCode = "201",
                       description = "Created"),
          @ApiResponse(responseCode = "500",
                       description = "Internal error",
                       content = @Content(schema = @Schema(implementation = ApiError.class))),
          @ApiResponse(responseCode = "400",
                       description = "Bad request",
                       content = @Content(schema = @Schema(implementation = ApiError.class))),
      })
  ResponseEntity<UserProfile> create(@RequestBody UserProfileRequest userProfileRequest) {
    UserProfile userProfile = userProfileRepository.save(new UserProfile(userProfileRequest.email(), userProfileRequest.active()));
    return new ResponseEntity<>(userProfile, HttpStatus.CREATED);
  }

  @GetMapping("/{id}")
  @Operation(
      description = "Retrieve user profile by id.",
      responses = {
          @ApiResponse(responseCode = "200",
                       description = "Success"),
          @ApiResponse(responseCode = "500",
                       description = "Internal error",
                       content = @Content(schema = @Schema(implementation = ApiError.class))),
          @ApiResponse(responseCode = "400",
                       description = "Bad request",
                       content = @Content(schema = @Schema(implementation = ApiError.class))),
          @ApiResponse(responseCode = "404",
                       description = "Not found",
                       content = @Content(schema = @Schema(implementation = ApiError.class)))
      })
  ResponseEntity<UserProfile> findById(@PathVariable("id") long id) {
    Optional<UserProfile> userProfileData = userProfileRepository.findById(id);
    return userProfileData.map(userProfile -> new ResponseEntity<>(userProfile, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @GetMapping
  @Operation(
      description = "Get all the user profiles.",
      responses = {
          @ApiResponse(responseCode = "200",
                       description = "Success"),
          @ApiResponse(responseCode = "500",
                       description = "Internal error",
                       content = @Content(schema = @Schema(implementation = ApiError.class))),
          @ApiResponse(responseCode = "400",
                       description = "Bad request",
                       content = @Content(schema = @Schema(implementation = ApiError.class))),
      })
  ResponseEntity<List<UserProfile>> findAll(@RequestParam(required = false) String email) {
    List<UserProfile> userProfiles = new ArrayList<>();

    if (email == null) {
      userProfiles.addAll(userProfileRepository.findAll());
    } else {
      userProfiles.addAll(userProfileRepository.findByEmailContaining(email));
    }

    if (userProfiles.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    return new ResponseEntity<>(userProfiles, HttpStatus.OK);
  }

  @PutMapping("/{id}")
  @Operation(
      description = "Updates an existing user profile.",
      responses = {
          @ApiResponse(responseCode = "200",
                       description = "Success"),
          @ApiResponse(responseCode = "500",
                       description = "Internal error",
                       content = @Content(schema = @Schema(implementation = ApiError.class))),
          @ApiResponse(responseCode = "400",
                       description = "Bad request",
                       content = @Content(schema = @Schema(implementation = ApiError.class))),
      })
  ResponseEntity<UserProfile> update(@PathVariable("id") long id, @RequestBody UserProfileRequest userProfileRequest) {
    Optional<UserProfile> userProfileData = userProfileRepository.findById(id);

    if (userProfileData.isPresent()) {
      UserProfile userProfile = userProfileData.get();
      userProfile.setEmail(userProfileRequest.email());
      userProfile.setActive(userProfileRequest.active());
      return new ResponseEntity<>(userProfileRepository.save(userProfile), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @DeleteMapping("/{id}")
  ResponseEntity<HttpStatus> delete(@PathVariable("id") long id) {
    userProfileRepository.deleteById(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);

  }

  @DeleteMapping
  @Operation(
      description = "Delete all user profiles.",
      responses = {
          @ApiResponse(responseCode = "204",
                       description = "No Content"),
          @ApiResponse(responseCode = "500",
                       description = "Internal error",
                       content = @Content(schema = @Schema(implementation = ApiError.class))),
          @ApiResponse(responseCode = "400",
                       description = "Bad request",
                       content = @Content(schema = @Schema(implementation = ApiError.class))),
      })
  ResponseEntity<HttpStatus> deleteAll() {
    userProfileRepository.deleteAll();
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @GetMapping("/active")
  @Operation(
      description = "Get all active profiles.",
      responses = {
          @ApiResponse(responseCode = "200",
                       description = "Success"),
          @ApiResponse(responseCode = "500",
                       description = "Internal error",
                       content = @Content(schema = @Schema(implementation = ApiError.class))),
          @ApiResponse(responseCode = "400",
                       description = "Bad request",
                       content = @Content(schema = @Schema(implementation = ApiError.class))),
      })
  ResponseEntity<List<UserProfile>> findByActive() {
    List<UserProfile> userProfiles = userProfileRepository.findByActive(true);

    if (userProfiles.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    return new ResponseEntity<>(userProfiles, HttpStatus.OK);
  }

  record UserProfileRequest(String email, boolean active) {
  }
}
