package org.ujar.sample.rest.userprofilecrud.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "sample_rest_user_profiles")
public class UserProfile {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private boolean active;
}
