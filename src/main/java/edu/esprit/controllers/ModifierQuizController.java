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
import java.sql.SQLException;

public class ModifierQuizController {

    @FXML
    private TextField TFCode1;

    @FXML
    private TextField TFDesc1;

    @FXML
    private TextField TFNom1;

    @FXML
    private TextField TFprix1;

    @FXML
    private TextField imageView1;

    @FXML
    private ImageView imageViewQ1;

    private Quiz quiz;
    private quizService qs;

    public void setData(Quiz quiz) {
        this.quiz = quiz;
        TFCode1.setText(String.valueOf(quiz.getCode_quiz()));
        TFNom1.setText(quiz.getNom_quiz());
        TFDesc1.setText(quiz.getDescription());
        TFprix1.setText(String.valueOf(quiz.getPrix_quiz()));

        // Afficher l'image actuelle du quiz
     /*   String imagePath = quiz.getImage_quiz();
        if (imagePath != null && !imagePath.isEmpty()) {
            Image image = new Image(imagePath);
            imageViewQ1.setImage(image);
        }*/
        if (quiz.getImage_quiz() != null && !quiz.getImage_quiz().isEmpty()) {
            Image image = new Image(getClass().getResourceAsStream("/img/" + quiz.getImage_quiz()));
            imageViewQ1.setImage(image);
    }}
    @FXML
    void ModifierQuizAction(ActionEvent event) {
        try {
            // Récupérer les valeurs des champs
            String codeQuizText = TFCode1.getText();
            String nomQuiz = TFNom1.getText();
            String description = TFDesc1.getText();
            String prixQuizText = TFprix1.getText();

            // Vérifier si tous les champs sont remplis
            if (codeQuizText.isEmpty() || nomQuiz.isEmpty() || description.isEmpty() || prixQuizText.isEmpty()) {
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

            // Vérifier la validité de la description
            if (!description.equalsIgnoreCase("facile") && !description.equalsIgnoreCase("moyen") && !description.equalsIgnoreCase("difficile")) {
                showAlert("Erreur de saisie", "La description doit être 'facile', 'moyen' ou 'difficile'.");
                return;
            }

            // Vérifier la correspondance entre la description et le prix
            if ((description.equalsIgnoreCase("facile") && (prixQuiz < 0 || prixQuiz > 15)) ||
                    (description.equalsIgnoreCase("moyen") && (prixQuiz < 16 || prixQuiz > 39)) ||
                    (description.equalsIgnoreCase("difficile") && (prixQuiz < 40 || prixQuiz > 49))) {
                showAlert("Erreur de saisie", "La correspondance entre la description et le prix est incorrecte.");
                return;
            }

            // Mettre à jour les attributs du quiz
            quiz.setCode_quiz(codeQuiz);
            quiz.setNom_quiz(nomQuiz);
            quiz.setDescription(description);
            quiz.setPrix_quiz(prixQuiz);

            // Mettre à jour le chemin de l'image dans le quiz
            quiz.setImage_quiz(imageView1.getText());

            if (qs == null) {
                qs = new quizService();
            }

            qs.modifier(quiz);

            // Afficher une alerte de succès
            showAlert("Success", "Le quiz a été modifié avec succès.");

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

    @FXML
    private void importImage1(ActionEvent event) {
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
            imageView1.setText(selectedFile.getName());

            // Display the image in the ImageView
            Image image = new Image(imagePath);
            imageViewQ1.setImage(image);



            // Do something with the imagePath, for example, display the image
            // imageView.setImage(new Image(imagePath));
            System.out.println("Selected Image: " + imagePath);
        } else {
            // The user canceled the operation
            System.out.println("Operation canceled.");
        }
    }


    public void navigateToAfficher(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherQuiz.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) TFCode1.getScene().getWindow(); // Utilisez la même fenêtre (Stage) actuelle
            stage.setScene(new Scene(root));
            stage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
