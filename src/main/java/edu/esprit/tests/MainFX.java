package edu.esprit.tests;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MainFX extends Application {
   // public static final String CURRENCY = "$";
   @Override
   public void start(Stage stage) throws Exception {
       FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
       Parent root = loader.load();

// Assuming your root is a Pane, you can replace it with the actual type of your root node
       Pane paneRoot = (Pane) root;

// Set your preferred width and height
       paneRoot.setPrefWidth(700);  // Set your preferred width
       paneRoot.setPrefHeight(500); // Set your preferred height

       Scene scene = new Scene(root);

       stage.setResizable(true);

// Set the scene to the stage
       stage.setScene(scene);

// Set the title
       stage.setTitle("Gestion Etablissement");

// Show the stage
       stage.show();

   }


    public static void main(String[] args) {
        launch();
    }
}
