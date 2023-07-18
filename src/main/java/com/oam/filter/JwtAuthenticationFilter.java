package com.oam.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oam.exception.ErrorMessage;
import com.oam.exception.model.InternalServerErrorException;
import com.oam.model.User;
import com.oam.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Date;

import static com.oam.util.SecurityConstants.*;
import static com.oam.util.SecurityConstants.SECRET_KEY_ENV;

@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final ApplicationContext context;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, ApplicationContext context) {
        this.authenticationManager = authenticationManager;
        this.context = context;

        setFilterProcessesUrl(SIGN_IN_URL);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) {
        try {
            User credentials = new ObjectMapper().readValue(req.getInputStream(), User.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            credentials.getEmail(),
                            credentials.getPassword()
                    )
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
                                            Authentication auth) throws IOException {
        String secretKey = System.getenv(SECRET_KEY_ENV);
        if (secretKey == null) {
            log.error("Missing environment variable: {}", SECRET_KEY_ENV);
            throw new InternalServerErrorException(ErrorMessage.INTERNAL_SERVER_ERROR);
        }

        UserService userService = context.getBean(UserService.class);
        User user = userService.getByEmail(((UserDetails) auth.getPrincipal()).getUsername());

        String token = JWT.create()
                .withSubject(user.getEmail())
                .withClaim(USER_ID, user.getId().toString())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(secretKey.getBytes()));

        res.setHeader("Authorization", token);
        res.setHeader("userId", user.getId().toString());
        res.getWriter().flush();
    }
}
