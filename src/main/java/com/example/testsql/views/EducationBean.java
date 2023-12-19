package com.example.testsql.views;

import com.example.testsql.models.Education;
import com.example.testsql.services.EducationService;
import com.example.testsql.session.UserSession;
import jakarta.faces.application.FacesMessage;
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
    private String diplomat;
    private String university;
    private String yearOfObtention;
    private String emailUser;

    @Inject
    private UserSession userSession;
    @Inject
    private EducationService educationService;

    public void save(){
        FacesContext context = FacesContext.getCurrentInstance();
        try{
            educationService.addEducation(userSession.getEmail(),diplomat,university,yearOfObtention);
            FacesMessage message = new FacesMessage("Success", "Infos saved successfully");
            context.addMessage(null, message);

            ExternalContext externalContext = context.getExternalContext();
            try {
                externalContext.redirect(externalContext.getRequestContextPath() + "/home.xhtml");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }catch (Exception exception){
            FacesMessage message = new FacesMessage("Something went wrong", "An error has occured");
            context.addMessage(null, message);
        }
    }

    public List<Education> getAllEducations(){
        return educationService.getAllEducations(userSession.getEmail());
    }
    public void deleteById(String id){educationService.deleteById(id);}


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

    public UserSession getUserSession() {
        return userSession;
    }

    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }

    public EducationService getEducationService() {
        return educationService;
    }

    public void setEducationService(EducationService educationService) {
        this.educationService = educationService;
    }

}

