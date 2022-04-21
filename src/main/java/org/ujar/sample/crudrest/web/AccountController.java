package org.ujar.sample.crudrest.web;

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
import org.ujar.sample.crudrest.dto.PageRequestDto;
import org.ujar.sample.crudrest.entity.Account;
import org.ujar.sample.crudrest.repository.AccountRepository;

@RestController
@Tag(name = "Account controller", description = "API for Accounts")
@RequestMapping("/account")
@Validated
@RequiredArgsConstructor
public class AccountController {

  private final AccountRepository accountRepository;

  @PostMapping
  public ResponseEntity<Account> create(@RequestBody Account account) {
    return new ResponseEntity<>(accountRepository.save(account), HttpStatus.CREATED);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Account> findById(@PathVariable Long id) {
    return new ResponseEntity<>(accountRepository.findById(id).orElse(null), HttpStatus.OK);
  }

  @GetMapping
  public ResponseEntity<Page<Account>> findAll(@ParameterObject @Valid PageRequestDto request) {
    var pageRequest = PageRequest.of(request.getPage(), request.getSize());
    return new ResponseEntity<>(accountRepository.findAll(pageRequest), HttpStatus.OK);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Account> update(@PathVariable Long id, @RequestBody Account account) {
    account.setId(id);
    return new ResponseEntity<>(accountRepository.save(account), HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public HttpStatus delete(@PathVariable Long id) {
    accountRepository.deleteById(id);
    return HttpStatus.OK;
  }

}
