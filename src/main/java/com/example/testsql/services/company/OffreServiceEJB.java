package com.example.testsql.services.company;

import com.example.testsql.models.Entreprise;
import com.example.testsql.models.Offre;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public Map<Offre, Entreprise> getAllOffresWithEntreprisesMap() {
        String jpql = "SELECT o, e FROM Offre o JOIN o.entreprise e";
        List<Object[]> resultList = entityManager.createQuery(jpql, Object[].class).getResultList();

        Map<Offre, Entreprise> offreEntrepriseMap = new HashMap<>();
        for (Object[] result : resultList) {
            Offre offre = (Offre) result[0];
            Entreprise entreprise = (Entreprise) result[1];
            offreEntrepriseMap.put(offre, entreprise);
        }

        return offreEntrepriseMap;
    }

    public Offre findOffreById(Long id) {
        TypedQuery<Offre> query = entityManager.createQuery("SELECT e FROM Offre e WHERE e.id = :id", Offre.class);
        query.setParameter("id", id);
        return  query.getResultList().get(0);
    }
}

