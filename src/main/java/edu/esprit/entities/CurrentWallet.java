package edu.esprit.entities;

import java.time.LocalDateTime;

public class CurrentWallet {

    private static int idWallet ;
    private static int balance;
    private static LocalDateTime dateCreation;
    private static int status;
    private static Etablissement etablissement;

    // Make sure to set these values before accessing them
    public static void setIdWallet(int id) {
        idWallet = id;
    }

    public static void setBalance(int bal) {
        balance = bal;
    }

    public static void setDateCreation(LocalDateTime date) {
        dateCreation = date;
    }

    public static void setStatus(int stat) {
        status = stat;
    }

    public static void setEtablissement(Etablissement etab) {
        etablissement = etab;
    }

    // Getters...

    public static int getIdWallet() {
        return idWallet;
    }

    public static int getBalance() {
        return balance;
    }

    public static LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public static int getStatus() {
        return status;
    }

    public static Etablissement getEtablissement() {
        return etablissement;
    }


}

