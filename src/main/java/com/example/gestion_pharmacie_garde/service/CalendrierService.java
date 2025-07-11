package com.example.gestion_pharmacie_garde.service;

import com.example.gestion_pharmacie_garde.model.Calendrier;
import com.example.gestion_pharmacie_garde.model.Pharmacie;
import com.example.gestion_pharmacie_garde.repository.CalendrierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CalendrierService {

    @Autowired
    CalendrierRepository calendrierRepository;

    public Calendrier ajouteCalendrier(Calendrier calendrier) {
        return calendrierRepository.save(calendrier);
    }

    public Calendrier trouverParId(Long id) {
        return calendrierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Calendrier non trouvé avec l'id : " + id));
    }

    public Calendrier getById(Long id) {
        return calendrierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Calendrier introuvable pour l'ID : " + id));
    }

    public List<Calendrier> getAll() {
        return calendrierRepository.findAll();
    }
    public void supprimerParId(Long id) {
        calendrierRepository.deleteById(id);
    }

//    public Optional<Calendrier> findCalendrierByDateAndCommune(LocalDate date, String commune) {
//        return calendrierRepository.findByDateAndCommune(date, commune);
//    }

    public Optional<Calendrier> findCalendrierByDateAndCommune(LocalDate date, String commune) {
        List<Calendrier> calendriers = calendrierRepository.findCalendriersByDateAndCommune(date, commune);
        return calendriers.isEmpty() ? Optional.empty() : Optional.of(calendriers.get(0));
    }



}
