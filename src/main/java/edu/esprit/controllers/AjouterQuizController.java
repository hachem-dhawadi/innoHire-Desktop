package edu.esprit.controllers;

import edu.esprit.entities.Quiz;
import edu.esprit.services.quizService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;

public class AjouterQuizController {
    @FXML
    private TextField TFCode;

    @FXML
    private TextField TFDesc;

    @FXML
    private TextField TFNom;

    @FXML
    private TextField imageView;

    @FXML
    private ImageView imageViewQ;

    @FXML
    private TextField TFprix;
    @FXML

    private void AjouterQuizAction(ActionEvent event) {
        try {
            // Récupérer les valeurs des champs
            String codeQuizText = TFCode.getText();
            String nomQuiz = TFNom.getText();
            String description = TFDesc.getText();
            String prixQuizText = TFprix.getText();
            String nomImage = imageView.getText(); // Assurez-vous que vous récupérez le chemin correctement

            // Vérifier si tous les champs sont remplis
            if (codeQuizText.isEmpty() || nomQuiz.isEmpty() || description.isEmpty() || prixQuizText.isEmpty() || nomImage.isEmpty()) {
                // Afficher une alerte en cas de champs manquants
                showAlert("Champs manquants", "Veuillez remplir tous les champs.");
                return;
            }

            // Convertir les valeurs en types appropriés
            int codeQuiz = Integer.parseInt(codeQuizText);
            int prixQuiz = Integer.parseInt(prixQuizText);

            // Vérifier que codeQuiz est supérieur à 0
            if (codeQuiz <= 0) {
                // Afficher une alerte en cas de codeQuiz incorrect
                showAlert("Erreur de saisie", "Le code du quiz doit être supérieur à 0.");
                return;
            }

            // Vérifier que prixQuiz est supérieur à 0 et inférieur à 50
            if (prixQuiz <= 0 || prixQuiz >= 50) {
                // Afficher une alerte en cas de prix incorrect
                showAlert("Erreur de saisie", "Le prix du quiz doit être supérieur à 0 et inférieur à 50.");
                return;
            }

            // Vérifier que la description est valide
            if (!description.equalsIgnoreCase("facile") && !description.equalsIgnoreCase("moyen") && !description.equalsIgnoreCase("difficile")) {
                showAlert("Erreur de saisie", "La description doit être 'facile', 'moyen' ou 'difficile'.");
                return;
            }

            // Vérifier les conditions spécifiques en fonction de la description
            if (description.equalsIgnoreCase("facile") && (prixQuiz < 0 || prixQuiz > 15)) {
                showAlert("Erreur de saisie", "Pour la description 'facile', le prix doit être entre 0 et 15.");
                return;
            } else if (description.equalsIgnoreCase("moyen") && (prixQuiz < 16 || prixQuiz > 39)) {
                showAlert("Erreur de saisie", "Pour la description 'moyen', le prix doit être entre 16 et 39.");
                return;
            } else if (description.equalsIgnoreCase("difficile") && (prixQuiz < 40 || prixQuiz > 49)) {
                showAlert("Erreur de saisie", "Pour la description 'difficile', le prix doit être entre 40 et 49.");
                return;
            }

            // Créer une instance de Quiz
            Quiz quiz = new Quiz(codeQuiz, nomQuiz, description, prixQuiz, nomImage);

            // Ajouter le quiz à la base de données
            quizService qs = new quizService();
            qs.ajouter(quiz);

            // Afficher une alerte de succès
            showAlert("Success", "Le quiz a été ajouté avec succès.");

            // Appeler la fonction pour afficher la notification

        } catch (NumberFormatException e) {
            // Afficher une alerte d'erreur si une exception de format numérique se produit
            e.printStackTrace();
            showAlert("Number Format Exception", "Erreur de format numérique : " + e.getMessage());
        } catch (SQLException e) {
            // Afficher une alerte d'erreur si une exception SQL se produit
            e.printStackTrace();
            showAlert("SQL Exception", "Erreur SQL : " + e.getMessage());
        }
    }


    // Méthode pour afficher une alerte
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
    public void navigateToAfficher(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherQuiz.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) TFCode.getScene().getWindow(); // Utilisez la même fenêtre (Stage) actuelle
            stage.setScene(new Scene(root));
            stage.show();


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
        fileChooser.setInitialDirectory(new File(currentDir + "/src/main/resources/images"));

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
            imageView.setText(selectedFile.getName());

            // Display the image in the ImageView
            Image image = new Image(imagePath);
            imageViewQ.setImage(image);



            // Do something with the imagePath, for example, display the image
            // imageView.setImage(new Image(imagePath));
            System.out.println("Selected Image: " + imagePath);
        } else {
            // The user canceled the operation
            System.out.println("Operation canceled.");
        }
    }


}
