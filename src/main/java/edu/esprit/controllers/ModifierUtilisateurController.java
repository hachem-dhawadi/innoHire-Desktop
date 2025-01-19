package edu.esprit.controllers;

import edu.esprit.entities.Admin;
import edu.esprit.entities.Representant;
import edu.esprit.entities.Utilisateur;
import edu.esprit.services.ServiceUtilisateur;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ModifierUtilisateurController  {


    private int id ;
    @FXML
    private TextField TFcin;
    @FXML
    private TextField imageETF;

    @FXML
    private TextField TFadresse;

    @FXML
    private PasswordField TFmdp;

    @FXML
    private TextField TFnom;

    @FXML
    private TextField TFprenom;

    @FXML
    private TextField TFrole;
    @FXML
    private ImageView profileImageView;



    private final ServiceUtilisateur serviceUtilisateur = new ServiceUtilisateur();
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
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

    public void initData(Utilisateur utilisateur) {
        if (utilisateur != null) {
            TFcin.setText(String.valueOf(utilisateur.getCin()));
            TFnom.setText(utilisateur.getNom());  // Corrected line
            TFprenom.setText(utilisateur.getPrenom());
            TFadresse.setText(utilisateur.getAdresse());
            TFmdp.setText(utilisateur.getMdp());
            imageETF.setText(serviceUtilisateur.getImagefromCin(utilisateur.getCin()));

            String imagePath = serviceUtilisateur.getImagefromCin(utilisateur.getCin());
            if (imagePath != null && !imagePath.isEmpty()) {
                Image image = new Image("file:" + imagePath); // Assuming imagePath is a file path
                profileImageView.setImage(image);
            }

            // Assign the cin to the user
            if (utilisateur instanceof Admin) {
                TFrole.setText("Admin");
            } else if (utilisateur instanceof Representant) {
                TFrole.setText("Representant");
            } else {
                TFrole.setText("Candidat");
            }
        }
    }



    @FXML
    void ok(ActionEvent event) throws SQLException {
        String photoUrl = (selectedFile != null) ? selectedFile.toURI().toString() : null;
        //int role = Integer.parseInt(TFrole.getText());
        if (controlSaisie(TFnom) && controlSaisie(TFprenom) && controlSaisie(TFadresse) && controlSaisie(TFmdp)) {
            Utilisateur u = new Utilisateur();
            u.setCin(Integer.parseInt(TFcin.getText()));
            u.setNom(TFnom.getText());
            u.setPrenom(TFprenom.getText());
            u.setAdresse(TFadresse.getText());
            u.setMdp(TFmdp.getText());
            u.setImage(serviceUtilisateur.getImagefromCin(u.getCin()));




            String Nom = TFnom.getText();
            String prenom = TFprenom.getText();
            String adresse = TFadresse.getText();
            String mdp = TFmdp.getText();


            // Vérifier si les champs requis sont vides
            if (Nom.isEmpty() || prenom.isEmpty() || adresse.isEmpty() || mdp.isEmpty() ) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Tous les champs sont obligatoires !");
                alert.showAndWait();
                return;
            }
            if (Nom.matches(".*[\\d\\W].*")) {
                // Nom contains digits or symbols
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Le champ 'Nom' ne doit pas contenir de chiffres ou de symboles !");
                alert.showAndWait();
                return;
            }

            if (prenom.matches(".*[\\d\\W].*")) {
                // Prenom contains digits or symbols
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Le champ 'Prenom' ne doit pas contenir de chiffres ou de symboles !");
                alert.showAndWait();
                return;
            }
            String emailPattern = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,6}$";
            Pattern pattern = Pattern.compile(emailPattern);
            Matcher matcher = pattern.matcher(adresse);

            if (!matcher.matches()) {
                // Invalid email format
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Adresse email invalide !");
                alert.showAndWait();
                return;
            }
                Utilisateur utilisateur = serviceUtilisateur.getOneByCin(u.getCin());
                if(!mdp.equals(utilisateur.getMdp()))
                {serviceUtilisateur.modifier_par_cin(u);
                    serviceUtilisateur.modifier_Image(u,imageETF.getText());
                }
                else
                {
                    serviceUtilisateur.modifier_par_cin_sansmdp(u);
                    serviceUtilisateur.modifier_Image(u,imageETF.getText());
                }

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Utilisateur modifié avec succès");  // Corrected line
            AfficherUtilisateur(event);
        }
    }


   /* public void AfficherEtablissement(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherEtablissement.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) NomETF.getScene().getWindow(); // Utilisez la même fenêtre (Stage) actuelle
            stage.setScene(new Scene(root));
            stage.show();

            // Vous pouvez fermer la fenêtre actuelle si nécessaire
            // ((Node)(event.getSource())).getScene().getWindow().hide();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    public void AfficherUtilisateur(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/listUsers.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) TFnom.getScene().getWindow(); // Utilisez la même fenêtre (Stage) actuelle
            stage.setScene(new Scene(root));
            stage.show();

            // Vous pouvez fermer la fenêtre actuelle si nécessaire
            // ((Node)(event.getSource())).getScene().getWindow().hide();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private File selectedFile;
    @FXML
    void choosePhoto(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");

        // Set the initial directory to the img folder in the resources
        String currentDir = System.getProperty("user.dir");
        fileChooser.setInitialDirectory(new File(currentDir + "/src/main/resources/img"));

        // Set the file extension filters if needed (e.g., for images)
        FileChooser.ExtensionFilter imageFilter =
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif");
        fileChooser.getExtensionFilters().add(imageFilter);

        // Show the file chooser dialog
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            // The user selected a file, you can handle it here
            String imagePath = selectedFile.toURI().toString();

            // Set the image file name to the TextField


            // Display the image in the ImageView
            Image image = new Image(imagePath);



            // Do something with the imagePath, for example, display the image
            // imageView.setImage(new Image(imagePath));

            System.out.println("Selected Image: " + imagePath);
            System.out.println(selectedFile.getName());
            profileImageView.setImage(image);
            imageETF.setText(selectedFile.getName());


        } else {
            // The user canceled the operation
            System.out.println("Operation canceled.");
        }
    }



}
