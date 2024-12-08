package com.example.testsql.views.company;

import com.example.testsql.models.Entreprise;
import com.example.testsql.models.Offre;
import com.example.testsql.services.company.CompanyServiceEJB;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Named
@ViewScoped
public class homeCompanyPageBean implements Serializable {
    @Inject
    private CompanyServiceEJB companyServiceEJB;

    private Map<Offre, Entreprise> entreprisesWithOffres;

    public void fetchCompanyOffre() {
        entreprisesWithOffres = companyServiceEJB.getEntreprisesWithOffres();
    }

    public Map<Offre, Entreprise> getEntreprisesWithOffres() {
        return entreprisesWithOffres;
    }

    public void redirectToCreateOffre() {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        try {
            externalContext.redirect(externalContext.getRequestContextPath() + "/createOffre.xhtml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void redirectToViewOffres() {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        try {
            externalContext.redirect(externalContext.getRequestContextPath() + "/viewListOffres.xhtml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
