package com.example.gestion_pharmacie_garde.repository;

import com.example.gestion_pharmacie_garde.model.Pharmacie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PharmacieRepository extends JpaRepository<Pharmacie, Long> {
    List<Pharmacie> findByResponsableId(Long responsableId);
    List<Pharmacie> findByNomContainingIgnoreCase(String nom);
    List<Pharmacie> findByResponsable_Id(Long responsableId);

}
