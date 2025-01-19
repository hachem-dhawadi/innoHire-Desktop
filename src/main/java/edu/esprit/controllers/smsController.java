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
import java.net.*;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;

public class smsController {
    @FXML
    private TextField TFapikey;

    @FXML
    private TextField TFmessage;

    @FXML
    private TextField TFnumber;

    @FXML
    private TextField TFsender;

    @FXML
    void sendAction(ActionEvent event) {
        try {
            // Construct data
            String apiKey = "apikey=" + "NzE3MjQxNzE1MzcxNzI0YTUyNDc2ZjMwNzk1NzY5NDk=";
            String message = "&message=" + "just a try";
            String sender = "&sender=" + "trying this ";
            String numbers = "&numbers=" + "55600034";

            // Send data
            HttpURLConnection conn = (HttpURLConnection) new URL("https://api.txtlocal.com/send/?").openConnection();
            String data = apiKey + numbers + message + sender;
            System.out.println(conn);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
            conn.getOutputStream().write(data.getBytes("UTF-8"));
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuffer stringBuffer = new StringBuffer();
            String line;
            while ((line = rd.readLine()) != null) {
                stringBuffer.append(line).append("\n");

            }
            System.out.println(stringBuffer.toString());
            rd.close();

           // return stringBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();  // Add this line
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("no ");
            alert.showAndWait();
        }
    }
    }

