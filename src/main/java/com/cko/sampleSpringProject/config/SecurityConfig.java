package com.cko.sampleSpringProject.config;

import com.cko.sampleSpringProject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
//@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;

    @Autowired
    private FailureHandler failureHandler;

    @Autowired
    private SuccessRedirectHandler successRedirectHandler;

    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("root@mail.ru").password(getEncoder().encode("root")).roles("ADMIN");
        auth.userDetailsService(userService).passwordEncoder(getEncoder());
    }

    @Bean
    public BCryptPasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.headers().frameOptions().disable();
        http.csrf().disable();

        http.authorizeRequests()
                .antMatchers("/**").permitAll()
//                .antMatchers("/css/*","/js/*", "/img/*").permitAll()
//                .antMatchers("/authenticate").permitAll()
//                .antMatchers("/registration").permitAll()
                .and()
                .formLogin()
                .loginPage("/loginPage")
                .loginProcessingUrl("/authenticate").passwordParameter("password").usernameParameter("email")
                .permitAll().successHandler(successRedirectHandler).failureHandler(failureHandler)
                .and()
                .logout()
                .and()
                .exceptionHandling().accessDeniedPage("/access-denied");
    }
}


