package edu.esprit.controllers;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailClass {
    private String username = "Innohire45@gmail.com";
    private String password = "urrc gyro inoo zoov";

    public void envoyer(String reciever,String OTP) {
        // Etape 1 : Création de la session
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            // Etape 2 : Création de l'objet Message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("Innohire@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(reciever));
            message.setSubject("Innohire code");
            message.setText(OTP);

            // Etape 3 : Envoyer le message
            Transport.send(message);
            System.out.println("Message envoyé");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
