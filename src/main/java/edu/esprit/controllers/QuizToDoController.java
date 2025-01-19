package edu.esprit.controllers;

import edu.esprit.entities.CurrentUser;
import edu.esprit.entities.Question;
import edu.esprit.services.ServiceUtilisateur;
import edu.esprit.services.quizService;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QuizToDoController {

    @FXML
    private VBox LVquestion;
    private Timeline timeline;
    @FXML
    private Label lblChronometre;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private Button btnValider;
    private static final int SECONDS_BEFORE_NOTIFICATION = 10;
    private boolean notificationAffichee = false;


    private List<Integer> reponsesCorrectes;
    quizService qs1=new quizService();
    private int codeQuiz;
    public void setCodeQuiz(int codeQuiz) {
        this.codeQuiz = codeQuiz;
    }



    public QuizToDoController() throws SQLException {
    }



    public void displayQuestions(List<Question> questions) {
        // Créez le VBox pour contenir les questions
        VBox vBox = new VBox();
        vBox.setSpacing(20.0);

        reponsesCorrectes = new ArrayList<>();

        for (Question question : questions) {
            Label questionLabel = new Label("Question: " + question.getQuestion());
            Label choixLabel = new Label("Choix: " + question.getChoix());
            TextField reponseTextField = new TextField();
            reponseTextField.setPromptText("Tapez votre réponse ici");

            reponsesCorrectes.add(question.getReponse_correcte());

            // Ajoutez des styles CSS pour rendre l'interface plus attrayante
            questionLabel.setStyle("-fx-font-size: 18; -fx-text-fill: #3498db; -fx-font-weight: bold;");
            choixLabel.setStyle("-fx-font-size: 16; -fx-text-fill: #2ecc71;");
            reponseTextField.setStyle(
                    "-fx-font-size: 16; -fx-background-color: #ecf0f1; -fx-border-color: #bdc3c7; -fx-border-radius: 5;");

            // Effet de transition sur le survol
            reponseTextField.setOnMouseEntered(e -> reponseTextField.setStyle(
                    "-fx-font-size: 16; -fx-background-color: #d0d3d4; -fx-border-color: #bdc3c7; -fx-border-radius: 5;"));
            reponseTextField.setOnMouseExited(e -> reponseTextField.setStyle(
                    "-fx-font-size: 16; -fx-background-color: #ecf0f1; -fx-border-color: #bdc3c7; -fx-border-radius: 5;"));

            // Ajoutez des styles CSS pour le padding
            VBox.setMargin(questionLabel, new Insets(0, 0, 10, 0));
            VBox.setMargin(choixLabel, new Insets(0, 0, 10, 0));
            VBox.setMargin(reponseTextField, new Insets(0, 0, 20, 0));

            // Ajoutez les éléments stylisés au VBox
            vBox.getChildren().addAll(questionLabel, choixLabel, reponseTextField);
        }

        // Appliquez le style du VBox
        vBox.setStyle("-fx-background-color: #f4f4f4; -fx-border-radius: 10; -fx-padding: 20;");

        // Appliquez le style du ScrollPane
        scrollPane.setStyle("-fx-background-color: #ffffff; -fx-border-radius: 15; -fx-padding: 10;");

        // Ajoutez le VBox au ScrollPane
        scrollPane.setContent(vBox);
    }


    @FXML
    void validerQuiz(ActionEvent event) throws SQLException {
        // Vérifier d'abord si le temps est écoulé
        if (tempsRestant() <= 0) {
            // Afficher une alerte indiquant que le temps est écoulé
            afficherAlerte("Temps épuisé", "Le temps pour le quiz est écoulé.");
        } else {
            // Si le temps n'est pas écoulé, vérifier les réponses
            if (toutesReponsesRemplies()) {
                int score = calculerScore();
                // Afficher le score (c'est facultatif, vous pouvez le retirer si vous le souhaitez)
                afficherAlerte("Résultat du Quiz", "Score final : " + score);
                ServiceUtilisateur su = new ServiceUtilisateur();
                // Enregistrez le passage du quiz par le candidat avec le score final
                int idCandidat =  su.getUserIdByCin(CurrentUser.getCin());
                qs1.saveQuizPass(codeQuiz, idCandidat, score);

                // Fermez la vue
                Stage stage = (Stage) lblChronometre.getScene().getWindow();
                stage.close();

                // Fermez la vue (vous devrez ajuster cela en fonction de votre logique)

            } else {
                // Si toutes les réponses ne sont pas remplies, afficher un avertissement
                afficherAlerte("Attention", "Veuillez répondre à toutes les questions avant de valider.");
            }
        }
    }

    private long tempsRestant() {
        if (timeline != null) {
            return Math.round(timeline.getCycleDuration().toSeconds() - timeline.getCurrentTime().toSeconds());
        }
        return 0;
    }

    private boolean toutesReponsesRemplies() {
        ObservableList<Node> children = ((VBox) scrollPane.getContent()).getChildren();

        for (Node child : children) {
            if (child instanceof TextField) {
                TextField reponseTextField = (TextField) child;
                if (reponseTextField.getText().isEmpty()) {
                    return false;
                }
            }
        }

        return true;
    }

    public void initialiserChronometre(int dureeEnMinutes) {
        // Arrêtez le chronomètre s'il était déjà en cours
        if (timeline != null) {
            timeline.stop();
        }

        // Initialisation du chronomètre avec la durée spécifiée en minutes
        int dureeEnSecondes = dureeEnMinutes * 60;
        timeline = new Timeline(new KeyFrame(Duration.seconds(dureeEnSecondes), e -> tempsEcoule()));

        // Gérez les événements liés au temps restant
        timeline.setOnFinished(e -> tempsEcoule());

        // Affichez le temps restant dans le label
        timeline.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
            long tempsRestant = Math.round(dureeEnSecondes - newValue.toSeconds());
            int minutes = (int) (tempsRestant / 60);
            int secondes = (int) (tempsRestant % 60);
            lblChronometre.setText(String.format("%02d:%02d", minutes, secondes));
        });

        // Démarrez le chronomètre
        timeline.play();
        timeline.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
            long tempsRestant = Math.round(dureeEnSecondes - newValue.toSeconds());
            int minutes = (int) (tempsRestant / 60);
            int secondes = (int) (tempsRestant % 60);
            lblChronometre.setText(String.format("%02d:%02d", minutes, secondes));

            // Vérifiez si le temps restant est égal à 10 secondes et que la notification n'a pas encore été affichée
            if (tempsRestant == SECONDS_BEFORE_NOTIFICATION && !notificationAffichee) {
                afficherNotification("Attention", "Il reste 10 secondes !");
                notificationAffichee = true;
            }
        });
    }
    private void afficherNotification(String titre, String texte) {
        Notifications.create()
                .title(titre)
                .text(texte)
                .showInformation();
    }
    private void tempsEcoule() {
        // Arrêtez le chronomètre
        timeline.stop();

        // Affichez l'alerte "Temps épuisé"
        afficherAlerte("Temps épuisé", "Le temps pour le quiz est écoulé.");

        // Fermez la vue
        fermerVueQuiz();
    }

    private void fermerVueQuiz() {
        // Obtenez la scène actuelle à partir de n'importe quel élément de la vue (ici, j'utilise LVquestion)
        Scene scene = lblChronometre.getScene();

        // Obtenez la fenêtre (stage) à partir de la scène
        Stage stage = (Stage) scene.getWindow();

        // Fermez la fenêtre (vue du quiz)
        stage.close();
    }
    public int calculerScore() {
        int score = 0;
        ObservableList<Node> children = ((VBox) scrollPane.getContent()).getChildren();

        for (Node child : children) {
            if (child instanceof TextField) {
                TextField reponseTextField = (TextField) child;
                try {
                    int reponseCandidat = Integer.parseInt(reponseTextField.getText());
                    int index = children.indexOf(child);
                    int reponseCorrecte = reponsesCorrectes.get(index / 3);

                    if (reponseCandidat == reponseCorrecte) {
                        score++;
                    }
                } catch (NumberFormatException e) {
                    // Gérez l'exception si l'utilisateur n'a pas entré un nombre valide
                }
            }
        }

        return score;
    }
    private void afficherAlerte(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.getButtonTypes().setAll(ButtonType.OK);
        alert.show();
    }

}
