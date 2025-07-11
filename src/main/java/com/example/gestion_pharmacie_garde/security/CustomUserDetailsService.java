package com.example.gestion_pharmacie_garde.security;

import com.example.gestion_pharmacie_garde.model.Responsable;
import com.example.gestion_pharmacie_garde.repository.ResponsableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private ResponsableRepository responsableRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Responsable user = responsableRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Responsable introuvable"));

        if (user.getEtatCompte() == null || !user.getEtatCompte()) {
            // Compte inactif
            throw new CompteInactiveException("Votre abonnement n'est pas actif. Veuillez le régler.");
        }

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getMotDePasse(),
                getAuthorities(user.getRole())
        );
    }

    private Collection<? extends GrantedAuthority> getAuthorities(String role) {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()));
    }
}
