package com.example.testsql.views;

import com.example.testsql.models.Experience;
import com.example.testsql.services.ExperienceServiceEJB;
import com.example.testsql.services.UserServiceEJB;
import com.example.testsql.session.UserSession;
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
public class ExperienceBean implements Serializable {

    @Inject
    private UserServiceEJB userServiceEJB;
    @Inject
    private ExperienceServiceEJB experienceServiceEJB;
    @Inject
    private UserSession userSession;

    private Experience experience;

    private String startDate;
    private String endDate;
    private String company;
    private String role;



    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String ajouterExperience() {
        experience=new Experience(startDate,endDate,company,role);

        //ajouter les données dans la database
        experienceServiceEJB.addExperience(userSession.getEmail(), experience);

        //redirection
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        try{
            externalContext.redirect(externalContext.getRequestContextPath() + "/home.xhtml");
        }catch(IOException e){
            e.printStackTrace();
        }
        startDate = "";
        endDate = "";
        company = "";
        role = "";
        return null;

    }

    public List<Experience> getAllExperiences(){
        return experienceServiceEJB.getExperiences(userSession.getEmail());
    }
}
