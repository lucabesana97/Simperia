package gui.elements;

import game.Coordinates;
import game.GameObject;
import main.Gameplay;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;

public class HUD extends GameObject {
    public BufferedImage currentImage;
    private BufferedImage originalImage; // Store the original image to be able to reset the HUD
    private URL hudImage;
    private String levelLabel;
    private final int MAX_HEALTH = Gameplay.player.maxHealth;

    public HUD(Coordinates coordinates, String path) {
        super(coordinates);
        hudImage = getClass().getResource(path);
        try {
            assert hudImage != null;
            this.originalImage = ImageIO.read(hudImage);
            this.currentImage = originalImage;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public HUD(Coordinates coordinates) {
        super(coordinates);
        levelLabel = "Level: " + Gameplay.player.level;
    }

    public void draw(Graphics graphics) {
        if (currentImage != null) {
            // Draw the health/xp bar
            graphics.drawImage(currentImage, (int) coordinates.topLeftCorner_x, (int) coordinates.topLeftCorner_y, (int) coordinates.size_X, (int) coordinates.size_Y, null);
        } else {
            // Load new font
            try {
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("src/gui/fonts/TT Octosquares Trial Black.ttf")));
            } catch (Exception e) { e.printStackTrace(); }
            // Draw the level label
            graphics.setColor(Color.WHITE);
            graphics.setFont(new Font("TT Octosquares Trl Blc", Font.PLAIN, 18));
            graphics.drawString(levelLabel, (int) coordinates.topLeftCorner_x, (int) coordinates.topLeftCorner_y);
        }
    }

    // Cut the image horizontally according to a percentage
    public void cutImageHorizontally(double percentage) {
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        int newWidth = (int) (width * percentage);

        if (newWidth <= 0) {
            newWidth = 1;
        }

        BufferedImage newImage = new BufferedImage(newWidth, height, BufferedImage.TYPE_INT_ARGB);
        coordinates.size_X = newWidth;

        Graphics2D g = newImage.createGraphics();
        g.drawImage(originalImage, 0, 0, newWidth, height, 0, 0, newWidth, height, null);
        g.dispose();

        currentImage = newImage;
    }

    public void updateHealthBar() {
        int health = Gameplay.player.health;
        //System.out.println("Health: " + health);
        if (health <= 0) {
            cutImageHorizontally(0.0);
        } else if (health >= MAX_HEALTH) {
            cutImageHorizontally(1.0);
        } else {
            cutImageHorizontally((double) health / MAX_HEALTH);
        }
    }

    public void update(int health) {
        //System.out.println("Health: " + health);
        if (health <= 0) {
            cutImageHorizontally(0.0);
        } else if (health >= MAX_HEALTH) {
            cutImageHorizontally(1.0);
        } else {
            cutImageHorizontally((double) health / MAX_HEALTH);
        }
    }

    public void updateXpBar() {
        int xp = Gameplay.player.xp;
        int MAX_XP = Gameplay.player.xpToNextLevel;
        //System.out.println("XP: " + xp);
        if (xp <= 0) {
            cutImageHorizontally(0.0);
        } else if (xp >= MAX_XP) {
            cutImageHorizontally(1.0);
        } else {
            cutImageHorizontally((double) xp / MAX_XP);
        }
    }

    public void updateLevelLabel() {
        levelLabel = "Level: " + Gameplay.player.level;
    }
}
