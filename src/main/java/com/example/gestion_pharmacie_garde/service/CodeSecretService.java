package com.example.gestion_pharmacie_garde.service;

import com.example.gestion_pharmacie_garde.repository.CodeSecretRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CodeSecretService {

    @Autowired
    private CodeSecretRespository codeSecretRespository;

    public boolean codeExiste(String code) {
        return codeSecretRespository.existsByCode(code);
    }
}
