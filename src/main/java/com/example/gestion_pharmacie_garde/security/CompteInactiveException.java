package com.example.gestion_pharmacie_garde.security;


import org.springframework.security.core.AuthenticationException;

public class CompteInactiveException extends AuthenticationException {
    public CompteInactiveException(String msg) {
        super(msg);
    }
}