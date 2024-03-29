package com.automart.security;



import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationProvider;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(c ->c.disable())
                .authorizeHttpRequests(auth -> auth.requestMatchers("/api/v1/auth/**", "/h2-console/**","/api/v1/admin/**", "/v2/api-docs",
                                "/v3/api-docs",
                                "/v3/api-docs/**",
                                "/swagger-resources",
                                "/swagger-resources/**",
                                "/configuration/ui",
                                "/configuration/security",
                                "/swagger-ui/**",
                                "/webjars/**",
                                "/swagger-ui.html").permitAll()
                      //  .requestMatchers(new NegatedRequestMatcher(new AntPathRequestMatcher(("/api/v1/admin/**")))).hasAnyRole(ADMIN.name())

//                                .requestMatchers(GET, "/api/v1/admin/**").hasAuthority(ADMIN_READ.name())
//                                .requestMatchers(POST, "/api/v1/admin/**").hasAuthority(ADMIN_CREATE.name())
//                                .requestMatchers(PUT, "/api/v1/admin/**").hasAuthority(ADMIN_UPDATE.name())
//                                .requestMatchers(DELETE, "/api/v1/admin/**").hasAuthority(ADMIN_DELETE.name())
                        .anyRequest()
                        .authenticated())
             //   .authorizeHttpRequests(auth -> auth).anyRequest().authenticated())
//                .headers(h->h.frameOptions(frameOptionsConfig -> frameOptionsConfig.disable()))
                .sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();



        }


//    public void configure(WebSecurity web) {
//        web.ignoring().requestMatchers(new NegatedRequestMatcher(new AntPathRequestMatcher("/h2-console/**")));
//    }

//.authorizeHttpRequests(auth -> auth.requestMatchers(new NegatedRequestMatcher(new AntPathRequestMatcher( "/api/v1/auth/**","/h2-console/**")))

//
    }
