package com.example.testsql.models;
import jakarta.persistence.*;

@Entity
public class Education {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String diplomat;

    private String university;

    @Column(name = "year_of_obtention")
    private String yearOfObtention;

    @ManyToOne
    @JoinColumn(name = "user_id" ,referencedColumnName = "ID")
    private User user;




    public Education(String diplomat, String university, String yearOfObtention) {
        this.diplomat = diplomat;
        this.university = university;
        this.yearOfObtention = yearOfObtention;
//        this.userId = idUser;
    }

    public Education() {
    }

    public String getDiplomat() {
        return diplomat;
    }

    public void setDiplomat(String diplomat) {
        this.diplomat = diplomat;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getYearOfObtention() {
        return yearOfObtention;
    }

    public void setYearOfObtention(String yearOfObtention) {
        this.yearOfObtention = yearOfObtention;
    }

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }
}