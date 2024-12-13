package net.dunice.features.auth.configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dunice.features.auth.constants.CorsMappings;
import net.dunice.features.auth.constants.CorsDefaults;
import net.dunice.features.auth.security.JwtAuthenticationFilter;
import net.dunice.features.core.dtos.constants.ErrorCodes;
import net.dunice.features.core.dtos.responses.common.BaseSuccessResponse;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class WebSecurityConfig implements WebMvcConfigurer {
    private final ObjectMapper mapper;

    @Bean
    public CorsConfiguration getCorsConfiguration() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(CorsDefaults.ALLOWED_ORIGINS);
        configuration.setAllowedMethods(CorsDefaults.ALLOWED_METHODS);
        configuration.setAllowedHeaders(CorsDefaults.ALLOWED_HEADERS);
        configuration.setAllowCredentials(true);
        return configuration;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(
            CorsConfiguration corsConfiguration
    ) {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(CorsDefaults.PATTERN, corsConfiguration);
        return source;
    }

    @Bean
    public SecurityFilterChain getFilterChain(
            HttpSecurity http,
            JwtAuthenticationFilter filter,
            AuthenticationProvider provider,
            CorsConfigurationSource corsConfigurationSource
    ) throws Exception {
        RequestMatcher toH2 = PathRequest.toH2Console();
        return http
                .formLogin(FormLoginConfigurer::disable)
                .headers(configurer -> configurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .csrf(configurer -> configurer.ignoringRequestMatchers(toH2).disable())
                .cors(c -> c
                        .configurationSource(corsConfigurationSource))
                .authorizeHttpRequests(customizer -> customizer
                        .requestMatchers(toH2).permitAll()
                        .requestMatchers(CorsMappings.FULL_PERMITTED_ENDPOINTS).permitAll()
                        .requestMatchers(HttpMethod.GET, CorsMappings.PERMITTED_GET_ENDPOINTS).permitAll()
                        .requestMatchers(HttpMethod.POST, CorsMappings.PERMITTED_POST_ENDPOINT).permitAll()
                        .anyRequest().authenticated()
                )
                .authenticationProvider(provider)
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exc -> exc.authenticationEntryPoint(createEntryPoint()))
                .build();
    }

    private AuthenticationEntryPoint createEntryPoint() {
        return (request, response, exception) -> {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            String json = mapper.writeValueAsString(new BaseSuccessResponse(ErrorCodes.UNAUTHORISED.getStatusCode()));
            response.getWriter().write(json);
        };
    }
}

