package com.example.gestion_pharmacie_garde.repository;

import com.example.gestion_pharmacie_garde.model.Calendrier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CalendrierRepository extends JpaRepository<Calendrier, Long> {
    Optional<Calendrier> findCalendrierById(long id);
    @Query("SELECT c FROM Calendrier c WHERE :date BETWEEN c.dateDebut AND c.dateFin AND c.responsable.commune = :commune")
    List<Calendrier> findCalendriersByDateAndCommune(@Param("date") LocalDate date, @Param("commune") String commune);


}
