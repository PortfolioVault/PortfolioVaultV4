package com.example.testsql.views.company;

import com.example.testsql.models.Entreprise;
import com.example.testsql.services.company.CompanyServiceEJB;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class homeCompanyPageBean implements Serializable {
    @Inject
    private CompanyServiceEJB companyServiceEJB;

    private List<Entreprise> entreprisesWithOffres;

    public void fetchCompanyOffre() {
        entreprisesWithOffres = companyServiceEJB.getEntreprisesWithOffres();
    }

    public List<Entreprise> getEntreprisesWithOffres() {
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
}
