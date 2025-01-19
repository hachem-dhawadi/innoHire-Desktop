package edu.esprit.controllers;

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
import javafx.scene.image.Image;
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
import java.sql.SQLException;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.Set;


public class ModifierEtablissementController extends AjouterEtablissementController implements Initializable {
//Initializable:setAjouterEtablissementController(this);Set the reference

    private int id;
    private String nomInit;

    private int codeInit;


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
    private ImageView imageViewETF;


    @FXML
    private GridPane gridA;

    @FXML
    AnchorPane selecUserAnchor;
    @FXML
    Label listeUsersLabels;
    @FXML
    private Label labelRegle;
    @FXML
    private CheckBox checkBoxRegle;
    private ServiceUtilisateur serviceU = new ServiceUtilisateur();


    @FXML
    private HBox hboxSelectionne;

    @FXML
    private ComboBox<String> comboBox ;





    Set<Utilisateur> setU;


    {
        try {
            setU = serviceU.getAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

//----------------------wallet------------------------------------


    private final ServiceEtablissement serviceEtablissement = new ServiceEtablissement();



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCodeInit() {
        return codeInit;
    }

    public void setCodeInit(int codeInit) {
        this.codeInit = codeInit;
    }

    public String getNomInit() {
        return nomInit;
    }

    public void setNomInit(String nomInit) {
        this.nomInit = nomInit;
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

    public void initData(Etablissement etablissement) {
        if (etablissement != null) {
            setId(etablissement.getIdEtablissement());
            setCodeInit(etablissement.getCodeEtablissement());
            setNomInit(etablissement.getNom());


            CodeETF.setText(String.valueOf(etablissement.getCodeEtablissement()));
            LieuETF.setText(etablissement.getLieu());
            NomETF.setText(etablissement.getNom());
            TypeETF.setText(etablissement.getTypeEtablissement());
            //------------------------------setup ComboBox

            // Ajoutez le texte du TextField au ComboBox
            comboBox.getItems().add(TypeETF.getText());

// Ajoutez d'autres éléments prédéfinis au ComboBox
            comboBox.getItems().addAll("faculte", "lycee", "college", "ecole");

// Ajoutez un gestionnaire d'événements pour détecter les changements dans le ComboBox
            comboBox.setOnAction(event -> {
                // Obtenez l'élément sélectionné dans le ComboBox
                String selectedValue = comboBox.getSelectionModel().getSelectedItem();

                // Mettez à jour le TextField avec l'élément sélectionné (ou le texte par défaut si aucun élément n'est sélectionné)
                TypeETF.setText(selectedValue != null ? selectedValue : TypeETF.getText());
            });

// Définissez le texte par défaut du ComboBox après d'avoir ajouté les éléments
            comboBox.setValue(TypeETF.getText());

//------------------------------End setup ComboBox

            String cheminImage = etablissement.getImage();
            int indexDernierSlash = cheminImage.lastIndexOf('/');
            String nomPhoto = cheminImage.substring(indexDernierSlash + 1);
            imageETF.setText(nomPhoto);

            cin_utilisateurETF.setText(String.valueOf(etablissement.getUser().getCin()));

        }
    }


    @FXML
    void ok(ActionEvent event) throws SQLException {


        if (controlSaisie(NomETF) && controlSaisie(LieuETF) && controlSaisie(CodeETF) && controlSaisie(TypeETF) && controlSaisie(imageETF)) {
            if (!NomETF.getText().matches("^[a-zA-Z]+(\\d+)?$")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Format invalide");
                alert.setContentText("Le champ Nom doit contenir des lettres seules ou des lettres suivies de chiffres !");
                alert.showAndWait();
                return;
            }

            if (!NomETF.getText().equals(getNomInit())) {
                if (serviceEtablissement.existeParNom(NomETF.getText())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur");
                    alert.setHeaderText(null);
                    alert.setContentText("Erreur : Un établissement avec le même nom existe déjà !");
                    alert.showAndWait();
                    return;
                }
            }

            int Code = Integer.parseInt(CodeETF.getText());
            if (Code != getCodeInit()) {
                if (serviceEtablissement.existe(Code)) {
                    // Afficher une alerte si le code existe déjà
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur");
                    alert.setHeaderText(null);
                    alert.setContentText("Erreur Un établissement avec le même code existe déjà !");
                    alert.showAndWait();

                    return;
                }
            }


            // Vérifier si le type est valide
            String[] validTypes = {"ecole", "college", "lycee", "faculte"};
            String lowerCaseType = TypeETF.getText().trim().toLowerCase(); // Convertir en minuscules et supprimer les espaces inutiles

            if (!Arrays.asList(validTypes).contains(lowerCaseType)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Erreur : Le type d'établissement doit être 'ecole', 'college', 'lycee' ou 'faculte' !");
                alert.showAndWait();
                return;
            }


            String currentDir = System.getProperty("user.dir");
            String imagePath = currentDir + "/src/main/resources/img/" + imageETF.getText();
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


            Etablissement newEtablissement = new Etablissement();

            newEtablissement.setIdEtablissement(getId());
            newEtablissement.setNom(NomETF.getText());
            newEtablissement.setLieu(LieuETF.getText());
            newEtablissement.setCodeEtablissement(Code);
            newEtablissement.setTypeEtablissement(TypeETF.getText());
            newEtablissement.setImage(imageETF.getText());


            newEtablissement.setUser(user);


            serviceEtablissement.modifier(newEtablissement);

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Service modifié avec succès");
            AfficherEtablissement(event);

        }
    }


    public void AfficherEtablissement(ActionEvent actionEvent) {
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

        TypeETF.setVisible(false);






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
            cin_utilisateurETF.setEditable(false);
        }


        imageViewETF.sceneProperty().addListener((observableScene, oldScene, newScene) -> {
            if (oldScene == null && newScene != null) {
                if (!imageETF.getText().isEmpty()) {
                    // Construct the full path to the image based on the application's working directory
                    String currentDir = System.getProperty("user.dir");
                    String imagePath = currentDir + "/src/main/resources/img/" + imageETF.getText();

                    // Create an Image object with the constructed path
                    Image image = new Image(new File(imagePath).toURI().toString());

                    // Set the Image object in the imageViewETF
                    imageViewETF.setImage(image);
                }
            }
        });





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



}

