package dev.alnat.tinylinkshortener.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.alnat.tinylinkshortener.mapper.UserMapper;
import dev.alnat.tinylinkshortener.security.HeaderKeySecurityFilter;
import dev.alnat.tinylinkshortener.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.savedrequest.NullRequestCache;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private static final RequestMatcher NOT_AUTH_ENDPOINTS = new OrRequestMatcher(
        new AntPathRequestMatcher("/s/**"), // redirects
        new AntPathRequestMatcher("/**/swagger-resources/**"),
        new AntPathRequestMatcher("/**/swagger-ui.html/**"),
        new AntPathRequestMatcher("/**/swagger-ui/**"),
        new AntPathRequestMatcher("/**/swagger-ui.html"),
        new AntPathRequestMatcher("/favicon.ico"),
        new AntPathRequestMatcher("/webjars/**"),
        new AntPathRequestMatcher("/v3/api-docs/**"),
        new AntPathRequestMatcher("/v3/api-docs"),
        new AntPathRequestMatcher("/actuator/**")
    );

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public HeaderKeySecurityFilter securityFilter(final UserService service,
                                                  final PasswordEncoder encoder,
                                                  final ObjectMapper mapper,
                                                  final UserMapper userMapper) {
        return new HeaderKeySecurityFilter(service, NOT_AUTH_ENDPOINTS, encoder, mapper, userMapper);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, HeaderKeySecurityFilter filter) throws Exception {
        return http
                .addFilterBefore(filter, BasicAuthenticationFilter.class)
                .requestCache().requestCache(new NullRequestCache()) // Disable caching
                .and()
                .csrf().disable()
                .authorizeRequests().requestMatchers(NOT_AUTH_ENDPOINTS).permitAll()
                .anyRequest().authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .build();
    }
}
