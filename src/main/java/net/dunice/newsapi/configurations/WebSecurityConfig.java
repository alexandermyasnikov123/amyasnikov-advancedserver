package net.dunice.newsapi.configurations;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.dunice.newsapi.security.JwtSecurityFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WebSecurityConfig {
    String[] permittedEndpoints = {"auth/**", "file/{path}"};

    String[] requireAuthEndpoints = {"file/uploadFile", "user/**"};

    @Bean
    public SecurityFilterChain getFilterChain(
            HttpSecurity http,
            JwtSecurityFilter filter,
            AuthenticationProvider provider
    ) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(customizer -> customizer
                        .requestMatchers(requireAuthEndpoints).authenticated()
                        .requestMatchers(permittedEndpoints).permitAll())
                .authenticationProvider(provider)
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }
}
