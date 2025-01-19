package edu.esprit.controllers;

import edu.esprit.entities.CurrentUser;
import edu.esprit.entities.Question;
import edu.esprit.entities.Quiz;
import edu.esprit.services.questionService;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class AfficherQuestionController implements Initializable {
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


    private questionService serviceQ = new questionService();
    Set<Question> setQ;

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


    // Méthode pour récupérer la question à partir de la grille

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        remplirReceiverNameLabel();
        remplirReceiverMailLabel();

        int column = 0;
        int row = 1;
        try {
            for (Question question : setQ) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/QuestionItem.fxml"));
                HBox hbox = fxmlLoader.load();

                QuestionItemController itemController = fxmlLoader.getController();
                itemController.setData(question);

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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterQuestion.fxml"));
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
                // Récupérer toutes les questions
                Set<Question> allQuestions = serviceQ.getAll();

                // Filtrer les questions en fonction du code Quiz
                List<Question> filteredQuestions = allQuestions.stream()
                        .filter(question -> String.valueOf(question.getQuiz().getCode_quiz()).startsWith(searchQuery))
                        .collect(Collectors.toList());

                Set<Question> filteredQuestionsSet = new HashSet<>(filteredQuestions);

                // Mettre à jour la vue avec les résultats de la recherche
                updateQuestionGridView(filteredQuestionsSet);
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
                // Récupérer toutes les questions
                Set<Question> allQuestions = serviceQ.getAll();

                // Mettre à jour la vue avec toutes les questions
                updateQuestionGridView(allQuestions);
            } catch (SQLException e) {
                // Gérer les exceptions SQL, par exemple afficher un message d'erreur ou lancer une nouvelle exception
                e.printStackTrace();
            }
        }
    }





    private void updateQuestionGridView(Set<Question> questions) {
        // Effacer le contenu actuel du GridPane
        gridA.getChildren().clear();

        // Mettre à jour le GridPane avec les nouvelles données
        int rowIndex = 0;

        for (Question question : questions) {
            // Charger le composant QuestionItem à partir du fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/QuestionItem.fxml"));
            try {
                Parent questionItem = loader.load();

                // Accéder au contrôleur du composant QuestionItem
                QuestionItemController questionItemController = loader.getController();

                // Configurer les données pour le QuestionItem
                questionItemController.setData(question);

                // Ajouter le QuestionItem à la GridPane
                gridA.add(questionItem, 0, rowIndex);

                // Incrémenter l'indice de ligne pour la prochaine question
                rowIndex++;
            } catch (IOException e) {
                // Gérer les exceptions liées au chargement du composant QuestionItem
                e.printStackTrace();
            }
        }
    }
    public void navigateToQuiz(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherQuiz.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) gridA.getScene().getWindow(); // Utilisez la même fenêtre (Stage) actuelle
            stage.setScene(new Scene(root));
            stage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void exportToExcel(ActionEvent event) {
        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Questions par Code Quiz");

            // En-têtes de colonnes
            Row headerRow = sheet.createRow(0);
            String[] columns = {"Code Quiz", "Question", "Choix", "Réponse Correcte"};
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
            }

            // Remplir les données dans la feuille Excel
            int rowIndex = 1;
            for (Question question : setQ) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(question.getQuiz().getCode_quiz());
                row.createCell(1).setCellValue(question.getQuestion());
                row.createCell(2).setCellValue(question.getChoix());
                row.createCell(3).setCellValue(question.getReponse_correcte());
            }

            // Ajuster automatiquement la largeur des colonnes
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Enregistrer le fichier Excel
            String excelFilePath = "/Users/msi/Downloads/QuestionsParCodeQuiz.xlsx";
            try (FileOutputStream outputStream = new FileOutputStream(excelFilePath)) {
                workbook.write(outputStream);
            }

            // Afficher une confirmation
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Export Excel");
            alert.setHeaderText(null);
            alert.setContentText("Les questions ont été exportées vers le fichier Excel : " + excelFilePath);
            alert.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            // Gérer les erreurs liées à l'écriture du fichier Excel
        }
    }
    }












