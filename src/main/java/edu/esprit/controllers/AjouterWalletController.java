package edu.esprit.controllers;

import edu.esprit.entities.CurrentEtablissement;
import edu.esprit.entities.CurrentUser;
import edu.esprit.entities.Etablissement;
import edu.esprit.entities.Wallet;
import edu.esprit.services.ServiceEtablissement;
import edu.esprit.services.ServiceWallet;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.Set;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AjouterWalletController implements Initializable {
    //--------------------wallet
    @FXML
    private TextField BalanceETF;



    @FXML
    private TextField code_EtabETF;

    @FXML
    private TextField statusETF;

    @FXML
    private TextField dateCreationETF;
    @FXML
    private Label labelRegle;
    @FXML
    private CheckBox checkBoxRegle;

    @FXML
    private ImageView changeStatus;
    private final ServiceWallet sw = new ServiceWallet();
    private final ServiceEtablissement se = new ServiceEtablissement();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        LocalDateTime currentDate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = currentDate.format(formatter);
        dateCreationETF.setText(formattedDate);
        code_EtabETF.setVisible(false);
        statusETF.setText("En attente de Confirmation Admin");

        if (CurrentEtablissement.getIdEtablissement()!=0) {
      Etablissement etablissement = null;
     try {
         etablissement = se.getOneByID(CurrentEtablissement.getIdEtablissement());
         } catch (SQLException e) {
        throw new RuntimeException(e);
    }
    code_EtabETF.setText(String.valueOf(etablissement.getCodeEtablissement()));
    code_EtabETF.setEditable(false);


}

        if (CurrentUser.getRole() != 0) {
            BalanceETF.setText("0");
            BalanceETF.setEditable(false);
            statusETF.setEditable(false);
            changeStatus.setVisible(false);
        }
        else {
            labelRegle.setVisible(false);
            labelRegle.setManaged(false);
            checkBoxRegle.setVisible(false);
            checkBoxRegle.setManaged(false);
        }





    }
    public void ajouterWalletAction(ActionEvent actionEvent) throws SQLException {
        ServiceWallet serviceWallet = new ServiceWallet();

        // Récupérer les valeurs des champs du formulaire
        String Balance = BalanceETF.getText();
        String Date=dateCreationETF.getText();
        String status = statusETF.getText();



        // Vérifier si les champs requis sont vides
        if (Balance.isEmpty()||Date.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Tous les champs sont obligatoires !");
            alert.showAndWait();
            return;
        }


        // Vérifier si le prix est un nombre valide
        int balanceE;
        try {
            balanceE = Integer.parseInt(Balance);
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Le balance doit être un nombre valide !");
            alert.showAndWait();
            return;
        }

          // Replace this with the actual status text

        int statueE;

// Check the status text and set statueE accordingly
        if ("En attente de Confirmation Admin".equals(status)) {
            statueE = 0;
        }  else {
            statueE = 1;
        }







        String code_Etab = code_EtabETF.getText();
        int code_EtabE;


            try {
                code_EtabE = Integer.parseInt(code_Etab);
            } catch (NumberFormatException e) { Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Le code_etab doit être un nombre valide !");
                alert.showAndWait();
                return;
            }


// Vérifier si un portefeuille existe déjà pour cet établissement
        if (serviceWallet.portefeuilleExistePourEtablissement(code_EtabE)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Un portefeuille existe déjà pour cet établissement !");
            alert.showAndWait();
            return;
        }

        if (CurrentUser.getRole()!=0) {
            if (!checkBoxRegle.isSelected()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Veuillez accepter les conditions d'utilisation.");
                alert.showAndWait();
                return;
            }
        }


        // Créer un nouvel objet Service avec les valeurs saisies
        Wallet wallet = new Wallet();
        wallet.setBalance(balanceE);
        wallet.setStatus(statueE);
        ServiceEtablissement se=new ServiceEtablissement();
        Etablissement etab = se.getOneByCode(code_EtabE);
        wallet.setEtablissement(etab);

        // Ajouter le service à la base de données
        try {
            serviceWallet.ajouter(wallet);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Succès");
            alert.setHeaderText(null);
            alert.setContentText("Wallet ajouté avec succès !");
            alert.showAndWait();

            // Effacer les champs du formulaire après l'ajout réussi
            BalanceETF.clear();
            statusETF.clear();
            code_EtabETF.clear();

            navigatetoAfficherEtablissementAction(actionEvent);

        }

        catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Erreur lors de l'ajout de Wallet' : " + e.getMessage());
            alert.showAndWait();
        }



    }

    public void navigatetoAfficherEtablissementAction(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Etablissement.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) BalanceETF.getScene().getWindow(); // Utilisez la même fenêtre (Stage) actuelle
            stage.setScene(new Scene(root));
            stage.show();

            // Vous pouvez fermer la fenêtre actuelle si nécessaire
            // ((Node)(event.getSource())).getScene().getWindow().hide();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void changeStatus(MouseEvent mouseEvent) {
        String currentStatus = statusETF.getText();

        if ("Actif".equals(currentStatus)) {
            // Changement à "Non Actif"
            statusETF.setText("En attente de Confirmation Admin");
        } else if ("En attente de Confirmation Admin".equals(currentStatus)) {
            // Changement à "Actif"
            statusETF.setText("Actif");
        } else {
            // Gérez le cas où le texte ne correspond ni à "Actif" ni à "Non Actif"
            System.out.println("Statut inconnu : " + currentStatus);
        }
    }
}

