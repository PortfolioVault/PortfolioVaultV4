package com.example.testsql.views.company;

import com.example.testsql.models.Entreprise;
import com.example.testsql.models.Offre;

import com.example.testsql.services.company.OffreServiceEJB;
import com.example.testsql.services.user.UserServiceEJB;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.Map;


@Named
@ViewScoped
public class PostulationHomePageBean implements Serializable {

    @Inject
    private OffreServiceEJB offreService;

    @Inject
    private UserServiceEJB userServiceEJB;

    private Map<Offre, Entreprise> offreEntrepriseMap;

    public void fetchPostulationData() {
        offreEntrepriseMap = offreService.getAllOffresWithEntreprisesMap();
    }

    public Map<Offre, Entreprise> getOffreEntrepriseMap() {
        return offreEntrepriseMap;
    }

    public void setOffreEntrepriseMap(Map<Offre, Entreprise> offreEntrepriseMap) {
        this.offreEntrepriseMap = offreEntrepriseMap;
    }

    public void postuler(Offre offre) {
        userServiceEJB.postuler(offre);


    }
}

