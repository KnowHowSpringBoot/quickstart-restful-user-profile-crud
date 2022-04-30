package org.ujar.basics.rest.userprofilecrud.dto;

public record UserProfileDto(boolean active) {
  public boolean isActive() {
    return active();
  }
}
