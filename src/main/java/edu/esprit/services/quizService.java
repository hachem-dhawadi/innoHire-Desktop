package edu.esprit.services;

import edu.esprit.entities.*;
import edu.esprit.utils.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class quizService implements IService<Quiz> {
    Connection cnx = DataSource.getInstance().getCnx();
    ServiceEtablissement se =new ServiceEtablissement();
    ServiceWallet sw =new ServiceWallet();
    Etablissement etablissementCo=se.getOneByID(CurrentEtablissement.getIdEtablissement());
    Wallet walletCo=sw.getOneByID(CurrentWallet.getIdWallet());

    public quizService() throws SQLException {
    }

    @Override
    public void ajouter(Quiz quiz) throws SQLException {

        try {
            String query = "INSERT INTO quiz (code_quiz, nom_quiz, description, prix_quiz, image_quiz) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = cnx.prepareStatement(query)) {
                preparedStatement.setInt(1, quiz.getCode_quiz());
                preparedStatement.setString(2, quiz.getNom_quiz());
                preparedStatement.setString(3, quiz.getDescription());
                preparedStatement.setInt(4, quiz.getPrix_quiz());
                preparedStatement.setString(5, quiz.getImage_quiz());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modifier(Quiz quiz) throws SQLException {

        try {
            String query = "UPDATE quiz SET code_quiz = ?, nom_quiz = ?, description = ?, prix_quiz = ?, image_quiz = ? WHERE id_quiz = ?";
            try (PreparedStatement preparedStatement = cnx.prepareStatement(query)) {
                preparedStatement.setInt(1, quiz.getCode_quiz());
                preparedStatement.setString(2, quiz.getNom_quiz());
                preparedStatement.setString(3, quiz.getDescription());
                preparedStatement.setInt(4, quiz.getPrix_quiz());
                preparedStatement.setString(5, quiz.getImage_quiz());
                preparedStatement.setInt(6, quiz.getId_quiz());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void supprimer(int id) {

        try {
            String query = "DELETE FROM quiz WHERE id_quiz = ?";
            try (PreparedStatement preparedStatement = cnx.prepareStatement(query)) {
                preparedStatement.setInt(1, id);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Set<Quiz> getAll() throws SQLException{
        Set<Quiz> quizSet = new HashSet<>();

        try {
            String query = "SELECT * FROM quiz";
            try (PreparedStatement preparedStatement = cnx.prepareStatement(query)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        Quiz quiz = new Quiz();
                        quiz.setId_quiz(resultSet.getInt("id_quiz"));
                        quiz.setCode_quiz(resultSet.getInt("code_quiz"));
                        quiz.setNom_quiz(resultSet.getString("nom_quiz"));
                        quiz.setDescription(resultSet.getString("description"));
                        quiz.setPrix_quiz(resultSet.getInt("prix_quiz"));
                        quiz.setImage_quiz(resultSet.getString("image_quiz"));
                        quizSet.add(quiz);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return quizSet;
    }

    @Override
    public Quiz getOneByID(int id) {
        try {
            String query = "SELECT * FROM quiz WHERE id_quiz = ?";
            try (PreparedStatement preparedStatement = cnx.prepareStatement(query)) {
                preparedStatement.setInt(1, id);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        Quiz quiz = new Quiz();
                        quiz.setId_quiz(resultSet.getInt("id_quiz"));
                        quiz.setCode_quiz(resultSet.getInt("code_quiz"));
                        quiz.setNom_quiz(resultSet.getString("nom_quiz"));
                        quiz.setDescription(resultSet.getString("description"));
                        quiz.setPrix_quiz(resultSet.getInt("prix_quiz"));
                        quiz.setImage_quiz(resultSet.getString("image_quiz"));
                        return quiz;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public List<Integer> getQuizcodes() throws SQLException {
        List<Integer> quizcodes = new ArrayList<>();


        try {
            String sql = "SELECT code_quiz FROM quiz";

            try (PreparedStatement preparedStatement = cnx.prepareStatement(sql)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        int codeQuiz = resultSet.getInt("code_quiz");
                        quizcodes.add(codeQuiz);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return quizcodes;
    }

    public List<Question> getQuestionsByCodeQuiz(int codeQuiz) {
        List<Question> questions = new ArrayList<>();

        try {
            // Utilisez la fonction getIdQuizByCode pour récupérer l'ID du quiz
            int idQuiz = getIdQuizByCode(codeQuiz);

            // Modifiez la requête pour utiliser l'ID du quiz
            String query = "SELECT * FROM question WHERE id_quiz = ?";
            try (PreparedStatement preparedStatement = cnx.prepareStatement(query)) {
                preparedStatement.setInt(1, idQuiz);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        Question question = new Question();
                        question.setId_question(resultSet.getInt("id_question"));
                        question.setQuestion(resultSet.getString("question"));
                        question.setChoix(resultSet.getString("choix"));
                        // ...
                        question.setReponse_correcte(resultSet.getInt("reponse_correcte"));

                        // Ajoutez la question à la liste
                        questions.add(question);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return questions;
    }
    public void saveQuizPass(int codeQuiz, int idCandidat,int score) throws SQLException {
        try {
            // Assurez-vous que la table quiz_pass existe dans votre base de données
            String query = "INSERT INTO quiz_pass (code_quiz, id_candidat,score) VALUES (?, ?,?)";
            try (PreparedStatement preparedStatement = cnx.prepareStatement(query)) {
                preparedStatement.setInt(1, codeQuiz);
                preparedStatement.setInt(2, idCandidat);
                preparedStatement.setInt(3, score);

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour vérifier si un candidat a déjà passé un quiz
    public boolean candidatHasPassedQuiz(int codeQuiz, int idCandidat) throws SQLException {
        // Votre logique de vérification ici
        String query = "SELECT * FROM quiz_pass WHERE code_quiz = ? AND id_candidat = ?";
        try (PreparedStatement preparedStatement = cnx.prepareStatement(query)) {
            preparedStatement.setInt(1, codeQuiz);
            preparedStatement.setInt(2, idCandidat);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next(); // Retourne true si le candidat a déjà passé ce quiz, sinon false
            }
        }
    }

    public int getIdQuizByCode(Integer codeQuiz) throws SQLException {



        try {
            String sql = "SELECT id_quiz FROM quiz WHERE code_quiz = ?";
            try (PreparedStatement preparedStatement = cnx.prepareStatement(sql)) {
                preparedStatement.setInt(1, codeQuiz);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt("id_quiz");
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        return -1;
    }
    public Set<Quiz> getQuizbyIDetablissement() {
        Set<Quiz> quizSet = new HashSet<>();

        try {

            int idEtablissement = walletCo.getEtablissement().getIdEtablissement();

            // Connexion à votre base de données (assurez-vous d'avoir une connexion valide ici)
            /* obtenir votre connexion à la base de données */;

            // Requête SQL pour récupérer les ID de Quiz associés à l'établissement
            String sql = "SELECT id_quiz FROM etablissement_quiz WHERE id_etablissement = ?";

            try (PreparedStatement statement = cnx.prepareStatement(sql)) {
                statement.setInt(1, idEtablissement);

                try (ResultSet resultSet = statement.executeQuery()) {
                    // Parcourir les résultats et ajouter les Quiz à l'ensemble
                    while (resultSet.next()) {
                        int idQuiz = resultSet.getInt("id_quiz");

                        // Ajouter le Quiz à l'ensemble en utilisant une méthode de votre choix pour récupérer les Quiz
                        // par exemple, vous pouvez avoir une méthode serviceQ.getQuizById(idQuiz) dans votre service
                        Quiz quiz =getOneByID(idQuiz);

                        if (quiz != null) {
                            quizSet.add(quiz);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return quizSet;
    }
    public Quiz getQuizByCode(int codeQuiz) {
        Quiz quiz = null;
        String query = "SELECT * FROM quiz WHERE code_quiz = ?";

        try (PreparedStatement pstmt = cnx.prepareStatement(query)) {
            pstmt.setInt(1, codeQuiz);

            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    int idQuiz = resultSet.getInt("id_quiz");
                    String nomQuiz = resultSet.getString("nom_quiz");
                    String descriptionQuiz = resultSet.getString("description");
                    int prixQuiz = resultSet.getInt("prix_quiz");
                    String imageQuiz = resultSet.getString("image_quiz");
                    // Ajoutez d'autres colonnes au besoin

                    // Créez l'instance de Quiz avec les données de la base de données
                    quiz = new Quiz(idQuiz, codeQuiz, nomQuiz, descriptionQuiz, prixQuiz, imageQuiz);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return quiz;
    }




}
