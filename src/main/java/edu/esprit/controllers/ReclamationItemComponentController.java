package edu.esprit.controllers;

import edu.esprit.entities.CurrentUser;
import edu.esprit.entities.Reclamation;
import edu.esprit.services.ServiceReclamation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ReclamationItemComponentController {

    @FXML
    private ImageView userPhoto;

    @FXML
    private Label userFullName;

    @FXML
    private Label pubCode;
    @FXML
    private Label tag;

    @FXML
    private Label dateRec;
    @FXML
    private Button chatItemButton;



    ServiceReclamation serviceReclamation = new ServiceReclamation();

    // Reference to the Reclamation object
    private Reclamation reclamation;

    // Reference to the container (parent) HBox
    private AnchorPane container;

    // Create a method to set data from Reclamation object
    public void setReclamationData(Reclamation reclamation, AnchorPane container) {
        this.reclamation = reclamation;
        this.container = container;
        if (CurrentUser.getRole()==0)
        {
            chatItemButton.setVisible(true);
        }else {
            chatItemButton.setVisible(true);
        }

        // Set data to UI elements
        userFullName.setText(reclamation.getUser().getNom() + " " + reclamation.getUser().getPrenom());
        if (reclamation.getStatus() == 0) {
            tag.setText("Unseen"); // You can change this to set the appropriate text
        } else {
            tag.setText("Seen"); // You can change this to set the appropriate text
        }
        System.out.println();
        pubCode.setText(String.valueOf(reclamation.getPub().getAudience()));
        dateRec.setText(String.valueOf(reclamation.getDate()));
        // Set user photo
        String imageName = reclamation.getUser().getImage();
        //System.out.println(imageName);// Replace with the actual method to get the image name
        if (imageName != null && !imageName.isEmpty()) {
            String imagePath = "/images/" + imageName; // Assuming images are stored in src/main/resources/images
            Image image = new Image(getClass().getResource(imagePath).toExternalForm());
            userPhoto.setImage(image);
        } else {
            // Set a default image if the name is not available
            userPhoto.setImage(new Image(getClass().getResource("/images/edit.png.jpg").toExternalForm()));
        }
    }


    public void navigateToAfficherReclamationAction(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AfficherReclamation.fxml"));
            pubCode.getScene().setRoot(root);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Sorry");
            alert.setTitle("Error");
            alert.show();
        }

    }

    @FXML
    public void deleteReclamationAction(ActionEvent event) {
        // Display a confirmation dialog before deleting
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this reclamation?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            // Perform deletion logic here
            try {
                // Delete the reclamation from the database
                serviceReclamation.supprimer(reclamation.getIdReclamation());

                // Remove the HBox from the parent container
                container.getChildren().remove(container);

                System.out.println("Reclamation deleted successfully!");
                navigateToAfficherReclamationAction(event);
            } catch (SQLException e) {
                // Handle any potential exceptions (e.g., database connection issues)
                e.printStackTrace();
                // You may want to display an error message to the user
                // or log the error for further investigation
            }
        }
    }

    public void navigateToChatRec(ActionEvent event) {
        try {
            // Load the AjouterAfficherMessage.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterAfficherMessage.fxml"));
            Parent root = loader.load();

            // Get the controller for the AjouterAfficherMessage page
            AjouterAfficherMessageController chatController = loader.getController();

            // Pass the selected reclamation data to the AjouterAfficherMessageController
            chatController.initData(reclamation);

            // Set the root of the scene to the AjouterAfficherMessage page
            pubCode.getScene().setRoot(root);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Sorry");
            alert.setTitle("Error");
            alert.show();
        }
    }


    public void DetailsReclamationAction(ActionEvent event) {
        try {
            // Load the DetailsReclamation.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierReclamation.fxml"));
            Parent root = loader.load();

            // Get the controller for the DetailsReclamation page
            ModifierReclamationController detailsController = loader.getController();

            // Pass the selected reclamation data to the DetailsReclamationController
            detailsController.initData(reclamation);

            // Set the root of the scene to the DetailsReclamation page
            pubCode.getScene().setRoot(root);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Sorry");
            alert.setTitle("Error");
            alert.show();
        }
    }


}
