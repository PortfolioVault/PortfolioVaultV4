package com.example.testsql.services.company;
import com.example.testsql.exceptions.EmailAlreadyTakenException;
import com.example.testsql.models.Entreprise;
import com.example.testsql.models.User;
import com.example.testsql.session.CompanySession;
import jakarta.ejb.Stateful;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Objects;

@Stateful
public class CompanyServiceEJB {

    @PersistenceContext
    private EntityManager entityManager;

    public void registerCompany(String name, String email, String phone, String password) throws EmailAlreadyTakenException {
        Entreprise entreprise = findCompnayByEmail(email);
        if (entreprise != null) {
            throw new EmailAlreadyTakenException();
        } else {
            Entreprise newEntreprise = new Entreprise(name,email,password,phone);
            entityManager.persist(newEntreprise);
            companySession.setEmail(email);
        }
    }

    public Entreprise findCompnayByEmail(String email) {
        TypedQuery<Entreprise> query = entityManager.createQuery("SELECT e FROM Entreprise e WHERE e.email = :email", Entreprise.class);
        query.setParameter("email", email);
        List<Entreprise> resultList = query.getResultList();
        return resultList.isEmpty() ? null : resultList.get(0);
    }


    public boolean login(String email, String password) {
        Entreprise entreprise = findCompnayByEmail(email);
        boolean loginResult = false;
        if (entreprise != null && Objects.equals(entreprise.getPassword(), password)) {
            loginResult = true;
            companySession.setEmail(email);
        }
        return loginResult;
    }




    @Inject
    private CompanySession companySession;

    public void saveInfos(String email, String phone, String address, int nbrEmplyes, Entreprise.SecteurTravail secteurTravail) {
        TypedQuery<Entreprise> query = entityManager.createQuery("SELECT s FROM Entreprise s WHERE s.email = :email", Entreprise.class);
        query.setParameter("email", email);
        List<Entreprise> resultList = query.getResultList();

        if (!resultList.isEmpty()) {
            Entreprise entreprise = resultList.get(0);
            entreprise.setNbrEmplyes(nbrEmplyes);
            entreprise.setAddress(address);
            entreprise.setPhone(phone);
            entreprise.setSecteurTravail(String.valueOf(secteurTravail));

            entityManager.merge(entreprise);
        }
    }

    public List<Entreprise> getEntreprisesWithOffres() {
        TypedQuery<Entreprise> query = entityManager.createQuery("SELECT DISTINCT e FROM Entreprise e LEFT JOIN FETCH e.offres", Entreprise.class);
        return query.getResultList();
    }
}

