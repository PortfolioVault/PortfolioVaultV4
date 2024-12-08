package com.example.testsql.views.company;

import com.example.testsql.services.company.CompanyServiceEJB;
import com.example.testsql.services.user.UserServiceEJB;
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
public class LoginCompanyBean implements Serializable {
    private String email;
    private String password;

    @Inject
    private CompanyServiceEJB companyServiceEJB;

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

    public void login(){
        Logger logger = Logger.getLogger(LoginCompanyBean.class.getName());
        logger.severe(this.email+" , "+this.password);
        boolean loginResult = companyServiceEJB.login(this.email,this.password);
        logger.severe(String.valueOf(loginResult));
        FacesContext context = FacesContext.getCurrentInstance();
        if(!loginResult){
            FacesMessage message = new FacesMessage("Wrong credentials", "Wrong email or password");
            context.addMessage(null, message);
        }else{
            ExternalContext externalContext = context.getExternalContext();
            try {
                externalContext.redirect(externalContext.getRequestContextPath() + "/homeCompanyPage.xhtml");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
