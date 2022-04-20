package org.ujar.sample.crudrest.web;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import javax.validation.ConstraintViolationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.ujar.sample.crudrest.dto.ErrorResponse;
import org.ujar.sample.crudrest.exception.EntityAlreadyExistsException;
import org.ujar.sample.crudrest.exception.EntityNotFoundException;

@ControllerAdvice
@ResponseBody
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
  @Nonnull
  @Override
  protected ResponseEntity<Object> handleExceptionInternal(
      Exception ex,
      Object body,
      @Nonnull HttpHeaders headers,
      @Nonnull HttpStatus status,
      @Nonnull WebRequest request
  ) {
    var errorResponse = new ErrorResponse(List.of(new ErrorResponse.Error(ex.getMessage())));
    return new ResponseEntity<>(errorResponse, headers, status);
  }

  @Nonnull
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
                                                                @Nonnull HttpHeaders headers,
                                                                @Nonnull HttpStatus status,
                                                                @Nonnull WebRequest request) {
    var fieldErrors = exception.getBindingResult().getFieldErrors().stream()
        .map(error -> String.format("Error in field \"%s\": %s", error.getField(), error.getDefaultMessage()))
        .map(ErrorResponse.Error::new);
    var globalErrors = exception.getBindingResult().getGlobalErrors().stream()
        .map(DefaultMessageSourceResolvable::getDefaultMessage)
        .map(ErrorResponse.Error::new);
    var errors = Stream.concat(fieldErrors, globalErrors).collect(Collectors.toList());

    return new ResponseEntity<>(new ErrorResponse(errors), HttpStatus.UNPROCESSABLE_ENTITY);
  }

  @Nonnull
  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException exception,
                                                                @Nonnull HttpHeaders headers,
                                                                @Nonnull HttpStatus status,
                                                                @Nonnull WebRequest request) {
    if (exception.getCause() instanceof InvalidFormatException) {
      var formatException = (InvalidFormatException) exception.getCause();
      var allowedValues = Arrays.toString(formatException.getTargetType().getEnumConstants());
      var providedValue = formatException.getValue();
      var enumType = formatException.getTargetType().getSimpleName();

      var message = String.format("%s value must be one of %s. Provided value is: %s.",
          enumType, allowedValues, providedValue);
      var errors = List.of(new ErrorResponse.Error(message));

      return new ResponseEntity<>(new ErrorResponse(errors), HttpStatus.BAD_REQUEST);
    } else {
      var errors = List.of(new ErrorResponse.Error("Request is not readable!"));
      return new ResponseEntity<>(new ErrorResponse(errors), HttpStatus.BAD_REQUEST);
    }
  }

  @Nonnull
  @Override
  protected ResponseEntity<Object> handleBindException(@Nonnull BindException bindException,
                                                       @Nonnull HttpHeaders headers,
                                                       @Nonnull HttpStatus status,
                                                       @Nonnull WebRequest request) {
    var errors = bindException.getAllErrors().stream()
        .map(error -> new ErrorResponse.Error(error.getDefaultMessage()))
        .collect(Collectors.toList());
    return new ResponseEntity<>(new ErrorResponse(errors), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse constraintViolationException(ConstraintViolationException exception) {
    var errors = exception.getConstraintViolations().stream()
        .map(violation -> String
            .format("Property path: \"%s\": %s", violation.getPropertyPath(), violation.getMessage()))
        .map(ErrorResponse.Error::new)
        .collect(Collectors.toList());
    return new ErrorResponse(errors);
  }

  // Custom exception handling
  @ExceptionHandler(EntityNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ErrorResponse entityNotFoundException(EntityNotFoundException exception) {
    var errors = List.of(new ErrorResponse.Error(exception.getMessage()));
    return new ErrorResponse(errors);
  }

  @ExceptionHandler(EntityAlreadyExistsException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  public ErrorResponse entityAlreadyExistsException(EntityAlreadyExistsException exception) {
    var errors = List.of(new ErrorResponse.Error(exception.getMessage()));
    return new ErrorResponse(errors);
  }
}
