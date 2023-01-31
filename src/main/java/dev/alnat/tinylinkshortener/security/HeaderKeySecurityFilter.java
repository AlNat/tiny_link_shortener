package dev.alnat.tinylinkshortener.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.alnat.tinylinkshortener.dto.common.ResultFactory;
import dev.alnat.tinylinkshortener.mapper.UserMapper;
import dev.alnat.tinylinkshortener.security.model.UserAuth;
import dev.alnat.tinylinkshortener.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static dev.alnat.tinylinkshortener.config.Constants.AUTH_HEADER_NAME;

/**
 * Created by @author AlNat on 28.01.2023.
 * Licensed by Apache License, Version 2.0
 */
@Slf4j
@RequiredArgsConstructor
public class HeaderKeySecurityFilter extends OncePerRequestFilter {

    private final UserService userService;
    private final RequestMatcher allowedEndpoints;
    private final PasswordEncoder encoder;
    private final ObjectMapper mapper;
    private final UserMapper userMapper;


    @Override
    protected void doFilterInternal(HttpServletRequest servletRequest,
                                    HttpServletResponse servletResponse,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        if (allowedEndpoints.matches(servletRequest)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        final String key = servletRequest.getHeader(AUTH_HEADER_NAME);
        if (!StringUtils.hasText(key)){
            log.debug("Not passed header {} to request", AUTH_HEADER_NAME);
            constructResponse(servletResponse, mapper.writeValueAsString(ResultFactory.unauthorized()));
            return;
        }

        var user = userService.findByKey(encoder.encode(key));
        if (user.isEmpty()) {
            log.warn("Unauthorized request with [{}]:[{}]", AUTH_HEADER_NAME, key);
            constructResponse(servletResponse, mapper.writeValueAsString(ResultFactory.unauthorized()));
            return;
        }

        SecurityContextHolder.getContext().setAuthentication(new UserAuth(key, userMapper.entityToDTO(user.get())));
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private static void constructResponse(ServletResponse response,
                                         String body) throws IOException {
        ((HttpServletResponse)response).setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().println(body);
    }

}
