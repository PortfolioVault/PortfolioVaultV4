package com.example.testsql.views.user;

import com.example.testsql.models.Education;
import com.example.testsql.models.Experience;
import com.example.testsql.models.User;
import com.example.testsql.services.user.ExperienceServiceEJB;
import com.example.testsql.services.user.UserServiceEJB;
import com.example.testsql.session.UserSession;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedList;

@Named
@ViewScoped
public class HomePageBean implements Serializable {
    @Inject
    private UserServiceEJB userServiceEJB;
    @Inject
    private ExperienceServiceEJB experienceServiceEJB;
    @Inject
    private UserSession userSession;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;
    private String age;
    private String professionalTitle;
    private LinkedList<Experience> experiences = new LinkedList<>();
    private LinkedList<Education> educations = new LinkedList<>();

    public void fetchUser() {
        // Initialize properties using values from UserSessionBean
        User user = userServiceEJB.findUserByEmail(userSession.getEmail());
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = userSession.getEmail();
        this.address = user.getAddress() != null ? user.getAddress() : "";
        this.age = user.getAge() != null ? user.getAge() : "";
        this.professionalTitle = user.getProfessionalTitle() != null ? user.getProfessionalTitle() : "";
        this.phoneNumber = user.getPhoneNumber() != null ? user.getPhoneNumber() : "";
//        this.experiences = experienceServiceEJB.getExperiences(userSession.getEmail());
    }

    public void logout(){
        userSession.setEmail("");
        this.email = "";
        this.lastName = "";
        this.firstName = "";
        this.address = "";
        this.age = "";
        this.phoneNumber = "";
        this.professionalTitle = "";
        this.experiences = null;
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        try {
            externalContext.redirect(externalContext.getRequestContextPath() + "/signup.xhtml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void savePersonalInfos(){
        FacesContext context = FacesContext.getCurrentInstance();
        try{
            userServiceEJB.savePersonalInfos(userSession.getEmail(), phoneNumber,age,professionalTitle,address);
            FacesMessage message = new FacesMessage("Success", "Infos saved successfully");
            context.addMessage(null, message);
        }catch (Exception exception){
            FacesMessage message = new FacesMessage("Something went wrong", "An error has occured");
            context.addMessage(null, message);
        }
    }

    public LinkedList<Education> getEducations() {
        return educations;
    }

    public void setEducations(LinkedList<Education> educations) {
        this.educations = educations;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getProfessionalTitle() {
        return professionalTitle;
    }

    public void setProfessionalTitle(String professionalTitle) {
        this.professionalTitle = professionalTitle;
    }

    public LinkedList<Experience> getExperiences() {
        return experiences;
    }

    public void setExperiences(LinkedList<Experience> experiences) {
        this.experiences = experiences;
    }
}
