package org.ujar.sample.crudrest.web;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.ujar.sample.crudrest.entity.Account;
import org.ujar.sample.crudrest.repository.AccountRepository;

@RestController
public class AccountController {

  private final AccountRepository accountRepository;

  public AccountController(@Autowired AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  @RequestMapping(value = "/account", method = RequestMethod.POST)
  public ResponseEntity<Account> create(@RequestBody Account account) {
    return new ResponseEntity<>(accountRepository.save(account), HttpStatus.CREATED);
  }

  @RequestMapping(value = "/account/{id}", method = RequestMethod.GET)
  public ResponseEntity<Account> findById(@PathVariable int id) {
    return new ResponseEntity<>(accountRepository.findById(id).orElse(null), HttpStatus.OK);
  }

  @RequestMapping(value = "/account", method = RequestMethod.GET)
  public ResponseEntity<List<Account>> findAll() {
    return new ResponseEntity<>((List<Account>) accountRepository.findAll(), HttpStatus.OK);
  }

  @RequestMapping(value = "/account/{id}", method = RequestMethod.PUT)
  public ResponseEntity<Account> update(@PathVariable int id, @RequestBody Account account) {
    account.setId(id);
    return new ResponseEntity<>(accountRepository.save(account), HttpStatus.OK);
  }

  @RequestMapping(value = "/account/{id}", method = RequestMethod.DELETE)
  public HttpStatus delete(@PathVariable int id) {
    accountRepository.deleteById(id);
    return HttpStatus.OK;
  }

}
