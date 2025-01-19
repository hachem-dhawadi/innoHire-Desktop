package edu.esprit.controllers;

import edu.esprit.entities.Commentaire;
import edu.esprit.entities.Utilisateur;
import edu.esprit.services.ServiceCommentaire;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static edu.esprit.controllers.ModifierPublication.showAlert;

public class ModifierCommentaire {
    @FXML
    private DatePicker dateTf1;

    @FXML
    private TextField descriptionTF1;

    @FXML
    private Button modifierCommentaire;


    @FXML
    private Button AfficherCommentaire;
    int id;
    Utilisateur utilisateur;

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }




    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public boolean controlSaisie(TextField field) {
        if (field.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Champ vide", "Veuillez remplir tous les champs.");
            return false;
        }
        return true;
    }

    @FXML
    /*void modifierCommentaire(ActionEvent event) throws SQLException {
        if (controlSaisie(descriptionTF1) ) {
            String description_co = descriptionTF1.getText();
            //String nb_etoile = ratingTF1.getText();

            // Vérification de la longueur de la description
            if (description_co.split("\\s+").length > 50) {
                showAlert(Alert.AlertType.WARNING, "Erreur de saisie", "La description ne doit pas dépasser 50 mots.");
                return;
            }



            Commentaire c = new Commentaire();
            c.setId_commentaire(getId());
            c.setDescription_co(description_co);
            c.setDate_co(dateTf1.getValue());
            //c.setNb_etoile(Integer.parseInt(nb_etoile));
            ServiceCommentaire sc = new ServiceCommentaire();
            // Appel de la méthode pour effectuer la modification dans la base de données
            sc.modifier(c);

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Commentaire modifié avec succès");
            // Assurez-vous d'ajuster le code pour afficher les publications après la modification
            AfficherCommentaire(event);
        }
    }*/
    void modifierCommentaire(ActionEvent event) throws SQLException {
        if (controlSaisie(descriptionTF1)) {
            String description_co = descriptionTF1.getText();

            // Vérification de la longueur de la description
            if (description_co.split("\\s+").length > 50) {
                showAlert(Alert.AlertType.WARNING, "Erreur de saisie", "La description ne doit pas dépasser 50 mots.");
                return;
            }

            // Vérification des mots interdits
            if (!verif(description_co)) {
                // Pas de mots interdits, procédez à la modification du commentaire
                Commentaire c = new Commentaire();
                c.setId_commentaire(getId());
                c.setDescription_co(description_co);
                c.setDate_co(dateTf1.getValue());

                ServiceCommentaire sc = new ServiceCommentaire();
                // Appel de la méthode pour effectuer la modification dans la base de données
                sc.modifier(c);

                showAlert(Alert.AlertType.INFORMATION, "Succès", "Commentaire modifié avec succès");
                // Assurez-vous d'ajuster le code pour afficher les commentaires après la modification
                AfficherCommentaire(event);
            } else {
                showAlert(Alert.AlertType.WARNING, "Mots interdits", "Votre commentaire contient des mots interdits. Veuillez les retirer avant de valider la modification.");
            }
        }
    }



    public void initData(Commentaire commentaire) {
        if (commentaire != null) {
            setId(commentaire.getId_commentaire());

            descriptionTF1.setText(commentaire.getDescription_co());
            dateTf1.setValue(commentaire.getDate_co()); // Assuming publication.getDate() returns LocalDate
            //ratingTF1.setText(String.valueOf(commentaire.getNb_etoile()));

        }
    }

    public void AfficherCommentaire(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherCommentaire2.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) descriptionTF1.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

            // Vous pouvez fermer la fenêtre actuelle si nécessaire
            // ((Node)(event.getSource())).getScene().getWindow().hide();
        } catch (IOException e) {
            e.printStackTrace();
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
        badWords.add("bonjour");

        for (String badWord : badWords) {
            if (message.toLowerCase().contains(badWord.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}
