package com.example.testsql.services.user;
import com.example.testsql.models.Experience;
import com.example.testsql.models.User;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.logging.Logger;


@Stateless
public class ExperienceServiceEJB {

    @PersistenceContext
    private EntityManager entityManager;

    private Logger logger = Logger.getLogger(String.valueOf(ExperienceServiceEJB.class));

    public void addExperience(String email, Experience experience) {
        User user = findUserByEmail(email);

        if (user != null) {
            experience.setUser(user);
            user.getExperiences().add(experience);
            entityManager.merge(user);
        }
    }

    public List<Experience> getExperiences(String email) {
        User user = findUserByEmail(email);
        if (user != null) {
            return  user.getExperiences();
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
