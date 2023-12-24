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
    private String address;
    private String password;
    @Column(name = "num_telephone")
    private String phone;
    @OneToMany(mappedBy = "entreprise", cascade = CascadeType.ALL)
    private List<Offre> offres;

    @Enumerated(EnumType.STRING)
    @Column(name = "secteur_travail")
    private SecteurTravail secteurTravail;

    public enum SecteurTravail {
        INFORMATIQUE,MARKETING,FINANCE
    }
    public Entreprise() {

    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Entreprise(String name, String email, String password, String phone) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
    }

    public SecteurTravail getSecteurTravail() {
        return secteurTravail;
    }

    public void setSecteurTravail(String secteurTravail) {
        this.secteurTravail = SecteurTravail.valueOf(secteurTravail);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNbrEmplyes() {
        return nbrEmplyes;
    }

    public void setNbrEmplyes(int nbrEmplyes) {
        this.nbrEmplyes = nbrEmplyes;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Offre> getOffres() {
        return offres;
    }

    public void setOffres(List<Offre> offres) {
        this.offres = offres;
    }
}
