package edu.esprit.services;

import edu.esprit.entities.Admin;
import edu.esprit.entities.Candidat;
import edu.esprit.entities.Representant;
import edu.esprit.entities.Utilisateur;
import edu.esprit.utils.DataSource;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class ServiceUtilisateur implements IService<Utilisateur> {
    Connection cnx = DataSource.getInstance().getCnx();

    @Override
    public void ajouter(Utilisateur utilisateur) throws SQLException {
        if (utilisateur instanceof Admin) {
            Admin a = (Admin) utilisateur;
            String req = "INSERT INTO `utilisateur`( `cin`, `nom`, `prenom`, `adresse`, `mdp`, `role`,`image`) VALUES (?,?,?,?,?,?,?)";
            try {
                PreparedStatement ps = cnx.prepareStatement(req);
                if(!utilisateurExiste(a.getCin())){
                ps.setInt(1, a.getCin());
                ps.setString(2, a.getNom());
                ps.setString(3, a.getPrenom());
                ps.setString(4, a.getAdresse());
                String hashed =hashPassword(a.getMdp());
                ps.setString(5, hashed);
                ps.setInt(6,0);
                ps.setString(7,a.getImage());
                ps.executeUpdate();
                System.out.println("Admin added");}



            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } else {
            String req = "INSERT INTO `utilisateur`( `cin`, `nom`, `prenom`, `adresse`, `mdp`, `role`,`image`) VALUES (?,?,?,?,?,?,?)";
            try {
                PreparedStatement ps = cnx.prepareStatement(req);

                ps.setInt(1, utilisateur.getCin());
                ps.setString(2, utilisateur.getNom());
                ps.setString(3, utilisateur.getPrenom());
                ps.setString(4, utilisateur.getAdresse());
                String hashed= hashPassword(utilisateur.getMdp());
                ps.setString(5, hashed);
                if (utilisateur instanceof Representant) {
                    ps.setInt(6, 1);
                    ps.setString(7,utilisateur.getImage());
                    ps.executeUpdate();
                    System.out.println("Representant added");
                } else {
                    ps.setInt(6,2);
                    ps.setString(7,utilisateur.getImage());
                    ps.executeUpdate();
                    System.out.println("Candidat added");
                }


            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

        }

    }

    @Override
    public void modifier(Utilisateur utilisateur) throws SQLException {
        String req = "UPDATE utilisateur SET cin =?, nom = ?, prenom = ?, adresse = ?, mdp = ? , image=? WHERE id_utilisateur = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, utilisateur.getCin());
            ps.setString(2, utilisateur.getNom());
            ps.setString(3, utilisateur.getPrenom());
            ps.setString(4, utilisateur.getAdresse());
            ps.setString(5, utilisateur.getMdp());
            ps.setString(6,utilisateur.getImage());
            ps.setInt(7, utilisateur.getId_utilisateur());

            ps.executeUpdate();
            System.out.println("utilisateur modifié!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


    }

    public void modifier_par_cin(Utilisateur utilisateur) throws SQLException {
        String req = "UPDATE utilisateur SET  nom = ?, prenom = ?, adresse = ?, mdp = ?  WHERE cin = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setString(1, utilisateur.getNom());
            ps.setString(2, utilisateur.getPrenom());
            ps.setString(3, utilisateur.getAdresse());
            String mdp = hashPassword(utilisateur.getMdp());
            ps.setString(4, mdp);
            ps.setInt(5, utilisateur.getCin());


            ps.executeUpdate();
            System.out.println("utilisateur modifié!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }



    }
    public void modifier_Image(Utilisateur u,String image )
    {        String req = "UPDATE utilisateur SET   image = ?  WHERE cin = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setString(1, image);
            ps.setInt(2, u.getCin());



            ps.executeUpdate();
            System.out.println("Image modifié!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }
    public void modifier_par_cin_sansmdp(Utilisateur utilisateur) throws SQLException {
        ServiceUtilisateur su = new ServiceUtilisateur();
        String req = "UPDATE utilisateur SET  nom = ?, prenom = ?, adresse = ?WHERE cin = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setString(1, utilisateur.getNom());
            ps.setString(2, utilisateur.getPrenom());
            ps.setString(3, utilisateur.getAdresse());
            ps.setInt(4, utilisateur.getCin());


            ps.executeUpdate();
            System.out.println("utilisateur modifié!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


    }
    public void modifier_representant_parcin(Representant u) throws SQLException {
        String req = "UPDATE utilisateur SET  nom = ?, prenom = ?, adresse = ?, mdp = ?,role=? WHERE cin = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);

            ps.setString(1, u.getNom());
            ps.setString(2, u.getPrenom());
            ps.setString(3, u.getAdresse());
            String hashed =hashPassword(u.getMdp());
            ps.setString(4, hashed);
            ps.setInt(5, 1);
            ps.setInt(6, u.getCin());


            ps.executeUpdate();
            System.out.println("representant modifié!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void modifier_candidat_parcin(Candidat representant) throws SQLException {
        String req = "UPDATE utilisateur SET  nom = ?, prenom = ?, adresse = ?, mdp = ?,role=? WHERE cin = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);

            ps.setString(1, representant.getNom());
            ps.setString(2, representant.getPrenom());
            ps.setString(3, representant.getAdresse());
            String hashed =hashPassword(representant.getMdp());
            ps.setString(4, hashed);
            ps.setInt(5, 2);
            ps.setInt(6, representant.getCin());


            ps.executeUpdate();
            System.out.println("Candidat modifié!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }




    public void modifier_admin(Admin admin) {
        String req = "UPDATE utilisateur SET cin =?, nom = ?, prenom = ?, adresse = ?, mdp = ? WHERE id_utilisateur = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, admin.getCin());
            ps.setString(2, admin.getNom());
            ps.setString(3, admin.getPrenom());
            ps.setString(4, admin.getAdresse());
            ps.setString(5, admin.getMdp());
            ps.setInt(6, admin.getId_utilisateur());

            ps.executeUpdate();
            System.out.println("admin modifié!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


    }
    public void modifier_admin_parcin(Admin admin) throws SQLException {
        String req = "UPDATE utilisateur SET  nom = ?, prenom = ?, adresse = ?, mdp = ? WHERE cin = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);

            ps.setString(1, admin.getNom());
            ps.setString(2, admin.getPrenom());
            ps.setString(3, admin.getAdresse());
            String hashed =hashPassword(admin.getMdp());
            ps.setString(4, hashed);
            ps.setInt(5, admin.getCin());

            ps.executeUpdate();
            System.out.println("admin modifié!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


    }

    @Override
    public void supprimer(int id) throws SQLException{
        String req = "DELETE FROM utilisateur WHERE id_utilisateur = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("l'utilisateur avec l'id  " + id + " a ete supprime!");
            } else {
                System.out.println("il n'y a pas d'utilisateur avec l'id:  " + id);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void supprimer_par_cin(int cin) throws SQLException{
        String req = "DELETE FROM utilisateur WHERE cin = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);

            ps.setInt(1, cin);

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("l'utilisateur avec le cin  " + cin + " a ete supprime!");
            }

            else {
                System.out.println("il n'y a pas d'utilisateur avec le cin:  " + cin);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public int getUserIdByCin(int cin) {
        String req = "SELECT id_utilisateur FROM utilisateur WHERE cin = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, cin);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                System.out.println("No user found with cin " + cin);
                return -1;
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }


    public void supprimer_admin(int id) {
        String req = "DELETE FROM admin WHERE id_admin = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("l'admin avec l'id  " + id + " a ete supprime!");
            } else {
                System.out.println("il n'y a pas d'admin avec l'id:  " + id);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    @Override
    public Set<Utilisateur> getAll() throws SQLException{
        Set<Utilisateur> utilisateurs = new HashSet<>();
        String req = "Select * from utilisateur";
        try {
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(req);
            while (rs.next()) {

                int cin = rs.getInt(2);
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                String adresse = rs.getString("adresse");
                String mdp = rs.getString("mdp");
                int role = rs.getInt(7);
                String image=rs.getString("image");
                if (role == 1) {
                    Representant p = new Representant(cin, nom, prenom, adresse, mdp,image);
                    utilisateurs.add(p);
                } else if (role==2) {
                    Candidat p = new Candidat(cin, nom, prenom, adresse, mdp,image);
                    utilisateurs.add(p);
                }


            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return utilisateurs;
    }
    public Set<Admin> getAll_admin() {
        Set<Admin> admins = new HashSet<>();
        String req = "Select * from utilisateur";
        try {
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(req);
            while (rs.next()) {

                int cin = rs.getInt(2);
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                String adresse = rs.getString("adresse");
                String mdp = rs.getString("mdp");
                int role = rs.getInt("role");

                if (role == 0) {
                    Admin p = new Admin(cin, nom, prenom, adresse, mdp);
                    admins.add(p);
                }

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return admins;
    }


    @Override
    public Utilisateur getOneByID(int id) {
        String req = "SELECT * FROM utilisateur WHERE id_utilisateur = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                // Assuming Utilisateur has an appropriate constructor
                Utilisateur user = new Utilisateur(
                        rs.getInt("id_utilisateur"),
                        rs.getInt("cin"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("adresse"),
                        rs.getString("mdp"),
                        rs.getString("image")
                );
                // Set other properties as needed
                return user;
            } else {
                System.out.println("pas de utilisateur avec ce id " + id);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null; // Return null if no user found
    }
    public int verifyRoleByCin(int cin)
    {
        String req = "SELECT * FROM utilisateur WHERE cin = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, cin);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                // Assuming Utilisateur has appropriate constructor
                int role = rs.getInt("role");

                return role;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return -1;

    }
    public int generateOTP()
    {

        Random random = new Random();
        return 1000 + random.nextInt(9000);

    }
    public void modifier_OTP_par_cin(int cin,int otp) {
        String req = "UPDATE utilisateur SET OTP =? WHERE cin = ?";

        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, otp);
            ps.setInt(2, cin);

            ps.executeUpdate();
            System.out.println("OTP modifié!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


    }
    public void modifier_Status_par_cin(int cin) {
        String req = "UPDATE utilisateur SET Status =? WHERE cin = ?";

        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, 1);
            ps.setInt(2, cin);

            ps.executeUpdate();
            System.out.println("OTP modifié!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


    }
    public int getIdByCin(int cin) {
        String req = "SELECT id_utilisateur FROM utilisateur WHERE cin = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, cin);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id_utilisateur");
            } else {
                System.out.println("No user found with cin " + cin);
                return -1; // You may choose a different value to represent "not found"
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            e.printStackTrace();
            return -1; // Handle the exception, you may choose a different value here as well
        }
    }



    public Utilisateur getOneByCin(int cin) throws SQLException {
        String req = "SELECT * FROM utilisateur WHERE cin = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, cin);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                // Assuming Utilisateur has appropriate constructor
                Utilisateur user = new Utilisateur();
                user.setCin(rs.getInt("cin"));
                user.setNom(rs.getString("nom"));
                user.setPrenom(rs.getString("prenom"));
                user.setAdresse(rs.getString("adresse"));
                user.setMdp(rs.getString("mdp"));
                user.setImage("image");
                // Set other properties as needed
                return user;
            } else {
                System.out.println("pas de utilisateur avec ce cin " + cin);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null; // Return null if no user found
    }

    public Utilisateur getOneAdminByID(int id) {
        String req = "SELECT * FROM admin WHERE id_admin = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                // Assuming Utilisateur has appropriate constructor
                Admin user = new Admin();
                user.setCin(rs.getInt("cin"));
                user.setNom(rs.getString("nom"));
                user.setPrenom(rs.getString("prenom"));
                user.setAdresse(rs.getString("email"));
                user.setMdp(rs.getString("mdp"));
                // Set other properties as needed
                return user;
            } else {
                System.out.println("pas de admin avec ce id " + id);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null; // Return null if no user found
    }


    public String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xFF & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Erreur lors du hachage du mot de passe : " + e.getMessage());
            return null;
        }
    }
    public boolean utilisateurExiste(int cin) {
        String req = "SELECT COUNT(*) FROM utilisateur WHERE cin = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, cin);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0; // Retourne true si l'utilisateur existe, sinon false
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false; // Si une erreur se produit ou si aucun résultat n'est trouvé
    }
    public boolean adminExiste(int cin) {
        String req = "SELECT COUNT(*) FROM utilisateur WHERE cin = ? AND role = 0";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, cin);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0; // Retourne true si l'utilisateur existe, sinon false
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false; // Si une erreur se produit ou si aucun résultat n'est trouvé
    }
    public int getStatusfromCIN(int cin)
    {
        String req = "SELECT status FROM utilisateur WHERE cin = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, cin);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                System.out.println("No user found with cin " + cin);
                return -1;
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }
    public String getMDPfromCIN(int cin)
    {
        String req = "SELECT mdp FROM utilisateur WHERE cin = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, cin);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString(1);
            } else {
                System.out.println("No user found with cin " + cin);
                return null;
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    public boolean verifyUser_Etab(int id) {
        String req = "SELECT * FROM etablissement WHERE id_utilisateur = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            return rs.next();  // Returns true if a user with the specified ID is found, false otherwise
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public String getImagefromCin(int cin)
    {
        String req = "SELECT image FROM utilisateur WHERE cin = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, cin);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString(1);
            } else {
                System.out.println("No user found with cin " + cin);
                return null;
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    public Utilisateur get_One_ByCin(int cin) {
        String req = "SELECT * FROM utilisateur WHERE cin = ?";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setInt(1, cin);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Assuming Utilisateur has an appropriate constructor
                    Utilisateur user = new Utilisateur();
                    user.setId_utilisateur(rs.getInt("id_utilisateur"));
                    user.setCin(rs.getInt("cin"));
                    user.setNom(rs.getString("nom"));
                    user.setPrenom(rs.getString("prenom"));
                    user.setAdresse(rs.getString("adresse"));
                    user.setMdp(rs.getString("mdp"));
                    // Set other properties as needed
                    return user;
                } else {
                    // If no user found, return null
                    return null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Log the exception for debugging
            // Handle or rethrow the exception as needed
        }
        return null; // Return null in case of an exception
    }

    public Utilisateur getByCin(int cin) {
        String req = "SELECT * FROM utilisateur WHERE cin = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, cin);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Utilisateur user = new Utilisateur();
                user.setId_utilisateur(rs.getInt("id_utilisateur"));
                user.setCin(rs.getInt("cin"));
                user.setNom(rs.getString("nom"));
                user.setPrenom(rs.getString("prenom"));
                user.setAdresse(rs.getString("adresse"));
                user.setMdp(rs.getString("mdp"));
                // Set other properties as needed
                return user;
            } else {
                System.out.println("Aucun utilisateur avec ce CIN : " + cin);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null; // Retourner null si aucun utilisateur n'est trouvé
    }



}