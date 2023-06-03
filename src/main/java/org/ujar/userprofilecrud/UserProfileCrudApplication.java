package org.ujar.userprofilecrud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;

@SpringBootApplication
public class UserProfileCrudApplication {
  public static void main(String[] args) {
    SpringApplication springApplication = new SpringApplication(UserProfileCrudApplication.class);
    springApplication.setApplicationStartup(new BufferingApplicationStartup(2048));
    springApplication.run(args);
  }
}
