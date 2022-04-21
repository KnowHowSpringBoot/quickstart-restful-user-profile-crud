package org.ujar.sample.crudrest.repository;

import org.springframework.data.repository.CrudRepository;
import org.ujar.sample.crudrest.entity.Account;

public interface AccountRepository extends CrudRepository<Account, Integer> {

}
