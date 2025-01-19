package edu.esprit.controllers;

import edu.esprit.entities.CurrentUser;
import edu.esprit.entities.Question;
import edu.esprit.entities.Quiz;
import edu.esprit.services.ServiceUtilisateur;
import edu.esprit.services.questionService;
import edu.esprit.services.quizService;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QuizParEtablissementItemController {
    @FXML
    private Button btnPasser;
    private Timeline timeline;
    @FXML
    private Text TFcodeA ;
    @FXML
    private Text TFnomA;
    questionService qs=new questionService();
    quizService qs1=new quizService();

    public QuizParEtablissementItemController() throws SQLException {
    }

    public void setQuizData(Quiz quiz) {
        // Personnalisez l'affichage en fonction des données du quiz
        TFnomA.setText(quiz.getNom_quiz());
        TFcodeA.setText(String.valueOf(quiz.getCode_quiz()));

        // Autres personnalisations si nécessaire
    }
    @FXML
    void PasserQuiz(ActionEvent event) throws SQLException {
        // Obtenez le code du quiz à partir du Text (convertissez-le en int si nécessaire)
        int codeQuiz = Integer.parseInt(TFcodeA.getText());
        ServiceUtilisateur su = new ServiceUtilisateur();
        // Enregistrez le passage du quiz par le candidat avec le score final
        int idCandidat =  su.getUserIdByCin(CurrentUser.getCin()); // Remplacez cela par la méthode appropriée pour obtenir l'ID du candidat

        // Vérifiez si le candidat a déjà passé le quiz
        if (qs1.candidatHasPassedQuiz(codeQuiz, idCandidat)) {
            // Affichez un message à l'utilisateur indiquant qu'il a déjà passé le quiz
            showAlert("Quiz déjà passé", "Vous avez déjà passé ce quiz. Vous ne pouvez pas le repasser.");

            // Vous pouvez également mettre en place une logique supplémentaire ici (par exemple, rediriger l'utilisateur)
        } else {
            // Appelez la méthode pour obtenir les questions, choix et réponses correctes
            List<Map<String, Object>> questionsData = qs.getQuestionsForQuiz(codeQuiz);

            // Créez une liste de questions
            List<Question> questions = new ArrayList<>();

            // Maintenant, questionsData contient les données nécessaires pour chaque question du quiz
            // Vous pouvez traiter ces données selon vos besoins
            for (Map<String, Object> questionData : questionsData) {
                String questionText = (String) questionData.get("question");
                String choix = (String) questionData.get("choix");
                int reponseCorrecte = (int) questionData.get("reponse_correcte");
                Quiz Q = new Quiz();
                Q.setCode_quiz(codeQuiz);

                // Créez un objet Question avec les données récupérées
                Question question = new Question(questionText, choix, Q, reponseCorrecte);
                questions.add(question);
            }

            // Affichez les questions dans la vue QuizToDo
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/QuizToDo.fxml"));
            Parent root;
            try {
                root = loader.load();
                QuizToDoController quizToDoController = loader.getController();

                // Initialisez et démarrez le chronomètre avec une durée en minutes (6 minutes pour l'exemple)
                int dureeQuizEnMinutes = 1;

                quizToDoController.initialiserChronometre(dureeQuizEnMinutes);

                quizToDoController.displayQuestions(questions);

                // Enregistrez le code du quiz pour une utilisation ultérieure dans la méthode validerQuiz
                quizToDoController.setCodeQuiz(codeQuiz);

                // Créez une nouvelle scène avec la vue QuizToDo
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setTitle("Quiz To Do");
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    // Méthode utilitaire pour afficher une alerte
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }





}
