package org.ujar.sample.crudrest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.ujar.sample.crudrest.entity.Account;

public interface AccountRepository extends PagingAndSortingRepository<Account, Long>, JpaRepository<Account, Long> {

}
