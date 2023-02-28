package com.hotel.security.filter;

import com.hotel.security.jwt.ValidateT;
import com.hotel.utils.ParseClaims;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
public class JwtValidationFilter extends OncePerRequestFilter {
    @Value("${jwt.s.secret}")
    private String TSecret;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {

        String jwt = request.getHeader(AUTHORIZATION);
        if (null != jwt) {
            String AccessToken = jwt.split(" ")[1].trim();

            if (!ValidateT.validate(AccessToken)) {
                //filterChain.doFilter(request, response);
                response.sendError(401, "Invalid credentials");
                return;
            }

            @SuppressWarnings("DuplicatedCode") Claims claims = ParseClaims.parseClaims(TSecret, AccessToken);
            @SuppressWarnings("DuplicatedCode") SecurityContext context = SecurityContextHolder.createEmptyContext();

            String username = String.valueOf(claims.get("username"));
            String authorities = (String) claims.get("authorities");
            Authentication auth = new UsernamePasswordAuthenticationToken(username, null,
                    AuthorityUtils.commaSeparatedStringToAuthorityList(authorities));

            context.setAuthentication(auth);
            SecurityContextHolder.setContext(context);
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getServletPath().equals("/api/v1/auth/login") ||
                request.getServletPath().equals("/api/v1/auth/register");
    }
}
