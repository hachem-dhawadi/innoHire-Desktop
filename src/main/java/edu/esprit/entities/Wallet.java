package edu.esprit.entities;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import java.time.LocalDateTime;
import java.util.Objects;

public class Wallet {

    private int idWallet;
    private int balance;
    private LocalDateTime dateCreation;
    private int status;
    private Etablissement etablissement;

    public int getIdWallet() {
        return idWallet;
    }

    public void setIdWallet(int idWallet) {
        this.idWallet = idWallet;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Etablissement getEtablissement() {
        return etablissement;
    }

    public void setEtablissement(Etablissement etablissement) {
        this.etablissement = etablissement;
    }




    public Wallet() {
    }

    public Wallet(int idWallet, int balance,LocalDateTime dateCreation, int status, Etablissement etablissement) {
        this.idWallet = idWallet;
        this.balance = balance;
        this.dateCreation = dateCreation;
        this.status = status;
        this.etablissement = etablissement;
    }
    public Wallet(int idWallet, int balance, int status, Etablissement etablissement) {
        this.idWallet = idWallet;
        this.balance = balance;
        //pas besoin de date parceque il est inchangable
        this.status = status;
        this.etablissement = etablissement;
    }

    public Wallet(int balance, LocalDateTime dateCreation, int status, Etablissement etablissement) {
        this.balance = balance;
        this.dateCreation = dateCreation;
        this.status = status;
        this.etablissement = etablissement;
    }
    public Wallet(int balance,int status, Etablissement etablissement) {
        this.balance = balance;
        this.status = status;
        this.etablissement = etablissement;
    }


    // Getters and setters...

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Wallet wallet = (Wallet) o;
        return idWallet == wallet.idWallet;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idWallet);
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = dateCreation.format(formatter);
        return "Wallet{" +

                "balance=" + balance +
                ", dateCreation=" + formattedDate +
                ", status=" + status +
                ", code etablissement=" + etablissement.getCodeEtablissement() +
                '}';
    }
}
