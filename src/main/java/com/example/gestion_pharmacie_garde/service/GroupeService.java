package com.example.gestion_pharmacie_garde.service;

import com.example.gestion_pharmacie_garde.model.Calendrier;
import com.example.gestion_pharmacie_garde.model.Groupe;
import com.example.gestion_pharmacie_garde.repository.CalendrierRepository;
import com.example.gestion_pharmacie_garde.repository.GroupeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupeService {

    @Autowired
    private GroupeRepository groupeRepository;

    @Autowired
    private CalendrierRepository CalendrierRepository;

    public Groupe ajouterGroupe(Groupe groupe) {
        return groupeRepository.save(groupe);
    }

    public Calendrier getById(Long id) {
        return CalendrierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Calendrier introuvable pour l'ID : " + id));
    }


    public List<Calendrier> getAll() {
        return CalendrierRepository.findAll();
    }

    public List<Groupe> findByCalendrierId(Long calendrierId) {
        return groupeRepository.findByCalendrierId(calendrierId);
    }

}
