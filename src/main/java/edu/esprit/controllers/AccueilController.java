package edu.esprit.controllers;
import edu.esprit.entities.*;
import edu.esprit.services.ServiceUtilisateur;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;

public class AccueilController implements Initializable {

    @FXML
    private AnchorPane AnchoPaneMessage131;

    @FXML
    private Label CurrentUserEmail;

    @FXML
    private TextField TFcurrentusercin;

    @FXML
    private Button chatButton;

    @FXML
    private AnchorPane container;

    @FXML
    private Label currentUserName;

    @FXML
    private Button etablissementButton;

    @FXML
    private Button helpButton;

    @FXML
    private Button homeButton;

    @FXML
    private Button listClaimButton;

    @FXML
    private Button listUserButton;

    @FXML
    private Button postButton;

    @FXML
    private Button settingsButton;




    @FXML
    void NavigateToEtablissement(ActionEvent event) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/Etablissement.fxml"));
            TFcurrentusercin.getScene().setRoot(root);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Sorry");
            alert.setTitle("Error");
            alert.show();
        }


    }

    @FXML
    void NavigatoToListUsers(ActionEvent event) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/listUsers.fxml"));
            TFcurrentusercin.getScene().setRoot(root);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Sorry");
            alert.setTitle("Error");
            alert.show();
        }

    }

    @FXML
    void navigatotoAccueil(ActionEvent event) {

    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TFcurrentusercin.setText(CurrentUser.getNom());
        currentUserName.setText(CurrentUser.getNom());
        CurrentUserEmail.setText(CurrentUser.getAdresse());
        if (CurrentUser.getRole()!=0)
        {
            listUserButton.setVisible(false);
            listClaimButton.setVisible(false);
            etablissementButton.setVisible(true);//khaleha ya kthiri aandha ma3na (sayari)
        }

    }
    @FXML
    void Logout(ActionEvent event) {
        CurrentUser.setId_utilisateur(0);
        CurrentUser.setCin(0);
        CurrentUser.setNom("");
        CurrentUser.setPrenom("");
        CurrentUser.setMdp("");
        CurrentUser.setAdresse("");
        CurrentUser.setRole(-1);
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/Login.fxml"));
            TFcurrentusercin.getScene().setRoot(root);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Sorry");
            alert.setTitle("Error");
            alert.show();
        }


    }
    @FXML
    void profilAction(ActionEvent event) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/Profil.fxml"));
            TFcurrentusercin.getScene().setRoot(root);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Sorry");
            alert.setTitle("Error");
            alert.show();
        }

    }


}
