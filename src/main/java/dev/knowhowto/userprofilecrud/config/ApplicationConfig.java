package dev.knowhowto.userprofilecrud.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories({"dev.knowhowto.userprofilecrud.repository"})
@EnableJpaAuditing
@EnableTransactionManagement
@OpenAPIDefinition(info = @Info(title = "User Profile Management", version = "24.0.0"))
class ApplicationConfig {

}
