package org.ujar.userprofilecrud.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = UserProfile.TABLE_NAME)
public class UserProfile {

  protected static final String TABLE_NAME = "basics_rest_user_profiles";

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

  @Email
  @Size(min = 5, max = 120)
  @Column(length = 120, unique = true)
  private String email;

  private boolean active;

  @Override
  public String toString() {
    return "UserProfile{" +
           "id=" + id +
           ", email='" + email + '\'' +
           ", active=" + active +
           '}';
  }
}
