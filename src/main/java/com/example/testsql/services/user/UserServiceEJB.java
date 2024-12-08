package com.example.testsql.services.user;
import com.example.testsql.exceptions.EmailAlreadyTakenException;
import com.example.testsql.models.Offre;
import com.example.testsql.models.User;
import com.example.testsql.session.UserSession;
import jakarta.ejb.Stateful;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;

@Stateful
public class UserServiceEJB {

    @PersistenceContext
    private EntityManager entityManager;



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

    @Inject
    private UserSession userSession;

    public void postuler(Offre offre) {
        User user = findUserByEmail(userSession.getEmail());
        String pdfPath = "C:\\Users\\hp\\IdeaProjects\\stateful\\PortfolioVaultV4\\src\\main\\java\\com\\example\\testsql\\pdf\\" + user.getFirstName() + " " + user.getLastName() + ".pdf";
//envoyer dans JMS
        //TEST pour tester si il recuper lobjet ou pas
        String pdfTestFolderPath = "C:\\Users\\hp\\IdeaProjects\\stateful\\PortfolioVaultV4\\src\\main\\java\\com\\example\\testsql\\pdfTest\\";
        Path sourcePath = Paths.get(pdfPath);
        java.nio.file.Path targetPath = Paths.get(pdfTestFolderPath + user.getFirstName() + " " + user.getLastName() + ".pdf");

        try {
            Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);

            // Autres opérations liées à la postulation...
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
