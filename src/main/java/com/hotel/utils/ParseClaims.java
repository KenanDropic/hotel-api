package com.hotel.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class ParseClaims {

    public static Claims parseClaims(String secret, String value) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(value)
                .getBody();
    }
}
