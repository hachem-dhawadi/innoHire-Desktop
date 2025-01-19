package edu.esprit.controllers;
import edu.esprit.entities.Admin;
import edu.esprit.entities.Candidat;
import edu.esprit.entities.Representant;
import edu.esprit.entities.Utilisateur;
import edu.esprit.services.ServiceUtilisateur;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;


public class AfficherUtilisateurController implements Initializable {
    @FXML
    private ListView<Utilisateur> listView;
    @FXML
    private ComboBox<String> comboRole;
    @FXML
    private TextField TFrecherche;
    private FilteredList<Utilisateur> filteredUsers;


    // Original list of Utilisateurs
    private ObservableList<Utilisateur> originalUtilisateurs;

    // Filtered list based on role
    private ObservableList<Utilisateur> filteredUtilisateurs;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ServiceUtilisateur serviceService = new ServiceUtilisateur();
        Set<Utilisateur> utilisateurs = null;
        Set<Admin> admins = null;

        try {
            utilisateurs = serviceService.getAll();
            admins = serviceService.getAll_admin();
            utilisateurs.addAll(admins);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        originalUtilisateurs = FXCollections.observableArrayList(utilisateurs);
        //filteredUtilisateurs = FXCollections.observableArrayList(originalUtilisateurs);
        filteredUsers = new FilteredList<>(originalUtilisateurs);

        listView.setItems(filteredUsers);

        listView.setCellFactory(param -> new ListCell<Utilisateur>() {
            @Override
            protected void updateItem(Utilisateur item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    String format = "%-15s%-20s%-25s%-30s%-10d";

                    if (item instanceof Admin) {
                        setText(String.format(format, item.getCin(), item.getNom(), item.getPrenom(), item.getAdresse(), 0));
                    } else if (item instanceof Representant) {
                        setText(String.format(format, item.getCin(), item.getNom(), item.getPrenom(), item.getAdresse(), 1));
                    } else if (item instanceof Candidat) {
                        setText(String.format(format, item.getCin(), item.getNom(), item.getPrenom(), item.getAdresse(), 2));
                    }
                }
            }
        });

        comboRole.setOnAction(event -> filterByRole(comboRole.getValue()));

        // Add a listener to the search text field to dynamically update the search
        TFrecherche.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredUsers.setPredicate(user -> {
                if (newValue.isEmpty()) {
                    return true; // Show all users if search term is empty
                }
                String cinAsString = String.valueOf(user.getCin());

                // Customize this based on how you want to search (by CIN, Nom, Prenom, Email, etc.)
                return cinAsString.toLowerCase().contains(newValue.toLowerCase()) ||
                        user.getNom().toLowerCase().contains(newValue.toLowerCase()) ||
                        user.getPrenom().toLowerCase().contains(newValue.toLowerCase()) ||
                        user.getAdresse().toLowerCase().contains(newValue.toLowerCase());
            });
        });
    }


    private void filterByRole(String selectedRole) {
        if ("Admin".equals(selectedRole)) {
            filteredUsers.setPredicate(u -> u.getRole() == 0);
        } else if ("Representant".equals(selectedRole)) {
            filteredUsers.setPredicate(u -> u.getRole() == 1);
        } else if ("Candidat".equals(selectedRole)) {
            filteredUsers.setPredicate(u -> u.getRole() == 2);
        } else {
            filteredUsers.setPredicate(u -> true);
        }
    }




    @FXML
    void supprimerUtilisateur(ActionEvent event) {
        Utilisateur selectedUtilisateur = listView.getSelectionModel().getSelectedItem();
        if (selectedUtilisateur != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation de suppression");
            alert.setHeaderText(null);
            alert.setContentText("Êtes-vous sûr de vouloir supprimer cet utilisateur?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    int cin = selectedUtilisateur.getCin();
                    ServiceUtilisateur serviceUtilisateur = new ServiceUtilisateur();
                    serviceUtilisateur.supprimer_par_cin(cin);
                    listView.getItems().remove(selectedUtilisateur);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }


    }





    @FXML
    public void modifierUtilisateur(ActionEvent actionEvent) {
        // Code pour modifier l'utilisateur sélectionné dans la liste
        Utilisateur selectedUtilisateur = listView.getSelectionModel().getSelectedItem();
        if (selectedUtilisateur != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierUtilisateur.fxml"));
                Parent root = loader.load();
                ModifierUtilisateurController controller = loader.getController();
                controller.initData(selectedUtilisateur); // Passer l'utilisateur sélectionné au contrôleur de l'interface de modification

                // Obtenir la scène actuelle
                Scene scene = listView.getScene();

                // Changer le contenu de la scène
                scene.setRoot(root);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public void ajouterUtilisateur(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AjouterUtilisateur.fxml"));
            listView.getScene().setRoot(root);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Sorry");
            alert.setTitle("Error");
            alert.show();
        }

    }
    @FXML
    void NavigateToAcc(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Accueil.fxml"));
            listView.getScene().setRoot(root);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Sorry");
            alert.setTitle("Error");
            alert.show();
        }
    }


}
