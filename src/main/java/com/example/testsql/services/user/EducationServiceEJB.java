package com.example.testsql.services.user;
import com.example.testsql.models.Education;
import com.example.testsql.models.User;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;

@Stateless
public class EducationServiceEJB {

    @PersistenceContext
    private EntityManager entityManager;

    public void addEducation(String email, Education education) {
        User user = findUserByEmail(email);

        if (user != null) {
            education.setUser(user);
            user.getEducations().add(education);
            entityManager.merge(user);
        }
    }

    public List<Education> getEducation(String email) {
        // Find the User entity by email
        User user = findUserByEmail(email);
        if (user != null) {
            // Return the list of experiences from the user entity
            return  user.getEducations();
        } else {
            return null;
        }
    }

    private User findUserByEmail(String email) {
        TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class);
        query.setParameter("email", email);
        List<User> resultList = query.getResultList();
        return resultList.isEmpty() ? null : resultList.get(0);
    }
}
