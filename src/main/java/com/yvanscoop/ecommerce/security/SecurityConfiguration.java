package com.yvanscoop.ecommerce.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Autowired
    public void globalConfiguration(AuthenticationManagerBuilder auth) throws Exception{
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("select username as principal, password as credentials,active from users where username=?")
                //.passwordEncoder(passwordEncoder())
                .authoritiesByUsernameQuery("select username as principal,role as role " +
                        "from users u,roles r,users_roles ur " +
                        "where u.id = ur.user_id and r.id = ur.roles_id and username=?")
                .rolePrefix("ROLE_")
                .passwordEncoder(NoOpPasswordEncoder.getInstance());
    }

    /*@Bean
    public BCryptPasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder();}*/


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll();
       // http.authorizeRequests().antMatchers("/produit/all").permitAll();
        http.formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/produits")
                .permitAll();
                //.failureUrl("/error.html");
        http.exceptionHandling().accessDeniedPage("/403");
    }
}
