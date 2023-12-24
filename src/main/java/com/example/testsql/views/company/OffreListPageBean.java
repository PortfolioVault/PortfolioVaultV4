package com.example.testsql.views.company;

import com.example.testsql.models.Entreprise;
import com.example.testsql.models.Offre;
import com.example.testsql.services.company.CompanyServiceEJB;
import com.example.testsql.services.company.OffreServiceEJB;
import com.example.testsql.session.CompanySession;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class OffreListPageBean implements Serializable {

    private String email; // Email de l'entreprise, peut être passé en paramètre ou récupéré autrement
    private Entreprise entreprise;
    private List<Offre> offres;

    @Inject
    private OffreServiceEJB offreService; // Assurez-vous d'avoir un service pour gérer les offres
    @Inject
    private CompanyServiceEJB companyServiceEJB;
    @Inject
    private CompanySession companySession;

    public void loadEntrepriseData() {
        // Charger les données de l'entreprise et ses offres

        entreprise = companyServiceEJB.findCompnayByEmail(companySession.getEmail());
        offres = entreprise.getOffres();
    }

    public void redirectToCandidatures(Offre offre) {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        try {
            externalContext.redirect(externalContext.getRequestContextPath() + "/createOffre.xhtml");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Entreprise getEntreprise() {
        return entreprise;
    }

    public void setEntreprise(Entreprise entreprise) {
        this.entreprise = entreprise;
    }

    public List<Offre> getOffres() {
        return offres;
    }

    public void setOffres(List<Offre> offres) {
        this.offres = offres;
    }
}

