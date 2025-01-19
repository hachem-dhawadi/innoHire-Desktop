package edu.esprit.controllers;

import edu.esprit.entities.CurrentEtablissement;
import edu.esprit.entities.CurrentWallet;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class TestKhedmetGafsi implements Initializable {
    @FXML
   private TextField labelNomCurrentEtab ;


    @FXML
         private TextField   labelBalanceCurrentWallet;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        labelNomCurrentEtab.setText(CurrentEtablissement.getNom());

        labelBalanceCurrentWallet.setText(String.valueOf(CurrentWallet.getBalance()));
    }


}
