
package com.example.testsql.views.user;

import com.example.testsql.models.Education;
import com.example.testsql.models.Experience;
import com.example.testsql.models.User;
import com.example.testsql.services.user.EducationServiceEJB;
import com.example.testsql.services.user.ExperienceServiceEJB;
import com.example.testsql.services.user.UserServiceEJB;
import com.example.testsql.session.UserSession;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@Named
@ViewScoped
public class ProfilPageBean implements Serializable {
    @Inject
    private UserServiceEJB userServiceEJB;
    @Inject
    private ExperienceServiceEJB experienceServiceEJB;
    @Inject
    private EducationServiceEJB educationService;
    @Inject
    private UserSession userSession;


    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;
    private String age;
    private String professionalTitle;
    private List<Experience> experiences = new LinkedList<>();
    private List<Education> educations = new LinkedList<>();


    public void getUser() {
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

    public void generatePDF() {
        Document document = new Document();

        try {
            // Obtenez le chemin du fichier PDF de sortie avec le nom d'utilisateur
            String pdfPath = "C:\\Users\\hp\\IdeaProjects\\stateful\\PortfolioVaultV4\\src\\main\\java\\com\\example\\testsql\\pdf\\" + firstName +" "+lastName + ".pdf";

            PdfWriter.getInstance(document, new FileOutputStream(pdfPath));

            document.open();

            // Ajoutez les données à votre document PDF avec mise en forme
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

            // Titre
            document.add(new Paragraph(getFirstName() + " " + getLastName(), titleFont));

            // Infos personnelles
            document.add(new Paragraph("Professional Title: " + getProfessionalTitle(), normalFont));
            document.add(new Paragraph("Age: " + getAge(), normalFont));
            document.add(new Paragraph("Email: " + getEmail(), normalFont));
            document.add(new Paragraph("Address: " + getAddress(), normalFont));
            document.add(new Paragraph("Phone Number: " + getPhoneNumber(), normalFont));

            // Éducation
            document.add(new Paragraph("Education", sectionFont));
            if (!educationService.getEducation(userSession.getEmail()).isEmpty()) {
                for (Education education : educationService.getEducation(userSession.getEmail())) {
                    document.add(new Paragraph("Date d'Obtention: " + education.getYearOfObtention(), normalFont));
                    document.add(new Paragraph("Diplôme: " + education.getDiplomat(), normalFont));
                    document.add(new Paragraph("Université: " + education.getUniversity(), normalFont));
                    document.add(new Paragraph(" ", normalFont)); // Ajoutez un espace entre chaque entrée
                }
            }

            // Expériences professionnelles
            document.add(new Paragraph("Experiences", sectionFont));
            if (!experienceServiceEJB.getExperiences(userSession.getEmail()).isEmpty()) {
                for (Experience experience : experienceServiceEJB.getExperiences(userSession.getEmail())) {
                    document.add(new Paragraph("Date de Début: " + experience.getStartDate(), normalFont));
                    document.add(new Paragraph("Date de Fin: " + experience.getEndDate(), normalFont));
                    document.add(new Paragraph("Rôle: " + experience.getRole(), normalFont));
                    document.add(new Paragraph("Entreprise: " + experience.getCompany(), normalFont));
                    document.add(new Paragraph(" ", normalFont)); // Ajoutez un espace entre chaque entrée
                }
            }

            // Fermez le document
            document.close();

            FacesContext context = FacesContext.getCurrentInstance();
            try{
                FacesMessage message = new FacesMessage("Success", "Pdf Exported successfully");
                context.addMessage(null, message);
            }catch (Exception exception){
                FacesMessage message = new FacesMessage("Something went wrong", "An error has occured");
                context.addMessage(null, message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public List<Experience> getExperiences() {
        return experienceServiceEJB.getExperiences(userSession.getEmail());
    }

    public void setExperiences(LinkedList<Experience> experiences) {
        this.experiences = experiences;
    }

    public List<Education> getEducations() {
        return educationService.getEducation(userSession.getEmail());
    }

    public void setEducations(LinkedList<Education> educations) {
        this.educations = educations;
    }


}


