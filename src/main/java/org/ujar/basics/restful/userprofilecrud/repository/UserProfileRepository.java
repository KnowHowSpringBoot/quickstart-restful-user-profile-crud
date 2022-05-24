package org.ujar.basics.restful.userprofilecrud.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.ujar.basics.restful.userprofilecrud.entity.UserProfile;

public interface UserProfileRepository
    extends PagingAndSortingRepository<UserProfile, Long>, JpaRepository<UserProfile, Long> {

}
