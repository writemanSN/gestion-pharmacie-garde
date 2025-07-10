package com.example.gestion_pharmacie_garde.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Garde {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dateSemaine;

    @ElementCollection
    private List<Long> pharmaciesIds; // ou List<Pharmacie> si tu veux des objets complets

    @ManyToOne
    @JoinColumn(name = "calendrier_id")
    private Calendrier calendrier;
}
