package com.example.testsql.services;
import com.example.testsql.exceptions.EmailAlreadyTakenException;
import com.example.testsql.models.User;
import com.example.testsql.session.UserSession;
import jakarta.ejb.Stateful;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Objects;

@Stateful
public class UserServiceEJB {

    @PersistenceContext
    private EntityManager entityManager;



    public void registerUser(String firstName, String lastName, String email, String password) throws EmailAlreadyTakenException {
        User user = findUserByEmail(email);
        if (user != null) {
            // Check if a user with the same email exists
            throw new EmailAlreadyTakenException();
        } else {
            // Create a new User entity
            User newUser = new User(firstName, lastName, email);
            newUser.setPassword(password);

            // Persist the user entity to the database
            entityManager.persist(newUser);

            // Set the email in the user session
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

    @Inject
    private UserSession userSession;
}
