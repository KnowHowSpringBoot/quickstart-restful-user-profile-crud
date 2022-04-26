package org.ujar.sample.crudrest.userprofile.config;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@Data
@ConfigurationProperties(prefix = "spring.security")
public class SecurityProperties {
  @NestedConfigurationProperty
  private Map<String, User> users = new HashMap<>();

  @Data
  public static class User {
    private String name;
    private String password;
  }
}
