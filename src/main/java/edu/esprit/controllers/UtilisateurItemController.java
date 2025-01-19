package edu.esprit.controllers;

import edu.esprit.entities.Utilisateur;
import edu.esprit.services.ServiceUtilisateur;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.io.InputStream;

public class UtilisateurItemController {




    @FXML
    private Label labelCin;

    @FXML
    private Label labelNom;
    @FXML
    private ImageView imageUser;


    private Utilisateur utilisateur;

    private ServiceUtilisateur su;
    @FXML
    private HBox hboxMere; // Assurez-vous d'avoir la référence correcte à votre HBox
    private static HBox hboxSelectionne; // Champ statique pour suivre le HBox précédemment sélectionné
    @FXML
    private Button affecteBtn;
    private AjouterEtablissementController ajouterEtablissementController;


    public void setAjouterEtablissementController(AjouterEtablissementController ajouterEtablissementController) {
        this.ajouterEtablissementController = ajouterEtablissementController;
    }


    public void setData(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
       String imageName = utilisateur.getImage();
        String imagePath = "/img/" + imageName;
        Image image = new Image(getClass().getResourceAsStream(imagePath));
        imageUser.setImage(image);

        labelCin.setText(String.valueOf(utilisateur.getCin()));
        labelNom.setText(utilisateur.getNom()+utilisateur.getPrenom());






    }





    public void affecterUtilisateurOnClick(ActionEvent actionEvent) {
        // Désélectionner le précédent HBox s'il existe
        if (hboxSelectionne != null) {
            simulateDeselection(hboxSelectionne);
        }

        // Affecter le nouvel utilisateur
        ajouterEtablissementController.updateCinTextField(labelCin.getText());
        simulateSelection(hboxMere);

        // Mettre à jour le HBox sélectionné actuel
        hboxSelectionne = hboxMere;
    }

    // ... (autres méthodes)

    private void simulateSelection(HBox hbox) {
        hbox.getStyleClass().add("selected-item");
    }

    private void simulateDeselection(HBox hbox) {
        hbox.getStyleClass().remove("selected-item");
    }





}