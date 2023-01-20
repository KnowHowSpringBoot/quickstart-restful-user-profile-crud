package org.ujar.bs.rst.userprofilecrud.web.dto;

public record UserProfileDto(String email, boolean active) {
  public boolean isActive() {
    return active();
  }
}
