package edu.esprit.entities;

import edu.esprit.services.ServiceUtilisateur;

public class CurrentUser {
    private static int id_utilisateur;
    private static  int cin;
    private static String nom;
    private static String prenom;
    private static String adresse;
    private static String mdp;
    private static int role;
    private static String ProfileImagePath;
    private static int otp;
    private static String captcha;

    public static String getCaptcha() {
        return captcha;
    }

    public static void setCaptcha(String captcha) {
        CurrentUser.captcha = captcha;
    }

    public static int getOtp()
    {
        return otp;
    }
    public static void setOtp(int otp)
    { CurrentUser.otp=otp;
    }
    public
    static ServiceUtilisateur su = new ServiceUtilisateur();

    public static int getRole() {
        return role;
    }

    public static String getProfileImagePath() {
        return CurrentUser.ProfileImagePath;
    }

    public static void setProfileImagePath(String profileImagePath) {
        ProfileImagePath = profileImagePath;
    }

    public static void setRole(int role) {
        CurrentUser.role = role;
    }

    public CurrentUser() {
    }

    public static int getId_utilisateur() {
        return id_utilisateur;
    }

    public static void setId_utilisateur(int id_utilisateur) {
        CurrentUser.id_utilisateur = id_utilisateur;
    }

    public static int getCin() {
        return cin;
    }

    public static void setCin(int cin) {
        CurrentUser.cin = cin;
    }

    public static String getNom() {
        return nom;
    }

    public static void setNom(String nom) {
        CurrentUser.nom = nom;
    }

    public static String getPrenom() {
        return prenom;
    }

    public static void setPrenom(String prenom) {
        CurrentUser.prenom = prenom;
    }

    public static String getAdresse() {
        return adresse;
    }

    public static void setAdresse(String adresse) {
        CurrentUser.adresse = adresse;
    }

    public static String getMdp() {
        return mdp;
    }

    public static void setMdp(String mdp) {
        CurrentUser.mdp = mdp;
    }


}
