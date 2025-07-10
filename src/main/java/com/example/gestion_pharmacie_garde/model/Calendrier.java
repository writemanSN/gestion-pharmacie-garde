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
public class Calendrier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private Integer nombreGroupes;

    @ManyToOne
    @JoinColumn(name = "responsable_id")
    private Responsable responsable;

    @OneToMany(mappedBy = "calendrier", cascade = CascadeType.ALL)
    private List<Groupe> groupes;
}
