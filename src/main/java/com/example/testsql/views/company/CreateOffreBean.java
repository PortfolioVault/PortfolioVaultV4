package com.example.testsql.views.company;

import com.example.testsql.models.Offre;
import com.example.testsql.services.company.CompanyServiceEJB;
import com.example.testsql.services.company.OffreServiceEJB;
import com.example.testsql.session.CompanySession;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Named
@ViewScoped
public class CreateOffreBean implements Serializable {
    @Inject
    private CompanyServiceEJB companyServiceEJB;

    @Inject
    private OffreServiceEJB offreServiceEJB;

    @Inject
    private CompanySession companySession;


    private Offre offre;
    private int salaire;
    private Offre.Type type;
    private Offre.SecteurTravail secteurTravail;
    private String description;
    private String role;
    private String niveauExperience;
    private List<Offre.Type> typeValues= Arrays.asList(Offre.Type.values());
    private List<Offre.SecteurTravail> secteurTravailValues= Arrays.asList(Offre.SecteurTravail.values());

    public int getSalaire() {
        return salaire;
    }

    public void setSalaire(int salaire) {
        this.salaire = salaire;
    }

    public Offre.Type getType() {
        return type;
    }

    public void setType(Offre.Type type) {
        this.type = type;
    }

    public Offre.SecteurTravail getSecteurTravail() {
        return secteurTravail;
    }

    public void setSecteurTravail(Offre.SecteurTravail secteurTravail) {
        this.secteurTravail = secteurTravail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getNiveauExperience() {
        return niveauExperience;
    }

    public void setNiveauExperience(String niveauExperience) {
        this.niveauExperience = niveauExperience;
    }

    public List<Offre.SecteurTravail> getSecteurTravailValues() {
        return secteurTravailValues;
    }

    public void setSecteurTravailValues(List<Offre.SecteurTravail> secteurTravailValues) {
        this.secteurTravailValues = secteurTravailValues;
    }

    public List<Offre.Type> getTypeValues() {
        return typeValues;
    }

    public void setTypeValues(List<Offre.Type> typeValues) {
        this.typeValues = typeValues;
    }

    public void createOffre() {
        offre=new Offre(salaire,niveauExperience,role,description,secteurTravail,type);

        if(description != null) {
            offreServiceEJB.addOffre(companySession.getEmail(), offre);

            FacesContext context = FacesContext.getCurrentInstance();
            ExternalContext externalContext = context.getExternalContext();
            try {
                externalContext.redirect(externalContext.getRequestContextPath() + "/homeCompanyPageBean.xhtml");
            } catch (IOException e) {
                e.printStackTrace();
            }
            salaire = 0;
            niveauExperience = "";
            description = "";
            role = "";
        }

    }
}
