package edu.esprit.entities;

import java.util.List;

public class CurrentEtablissement {

    private static int idEtablissement;
    private static String nom;
    private static String lieu;
    private static int codeEtablissement;
    private static String typeEtablissement;
    private static String image;
    private static List<Quiz> listeQuizzAchetes;
    private static Utilisateur user;

    private CurrentEtablissement() {
        // La classe statique ne peut pas être instanciée, donc le constructeur est privé
    }

    // Ajoutez des méthodes statiques pour accéder et modifier les champs statiques
    public static void init(int id, String nom, String lieu, int code, String type, String img, List<Quiz> quizzes, Utilisateur utilisateur) {
        idEtablissement = id;
        CurrentEtablissement.nom = nom;
        lieu = lieu;
        codeEtablissement = code;
        typeEtablissement = type;
        image = img;
       // listeQuizzAchetes = (quizzes != null) ? List.copyOf(quizzes) : List.of();
        user = utilisateur;
    }

    public static int getIdEtablissement() {
        return idEtablissement;
    }

    public static void setIdEtablissement(int idEtablissement) {
        CurrentEtablissement.idEtablissement = idEtablissement;
    }

    public static String getNom() {
        return nom;
    }

    public static void setNom(String nom) {
        CurrentEtablissement.nom = nom;
    }

    public static String getLieu() {
        return lieu;
    }

    public static void setLieu(String lieu) {
        CurrentEtablissement.lieu = lieu;
    }

    public static int getCodeEtablissement() {
        return codeEtablissement;
    }

    public static void setCodeEtablissement(int codeEtablissement) {
        CurrentEtablissement.codeEtablissement = codeEtablissement;
    }

    public static String getTypeEtablissement() {
        return typeEtablissement;
    }

    public static void setTypeEtablissement(String typeEtablissement) {
        CurrentEtablissement.typeEtablissement = typeEtablissement;
    }

    public static String getImage() {
        return image;
    }

    public static void setImage(String image) {
        CurrentEtablissement.image = image;
    }





    public static Utilisateur getUser() {
        return user;
    }

    public static void setUser(Utilisateur user) {
        CurrentEtablissement.user = user;
    }
}
