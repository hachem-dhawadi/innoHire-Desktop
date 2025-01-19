package edu.esprit.controllers;

import edu.esprit.entities.*;
import edu.esprit.services.ServiceEtablissement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import edu.esprit.services.MyListener;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

public class EtablissementController implements Initializable {
    private Wallet walletCo;
    private Etablissement etablissementCo;
    @FXML
    private VBox chosenetablissementCard;

    @FXML
    private Label etablissementNameLable;

    @FXML
    private Label etablissementCodeLabel;

    @FXML
    private Label label_no_data;
    @FXML
    private HBox Hbox_no_data;

    @FXML
    private ImageView etablissementImg;

    @FXML
    private ScrollPane scroll;

    @FXML
    private GridPane grid;

    @FXML
    private Label typeETF;
    @FXML
    private Label lieuETF;
    @FXML
    private Label cinETF;
    @FXML
    private AnchorPane grandAnchor;
    @FXML
    private StackPane StackPane;

    @FXML
    private AnchorPane CandidatPane;
    @FXML
    private AnchorPane RepresentantPane;
    @FXML
    private AnchorPane AdminPane;

    @FXML
    private AnchorPane anchorContenu;
    @FXML
    private Label nameAdminLabel;
    @FXML
    private Label nameRepLabel;
    @FXML
    private Label emailAdminLabel;
    @FXML
    private Label emailRepLabel;

    @FXML
    private Button afficherWalletListImg;


    private List<Etablissement> etablissements = new ArrayList<>();
    private Image image;
    private MyListener myListener;
    ServiceEtablissement se = new ServiceEtablissement();
    //-----------------------Wallet-------------------------------
    @FXML
    private Label balanceLabel;

    @FXML
    private Button ajouterWalletBtn;

    @FXML
    private Button acheterQuizzBtn;

    @FXML
    private TextField searchField;



    @FXML
    private ImageView imageRepresenter;






    private List<Etablissement> getData() throws SQLException {


        Set<Etablissement> etablissements = se.getAll();//admin
        if (CurrentUser.getRole() != 0) {

            if (CurrentUser.getCin() == 0) {
                System.out.println("CurrentUser Introuvable");
            }

            etablissements = se.getByCin(CurrentUser.getCin());//front
        }


        List<Etablissement> modifiedEtablissements = new ArrayList<>();

        for (Etablissement etablissement : etablissements) {
            // Assuming setNom, setPrice, and setImage methods are available in the Etablissement class
            Etablissement modifiedEtablissement = new Etablissement();
            modifiedEtablissement.setIdEtablissement(etablissement.getIdEtablissement());
            modifiedEtablissement.setNom(etablissement.getNom());
            modifiedEtablissement.setLieu(etablissement.getLieu());
            modifiedEtablissement.setCodeEtablissement(etablissement.getCodeEtablissement());
            modifiedEtablissement.setTypeEtablissement(etablissement.getTypeEtablissement());


            // If setPrice method is available, you can uncomment the following line
            // modifiedEtablissement.setPrice(etablissement.getPrice());
            modifiedEtablissement.setImage("/img/" + etablissement.getImage());

            modifiedEtablissement.setUser(etablissement.getUser());

            modifiedEtablissements.add(modifiedEtablissement);
        }

        return modifiedEtablissements;
    }


    private void setChosenetablissement(Etablissement etablissement) {


        etablissementNameLable.setText(etablissement.getNom());
        etablissementCodeLabel.setText(String.valueOf(etablissement.getCodeEtablissement()));

        image = new Image(getClass().getResourceAsStream(etablissement.getImage()));
        etablissementImg.setImage(image);
        lieuETF.setText(etablissement.getLieu());
        typeETF.setText(etablissement.getTypeEtablissement());
        cinETF.setText(String.valueOf(etablissement.getUser().getCin()));


        CurrentEtablissement.setIdEtablissement(etablissement.getIdEtablissement());


        Wallet wallet = se.getWalletByEtablissement(etablissement);

        if (wallet != null) {
            // Le portefeuille existe pour cet établissement


            if (wallet.getStatus() != 0) {
                int balance = wallet.getBalance();
                balanceLabel.setText(String.valueOf(balance) + "DT");
                acheterQuizzBtn.setVisible(true);
            } else {
                balanceLabel.setText("Non Actif");
                acheterQuizzBtn.setVisible(false);
            }


            CurrentWallet.setIdWallet(wallet.getIdWallet());

            // Afficher balanceLabel et masquer ajouterWalletBtn
            balanceLabel.setVisible(true);
            afficherWalletListImg.setVisible(true);
            ajouterWalletBtn.setVisible(false);


        } else {

            // Masquer balanceLabel et afficher ajouterWalletBtn
            balanceLabel.setVisible(false);
            afficherWalletListImg.setVisible(false);
            ajouterWalletBtn.setVisible(true);

            CurrentWallet.setIdWallet(0);
        }


        chosenetablissementCard.requestLayout(); // Force la mise à jour du layout


        etablissementCo = etablissement;//lkhedmet lgafsi
        walletCo = wallet;//lkhedmet lgafsi
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {


        grandAnchor.setPrefWidth(1451);  // Set your preferred width
        grandAnchor.setPrefHeight(830);

        /*----------------------------- Affichage Quel Navbar?----------------------- */
        int userRole = CurrentUser.getRole();
        switch (userRole) {
            case 0:
                AdminPane.setVisible(true);
                RepresentantPane.setVisible(false);
                CandidatPane.setVisible(false);
                nameAdminLabel.setText("Admin " + CurrentUser.getNom());
                emailAdminLabel.setText(CurrentUser.getAdresse());
                break;
            case 1:
                AdminPane.setVisible(false);
                RepresentantPane.setVisible(true);
                CandidatPane.setVisible(false);
                nameRepLabel.setText(CurrentUser.getNom());
                emailRepLabel.setText(CurrentUser.getAdresse());


                String imagePath = CurrentUser.getProfileImagePath();
                System.out.println(imagePath);
                String currentDir = System.getProperty("user.dir");
// imagePath = currentDir + "\\src\\main\\resources\\img" + imagePath; // Use double backslashes for path separators
                System.out.println(imagePath);

// Set the image file name to the TextField

// Display the image in the ImageView
                Image image = new Image("file:" + currentDir + "/src/main/resources/img/" + imagePath); // Use forward slashes for path separators
                imageRepresenter.setImage(image);





                break;
            case 2:
                AdminPane.setVisible(false);
                RepresentantPane.setVisible(false);
                CandidatPane.setVisible(true);
                // nameUserLabel.setText(CurrentUser.getNom());
                //emailUserLabel.setText(CurrentUser.getAdresse());
                break;

        }

        acheterQuizzBtn.setVisible(false);


        try {
            etablissements.addAll(getData());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        setupListeners();

        if (etablissements.isEmpty()) {

            Hbox_no_data.setVisible(true);
            chosenetablissementCard.setVisible(false); // Hide chosenetablissementCard


        } else {
            Hbox_no_data.setVisible(false);
            chosenetablissementCard.setVisible(true); // Show chosenetablissementCard
            setChosenetablissement(etablissements.get(0));


            populateGrid();


        }


// Add an event listener to the textProperty of the searchField
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if (newValue.isEmpty()) {
                    // If the search text is empty, reload the full list of establishments
                    etablissements.clear();
                    etablissements.addAll(getData());
                    populateGrid();
                } else {
                    // Filter the establishments based on the search text
                    List<Etablissement> filteredData = filterData(newValue);

                    // Update the UI with the filtered data
                    updateUI(filteredData);
                }
            } catch (SQLException e) {
                e.printStackTrace(); // Handle the exception appropriately
            }
        });


    }


    private void setupListeners() {
        myListener = new MyListener() {
            @Override
            public void onClickListener(Etablissement etablissement) {
                setChosenetablissement(etablissement);

                CurrentEtablissement.setIdEtablissement(etablissement.getIdEtablissement());
            }

            @Override
            public void onDeleteListener(Etablissement etablissement) {
                etablissements.remove(etablissement);
                rafraichirPage();

                if (etablissements.isEmpty()) {
                    // Display a message when there are no elements in the list
                    label_no_data.setText("Vous n'avez pas encore d'établissement.");
                    Hbox_no_data.setVisible(true);
                    // chosenetablissementCard.setVisible(false);// Show the HBox


                }
                populateGrid();
            }

        };
    }

    private void populateGrid() {
        grid.getChildren().clear();

        int column = 0;
        int row = 1;

        for (int i = 0; i < etablissements.size(); i++) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/ItemEtablissement.fxml"));
            AnchorPane anchorPane = null;
            try {
                anchorPane = fxmlLoader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            EtablissementItemController etablissementItemController = fxmlLoader.getController();
            etablissementItemController.setData(etablissements.get(i), myListener);

            if (column == 2) {
                column = 0;
                row++;
            }

            grid.add(anchorPane, column++, row); //(child, column, row)
            GridPane.setMargin(anchorPane, new Insets(10));

            // Adjust grid dimensions
            grid.setMinWidth(Region.USE_COMPUTED_SIZE);
            grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
            grid.setMaxWidth(Region.USE_PREF_SIZE);
            grid.setMinHeight(Region.USE_COMPUTED_SIZE);
            grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
            grid.setMaxHeight(Region.USE_PREF_SIZE);
        }
    }


    public void ajouterEtablissement(ActionEvent actionEvent) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterEtablissement.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) grid.getScene().getWindow(); // Utilisez la même fenêtre (Stage) actuelle
            stage.setScene(new Scene(root));
            stage.show();

            // Vous pouvez fermer la fenêtre actuelle si nécessaire
            // ((Node)(event.getSource())).getScene().getWindow().hide();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    //-------------------WALLETS------------------

    public void ajouterWallet(ActionEvent actionEvent) {

        try {

            Parent root = FXMLLoader.load(getClass().getResource("/AjouterWallet.fxml"));
            grid.getScene().setRoot(root);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Sorry");
            alert.setTitle("Error");
            alert.show();
        }


    }


    private void afficherAlerte(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.setTitle("Error");
        alert.show();
    }

    public void rafraichirPage() {
        try {
            etablissements.clear(); // Efface les données existantes
            etablissements.addAll(getData()); // Recharge les données

            if (etablissements.isEmpty()) {

                Hbox_no_data.setVisible(true);
                chosenetablissementCard.setVisible(false); // Hide chosenetablissementCard


            } else {
                Hbox_no_data.setVisible(false);
                chosenetablissementCard.setVisible(true); // Show chosenetablissementCard
                setChosenetablissement(etablissements.get(0));
                populateGrid();


            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void openAiHelper(ActionEvent actionEvent) {
    }

    public void afficherWalletList(ActionEvent actionEvent) {
        Etablissement etablissementConnecte = null;
        try {
            etablissementConnecte = se.getOneByID(CurrentEtablissement.getIdEtablissement());
        } catch (SQLException e) {
            e.printStackTrace();
            afficherAlerte("Erreur lors de la récupération de l'établissement connecté : " + e.getMessage());
        }

        // Vérifier d'abord si etablissementConnecte est null
        if (etablissementConnecte != null) {
            Wallet walletConnecte = null;
            try {
                walletConnecte = se.getWalletByEtablissement(etablissementConnecte);
            } catch (Throwable t) {
                t.printStackTrace();
                afficherAlerte("Erreur lors de la récupération du portefeuille : " + t.getMessage());
            }

            if (walletConnecte != null) {


                try {

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierWallet2.fxml"));
                    Parent root = loader.load();
                    ModifierWallet2Controller controller = loader.getController();
                    controller.initDataWallet(walletConnecte);

// Get the scene from the current grid
                    Scene currentScene = balanceLabel.getScene();

// Set your preferred width and height
                    double preferredWidth = 520;
                    double preferredHeight = 520;

                    currentScene.setRoot(root);
                    currentScene.getWindow().setWidth(preferredWidth);
                    currentScene.getWindow().setHeight(preferredHeight);
                } catch (IOException e) {
                    e.printStackTrace();
                    afficherAlerte("Erreur lors du chargement d'AfficherWallet.fxml : " + e.getMessage());
                }


            } else {
                // Afficher une alerte demandant d'ajouter un portefeuille
                afficherAlerte("Ajoutez un portefeuille à votre établissement.");
            }
        } else {
            // Si etablissementConnecte est null, afficher un message spécifique
            afficherAlerte("Ajoutez un portefeuille à votre établissement.");
        }
    }


    /***-------------------------Metier-----------------------**/
    /***-------------Recherche----------------**/
    private List<Etablissement> filterData(String searchText) throws SQLException {
        List<Etablissement> filteredEtablissements = new ArrayList<>();

        for (Etablissement etablissement : etablissements) {
            // Check if the name or other relevant fields contain the search text
            if (etablissement.getNom().toLowerCase().contains(searchText.toLowerCase())) {
                filteredEtablissements.add(etablissement);
            }
        }

        return filteredEtablissements;
    }

    // Add a method to update the UI with the filtered data
    private void updateUI(List<Etablissement> filteredData) {
        // Update the UI with the filtered data
        etablissements.clear();
        etablissements.addAll(filteredData);

        // Populate the grid with the updated data
        populateGrid();
    }

    /***-------------Tri----------------**/
    @FXML
    private void sortByName() {
        // Sort the etablissements by name
        etablissements.sort(Comparator.comparing(Etablissement::getNom));
        // Update the UI with the sorted data
        populateGrid();
    }

    /***-------------------------End Metier-----------------------**/


    public void acheterQuiz(ActionEvent actionEvent) {
        //Jaww Etablissement
        CurrentEtablissement.setIdEtablissement(etablissementCo.getIdEtablissement());
        CurrentEtablissement.setNom(etablissementCo.getNom());
        CurrentEtablissement.setCodeEtablissement(etablissementCo.getCodeEtablissement());
        CurrentEtablissement.setImage(etablissementCo.getImage());
       // CurrentEtablissement.setListeQuizzAchetes(etablissementCo.getListeQuizzAchetes());
        //Jaww Wallet
        CurrentWallet.setIdWallet(walletCo.getIdWallet());
        CurrentWallet.setBalance(walletCo.getBalance());


        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AfficherQuizDisponible.fxml"));
            chosenetablissementCard.getScene().setRoot(root);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Sorry");
            alert.setTitle("Error");
            alert.show();
        }
    }
//-------------------------------NAVBAR REDIRECTIONS---------------------------------------------------------------
//-------------------------------------ADMIN---------------------------------------------------------------

    public void listUsersNavBar(ActionEvent actionEvent) {
        try {

            Parent root = FXMLLoader.load(getClass().getResource("/ListUsers.fxml"));
            chosenetablissementCard.getScene().setRoot(root);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Sorry");
            alert.setTitle("Error");
            alert.show();
        }

    }




    public void downloadFileHandler(ActionEvent event) {
        //String nbShares = String.valueOf(selectedReclamation.getPub().getNbShares());
        String NomEtab = etablissementCo.getNom();
        int CodeEtab = etablissementCo.getCodeEtablissement();
        String Location = etablissementCo.getLieu();
        String type = etablissementCo.getTypeEtablissement();

        //String nbReactions = String.valueOf(selectedReclamation.getPub().getTotalReactions());
        //String nbComments = String.valueOf(selectedReclamation.getPub().getNbShares());
        //String date = String.valueOf(selectedReclamation.getPub().getDate());

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

            /*contentStream.beginText();
            contentStream.newLineAtOffset(100, 660);
            contentStream.showText("Reactions: " + nbReactions);
            contentStream.endText();*/

            contentStream.beginText();
            contentStream.newLineAtOffset(100, 640);
            contentStream.showText("Comments: " + NomEtab);
            contentStream.endText();

            /*contentStream.beginText();
            contentStream.newLineAtOffset(100, 620);
            contentStream.showText("Date: " + date);
            contentStream.endText();*/

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

    public void camera(ActionEvent actionEvent) {



    }




    }

