package edu.esprit.controllers;
import edu.esprit.entities.*;
import edu.esprit.services.ServiceEtablissement;
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

public class OTPController {
    @FXML
    private Label LabelCIN;

    @FXML
    private TextField TFcode;

    @FXML
    void OTPverifyAction(ActionEvent event) {
        int code;
        ServiceUtilisateur serviceUtilisateur = new ServiceUtilisateur() ;
        try {
            code = Integer.parseInt(TFcode.getText());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("ENTREZ LE CODE ENVOYE VIA MAIL !");
            alert.showAndWait();
            return;
        }
        if (code!= CurrentUser.getOtp())
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Le code OTP n'est pas juste !");
            alert.showAndWait();
        }
        else
        {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/Newmdp.fxml"));
                TFcode.getScene().setRoot(root);
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Sorry");
                alert.setTitle("Error");
                alert.show();
            }
        }


    }
}
