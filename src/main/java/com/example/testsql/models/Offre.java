package com.example.testsql.models;

import jakarta.persistence.*;

@Entity
public class Offre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Offre() {

    }

    public enum etatOffre {
        ATTENTE,ACCEPTEE,REJETEE
    }
    private int salaire;
    @Column(name = "niveau_experience")
    private String niveauExperience;
    private String role;
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "secteur_travail")
    private SecteurTravail secteurTravail;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private Type type;

    @ManyToOne
    @JoinColumn(name = "entreprise_id")
    private Entreprise entreprise;

    public enum Type {
        STAGE,AMBAUCHE
    }
    public enum SecteurTravail {
        INFORMATIQUE,MARKETING,FINANCE
    }

    public Offre(int salaire, String niveauExperience, String role, String description, SecteurTravail secteurTravail, Type type) {
        this.salaire = salaire;
        this.niveauExperience = niveauExperience;
        this.role = role;
        this.description = description;
        this.secteurTravail = secteurTravail;
        this.type = type;
        this.entreprise = entreprise;
    }

    public int getSalaire() {
        return salaire;
    }

    public void setSalaire(int salaire) {
        this.salaire = salaire;
    }

    public String getNiveauExperience() {
        return niveauExperience;
    }

    public void setNiveauExperience(String niveauExperience) {
        this.niveauExperience = niveauExperience;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SecteurTravail getSecteurTravail() {
        return secteurTravail;
    }

    public void setSecteurTravail(SecteurTravail secteurTravail) {
        this.secteurTravail = secteurTravail;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Entreprise getEntreprise() {
        return entreprise;
    }

    public void setEntreprise(Entreprise entreprise) {
        this.entreprise = entreprise;
    }
}
