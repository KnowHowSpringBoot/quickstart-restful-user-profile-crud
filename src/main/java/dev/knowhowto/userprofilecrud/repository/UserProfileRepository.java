package dev.knowhowto.userprofilecrud.repository;

import java.util.List;

import dev.knowhowto.userprofilecrud.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepository
    extends PagingAndSortingRepository<UserProfile, Long>, JpaRepository<UserProfile, Long> {

  List<UserProfile> findByEmail(String email);

  List<UserProfile> findByEmailContaining(String email);

  List<UserProfile> findByActive(boolean activeState);
}
