package edu.esprit.entities;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.imageio.ImageIO;
import javax.swing.*;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import edu.esprit.controllers.jaw;

public class CameraCapture {
    private String imageJdida;
    public String getImageJdida() {
        return imageJdida;
    }

    public void setImageJdida(String imageJdida) {
        this.imageJdida = imageJdida;
    }

    public static void startCameraCapture() {
        JFrame frame = new JFrame("Camera Surveillance");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        Webcam webcam = Webcam.getDefault();
        webcam.setViewSize(WebcamResolution.VGA.getSize());

        WebcamPanel webcamPanel = new WebcamPanel(webcam);
        webcamPanel.setFillArea(true);

        JButton captureButton = new JButton("Capture");
        captureButton.addActionListener(e -> captureImage(webcam));

        frame.setLayout(new BorderLayout());
        frame.add(webcamPanel, BorderLayout.CENTER);
        frame.add(captureButton, BorderLayout.SOUTH);

        frame.pack();
        frame.setVisible(true);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                webcam.close();
            }
        });
    }

    private static void captureImage(Webcam webcam) {
        String currentDir = System.getProperty("user.dir");
        File imgDir = new File(currentDir + "/src/main/resources/img");

        // Create the directory if it doesn't exist
        if (!imgDir.exists()) {
            imgDir.mkdirs();
        }

        // Generate a unique filename with a timestamp
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timestamp = dateFormat.format(new Date());
        String filename = "captured_image_" + timestamp + ".jpg";

        File imageFile = new File(imgDir, filename);

        try {
            webcam.open();

            ImageIO.write(webcam.getImage(), "JPG", imageFile);
            jaw.setImageJdida(imageFile.getAbsolutePath());

            JOptionPane.showMessageDialog(null, "Image captured and saved to: " + imageFile.getAbsolutePath());

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            webcam.close();
        }
    }


}
