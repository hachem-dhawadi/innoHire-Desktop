package edu.esprit.entities;

import edu.esprit.utils.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects ;
public class Quiz {
    private int id_quiz ;
    private int code_quiz ;
    private String nom_quiz ;
    private String description ;
    private int prix_quiz ;
    private String image_quiz ;
    private List<Question> questions;
    public Quiz ()
    {

    }

    public Quiz(int id_quiz, int code_quiz, String nom_quiz, String description, int prix_quiz, String image_quiz) {
        this.id_quiz = id_quiz;
        this.code_quiz = code_quiz;
        this.nom_quiz = nom_quiz;
        this.description = description;
        this.prix_quiz = prix_quiz;
        this.image_quiz=image_quiz;
    }
    public Quiz( int code_quiz, String nom_quiz, String description, int prix_quiz, String image_quiz) {
        this.code_quiz = code_quiz;
        this.nom_quiz = nom_quiz;
        this.description = description;
        this.prix_quiz = prix_quiz;
        this.image_quiz = image_quiz;
    }

    public int getId_quiz() {
        return id_quiz;
    }

    public int getCode_quiz() {
        return code_quiz;
    }

    public String getNom_quiz() {
        return nom_quiz;
    }

    public String getDescription() {
        return description;
    }

    public int getPrix_quiz() {
        return prix_quiz;
    }

    public String getImage_quiz() {
        return image_quiz;
    }

    public void setId_quiz(int id_quiz) {
        this.id_quiz = id_quiz;
    }

    public void setCode_quiz(int code_quiz) {
        this.code_quiz = code_quiz;
    }

    public void setNom_quiz(String nom_quiz) {
        this.nom_quiz = nom_quiz;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrix_quiz(int prix_quiz) {
        this.prix_quiz = prix_quiz;
    }

    public void setImage_quiz(String image_quiz) {
        this.image_quiz= image_quiz;
    }
    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    @Override
    public String toString() {
        return "Quiz{"
                +
                ", code_quiz=" + code_quiz +
                ", nom_quiz='" + nom_quiz + '\'' +
                ", description='" + description + '\'' +
                ", prix_quiz=" + prix_quiz +
                ", image_quiz=" + image_quiz +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quiz quiz = (Quiz) o;
        return id_quiz == quiz.id_quiz && code_quiz == quiz.code_quiz;
    }


    @Override
    public int hashCode() {
        return Objects.hash(code_quiz);
    }
}
