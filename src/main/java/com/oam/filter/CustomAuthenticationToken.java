package com.oam.filter;

import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Getter
public class CustomAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private final Map<String, String> claims = new HashMap<>();

    public CustomAuthenticationToken(Object principal, Map<String, String> claims) {
        super(principal, null, Collections.emptyList());

        this.claims.putAll(claims);
    }
}
