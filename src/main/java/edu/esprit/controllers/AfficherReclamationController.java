package edu.esprit.controllers;

import edu.esprit.entities.CurrentUser;
import edu.esprit.entities.Messagerie;
import edu.esprit.entities.Reclamation;
import edu.esprit.services.ServiceReclamation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.scene.input.KeyEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.ResourceBundle;




public class AfficherReclamationController implements Initializable {

    private final ServiceReclamation serviceReclamation = new ServiceReclamation();
    private boolean orderByDateAsc = true;

    @FXML
    private VBox reclamationsContainer;

    // Add the container variable
    @FXML
    private AnchorPane container;
    @FXML
    private AnchorPane RepresentantPane;

    @FXML
    private AnchorPane AdminPane;
    @FXML
    private AnchorPane CandidatPane;

    @FXML
    private TextField searchField;
    @FXML
    private ChoiceBox<String> CbFilter;

    @FXML
    private ImageView currentUserPhoto;
    @FXML
    private javafx.scene.control.Label currentUserFullName;
    @FXML
    private Label currentUserAdresse;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set a default value for the ChoiceBox
    //    CbFilter.setValue("All");
        // Add an event listener to the ChoiceBox
      //  CbFilter.setOnAction(this::handleFilterChange);
        String imagePath = CurrentUser.getProfileImagePath();
        System.out.println(imagePath);
        String currentDir = System.getProperty("user.dir");
// imagePath = currentDir + "\\src\\main\\resources\\img" + imagePath; // Use double backslashes for path separators
        System.out.println(imagePath);

// Set the image file name to the TextField

// Display the image in the ImageView
        Image image = new Image("file:" + currentDir + "/src/main/resources/img/" + imagePath); // Use forward slashes for path separators
        currentUserPhoto.setImage(image);

        currentUserFullName.setText(CurrentUser.getNom() +" "+CurrentUser.getPrenom());
        currentUserAdresse.setText(CurrentUser.getAdresse());

        searchField.setOnKeyReleased(this::handleSearch);
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
        // Retrieve data from the database
        try {
            if(CurrentUser.getRole()==0){
                Set<Reclamation> reclamations = serviceReclamation.getAll();
                //Set<Reclamation> reclamations = serviceReclamation.getAllRecByUser(candidat.getId_utilisateur());
                // Load and add ReclamationItemComponent for each Reclamation
                for (Reclamation reclamation : reclamations) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/ReclamationItemComponent.fxml"));
                    try {
                        reclamationsContainer.getChildren().add(loader.load());
                        ReclamationItemComponentController controller = loader.getController();
                        controller.setReclamationData(reclamation, container);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            else {
                //Set<Reclamation> reclamations = serviceReclamation.getAll();
                System.out.println(CurrentUser.getId_utilisateur());
                //Set<Reclamation> reclamations = serviceReclamation.getAllRecByUser(CurrentUser.getId_utilisateur());
                Set<Reclamation> reclamations = serviceReclamation.getAll();
                // Load and add ReclamationItemComponent for each Reclamation
                for (Reclamation reclamation : reclamations) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/ReclamationItemComponent.fxml"));
                    try {
                        reclamationsContainer.getChildren().add(loader.load());
                        ReclamationItemComponentController controller = loader.getController();
                        controller.setReclamationData(reclamation, container);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /*private void handleFilterChange(ActionEvent event) {
        // Get the selected filter from the ChoiceBox
        String selectedFilter = CbFilter.getValue();
        System.out.println("Selected Filter: " + selectedFilter);

        // Update the reclamation list based on the selected filter
        updateReclamationsList(selectedFilter);
    }*/

   /* private void updateReclamationsList(String filter) {
        // Clear existing reclamation items
        reclamationsContainer.getChildren().clear();

        try {
            Set<Reclamation> reclamations;

            // Apply the appropriate filter logic based on the selected option
            if ("Date".equals(filter)) {
                reclamations = serviceReclamation.getAllOrderByDate(); // Replace with your date-based filtering logic
            }else if ("MostReactions".equals(filter)) {
                reclamations = serviceReclamation.getAllOrderByMostReactions(); // Filter by status 0 (Unseen)
            } else if ("Unseen".equals(filter)) {
                reclamations = serviceReclamation.getAllByStatusOrderByDate(0); // Filter by status 0 (Unseen)
            } else if ("Seen".equals(filter)) {
                reclamations = serviceReclamation.getAllByStatusOrderByDate(1); // Filter by status 1 (Seen)
            } else if ("All".equals(filter)) {
                reclamations = serviceReclamation.getAll(); // Filter by status 1 (Seen)
            } else {
                // Default case (no filter)
                reclamations = serviceReclamation.getAll();
                //nbReactions
            }

            // Load and add ReclamationItemComponent for each filtered Reclamation
            for (Reclamation reclamation : reclamations) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ReclamationItemComponent.fxml"));
                try {
                    reclamationsContainer.getChildren().add(loader.load());
                    ReclamationItemComponentController controller = loader.getController();
                    controller.setReclamationData(reclamation, container);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }*/

    @FXML
    private void handleSearch(KeyEvent event) {
        // Get the search query from the TextField
 String searchQuery = searchField.getText().toLowerCase().trim();
       // String searchQuery = searchField.getText().trim();

        // Clear existing reclamation items
        reclamationsContainer.getChildren().clear();

        // Retrieve data from the database based on the search query
        try {
            Set<Reclamation> filteredReclamations;
            if (CurrentUser.getRole() == 0) {
                filteredReclamations = serviceReclamation.getAll(); // or apply the appropriate filter logic
            } else {
                filteredReclamations = serviceReclamation.getAllRecByUser(CurrentUser.getId_utilisateur());
            }

            // Load and add ReclamationItemComponent for each filtered Reclamation
            for (Reclamation reclamation : filteredReclamations) {
                // Check if any of the fields contain the search query
                if (containsSearchQuery(reclamation, searchQuery)) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/ReclamationItemComponent.fxml"));
                    try {
                        reclamationsContainer.getChildren().add(loader.load());
                        ReclamationItemComponentController controller = loader.getController();
                        controller.setReclamationData(reclamation, container);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to check if any of the fields in the Reclamation object contain the search query
    private boolean containsSearchQuery(Reclamation reclamation, String searchQuery) {
        String nomPrenom = reclamation.getUser().getNom().toLowerCase() +" "+ reclamation.getUser().getPrenom().toLowerCase();
        String cin = String.valueOf(reclamation.getUser().getCin());
        String trimmedSearchQuery = searchQuery.trim().toLowerCase();

        // Check if any of the fields contain the search query
        return nomPrenom.contains(trimmedSearchQuery)|| cin.contains(trimmedSearchQuery);
    }

    public void navigateToAddReclamation(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AjouterReclamation.fxml"));
            reclamationsContainer.getScene().setRoot(root);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Sorry");
            alert.setTitle("Error");
            alert.show();
        }
    }



    public void openAiHelper(ActionEvent event) {
        // Create a new stage for the pop-up window
      /*  Stage aiHelperStage = new Stage();
        aiHelperStage.setTitle("OpenAI Helper");

        // Create a VBox to hold any content you want in the popup
        VBox aiHelperVBox = new VBox();

        // Create a scene and set it on the stage
        Scene aiHelperScene = new Scene(aiHelperVBox);
        aiHelperStage.setScene(aiHelperScene);

        // Show the pop-up window
        aiHelperStage.show();*/
        String githubLink = "https://github.com/InnoMinds1/InnoHire";

        try {
            // Create a URI object from the GitHub link
            URI uri = new URI(githubLink);

            // Open the link in the default browser
            Desktop.getDesktop().browse(uri);
        } catch (URISyntaxException | IOException e) {
            // Handle any exceptions that might occur
            e.printStackTrace();
        }
    }

    public void navigateToChatBot(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ChatBotAi.fxml"));
            searchField.getScene().setRoot(root);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Sorry");
            alert.setTitle("Error");
            alert.show();
        }
    }

    private void refreshReclamationsByDateOrder(boolean orderByOldest) {
        // Clear existing reclamation items
        reclamationsContainer.getChildren().clear();

        try {
            Set<Reclamation> reclamations;

            // Order reclamations based on the specified order
            if (orderByOldest) {
                reclamations = serviceReclamation.getAllOrderByOldestDateAndTime();
            } else {
                reclamations = serviceReclamation.getAllOrderByNewestDateAndTime();
            }

            // Load and add ReclamationItemComponent for each ordered Reclamation
            for (Reclamation reclamation : reclamations) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ReclamationItemComponent.fxml"));
                try {
                    reclamationsContainer.getChildren().add(loader.load());
                    ReclamationItemComponentController controller = loader.getController();
                    controller.setReclamationData(reclamation, container);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void OrderByDate(ActionEvent event) {
        // Refresh the displayed reclamations by ordering them by the newest date
        refreshReclamationsByDateOrder(false);
    }

    @FXML
    public void OrderByOldestDate(ActionEvent event) {
        // Refresh the displayed reclamations by ordering them by the oldest date
        refreshReclamationsByDateOrder(true);
    }


    public void navigateToUserList(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/listUsers.fxml"));
            searchField.getScene().setRoot(root);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Sorry");
            alert.setTitle("Error");
            alert.show();
        }
    }

    public void navigateToPostList(MouseEvent mouseEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Pub.fxml"));
            searchField.getScene().setRoot(root);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Sorry");
            alert.setTitle("Error");
            alert.show();
        }
    }

    public void navigateToEtablissementList(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Etablissement.fxml"));
            searchField.getScene().setRoot(root);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Sorry");
            alert.setTitle("Error");
            alert.show();
        }
    }

    public void navigateToSettings(ActionEvent event) {
        System.out.println("settings");
    }

    public void navigateToChat(ActionEvent event) {
        /*try {
            Parent root = FXMLLoader.load(getClass().getResource("/ChatBotAi.fxml"));
            searchField.getScene().setRoot(root);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Sorry");
            alert.setTitle("Error");
            alert.show();
        }*/
    }
}
