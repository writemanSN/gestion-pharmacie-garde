package com.example.gestion_pharmacie_garde.service;

import com.example.gestion_pharmacie_garde.model.Responsable;
import com.example.gestion_pharmacie_garde.repository.ResponsableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ResponsableService {
    @Autowired
    private ResponsableRepository responsableRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public Responsable AjouterResponsable(Responsable responsable) {
        return responsableRepository.save(responsable);
    }


    public boolean emailExiste(String email) {
        return responsableRepository.existsByEmail(email);
    }

    public Optional<Responsable> RechercherByEmail(String email) {
        return responsableRepository.findByEmail(email);
    }

    public void MettreAJourResponsable(Responsable responsable) {
        Responsable existant = responsableRepository.findById(responsable.getId()).orElse(null);

        if (existant != null) {
            existant.setEmail(responsable.getEmail());
            existant.setTelephone(responsable.getTelephone());
            existant.setCommune(responsable.getCommune());

            // Met à jour le mot de passe seulement si le champ n'est pas vide
//            if (responsable.getMotDePasse() != null && !responsable.getMotDePasse().isEmpty()) {
//                existant.setMotDePasse(passwordEncoder.encode(responsable.getMotDePasse()));
//            }

            responsableRepository.save(existant);
        }
    }


    public List<Responsable> getResponsablesParRole(String role) {
        return responsableRepository.findByRole(role);
    }

    public List<Responsable> getResponsablesParCommune(String commune) {
        return responsableRepository.findByCommune(commune);
    }

    public List<String> findAllCommunes() {
        return responsableRepository.findDistinctCommunes();
    }



}
