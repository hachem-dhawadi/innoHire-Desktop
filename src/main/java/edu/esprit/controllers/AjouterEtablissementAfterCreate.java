package edu.esprit.controllers;


import edu.esprit.entities.CurrentUser;
import edu.esprit.entities.Etablissement;
import edu.esprit.entities.Utilisateur;

import edu.esprit.services.ServiceEtablissement;

import edu.esprit.services.ServiceUtilisateur;

import edu.esprit.utils.DataSource;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.plaf.ColorUIResource;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.Set;

public class AjouterEtablissementAfterCreate implements Initializable {
    Connection cnx = DataSource.getInstance().getCnx();

    @FXML
    private TextField CodeETF;

    @FXML
    private TextField cin_utilisateurETF;

    @FXML
    private TextField LieuETF;

    @FXML
    private TextField NomETF;

    @FXML
    private TextField TypeETF;

    @FXML
    private TextField imageETF;

    @FXML
    private ImageView EtabImageView;

    private final ServiceEtablissement se = new ServiceEtablissement();




    @FXML
    void ajouterEtablissementAction(ActionEvent event) throws SQLException {
        // Créer une instance de ServiceService
        ServiceEtablissement serviceEtablissement = new ServiceEtablissement();

        // Récupérer les valeurs des champs du formulaire
        String Nom = NomETF.getText();
        String Lieu = LieuETF.getText();
        String Code = CodeETF.getText();
        String Type = TypeETF.getText();


        // Vérifier si les champs requis sont vides
        if (Nom.isEmpty() || Lieu.isEmpty() || Code.isEmpty() || Type.isEmpty() ) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Tous les champs sont obligatoires !");
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




        String cin_utilisateur = cin_utilisateurETF.getText();
        int cin_utilisateurE;
        try {
                cin_utilisateurE = CurrentUser.getId_utilisateur();
            } catch (NumberFormatException e) { Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Le CIN doit être un nombre valide !");
                alert.showAndWait();
                return;
            }
        if (imageETF.getText().equals("no Photo chosen"))
        {  Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setTitle("Erreur");
            alert1.setHeaderText(null);
            alert1.setContentText("Choisir photo!");
            alert1.showAndWait();
            return;
        }







        // Créer un nouvel objet Service avec les valeurs saisies
        Etablissement etablissement = new Etablissement();
        etablissement.setNom(Nom);
        etablissement.setCodeEtablissement(codeE);
        etablissement.setLieu(Lieu);
        etablissement.setTypeEtablissement(Type);
        etablissement.setImage(imageETF.getText());

        ServiceUtilisateur su=new ServiceUtilisateur();
        Utilisateur user = su.getOneByCin(CurrentUser.getCin());
        System.out.println(user);
        etablissement.setUser(user);

        // Ajouter le service à la base de données
        try {
            this.ajoutEtab_sansid(etablissement);
            this.addIdToEtab(etablissement);

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Succès");
            alert.setHeaderText(null);
            alert.setContentText("Etablissement ajouté avec succès !");
            alert.showAndWait();
            CurrentUser.setId_utilisateur(user.getId_utilisateur());
            CurrentUser.setNom(user.getNom());
            CurrentUser.setPrenom(user.getPrenom());
            CurrentUser.setAdresse(user.getAdresse());
            CurrentUser.setMdp(user.getMdp());
            CurrentUser.setRole(1);

            // Effacer les champs du formulaire après l'ajout réussi
            NomETF.clear();
            LieuETF.clear();
            CodeETF.clear();
            TypeETF.clear();
            cin_utilisateurETF.clear();
            Parent root = FXMLLoader.load(getClass().getResource("/EnAttente.fxml"));
            cin_utilisateurETF.getScene().setRoot(root);
        }

        catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Erreur lors de l'ajout d'etablissement' : " + e.getMessage());
            alert.showAndWait();
        }

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
            EtabImageView.setImage(image);
            imageETF.setText(selectedFile.getName());


        } else {
            // The user canceled the operation
            System.out.println("Operation canceled.");
        }
    }



    public void navigatetoAfficherEtablissementAction(ActionEvent actionEvent) {
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

    }

    @FXML
    void skip(ActionEvent event) {
        Parent root = null;
        ServiceUtilisateur su = new ServiceUtilisateur();
        try {
            Utilisateur user = su.getOneByCin(CurrentUser.getCin());
            CurrentUser.setId_utilisateur(user.getId_utilisateur());
            CurrentUser.setNom(user.getNom());
            CurrentUser.setPrenom(user.getPrenom());
            CurrentUser.setAdresse(user.getAdresse());
            CurrentUser.setMdp(user.getMdp());
            CurrentUser.setRole(1);


            root = FXMLLoader.load(getClass().getResource("/EnAttente.fxml"));
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
        cin_utilisateurETF.getScene().setRoot(root);

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cin_utilisateurETF.setText(String.valueOf(CurrentUser.getCin()));
        imageETF.setText("no Photo chosen");
    }
    public void ajoutEtab_sansid(Etablissement etablissement) {
        String req = "INSERT INTO `etablissement`(`nom`, `lieu`, `code_etablissement`, `type_etablissement`,`image`) VALUES (?,?,?,?,?)";
        try {
            PreparedStatement ps = cnx.prepareStatement(req, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, etablissement.getNom());
            ps.setString(2, etablissement.getLieu());
            ps.setInt(3, etablissement.getCodeEtablissement());
            ps.setString(4, etablissement.getTypeEtablissement());
            ps.setString(5, etablissement.getImage());

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                // Retrieve the auto-generated ID
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int id_etablissement = generatedKeys.getInt(1);
                    etablissement.setIdEtablissement(id_etablissement);
                    System.out.println("Etablissement added with ID: " + id_etablissement);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addIdToEtab(Etablissement etablissement) {
        ServiceUtilisateur su = new ServiceUtilisateur();
        String req = "UPDATE etablissement SET id_utilisateur = ? WHERE id_etablissement = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, su.getUserIdByCin(CurrentUser.getCin()));
            ps.setInt(2, etablissement.getIdEtablissement());


            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("id added to etablissement: " + etablissement.getIdEtablissement());
            } else {
                System.out.println("Failed to add id to etablissement");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}


