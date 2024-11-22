package net.dunice.newsapi.configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
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
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Configuration
@EnableWebSecurity
public class WebSecurityConfig implements WebMvcConfigurer {
    EndpointsConfiguration endpointsConfiguration;

    ObjectMapper mapper;

    HandlerInterceptor loggerInterceptor;

    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        registry.addInterceptor(loggerInterceptor).addPathPatterns(endpointsConfiguration.getLoggingEndpoints());
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(
            EndpointsConfiguration endpointsConfiguration
    ) {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(endpointsConfiguration.getAllowedOrigins());
        configuration.setAllowedMethods(endpointsConfiguration.getAllowedMethods());
        configuration.setAllowedHeaders(endpointsConfiguration.getAllowedHeaders());
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(endpointsConfiguration.getCorsPattern(), configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain getFilterChain(
            HttpSecurity http,
            JwtAuthenticationFilter filter,
            AuthenticationProvider provider,
            CorsConfigurationSource corsConfigurationSource
    ) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(c -> c.configurationSource(corsConfigurationSource))
                .authorizeHttpRequests(customizer -> customizer
                        .requestMatchers(endpointsConfiguration.getPermittedAllEndpoints()).permitAll()
                        .requestMatchers(HttpMethod.GET, endpointsConfiguration.getPermittedGetEndpoints()).permitAll()
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
            String json = mapper.writeValueAsString(BaseSuccessResponse.error(ErrorCodes.UNAUTHORISED.getStatusCode()));
            response.getWriter().write(json);
        };
    }
}
