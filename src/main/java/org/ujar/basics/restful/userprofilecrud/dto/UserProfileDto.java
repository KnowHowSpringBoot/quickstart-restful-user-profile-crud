package org.ujar.basics.restful.userprofilecrud.dto;

public record UserProfileDto(String email, boolean active) {
  public boolean isActive() {
    return active();
  }
}
