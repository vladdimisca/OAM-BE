package com.oam.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.oam.exception.ErrorMessage;
import com.oam.exception.model.InternalServerErrorException;
import com.oam.model.User;
import com.oam.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.oam.exception.ErrorMessage.ACCOUNT_IS_BANNED;
import static com.oam.util.SecurityConstants.*;

@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final ApplicationContext context;

    public JwtAuthorizationFilter(AuthenticationManager authManager, ApplicationContext context) {
        super(authManager);
        this.context = context;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        String header = req.getHeader(AUTHORIZATION_HEADER);

        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            chain.doFilter(req, res);
            return;
        }

        SecurityContextHolder.getContext().setAuthentication(getAuthentication(req));
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION_HEADER);

        if (token != null) {
            String secretKey = System.getenv(SECRET_KEY_ENV);
            if (secretKey == null) {
                log.error("Missing environment variable: {}", SECRET_KEY_ENV);
                throw new InternalServerErrorException(ErrorMessage.INTERNAL_SERVER_ERROR);
            }

            DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(secretKey.getBytes()))
                    .build()
                    .verify(token.replace(TOKEN_PREFIX, ""));

            if (decodedJWT.getSubject() != null) {
                UUID userId = decodedJWT.getClaim(USER_ID).as(UUID.class);
                UserService userService = context.getBean(UserService.class);
                User user = userService.getById(userId);
                if (user.getIsBanned()) {
                    return null;
                }

                List<String> authorities = decodedJWT.getClaim(AUTHORITIES).asList(String.class);
                Map<String, String> claims = new HashMap<>();
                claims.put(USER_ID, userId.toString());

                return new CustomAuthenticationToken(decodedJWT.getSubject(), claims,
                        authorities.stream().map(authority -> (GrantedAuthority) () -> authority).toList());
            }
        }

        return null;
    }
}
