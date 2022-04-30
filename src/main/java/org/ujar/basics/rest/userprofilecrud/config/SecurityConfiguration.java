package org.ujar.basics.rest.userprofilecrud.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableGlobalMethodSecurity(securedEnabled = true)
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  private static final String EDITOR = "EDITOR";
  public static final String ROLE_EDITOR = "ROLE_" + EDITOR;
  private static final String ADMIN = "ADMIN";
  public static final String ROLE_ADMIN = "ROLE_" + ADMIN;

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder authenticationManagerBuilder,
                              SecurityProperties properties) throws Exception {
    var usersToCredentials = properties.getUsers();
    var editorUser = usersToCredentials.get("editor");
    var adminUser = usersToCredentials.get("admin");
    authenticationManagerBuilder.inMemoryAuthentication()
        .withUser(editorUser.getName()).password("{noop}" + editorUser.getPassword()).roles(EDITOR)
        .and()
        .withUser(adminUser.getName()).password("{noop}" + adminUser.getPassword()).roles(ADMIN);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .cors().and().csrf().disable()
        .authorizeRequests()
        .antMatchers("/**").permitAll()
        .and()
        .httpBasic();
  }
}
