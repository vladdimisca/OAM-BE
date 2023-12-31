package com.oam.service;

import com.oam.exception.ErrorMessage;
import com.oam.exception.model.ForbiddenException;
import com.oam.exception.model.InternalServerErrorException;
import com.oam.filter.CustomAuthenticationToken;
import com.oam.model.Role;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.oam.util.SecurityConstants.AUTHORITIES;
import static com.oam.util.SecurityConstants.USER_ID;

@Service
public class SecurityService {

    public void authorize(UUID userId) {
        if (!hasRequiredId(userId)) {
            throw new ForbiddenException(ErrorMessage.FORBIDDEN);
        }
    }

    public UUID getUserId() {
        return UUID.fromString(getCustomAuthentication().getClaims().get(USER_ID));
    }

    public boolean hasRole(Role userRole) {
        return getCustomAuthentication().getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_" + userRole.name()));
    }

    private boolean hasRequiredId(UUID userId) {
        return userId.toString().equals(getCustomAuthentication().getClaims().get(USER_ID));
    }

    private CustomAuthenticationToken getCustomAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof CustomAuthenticationToken)) {
            throw new InternalServerErrorException(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
        return (CustomAuthenticationToken) authentication;
    }
}
