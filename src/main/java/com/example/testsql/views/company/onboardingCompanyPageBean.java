package com.example.testsql.views.company;

import com.example.testsql.models.*;
import com.example.testsql.services.company.CompanyServiceEJB;
import com.example.testsql.session.CompanySession;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Named
@ViewScoped
public class onboardingCompanyPageBean implements Serializable {
    @Inject
    private CompanyServiceEJB companyServiceEJB;

    @Inject
    private CompanySession companySession;

    private String name;
    private String email;
    private String phone;
    private String address;
    private int nbrEmplyes;

    private List<Entreprise.SecteurTravail> secteurTravailValues= Arrays.asList(Entreprise.SecteurTravail.values());
    private Entreprise.SecteurTravail selectedSecteurTravail;


    private LinkedList<Offre> offres = new LinkedList<>();

    public void fetchCompany() {
        // Initialize properties using values from UserSessionBean
        Entreprise entreprise = companyServiceEJB.findCompnayByEmail(companySession.getEmail());
        this.name = entreprise.getName();
        this.email = entreprise.getEmail();
        this.phone = entreprise.getPhone();
        this.address = entreprise.getAddress() != null ? entreprise.getAddress() : "";
//        this.age = user.getAge() != null ? user.getAge() : "";
        this.nbrEmplyes = entreprise.getNbrEmplyes() != 0 ? entreprise.getNbrEmplyes() : 0;
//        this.phoneNumber = user.getPhoneNumber() != null ? user.getPhoneNumber() : "";
//        this.experiences = experienceServiceEJB.getExperiences(userSession.getEmail());
    }

    public void logout(){
        companySession.setEmail("");
        this.email = "";
        this.name = "";
        this.email = "";
        this.address = "";
        this.phone = "";
        this.nbrEmplyes = 0;
        this.offres = null;
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        try {
            externalContext.redirect(externalContext.getRequestContextPath() + "/signupCompany.xhtml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveInfos(){
        FacesContext context = FacesContext.getCurrentInstance();
        try{
            companyServiceEJB.saveInfos(companySession.getEmail(), phone,address,nbrEmplyes,selectedSecteurTravail);
            FacesMessage message = new FacesMessage("Success", "Infos saved successfully");
            context.addMessage(null, message);
        }catch (Exception exception){
            FacesMessage message = new FacesMessage("Something went wrong", "An error has occured");
            context.addMessage(null, message);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getNbrEmplyes() {
        return nbrEmplyes;
    }

    public void setNbrEmplyes(int nbrEmplyes) {
        this.nbrEmplyes = nbrEmplyes;
    }

    public LinkedList<Offre> getOffres() {
        return offres;
    }

    public void setOffres(LinkedList<Offre> offres) {
        this.offres = offres;
    }

    public List<Entreprise.SecteurTravail> getSecteurTravailValues() {
        return secteurTravailValues;
    }

    public void setSecteurTravailValues(List<Entreprise.SecteurTravail> secteurTravailValues) {
        this.secteurTravailValues = secteurTravailValues;
    }

    public Entreprise.SecteurTravail getSelectedSecteurTravail() {
        return selectedSecteurTravail;
    }

    public void setSelectedSecteurTravail(Entreprise.SecteurTravail selectedSecteurTravail) {
        this.selectedSecteurTravail = selectedSecteurTravail;
    }

    public void redirectToCreateOffre() {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        try {
            externalContext.redirect(externalContext.getRequestContextPath() + "/createOffre.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void redirectToHomePage() {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        try {
            externalContext.redirect(externalContext.getRequestContextPath() + "/homeCompanyPage.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
