package edu.esprit.controllers;
import edu.esprit.entities.*;
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
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;

public class NewmdpController {

    @FXML
    private PasswordField TFconfirm;

    @FXML
    private PasswordField TFnouveaumdp;

    @FXML
    void RetourLogin(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Login.fxml"));
            TFnouveaumdp.getScene().setRoot(root);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Sorry");
            alert.setTitle("Error");
            alert.show();
        }

    }

    @FXML
    void ok(ActionEvent event) {
        String nouveau =TFnouveaumdp.getText();
        String confirm = TFconfirm.getText();
        ServiceUtilisateur serviceUtilisateur = new ServiceUtilisateur();
        if (nouveau.isEmpty() || confirm.isEmpty()  ) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Tous les champs sont obligatoires !");
            alert.showAndWait();
            return;
        }
        if (!nouveau.equals(confirm))
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("les champs ne sont pas identiques !");
            alert.showAndWait();
            return;
        }
        else
        {
            try {
                Utilisateur u = serviceUtilisateur.getOneByCin(CurrentUser.getCin());
                u.setMdp(nouveau);
                serviceUtilisateur.modifier_par_cin(u);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("MDP MODIFIEE");
                alert.setTitle("GG");
                alert.show();
                Parent root = null;
                try {
                    root = FXMLLoader.load(getClass().getResource("/Login.fxml"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                TFnouveaumdp.getScene().setRoot(root);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }

    }

}
