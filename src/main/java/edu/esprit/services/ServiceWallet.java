package edu.esprit.services;


import edu.esprit.entities.Etablissement;
import edu.esprit.entities.Utilisateur;
import edu.esprit.entities.Wallet;
import edu.esprit.utils.DataSource;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.sql.Timestamp;

public class ServiceWallet implements IService<Wallet> {
    Connection cnx = DataSource.getInstance().getCnx();

    @Override
    public void ajouter(Wallet wallet) throws SQLException {
   /*
String req = "INSERT INTO `wallet`(`nom`, `prenom`) VALUES ('"+personne.getNom()+"','"+personne.getPrenom()+"')";
        try {
            Statement st = cnx.createStatement();
            st.executeUpdate(req);
            System.out.println("Personne added !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        --> cest pas pratique car si on a plusieur attributs on doit faire concatenation(+) pour chacun
*/
        LocalDateTime currentDate = LocalDateTime.now();
        String req = "INSERT INTO `wallet`(`balance`,`date_c`,`status`, `id_etablissement`) VALUES (?,?,?,?)";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, wallet.getBalance());
            ps.setTimestamp(2, Timestamp.valueOf(currentDate));

            ps.setInt(3, wallet.getStatus());

            ps.setInt(4, wallet.getEtablissement().getIdEtablissement());

            ps.executeUpdate();
            System.out.println("Wallet added !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void modifier(Wallet wallet) throws SQLException {
        int id = wallet.getIdWallet();
        Wallet existingWallet = getOneByID(id);
        if (existingWallet != null) {
            String req = "UPDATE `wallet` SET `balance`=?,`status`=?, `id_etablissement`=? WHERE `id_wallet`=?";
            try {
                PreparedStatement ps = cnx.prepareStatement(req);
                ps.setInt(1, wallet.getBalance());

                ps.setInt(2, wallet.getStatus());

                ps.setInt(3, wallet.getEtablissement().getIdEtablissement());

                ps.setInt(4, id);

                ps.executeUpdate();
                System.out.println("Wallet updated !");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("introuvable : Échec de mise à jour ");
        }


    }

    @Override
    public void supprimer(int idWallet) throws SQLException {

        Wallet wallet = getOneByID(idWallet);
        if (wallet != null) {
            String req = "DELETE FROM `wallet` WHERE `id_wallet`=?";
            try {
                PreparedStatement ps = cnx.prepareStatement(req);
                ps.setInt(1, idWallet);
                ps.executeUpdate();
                System.out.println("Wallet deleted !");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("introuvable : Échec de suppression ");
        }

    }



// ...

    @Override
    public Set<Wallet> getAll() {
        Set<Wallet> wallets = new HashSet<>();

        String req = "SELECT * FROM wallet";
        try {
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(req);
            while(rs.next()) {
                int idWallet = rs.getInt("id_wallet");
                int balance = rs.getInt("balance");

                // Utilisation de Timestamp pour récupérer les dates de type datetime
                Timestamp timestamp = rs.getTimestamp("date_c");
                LocalDateTime dateCreation = timestamp.toLocalDateTime();

                int status = rs.getInt("status");
                int idEtablissement = rs.getInt("id_etablissement");

                ServiceEtablissement se = new ServiceEtablissement();
                Etablissement etablissement = se.getOneByID(idEtablissement);

                Wallet e = new Wallet(idWallet, balance, dateCreation, status, etablissement);
                wallets.add(e);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return wallets;
    }


    @Override
    public Wallet getOneByID(int idWallet) {
        String req = "SELECT * FROM wallet WHERE id_wallet = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, idWallet);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int balance = rs.getInt("balance");

                // Utilisation de Timestamp pour récupérer les dates de type datetime
                Timestamp timestamp = rs.getTimestamp("date_c");
                LocalDateTime dateCreation = timestamp.toLocalDateTime();

                int status = rs.getInt("status");
                int id_etablissement = rs.getInt("id_etablissement");

                ServiceEtablissement se = new ServiceEtablissement();
                Etablissement etablissement = se.getOneByID(id_etablissement);

                return new Wallet(idWallet, balance, dateCreation, status, etablissement);
            } else {
                System.out.print("Echec! Wallet with ID " + idWallet + " is not found.");
                return null;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }


    // Fonction pour obtenir l'id_etablissement à partir du code_etablissement


    public boolean portefeuilleExistePourEtablissement(int codeEtablissement) throws SQLException {
        ServiceEtablissement se=new ServiceEtablissement();
        int idEtablissement = se.getIdEtablissement(codeEtablissement);

        // Vérifier si on a pu obtenir l'id_etablissement
        if (idEtablissement == -1) {
            return false; // Aucun établissement trouvé
        }

        // Requête pour vérifier si un portefeuille existe pour cet id_etablissement
        String reqPortefeuille = "SELECT * FROM wallet WHERE id_etablissement = ?";
        try {
            PreparedStatement psPortefeuille = cnx.prepareStatement(reqPortefeuille);
            psPortefeuille.setInt(1, idEtablissement);
            ResultSet rsPortefeuille = psPortefeuille.executeQuery();

            return rsPortefeuille.next(); // Retourne true si un portefeuille pour l'établissement existe déjà
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }





}
