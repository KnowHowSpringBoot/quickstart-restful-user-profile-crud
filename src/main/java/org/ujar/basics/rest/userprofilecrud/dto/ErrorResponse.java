package org.ujar.basics.rest.userprofilecrud.dto;

import java.util.List;
import lombok.Getter;

@Getter
public record ErrorResponse(List<Error> errors) {
  @Getter
  public record Error(String message) {
  }
}
