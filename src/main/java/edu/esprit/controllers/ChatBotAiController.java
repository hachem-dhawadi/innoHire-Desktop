package edu.esprit.controllers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import edu.esprit.entities.CurrentUser;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ResourceBundle;

public class ChatBotAiController implements Initializable{
    @FXML
    private VBox chatVbox;

    @FXML
    private TextField messageTextField;

    @FXML
    private Button sendButton;

    private String userPhoto;
    private String userFullName;
    @FXML
    private ImageView loadingImageView;


    private static final String OPENAI_API_KEY = "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";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize any setup logic here
        loadingImageView.setVisible(false);
    }

    @FXML
    private void sendButtonAction(ActionEvent event) {
        String userMessage = messageTextField.getText().trim();

        if (!userMessage.isEmpty()) {
            // Assuming you have a method to get the user's image path
            String userImage = CurrentUser.getProfileImagePath();

            // Set user's full name and photo (replace these lines with actual user data retrieval)
            userFullName = "John Doe";  // Replace with actual full name
            userPhoto = userImage;

            // Clear the text field
            messageTextField.clear();

            // Display the user's message in the UI immediately
            displayMessage(userFullName, userMessage, CurrentUser.getProfileImagePath());

            // Show loading image
            loadingImageView.setVisible(true);

            // Fetch the chatbot's response asynchronously
            fetchChatbotResponse(userMessage, userImage);
        }
    }




    private HBox createMessageBox(String sender, String messageContent, String userImage) {
        HBox messageHbox = new HBox();
        messageHbox.setSpacing(10.0);

        ImageView profileImage;

        // Use different profile images based on the sender (user or chatbot)
        if ("User".equals(sender)) {
            profileImage = new ImageView(new Image(userImage));
            System.out.println(userImage);
        } else {
            profileImage = new ImageView(new Image("/images/"+userImage));
            // profileImage = new ImageView(new Image("/images/chatbot.png"));
        }

        profileImage.setFitWidth(40.0);
        profileImage.setFitHeight(40.0);

        AnchorPane messagePane = new AnchorPane();
        messagePane.setStyle("-fx-background-color: #f2f5ec; -fx-background-radius: 0 30 30 30;");

        Label contentLabel = new Label(messageContent);
        contentLabel.setLayoutX(17.0);
        contentLabel.setLayoutY(10.0);
        contentLabel.setStyle("-fx-text-fill: #000000; -fx-font-weight: bold; -fx-font-size: 13; -fx-padding: 0 0 15 0;");
        contentLabel.setWrapText(true);
        contentLabel.setMaxWidth(450);

        messagePane.prefWidthProperty().bind(contentLabel.widthProperty().add(34.0));

        // You can add more customization for the message box here if needed

        messagePane.getChildren().addAll(contentLabel);

        messageHbox.getChildren().addAll(profileImage, messagePane);

        return messageHbox;
    }



    private void fetchChatbotResponse(String userMessage, String userImage) {
        Task<String> task = new Task<>() {
            @Override
            protected String call() {
                return getChatbotResponse(userMessage);
            }

            @Override
            protected void succeeded() {
                String chatbotResponse = getValue();

                // Print the chatbot's response to the console
                System.out.println("Chatbot: " + chatbotResponse);

                // Hide loading image
                loadingImageView.setVisible(false);

                if (chatbotResponse != null) {
                    // Display the chatbot's response in the UI, including user information
                    displayMessage("Chatbot", chatbotResponse, "chatbot.png");
                } else {
                    // Handle the case when the chatbot's response is null
                    displayMessage("Chatbot", "the internet connexion is too slow i mtrying to generate a response.", "chatbot.png");
                    System.out.println("the internet connexion is too slow i mtrying to generate a response.");
                }
            }



            @Override
            protected void failed() {
                Throwable exception = getException();
                exception.printStackTrace();
            }
        };

        new Thread(task).start();
    }


    private String getChatbotResponse(String userMessage) {
        try {            URL url = new URL("https://api.openai.com/v1/chat/completions");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + OPENAI_API_KEY);
            connection.setDoOutput(true);


            String requestBody = String.format("{\"model\": \"gpt-3.5-turbo\", \"messages\": [{\"role\": \"user\", \"content\": \"%s\"}]}", userMessage);

            try (OutputStream outputStream = connection.getOutputStream()) {
                outputStream.write(requestBody.getBytes());
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    // Parse the JSON response
                    JsonObject jsonResponse = JsonParser.parseReader(reader).getAsJsonObject();

                    // Extract the content from the assistant's message
                    String assistantMessageContent = jsonResponse.getAsJsonArray("choices")
                            .get(0).getAsJsonObject()
                            .getAsJsonObject("message")
                            .get("content").getAsString();

                    return assistantMessageContent;
                }
            } else {
                System.out.println("Error: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private void displayMessage(String sender, String message, String userImage) {
        // Use the createMessageBox method to create an HBox for the message
        HBox messageHbox = createMessageBox(sender, message, userImage);
        // Set bottom margin for each HBox
        VBox.setMargin(messageHbox, new Insets(0, 0, 10, 0));

        // Add the message box to your chatVbox
        chatVbox.getChildren().add(messageHbox);
    }




}