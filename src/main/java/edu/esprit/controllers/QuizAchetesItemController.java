package edu.esprit.controllers;

    import edu.esprit.entities.Quiz;
    import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

    public class QuizAchetesItemController {

        @FXML
        private ImageView imageView;

        @FXML
        private Text TFImageA;

        @FXML
        private Text TFNomA;

        @FXML
        private Button btnpasser;

        // Ajoutez d'autres éléments FXML au besoin

        public void setData(Quiz quiz) {
            // Initialisez les éléments d'interface avec les données du Quiz
            //    imageView.setImage(new Image(quiz.getImage_quiz()));
            // Remplacez cela par les données réelles du Quiz
            TFNomA.setText(quiz.getNom_quiz());
            if (quiz.getImage_quiz() != null && !quiz.getImage_quiz().isEmpty()) {
                Image image = new Image(getClass().getResourceAsStream("/images/" + quiz.getImage_quiz()));
                imageView.setImage(image);

                // Ajoutez d'autres initialisations d'interface au besoin
            }
        }
    }