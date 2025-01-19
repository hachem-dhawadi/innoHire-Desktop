package edu.esprit.controllers;

import edu.esprit.entities.CurrentUser;
import edu.esprit.entities.Quiz;
import edu.esprit.services.quizService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.Set;

public class AfficherQuizDisponibleController implements Initializable {
    @FXML
    private GridPane gridA;

    @FXML
    private ScrollPane scrollA;

    @FXML
    private Label receiverNameLabel21;

    @FXML
    private Label LabelMail;

    private quizService serviceQ = new quizService();
    Set<Quiz> setQ;

    {
        try {
            setQ = serviceQ.getAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private void remplirReceiverNameLabel() {
        // Assurez-vous que CurrentUser.getNom() renvoie la valeur souhaitée.
        String nomUtilisateur = CurrentUser.getNom();

        // Mettez à jour le texte du Label avec le nom de l'utilisateur.
        receiverNameLabel21.setText(nomUtilisateur);
    }
    private void remplirReceiverMailLabel() {
        // Assurez-vous que CurrentUser.getNom() renvoie la valeur souhaitée.
        String mail = CurrentUser.getAdresse();

        // Mettez à jour le texte du Label avec le nom de l'utilisateur.
        LabelMail.setText(mail);
    }








    public AfficherQuizDisponibleController() throws SQLException {
    }


    public void initialize(URL url, ResourceBundle resourceBundle) {
        remplirReceiverNameLabel();
        remplirReceiverMailLabel();

        int column = 0;
        int row = 1;
        try {
            for (Quiz quiz1 : setQ) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/QuizPourAcheter.fxml"));
                HBox hbox = fxmlLoader.load();

                QuizPourAcheterController itemController = fxmlLoader.getController();
                itemController.setData(quiz1);

                if (column == 1) {
                    column = 0;
                    row++;
                }

                gridA.add(hbox, column++, row);
                gridA.setMinWidth(Region.USE_COMPUTED_SIZE);
                gridA.setPrefWidth(Region.USE_COMPUTED_SIZE);
                gridA.setMaxWidth(Region.USE_PREF_SIZE);
                gridA.setMinHeight(Region.USE_COMPUTED_SIZE);
                gridA.setPrefHeight(Region.USE_COMPUTED_SIZE);
                gridA.setMaxHeight(Region.USE_PREF_SIZE);

                GridPane.setMargin(hbox, new Insets(10));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void navigateToAfficher(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherQuizAchetés.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) gridA.getScene().getWindow(); // Utilisez la même fenêtre (Stage) actuelle
            stage.setScene(new Scene(root));
            stage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
