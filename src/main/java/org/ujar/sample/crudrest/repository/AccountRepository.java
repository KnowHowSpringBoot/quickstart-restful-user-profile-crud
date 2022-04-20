package org.ujar.sample.crudrest.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.ujar.sample.crudrest.entity.Account;

public interface AccountRepository extends CrudRepository<Account, Integer> {

  List<Account> findAll();
}
