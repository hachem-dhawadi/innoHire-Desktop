package edu.esprit.entities;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;

public class NotificationExample extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Button button = new Button("Afficher la notification");
        button.setOnAction(e -> showNotification("Bonjour !", "Ceci est une notification."));

        StackPane root = new StackPane();
        root.getChildren().add(button);

        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
    }

    private void showNotification(String titre, String texte) {
        Notifications.create()
                .title(titre)
                .text(texte)
                .showInformation();
    }
}