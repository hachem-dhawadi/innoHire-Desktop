package edu.esprit.entities;

public class CurrentPost {
    private static int id_post;
    private static Utilisateur utilisateur;
    private static PostAudience audience;
    private static String date;
    private static String caption;
    private static String image;
    private static int totalReactions;
    private static int nbComments;
    private static int nbShares;

    static {
        totalReactions = 0;
        nbComments = 0;
        nbShares = 0;
    }

    public static int getId_post() {
        return id_post;
    }

    public static void setId_post(int id_post) {
        CurrentPost.id_post = id_post;
    }

    public static Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public static void setUtilisateur(Utilisateur utilisateur) {
        CurrentPost.utilisateur = utilisateur;
    }

    public static PostAudience getAudience() {
        return audience;
    }

    public static void setAudience(PostAudience audience) {
       CurrentPost.audience = audience;
    }

    public static String getDate() {
        return date;
    }

    public static void setDate(String date) {
        CurrentPost.date = date;
    }

    public static String getCaption() {
        return caption;
    }

    public static void setCaption(String caption) {
        CurrentPost.caption = caption;
    }

    public static String getImage() {
        return image;
    }

    public static void setImage(String image) {
        CurrentPost.image = image;
    }

    public static int getTotalReactions() {
        return totalReactions;
    }

    public static void setTotalReactions(int totalReactions) {
        CurrentPost.totalReactions = totalReactions;
    }

    public static int getNbComments() {
        return nbComments;
    }

    public static void setNbComments(int nbComments) {
        CurrentPost.nbComments = nbComments;
    }

    public static int getNbShares() {
        return nbShares;
    }

    public static void setNbShares(int nbShares) {
        CurrentPost.nbShares = nbShares;
    }



}
