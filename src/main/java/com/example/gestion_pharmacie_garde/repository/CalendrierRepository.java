package com.example.gestion_pharmacie_garde.repository;

import com.example.gestion_pharmacie_garde.model.Calendrier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CalendrierRepository extends JpaRepository<Calendrier, Long> {
    Optional<Calendrier> findCalendrierById(long id);
}
