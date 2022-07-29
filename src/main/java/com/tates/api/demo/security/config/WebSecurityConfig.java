package com.tates.api.demo.security.config;

import com.tates.api.demo.security.filters.CustomAuthenticationFilter;
import com.tates.api.demo.security.filters.CustomAuthorizationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.ArrayList;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(inMemoryUserDetailsManager());
    }
    @Bean
    protected InMemoryUserDetailsManager inMemoryUserDetailsManager(){
        ArrayList<UserDetails> userDetails = new ArrayList<>();
        userDetails.add(User
                .withUsername("tilltates")
                .password(passwordEncoder().encode("tilltates"))
                .roles("ADMIN")
                .build()
        );
        userDetails.add(User
                .withUsername("clairetates")
                .password(passwordEncoder().encode("clairetates"))
                .roles("USER")
                .build()
        );
        return new InMemoryUserDetailsManager(userDetails);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(STATELESS);
        http.csrf().disable().authorizeRequests()
                .antMatchers("/api/v1/login/tokens/**", "/api/v1/login/refresh_token/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/**").hasAnyRole("ADMIN", "USER")
                .antMatchers(HttpMethod.POST, "/api/v1/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/v1/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/v1/**").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .addFilter(new CustomAuthenticationFilter("/api/v1/login/tokens", authenticationManagerBean()))
                .addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                //.addFilterBefore(new JWTLoginFilter("api/v1/login", authenticationManager()), UsernamePasswordAuthenticationFilter.class)
               ;// .addFilterBefore(new JWTAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }
    @Bean
    protected BCryptPasswordEncoder passwordEncoder(){
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder;
    }
}
