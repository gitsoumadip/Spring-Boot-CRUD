package com.example.helloworld.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * ==========================================================================================
 * SECURITY CONFIGURATION
 * ==========================================================================================
 * CURRENT STATUS:
 * - Authentication is DISABLED.
 * - All APIs can be accessed freely WITHOUT any JWT token or credentials.
 * - Suitable for frontend/backend integration and testing.
 *
 * ==========================================================================================
 * HOW TO RE-ENABLE JWT & KEYCLOAK IN THE FUTURE:
 * ==========================================================================================
 * 
 * 1. RUN KEYCLOAK:
 * - Start Keycloak server (e.g., http://localhost:8080).
 * - Create a Realm (e.g., "myrealm"), a Client, and Roles ("ADMIN", "OFFICER").
 *
 * 2. CONFIGURE application.properties:
 * Uncomment the following line with your Keycloak realm URL:
 * spring.security.oauth2.resourceserver.jwt.issuer-uri=http://localhost:8080/realms/myrealm
 *
 * 3. UPDATE securityFilterChain() IN THIS CLASS:
 * Replace the current authorizeHttpRequests block with:
 *
 * http
 * .csrf(csrf -> csrf.disable())
 * .sessionManagement(session ->
 * session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
 * .authorizeHttpRequests(auth -> auth
 * .requestMatchers("/api/auth/**", "/actuator/health", "/db-test").permitAll()
 * .requestMatchers(HttpMethod.GET, "/api/users/**").hasAnyRole("ADMIN",
 * "OFFICER")
 * .requestMatchers(HttpMethod.POST, "/api/users/**").hasRole("ADMIN")
 * .requestMatchers(HttpMethod.PUT, "/api/users/**").hasRole("ADMIN")
 * .requestMatchers(HttpMethod.DELETE, "/api/users/**").hasRole("ADMIN")
 * .anyRequest().authenticated()
 * )
 * .oauth2ResourceServer(oauth2 -> oauth2
 * .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
 * );
 *
 * 4. ADD KEYCLOAK ROLE CONVERTER BEAN:
 * Uncomment or add the following methods:
 *
 * @Bean
 *       public JwtAuthenticationConverter jwtAuthenticationConverter() {
 *       JwtAuthenticationConverter converter = new
 *       JwtAuthenticationConverter();
 *       converter.setJwtGrantedAuthoritiesConverter(new
 *       KeycloakRealmRoleConverter());
 *       return converter;
 *       }
 *
 *       static class KeycloakRealmRoleConverter implements Converter<Jwt,
 *       Collection<GrantedAuthority>> {
 * @Override
 *           @SuppressWarnings("unchecked")
 *           public Collection<GrantedAuthority> convert(Jwt jwt) {
 *           Map<String, Object> realmAccess =
 *           jwt.getClaimAsMap("realm_access");
 *           if (realmAccess == null || realmAccess.isEmpty()) {
 *           return Collections.emptyList();
 *           }
 *           List<String> roles = (List<String>) realmAccess.get("roles");
 *           if (roles == null) {
 *           return Collections.emptyList();
 *           }
 *           return roles.stream()
 *           .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
 *           .collect(Collectors.toList());
 *           }
 *           }
 *           ==========================================================================================
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF for REST APIs
                .csrf(csrf -> csrf.disable())

                // Disable HTTP Basic authentication popup
                .httpBasic(basic -> basic.disable())

                // Disable default form login redirect
                .formLogin(form -> form.disable())

                // Stateless session management
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Permit ALL API endpoints without any token / authentication
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll());

        return http.build();
    }
}
