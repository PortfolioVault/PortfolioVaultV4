package com.example.testsql.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "entreprise")
public class Entreprise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Column(name = "nombres_employes")
    private int nbrEmplyes;
    private String email;
    private String password;
    @Column(name = "num_telephone")
    private String phone;
    @OneToMany(mappedBy = "entreprise", cascade = CascadeType.ALL)
    private List<Offre> offres;

    public enum secteurTravail {
        INFORMATIQUE,MARKETING,FINANCE
    }


}
