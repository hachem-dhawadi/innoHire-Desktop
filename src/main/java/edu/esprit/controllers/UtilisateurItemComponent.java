package edu.esprit.controllers;
import edu.esprit.entities.Admin;
import edu.esprit.entities.Candidat;
import edu.esprit.entities.Representant;
import edu.esprit.entities.Utilisateur;
import edu.esprit.services.ServiceUtilisateur;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;

public class UtilisateurItemComponent {

    @FXML
    private AnchorPane AnchoPaneMessage1111;

    @FXML
    private Label UserAdresse;

    @FXML
    private Label UserPRENOM;

    @FXML
    private Label UserRole;

    @FXML
    private Label userCIN;

    @FXML
    private Label userNOM;

    @FXML
    private ImageView userPhoto;

    private Utilisateur utilisateur;
    public Utilisateur getUser() {
        return utilisateur;
    }
    private AnchorPane container;
    ServiceUtilisateur serviceUtilisateur = new ServiceUtilisateur();


    public void setUtilisateurData(Utilisateur utilisateur, AnchorPane container) {
        this.utilisateur = utilisateur;
        this.container = container;
        // Set data to UI elements
        userCIN.setText(String.valueOf(utilisateur.getCin()));
        userNOM.setText(utilisateur.getNom());
        UserPRENOM.setText(utilisateur.getPrenom());
        int role = serviceUtilisateur.verifyRoleByCin(utilisateur.getCin());
        UserRole.setText(String.valueOf(role));
        UserAdresse.setText(utilisateur.getAdresse());


        // Set user photo
        String imageName = utilisateur.getProfileImagePath();
        //System.out.println(imageName);// Replace with the actual method to get the image name
       /* if (imageName != null && !imageName.isEmpty()) {
            String imagePath = "/images/" + imageName; // Assuming images are stored in src/main/resources/images
            Image image = new Image(getClass().getResource(imagePath).toExternalForm());
            userPhoto.setImage(image);
        } else {
            // Set a default image if the name is not available
            userPhoto.setImage(new Image(getClass().getResource("/images/edit.png.jpg").toExternalForm()));
        }*/

    }

    public void navigateToAfficherAction(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/listUsers.fxml"));
            UserAdresse.getScene().setRoot(root);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Sorry");
            alert.setTitle("Error");
            alert.show();
        }

    }

    @FXML
    void deleteUtilisateurAction(ActionEvent event) {
        // Display a confirmation dialog before deleting
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to cancel this USER?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            // Perform deletion logic here
            try {
                // Delete the reclamation from the database
                serviceUtilisateur.supprimer_par_cin(utilisateur.getCin());

                // Remove the HBox from the parent container
                container.getChildren().remove(container);

                System.out.println("User deleted successfully!");
                navigateToAfficherAction(event);
            } catch (SQLException e) {
                // Handle any potential exceptions (e.g., database connection issues)
                e.printStackTrace();
                // You may want to display an error message to the user
                // or log the error for further investigation
            }
        }
    }

    @FXML
    public   void modifierUser(ActionEvent actionEvent) {
        // Code pour modifier l'utilisateur sélectionné dans la liste
        if (this.utilisateur != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierUtilisateur.fxml"));
                Parent root = loader.load();
                ModifierUtilisateurController controller = loader.getController();
                controller.initData(this.utilisateur); // Passer l'utilisateur sélectionné au contrôleur de l'interface de modification

                // Obtenir la scène actuelle
                Scene scene = UserAdresse.getScene();

                // Changer le contenu de la scène
                scene.setRoot(root);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    void activateUser(ActionEvent event) {
        if (this.utilisateur != null)
        {
            if (serviceUtilisateur.getStatusfromCIN(utilisateur.getCin())!=1)
            {
                serviceUtilisateur.modifier_Status_par_cin(utilisateur.getCin());
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Compte Utilisateur activé ");
                alert.setTitle("GG");
                alert.show();
            }
            else
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Compte Utilisateur deja actif ");
                alert.setTitle("GG");
                alert.show();
            }
        }

    }
}
