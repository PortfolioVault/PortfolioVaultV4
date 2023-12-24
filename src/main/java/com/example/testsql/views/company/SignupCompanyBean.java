package com.example.testsql.views.company;

import com.example.testsql.exceptions.EmailAlreadyTakenException;
import com.example.testsql.services.company.CompanyServiceEJB;
import com.example.testsql.services.user.UserServiceEJB;
import com.example.testsql.views.user.SignupBean;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Logger;


@Named
@ViewScoped
public class SignupCompanyBean implements Serializable {
    private static final long serialVersionUID = 2729758432756108274L;
    Logger logger = Logger.getLogger(SignupBean.class.getName());

    private String name;
    private String phone;
    private String email;
    private String password;
    private String secteurTravail;

    @Inject
    private CompanyServiceEJB companyServiceEJB;

    public String signup() {
        try{
            companyServiceEJB.registerCompany(name, email, phone, password);
            FacesContext context = FacesContext.getCurrentInstance();
            ExternalContext externalContext = context.getExternalContext();
            externalContext.redirect(externalContext.getRequestContextPath() + "/onboardingCompanyPage.xhtml");
        }catch (EmailAlreadyTakenException exception){
            FacesContext context = FacesContext.getCurrentInstance();
            FacesMessage message = new FacesMessage("Email Already taken", "Please choose another email");
            context.addMessage(null, message);
        }
        catch(IOException e){
            logger.warning(e.getMessage());
        }catch (Exception exception){
            logger.warning(exception.getMessage());
            FacesContext context = FacesContext.getCurrentInstance();
            FacesMessage message = new FacesMessage("Something went wrong", "");
            context.addMessage(null, message);
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSecteurTravail() {
        return secteurTravail;
    }

    public void setSecteurTravail(String secteurTravail) {
        this.secteurTravail = secteurTravail;
    }
}
