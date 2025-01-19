package edu.esprit.entities;

import java.time.LocalDate;
import java.util.Objects;

public class Commentaire {
 private int id_commentaire ;
 private Post publication ;
 private Utilisateur utilisateur ;
 private String description_co ;
 private LocalDate date_co ;
 //private   int  nb_etoile ;

 public Commentaire(int id_commentaire, String description_co, LocalDate date_co /*,int  nb_etoile*/) {
  this.id_commentaire = id_commentaire;
  this.description_co = description_co;
  this.date_co = date_co;
  //this.nb_etoile=nb_etoile;
 }


 public Commentaire(int id_commentaire, Post publication, Utilisateur utilisateur, String description_co, LocalDate date_co/*,int  nb_etoile*/) {
  this.id_commentaire = id_commentaire;
  this.publication = publication;
  this.utilisateur = utilisateur;
  this.description_co = description_co;
  this.date_co = date_co;
  //this.nb_etoile=nb_etoile;
 }

 public Commentaire(Post publication, Utilisateur utilisateur, String description_co, LocalDate date_co/*,int  nb_etoile*/) {
  this.publication = publication;
  this.utilisateur = utilisateur;
  this.description_co = description_co;
  this.date_co = date_co;
  //this.nb_etoile=nb_etoile;
 }

 public Commentaire() {
 }

 public int getId_commentaire() {
  return id_commentaire;
 }

 public void setId_commentaire(int id_commentaire) {
  this.id_commentaire = id_commentaire;
 }

 public Post getPublication() {
  return publication;
 }

 public void setPublication(Post publication) {
  this.publication = publication;
 }

 public Utilisateur getUtilisateur() {
  return utilisateur;
 }

 public void setUtilisateur(Utilisateur utilisateur) {
  this.utilisateur = utilisateur;
 }

 public String getDescription_co() {
  return description_co;
 }

 public void setDescription_co(String description_co) {
  this.description_co = description_co;
 }

 public LocalDate getDate_co() {
  return date_co;
 }

 public void setDate_co(LocalDate date_co) {
  this.date_co = date_co;
 }

 /*public int getNb_etoile() {
  return nb_etoile;
 }

 public void setNb_etoile(int nb_etoile) {
  this.nb_etoile = nb_etoile;
 }*/

 @Override
 public boolean equals(Object o) {
  if (this == o) return true;
  if (o == null || getClass() != o.getClass()) return false;
  Commentaire that = (Commentaire) o;
  return id_commentaire == that.id_commentaire;
 }

 @Override
 public int hashCode() {
  return Objects.hash(id_commentaire);
 }

 @Override

 public String
 toString() {
  return "commentaire{" +
          "Code_publication=" + publication.getId_post() +
          ",Cin_utilisateur=" + utilisateur.getCin() +
          ", description_co='" + description_co + '\'' +
          ", date_co=" + date_co +
          //",nb_etoile=" +nb_etoile+
          '}';
 }
}
