package com.example.gestion_pharmacie_garde.service;

import com.example.gestion_pharmacie_garde.model.Pharmacie;
import com.example.gestion_pharmacie_garde.model.Responsable;
import com.example.gestion_pharmacie_garde.repository.PharmacieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class PharmacieService {

    @Autowired
    private PharmacieRepository pharmacieRepository;

    public List<Pharmacie> getAll() {
        return pharmacieRepository.findAll();
    }

    public Pharmacie ajouterPharmacie(Pharmacie pharmacie) {
        return pharmacieRepository.save(pharmacie);
    }

    public void supprimerPharmacie(Long id) {
        pharmacieRepository.deleteById(id); // ✅ suffisant pour supprimer par ID
    }

    public Pharmacie trouverParId(Long id) {
        return pharmacieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pharmacie non trouvée avec l'id : " + id));
    }

    public Pharmacie getById(Long id) {
        return pharmacieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Calendrier introuvable pour l'ID : " + id));
    }

    public List<Pharmacie> getPharmaciesParResponsable(Responsable responsable) {
        return pharmacieRepository.findByResponsable(responsable);
    }





}
