package org.ujar.sample.crudrest.userprofile.config;

import javax.sql.DataSource;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.ujar.starter.rest.logbook.LogbookJsonBodyFilter;
import org.ujar.starter.rest.logbook.LogbookResponseOnStatus;

@Configuration
@LogbookResponseOnStatus
@LogbookJsonBodyFilter
@EnableJpaRepositories({"org.ujar.sample.crudrest.userprofile.repository"})
@EnableJpaAuditing
@EnableTransactionManagement
@EnableConfigurationProperties({SecurityProperties.class})
public class ApplicationConfig {

  @Bean
  public SpringLiquibase liquibase(@Autowired DataSource dataSource) {
    var liquibase = new SpringLiquibase();
    liquibase.setChangeLog("classpath:liquibase/master.xml");
    liquibase.setDataSource(dataSource);
    return liquibase;
  }
}
