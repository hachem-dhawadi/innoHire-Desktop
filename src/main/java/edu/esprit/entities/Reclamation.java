package edu.esprit.entities;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

public class Reclamation {
    public int idReclamation,status;
    public String type,titre,description;
    public Timestamp date;

    Post pub;

    Utilisateur user;

    public Reclamation() {
    }

    public Reclamation(int status, String type, String titre, String description,Timestamp  date) {
        this.status = status;
        this.type = type;
        this.titre = titre;
        this.description = description;
        this.date = date;
    }

    public Reclamation(int status, String type, String titre, String description, Timestamp  date, Post pub, Utilisateur user) {
        this.status = status;
        this.type = type;
        this.titre = titre;
        this.description = description;
        this.date = date;
        this.pub = pub;
        this.user = user;
    }

    public Reclamation(int idReclamation, int status, String type, String titre, String description, Timestamp  date, Post pub, Utilisateur user) {
        this.idReclamation = idReclamation;
        this.status = status;
        this.type = type;
        this.titre = titre;
        this.description = description;
        this.date = date;
        this.pub = pub;
        this.user = user;
    }

    public int getIdReclamation() {
        return idReclamation;
    }

    public void setIdReclamation(int idReclamation) {
        this.idReclamation = idReclamation;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public Date getDate() {
        return date;
    }

    public void setDate(Timestamp  date) {
        this.date = date;
    }

    public Post getPub() {
        return pub;
    }

    public void setPub(Post pub) {
        this.pub = pub;
    }

    public Utilisateur getUser() {
        return user;
    }

    public void setUser(Utilisateur user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reclamation that = (Reclamation) o;
        return idReclamation == that.idReclamation && Objects.equals(pub, that.pub) && Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idReclamation, pub, user);
    }

    @Override
    public String toString() {
        return "Reclamation{" +
                "id_reclamation=" + idReclamation +
                ", status=" + status +
                ", type='" + type + '\'' +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", date=" + date +
                ", pub=" + pub +
                ", user Info= " + user+
                '}'+'\n';
    }
}
