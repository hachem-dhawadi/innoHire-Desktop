package edu.esprit.controllers;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import edu.esprit.entities.*;
import edu.esprit.services.ServiceCommentaire;
import edu.esprit.services.ServicePost;
import edu.esprit.services.ServiceUtilisateur;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

public class AjouterCommentaire implements Initializable {

    @FXML
    private Button validerCommentaire;
    @FXML
    private TextField cinTF1;

    @FXML
    private TextField descriptionTF1;
    @FXML
    private DatePicker dateTf1;

    @FXML
    private TextField ratingTF1;


    @FXML
    private Button afficherCommentaire;
    private final ServicePost es = new ServicePost();


    public static final String ACCOUNT_SID = "AC70a74de16f73d480ab10ed142e6c3060";
    public static final String AUTH_TOKEN = "9f26bc225cc773e0b87c20a8a52f681c";


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        cinTF1.setText(String.valueOf(CurrentUser.getCin()));

        ServicePost serviceService = new ServicePost();
        Set<Post> publications = null;

        try {
            publications = serviceService.getAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }



    @FXML

    void validerCommentaire(ActionEvent event) throws SQLException {
        ServicePost sp = new ServicePost();
        Post publicationSelectionne = sp.getOneByID(CurrentPost.getId_post());
        ServiceUtilisateur su = new ServiceUtilisateur();

        // Récupération des données des champs
        int cin = Integer.parseInt(cinTF1.getText());
        String description_co = descriptionTF1.getText();
        LocalDate date_co = dateTf1.getValue();
        //String nb_etoile = ratingTF1.getText();
        Utilisateur u = new Utilisateur();
        u = su.getByCin(cin);

        // Vérification de la longueur de la description
        if (description_co.split("\\s+").length > 50) {
            AfficherAvertissement("Description trop longue", "La description ne doit pas dépasser 50 mots.");
            return;
        }

        // Vérification des autres champs
        if (publicationSelectionne != null && u != null && !description_co.isEmpty() && date_co != null) {
            LocalDate dateActuelle = LocalDate.now();
            if (!date_co.isBefore(dateActuelle) && !date_co.isAfter(dateActuelle)) {
                // Vérifiez s'il y a des mots interdits
                if (!verif(description_co)) {
                    // La date est valide et il n'y a pas de mots interdits, procédez à l'ajout du commentaire
                    Commentaire commentaire = new Commentaire();
                    commentaire.setPublication(publicationSelectionne);
                    commentaire.setUtilisateur(u);
                    commentaire.setDescription_co(description_co);
                    commentaire.setDate_co(date_co);
                    // Ajoutez l'utilisateur actuel au commentaire

                    ServiceCommentaire serviceCommentaire = new ServiceCommentaire();
                    try {
                        serviceCommentaire.ajouter(commentaire);
                        AfficherInformation("Commentaire ajouté", "Le commentaire a été ajouté avec succès.");
                    } catch (SQLException e) {
                        AfficherErreur("Erreur d'ajout", "Une erreur s'est produite lors de l'ajout du commentaire.", e.getMessage());
                    }
                } else {
                    Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

                    String fromPhoneNumber = "+14846462181";
                    String toPhoneNumber = "+21621545013";  // Remplacez par le numéro de téléphone du destinataire

                    String messageBody = "l'utilisateur d'ID : "+cinTF1.getText()+" utilise des mots interdits";

                    try
                    {Message message = Message.creator(
                                    new PhoneNumber(toPhoneNumber),
                                    new PhoneNumber(fromPhoneNumber),
                                    messageBody)
                            .create();
                        System.out.println("Message SID: " + message.getSid());
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }


                    AfficherAvertissement("Attention", "SVP de ne pas utiliser des mots interdits");
                   /* Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Attention");
                    alert.setContentText("SVP de ne pas utiliser des mots interdits");
                    alert.showAndWait();*/
                }

            } else {
                AfficherAvertissement("Date non valide", "Veuillez sélectionner une date valide.");
            }
        } else {
            AfficherAvertissement("Champs non remplis", "Veuillez remplir tous les champs avant de valider le commentaire.");
        }
    }









    private void AfficherAvertissement(String titre, String contenu) {
    Alert alert = new Alert(Alert.AlertType.WARNING);
    alert.setTitle(titre);
    alert.setHeaderText(null);
    alert.setContentText(contenu);
    alert.showAndWait();
}

// Méthode pour afficher une boîte de dialogue d'information
private void AfficherInformation(String titre, String contenu) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(titre);
    alert.setHeaderText(null);
    alert.setContentText(contenu);
    alert.showAndWait();
}


// Méthode utilitaire pour afficher une boîte de dialogue d'erreur
private void AfficherErreur(String titre, String contenu, String details) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle(titre);
    alert.setContentText(contenu);
    alert.setHeaderText(details);
    alert.showAndWait();
}


    @FXML
    void navigatetoAfficherCommentaireAction(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AfficherCommentaire2.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public boolean verif(String message){

        List<String> badWords = new ArrayList<>();
        badWords.add("Connard");
        badWords.add("Va te faire foutre");
        badWords.add("Ferme ta gueule");
        badWords.add("Bâtard");
        badWords.add("Trou du cul");
        badWords.add("Sac à merde");
        badWords.add("Casse-couilles");
        badWords.add("Enfoiré");
        badWords.add("Tête de bite");
        badWords.add("Pas le couteau le plus affûté du tiroir");
        badWords.add("Fini à la pisse");
        badWords.add("Pétasse");
        badWords.add("Abruti");
        badWords.add("Pas la lumière à tous les étages");
        badWords.add("Cn");
        badWords.add("Sale merde");
        badWords.add("Tocard");
        badWords.add("Sous-merde");
        badWords.add("Mange-merde");
        badWords.add("Pouffiasse");
        badWords.add("Va te faire cuire le cul");
        badWords.add("Bercé un peu trop près du mur");
        badWords.add("Petite bite");
        badWords.add("Bouffon");
        badWords.add("Branleur");
        badWords.add("Grognasse");
        badWords.add("Couille molle");
        badWords.add("Branquignole");
        badWords.add("Fils de chien");
        badWords.add("Salaud");
        badWords.add("cul");
        badWords.add("fuck");
        badWords.add("pute");
        badWords.add("ass");
        badWords.add("bite");
        badWords.add("cnne");


        for (String badWord : badWords) {
            if (message.toLowerCase().contains(badWord.toLowerCase())) {
                return true;
            }
        }
        return false;
    }



}



