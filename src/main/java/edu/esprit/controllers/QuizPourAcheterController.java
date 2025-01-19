package edu.esprit.controllers;

import edu.esprit.entities.*;
import edu.esprit.services.ServiceEtablissement;
import edu.esprit.services.ServiceWallet;
import edu.esprit.services.quizService;
import edu.esprit.utils.DataSource;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class QuizPourAcheterController {


    @FXML
    private Text TFnomA;

    @FXML
    private Text TFprixA;

    @FXML
    private Button btnAcheter;

    @FXML
    private ImageView imageView;
    private Quiz quiz;
    private quizService qs;




    public QuizPourAcheterController() throws SQLException {
    }


    public void setData(Quiz quiz) {
        this.quiz = quiz;
        TFnomA.setText(quiz.getNom_quiz());

        TFprixA.setText(String.valueOf(quiz.getPrix_quiz()) + "DT");

        // Charger et afficher l'image du Quiz
      /*  String imagePath = quiz.getImage_quiz();
        if (imagePath != null && !imagePath.isEmpty()) {
            Image image = new Image(imagePath);
            imageView.setImage(image);
        }*/
        if (quiz.getImage_quiz() != null && !quiz.getImage_quiz().isEmpty()) {
            Image image = new Image(getClass().getResourceAsStream("/img/" + quiz.getImage_quiz()));
            imageView.setImage(image);

        }
    }



    @FXML
    void AcheterQuizOnClick(ActionEvent event) {

        try {
            ServiceEtablissement se =new ServiceEtablissement();
            ServiceWallet sw =new ServiceWallet();
            Etablissement etablissementCo=se.getOneByID(CurrentEtablissement.getIdEtablissement());
            System.out.println(etablissementCo);
            Wallet walletCo=sw.getOneByID(CurrentWallet.getIdWallet());

            int idQuiz = quiz.getId_quiz();

            int idEtablissement = etablissementCo.getIdEtablissement();
            Connection cnx = DataSource.getInstance().getCnx();

            // Vérifier si le quiz est déjà acheté
            String checkQuery = "SELECT * FROM etablissement_quiz WHERE id_etablissement = ? AND id_quiz = ?";
            try (PreparedStatement checkStatement = cnx.prepareStatement(checkQuery)) {
                checkStatement.setInt(1, idEtablissement);
                checkStatement.setInt(2, idQuiz);
                ResultSet resultSet = checkStatement.executeQuery();

                if (resultSet.next()) {
                    // Afficher un message indiquant que le quiz est déjà acheté
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Quiz déjà acheté");
                    alert.setContentText("Le quiz est déjà acheté.");
                    alert.show();
                    return; // Quitter la méthode car le quiz est déjà acheté
                }
            }

            // Vérifier si le solde est suffisant pour l'achat
            int prixQuiz = quiz.getPrix_quiz(); // Remplacez getPrixQuiz par la méthode réelle pour obtenir le prix du quiz
            if (walletCo.getBalance() < prixQuiz) {
                // Afficher un message indiquant que le solde est insuffisant
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Solde insuffisant");
                alert.setContentText("Le solde de votre portefeuille est insuffisant pour acheter ce quiz.");
                alert.show();
                return; // Quitter la méthode car le solde est insuffisant
            }

            // Si le code arrive ici, le quiz n'est pas encore acheté, le solde est suffisant, procéder à l'achat
            String insertQuery = "INSERT INTO etablissement_quiz (id_etablissement, id_quiz) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = cnx.prepareStatement(insertQuery)) {
                preparedStatement.setInt(1, idEtablissement);
                preparedStatement.setInt(2, idQuiz);

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    // Afficher une alerte de succès
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Achat réussi");

                    // Mettre à jour le solde après l'achat
                    walletCo.setBalance(walletCo.getBalance() - prixQuiz);
                    alert.setContentText("Achat du quiz réussi. Nouveau solde : " + walletCo.getBalance());
                    alert.show();
                } else {
                    // Afficher une alerte d'échec
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Échec de l'achat");
                    alert.setContentText("Échec de l'achat du quiz.");
                    alert.show();
                }
                sw.modifier(walletCo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les exceptions SQL, par exemple afficher un message d'erreur ou lancer une nouvelle exception
        } catch (Exception e) {
            e.printStackTrace();
            // Gérer les exceptions, par exemple afficher un message d'erreur
        }
    }





    @FXML
    private void showQuizDetails(ActionEvent event) throws SQLException {
        int codeQuiz = quiz.getCode_quiz();
        if (qs == null) {
            qs = new quizService();
        }

        // Récupérer les questions associées à ce code de quiz
        List<Question> questions = qs.getQuestionsByCodeQuiz(codeQuiz);

        // Créer une ListView pour afficher les détails des questions
        ListView<HBox> listView = new ListView<>();

        // Ajouter seulement l'attribut "question" et "reponse_correcte"
        for (Question question : questions) {
            String questionDetails = "Question: " + question.getQuestion() +"\nChoix:"+ question.getChoix()+
                    "\nRéponse correcte: " + question.getReponse_correcte() + "\n";

            Label label = new Label(questionDetails);
            label.setStyle("-fx-font-size: 14; -fx-text-fill: #333333;"); // Ajouter des styles CSS au label

            HBox hbox = new HBox(label);
            hbox.setStyle("-fx-border-color: #CCCCCC; -fx-border-width: 1; -fx-padding: 5; -fx-background-color: #FFFFFF;");
            hbox.setSpacing(10);

            // Ajouter la ligne entière (Label) à la liste d'affichage
            listView.getItems().add(hbox);
        }

        // Créer une boîte pour contenir la ListView
        VBox vbox = new VBox(listView);
        vbox.setPadding(new Insets(10));
        vbox.setStyle("-fx-background-color: #F5F5F5;"); // Ajouter un fond à la VBox

        // Afficher la boîte de dialogue
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Questions associées au quiz");
        alert.getDialogPane().setContent(vbox);

        // Ajouter un bouton "Fermer"
        ButtonType closeButton = new ButtonType("Fermer", ButtonBar.ButtonData.OK_DONE);
        alert.getButtonTypes().add(closeButton);

        // Afficher la boîte de dialogue
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.showAndWait();
    }
    }
