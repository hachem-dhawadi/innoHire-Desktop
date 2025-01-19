package edu.esprit.services;

import edu.esprit.entities.CurrentUser;
import edu.esprit.entities.Post;
import edu.esprit.entities.Reclamation;
import edu.esprit.entities.Utilisateur;
import edu.esprit.utils.DataSource;

import java.sql.*;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class ServiceReclamation implements IService<Reclamation>{
    private Connection cnx = DataSource.getInstance().getCnx();

    @Override
    public void ajouter(Reclamation rec) throws SQLException {
        //try (PreparedStatement ps = cnx.prepareStatement(req)) {
        String req = "INSERT INTO `reclamation`(`type`, `titre`, `description`, `date`, `status`, `id_post`, `id_utilisateur`) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = cnx.prepareStatement(req);
            ps.setString(1, rec.getType());
            ps.setString(2, rec.getTitre());
            ps.setString(3, rec.getDescription());
            ps.setTimestamp(4, new java.sql.Timestamp(rec.getDate().getTime()));
            ps.setInt(5,rec.getStatus());
            ps.setInt(6, rec.getPub().getId_post());
            ps.setInt(7, rec.getUser().getId_utilisateur());
            ps.executeUpdate();
            System.out.println("Reclamation added!");
       /* } catch (SQLException e) {
            System.err.println("Error adding Reclamation: " + e.getMessage());
        }*/
    }

    @Override
    public void modifier(Reclamation reclamation) throws SQLException{
        String req = "UPDATE `reclamation` SET `type`=?, `titre`=?, `description`=?, `date`=?, `status`=?, `id_post`=?, `id_utilisateur`=? WHERE `id_reclamation`=?";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setString(1, reclamation.getType());
            ps.setString(2, reclamation.getTitre());
            ps.setString(3, reclamation.getDescription());
            ps.setTimestamp(4, new java.sql.Timestamp(reclamation.getDate().getTime()));
            ps.setInt(5, reclamation.getStatus());
            ps.setInt(6, reclamation.getPub().getId_post());
            ps.setInt(7, reclamation.getUser().getId_utilisateur());
            ps.setInt(8, reclamation.getIdReclamation());
            ps.executeUpdate();
            System.out.println("Reclamation updated!");
        } catch (SQLException e) {
            System.err.println("Error updating Reclamation: " + e.getMessage());
        }
    }

    @Override
    public void supprimer(int id) throws SQLException{
        String req = "DELETE FROM `reclamation` WHERE `id_reclamation`=?";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("Reclamation deleted!");
        } catch (SQLException e) {
            System.err.println("Error deleting Reclamation: " + e.getMessage());
        }
    }

    @Override
    public Set<Reclamation> getAll() throws SQLException{
        Set<Reclamation> reclamations = new HashSet<>();
        String req = "SELECT * FROM `reclamation`";
        try (PreparedStatement ps = cnx.prepareStatement(req);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Reclamation rec = new Reclamation();
                rec.setIdReclamation(rs.getInt("id_reclamation"));
                rec.setStatus(rs.getInt("status"));
                rec.setType(rs.getString("type"));
                rec.setTitre(rs.getString("titre"));
                rec.setDescription(rs.getString("description"));
                rec.setDate(rs.getTimestamp("date"));

                // Use ServicePublication to get Publication by ID
                Post post = new ServicePost().getOneByID(rs.getInt("id_post"));
                rec.setPub(post);

                // Use ServiceUtilisateur to get Utilisateur by ID
                Utilisateur utilisateur = new ServiceUtilisateur().getOneByID(rs.getInt("id_utilisateur"));
                rec.setUser(utilisateur);

                reclamations.add(rec);
            }
        } catch (SQLException e) {
            System.err.println("Error getting Reclamations: " + e.getMessage());
        }
        return reclamations;
    }



    @Override
    public Reclamation getOneByID(int id) {
        String req = "SELECT * FROM `reclamation` WHERE `id_reclamation` = ?";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Reclamation rec = new Reclamation();
                    rec.setIdReclamation(rs.getInt("id_reclamation"));
                    rec.setStatus(rs.getInt("status"));
                    rec.setType(rs.getString("type"));
                    rec.setTitre(rs.getString("titre"));
                    rec.setDescription(rs.getString("description"));
                    rec.setDate(rs.getTimestamp("date"));

                    // Use ServicePublication to get Publication by ID
                    Post post = new ServicePost().getOneByID(rs.getInt("id_post"));
                    rec.setPub(post);

                    // Use ServiceUtilisateur to get Utilisateur by ID
                    Utilisateur utilisateur = new ServiceUtilisateur().getOneByID(rs.getInt("id_utilisateur"));
                    rec.setUser(utilisateur);

                    return rec;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving reclamation: " + e.getMessage());
        }
        return null;
    }

    public Set<Reclamation> getAllRecByUser(int userId) throws SQLException{
        Set<Reclamation> reclamations = new HashSet<>();
        String req = "SELECT * FROM `reclamation` WHERE id_utilisateur = ?";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Reclamation rec = new Reclamation();
                    rec.setIdReclamation(rs.getInt("id_reclamation"));
                    rec.setStatus(rs.getInt("status"));
                    rec.setType(rs.getString("type"));
                    rec.setTitre(rs.getString("titre"));
                    rec.setDescription(rs.getString("description"));
                    rec.setDate(rs.getTimestamp("date"));

                    // Use ServicePublication to get Publication by ID
                    Post post = new ServicePost().getOneByID(rs.getInt("id_post"));
                    rec.setPub(post);

                    // Use ServiceUtilisateur to get Utilisateur by ID
                    Utilisateur utilisateur = new ServiceUtilisateur().getOneByID(rs.getInt("id_utilisateur"));
                    rec.setUser(utilisateur);

                    reclamations.add(rec);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting Reclamations: " + e.getMessage());
        }
        return reclamations;
    }

    public Set<Reclamation> getAllOrderByDateAndTime() throws SQLException {
        Set<Reclamation> reclamations = new TreeSet<>((rec1, rec2) -> rec2.getDate().compareTo(rec1.getDate()));

        String req = "SELECT * FROM `reclamation` ORDER BY `date` DESC"; // Order by date in descending order

        try (PreparedStatement ps = cnx.prepareStatement(req);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Reclamation rec = new Reclamation();
                rec.setIdReclamation(rs.getInt("id_reclamation"));
                rec.setStatus(rs.getInt("status"));
                rec.setType(rs.getString("type"));
                rec.setTitre(rs.getString("titre"));
                rec.setDescription(rs.getString("description"));
                rec.setDate(rs.getTimestamp("date"));

                // Use ServicePublication to get Publication by ID
                Post post = new ServicePost().getOneByID(rs.getInt("id_post"));
                rec.setPub(post);

                // Use ServiceUtilisateur to get Utilisateur by ID
                Utilisateur utilisateur = new ServiceUtilisateur().getOneByID(rs.getInt("id_utilisateur"));
                rec.setUser(utilisateur);

                reclamations.add(rec);
            }
        } catch (SQLException e) {
            System.err.println("Error getting Reclamations: " + e.getMessage());
        }
        return reclamations;
    }




    public Set<Reclamation> getAllByStatusOrderByDate(int status) throws SQLException {
        Set<Reclamation> reclamations = new HashSet<>();
        String req = "SELECT * FROM `reclamation` WHERE `status` = ? ORDER BY `date` DESC"; // Order by date in descending order
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setInt(1, status); // Set the status parameter
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Reclamation rec = new Reclamation();
                    rec.setIdReclamation(rs.getInt("id_reclamation"));
                    rec.setStatus(rs.getInt("status"));
                    rec.setType(rs.getString("type"));
                    rec.setTitre(rs.getString("titre"));
                    rec.setDescription(rs.getString("description"));
                    rec.setDate(rs.getTimestamp("date"));

                    // Use ServicePublication to get Publication by ID
                    Post post = new ServicePost().getOneByID(rs.getInt("id_post"));
                    rec.setPub(post);

                    // Use ServiceUtilisateur to get Utilisateur by ID
                    Utilisateur utilisateur = new ServiceUtilisateur().getOneByID(rs.getInt("id_utilisateur"));
                    rec.setUser(utilisateur);

                    reclamations.add(rec);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting Reclamations: " + e.getMessage());
        }
        return reclamations;
    }

    public Set<Reclamation> getAllOrderByMostReactions() throws SQLException {
        Set<Reclamation> reclamations = new HashSet<>();
        String req = "SELECT r.*, p.totalReactions " +
                "FROM `reclamation` r " +
                "JOIN `post` p ON r.id_post = p.id_post " +
                "ORDER BY p.totalReactions ASC"; // Order by totalReactions in descending order

        try (PreparedStatement ps = cnx.prepareStatement(req);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Reclamation rec = new Reclamation();
                rec.setIdReclamation(rs.getInt("id_reclamation"));
                rec.setStatus(rs.getInt("status"));
                rec.setType(rs.getString("type"));
                rec.setTitre(rs.getString("titre"));
                rec.setDescription(rs.getString("description"));
                rec.setDate(rs.getTimestamp("date"));

                // Use ServicePublication to get Publication by ID
                Post post = new ServicePost().getOneByID(rs.getInt("id_post"));
                post.setTotalReactions(rs.getInt("totalReactions")); // Set totalReactions
                rec.setPub(post);

                // Use ServiceUtilisateur to get Utilisateur by ID
                Utilisateur utilisateur = new ServiceUtilisateur().getOneByID(rs.getInt("id_utilisateur"));
                rec.setUser(utilisateur);

                reclamations.add(rec);
            }
        } catch (SQLException e) {
            System.err.println("Error getting Reclamations: " + e.getMessage());
        }
        return reclamations;
    }


    private Reclamation createReclamationFromResultSet(ResultSet rs) throws SQLException {
        Reclamation rec = new Reclamation();
        rec.setIdReclamation(rs.getInt("id_reclamation"));
        rec.setStatus(rs.getInt("status"));
        rec.setType(rs.getString("type"));
        rec.setTitre(rs.getString("titre"));
        rec.setDescription(rs.getString("description"));
        rec.setDate(rs.getTimestamp("date"));

        // Use ServicePublication to get Publication by ID
        Post post = new ServicePost().getOneByID(rs.getInt("id_post"));
        rec.setPub(post);

        // Use ServiceUtilisateur to get Utilisateur by ID
        Utilisateur utilisateur = new ServiceUtilisateur().getOneByID(rs.getInt("id_utilisateur"));
        rec.setUser(utilisateur);

        return rec;
    }


    public Set<Reclamation> getAllOrderByNewestDateAndTime() throws SQLException {
        Set<Reclamation> reclamations = new TreeSet<>((rec1, rec2) -> rec2.getDate().compareTo(rec1.getDate()));

        String req = "SELECT * FROM `reclamation` ORDER BY `date` DESC"; // Order by date in descending order

        try (PreparedStatement ps = cnx.prepareStatement(req);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Reclamation rec = createReclamationFromResultSet(rs);
                reclamations.add(rec);
            }
        } catch (SQLException e) {
            System.err.println("Error getting Reclamations: " + e.getMessage());
        }
        return reclamations;
    }

    public Set<Reclamation> getAllOrderByOldestDateAndTime() throws SQLException {
        Set<Reclamation> reclamations = new TreeSet<>(Comparator.comparing(Reclamation::getDate));

        String req = "SELECT * FROM `reclamation` ORDER BY `date` ASC"; // Order by date in ascending order

        try (PreparedStatement ps = cnx.prepareStatement(req);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Reclamation rec = createReclamationFromResultSet(rs);
                reclamations.add(rec);
            }
        } catch (SQLException e) {
            System.err.println("Error getting Reclamations: " + e.getMessage());
        }
        return reclamations;
    }







}
