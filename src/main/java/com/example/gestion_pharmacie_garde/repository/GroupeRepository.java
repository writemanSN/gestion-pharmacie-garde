package com.example.gestion_pharmacie_garde.repository;

import com.example.gestion_pharmacie_garde.model.Groupe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupeRepository extends JpaRepository<Groupe, Long> {
    List<Groupe> findByCalendrierId(Long calendrierId);
}
