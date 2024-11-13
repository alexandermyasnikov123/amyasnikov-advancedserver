package net.dunice.newsapi.configurations;

import net.dunice.newsapi.controllers.AuthController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import java.util.stream.Stream;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    private final String[] permittedEndpoints = Stream.of(
            AuthController.ENDPOINT, "file"
            )
            .map("%s/**"::formatted)
            .toArray(String[]::new);

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(customizer -> customizer.requestMatchers(permittedEndpoints).permitAll())
                .build();
    }
}
