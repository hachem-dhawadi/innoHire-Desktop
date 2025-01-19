package edu.esprit.controllers;


import edu.esprit.entities.Commentaire;
import edu.esprit.entities.CurrentPost;

import edu.esprit.entities.Post;
import edu.esprit.services.ServiceCommentaire;
import edu.esprit.services.ServicePost;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

public class AfficherCommentaireController2 implements Initializable {

  // Assurez-vous que l'attribut fx:id correspond à celui dans votre fichier FXML



    @FXML
    private AnchorPane NavBar;
    @FXML
    private AnchorPane grandAnchor;
    @FXML
    private AnchorPane anchorContenu;
    @FXML
    private AnchorPane selecUserAnchor;
    @FXML
    private GridPane gridA;
    @FXML
    private ScrollPane scrollA;
    ServiceCommentaire serviceC = new ServiceCommentaire();








    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Set<Commentaire> setC;


          /*  //-----------------------Navebar setup
            NavBar.setVisible(false);
            NavBar.setManaged(false);

            anchorContenu.setLayoutX(0);
            grandAnchor.setPrefWidth(anchorContenu.getPrefWidth());
            //-------------------------------------*/

        ServiceCommentaire sc = new ServiceCommentaire();
        ServicePost sp = new ServicePost();

        Post publicationConnecte;
        try {
            publicationConnecte = sp.getOneByID(CurrentPost.getId_post());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        Set<Commentaire> commentaireConnecte;

        try {
            commentaireConnecte = sc.getListCommentsByPost(publicationConnecte);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        setC = commentaireConnecte;




        int column = 0;
        int row = 1;
        try {
            for (Commentaire commentaire :setC) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/ItemCommentaire.fxml"));
                HBox hbox = fxmlLoader.load();

                  ItemCommentController itemController = fxmlLoader.getController();

                 itemController.setData(commentaire);

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




    public void ajouterCommentaire(ActionEvent actionEvent) {

        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/AjouterCommentaire.fxml")));
            gridA.getScene().setRoot(root);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Sorry");
            alert.setTitle("Error");
            alert.show();
        }
    }


    public void back(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Pub.fxml")));
            gridA.getScene().setRoot(root);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Sorry");
            alert.setTitle("Error");
            alert.show();
        }
    }
}

