package com.example.gestion_pharmacie_garde.repository;

import com.example.gestion_pharmacie_garde.model.CodeSecret;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CodeSecretRespository extends JpaRepository<CodeSecret, Long> {
    boolean existsByCode(String code);
}
