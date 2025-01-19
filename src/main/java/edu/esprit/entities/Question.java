package edu.esprit.entities;


import java.util.Objects;

public class Question {
    private int id_question;
    private String question;
    private String choix;
    private Quiz quiz ;

    private int reponse_correcte ;

    public Question ()
    {

    }

    public Question(int id_question, String question, String choix, Quiz quiz,int reponse_correcte) {
        this.id_question = id_question;
        this.question = question;
        this.choix = choix;
        this.quiz = quiz;
        this.reponse_correcte=reponse_correcte;
    }

    public Question(String question, String choix, Quiz quiz,int reponse_correcte) {
        this.question = question;
        this.choix = choix;
        this.quiz = quiz;
        this.reponse_correcte=reponse_correcte;
    }

    public int getId_question() {
        return id_question;
    }

    public void setId_question(int id_question) {
        this.id_question = id_question;
    }
    public int getReponse_correcte() {
        return reponse_correcte;
    }

    public void setReponse_correcte(int reponse_correcte) {
        this.reponse_correcte = reponse_correcte;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getChoix() {
        return choix;
    }

    public void setChoix(String choix) {
        this.choix = choix;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {this.quiz = quiz;
    }
    @Override
    public String toString() {
        return "Question{" +
                "question='" + question + '\'' +
                ", choix='" + choix + '\'' +
                ", reponse_correcte=" + reponse_correcte +
                ", quiz=" + quiz.getCode_quiz() +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return id_question == question.id_question;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_question);
    }
}
