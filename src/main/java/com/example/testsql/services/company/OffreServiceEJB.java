package com.example.testsql.services.company;

import com.example.testsql.models.Entreprise;
import com.example.testsql.models.Offre;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;

@Stateless
public class OffreServiceEJB {

    @PersistenceContext
    private EntityManager entityManager;


    public void addOffre(String email, Offre offre) {
        Entreprise entreprise = findCompnayByEmail(email);
        if (entreprise != null) {
            offre.setEntreprise(entreprise);
            entreprise.getOffres().add(offre);
            entityManager.merge(entreprise);
        }
    }

    public Entreprise findCompnayByEmail(String email) {
        TypedQuery<Entreprise> query = entityManager.createQuery("SELECT e FROM Entreprise e WHERE e.email = :email", Entreprise.class);
        query.setParameter("email", email);
        List<Entreprise> resultList = query.getResultList();
        return resultList.isEmpty() ? null : resultList.get(0);
    }
}
