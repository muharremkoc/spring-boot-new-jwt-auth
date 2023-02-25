package com.security.springbootnewjwtauth.config;

import com.security.springbootnewjwtauth.jwt.JwtTokenFilter;
import com.security.springbootnewjwtauth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig{
    @Autowired
    private JwtTokenFilter jwtTokenFilter;

    @Autowired
    @Lazy
    private UserService userService;


    @Autowired
    private Environment environment;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.cors().and().csrf()
                .disable()
                .authorizeRequests()
                .antMatchers(environment.getProperty("auth.white-list",String[].class)).permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);


        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();


    }
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder)
            throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userService)
                .passwordEncoder(bCryptPasswordEncoder)
                .and()
                .build();
    }


    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.debug(false)
                .ignoring()
                .antMatchers(
                        "/api/v3/api-docs/swagger-config",
                        "/v3/api-docs/**",
                        "/v3/api-docs",
                        "/configuration/ui",
                        "/swagger-resources/**",
                        "/configuration/security",
                        "/swagger-ui.html",
                        "/swagger-ui/**",
                        "/swagger-config",
                        "/swagger-ui/index.html",
                        "/api/docs/**",
                        "/api/swagger-ui",
                        "/api/swagger-ui/**",
                        "/webjars/**",
                        "/api/versions/1/auth/login"
                );
    }

}
