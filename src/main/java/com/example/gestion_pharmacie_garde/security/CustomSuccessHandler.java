package com.example.gestion_pharmacie_garde.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {

        Collection<? extends GrantedAuthority> roles = authentication.getAuthorities();

        if (roles.stream().anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"))) {
            response.sendRedirect("/admin/accueil");
        } else if (roles.stream().anyMatch(r -> r.getAuthority().equals("ROLE_RESPONSABLE"))) {
            response.sendRedirect("/responsable/accueil");
        } else {
            response.sendRedirect("/login?erreur=true");
        }
    }
}

