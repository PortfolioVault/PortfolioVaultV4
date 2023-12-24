package com.example.testsql.views.company;

import com.example.testsql.activeMQ.SimpleQueue;
import com.example.testsql.models.Entreprise;
import com.example.testsql.models.Offre;
import com.example.testsql.models.User;
import com.example.testsql.services.company.CompanyServiceEJB;
import com.example.testsql.services.company.CondidatureServiceEJB;
import com.example.testsql.services.company.OffreServiceEJB;
import com.example.testsql.services.user.UserServiceEJB;
import com.example.testsql.session.CompanySession;
import jakarta.annotation.PostConstruct;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.net.URLDecoder;

@Named
@ViewScoped
public class OffreListPageBean implements Serializable {
    private static final String QUEUE_NAME = "pendingPostulat";
    private List<List<String>> receivedMessages;

    private String email;
    private Entreprise entreprise;
    private List<Offre> offres;
    private User user;
    private Offre offre;

    @Inject
    private OffreServiceEJB offreService;

    @Inject
    private CompanyServiceEJB companyServiceEJB;

    @Inject
    private CondidatureServiceEJB condidatureServiceEJB ;


    @Inject
    private CompanySession companySession;


    public void loadEntrepriseData() {
        // Charger les données de l'entreprise et ses offres

        entreprise = companyServiceEJB.findCompnayByEmail(companySession.getEmail());
        offres = entreprise.getOffres();
    }

    public void fetchData() {
        try {
            receivedMessages = Collections.singletonList(receiveAsync().get());
        } catch (Exception e) {
            e.printStackTrace();
            receivedMessages = Collections.emptyList();
        }
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public CompletableFuture<List<String>> receiveAsync() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                SimpleQueue queue = new SimpleQueue(QUEUE_NAME);
                byte[] nameBytes = queue.receive();
                queue.close();
                // Convert the received bytes to a string
                String combinedString = new String(nameBytes, StandardCharsets.UTF_8);
                String[] parts = combinedString.split(":");

                if (parts.length >= 2) {
                    String email_user = parts[0];
                    String id_offre = parts[1];

                    user = userServiceEJB.findUserByEmail(email_user);
//                    user.setEducations(userServiceEJB.findEducationsByUserId(user.getId()));
//                    user.setExperiences(userServiceEJB.findExperiencesByUserId(user.getId()));
                    System.out.println("test ======="+user.getEducations());
                    offre = offreService.findOffreById(Long.valueOf(id_offre));
                        List<String> receivedMessage = Arrays.asList(email_user,offre.getDescription());

                    return receivedMessage;
                } else {
                    System.out.println("Invalid data format");
                    return Collections.emptyList();
                }
            } catch (Exception e) {
                e.printStackTrace();
                return Collections.emptyList();
            }
        });
    }

    public List<List<String>> getReceivedMessages() {
        return receivedMessages;
    }

    public void redirectToCandidatures(Offre offre) {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        try {
            externalContext.redirect(externalContext.getRequestContextPath() + "/condidature.xhtml");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void accepterCandidature() {
        condidatureServiceEJB.addCandidature(user, offre);
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        try {
            externalContext.redirect(externalContext.getRequestContextPath() + "/viewListOffres.xhtml");
            context.responseComplete();  // Indique à JSF que la réponse est complète
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void rejeterCandidature() {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        try{
            externalContext.redirect(externalContext.getRequestContextPath() + "/viewListOffres.xhtml");
            context.responseComplete();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void openPDF(String pdfBytes) {
        try {
            // URL decode the string if it's URL-encoded
            String decodedPdfBytes = URLDecoder.decode(pdfBytes, StandardCharsets.UTF_8.toString());

            // Convert the byte string to a byte array
            byte[] fileBytes = Base64.getDecoder().decode(decodedPdfBytes);

            // Set the response headers for PDF content
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();
            externalContext.responseReset();
            externalContext.setResponseContentType("application/pdf");
            externalContext.setResponseContentLength(fileBytes.length);
            externalContext.setResponseHeader("Content-Disposition", "inline; filename=\"document.pdf\"");

            // Write the PDF content to the response output stream
            try {
                externalContext.getResponseOutputStream().write(fileBytes);
            } catch (Exception e) {
                e.printStackTrace(); // Handle the exception appropriately in a production environment
            }

            facesContext.responseComplete();
        } catch (Exception e) {
            e.printStackTrace(); // Handle the exception appropriately in a production environment
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

    @Inject
    private UserServiceEJB userServiceEJB;


}

