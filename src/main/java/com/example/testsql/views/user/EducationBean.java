package com.example.testsql.views.user;

import com.example.testsql.models.Education;
import com.example.testsql.services.user.EducationServiceEJB;
import com.example.testsql.services.user.UserServiceEJB;
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
public class EducationBean implements Serializable {

    @Inject
    private UserServiceEJB userServiceEJB;
    @Inject
    private EducationServiceEJB educationServiceEJB;
    @Inject
    private UserSession userSession;

    private Education education;

    private String diplomat;
    private String university;
    private String yearOfObtention;
    private String emailUser;



    public String ajouterEducation() {
        education=new Education(diplomat,university,yearOfObtention);

        //ajouter les données dans la database
        educationServiceEJB.addEducation(userSession.getEmail(), education);

        //redirection
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        try{
            externalContext.redirect(externalContext.getRequestContextPath() + "/home.xhtml");
        }catch(IOException e){
            e.printStackTrace();
        }
        diplomat = "";
        university = "";
        yearOfObtention = "";
        return null;

    }

    public List<Education> getAllEducation(){
        return educationServiceEJB.getEducation(userSession.getEmail());
    }

    public String getDiplomat() {
        return diplomat;
    }

    public void setDiplomat(String diplomat) {
        this.diplomat = diplomat;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getYearOfObtention() {
        return yearOfObtention;
    }

    public void setYearOfObtention(String yearOfObtention) {
        this.yearOfObtention = yearOfObtention;
    }

    public String getEmailUser() {
        return emailUser;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }
}
