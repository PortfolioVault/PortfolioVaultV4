package com.example.testsql.models;

import jakarta.persistence.*;

@Entity
public class Offre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    public enum etatOffre {
        ATTENTE,ACCEPTEE,REJETEE
    }
    private int salaire;
    @Column(name = "niveau_experience")
    private String niveauExperience;
    private String role;
    private String description;

    @ManyToOne
    @JoinColumn(name = "entreprise_id")
    private Entreprise entreprise;

    public enum type {
        STAGE,AMBAUCHE
    }
    public enum secteurTravail {
        INFORMATIQUE,MARKETING,FINANCE
    }

}
