package edu.esprit.controllers;

import edu.esprit.entities.CurrentUser;
import edu.esprit.entities.PostAudience;
import edu.esprit.entities.Reclamation;
import edu.esprit.services.ServiceReclamation;
import edu.esprit.services.ServiceUtilisateur;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.IOException;
import java.sql.SQLException;

public class ModifierReclamationController {
    @FXML
    private TextField TFType;

    @FXML
    private TextArea TADescription;

    @FXML
    private TextField TFTitre;

    @FXML
    private Label TitleError;

    @FXML
    private Label TypeError;

    @FXML
    private Label DescriptionError;

    @FXML
    private ImageView userPhoto;

    @FXML
    private Label labelFullName;

    @FXML
    private Label labelCin;

    @FXML
    private Label labelEmail;

    @FXML
    private Label labelRole;

    @FXML
    private ImageView userPhotoPub;

    @FXML
    private Label labelFullNamePub;

    @FXML
    private Label labelCinPub;

    @FXML
    private Label labelEmailPub;

    @FXML
    private Label labelRolePub;

    @FXML
    private Label labelNbShares;
    @FXML
    private Label labelNbReactions;
    @FXML
    private Label labelNbComments;
    @FXML
    private ImageView ImagePub;
    @FXML
    private ImageView ppPublic;

    @FXML
    private Label labelDatePub;

    @FXML
    private Label labelNbReports;

    @FXML
    private AnchorPane AnchoPaneClaim;
    @FXML
    private AnchorPane RepresentantPane;

    @FXML
    private AnchorPane AdminPane;
    @FXML
    private AnchorPane CandidatPane;


    private Reclamation selectedReclamation;
    private ServiceReclamation serviceReclamation = new ServiceReclamation();
    private ServiceUtilisateur su = new ServiceUtilisateur();

    public void initData(Reclamation selectedReclamation) {
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

        // Store the selected Reclamation for later use
        this.selectedReclamation = selectedReclamation;

        //labelNbShares.setText(String.valueOf(selectedReclamation.getPub().getNbShares()));
        labelNbReactions.setText(String.valueOf(selectedReclamation.getPub().getTotalReactions()));
        //labelNbComments.setText(String.valueOf(selectedReclamation.getPub().getNbShares()));
        labelDatePub.setText(String.valueOf(selectedReclamation.getPub().getDate()));
        if (selectedReclamation.getPub().getAudience()== PostAudience.PUBLIC) {
            ppPublic.setImage(new Image(getClass().getResource("/img/world (1).png").toExternalForm()));
        } else {
            // Set a default image if the name is not available
            ppPublic.setImage(new Image(getClass().getResource("/img/friends.png").toExternalForm()));
        }
        String imgPost = selectedReclamation.getPub().getImage();
        if (imgPost != null  && !imgPost.isEmpty()) {
            String imagePath = "/img/" + imgPost; // Assuming images are stored in src/main/resources/images
            Image image = new Image(getClass().getResource(imagePath).toExternalForm());
            ImagePub.setImage(image);
        } else {
            // Set a default image if the name is not available
            ImagePub.setImage(new Image(getClass().getResource("/img/postCandi.png").toExternalForm()));
        }


        // Initialize the fields with data from the selected Reclamation
        TFType.setText(selectedReclamation.getType());
        TFTitre.setText(selectedReclamation.getTitre());
        //datePicker.setValue(LocalDate.now());
        TADescription.setText(selectedReclamation.getDescription());


        if (CurrentUser.getRole()==0){
            labelFullName.setText(selectedReclamation.getUser().getNom()+" "+selectedReclamation.getUser().getPrenom());
            labelCin.setText(String.valueOf(selectedReclamation.getUser().getCin()));
            if (selectedReclamation.getUser().getRole()==1)
            {
                labelRole.setText("Representant");
                labelRole.setText("Representant");
            }else {
                labelRole.setText("Candidat");
                labelRolePub.setText("Candidat");
            }
            labelEmail.setText(selectedReclamation.getUser().getAdresse());
        }else
        {
            AnchoPaneClaim.setVisible(false);
        }



        labelFullNamePub.setText(selectedReclamation.getPub().getUtilisateur().getNom()+" "+selectedReclamation.getPub().getUtilisateur().getPrenom());
        labelCinPub.setText(String.valueOf(selectedReclamation.getPub().getUtilisateur().getCin()));
        labelEmailPub.setText(selectedReclamation.getPub().getUtilisateur().getAdresse());

        String imageNamePub = selectedReclamation.getPub().getUtilisateur().getImage();
        //String imageNamePub = su.getImagefromCin(cinPub);
        if (imageNamePub != null  && !imageNamePub.isEmpty()) {
            String imagePath = "/img/" + imageNamePub; // Assuming images are stored in src/main/resources/images
            Image image = new Image(getClass().getResource(imagePath).toExternalForm());
            userPhotoPub.setImage(image);
        } else {
            // Set a default image if the name is not available
            userPhotoPub.setImage(new Image(getClass().getResource("/img/edit.png").toExternalForm()));
        }

        //labelNbReports.setText(String.valueOf(selectedReclamation.getPub().getNb_report()));
        // Set user photo
        String imageName = selectedReclamation.getUser().getImage();

        ///System.out.println(imageNamePub);// Replace with the actual method to get the image name
        if (imageName != null  && !imageName.isEmpty()) {
            String imagePath = "/img/" + imageName; // Assuming images are stored in src/main/resources/images
            Image image = new Image(getClass().getResource(imagePath).toExternalForm());
            userPhoto.setImage(image);
        } else {
            // Set a default image if the name is not available
            userPhoto.setImage(new Image(getClass().getResource("/img/edit.png").toExternalForm()));
        }

    }


    /*public void ModifierReclamation(ActionEvent event) {
        // Get the modified values from the input fields
        String newType = TFType.getText();
        String newTitre = TFTitre.getText();
        // LocalDate newDate = datePicker.getValue();  // Uncomment this line if you have a DatePicker
        String newDescription = TADescription.getText();

        // Store the original values
        String originalType = selectedReclamation.getType();
        String originalTitre = selectedReclamation.getTitre();
        // LocalDate originalDate = selectedReclamation.getDate().toLocalDateTime().toLocalDate();
        String originalDescription = selectedReclamation.getDescription();

        LocalDateTime currentDateTime = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(currentDateTime);

        // Regular expression to allow only letters
        String lettersOnlyRegex = "^[a-zA-Z]+$";


        if (selectedReclamation!=null) {

            if (newTitre.trim().isEmpty()){
                TitleError.setText("Please enter a non-empty title");
                TitleError.setTextFill(Color.RED);
                TFTitre.setStyle("-fx-border-color:  #FF0000;");
            }

            if (newType.trim().isEmpty()){
                TypeError.setText("Please enter a non-empty type");
                TypeError.setTextFill(Color.RED);
                TFType.setStyle("-fx-border-color:  #FF0000;");
            }
            if (newDescription.trim().isEmpty()){
                DescriptionError.setText("Please enter a non-empty description");
                DescriptionError.setTextFill(Color.RED);
                TADescription.setStyle("-fx-border-color:  #FF0000;");
            }

            // Check if the newTitre contains only letters
            if (!newTitre.matches(lettersOnlyRegex)) {
                TitleError.setTextFill(Color.RED);
                TFTitre.setStyle("-fx-border-color:  #FF0000;");
                TitleError.setText("Titre should contain only letters");
                return;  // Exit the method if newTitre is invalid
            }

            // Check if the newType contains only letters
            if (!newType.matches(lettersOnlyRegex)) {
                TypeError.setTextFill(Color.RED);
                TFType.setStyle("-fx-border-color:  #FF0000;");
                TypeError.setText("Type should contain only letters");
                return;  // Exit the method if newType is invalid
            }

            // Update the selectedReclamation object with the modified values
            selectedReclamation.setType(newType);
            selectedReclamation.setTitre(newTitre);
            selectedReclamation.setDate(timestamp);
            selectedReclamation.setDescription(newDescription);

            try {
                // Call the update method from ServiceReclamation to update the record in the database
                serviceReclamation.modifier(selectedReclamation);

                // Show success alert
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Success");
                successAlert.setContentText("Reclamation updated successfully!");
                successAlert.show();
                navigateToAfficherReclamationAction(event);
            } catch (SQLException e) {
                // Handle any SQL exception that might occur during the update
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("SQL Exception");
                errorAlert.setContentText(e.getMessage());
                errorAlert.showAndWait();
            }
        } else {
            // If no modifications are made, show an information alert
            Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
            infoAlert.setTitle("Information");
            infoAlert.setContentText("No changes were made to the reclamation.");
            infoAlert.show();
        }
    }*/
    public void ModifierReclamation(ActionEvent event) {
        // Get the modified values from the input fields
        String newType = TFType.getText().trim();
        String newTitre = TFTitre.getText().trim();
        // LocalDate newDate = datePicker.getValue();  // Uncomment this line if you have a DatePicker
        String newDescription = TADescription.getText().trim();

        // Regular expression to allow letters and spaces
        String lettersAndSpacesRegex = "^[a-zA-Z\\s]+$";

        // Check if title is not empty
        if (newTitre.isEmpty()) {
            TitleError.setText("Please enter a non-empty title");
            TitleError.setTextFill(Color.RED);
            TFTitre.setStyle("-fx-border-color:  #FF0000;");
        } else if (!newTitre.matches(lettersAndSpacesRegex)) {  // Check if title contains only letters and spaces
            TitleError.setText("Title should contain only letters and spaces");
            TitleError.setTextFill(Color.RED);
            TFTitre.setStyle("-fx-border-color:  #FF0000;");
        } else {
            TitleError.setText("");  // Clear the error text
            TitleError.setTextFill(Color.BLACK);  // Set the text color to black
            TFTitre.setStyle("");  // Reset the border color
        }

        // Check if type is not empty
        if (newType.isEmpty()) {
            TypeError.setText("Please enter a non-empty type");
            TypeError.setTextFill(Color.RED);
            TFType.setStyle("-fx-border-color:  #FF0000;");
        } else if (!newType.matches(lettersAndSpacesRegex)) {  // Check if type contains only letters and spaces
            TypeError.setText("Type should contain only letters and spaces");
            TypeError.setTextFill(Color.RED);
            TFType.setStyle("-fx-border-color:  #FF0000;");
        } else {
            TypeError.setText("");  // Clear the error text
            TypeError.setTextFill(Color.BLACK);  // Set the text color to black
            TFType.setStyle("");  // Reset the border color
        }

        // Check if description is not empty
        if (newDescription.isEmpty()) {
            DescriptionError.setText("Please enter a non-empty description");
            DescriptionError.setTextFill(Color.RED);
            TADescription.setStyle("-fx-border-color:  #FF0000;");
        } else {
            DescriptionError.setText("");  // Clear the error text
            DescriptionError.setTextFill(Color.BLACK);  // Set the text color to black
            TADescription.setStyle("");  // Reset the border color
        }

        // If any of the error labels still have text, return without updating
        if (!TitleError.getText().isEmpty() || !TypeError.getText().isEmpty() || !DescriptionError.getText().isEmpty()) {
            return;
        }

        // Check if no modifications are made
        if (newType.equals(selectedReclamation.getType())
                && newTitre.equals(selectedReclamation.getTitre())
                && newDescription.equals(selectedReclamation.getDescription())) {
            // If no modifications are made, show an information alert
            Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
            infoAlert.setTitle("Information");
            infoAlert.setContentText("No changes were made to the reclamation.");
            infoAlert.show();
            return;
        }

        // Update the selectedReclamation object with the modified values
        selectedReclamation.setType(newType);
        selectedReclamation.setTitre(newTitre);
        selectedReclamation.setDescription(newDescription);

        try {
            // Call the update method from ServiceReclamation to update the record in the database
            serviceReclamation.modifier(selectedReclamation);

            // Show success alert
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Success");
            successAlert.setContentText("Reclamation updated successfully!");
            successAlert.show();
            navigateToAfficherReclamationAction(event);
        } catch (SQLException e) {
            // Handle any SQL exception that might occur during the update
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("SQL Exception");
            errorAlert.setContentText(e.getMessage());
            errorAlert.showAndWait();
        }
    }

    @FXML
    private void downloadFileHandler() {
        //String nbShares = String.valueOf(selectedReclamation.getPub().getNbShares());
        String nbReactions = String.valueOf(selectedReclamation.getPub().getTotalReactions());
        //String nbComments = String.valueOf(selectedReclamation.getPub().getNbShares());
        String date = String.valueOf(selectedReclamation.getPub().getDate());

        try {
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            // Set font
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);

            // Write data to the PDF
            // Write data to the PDF
            contentStream.beginText();
            contentStream.newLineAtOffset(50, 700);
            contentStream.showText("Invoice");
            contentStream.endText();

            /*contentStream.beginText();
            contentStream.newLineAtOffset(100, 680);
            contentStream.showText("Shares: " + nbShares);
            contentStream.endText();*/

            contentStream.beginText();
            contentStream.newLineAtOffset(100, 660);
            contentStream.showText("Reactions: " + nbReactions);
            contentStream.endText();

          /*  contentStream.beginText();
            contentStream.newLineAtOffset(100, 640);
            contentStream.showText("Comments: " + nbComments);
            contentStream.endText();*/

            contentStream.beginText();
            contentStream.newLineAtOffset(100, 620);
            contentStream.showText("Date: " + date);
            contentStream.endText();

            // Add an image to the PDF
            //PDImageXObject pdImage = PDImageXObject.createFromFile("src/main/resources/img/folder.png", document);
            //contentStream.drawImage(pdImage, 50, 500, pdImage.getWidth(), pdImage.getHeight());
            // Add an image to the PDF
            PDImageXObject pdImage = PDImageXObject.createFromFile("src/main/resources/img/folder.png", document);
            float imageWidth = 50;//pdImage.getWidth();
            float imageHeight = 50;//pdImage.getHeight();
            float xImage = page.getMediaBox().getWidth() - imageWidth - 50; // X-coordinate of the image (top right corner)
            float yImage = page.getMediaBox().getHeight() - imageHeight - 50; // Y-coordinate of the image (top right corner)
            contentStream.drawImage(pdImage, xImage, yImage, imageWidth, imageHeight);


            // Close the content stream
            contentStream.close();

            // Save the document to a file (you can modify this as needed)
            document.save("src/main/resources/downloads/invoice.pdf");

            // Close the document
            document.close();

            System.out.println("PDF invoice generated successfully.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    public void navigateToAfficherReclamationAction(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AfficherReclamation.fxml"));
            TFTitre.getScene().setRoot(root);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Sorry");
            alert.setTitle("Error");
            alert.show();
        }
    }

}
