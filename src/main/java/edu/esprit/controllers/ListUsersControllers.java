package edu.esprit.controllers;

import edu.esprit.entities.Admin;
import java.util.Comparator;
import edu.esprit.entities.CurrentUser;
import edu.esprit.entities.Utilisateur;
import edu.esprit.services.ServiceUtilisateur;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import javax.management.relation.Role;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

public class ListUsersControllers implements Initializable {

    @FXML
    private AnchorPane AnchoPaneMessage131;
    @FXML
    private TextField searchField;
    @FXML
    private ImageView image_view;

    @FXML
    private TextField TFrecherche;

    @FXML
    private Label adresseLabel;

    @FXML
    private Label cinLabel;


    @FXML
    private AnchorPane container;

    @FXML
    private Label currentUserLabel;

    @FXML
    private Label nomLabel;

    @FXML
    private Label prenomLabel;

    @FXML
    private Label roleLabel;

    @FXML
    private ScrollPane scrollPaneClaim;

    @FXML
    private VBox utilisateurContainer;

    @FXML
    private ComboBox<String> comboRole;
    @FXML
    private Label CurrentUserEmail;
    @FXML
    private Label currentUserName;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ServiceUtilisateur serviceService = new ServiceUtilisateur();

        CurrentUserEmail.setText(CurrentUser.getAdresse());
        currentUserName.setText(CurrentUser.getNom());
        String imagePath = CurrentUser.getProfileImagePath();
        System.out.println(imagePath);
        String currentDir = System.getProperty("user.dir");
// imagePath = currentDir + "\\src\\main\\resources\\img" + imagePath; // Use double backslashes for path separators
        System.out.println(imagePath);

// Set the image file name to the TextField

// Display the image in the ImageView
        Image image = new Image("file:" + currentDir + "/src/main/resources/img/" + imagePath); // Use forward slashes for path separators
        image_view.setImage(image);



        // Do something with the imagePath, for example, display the image
        // imageView.setImage(new Image(imagePath));

        // Load all utilisateurs initially
        Set<Utilisateur> utilisateurs1 = null;
        Set<Admin> admins1 = null;
        try {
            utilisateurs1 = serviceService.getAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        admins1 = serviceService.getAll_admin();
        utilisateurs1.addAll(admins1);

        // Load and add UtilisateurItemComponent for each Utilisateur
        for (Utilisateur utilisateur : utilisateurs1) {
            loadUtilisateurItemComponent(utilisateur);
        }

        // Moved the 'role' declaration inside the lambda expression
        comboRole.setOnAction(event -> {
            String role = comboRole.getValue(); // Get the selected role

            try {
                Set<Utilisateur> utilisateurs = serviceService.getAll();
                Set<Admin> admins = serviceService.getAll_admin();
                utilisateurs.addAll(admins);

                // Clear previous components
                utilisateurContainer.getChildren().clear();

                // Load and add UtilisateurItemComponent for each Utilisateur based on the selected role
                for (Utilisateur utilisateur : utilisateurs) {
                    if (role.equals("NO FILTER") || role.equals("Admin") && serviceService.verifyRoleByCin(utilisateur.getCin()) == 0 ||
                            role.equals("Representant") && serviceService.verifyRoleByCin(utilisateur.getCin()) == 1 ||
                            role.equals("Candidat") && serviceService.verifyRoleByCin(utilisateur.getCin()) == 2) {
                        loadUtilisateurItemComponent(utilisateur);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            // Call your search method here, passing the newValue as the search term
            performSearch(newValue);
        });
    }

    // Helper method to load UtilisateurItemComponent
    private void loadUtilisateurItemComponent(Utilisateur utilisateur) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/UtilisateurItemComponent.fxml"));
        try {
            utilisateurContainer.getChildren().add(loader.load());
            UtilisateurItemComponent controller = loader.getController();
            controller.setUtilisateurData(utilisateur, container);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Helper method to load UtilisateurItemComponent


    @FXML
    void navigateToAjouterUtilisateur(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AjouterUtilisateur.fxml"));
            utilisateurContainer.getScene().setRoot(root);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Sorry");
            alert.setTitle("Error");
            alert.show();
        }
    }

    @FXML
    void navigatotoAccueil(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Pub.fxml"));
            utilisateurContainer.getScene().setRoot(root);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Sorry");
            alert.setTitle("Error");
            alert.show();
        }
    }

    @FXML
    public void onSearchTextChanged(KeyEvent keyEvent) {
        // Handle search text change
    }

    @FXML
    void NavigateToEtablissement(ActionEvent event) {
        try {

            Parent root = FXMLLoader.load(getClass().getResource("/Etablissement.fxml"));
            utilisateurContainer.getScene().setRoot(root);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Sorry");
            alert.setTitle("Error");
            alert.show();
        }

    }

    public void navigateToMakePost(ActionEvent actionEvent) {
        try {

            Parent root = FXMLLoader.load(getClass().getResource("/Pub.fxml"));
            utilisateurContainer.getScene().setRoot(root);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Sorry");
            alert.setTitle("Error");
            alert.show();
        }
    }
    public void navigateToReclamation(ActionEvent actionEvent) {
        try {

            Parent root = FXMLLoader.load(getClass().getResource("/AfficherReclamation.fxml"));
            utilisateurContainer.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Sorry");
            alert.setTitle("Error");
            alert.show();
        }
    }

    @FXML
    void Logout(ActionEvent event) {
        CurrentUser.setId_utilisateur(0);
        CurrentUser.setCin(0);
        CurrentUser.setNom("");
        CurrentUser.setPrenom("");
        CurrentUser.setMdp("");
        CurrentUser.setAdresse("");
        CurrentUser.setRole(-1);
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/Login.fxml"));
            cinLabel.getScene().setRoot(root);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Sorry");
            alert.setTitle("Error");
            alert.show();
        }
    }

    private void performSearch(String searchTerm) {
        ServiceUtilisateur serviceService = new ServiceUtilisateur();

        // Get the selected role from the combo box
        String role = comboRole.getValue();

        try {
            // Retrieve all users based on the selected role
            Set<Utilisateur> utilisateurs = serviceService.getAll();
            Set<Admin> admins = serviceService.getAll_admin();
            utilisateurs.addAll(admins);

            // Clear previous components
            utilisateurContainer.getChildren().clear();

            // Load and add UtilisateurItemComponent for each Utilisateur based on the selected role and search term
            for (Utilisateur utilisateur : utilisateurs) {
                boolean roleFilterPassed = roleFilterPassed(role, serviceService, utilisateur);
                boolean searchTermMatched = searchTermMatched(searchTerm, utilisateur);

                if (roleFilterPassed && searchTermMatched) {
                    loadUtilisateurItemComponent(utilisateur);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Helper method to check if the user passes the role filter
    private boolean roleFilterPassed(String role, ServiceUtilisateur serviceService, Utilisateur utilisateur) {
        if (role == null || role.equals("NO FILTER")) {
            return true; // No role filter, so all users pass
        }

        int roleByCin = serviceService.verifyRoleByCin(utilisateur.getCin());

        switch (role) {
            case "Admin":
                return roleByCin == 0;
            case "Representant":
                return roleByCin == 1;
            case "Candidat":
                return roleByCin == 2;
            default:
                return false; // Unknown role, consider as not passing
        }
    }

    // Helper method to check if the user matches the search term
    private boolean searchTermMatched(String searchTerm, Utilisateur utilisateur) {
        if (searchTerm == null || searchTerm.isEmpty()) {
            return true; // No search term, so all users pass
        }

        String lowerCaseSearchTerm = searchTerm.toLowerCase();

        return utilisateur.getNom().toLowerCase().contains(lowerCaseSearchTerm)
                || utilisateur.getPrenom().toLowerCase().contains(lowerCaseSearchTerm)
                || utilisateur.getAdresse().toLowerCase().contains(lowerCaseSearchTerm)
                || String.valueOf(utilisateur.getCin()).contains(lowerCaseSearchTerm);
    }
    @FXML
    void trierparCin(ActionEvent event) {
        try {
            // Get the selected role
            String selectedRole = comboRole.getValue();

            // If role is null, set it to "NO FILTER"
            final String role = (selectedRole == null) ? "NO FILTER" : selectedRole;

            // Retrieve all users
            Set<Utilisateur> utilisateurs = new ServiceUtilisateur().getAll();
            Set<Admin> admins = new ServiceUtilisateur().getAll_admin();
            utilisateurs.addAll(admins);

            // Clear previous components
            utilisateurContainer.getChildren().clear();

            // Sort and filter users based on the selected role
            utilisateurs.stream()
                    .filter(utilisateur -> {
                        int roleByCin = new ServiceUtilisateur().verifyRoleByCin(utilisateur.getCin());
                        return role.equals("NO FILTER") || (roleByCin == 0 && role.equals("Admin")) ||
                                (roleByCin == 1 && role.equals("Representant")) ||
                                (roleByCin == 2 && role.equals("Candidat"));
                    })
                    .sorted(Comparator.comparingInt(Utilisateur::getCin))
                    .forEach(this::loadUtilisateurItemComponent);

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception (e.g., show an alert)
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Error sorting users by Cin.");
            alert.setTitle("Error");
            alert.show();
        }
    }
    @FXML
    void trierparNom(ActionEvent event) {
        try {
            // Get the selected role
            String selectedRole = comboRole.getValue();

            // If role is null, set it to "NO FILTER"
            final String role = (selectedRole == null) ? "NO FILTER" : selectedRole;

            // Retrieve all users
            Set<Utilisateur> utilisateurs = new ServiceUtilisateur().getAll();
            Set<Admin> admins = new ServiceUtilisateur().getAll_admin();
            utilisateurs.addAll(admins);

            // Clear previous components
            utilisateurContainer.getChildren().clear();

            // Sort and filter users based on the selected role
            utilisateurs.stream()
                    .filter(utilisateur -> {
                        int roleByCin = new ServiceUtilisateur().verifyRoleByCin(utilisateur.getCin());
                        return role.equals("NO FILTER") || (roleByCin == 0 && role.equals("Admin")) ||
                                (roleByCin == 1 && role.equals("Representant")) ||
                                (roleByCin == 2 && role.equals("Candidat"));
                    })
                    .sorted(Comparator.comparing(Utilisateur::getNom, String.CASE_INSENSITIVE_ORDER))
                    .forEach(this::loadUtilisateurItemComponent);

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception (e.g., show an alert)
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Error sorting users by Name.");
            alert.setTitle("Error");
            alert.show();
        }
    }
    @FXML
    void NavigateToQuiz(ActionEvent event) {
       Parent root =null;
        try {
            root = FXMLLoader.load(getClass().getResource("/AfficherQuiz.fxml"));
            comboRole.getScene().setRoot(root);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Sorry");
            alert.setTitle("Error");
            alert.show();
        }

    }


}
