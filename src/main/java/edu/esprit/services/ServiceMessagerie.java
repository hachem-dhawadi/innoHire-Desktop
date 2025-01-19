package edu.esprit.services;

import edu.esprit.entities.Messagerie;
import edu.esprit.entities.Utilisateur;
import edu.esprit.utils.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class ServiceMessagerie implements IService<Messagerie> {
    private Connection cnx = DataSource.getInstance().getCnx();
    @Override
    public void ajouter(Messagerie mess) throws SQLException{
        String req = "INSERT INTO `messagerie`(`date`, `type`, `contenu`, `sender_id`, `reciver_id`) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setTimestamp(1, new java.sql.Timestamp(mess.getDate().getTime()));
            ps.setString(2, mess.getType());
            ps.setString(3, mess.getContenu());
            ps.setInt(4, mess.getSenderId().getId_utilisateur());
            ps.setInt(5, mess.getReciverId().getId_utilisateur());
            ps.executeUpdate();
            System.out.println("Messaagerie added!");
        } catch (SQLException e) {
            System.err.println("Error adding Messaagerie: " + e.getMessage());
        }
    }

    @Override
    public void modifier(Messagerie messagerie) {
        String req = "UPDATE `messagerie` SET `date`=?, `type`=?, `contenu`=?, `sender_id`=?, `reciver_id`=? WHERE `id_message`=?";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setTimestamp(1, new java.sql.Timestamp(messagerie.getDate().getTime()));
            ps.setString(2, messagerie.getType());
            ps.setString(3, messagerie.getContenu());
            ps.setInt(4, messagerie.getSenderId().getId_utilisateur());
            ps.setInt(5, messagerie.getReciverId().getId_utilisateur());
            ps.setInt(6, messagerie.getIdMessage());
            ps.executeUpdate();
            System.out.println("Messagerie updated!");
        } catch (SQLException e) {
            System.err.println("Error updating Messagerie: " + e.getMessage());
        }
    }

    @Override
    public void supprimer(int id) {
        String req = "DELETE FROM `messagerie` WHERE `id_message`=?";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("Messagerie deleted!");
        } catch (SQLException e) {
            System.err.println("Error deleting Messagerie: " + e.getMessage());
        }
    }

    @Override
    public Set<Messagerie> getAll() {
        Set<Messagerie> messages = new HashSet<>();
        String req = "SELECT * FROM `messagerie`";
        try (PreparedStatement ps = cnx.prepareStatement(req);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Messagerie mess = new Messagerie();
                mess.setIdMessage(rs.getInt("id_message"));
                mess.setType(rs.getString("type"));
                mess.setContenu(rs.getString("contenu"));
                mess.setDate(rs.getTimestamp("date"));

                // Utilisez ServiceUtilisateur pour obtenir l'Utilisateur par ID
                Utilisateur sender = new ServiceUtilisateur().getOneByID(rs.getInt("sender_id"));
                Utilisateur receiver = new ServiceUtilisateur().getOneByID(rs.getInt("reciver_id"));
                mess.setSenderId(sender);
                mess.setReciverId(receiver);

                messages.add(mess);
            }
        } catch (SQLException e) {
            System.err.println("Error getting Messagerie: " + e.getMessage());
        }
        return messages;
    }

    @Override
    public Messagerie getOneByID(int id) {
        String req = "SELECT * FROM `messagerie` WHERE `id_message` = ?";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Messagerie mess = new Messagerie();
                    mess.setIdMessage(rs.getInt("id_message"));
                    mess.setType(rs.getString("type"));
                    mess.setContenu(rs.getString("contenu"));
                    mess.setDate(rs.getTimestamp("date"));

                    // Utilisez ServiceUtilisateur pour obtenir l'Utilisateur par ID
                    Utilisateur sender = new ServiceUtilisateur().getOneByID(rs.getInt("sender_id"));
                    Utilisateur receiver = new ServiceUtilisateur().getOneByID(rs.getInt("reciver_id"));
                    mess.setSenderId(sender);
                    mess.setReciverId(receiver);

                    return mess;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving Messagerie: " + e.getMessage());
        }
        return null;
    }
    public Set<Messagerie> getAllMessagesByReciverAndSender(int senderId, int receiverId) {
        Set<Messagerie> messages = new HashSet<>();
        String req = "SELECT * FROM `messagerie` WHERE (sender_id = ? AND reciver_id = ?) OR (sender_id = ? AND reciver_id = ?) ORDER BY date";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setInt(1, senderId);
            ps.setInt(2, receiverId);
            ps.setInt(3, receiverId);
            ps.setInt(4, senderId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Messagerie mess = new Messagerie();
                    mess.setIdMessage(rs.getInt("id_message"));
                    mess.setType(rs.getString("type"));
                    mess.setContenu(rs.getString("contenu"));
                    mess.setDate(rs.getTimestamp("date"));

                    Utilisateur sender = new ServiceUtilisateur().getOneByID(rs.getInt("sender_id"));
                    Utilisateur receiver = new ServiceUtilisateur().getOneByID(rs.getInt("reciver_id"));
                    mess.setSenderId(sender);
                    mess.setReciverId(receiver);

                    messages.add(mess);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting Messagerie: " + e.getMessage());
        }
        return messages;
    }





}
