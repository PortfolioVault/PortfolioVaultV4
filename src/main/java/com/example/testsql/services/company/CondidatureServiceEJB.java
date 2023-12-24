package com.example.testsql.services.company;

import com.example.testsql.models.Candidature;
import com.example.testsql.models.Offre;
import com.example.testsql.models.User;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class CondidatureServiceEJB {

    @PersistenceContext
    private EntityManager entityManager;

    public void addCandidature(User user, Offre offre) {
        Candidature candidature = new Candidature();

        candidature.setUser(user);
        candidature.setOffre(offre);

        entityManager.merge(user);
        entityManager.merge(offre);
        // Utilisez l'EntityManager pour persister la candidature dans la base de données
        entityManager.persist(candidature);

        // Après l'acceptation, vous pouvez effectuer d'autres actions si nécessaire

        // Vous pouvez également rediriger l'utilisateur vers une nouvelle page ou mettre à jour la vue
    }
}
