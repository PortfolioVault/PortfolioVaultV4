package com.example.testsql.services;
import com.example.testsql.models.Education;
import com.example.testsql.models.User;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;

@Stateless
public class EducationService {

    @PersistenceContext
    private EntityManager entityManager;

    public void addEducation(String email, String diplomat, String university, String yearOfObtention) {

        User user = findUserByEmail(email);
        if (user != null) {
            Education education = new Education(diplomat, university, yearOfObtention);

            // Définir la relation bidirectionnelle entre Education et User
            education.setUser(user);
            user.getEducations().add(education);
            entityManager.persist(education);
            entityManager.merge(user);

        }
    }

    public List<Education> getAllEducations(String email) {
        User user = findUserByEmail(email);
        if (user != null) {
            // Return the list of experiences from the user entity
            return  user.getEducations();
        } else {
            return null;
        }
//        TypedQuery<Education> query = entityManager.createQuery("SELECT e FROM Education e WHERE e.user.email = :email", Education.class);
//        query.setParameter("email", email);
//        return query.getResultList();
    }

    private User findUserByEmail(String email) {
        TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class);
        query.setParameter("email", email);
        List<User> resultList = query.getResultList();
        return resultList.isEmpty() ? null : resultList.get(0);
    }

    public void deleteById(String id) {
        // Find the Education entity by ID and remove it
        Education education = entityManager.find(Education.class, Long.parseLong(id));
        if (education != null) {
            entityManager.remove(education);
        }
    }
}
