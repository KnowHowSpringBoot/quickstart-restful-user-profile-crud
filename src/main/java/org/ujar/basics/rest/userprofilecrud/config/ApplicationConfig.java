package org.ujar.basics.rest.userprofilecrud.config;

import javax.sql.DataSource;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Autowired;
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
@EnableJpaRepositories({"org.ujar.basics.rest.userprofilecrud.repository"})
@EnableJpaAuditing
@EnableTransactionManagement
public class ApplicationConfig {

  @Bean
  public SpringLiquibase liquibase(@Autowired DataSource dataSource) {
    var liquibase = new SpringLiquibase();
    liquibase.setChangeLog("classpath:liquibase/master.xml");
    liquibase.setDataSource(dataSource);
    return liquibase;
  }
}
