package edu.esprit.tests;

import edu.esprit.entities.*;
import edu.esprit.services.ServiceEtablissement;
import edu.esprit.services.ServiceUtilisateur;
import edu.esprit.utils.DataSource;

import java.sql.SQLException;
import java.util.*;


public class Main {
    public static void main(String[] args)
    {
        DataSource.getInstance();

     /* ServiceUtilisateur sr= new ServiceUtilisateur();
        try {
            sr.supprimer_par_cin(33);
        } catch (SQLException e) {
            System.out.printf(e.getMessage());
        }
        try {
            sr.supprimer_par_cin(12);
        } catch (SQLException e) {
            throw new RuntimeException(e);

        }*/
        Utilisateur u = new Utilisateur();
        u.setCin(123);
       /* Etablissement e = new Etablissement("esprit","lie",133,"er",u);
        ServiceEtablissement se= new ServiceEtablissement();
        try {
            se.ajouter(e);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }*/
      /*  Admin a = new Admin(1,234,"AMENALLAH123","KTHIRI","amenallah@esprit.tn","ELKING");
        //sr.ajouter(a);
        Representant r = new Representant(2,33,"AMENALLAH","KTHIRI","amenallah@esprit.tn","ELKING");
       //sr.ajouter(r);
        Candidat c= new Candidat(3,322,"YO222","ee","SLFJ@","TRAHHH");
        //sr.ajouter(c);

        System.out.println(sr.getAll());
        System.out.println(sr.getAll_admin());
        sr.modifier(c);
        sr.modifier_admin(a);
        sr.supprimer(44);
        sr.supprimer(3);
        sr.supprimer_admin(44);
        sr.supprimer_admin(12);
        System.out.println(sr.getOneByID(1));
        System.out.println(sr.getOneAdminByID(12));
        System.out.println(sr.getOneAdminByID(1));*/




    }
}
