package edu.esprit.controllers;
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

import javax.imageio.ImageIO;
import javax.swing.plaf.ColorUIResource;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateAccountController implements Initializable{
    private File selectedFile;

    @FXML
    private TextField TFadresse;
    @FXML
    private TextField captchaETF;
    @FXML
    private ImageView profileImageCaptcha;
    @FXML
    private TextField imageETF;

    @FXML
    private TextField TFcin;

    @FXML
    private TextField TFnom;

    @FXML
    private TextField TFprenom;

    @FXML
    private ImageView profileImageView;


    @FXML
    private PasswordField tfmdp;

    @FXML
    private PasswordField tfmdp_confirm;
    @FXML
    private ComboBox<?> comboRole;

    @FXML
    void ajouterUtilisateurAction(ActionEvent event)  {
        // Créer une instance de ServiceService
        String photoUrl = (selectedFile != null) ? selectedFile.toURI().toString() : null;

        ServiceUtilisateur serviceUtilisateur = new ServiceUtilisateur();

        int cin;
       // int role;
        String Nom = TFnom.getText();
        String prenom = TFprenom.getText();
        String adresse = TFadresse.getText();
        String mdp = tfmdp.getText();
        String mdp_confirm= tfmdp_confirm.getText();

        if (Nom.isEmpty() || prenom.isEmpty() || adresse.isEmpty() || mdp.isEmpty() ||mdp_confirm.isEmpty() ) {
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
        if (imageETF.getText().equals("no Photo chosen"))
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("AJOUTER PHOTO !");
            alert.showAndWait();
            return;
        }
        if (captchaETF.getText().equals("no capctha generated"))
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("GENERER CAPCTHA POUR VERIFICATION!");
            alert.showAndWait();
            return;
        }
        if (!captchaETF.getText().equals(CurrentUser.getCaptcha()))
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Ecrire le captcha correctement!");
            alert.showAndWait();
            return;
        }

        if (!mdp.equals(mdp_confirm))
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("CONFIRMATION DE MDP NON CORRECT!");
            alert.showAndWait();
            return;
        }



        try {
            cin = Integer.parseInt(TFcin.getText());


        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Le cin et le role doivent etre être un nombre valide !");
            alert.showAndWait();
            return;
        }
        if(cin<0)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Le cin  doivent etre être un nombre valide !");
            alert.showAndWait();
            return;
        }



        if (serviceUtilisateur.utilisateurExiste(cin))
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Le cin doit etre unique");
            alert.showAndWait();
            return;

        }
        String role = (String) comboRole.getSelectionModel().getSelectedItem();





        // Créer un nouvel objet Service avec les valeurs saisies
        if (role==null)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("veillez choisir un role");
            alert.showAndWait();
        }
        else if(role.equals("Representant"))
        {Representant u = new Representant();
            u.setNom(Nom);
            u.setCin(cin);
            u.setMdp(mdp);
            u.setAdresse(adresse);
            u.setPrenom(prenom);
            u.setImage(imageETF.getText());
            try {
                serviceUtilisateur.ajouter(u);
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Succès");
                alert.setHeaderText(null);
                alert.setContentText("Representant ajouté avec succès.Veillez ajouter un etablissement !");
                alert.showAndWait();
                CurrentUser.setCin(Integer.parseInt(TFcin.getText()));
                CurrentUser.setId_utilisateur(serviceUtilisateur.getUserIdByCin(CurrentUser.getCin()));


                        // Effacer les champs du formulaire après l'ajout réussi
                TFcin.clear();
                TFprenom.clear();
                TFnom.clear();
                TFadresse.clear();
                tfmdp.clear();

                Parent root = FXMLLoader.load(getClass().getResource("/AjouterEtablissementAfterCreate.fxml"));
                TFadresse.getScene().setRoot(root);
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Erreur lors de l'ajout d'Utilisateur' : " + e.getMessage());
                alert.showAndWait();


            }


        }
        else if (role .equals("Candidat")){
            {Candidat u = new Candidat();
                u.setNom(Nom);
                u.setCin(cin);
                u.setMdp(mdp);
                u.setAdresse(adresse);
                u.setPrenom(prenom);
                u.setImage(imageETF.getText());
                try {
                    serviceUtilisateur.ajouter(u);
                    serviceUtilisateur.modifier_Status_par_cin(u.getCin());
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Succès");
                    alert.setHeaderText(null);
                    alert.setContentText("Candidat ajouté avec succès !");
                    alert.showAndWait();

                    // Effacer les champs du formulaire après l'ajout réussi
                    TFcin.clear();
                    TFprenom.clear();
                    TFnom.clear();
                    TFadresse.clear();
                    tfmdp.clear();
                    CurrentUser.setCin(u.getCin());
                    CurrentUser.setNom(u.getNom());
                    CurrentUser.setPrenom(u.getPrenom());
                    CurrentUser.setAdresse(u.getAdresse());
                    CurrentUser.setMdp(u.getMdp());
                    CurrentUser.setRole(2);
                    Parent root = FXMLLoader.load(getClass().getResource("/Accueil.fxml"));
                    TFadresse.getScene().setRoot(root);

                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur");
                    alert.setHeaderText(null);
                    alert.setContentText("Erreur lors de l'ajout d'Utilisateur' : " + e.getMessage());
                    alert.showAndWait();
                }
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("ROLE DOIT ETRE  1 pour Rep et 2 pour candidat ");
            alert.showAndWait();
        }




        // Ajouter le service à la base de données

    }

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
    @FXML
    void cancelAjouter(ActionEvent event) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/Login.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        TFadresse.getScene().setRoot(root);

    }
    @FXML
    public void importImage(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");

        // Set the initial directory to the img folder in the resources
        String currentDir = System.getProperty("user.dir");
        fileChooser.setInitialDirectory(new File(currentDir + "/src/main/resources/img"));

        // Set the file extension filters if needed (e.g., for images)
        FileChooser.ExtensionFilter imageFilter =
                new FileChooser.ExtensionFilter("Image Files", ".png", ".jpg", ".jpeg", ".gif");
        fileChooser.getExtensionFilters().add(imageFilter);

        // Show the file chooser dialog
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            // The user selected a file, you can handle it here
            String imagePath = selectedFile.toURI().toString();

            // Set the image file name to the TextField
            imageETF.setText(selectedFile.getName());

            // Display the image in the ImageView
            Image image = new Image(imagePath);
            profileImageView.setImage(image);


            // Do something with the imagePath, for example, display the image
            // imageView.setImage(new Image(imagePath));
            System.out.println("Selected Image: " + imagePath);
        } else {
            // The user canceled the operation
            System.out.println("Operation canceled.");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        imageETF.setText("no Photo chosen");
        captchaETF.setText("no captcha generated");

    }

    @FXML
    void GenererCaptcha(ActionEvent event) {
        CaptchaGenerator captchaGenerator = new CaptchaGenerator();

        // Generate captcha
        CaptchaGenerator.CaptchaResult captchaResult = captchaGenerator.generateCaptcha();
        String captchaCode = captchaResult.getCaptchaCode();
        BufferedImage captchaImage = captchaResult.getCaptchaImage();

        // Print captcha code
        System.out.println("Generated Captcha Code: " + captchaCode);
        CurrentUser.setCaptcha(captchaCode);

        // Save the captcha image to a file (optional)
        try {
            ImageIO.write(captchaImage, "png", new File("captcha.png"));
            System.out.println("Captcha image saved to captcha.png");
            System.out.println(captchaCode);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Display the image in the ImageView
        Image image = new Image("file:./captcha.png");
        profileImageCaptcha.setImage(image);


        // Do something with the imagePath, for example, display the image
        // imageView.setImage(new Image(imagePath));
        System.out.println("Selected Image: " + "captcha.png");
    }
    }

