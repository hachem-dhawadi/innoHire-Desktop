package edu.esprit.controllers;

import edu.esprit.entities.Quiz;
import edu.esprit.services.ServiceEtablissement;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Set;

public class PasserQuizController {
    ServiceEtablissement se=new ServiceEtablissement();
    @FXML
    private TextField codeEtablissementTextField;
    @FXML
    public void rechercherEtAfficherQuizzes() {
        // Récupérer le code_etablissement saisi par le candidat
        String codeEtablissement = codeEtablissementTextField.getText().trim();

        // Vérifier que le code n'est pas vide
        if (!codeEtablissement.isEmpty()) {
            try {
                // Convertir le code en entier (assurez-vous que le code est un nombre)
                int codeEtablissementInt = Integer.parseInt(codeEtablissement);

                // Utiliser la méthode pour obtenir l'id_etablissement
                int idEtablissement = se.getIdEtablissementByCode(codeEtablissementInt);

                if (idEtablissement != -1) {
                    // Utiliser l'id_etablissement pour récupérer les quizzes de la table etablissement_quiz
                    Set<Quiz> quizzes = se.getQuizzesForEtablissement(idEtablissement);

                    // Créer un FXMLLoader pour charger la nouvelle vue
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherQuizParEtablissement.fxml"));

                    try {
                        Parent root = loader.load();

                        // Récupérer le contrôleur de la nouvelle vue
                        AfficherQuizParEtablissementController nouvelleVueController = loader.getController();

                        // Manipuler directement les éléments de la nouvelle vue
                        GridPane gridQuiz = nouvelleVueController.gridA;

                        // Ajouter les éléments QuizParEtablissementItemController personnalisés dans la grille
                        int row = 1;
                        for (Quiz quiz : quizzes) {
                            // Charger le contrôleur pour chaque élément de la grille
                            FXMLLoader itemLoader = new FXMLLoader(getClass().getResource("/QuizParEtablissementItem.fxml"));
                            HBox itemRoot = itemLoader.load();
                            QuizParEtablissementItemController itemController = itemLoader.getController();

                            // Personnaliser l'élément avec les données du quiz
                            itemController.setQuizData(quiz);

                            // Ajouter l'élément personnalisé dans la grille
                            gridQuiz.add(itemRoot, 1, row++);
                        }

                        // Créer une nouvelle scène
                        Scene nouvelleScene = new Scene(root);

                        // Récupérer la fenêtre actuelle (Stage)
                        Stage stageActuel = (Stage) codeEtablissementTextField.getScene().getWindow();

                        // Changer la scène de la fenêtre actuelle
                        stageActuel.setScene(nouvelleScene);

                    } catch (IOException e) {
                        // Gérer l'exception liée au chargement de la nouvelle vue
                        e.printStackTrace();
                    }
                } else {
                    // Le code_etablissement n'existe pas
                    afficherMessageErreur("Code etablissement non valide");
                }
            } catch (NumberFormatException e) {
                // Gérer l'exception si le code n'est pas un nombre
                afficherMessageErreur("Veuillez saisir un code etablissement valide (nombre entier).");
            }
        } else {
            // Le champ de texte est vide
            afficherMessageErreur("Veuillez saisir un code etablissement.");
        }
    }





    private void afficherMessageErreur(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
