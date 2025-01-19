package edu.esprit.controllers;

import edu.esprit.entities.Etablissement;
import edu.esprit.services.ServiceEtablissement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import edu.esprit.services.MyListener;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class EtablissementItemController {
    @FXML
    private Label nameLabel;

    @FXML
    private Label codeLable;

    @FXML
    private ImageView img;

    @FXML
    private void click(MouseEvent mouseEvent) {
        myListener.onClickListener(etablissement);
    }

    private Etablissement etablissement;
    private MyListener myListener;


    public void setData(Etablissement etablissement, MyListener myListener) {
        this.etablissement = etablissement;
        this.myListener = myListener;
        nameLabel.setText(etablissement.getNom());

       codeLable.setText(String.valueOf(etablissement.getCodeEtablissement()));
        Image image = new Image(getClass().getResourceAsStream(etablissement.getImage()));
        img.setImage(image);
    }

    @FXML
    public void modifier(ActionEvent actionEvent) {
        // Code pour modifier l'établissement sélectionné dans la liste
        Etablissement selectedEtablissement = etablissement;
        if (selectedEtablissement == null) {
            // Aucun élément sélectionné, afficher une alerte
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Avertissement");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner un établissement à modifier.");
            alert.showAndWait();
            return; // Sortir de la méthode, car rien à modifier
        } else {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierEtablissement.fxml"));
                Parent root = loader.load();
                ModifierEtablissementController controller = loader.getController();
                controller.initData(selectedEtablissement); // Passer l'établissement sélectionné au contrôleur de l'interface de modification

                // Obtenir la scène actuelle
                Scene scene = ((Node) actionEvent.getSource()).getScene();

                // Changer le contenu de la scène
                scene.setRoot(root);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public void supprimer(ActionEvent actionEvent) {

        // Si un élément est sélectionné, afficher la confirmation de suppression
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText(null);
        alert.setContentText("Êtes-vous sûr de vouloir supprimer cette conversation ?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                int idEtablissement = etablissement.getIdEtablissement();
                ServiceEtablissement serviceEtablissement = new ServiceEtablissement();
                serviceEtablissement.supprimer(idEtablissement);
                myListener.onDeleteListener(etablissement);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
