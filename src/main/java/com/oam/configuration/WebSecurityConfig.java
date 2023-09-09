package com.oam.configuration;

import com.oam.exception.ErrorMessage;
import com.oam.exception.model.NotFoundException;
import com.oam.filter.JwtAuthenticationFilter;
import com.oam.filter.JwtAuthorizationFilter;
import com.oam.model.Role;
import com.oam.model.User;
import com.oam.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collections;

import static com.oam.util.SecurityConstants.FORGOT_PASSWORD_URL;
import static com.oam.util.SecurityConstants.SIGN_UP_URL;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final UserRepository userRepository;
    private final ApplicationContext applicationContext;

    @Bean
    @Order(1)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors().and().csrf().disable()
            .authorizeHttpRequests((authorize) ->
                    {
                        try {
                            authorize
                                    .requestMatchers(
                                            antMatcher(HttpMethod.POST, SIGN_UP_URL),
                                            antMatcher(HttpMethod.POST, FORGOT_PASSWORD_URL),
                                            antMatcher(HttpMethod.POST, "/api/stripe/webhook")
                                    ).permitAll()
                                    .requestMatchers(
                                            antMatcher(HttpMethod.POST, "/api/users/*/ban"),
                                            antMatcher(HttpMethod.DELETE, "/api/users/*/ban"),
                                            antMatcher(HttpMethod.GET, "/api/invoices/statistics"),
                                            antMatcher(HttpMethod.GET, "/api/users")
                                    ).hasRole(Role.ADMIN.name())
                                    .anyRequest().authenticated();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }

            )
            .apply(customHttpConfigurer());
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration corsConfiguration = new CorsConfiguration().applyPermitDefaultValues();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return email -> {
            User user = userRepository
                    .findByEmail(email)
                    .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND, "user", email));

            return org.springframework.security.core.userdetails.User
                .withUsername(email)
                .password(user.getPassword())
                .authorities(Collections.emptyList())
                .build();
        };
    }

    @Bean
    @Order(0)
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CustomHttpConfigurer customHttpConfigurer() {
        return new CustomHttpConfigurer();
    }

    private class CustomHttpConfigurer extends AbstractHttpConfigurer<CustomHttpConfigurer, HttpSecurity> {
        @Override
        public void configure(HttpSecurity http) {
            AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
            http
                .addFilter(new JwtAuthenticationFilter(authenticationManager, applicationContext))
                .addFilter(new JwtAuthorizationFilter(authenticationManager, applicationContext));
        }
    }
}
