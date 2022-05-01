package org.ujar.basics.rest.userprofilecrud.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = UserProfile.TABLE_NAME)
public class UserProfile {

  protected static final String TABLE_NAME =  "BASICS_REST_USER_PROFILES";

  @Id
  @SequenceGenerator(
      name = "USER_PROFILE_ID_SEQ",
      sequenceName = "USER_PROFILE_ID_SEQ"
  )
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "USER_PROFILE_ID_SEQ"
  )
  private Long id;
  private boolean active;
}
