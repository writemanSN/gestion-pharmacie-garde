package com.example.gestion_pharmacie_garde.dto;

import com.example.gestion_pharmacie_garde.model.Groupe;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LigneSemaine {
    private int numeroSemaine;

    private Groupe groupe;
    private LocalDate debutSemaine;
    private LocalDate finSemaine;
}
