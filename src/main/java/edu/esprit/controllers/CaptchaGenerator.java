package edu.esprit.controllers;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.image.BufferedImage;
import java.util.Random;

public class CaptchaGenerator {

    private Random random;
    private String chars;
    private Font font;
    private Font smallFont;

    public CaptchaGenerator() {
        random = new Random();
        chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        font = new Font("Times New Roman", Font.BOLD, 100);
        smallFont = new Font("Arial", Font.PLAIN, 45);
    }

    public CaptchaResult generateCaptcha() {
        int width = 2048;
        int height = 2048;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);

        int captchaLength = 6;
        StringBuilder captchaText = new StringBuilder();
        for (int i = 0; i < captchaLength; i++) {
            captchaText.append(chars.charAt(random.nextInt(chars.length())));
        }

        StringBuilder captchaStringBuffer = new StringBuilder();
        for (int i = 0; i < captchaText.length(); i++) {
            captchaStringBuffer.append(captchaText.charAt(i)).append(" ");
        }

        int fontSize = Math.max(100, 800 / captchaLength);

        FontRenderContext frc = g.getFontRenderContext();
        Font font = new Font("Times New Roman", Font.BOLD, fontSize);
        Rectangle bounds = font.getStringBounds(captchaStringBuffer.toString(), frc).getBounds();

        int x = (width - bounds.width) / 2;
        int y = (height - bounds.height) / 2 + bounds.height;
        if (y > height - 100) {
            y = height - 100;
        }

        for (int i = 0; i < captchaStringBuffer.length(); i++) {
            double angle = random.nextInt(60) - 30;
            g.rotate(Math.toRadians(angle), x + bounds.x + bounds.width / 2, y + bounds.y - bounds.height / 2);
            g.setColor(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
            g.setFont(font);
            g.drawString(captchaStringBuffer.charAt(i) + "", x, y);
            g.rotate(Math.toRadians(-angle), x + bounds.x + bounds.width / 2, y + bounds.y - bounds.height / 2);
            x += bounds.width / captchaStringBuffer.length() + 20;
        }

        for (int i = 0; i < 10; i++) {
            g.setColor(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
            g.setStroke(new BasicStroke(5 + random.nextInt(10)));
            g.drawLine(random.nextInt(width), random.nextInt(height), random.nextInt(width), random.nextInt(height));
        }

        g.setFont(smallFont);
        g.setColor(Color.BLACK);

        g.dispose(); // Release resources

        return new CaptchaResult(captchaText.toString(), image);
    }

    public static class CaptchaResult {
        private final String captchaCode;
        private final BufferedImage captchaImage;

        public CaptchaResult(String captchaCode, BufferedImage captchaImage) {
            this.captchaCode = captchaCode;
            this.captchaImage = captchaImage;
        }

        public String getCaptchaCode() {
            return captchaCode;
        }

        public BufferedImage getCaptchaImage() {
            return captchaImage;
        }
    }
}
