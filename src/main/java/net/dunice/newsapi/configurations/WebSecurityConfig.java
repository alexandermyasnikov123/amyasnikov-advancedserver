package net.dunice.newsapi.configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dunice.newsapi.constants.AuthDefaults;
import net.dunice.newsapi.constants.CorsDefaults;
import net.dunice.newsapi.constants.ErrorCodes;
import net.dunice.newsapi.dtos.responses.common.BaseSuccessResponse;
import net.dunice.newsapi.security.JwtAuthenticationFilter;
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
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.util.List;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class WebSecurityConfig implements WebMvcConfigurer {
    private static final List<String> LOGGING_ENDPOINTS = List.of("/**");

    private final ObjectMapper mapper;

    private final HandlerInterceptor loggerInterceptor;

    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        registry.addInterceptor(loggerInterceptor).addPathPatterns(LOGGING_ENDPOINTS);
    }

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
        RequestMatcher toH2 = toH2Console();
        return http
                .formLogin(FormLoginConfigurer::disable)
                .headers(configurer -> configurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .csrf(configurer -> configurer.ignoringRequestMatchers(toH2).disable())
                .cors(c -> c
                        .configurationSource(corsConfigurationSource))
                .authorizeHttpRequests(customizer -> customizer
                        .requestMatchers(toH2).permitAll()
                        .requestMatchers(AuthDefaults.FULL_PERMITTED_ENDPOINTS).permitAll()
                        .requestMatchers(HttpMethod.GET, AuthDefaults.PERMITTED_GET_ENDPOINTS).permitAll()
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

