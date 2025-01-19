package edu.esprit.controllers;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import jdk.jshell.execution.Util;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;


public class ChangeMDPController {
    @FXML
    private PasswordField TFancienmdp;

    @FXML
    private PasswordField TFnouveau;
    @FXML
    private PasswordField TFnouveau_confirm;



    @FXML
    void RetourAccueil(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Pub.fxml"));
            TFancienmdp.getScene().setRoot(root);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Sorry");
            alert.setTitle("Error");
            alert.show();
        }

    }

    @FXML
    void ok(ActionEvent event) throws WriterException, IOException {
        ServiceUtilisateur su = new ServiceUtilisateur();
        String ancienMDP = su.hashPassword(TFancienmdp.getText());

        try {
            String verifmdp = su.getMDPfromCIN(CurrentUser.getCin());
            System.out.println(ancienMDP);
            System.out.println(verifmdp);
            Utilisateur u = su.getOneByCin(CurrentUser.getCin());
            System.out.println(u);

            if (!TFnouveau.getText().equals(TFnouveau_confirm.getText())) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("CONFIRMATION INCORRECT");
                alert.setTitle("Error");
                alert.show();
                return;
            }

            if (ancienMDP.equals(verifmdp)) {
                // You can directly set the new password without rehashing
                u.setMdp(TFnouveau.getText());
                try {
                    su.modifier_par_cin(u);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                String text = TFnouveau.getText();

                Code128Writer writer = new Code128Writer();
                BitMatrix matrix = writer.encode(text, BarcodeFormat.CODE_128, 500, 300);

                // Create a BufferedImage from the BitMatrix
                BufferedImage image = MatrixToImageWriter.toBufferedImage(matrix);

                // Show a file dialog to choose the save location
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Save Barcode Image");
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JPEG files (.jpg)", ".jpg"));
                File outputFile = fileChooser.showSaveDialog(new Stage()); // Initialize a new Stage

                if (outputFile != null) {
                    ImageIO.write(image, "jpg", outputFile);
                    System.out.println("Barcode saved to: " + outputFile.getAbsolutePath());
                } else {
                    System.out.println("User canceled the save operation.");
                }

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("MDP MODIFIEE");
                alert.setTitle("GG");
                alert.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("ANCIEN MDP INCORRECT");
                alert.setTitle("Error");
                alert.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}
