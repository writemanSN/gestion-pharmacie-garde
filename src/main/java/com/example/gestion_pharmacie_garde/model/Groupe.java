package com.example.gestion_pharmacie_garde.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Groupe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer numero; // Groupe 1, Groupe 2...

    @ManyToOne
    @JoinColumn(name = "calendrier_id")
    private Calendrier calendrier;

    @ManyToMany
    private List<Pharmacie> pharmacies;
}
