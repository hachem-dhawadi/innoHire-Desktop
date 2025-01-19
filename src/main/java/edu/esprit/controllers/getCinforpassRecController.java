package edu.esprit.controllers;


import edu.esprit.entities.CurrentUser;
import edu.esprit.entities.Utilisateur;
import edu.esprit.services.ServiceUtilisateur;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;


import java.io.IOException;
import java.sql.SQLException;


public class getCinforpassRecController {



    @FXML
    private Label LabelCIN;

    @FXML
    private TextField TFcin;

    @FXML
    void cinsearchAction(ActionEvent event) {
        int cin;
        ServiceUtilisateur serviceUtilisateur = new ServiceUtilisateur();
        try {
            cin = Integer.parseInt(TFcin.getText());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Le cin doit être un nombre valide !");
            alert.showAndWait();
            return;
        }

        if (cin < 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Le cin doit être un nombre valide !");
            alert.showAndWait();
            return;
        }

        if (!serviceUtilisateur.utilisateurExiste(cin)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("CIN INTROUVABLE");
            alert.showAndWait();
            return;
        } else {
            Utilisateur u = null;
            try {
                u = serviceUtilisateur.getOneByCin(cin);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            CurrentUser.setCin(cin);
            CurrentUser.setRole(u.getRole());
            CurrentUser.setMdp(u.getMdp());
            CurrentUser.setAdresse(u.getAdresse());
            CurrentUser.setNom(u.getNom());
            CurrentUser.setPrenom(u.getPrenom());
            CurrentUser.setOtp(serviceUtilisateur.generateOTP());
            System.out.println("otp = " + CurrentUser.getOtp());
            serviceUtilisateur.modifier_OTP_par_cin(CurrentUser.getCin(), CurrentUser.getOtp());
            EmailClass test = new EmailClass();
            test.envoyer(CurrentUser.getAdresse(), String.valueOf(CurrentUser.getOtp()));

            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
            alert1.setTitle("CIN FOUND");
            alert1.setHeaderText(null);
            alert1.setContentText("CIN TROUVEE ET EMAIL RECU AVEC CODE");
            alert1.showAndWait();

            try {
                Parent root = FXMLLoader.load(getClass().getResource("/OTP.fxml"));
                TFcin.getScene().setRoot(root);
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Sorry");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }
    @FXML
    void backTOLOGIN(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Login.fxml"));
            TFcin.getScene().setRoot(root);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Sorry");
            alert.setTitle("Error");
            alert.show();
        }
    }

    }


