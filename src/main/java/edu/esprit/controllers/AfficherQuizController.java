package edu.esprit.controllers;

import edu.esprit.entities.CurrentUser;
import edu.esprit.entities.Question;
import edu.esprit.entities.Quiz;
import edu.esprit.services.questionService;
import edu.esprit.services.quizService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class AfficherQuizController implements Initializable {

    @FXML
    private GridPane gridA;

    @FXML
    private ScrollPane scrollA;
    @FXML
    private TextField TFsearch;
    @FXML
    private Label receiverNameLabel21;

    @FXML
    private Label LabelMail;

    private quizService serviceQ = new quizService();
    Set<Quiz> setQ;

    {
        try {
            setQ = serviceQ.getAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private void remplirReceiverNameLabel() {
        // Assurez-vous que CurrentUser.getNom() renvoie la valeur souhaitée.
        String nomUtilisateur = CurrentUser.getNom();

        // Mettez à jour le texte du Label avec le nom de l'utilisateur.
        receiverNameLabel21.setText(nomUtilisateur);
    }
    private void remplirReceiverMailLabel() {
        // Assurez-vous que CurrentUser.getNom() renvoie la valeur souhaitée.
        String mail = CurrentUser.getAdresse();

        // Mettez à jour le texte du Label avec le nom de l'utilisateur.
        LabelMail.setText(mail);
    }

    public AfficherQuizController() throws SQLException {
    }

    @FXML
    void TrierCode(ActionEvent event) {
        // Trier le Set par code de manière ascendante
        Set<Quiz> sortedQuizSet = setQ.stream()
                .sorted(Comparator.comparingInt(Quiz::getCode_quiz))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        // Mettre à jour la vue avec les résultats triés
        updateQuizGridView(sortedQuizSet);
    }






    @FXML
    void TrierPrix(ActionEvent event) {
        // Trier par prix de manière ascendante
        Set<Quiz> sortedQuizSet = setQ.stream()
                .sorted((quiz1, quiz2) -> Double.compare(quiz1.getPrix_quiz(), quiz2.getPrix_quiz()))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        // Mettre à jour la vue avec les résultats triés
        updateQuizGridView(sortedQuizSet);}


    public void initialize(URL url, ResourceBundle resourceBundle) {
        remplirReceiverNameLabel();
        remplirReceiverMailLabel();

        int column = 0;
        int row = 1;
        try {
            for (Quiz quiz : setQ) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/QuizItem.fxml"));
                HBox hbox = fxmlLoader.load();

                QuizItemController itemController = fxmlLoader.getController();
                itemController.setData(quiz);

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
        TFsearch.setOnKeyReleased(this::searchByCode);
    }
    public void navigateToAjouter(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterQuiz.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) gridA.getScene().getWindow(); // Utilisez la même fenêtre (Stage) actuelle
            stage.setScene(new Scene(root));
            stage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void navigateToQuestion(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherQuestion.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) gridA.getScene().getWindow(); // Utilisez la même fenêtre (Stage) actuelle
            stage.setScene(new Scene(root));
            stage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void searchByCode(KeyEvent event) {
        // Récupérer le texte de recherche
        String searchQuery = TFsearch.getText().trim();

        // Appliquer la recherche seulement si la saisie n'est pas vide
        if (!searchQuery.isEmpty()) {
            try {
                // Récupérer tous les quiz
                Set<Quiz> allQuizzes = serviceQ.getAll();

                // Filtrer les quiz en fonction du code Quiz
                List<Quiz> filteredQuizzes = new ArrayList<>();

                for (Quiz quiz : allQuizzes) {
                    String codeString = String.valueOf(quiz.getCode_quiz());
                    if (codeString.startsWith(searchQuery)) {
                        filteredQuizzes.add(quiz);
                    }
                }

                Set<Quiz> filteredQuizzesSet = new HashSet<>(filteredQuizzes);

                // Mettre à jour la vue avec les résultats de la recherche
                updateQuizGridView(filteredQuizzesSet);
            } catch (NumberFormatException e) {
                // Gérer l'exception si la saisie n'est pas un nombre
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur de saisie");
                alert.setContentText("Veuillez saisir un code Quiz valide (nombre entier).");
                alert.show();
            } catch (SQLException e) {
                // Gérer les exceptions SQL, par exemple afficher un message d'erreur ou lancer une nouvelle exception
                e.printStackTrace();
            }
        } else {
            try {
                // Récupérer tous les quiz
                Set<Quiz> allQuizzes = serviceQ.getAll();

                // Mettre à jour la vue avec tous les quiz
                updateQuizGridView(allQuizzes);
            } catch (SQLException e) {
                // Gérer les exceptions SQL, par exemple afficher un message d'erreur ou lancer une nouvelle exception
                e.printStackTrace();
            }
        }
    }

    private void updateQuizGridView(Set<Quiz> quizs) {
        // Effacer le contenu actuel du GridPane
        gridA.getChildren().clear();

        // Mettre à jour le GridPane avec les nouvelles données
        int rowIndex = 1; // Commencer à l'indice 1

        for (Quiz quiz : quizs) {
            // Charger le composant QuestionItem à partir du fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/QuizItem.fxml"));
            try {
                Parent quizItem = loader.load();

                // Accéder au contrôleur du composant QuestionItem
                QuizItemController quizItemController = loader.getController();

                // Configurer les données pour le QuestionItem
                quizItemController.setData(quiz);

                // Ajouter le QuestionItem à la GridPane
                gridA.add(quizItem, 0, rowIndex);

                // Incrémenter l'indice de ligne pour la prochaine question
                rowIndex++;
            } catch (IOException e) {
                // Gérer les exceptions liées au chargement du composant QuestionItem
                e.printStackTrace();
            }
        }
    }




}
