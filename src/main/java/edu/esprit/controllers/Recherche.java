package edu.esprit.controllers;


import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Recherche extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Créer une liste de données de test
        ObservableList<String> items = FXCollections.observableArrayList(
                "Id_service", "nom_service", "titre_service","prix", "tmpService ", "domaine");

        // Créer un ListView et lui fournir les données
        ListView<String> listVieww = new ListView<>(items);

        // Créer un TextField pour la recherche
        TextField searchField = new TextField();
        searchField.setPromptText("Search...");

        // Ajouter un listener pour la saisie dans le TextField
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            // Filtrer la liste en fonction du texte de recherche
            if (newValue == null || newValue.isEmpty()) {
                listVieww.setItems(items); // Afficher tous les éléments si le texte de recherche est vide
            } else {
                ObservableList<String> filteredList = FXCollections.observableArrayList();
                for (String item : items) {
                    if (item.toLowerCase().contains(newValue.toLowerCase())) {
                        filteredList.add(item);
                    }
                }
                listVieww.setItems(filteredList);
            }
        });

        // Créer un conteneur VBox pour organiser les éléments
        VBox root = new VBox(searchField, listVieww);
        root.setPadding(new Insets(10));
        root.setSpacing(10);

        // Créer la scène et afficher la fenêtre
        Scene scene = new Scene(root, 300, 250);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Searchable ListView Example");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}