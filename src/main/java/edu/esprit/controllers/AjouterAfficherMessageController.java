package edu.esprit.controllers;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import edu.esprit.entities.*;
import edu.esprit.services.ServiceMessagerie;
import edu.esprit.services.ServiceUtilisateur;
import javafx.animation.PauseTransition;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.*;
import com.google.zxing.pdf417.PDF417Writer;

public class AjouterAfficherMessageController implements Initializable{
    @FXML
    private VBox chatVbox;
    @FXML
    private Label receiverNameLabel;

    @FXML
    private ImageView receiverProfileImage;
    @FXML
    private TextField TFmessage;
    @FXML
    private Label messageError;
    @FXML
    private Label emptyMessage;
    String userPhoto;
    Utilisateur userReciver;
    @FXML
    private Label userFullName;
    @FXML
    private Label userEmail;
    @FXML
    private Label userCin;
    @FXML
    private Label userRole;
    @FXML
    private ImageView userPic;
    @FXML
    private AnchorPane RepresentantPane;

    @FXML
    private AnchorPane AdminPane;
    @FXML
    private AnchorPane CandidatPane;
    @FXML
    private Label fileName;
    @FXML
    private Button cancelSendingFileButton;
    @FXML
    private ImageView qrCodeImage;



    private final ServiceMessagerie serviceMessagerie = new ServiceMessagerie();
    private final ServiceUtilisateur su = new ServiceUtilisateur();
    Utilisateur userSender = su.get_One_ByCin(CurrentUser.getCin());





    //Utilisateur amen=new Utilisateur(1,11417264,"dhawadi","hachem","bizerte","123456789","edit.png");
    //Utilisateur userReciver=new Utilisateur(9,11417264,"dhawadi","hachem","bizerte","123456789","edit.png");

    public void initData(Reclamation selectedReclamation) {
        System.out.println(userSender.getId_utilisateur());
        userReciver = selectedReclamation.getUser();
        userPhoto=userReciver.getImage();
        //System.out.println(userReciver);
        updateReceiverInfo();
        updateChatMessages();
        // You can do additional initialization with the passed data if needed
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cancelSendingFileButton.setVisible(false);
        fileName.setVisible(false);
        if (CurrentUser.getRole() == 0) {
            // Admin role, so show AdminPane and hide RepresentantPane
            AdminPane.setVisible(true);
            RepresentantPane.setVisible(false);
            CandidatPane.setVisible(false);
        } else if (CurrentUser.getRole()==1){
            // Representant role, so show RepresentantPane and hide AdminPane
            RepresentantPane.setVisible(true);
            AdminPane.setVisible(false);
            CandidatPane.setVisible(false);
        }else {
            CandidatPane.setVisible(true);
            AdminPane.setVisible(false);
            RepresentantPane.setVisible(false);
        }
        // updateReceiverInfo();
        //updateChatMessages();
    }
    public void updateReceiverInfo() {
        // Get the name and profile image URL of the receiver
        //String receiverName = serviceMessagerie.getOneByID(9).getReciver_id().getNom(); // Replace with the actual receiver's name
        String receiverName = userReciver.getNom()+" "+userReciver.getPrenom();
        String profileImageUrl = userReciver.getImage(); // Replace with the actual image URL

        // Set the receiver's name and profile image
        receiverNameLabel.setText(receiverName);
        String imageName = userReciver.getImage();
        if (imageName != null && !imageName.isEmpty()) {
            String imagePath = "/images/" + imageName; // Assuming images are stored in src/main/resources/images
            Image image = new Image(getClass().getResource(imagePath).toExternalForm());
            receiverProfileImage.setImage(image);
            if (userReciver != null) {
                //userFullName.setText(userReciver.getNom()+" "+userReciver.getPrenom());
                userEmail.setText(userReciver.getAdresse());
                userCin.setText(String.valueOf(userReciver.getCin()));
                userRole.setText("Represnetant");
                //userPic.setImage(image);
            }
        } else {
            // Set a default image if the name is not available
            receiverProfileImage.setImage(new Image(getClass().getResource("/images/edit.png.jpg").toExternalForm()));
        }
        //receiverProfileImage.setImage(new Image(profileImageUrl));
    }
    // AjouterAfficherMessageController class
    public void updateChatMessages() {
        // Get the messages from the database for both sender and receiver
        Set<Messagerie> senderMessages = serviceMessagerie.getAllMessagesByReciverAndSender(userSender.getId_utilisateur(), userReciver.getId_utilisateur()); // Replace 1 with the sender's user ID
        Set<Messagerie> receiverMessages = serviceMessagerie.getAllMessagesByReciverAndSender(userReciver.getId_utilisateur(), userSender.getId_utilisateur()); // Replace 9 with the receiver's user ID

        // Combine the messages and order them by date
        Set<Messagerie> allMessages = new TreeSet<>(Comparator.comparing(Messagerie::getDate));
        allMessages.addAll(senderMessages);
        allMessages.addAll(receiverMessages);

        // Clear the existing content
        chatVbox.getChildren().clear();

        // Check if there are no messages
        if (allMessages.isEmpty()) {
            emptyMessage.setText("Start chatting now!");
            emptyMessage.setVisible(true);
        } else {
            emptyMessage.setVisible(false);

            // Iterate through the messages and add them to the chatVbox with margins
            for (Messagerie message : allMessages) {
                HBox messageBox = createMessageBox(message);
                VBox.setMargin(messageBox, new Insets(0.0, 0.0, 10.0, 0.0)); // Set top and bottom margins
                chatVbox.getChildren().add(messageBox);
            }
        }
    }


    private HBox createMessageBox(Messagerie message) {
        HBox messageHbox = new HBox();
        messageHbox.setSpacing(10.0);

        ImageView profileImage = new ImageView(new Image("/images/manh.png"));
        profileImage.setFitWidth(40.0);
        profileImage.setFitHeight(40.0);

        AnchorPane messagePane = new AnchorPane();
        messagePane.setStyle("-fx-background-color: #f2f5ec; -fx-background-radius: 0 30 30 30;");

        Label contentLabel = new Label(message.getContenu());
        contentLabel.setLayoutX(17.0);
        contentLabel.setLayoutY(10.0);
        contentLabel.setStyle("-fx-text-fill: #000000; -fx-font-weight: bold; -fx-font-size: 13; -fx-padding: 0 0 15 0;");
        contentLabel.setWrapText(true);
        contentLabel.setMaxWidth(350);

        messagePane.prefWidthProperty().bind(contentLabel.widthProperty().add(34.0));

        Label dateLabel = new Label(message.getDate().toString());
        dateLabel.setLayoutX(370.0);
        dateLabel.setLayoutY(40.0);
        dateLabel.setStyle("-fx-text-fill: #888888; -fx-font-size: 10;");

        Button deleteButton = new Button("", new ImageView(new Image("/images/de.png")));
        deleteButton.setLayoutX(370);
        deleteButton.setLayoutY(-40);
        ((ImageView) deleteButton.getGraphic()).setFitWidth(15);
        ((ImageView) deleteButton.getGraphic()).setFitHeight(15);
        deleteButton.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #FFFFFF; -fx-font-weight: bold; -fx-background-radius: 10; -fx-font-size: 13; -fx-cursor: hand;");

        Button editButton = new Button("", new ImageView(new Image("/images/message.png")));
        editButton.setLayoutX(380);
        editButton.setLayoutY(-40);
        ((ImageView) editButton.getGraphic()).setFitWidth(17);
        ((ImageView) editButton.getGraphic()).setFitHeight(17);
        editButton.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #FFFFFF; -fx-font-weight: bold; -fx-background-radius: 10; -fx-font-size: 13; -fx-cursor: hand;");

        Button downloadButton = new Button("", new ImageView(new Image("/images/d3.png")));
        downloadButton.setLayoutX(380);
        downloadButton.setLayoutY(-40);

        ImageView downloadImageView = (ImageView) downloadButton.getGraphic();
        downloadImageView.setFitWidth(17);
        downloadImageView.setFitHeight(17);

        downloadButton.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #FFFFFF; -fx-font-weight: bold; -fx-background-radius: 10; -fx-font-size: 13; -fx-cursor: hand;");

        editButton.setOnAction(event -> openEditMessagePopup(message));
        downloadButton.setOnAction(event -> {
            String fileName = message.getContenu();
            String filePath = "/downloads/" + fileName; // Adjust the path as needed



            try (InputStream inputStream = getClass().getResourceAsStream(filePath)) {
                if (inputStream != null) {
                 /*   Image image = new Image(inputStream);

                    // Set the image to the download button
                    downloadImageView.setImage(image);*/

                    Path destinationPath = Paths.get(System.getProperty("user.home"), "Downloads", fileName);

                    // Show a progress alert
                    Alert progressAlert = new Alert(Alert.AlertType.INFORMATION);
                    progressAlert.setTitle("Downloading File");
                    progressAlert.setHeaderText(null);
                    progressAlert.setContentText("Downloading file...");

                    // Create a background task to copy the file
                    Task<Void> copyTask = new Task<Void>() {
                        @Override
                        protected Void call() throws Exception {
                            // Simulate a 3-second download
                            Thread.sleep(1500);
                            Files.copy(inputStream, destinationPath, StandardCopyOption.REPLACE_EXISTING);
                            return null;
                        }
                    };

                    copyTask.setOnSucceeded(e -> {
                        // Close the progress alert after the task is completed
                        progressAlert.setResult(ButtonType.CANCEL);
                        System.out.println("File downloaded to: " + destinationPath.toString());

                        // Show a success message
                        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                        successAlert.setTitle("Download Successful");
                        successAlert.setHeaderText(null);
                        successAlert.setContentText("File downloaded successfully to: " + destinationPath.toString());
                        successAlert.showAndWait();
                    });

                    copyTask.setOnFailed(e -> {
                        // Close the progress alert after the task is completed
                        progressAlert.setResult(ButtonType.CANCEL);
                        e.getSource().getException().printStackTrace();
                        // Handle the exception (e.g., show an error message)

                        // Show a failure alert
                        Alert failureAlert = new Alert(Alert.AlertType.ERROR);
                        failureAlert.setTitle("Download Failed");
                        failureAlert.setHeaderText(null);
                        failureAlert.setContentText("Failed to download file: " + e.getSource().getException().getMessage());
                        failureAlert.showAndWait();
                    });

                    new Thread(copyTask).start();

                    // Use PauseTransition to close the alert after 3 seconds
                    PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
                    pause.setOnFinished(event1 -> progressAlert.setResult(ButtonType.CANCEL));
                    pause.play();

                    // Show the progress alert
                    progressAlert.showAndWait();
                } else {
                    // Show a failure alert
                    Alert failureAlert = new Alert(Alert.AlertType.ERROR);
                    failureAlert.setTitle("Download Failed");
                    failureAlert.setHeaderText(null);
                    failureAlert.setContentText("Failed to open input stream for file: " + filePath);
                    failureAlert.showAndWait();
                }
            } catch (IOException e) {
                e.printStackTrace();
                // Handle the exception (e.g., show an error message)
            }
        });

        deleteButton.setOnAction(event -> {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirmation Dialog");
            confirmationAlert.setHeaderText("Delete Message");
            confirmationAlert.setContentText("Are you sure you want to delete this message?");

            ButtonType confirmButton = new ButtonType("Yes");
            ButtonType cancelButton = new ButtonType("No");

            confirmationAlert.getButtonTypes().setAll(confirmButton, cancelButton);

            confirmationAlert.showAndWait().ifPresent(buttonType -> {
                if (buttonType == confirmButton) {
                    serviceMessagerie.supprimer(message.getIdMessage());
                    updateChatMessages();
                }
            });
        });






        if(CurrentUser.getRole()==0){
            if (message.getSenderId().getId_utilisateur()!=CurrentUser.getId_utilisateur()) {
                //  if (message.getSender_id().getId_utilisateur() == 1) {
                //System.out.println(receiverProfileImage);
                //profileImage = new ImageView(new Image("/images/"+CurrentUser.getProfileImagePath()));
                profileImage = new ImageView(new Image("/images/"+userPhoto));
                profileImage.setFitWidth(40.0);
                profileImage.setFitHeight(40.0);
                // messageHbox.getChildren().addAll(messagePane, dateLabel);
                messagePane.getChildren().addAll(contentLabel);
                messageHbox.getChildren().addAll(profileImage, messagePane);
                messageHbox.getChildren().add(dateLabel);
                messageHbox.getChildren().add(deleteButton);
                //messageHbox.getChildren().add(editButton);
                if ("text".equals(message.getType())) {
                    messageHbox.getChildren().add(editButton);
                }else {
                    String fileName = message.getContenu();
                    String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();

                    ImageView fileTypeImage = new ImageView();
                    fileTypeImage.setFitHeight(45.0);
                    fileTypeImage.setFitWidth(45.0);
                    fileTypeImage.setLayoutX(14.0);
                    fileTypeImage.setLayoutY(14.0);

                    if (fileExtension.equals("pdf")) {
                        fileTypeImage.setImage(new Image(getClass().getResource("/images/pdf.png").toExternalForm()));
                        messageHbox.getChildren().add(downloadButton);
                    } else if (fileExtension.equals("png") || fileExtension.equals("jpg")) {
                        fileTypeImage.setImage(new Image(getClass().getResource("/images/image.png").toExternalForm()));
                        messageHbox.getChildren().add(downloadButton);
                    } else {
                        messagePane.getChildren().addAll(contentLabel);
                    }

                    //messageHbox.getChildren().addAll(messagePane, dateLabel, deleteButton, downloadButton);

                    // messageHbox.getChildren().add(downloadButton);
                }
            } else {
                profileImage = new ImageView(new Image("/images/"+CurrentUser.getProfileImagePath()));
                //profileImage = new ImageView(new Image("/images/"+userPhoto));
                profileImage.setFitWidth(40.0);
                profileImage.setFitHeight(40.0);
                //DeleteButton.setLayoutX(370);
                //DeleteButton.setLayoutY(-40);
                //messageHbox.getChildren().addAll(profileImage, messagePane, dateLabel);
                messagePane.getChildren().addAll(contentLabel);
                messageHbox.getChildren().addAll(profileImage, messagePane);
                messageHbox.getChildren().add(dateLabel);
                messageHbox.getChildren().add(deleteButton);
                if ("text".equals(message.getType())) {
                    messageHbox.getChildren().add(editButton);
                }else {
                    String fileName = message.getContenu();
                    String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();

                    ImageView fileTypeImage = new ImageView();
                    fileTypeImage.setFitHeight(45.0);
                    fileTypeImage.setFitWidth(45.0);
                    fileTypeImage.setLayoutX(14.0);
                    fileTypeImage.setLayoutY(14.0);

                    if (fileExtension.equals("pdf")) {
                        fileTypeImage.setImage(new Image(getClass().getResource("/images/pdf.png").toExternalForm()));
                        messageHbox.getChildren().add(downloadButton);
                    } else if (fileExtension.equals("png") || fileExtension.equals("jpg")) {
                        fileTypeImage.setImage(new Image(getClass().getResource("/images/image.png").toExternalForm()));
                        messageHbox.getChildren().add(downloadButton);
                    } else {
                        messagePane.getChildren().addAll(contentLabel);
                    }

                    //messageHbox.getChildren().addAll(messagePane, dateLabel, deleteButton, downloadButton);

                    // messageHbox.getChildren().add(downloadButton);
                }
                //messageHbox.getChildren().add(editButton);
            }
        }else {
            if (message.getSenderId().getId_utilisateur()!=CurrentUser.getId_utilisateur()) {
                //  if (message.getSender_id().getId_utilisateur() == 1) {
                //System.out.println(receiverProfileImage);
                profileImage = new ImageView(new Image("/images/"+userPhoto));
                profileImage.setFitWidth(40.0);
                profileImage.setFitHeight(40.0);
                // messageHbox.getChildren().addAll(messagePane, dateLabel);
                messagePane.getChildren().addAll(contentLabel);
                messageHbox.getChildren().addAll(profileImage, messagePane);
                messageHbox.getChildren().add(dateLabel);
                if ("text".equals(message.getType())) {
                    System.out.println("Not Admin no Edit");
                    //messageHbox.getChildren().add(editButton);
                }else {
                    String fileName = message.getContenu();
                    String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();

                    ImageView fileTypeImage = new ImageView();
                    fileTypeImage.setFitHeight(45.0);
                    fileTypeImage.setFitWidth(45.0);
                    fileTypeImage.setLayoutX(14.0);
                    fileTypeImage.setLayoutY(14.0);

                    if (fileExtension.equals("pdf")) {
                        fileTypeImage.setImage(new Image(getClass().getResource("/images/pdf.png").toExternalForm()));
                        messageHbox.getChildren().add(downloadButton);
                    } else if (fileExtension.equals("png") || fileExtension.equals("jpg")) {
                        fileTypeImage.setImage(new Image(getClass().getResource("/images/image.png").toExternalForm()));
                        messageHbox.getChildren().add(downloadButton);
                    } else {
                        messagePane.getChildren().addAll(contentLabel);
                    }

                    //messageHbox.getChildren().addAll(messagePane, dateLabel, deleteButton, downloadButton);

                    // messageHbox.getChildren().add(downloadButton);
                }
                //messageHbox.getChildren().add(DeleteButton);
                //messageHbox.getChildren().add(EditButton);
            } else {
                System.out.println(CurrentUser.getProfileImagePath());
                profileImage = new ImageView(new Image("/images/"+CurrentUser.getProfileImagePath()));
                profileImage.setFitWidth(40.0);
                profileImage.setFitHeight(40.0);
                //DeleteButton.setLayoutX(370);
                //DeleteButton.setLayoutY(-40);
                //messageHbox.getChildren().addAll(profileImage, messagePane, dateLabel);
                messagePane.getChildren().addAll(contentLabel);
                messageHbox.getChildren().addAll(profileImage, messagePane);
                messageHbox.getChildren().add(dateLabel);
                messageHbox.getChildren().add(deleteButton);
                if ("text".equals(message.getType())) {
                    messageHbox.getChildren().add(editButton);
                }else {
                    String fileName = message.getContenu();
                    String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();

                    ImageView fileTypeImage = new ImageView();
                    fileTypeImage.setFitHeight(45.0);
                    fileTypeImage.setFitWidth(45.0);
                    fileTypeImage.setLayoutX(14.0);
                    fileTypeImage.setLayoutY(14.0);

                    if (fileExtension.equals("pdf")) {
                        fileTypeImage.setImage(new Image(getClass().getResource("/images/pdf.png").toExternalForm()));
                        messageHbox.getChildren().add(downloadButton);
                    } else if (fileExtension.equals("png") || fileExtension.equals("jpg")) {
                        fileTypeImage.setImage(new Image(getClass().getResource("/images/image.png").toExternalForm()));
                        messageHbox.getChildren().add(downloadButton);
                    } else {
                        messagePane.getChildren().addAll(contentLabel);
                    }

                    //messageHbox.getChildren().addAll(messagePane, dateLabel, deleteButton, downloadButton);

                    // messageHbox.getChildren().add(downloadButton);
                }
                //messageHbox.getChildren().add(editButton);
            }
        }

        return messageHbox;
    }


    public void navigateToAfficherReclamationAction(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AfficherReclamation.fxml"));
            TFmessage.getScene().setRoot(root);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Sorry");
            alert.setTitle("Error");
            alert.show();
        }

    }

    @FXML
    void cancelSendingFileAction(ActionEvent event) {
        // Clear the TFmessage and make it visible again
        TFmessage.setText("");
        TFmessage.setVisible(true);
        // Hide the cancelSendingFile button
        cancelSendingFileButton.setVisible(false);
        fileName.setVisible(false);
    }

    // Your existing fileButtonAction method
    public void fileButtonAction(ActionEvent event) {
        // Create a file chooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a File");

        // Show the file chooser dialog
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        // Check if a file was selected
        if (selectedFile != null) {
            // Set the TFmessage with the file name
            TFmessage.setText(selectedFile.getName());
            // Make TFmessage invisible
            TFmessage.setVisible(false);
            // Make the cancelSendingFile button visible
            cancelSendingFileButton.setVisible(true);
            fileName.setText(selectedFile.getName());
            fileName.setVisible(true);
        } else {
            System.out.println("No file selected");
        }
    }

    @FXML
    void ajouterMessageAction(ActionEvent event) {
        String messageContent = TFmessage.getText();

        // Check if TFmessage is empty
        if (messageContent.trim().isEmpty()) {
            // Set the messageError label to red
            messageError.setTextFill(Color.RED);
            TFmessage.setStyle("-fx-border-color: #FF0000;");
        } else {
            try {
                // Proceed to send the message and file information if it's not empty
                String lowerCaseContent = messageContent.toLowerCase();
                if (lowerCaseContent.endsWith(".png") || lowerCaseContent.endsWith(".jpg"))
                {
                    serviceMessagerie.ajouter(new Messagerie("image", messageContent, new Date(), userSender, userReciver));
                }
                else if (lowerCaseContent.endsWith(".pdf")) {
                    // Process as a file message
                    serviceMessagerie.ajouter(new Messagerie("file", messageContent, new Date(), userSender, userReciver));
                } else {
                    // Process as a text message
                    System.out.println(messageContent);
                    System.out.println(new Date());
                    System.out.println(userSender);
                    System.out.println(userReciver);
                    serviceMessagerie.ajouter(new Messagerie("text", messageContent, new Date(), userSender, userReciver));
                }
                TFmessage.setText("");
                TFmessage.setVisible(true); // Make TFmessage visible again
                cancelSendingFileButton.setVisible(false); // Hide the cancelSendingFile button
                fileName.setVisible(false);
                updateChatMessages();
            } catch (SQLException e) {
                // Show an alert for SQL exception
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("SQL Exception");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        }
    }

    private void openEditMessagePopup(Messagerie message) {
        // Create a new stage for the pop-up window
        Stage editStage = new Stage();
        editStage.setTitle("Edit Message");

        // Create a TextArea to allow the user to edit the message content
        TextArea editTextArea = new TextArea(message.getContenu());
        editTextArea.setWrapText(true); // Enable text wrapping
        editTextArea.setMaxWidth(300); // Set a max width to trigger wrapping
        editTextArea.setPrefRowCount(5);
        editTextArea.setPrefColumnCount(20);

        // Create a button to save the edited message
        Button saveButton = new Button("Update", new ImageView(new Image("/images/edit.png")));
        ((ImageView) saveButton.getGraphic()).setFitWidth(15);
        ((ImageView) saveButton.getGraphic()).setFitHeight(15);

        saveButton.setStyle("-fx-background-color:  #008000; -fx-text-fill: #FFFFFF; -fx-font-weight: bold; -fx-background-radius: 10; -fx-font-size: 13;");
        saveButton.setOnAction(saveEvent -> {
            // Update the message content in the database
            message.setContenu(editTextArea.getText());
            //message.setDate(new Date());
            serviceMessagerie.modifier(message);
            updateChatMessages();
            // Close the pop-up window
            editStage.close();
            // Update the chat messages
            //updateChatMessages();
        });

        // Create a VBox to hold the TextArea and the Save button
        VBox editVBox = new VBox(editTextArea, saveButton);
        editVBox.setSpacing(10.0);
        editVBox.setPadding(new Insets(10.0));

        // Create a scene and set it on the stage
        Scene editScene = new Scene(editVBox);
        editStage.setScene(editScene);

        // Show the pop-up window
        editStage.show();
    }

        public void GenerateQrCode(ActionEvent event) throws WriterException, IOException {
            // Include user information in the data variable
           String data = "Name: " + userReciver.getNom() + " " + userReciver.getPrenom() +
                    "\nEmail: " + userReciver.getAdresse() +
                    "\nCIN: " + userReciver.getCin() +
                    "\nImage: " + userReciver.getImage()+
                    "\nRole: Representant";

            String currentDir = System.getProperty("user.dir");
            String directoryPath = currentDir+"src/main/resources/downloads";
            String fileName = "QrCode"+userReciver.getNom()+userReciver.getPrenom()+".jpg";


            // Create the directory if it doesn't exist
            Path directory = Paths.get(directoryPath);
            Files.createDirectories(directory);

            // Create the QR code
            BitMatrix matrix = new MultiFormatWriter().encode(data, BarcodeFormat.QR_CODE, 500, 500);

            // Write the QR code image to the file
            Path filePath = directory.resolve(fileName);
            MatrixToImageWriter.writeToPath(matrix, "jpg", filePath);

            // Load the generated QR code image
            File qrCodeFile = new File(directoryPath, fileName);
            Image qrCodeImage = new Image(qrCodeFile.toURI().toString());

            // Set the loaded image to the ImageView
            this.qrCodeImage.setImage(qrCodeImage);

            System.out.println("QRCode successfully created at: " + filePath.toAbsolutePath());

        }



    public void makeCall(ActionEvent event) {

    }
}
