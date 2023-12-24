package com.example.testsql.views.company;

import com.example.testsql.activeMQ.SimpleQueue;
import com.example.testsql.models.Entreprise;
import com.example.testsql.models.Offre;
import com.example.testsql.services.company.CompanyServiceEJB;
import com.example.testsql.services.company.OffreServiceEJB;
import com.example.testsql.session.CompanySession;
import jakarta.annotation.PostConstruct;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Named
@ViewScoped
public class OffreListPageBean implements Serializable {
    private static final String QUEUE_NAME = "pendingPostulat";
    private List<List<String>> receivedMessages;

    private String email; // Email de l'entreprise, peut être passé en paramètre ou récupéré autrement
    private Entreprise entreprise;
    private List<Offre> offres;

    @Inject
    private OffreServiceEJB offreService; // Assurez-vous d'avoir un service pour gérer les offres
    @Inject
    private CompanyServiceEJB companyServiceEJB;
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

//    public Future<List<List<String>>> receiveAsync() {
//        return CompletableFuture.supplyAsync(() -> {
//            try {
//                SimpleQueue queue = new SimpleQueue(QUEUE_NAME);
//                byte[] msgBytes = queue.receive();
//                queue.close();
//
//                // Convertir les octets en chaîne
//                String msg = new String(msgBytes, StandardCharsets.UTF_8);
//
//                System.out.println("le msg reçu est " + msg);
//
//                // Supposons que msg est une chaîne au format "Nom:<|>Prénom:PDFBytes"
//                String[] parts = msg.split(":");
//                String[] nameParts = parts[0].split("<\\|>");
//                String nom = nameParts[0];
//                String prenom = nameParts[1];
//                String pdfBytes = parts[1];
//
//                // Créer une liste contenant le nom, le prénom et le PDF
//                List<String> receivedMessage = Arrays.asList(nom, prenom, pdfBytes);
//
//                // Ajouter la liste à receivedMessages
//                List<List<String>> receivedMessages = Collections.singletonList(receivedMessage);
//
//                System.out.println("le nom est " + nom);
//                System.out.println("le prénom est " + prenom);
//                return receivedMessages;
//            } catch (Exception e) {
//                e.printStackTrace(); // Gérer les erreurs correctement dans un environnement de production
//                return Collections.emptyList();
//            }
//        });
//    }

    public Future<List<String>> receiveAsync() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                SimpleQueue queue = new SimpleQueue(QUEUE_NAME);
                byte[] nameBytes = queue.receive();
                queue.close();

                // Convert the received bytes to a string
                String combinedString = new String(nameBytes, StandardCharsets.UTF_8);

                // Split the string based on the delimiter (colon ":")
                String[] parts = combinedString.split(":");

                if (parts.length >= 2) {
                    String id_user = parts[0];
                    String id_offre = parts[1];

                    // Create a list with individual data fields
                    List<String> receivedMessage = Arrays.asList(id_user, id_offre);

                    System.out.println("Received data: id_user=" + id_user + ", id_offre=" + id_offre);
                    return receivedMessage;
                } else {
                    System.out.println("Invalid data format");
                    return Collections.emptyList();
                }

                // Convertir les octets en chaîne
//                String name = new String(nameBytes, StandardCharsets.UTF_8);
//
//                // Créer une liste contenant le nom
//                List<String> receivedMessage = Collections.singletonList(name);
//
//                System.out.println("le nom reçu est " + name);
//                return receivedMessage;
            } catch (Exception e) {
                e.printStackTrace(); // Gérer les erreurs correctement dans un environnement de production
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
}

