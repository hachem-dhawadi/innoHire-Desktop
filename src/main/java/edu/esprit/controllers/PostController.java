package edu.esprit.controllers;

import edu.esprit.entities.*;
import edu.esprit.services.ServiceCommentaire;
import edu.esprit.services.ServicePost;
import edu.esprit.services.ServiceUtilisateur;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PostController implements Initializable {
    @FXML
    private ImageView imgProfile;

    @FXML
    private Label usernom;
    @FXML
    private Button TFfavori;



    @FXML
    private Label dateETF;

    @FXML
    private ImageView audience;

    @FXML
    private Label caption;

    @FXML
    private ImageView imgPost;

    @FXML
    private Label nbReactions;

    @FXML
    private Label nbComments;


    @FXML
    private HBox reactionsContainer;

    @FXML
    private ImageView imgLike;

    @FXML
    private ImageView imgLove;

    @FXML
    private ImageView imgCare;

    @FXML
    private ImageView imgHaha;

    @FXML
    private ImageView imgWow;

    @FXML
    private ImageView imgSad;

    @FXML
    private ImageView imgAngry;

    @FXML
    private HBox likeContainer;

    @FXML
    private ImageView imgReaction;

    @FXML
    private Label reactionnom;

    @FXML
    private Button Modifier;
    @FXML
    private Button supprimer;

    @FXML
    private Button AjouterC;


    private long startTime = 0;

    private Reactions currentReaction;
    private Post post;



    private ScheduledExecutorService scheduler;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Initialisez le planificateur pour mettre à jour la date toutes les 1 minute
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(this::updateRelativeDate, 0, 1, TimeUnit.MINUTES);


    }
    private void updateRelativeDate() {
        Platform.runLater(() -> {
            String relativeDate = formatRelativeDate(post.getDate());
            dateETF.setText(relativeDate);
        });
    }

    @FXML
    public void onLikeContainerPressed(MouseEvent me) {
        startTime = System.currentTimeMillis();
    }

    @FXML
    public void onLikeContainerMouseReleased(MouseEvent me){
        if(System.currentTimeMillis() - startTime > 500){
            reactionsContainer.setVisible(true);
        }else {
            if(reactionsContainer.isVisible()){
                reactionsContainer.setVisible(false);
            }
            if(currentReaction == Reactions.NON){
                setReaction(Reactions.LIKE);
            }else{
                setReaction(Reactions.NON);
            }
        }
    }



    @FXML
    public void onReactionImgPressed(MouseEvent me) {
        switch (((ImageView) me.getSource()).getId()) {
            case "imgLove":
                setReaction(Reactions.LOVE);
                break;
            case "imgCare":
                setReaction(Reactions.CARE);
                break;
            case "imgHaha":
                setReaction(Reactions.HAHA);
                break;
            case "imgWow":
                setReaction(Reactions.WOW);
                break;
            case "imgSad":
                setReaction(Reactions.SAD);
                break;
            case "imgAngry":
                setReaction(Reactions.ANGRY);
                break;
            default:
                setReaction(Reactions.LIKE);
                break;
        }
        reactionsContainer.setVisible(false);
    }

    public void setReaction(Reactions reaction) {
        ServiceCommentaire sc = new ServiceCommentaire();



            Image image = new Image(getClass().getResourceAsStream(reaction.getImgSrc()));
            imgReaction.setImage(image);
            reactionnom.setText(reaction.getNom());
            reactionnom.setTextFill(Color.web(reaction.getColor()));

            if (currentReaction == Reactions.NON) {
                post.setTotalReactions(post.getTotalReactions() + 1);


            }

            currentReaction = reaction;

            if (currentReaction == Reactions.NON) {
                post.setTotalReactions(post.getTotalReactions() - 1);
            }

            nbReactions.setText(String.valueOf(post.getTotalReactions()));


            this.post = post;
            ServicePost sp = new ServicePost();
            int nbRactions = post.getTotalReactions();
            post.setTotalReactions(nbRactions);
            try {
                sp.modifier(post);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }


            ServiceUtilisateur su = new ServiceUtilisateur();
            Utilisateur cu = su.get_One_ByCin(CurrentUser.getCin());
            Commentaire c1 = new Commentaire(post, cu, "", LocalDate.of(2023, 02, 4));
            try {
                sc.ajouter(c1);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }


    }

    public void setData(Post post) {
        this.post = post;

        // Set profile image
        if (post.getUtilisateur() != null && post.getUtilisateur().getImage() != null) {
            String imageName = post.getUtilisateur().getImage();
            String profileImgPath = "/img/" + imageName;
            if (getClass().getResource(profileImgPath) != null) { // Vérifie si le chemin d'accès est valide
                Image img = new Image(getClass().getResourceAsStream(profileImgPath));
                this.imgProfile.setImage(img);
            } else {
                System.err.println("Chemin d'accès à l'image de profil invalide : " + profileImgPath);
            }
        }


        if (post.getUtilisateur() != null) {
            usernom.setText(post.getUtilisateur().getNom());

        }

        String relativeDate = formatRelativeDate(post.getDate());
        dateETF.setText(relativeDate);

        // Set audience image
        Image img;
        if (post.getAudience() == PostAudience.PUBLIC) {
            img = new Image(getClass().getResourceAsStream(PostAudience.PUBLIC.getImgSrc()));
        } else {
            img = new Image(getClass().getResourceAsStream(PostAudience.FRIENDS.getImgSrc()));
        }
        audience.setImage(img);

        // Set caption
        if (post.getCaption() != null && !post.getCaption().isEmpty()) {
            caption.setText(post.getCaption());
        } else {
            caption.setManaged(false);
        }

        // Set post image
        // Set post image
        if (post.getImage() != null && !post.getImage().isEmpty()) {
            String imagePath = "/img/" + post.getImage(); // Concatenate "/img/" to the image name
            Image img2 = new Image(getClass().getResourceAsStream(imagePath));
            this.imgPost.setImage(img2);
        } else {
            imgPost.setVisible(false);
            imgPost.setManaged(false);
        }


        // Set reactions count
        nbReactions.setText(String.valueOf(post.getTotalReactions()));

        // Set comments count
        nbComments.setText(post.getNbComments() + " comments");



        // Reset current reaction
        currentReaction = Reactions.NON;



    }


    public static String formatRelativeDate(LocalDateTime postDate) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(postDate, now);

        long seconds = duration.getSeconds();
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        long weeks = days / 7;

        if (seconds < 60) {
            return "Just Now";
        } else if (minutes < 60) {
            return "publié il y a " + minutes + " m";
        } else if (hours < 24) {
            return "publié il y a " + hours + " h";
        } else if (days < 7) {
            return "publié il y a " + days + " d";
        } else {
            return "plus 1 week";
        }
    }

    private Post getPost() throws SQLException {

        ServicePost sp = new ServicePost();

        ServiceUtilisateur su = new ServiceUtilisateur();



        Post post1 = sp.getOneByID(1);

        Post post = new Post();
        Utilisateur user = new Utilisateur();
        user.setNom(post1.getUtilisateur().getNom());
        user.setImage(post1.getUtilisateur().getImage());

        post.setUtilisateur(user);
        post.setDate(post1.getDate());
        post.setAudience(post1.getAudience());
        post.setCaption(post1.getCaption());
        post.setImage(post1.getImage());
        post.setTotalReactions(post1.getTotalReactions());
        post.setNbComments(post1.getNbComments());

        return post;
    }



    @FXML
    public void navigatetoModifierPublicationAction(ActionEvent actionEvent) {
        // Code to modify the selected post in the list
        Post selectedPost = post;

        // Check if an item is selected
        if (selectedPost == null) {
            // No item selected, show a warning alert
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Avertissement");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner une publication à modifier.");
            alert.showAndWait();
            return; // Exit the method as there's nothing to modify
        } else {
            // Check if the current user has the right to modify posts

            if (CurrentUser.getRole() == 0 || selectedPost.getUtilisateur().getCin() == CurrentUser.getCin()) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierPublication.fxml"));
                    Parent root = loader.load();
                    ModifierPublication controller = loader.getController();
                    controller.initData(selectedPost); // Pass the selected post to the modification interface controller

                    // Get the current scene
                    Scene scene = ((Node) actionEvent.getSource()).getScene();

                    // Change the content of the scene
                    scene.setRoot(root);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                // Display a message indicating that the user doesn't have the right to modify this post
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Erreur");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("Vous n'avez pas le droit de modifier cette publication.");
                errorAlert.showAndWait();
            }
        }
    }


    public void NaviguerversPub(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Pub.fxml"));
            caption.getScene().setRoot(root);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Sorry jjjj");
            alert.setTitle("Error");
            alert.show();
        }

    }


    public void supprimer(ActionEvent actionEvent) {
        // If an item is selected, show the deletion confirmation
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText(null);
        alert.setContentText("Êtes-vous sûr de vouloir supprimer cette conversation ?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Check if the current user has the right to delete posts
            if (CurrentUser.getRole() == 0 || (CurrentUser.getRole() != 0 && post.getUtilisateur().getCin() == CurrentUser.getCin())) {
                try {
                    int idPost = post.getId_post();
                    ServicePost servicePost = new ServicePost();
                    servicePost.supprimer(idPost);
                    NaviguerversPub(actionEvent);

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                // Display a message indicating that the user can only delete their own posts
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Erreur");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("Vous ne pouvez supprimer que vos propres publications.");
                errorAlert.showAndWait();
            }
        }
    }


    @FXML
    void NavigatetoC(MouseEvent event) {
        try {
            CurrentPost.setId_post(post.getId_post());
            Parent root = FXMLLoader.load(getClass().getResource("/AfficherCommentaire2.fxml"));
            caption.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Sorry");
            alert.setTitle("Error");
            alert.show();
        }
    }



    public void NavigateToClaim(MouseEvent mouseEvent) {
        try {
            CurrentPost.setId_post(post.getId_post());
            Parent root = FXMLLoader.load(getClass().getResource("/AjouterReclamation.fxml"));
            caption.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Sorry");
            alert.setTitle("Error");
            alert.show();
        }

    }

    public void MakeClaim(ActionEvent event) {
        try {
            Post selectedPost = post;
            // Load the AjouterReclamation.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterReclamation.fxml"));
            Parent root = loader.load();

            // Access the controller
            AjouterReclamationController ajouterReclamationController = loader.getController();

            // Set the data in the controller
            ajouterReclamationController.initData(selectedPost);

            // Create a new dialog
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Ajouter Réclamation");
            dialog.getDialogPane().setContent(root);

            // Add a button to the dialog
            ButtonType closeButton = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().add(closeButton);

            // Show the dialog
            dialog.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            // Handle IO exceptions
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Error loading FXML file");
            alert.setTitle("Error");
            alert.show();
        } catch (Exception e) {
            e.printStackTrace();
            // Handle other exceptions
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Unexpected error");
            alert.setTitle("Error");
            alert.show();
        }
        downloadFileHandler();
    }

    @FXML
    private void downloadFileHandler() {
        Post selectedPost = post;
            String caption = selectedPost.getCaption();
            String imagePost = selectedPost.getImage();
        System.out.println(caption);
        System.out.println(imagePost);

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
            contentStream.newLineAtOffset(60, 720);
            contentStream.showText("-- Post Details --");
            contentStream.endText();

            /*contentStream.beginText();
            contentStream.newLineAtOffset(100, 680);
            contentStream.showText("Shares: " + nbShares);
            contentStream.endText();*/

            contentStream.beginText();
            contentStream.newLineAtOffset(200, 700); // Adjust the X and Y coordinates for the caption
            contentStream.showText("Caption: " + caption);
            contentStream.endText();

          /*  contentStream.beginText();
            contentStream.newLineAtOffset(100, 640);
            contentStream.showText("Comments: " + nbComments);
            contentStream.endText();*/

            /*contentStream.beginText();
            contentStream.newLineAtOffset(100, 620);
            contentStream.showText(": " + userName);
            contentStream.endText();*/

            // Add an image to the PDF
            //PDImageXObject pdImage = PDImageXObject.createFromFile("src/main/resources/img/folder.png", document);
            //contentStream.drawImage(pdImage, 50, 500, pdImage.getWidth(), pdImage.getHeight());
            // Add an image to the PDF
            PDImageXObject pdImage = PDImageXObject.createFromFile("src/main/resources/img/" + imagePost, document);
            float imageWidth = 250; // pdImage.getWidth();
            float imageHeight = 250; // pdImage.getHeight();
            float xImage = 150; // Adjust the X-coordinate of the image
            float yImage = 400; // Adjust the Y-coordinate of the image
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

}


