














package edu.esprit.services;

import edu.esprit.entities.Question;


import edu.esprit.entities.Quiz;
import edu.esprit.utils.DataSource;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class questionService implements IService<Question> {
    Connection cnx = DataSource.getInstance().getCnx();

    @Override


    public void ajouter(Question question) throws SQLException {
        try{
            String query = "INSERT INTO question (question, choix, id_quiz,reponse_correcte) VALUES (?, ?, ?,?)";
            try (PreparedStatement preparedStatement = cnx.prepareStatement(query)) {
                preparedStatement.setString(1, question.getQuestion());
                preparedStatement.setString(2, question.getChoix());
                preparedStatement.setInt(3, question.getQuiz().getId_quiz());
                preparedStatement.setInt(4, question.getReponse_correcte());

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void modifier(Question question) throws SQLException {
        try {
            String query = "UPDATE question SET question = ?, choix = ?, id_quiz = ?, reponse_correcte = ? WHERE id_question = ?";
            try (PreparedStatement preparedStatement = cnx.prepareStatement(query)) {
                preparedStatement.setString(1, question.getQuestion());
                preparedStatement.setString(2, question.getChoix());
                preparedStatement.setInt(3, question.getQuiz().getId_quiz());
                preparedStatement.setInt(4, question.getReponse_correcte());
                preparedStatement.setInt(5, question.getId_question());

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    @Override

    public void supprimer(int id_question) throws SQLException{
        String query = "DELETE FROM `question` WHERE `id_question`= ?";

        try (
             PreparedStatement preparedStatement = cnx.prepareStatement(query)) {
            preparedStatement.setInt(1, id_question);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    @Override
    public  Set<Question> getAll() throws SQLException {
        Set<Question> questions = new HashSet<>();

        String req = "Select * from question";
        try {
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(req);
            while(rs.next()){
                int id_question = rs.getInt("id_question"); //wala t7ot num colomn kima eli ta7etha
                String question1 = rs.getString("question");
                String choix = rs.getString("choix");
                int id_quiz = rs.getInt("id_quiz");
                int repC = rs.getInt("reponse_correcte");


                quizService QS= new quizService();
                Quiz Q = QS.getOneByID(id_quiz);

                Question q = new Question(id_question,question1,choix,Q,repC);
                questions.add(q);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return questions;
    }

    @Override
    public Question getOneByID(int id) {

        try {
            String query = "SELECT * FROM question WHERE id_question = ?";
            try (PreparedStatement preparedStatement = cnx.prepareStatement(query)) {
                preparedStatement.setInt(1, id);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        Question question = new Question();
                        question.setId_question(resultSet.getInt("id_question"));
                        question.setQuestion(resultSet.getString("question"));
                        question.setChoix(resultSet.getString("choix"));
                        question.getQuiz().setId_quiz(resultSet.getInt("id_quiz"));
                        question.setReponse_correcte(resultSet.getInt("reponse_correcte"));

                        return question;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public List<Map<String, Object>> getQuestionsForQuiz(int codeQuiz) throws SQLException {
        List<Map<String, Object>> questions = new ArrayList<>();
        quizService qs=new quizService();
       int id_quiz= qs.getIdQuizByCode(codeQuiz);




        try  {
            String query = "SELECT question, choix, reponse_correcte FROM question WHERE id_quiz =?";
            try (PreparedStatement preparedStatement = cnx.prepareStatement(query)) {
                preparedStatement.setInt(1, id_quiz);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        String questionText = resultSet.getString("question");
                        String choix = resultSet.getString("choix");
                        int reponseCorrecte = resultSet.getInt("reponse_correcte");

                        // Créer une map pour stocker les données de la question
                        Map<String, Object> questionMap = new HashMap<>();
                        questionMap.put("question", questionText);
                        questionMap.put("choix", choix);
                        questionMap.put("reponse_correcte", reponseCorrecte);

                        questions.add(questionMap);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return questions;
    }

}
