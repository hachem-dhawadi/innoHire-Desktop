package edu.esprit.controllers;



import edu.esprit.entities.Question;
import edu.esprit.entities.Quiz;
import edu.esprit.services.questionService;
import edu.esprit.services.quizService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ModifierQuestionController {

    @FXML
    private TextField TFquestionModify;

    @FXML
    private TextField TFchoixModify;
    @FXML

    private ComboBox<Integer> TFcode_quizModify;

    @FXML
    private TextField TFreponse_correcteModify;

    private Question question;
    private questionService qs;
    public void setData(Question question) throws SQLException {
        this.question = question;
        TFquestionModify.setText(question.getQuestion());
        TFchoixModify.setText(question.getChoix());
        quizService a = new quizService();
        List<Integer> codesQuiz;
        try {
            codesQuiz = a.getQuizcodes();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        TFcode_quizModify.getItems().setAll(codesQuiz);
        TFcode_quizModify.getSelectionModel().select(question.getQuiz().getCode_quiz());
        TFreponse_correcteModify.setText(String.valueOf(question.getReponse_correcte()));
    }


    @FXML

    void modifierQuestionAction() {
        try {
            // Réinitialiser le style des TextFields
            resetModifyTextFieldStyles();

            Integer selectedCodeQuiz = TFcode_quizModify.getSelectionModel().getSelectedItem();
            Integer codeQuiz = TFcode_quizModify.getValue();

            quizService qs1 = new quizService();
            int idQuiz = qs1.getIdQuizByCode(codeQuiz);

            // Vérification de la question et des choix non vides
            String modifiedQuestion = TFquestionModify.getText();
            String modifiedChoix = TFchoixModify.getText();

            if (modifiedQuestion.isEmpty() || modifiedChoix.isEmpty()) {
                // Afficher un message d'erreur
                setModifyTextFieldErrorStyle(TFquestionModify);
                setModifyTextFieldErrorStyle(TFchoixModify);
                showAlert("Erreur de saisie", "Veuillez saisir une question et des choix.");
                return;
            }

            // Vérification du format des choix
            if (!isValidFormatChoix(modifiedChoix)) {
                // Afficher un message d'erreur
                setModifyTextFieldErrorStyle(TFchoixModify);
                showAlert("Erreur de saisie", "Le format des choix est incorrect. Utilisez le format '1) ... 2) ... 3) ...'.");
                return;
            }

            // Vérification de la réponse correcte
            try {
                int modifiedReponseCorrecte = Integer.parseInt(TFreponse_correcteModify.getText());

                if (modifiedReponseCorrecte < 1 || modifiedReponseCorrecte > 3) {
                    // Afficher un message d'erreur
                    setModifyTextFieldErrorStyle(TFreponse_correcteModify);
                    showAlert("Erreur de saisie", "La réponse correcte doit être entre 1 et 3.");
                    return;
                }

                question.setQuestion(modifiedQuestion);
                question.setChoix(modifiedChoix);

                // Assurez-vous que la méthode getOneByID dans quizService renvoie le quiz avec le bon ID
                question.setQuiz(qs1.getOneByID(idQuiz));
                question.getQuiz().setCode_quiz(selectedCodeQuiz);
                question.setReponse_correcte(modifiedReponseCorrecte);
                if (qs == null) {
                    qs = new questionService();
                }

                try {
                    qs.modifier(question);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Question modifiée avec succès.");
                alert.setTitle("Modification réussie");
                alert.show();
            } catch (NumberFormatException e) {
                // Afficher un message d'erreur
                setModifyTextFieldErrorStyle(TFreponse_correcteModify);
                showAlert("Erreur de saisie", "Veuillez saisir une réponse correcte valide (nombre entier).");
            }

        } catch (Exception e) {
            // Gérer les autres exceptions
            e.printStackTrace();
            showAlert("Erreur de modification", "Erreur lors de la modification de la question.");
        }
    }

    private boolean isValidFormatChoix(String choix) {
        // Vérifiez si le format des choix est correct
        String regex = "\\d+\\)[^\\d]*\\d+\\)[^\\d]*\\d+.*";

        return choix.matches(regex);
    }



    // Méthode pour réinitialiser le style des TextFields de modification
    private void resetModifyTextFieldStyles() {
        TFquestionModify.setStyle(null);
        TFchoixModify.setStyle(null);
        TFcode_quizModify.setStyle(null);
        TFreponse_correcteModify.setStyle(null);
    }

    // Méthode pour définir le style d'erreur sur un TextField de modification
    private void setModifyTextFieldErrorStyle(TextField textField) {
        textField.setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
    }
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

// Le reste du code reste inchangé

    public void navigateToAfficher(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherQuestion.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) TFcode_quizModify.getScene().getWindow(); // Utilisez la même fenêtre (Stage) actuelle
            stage.setScene(new Scene(root));
            stage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

