package edu.esprit.controllers;

import edu.esprit.entities.CurrentUser;
import edu.esprit.entities.Utilisateur;
import edu.esprit.services.ServiceUtilisateur;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javax.imageio.ImageIO;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.sql.SQLException;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import java.io.IOException;
import java.nio.file.Paths;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField TFcin;

    @FXML
    private PasswordField TFmdp;
    ServiceUtilisateur su = new ServiceUtilisateur();

    @FXML
    void loginAction(ActionEvent event) {
        String mdp = TFmdp.getText();
        ServiceUtilisateur sp = new ServiceUtilisateur();
        String cinText = TFcin.getText().trim();

        // Check if TFcin is not empty and contains a numeric value
        if (cinText.isEmpty() || !cinText.matches("\\d+")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Veuillez entrer un nombre valide dans le champ CIN.");
            alert.setTitle("Erreur");
            alert.show();
            return;
        }
        int cin = Integer.parseInt(TFcin.getText());
        int id = sp.getIdByCin(cin);


        if (mdp.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("REMPLIR MDP");
            alert.setTitle("NON");
            alert.show();
        }
        /*else if (!sp.utilisateurExiste(cin))
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("CIN n EXISTE PAS");
            alert.setTitle("NON");
            alert.show();
        }*/

        else if (sp.utilisateurExiste(cin)) {
            try {
                Utilisateur u = sp.getOneByCin(cin);
                String hashed_mdp = sp.hashPassword(mdp);
                if (!u.getMdp().equals(hashed_mdp)) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("MDP INCORRECT");
                    alert.setTitle("NON");
                    alert.show();
                    return;
                } else {
                    int role = sp.verifyRoleByCin(cin);
                    if (role == 0) {
                        CurrentUser.setCin(u.getCin());
                        CurrentUser.setNom(u.getNom());
                        CurrentUser.setPrenom(u.getPrenom());
                        CurrentUser.setAdresse(u.getAdresse());
                        CurrentUser.setMdp(u.getMdp());
                        CurrentUser.setRole(0);
                        CurrentUser.setProfileImagePath(su.getImagefromCin(CurrentUser.getCin()));
                        System.out.println(CurrentUser.getProfileImagePath());
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText("WELCOME ADMINE");
                        alert.setTitle("Oui");
                        alert.show();
                        try {
                            Parent root = FXMLLoader.load(getClass().getResource("/listUsers.fxml"));
                            TFcin.getScene().setRoot(root);
                        } catch (IOException e) {
                            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                            alert1.setContentText("Sorry");
                            alert1.setTitle("Error");
                            alert1.show();
                        }
                    } else if (role == 1) {
                        CurrentUser.setCin(u.getCin());
                        CurrentUser.setNom(u.getNom());
                        CurrentUser.setPrenom(u.getPrenom());
                        CurrentUser.setAdresse(u.getAdresse());
                        CurrentUser.setMdp(u.getMdp());
                        CurrentUser.setRole(1);
                        CurrentUser.setProfileImagePath(su.getImagefromCin(CurrentUser.getCin()));
                        System.out.println(CurrentUser.getProfileImagePath());
                        int status = sp.getStatusfromCIN(u.getCin());
                        if (status == 1) {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setContentText("WELCOME Rep");
                            alert.setTitle("Oui");
                            alert.show();
                            try {
                                Parent root = FXMLLoader.load(getClass().getResource("/Pub.fxml"));
                                TFcin.getScene().setRoot(root);
                            } catch (IOException e) {
                                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                                alert1.setContentText("Sorry");
                                alert1.setTitle("Error");
                                alert1.show();
                            }
                        } else if (status == 0 && !sp.verifyUser_Etab(id)) {
                            System.out.println("first");
                            System.out.println(status);
                            System.out.println(u.getId_utilisateur());
                            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                            alert1.setContentText("VEILLEZ creer un etablissement");
                            alert1.setTitle("Error");
                            alert1.show();
                            Parent root = null;
                            try {
                                root = FXMLLoader.load(getClass().getResource("/AjouterEtablissementAfterCreate.fxml"));
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            TFcin.getScene().setRoot(root);
                        } else if (status == 0 && sp.verifyUser_Etab(id)) {
                            System.out.println("last");
                            System.out.println(status);
                            sp.verifyUser_Etab(u.getId_utilisateur());
                            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                            alert1.setContentText("EN ATTENTE");
                            alert1.setTitle("ATTENDRE");
                            alert1.show();
                            Parent root = null;
                            try {
                                root = FXMLLoader.load(getClass().getResource("/EnAttente.fxml"));
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            TFcin.getScene().setRoot(root);
                        }
                    } else if (role == 2) {
                        CurrentUser.setCin(u.getCin());
                        CurrentUser.setNom(u.getNom());
                        CurrentUser.setPrenom(u.getPrenom());
                        CurrentUser.setAdresse(u.getAdresse());
                        CurrentUser.setMdp(u.getMdp());
                        CurrentUser.setRole(2);
                        CurrentUser.setProfileImagePath(su.getImagefromCin(CurrentUser.getCin()));
                        System.out.println(CurrentUser.getProfileImagePath());
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText("WELCOME CANDIDAT");
                        alert.setTitle("Oui");
                        alert.show();
                        try {
                            Parent root = FXMLLoader.load(getClass().getResource("/Pub.fxml"));
                            TFcin.getScene().setRoot(root);
                        } catch (IOException e) {
                            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                            alert1.setContentText("Sorry");
                            alert1.setTitle("Error");
                            alert1.show();
                        }
                    }

                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("USER AVEC CE CIN N EXISTE PAS");
            alert.setTitle("Error");
            alert.show();
        }
    }

    @FXML
    void navigateToCreateAccount(ActionEvent event) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/CreateAccount.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        TFcin.getScene().setRoot(root);


    }

    @FXML
    void navigateToPassRecovery(ActionEvent event) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/getCinforpassRec.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        TFcin.getScene().setRoot(root);

    }

  /*@FXML
    void navigateToQR(ActionEvent event) throws IOException, WriterException {
        try {
            String text = "test";

            Code128Writer writer = new Code128Writer();
            BitMatrix matrix = writer.encode(text, BarcodeFormat.CODE_128, 500, 300);

            // Create a BufferedImage from the BitMatrix
            BufferedImage image = MatrixToImageWriter.toBufferedImage(matrix);

            // Show a file dialog to choose the save location
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Barcode Image");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JPEG files (*.jpg)", "*.jpg"));
            File outputFile = fileChooser.showSaveDialog(new Stage()); // Initialize a new Stage

            if (outputFile != null) {
                ImageIO.write(image, "jpg", outputFile);
                System.out.println("Barcode saved to: " + outputFile.getAbsolutePath());
            } else {
                System.out.println("User canceled the save operation.");
            }
        } catch (Exception e) {
            System.out.println("Error while creating barcode: " + e.getMessage());
        }
    }*/

}


