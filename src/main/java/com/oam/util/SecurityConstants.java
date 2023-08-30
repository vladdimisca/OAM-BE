package com.oam.util;

public final class SecurityConstants {

    public static final String SECRET_KEY_ENV = "SECRET_KEY_ENV";
    public static final long EXPIRATION_TIME = 900_000_000;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String SIGN_UP_URL = "/api/users";
    public static final String FORGOT_PASSWORD_URL = "/api/users/*/forgotPassword";
    public static final String SIGN_IN_URL = "/api/users/login";
    public static final String USER_ID = "USER_ID";
    public static final String AUTHORITIES = "AUTHORITIES";
}
