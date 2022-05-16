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

  protected static final String TABLE_NAME =  "basics_rest_user_profiles";

  @Id
  @SequenceGenerator(
      name = "user_profile_id_seq",
      sequenceName = "user_profile_id_seq"
  )
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "user_profile_id_seq"
  )
  private Long id;
  private boolean active;
}
