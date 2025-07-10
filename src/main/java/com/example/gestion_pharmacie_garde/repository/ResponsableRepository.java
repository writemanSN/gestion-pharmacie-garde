package com.example.gestion_pharmacie_garde.repository;


import com.example.gestion_pharmacie_garde.model.Responsable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface ResponsableRepository extends JpaRepository<Responsable, Long> {
    Optional<Responsable> findByEmail(String email);
    List<Responsable> findByRole(String role);
    List<Responsable> findByCommune(String commune);
    boolean existsByEmail(String email);
}
