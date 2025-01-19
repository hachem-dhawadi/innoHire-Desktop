package edu.esprit.controllers;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import edu.esprit.entities.CurrencyConverter;
import edu.esprit.entities.CurrentUser;
import edu.esprit.entities.Etablissement;
import edu.esprit.entities.Wallet;
import edu.esprit.services.ServiceEtablissement;
import edu.esprit.services.ServiceWallet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class ModifierWallet2Controller implements Initializable {
    private int idW ;
    private int CodeInit;
    @FXML
   private AnchorPane mainAnchor;
    @FXML
    private TextField BalanceETF;
    @FXML
    private TextField statusETF;

    @FXML
    private TextField dateCreationETF;

    @FXML
    private TextField code_EtabETF;

    @FXML
    private Button rechargerBtn;
    @FXML
    private Button recharger10Btn;
    @FXML
    private Button updateButton;
    @FXML
    private ImageView changeStatus;
  @FXML
   private TextField prixReacharge;

    private final ServiceWallet serviceWallet = new ServiceWallet();
    public int getIdW() {
        return idW;
    }

    public void setIdW(int idW) {
        this.idW = idW;
    }

    public int getCodeInit() {
        return CodeInit;
    }

    public void setCodeInit(int codeInit) {
        CodeInit = codeInit;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        code_EtabETF.setVisible(false);
        if (CurrentUser.getRole()==0)
        {
            rechargerBtn.setVisible(false);
            rechargerBtn.setManaged(false);
                    recharger10Btn.setVisible(false);
            recharger10Btn.setManaged(false);
            prixReacharge.setVisible(false);
            prixReacharge.setManaged(false);
            BalanceETF.setEditable(true);

        }
        else{
            updateButton.setVisible(false);
            BalanceETF.setEditable(false);
            changeStatus.setVisible(false);


        }




    }
    public static void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public boolean controlSaisie(TextField field) {
        if (field.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Champ vide", "Veuillez remplir tous les champs.");
            return false;
        }
        return true;
    }

    public void initDataWallet(Wallet wallet) {
        if (wallet != null) {
            setIdW(wallet.getIdWallet());

            BalanceETF.setText(String.valueOf(wallet.getBalance()));
            statusETF.setText(wallet.getStatus() == 1 ? "Actif" : "Non Actif");
            code_EtabETF.setText(String.valueOf(wallet.getEtablissement().getCodeEtablissement()));
            dateCreationETF.setText(String.valueOf(wallet.getDateCreation()));
            setCodeInit(wallet.getEtablissement().getCodeEtablissement());

        }
    }

    public void okWallet1(ActionEvent actionEvent) throws SQLException {
        if (controlSaisie(BalanceETF)&&controlSaisie(statusETF)) {
            int Balance ;
            try {
                Balance = Integer.parseInt(BalanceETF.getText());

            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Format invalide", "Balance doit etre un nombre valide.");
                return;
            }

            String statusText = statusETF.getText();
            int status;

            if ("Actif".equals(statusText)) {
                status = 1;
            } else if ("Non Actif".equals(statusText)) {
                status = 0;
            } else {
                showAlert(Alert.AlertType.ERROR, "Format invalide", "Le statut doit être 'Actif' ou 'Non Actif'.");
                return;
            }





            int code_Etab = Integer.parseInt(code_EtabETF.getText());


            Wallet newWallet = new Wallet();

            newWallet.setIdWallet(getIdW());

            newWallet.setBalance(Balance);
            newWallet.setStatus(status);


            ServiceEtablissement se = new ServiceEtablissement();
            Etablissement etab = se.getOneByCode(code_Etab);


            newWallet.setEtablissement(etab);

            if (getCodeInit() != etab.getCodeEtablissement()) {
                if (serviceWallet.portefeuilleExistePourEtablissement(etab.getCodeEtablissement())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur");
                    alert.setHeaderText(null);
                    alert.setContentText("Erreur : Cet établissement est déjà associé à un portefeuille. La modification n'est pas autorisée.");
                    alert.showAndWait();
                    return;
                }
            }


            serviceWallet.modifier(newWallet);

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Service modifié avec succès");
            AfficherWallet(actionEvent);

        }
    }

    public void AfficherWallet(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Etablissement.fxml"));
            Parent root = loader.load();

// Set your preferred width and height
            double preferredWidth = 1451;
            double preferredHeight = 830;

            Scene currentScene = mainAnchor.getScene();



            currentScene.setRoot(root);
            currentScene.getWindow().setWidth(preferredWidth);
            currentScene.getWindow().setHeight(preferredHeight);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void changeStatus(MouseEvent mouseEvent) {

            String currentStatus = statusETF.getText();

            if ("Actif".equals(currentStatus)) {
                // Changement à "Non Actif"
                statusETF.setText("Non Actif");
            } else if ("Non Actif".equals(currentStatus)) {
                // Changement à "Actif"
                statusETF.setText("Actif");
            } else {
                // Gérez le cas où le texte ne correspond ni à "Actif" ni à "Non Actif"
                System.out.println("Statut inconnu : " + currentStatus);
            }
        }


    public void recharger10Btn(ActionEvent actionEvent) {
        if (showConfirmationAlert("Confirmer la recharge de "+prixReacharge.getText()+" DT ?")) {
            // Logique à exécuter si l'utilisateur a confirmé
            try {
// Set your secret key here
                Stripe.apiKey = "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";

                int amount;
                try {
                    amount = Integer.parseInt(prixReacharge.getText());

                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.ERROR, "Format invalide", "prix a recharge doit etre un nombre valide.");
                    return;
                }
                int amountEur = (int) Math.ceil(CurrencyConverter.convertToEUR(amount));
                long amountInCents = amountEur * 100L;


// Create a PaymentIntent with other payment details
                PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                        .setAmount(amountInCents) // Amount in cents (e.g., $10.00)
                        .setCurrency("eur")
                        .build();

                System.out.println(params);
                PaymentIntent intent = PaymentIntent.create(params);

// If the payment was successful, display a success message
                System.out.println("Payment successful. PaymentIntent ID: " + intent.getId());
            } catch (StripeException e) {
// If there was an error processing the payment, display the error message
                System.out.println("Payment failed. Error: " + e.getMessage());
            }
        }
    }




    public void rechargerBtn(ActionEvent actionEvent) {
        if (showConfirmationAlert("Confirmer l'attachement su carte bancaire ?")) {
            // Spécifiez l'URL vers laquelle vous souhaitez rediriger (Google dans cet exemple)
            String targetUrl = "https://buy.stripe.com/5kAcPs7zb9hV4s88ww";

            // Utilisez Desktop pour ouvrir l'URL dans le navigateur par défaut
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(new URI(targetUrl));
                } catch (IOException | URISyntaxException e) {
                    e.printStackTrace(); // Gérez les exceptions selon vos besoins
                }
            }


        }
    }



    private boolean showConfirmationAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText(message);

        // Ajouter les boutons OK et Annuler à l'alerte
        alert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);

        // Afficher l'alerte et attendre la réponse de l'utilisateur
        return alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
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

                ServiceWallet serviceWallet = new ServiceWallet();
                serviceWallet.supprimer(idW);
                AfficherWallet(actionEvent);


            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}


