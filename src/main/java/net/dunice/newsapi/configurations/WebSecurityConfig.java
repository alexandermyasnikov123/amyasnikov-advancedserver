package net.dunice.newsapi.configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.dunice.newsapi.constants.ErrorCodes;
import net.dunice.newsapi.dtos.responses.common.BaseSuccessResponse;
import net.dunice.newsapi.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Value("${permitted-endpoints}")
    private String[] permittedEndpoints;

    @Autowired
    private ObjectMapper mapper;

    @Bean
    public SecurityFilterChain getFilterChain(
            HttpSecurity http,
            JwtAuthenticationFilter filter,
            AuthenticationProvider provider
    ) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(customizer -> customizer
                        .requestMatchers(permittedEndpoints).permitAll()
                        .anyRequest().authenticated())
                .authenticationProvider(provider)
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exc -> exc.authenticationEntryPoint(createEntryPoint()))
                .build();
    }

    private AuthenticationEntryPoint createEntryPoint() {
        return (request, response, exception) -> {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            String json = mapper.writeValueAsString(BaseSuccessResponse.error(ErrorCodes.UNAUTHORISED.getStatusCode()));
            response.getWriter().write(json);
        };
    }
}
