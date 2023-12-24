package com.example.testsql.services.user;
import com.example.testsql.activeMQ.SimpleQueue;
import com.example.testsql.exceptions.EmailAlreadyTakenException;
import com.example.testsql.models.Education;
import com.example.testsql.models.Experience;
import com.example.testsql.models.Offre;
import com.example.testsql.models.User;
import com.example.testsql.session.UserSession;
import jakarta.ejb.Stateful;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Stateful
public class UserServiceEJB {

    @PersistenceContext
    private EntityManager entityManager;

    private static final String QUEUE_NAME = "pendingPostulat";

    public void registerUser(String firstName, String lastName, String email, String password) throws EmailAlreadyTakenException {
        User user = findUserByEmail(email);
        if (user != null) {
            throw new EmailAlreadyTakenException();
        } else {
            User newUser = new User(firstName, lastName, email);
            newUser.setPassword(password);

            entityManager.persist(newUser);

            userSession.setEmail(email);
        }
    }

    public User findUserByEmail(String email) {
        TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class);
        query.setParameter("email", email);
        List<User> resultList = query.getResultList();
        return resultList.isEmpty() ? null : resultList.get(0);
    }

    public boolean login(String email, String password) {
        User user = findUserByEmail(email);
        boolean loginResult = false;
        if (user != null && Objects.equals(user.getPassword(), password)) {
            loginResult = true;
            userSession.setEmail(email);
        }
        return loginResult;
    }

    public void savePersonalInfos(String email, String phoneNumber, String age, String professionalTitle, String address) {
        TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class);
        query.setParameter("email", email);
        List<User> resultList = query.getResultList();

        if (!resultList.isEmpty()) {
            User user = resultList.get(0);
            user.setProfessionalTitle(professionalTitle);
            user.setPhoneNumber(phoneNumber);
            user.setAge(age);
            user.setAddress(address);
            entityManager.merge(user);
        }
    }


    public List<Education> findEducationsByUserId(int userId) {
        TypedQuery<Education> query = entityManager.createQuery(
                "SELECT e FROM Education e WHERE e.user.id = :userId",
                Education.class
        );
        query.setParameter("userId", userId);

        return query.getResultList();
    }

    public List<Experience> findExperiencesByUserId(int userId) {
        TypedQuery<Experience> query = entityManager.createQuery(
                "SELECT exp FROM Experience exp WHERE exp.user.id = :userId",
                Experience.class
        );
        query.setParameter("userId", userId);

        return query.getResultList();
    }

    public User findUserByIdWithEducationsAndExperiences(String email) {
        TypedQuery<User> query = entityManager.createQuery(
                "SELECT u FROM User u LEFT JOIN FETCH u.educations LEFT JOIN FETCH u.experiences WHERE u.email = :email",
                User.class
        );
        query.setParameter("email", email);

        List<User> resultList = query.getResultList();

        return resultList.isEmpty() ? null : resultList.get(0);
    }


    @Inject
    private UserSession userSession;

    public void postuler(Offre offre) {
        User user = findUserByEmail(userSession.getEmail());

        try {
            // Convertir le nom et le prénom en octets
            byte[] nameBytes = (userSession.getEmail()+":"+offre.getId()).getBytes(StandardCharsets.UTF_8);

            // Envoyer le contenu combiné en tant que tableau d'octets
            SimpleQueue queue = new SimpleQueue(QUEUE_NAME);

            queue.send(nameBytes);
            queue.close();
        } catch (Exception e) {
            e.printStackTrace(); // Gérer les erreurs correctement dans un environnement de production
        }
    }

}
