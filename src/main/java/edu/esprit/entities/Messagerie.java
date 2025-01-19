package edu.esprit.entities;

import java.util.Date;
import java.util.Objects;

public class Messagerie {
    public int idMessage;
    public String type,contenu;
    public Date date;
    Utilisateur senderId, reciverId;

    public Messagerie() {
    }

    public Messagerie(String type, String contenu, Date date) {
        this.type = type;
        this.contenu = contenu;
        this.date = date;
    }

    public Messagerie(String type, String contenu, Date date, Utilisateur senderId, Utilisateur reciverId) {
        this.type = type;
        this.contenu = contenu;
        this.date = date;
        this.senderId = senderId;
        this.reciverId = reciverId;
    }

    public Messagerie(int idMessage, String type, String contenu, Date date, Utilisateur senderId, Utilisateur reciverId) {
        this.idMessage = idMessage;
        this.type = type;
        this.contenu = contenu;
        this.date = date;
        this.senderId = senderId;
        this.reciverId = reciverId;
    }

    public int getIdMessage() {
        return idMessage;
    }

    public void setIdMessage(int idMessage) {
        this.idMessage = idMessage;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Utilisateur getSenderId() {
        return senderId;
    }

    public void setSenderId(Utilisateur senderId) {
        this.senderId = senderId;
    }

    public Utilisateur getReciverId() {
        return reciverId;
    }

    public void setReciverId(Utilisateur reciverId) {
        this.reciverId = reciverId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Messagerie that = (Messagerie) o;
        return idMessage == that.idMessage && Objects.equals(senderId, that.senderId) && Objects.equals(reciverId, that.reciverId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idMessage, senderId, reciverId);
    }

    @Override
    public String toString() {
        return "Messagerie{" +
                "id_message=" + idMessage +
                ", type='" + type + '\'' +
                ", contenu='" + contenu + '\'' +
                ", date=" + date +
                ", sender_id=" + senderId +
                ", reciver_id=" + reciverId +
                '}';
    }
}
