package edu.esprit.controllers;

import edu.esprit.services.ServiceUtilisateur;
import javafx.scene.control.Alert;
import edu.esprit.entities.Utilisateur;
import edu.esprit.services.ServiceUtilisateur;

import java.sql.SQLException;

public class SupprimerUtilisateurController {

    private ServiceUtilisateur ServiceUtilisateur = new ServiceUtilisateur();

    public void supprimerUtilisateur(Utilisateur u) {
        try {
            ServiceUtilisateur.supprimer(u.getId_utilisateur());
            // Afficher une alerte de confirmation après la suppression
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Confirmation de suppression");
            alert.setHeaderText(null);
            alert.setContentText("La conversation a été supprimée avec succès.");
            alert.showAndWait();
        } catch (SQLException e) {
            e.printStackTrace();
            // Afficher une alerte en cas d'erreur lors de la suppression
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Erreur");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("Une erreur s'est produite lors de la suppression de la conversation.");
            errorAlert.showAndWait();
        }
    }
}