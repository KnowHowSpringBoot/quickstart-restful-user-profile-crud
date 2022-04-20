package org.ujar.sample.crudrest.config;

import javax.sql.DataSource;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.ujar.starter.rest.logbook.LogbookJsonBodyFilter;
import org.ujar.starter.rest.logbook.LogbookResponseOnStatus;

@Configuration
// Use custom logbook strategy
@LogbookResponseOnStatus
// Remove CompactingJsonBodyFilter from logbook
@LogbookJsonBodyFilter
@EnableConfigurationProperties({SecurityProperties.class})
public class ApplicationConfig {

  @Bean
  public SpringLiquibase liquibase(@Autowired DataSource dataSource) {
    var liquibase = new SpringLiquibase();
    liquibase.setChangeLog("classpath:liquibase/changelog-master.xml");
    liquibase.setDataSource(dataSource);
    return liquibase;
  }
}
