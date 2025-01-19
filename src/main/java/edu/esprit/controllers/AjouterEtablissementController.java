package edu.esprit.controllers;


import edu.esprit.entities.CameraCapture;
import edu.esprit.entities.CurrentUser;
import edu.esprit.entities.Etablissement;
import edu.esprit.entities.Utilisateur;
import edu.esprit.services.ServiceEtablissement;

import edu.esprit.services.ServiceUtilisateur;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.scene.image.Image;

import javax.swing.*;


public class AjouterEtablissementController implements Initializable {

    @FXML
    private TextField CodeETF;

    @FXML
    private TextField cin_utilisateurETF;

    @FXML
    private TextField LieuETF;

    @FXML
    private TextField NomETF;


    @FXML
    private TextField imageETF;
    @FXML
    private ImageView imageViewETF;


    @FXML
    private GridPane gridA;

    @FXML
    private ScrollPane scrollA;

    @FXML
    AnchorPane selecUserAnchor;
    @FXML
    Label listeUsersLabels;
    @FXML
    private Label labelRegle;
    @FXML
    private CheckBox checkBoxRegle;

    @FXML
    private RadioButton faculteRadio, ecoleRadio, lyceeRadio, collegeRadio;
    @FXML
    private Label labelType;


    private ServiceUtilisateur serviceU = new ServiceUtilisateur();
    Set<Utilisateur> setU;

    {
        try {
            setU = serviceU.getAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private final ServiceEtablissement se = new ServiceEtablissement();


    @FXML
    void ajouterEtablissementAction(ActionEvent event) throws SQLException {
        // Créer une instance de ServiceService
        ServiceEtablissement serviceEtablissement = new ServiceEtablissement();

        // Récupérer les valeurs des champs du formulaire
        String Nom = NomETF.getText();
        String Lieu = LieuETF.getText();
        String Code = CodeETF.getText();
        //  String Type = TypeETF.getText();
        String image = imageETF.getText();


        // Vérifier si les champs requis sont vides
        if (Nom.isEmpty() || Lieu.isEmpty() || Code.isEmpty() || image.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Tous les champs sont obligatoires !");
            alert.showAndWait();
            return;
        }

        if (!Nom.matches("^[a-zA-Z]+(\\d+)?$")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Format invalide");
            alert.setContentText("Le champ Nom doit contenir des lettres seules ou des lettres suivies de chiffres !");
            alert.showAndWait();
            return;
        }


        if (serviceEtablissement.existeParNom(Nom)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Erreur : Un établissement avec le même nom existe déjà !");
            alert.showAndWait();
            return;
        }


        // Vérifier si le prix est un nombre valide
        int codeE;
        try {
            codeE = Integer.parseInt(Code);
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Le code doit être un nombre valide !");
            alert.showAndWait();
            return;
        }
        if (serviceEtablissement.existe(codeE)) {
            // Afficher une alerte si le code existe déjà
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Erreur Un établissement avec le même code existe déjà !");
            alert.showAndWait();

            return;
        }


        String currentDir = System.getProperty("user.dir");
        String imagePath = currentDir + "/src/main/resources/img/" + image;
        File imageFile = new File(imagePath);
        if (!imageFile.exists()) {
            // Afficher une alerte si l'image n'existe pas
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Erreur : L'image spécifiée n'existe pas !");
            alert.showAndWait();
            return;
        }


        String cin_utilisateur = cin_utilisateurETF.getText();
        int cin_utilisateurE;

        if (cin_utilisateur.isEmpty()) {
            // If the TextField is empty, show an error alert
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez saisir un CIN ou choisir de la Liste !");
            alert.showAndWait();
            return;
        } else {
            try {
                // Parse the CIN from the TextField
                cin_utilisateurE = Integer.parseInt(cin_utilisateur);
            } catch (NumberFormatException e) {
                // If parsing fails, show an error alert
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Le CIN doit être un nombre valide !");
                alert.showAndWait();
                return;
            }
        }

        ServiceUtilisateur su = new ServiceUtilisateur();
        Utilisateur user = su.get_One_ByCin(cin_utilisateurE);

        if (user == null) {
            // Show an error alert for non-existent user
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Aucun utilisateur trouvé avec le CIN saisi !");
            alert.showAndWait();
            return;
        }


        if (CurrentUser.getRole() != 0) {
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
        Etablissement etablissement = new Etablissement();
        etablissement.setNom(Nom);
        etablissement.setCodeEtablissement(codeE);
        etablissement.setLieu(Lieu);
        etablissement.setTypeEtablissement(labelType.getText());
        etablissement.setImage(image);
        etablissement.setUser(user);

        // Ajouter le service à la base de données
        try {
            serviceEtablissement.ajouter(etablissement);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Succès");
            alert.setHeaderText(null);
            alert.setContentText("Etablissement ajouté avec succès !");
            alert.showAndWait();
            if (CurrentUser.getRole()!=0) {
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Succès");
                alert.setHeaderText(null);
                alert.setContentText("Veuiller faire une photo RealTime pour la verification");
                alert.showAndWait();

                SwingUtilities.invokeLater(() -> CameraCapture.startCameraCapture());
                currentDir = System.getProperty("user.dir");
                File imgDir = new File(currentDir + "/src/main/resources/img");
                String chemin=jaw.getImageJdida();// Assuming this returns the full path
                int dernierIndiceBarre = Math.max(chemin.lastIndexOf('/'), chemin.lastIndexOf('\\'));
                String nomFichier = (dernierIndiceBarre > -1 && dernierIndiceBarre < chemin.length() - 1)
                        ? chemin.substring(dernierIndiceBarre + 1)
                        : chemin;

// Get the filename with extension


                System.out.println(nomFichier);

                Utilisateur user1 = su.get_One_ByCin(CurrentUser.getCin()) ;

                user1.setImage(nomFichier);
                user1.setId_utilisateur(CurrentUser.getId_utilisateur());
                su.modifier(user1);


                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Succès");
                alert.setHeaderText(null);
                alert.setContentText("Merci");
                alert.showAndWait();
            }

            // Effacer les champs du formulaire après l'ajout réussi
            NomETF.clear();
            LieuETF.clear();
            CodeETF.clear();
            imageETF.clear();
            cin_utilisateurETF.clear();
            imageViewETF.setImage(null);

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Erreur lors de l'ajout d'etablissement' : " + e.getMessage());
            alert.showAndWait();
        }
        navigatetoAfficherEtablissementAction(event);

    }


    public void navigatetoAfficherEtablissementAction(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Etablissement.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) NomETF.getScene().getWindow(); // Utilisez la même fenêtre (Stage) actuelle
            stage.setScene(new Scene(root));
            stage.show();

            // Vous pouvez fermer la fenêtre actuelle si nécessaire
            // ((Node)(event.getSource())).getScene().getWindow().hide();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        labelType.setVisible(false);

        cin_utilisateurETF.setVisible(false);

        if (CurrentUser.getRole() == 0) {
            labelRegle.setVisible(false);
            labelRegle.setManaged(false);
            checkBoxRegle.setVisible(false);
            checkBoxRegle.setManaged(false);
        } else {
            // Hide and reclaim space
            selecUserAnchor.setVisible(false);
            selecUserAnchor.setManaged(false);
            listeUsersLabels.setVisible(false);
            listeUsersLabels.setManaged(false);
            cin_utilisateurETF.setText(String.valueOf(CurrentUser.getCin()));
            cin_utilisateurETF.setEditable(false);
        }


        ServiceUtilisateur serviceService = new ServiceUtilisateur();

        Set<Utilisateur> users = null;


        try {
            users = serviceService.getAll();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        int column = 0;
        int row = 1;
        try {
            for (Utilisateur utilisateur : setU) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/UtilisateurItem.fxml"));
                HBox hbox = fxmlLoader.load();

                UtilisateurItemController itemController = fxmlLoader.getController();
                itemController.setAjouterEtablissementController(this);// Set the reference
                itemController.setData(utilisateur);

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

    @FXML
    public void importImage(ActionEvent actionEvent) {
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
            imageETF.setText(selectedFile.getName());

            // Display the image in the ImageView
            Image image = new Image(imagePath);
            imageViewETF.setImage(image);


            // Do something with the imagePath, for example, display the image
            // imageView.setImage(new Image(imagePath));
            System.out.println("Selected Image: " + imagePath);
        } else {
            // The user canceled the operation
            System.out.println("Operation canceled.");
        }
    }

    @FXML
    void updateCinTextField(String cin) {
        cin_utilisateurETF.setText(cin);
    }


    public void getType(ActionEvent actionEvent) {
        if (faculteRadio.isSelected()) {
            labelType.setText(faculteRadio.getText());
        } else if (lyceeRadio.isSelected()) {
            labelType.setText(lyceeRadio.getText());
        } else if (collegeRadio.isSelected()) {
            labelType.setText(collegeRadio.getText());
        } else if (ecoleRadio.isSelected()) {
            labelType.setText(ecoleRadio.getText());
        }

    }


    private static String getLatestImageFileName(File imgDir) {
        File[] files = imgDir.listFiles();

        if (files != null && files.length > 0) {
            // Sort files by last modified timestamp in descending order
            Arrays.sort(files, Comparator.comparingLong(File::lastModified).reversed());
            return files[0].getName(); // Return the name of the latest file
        }

        return null; // No files in the directory
    }
}
