package org.ujar.sample.rest.userprofilecrud.web;

import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import org.springframework.web.bind.annotation.RestController;
import org.ujar.sample.rest.userprofilecrud.dto.PageRequestDto;
import org.ujar.sample.rest.userprofilecrud.entity.UserProfile;
import org.ujar.sample.rest.userprofilecrud.repository.UserProfileRepository;

@RestController
@Tag(name = "User profile controller", description = "API for user profiles management")
@RequestMapping("/user-profile")
@Validated
@RequiredArgsConstructor
public class UserProfileController {

  private final UserProfileRepository profileRepository;

  @PostMapping
  public ResponseEntity<UserProfile> create(@RequestBody UserProfile profile) {
    return new ResponseEntity<>(profileRepository.save(profile), HttpStatus.CREATED);
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserProfile> findById(@PathVariable Long id) {
    return new ResponseEntity<>(profileRepository.findById(id).orElse(null), HttpStatus.OK);
  }

  @GetMapping
  public ResponseEntity<Page<UserProfile>> findAll(@ParameterObject @Valid PageRequestDto request) {
    var pageRequest = PageRequest.of(request.getPage(), request.getSize());
    return new ResponseEntity<>(profileRepository.findAll(pageRequest), HttpStatus.OK);
  }

  @PutMapping("/{id}")
  public ResponseEntity<UserProfile> update(@PathVariable Long id, @RequestBody UserProfile profile) {
    profile.setId(id);
    return new ResponseEntity<>(profileRepository.save(profile), HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public HttpStatus delete(@PathVariable Long id) {
    profileRepository.deleteById(id);
    return HttpStatus.OK;
  }

}
