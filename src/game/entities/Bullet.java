package game.entities;

import game.Coordinates;
import objState.EnemyState;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;

import static helperFunctions.Utility.rotateImageByDegrees;

public class Bullet extends Enemy{
    public final static int ENEMY = 1;
    public final static int PLAYER = 2;
    public int owner;
    public int angle;
    public int traveledDistance;
    public int maxTraveledDistance;
    public Bullet(int angle, Coordinates coordinates, int owner, int speed, int attack, int maxTraveledDistance) {
        super();
        this.maxTraveledDistance = maxTraveledDistance;
        this.attack = attack;
        this.speed = speed;
        this.angle = angle;
        this.name = "Bullet";
        this.enemyState = EnemyState.HOSTILE;
        this.owner = owner;

        URL bullet = getClass().getResource("/sprites/bullet.png");
        BufferedImage bulletImage;

        try {
            bulletImage = ImageIO.read(bullet);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        this.coordinates = new Coordinates((coordinates.topLeftCorner_x + coordinates.bottomRightCorner_x)/2, (coordinates.topLeftCorner_y + coordinates.bottomRightCorner_y)/2, bulletImage.getWidth(), bulletImage.getHeight());
        this.sprites.current = rotateImageByDegrees(bulletImage, angle);
    }

    public Bullet(int angle, Coordinates coordinates, int owner, int speed, int attack, String bulletPath, int maxTraveledDistance) {
        super();
        this.maxTraveledDistance = maxTraveledDistance;
        this.attack = attack;
        this.speed = speed;
        this.angle = angle;
        this.name = "Bullet";
        this.enemyState = EnemyState.HOSTILE;
        this.owner = owner;

        URL bullet = getClass().getResource(bulletPath);
        BufferedImage bulletImage;

        try {
            bulletImage = ImageIO.read(bullet);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        this.coordinates = new Coordinates((coordinates.topLeftCorner_x + coordinates.bottomRightCorner_x)/2, (coordinates.topLeftCorner_y + coordinates.bottomRightCorner_y)/2, bulletImage.getWidth(), bulletImage.getHeight());
        this.sprites.current = rotateImageByDegrees(bulletImage, angle);
    }
    @Override
    public void draw(Graphics graphics) {
        graphics.drawImage(sprites.current, (int) coordinates.screenX - 24, (int) coordinates.screenY - 24, null);
    }

    public void move(double diffSeconds, Player player) {
        this.invincibilityTimer += diffSeconds;
        // based on the degrees, move the bullet in that direction
        double radians = Math.toRadians(angle);
        double dx = Math.cos(radians) * speed;
        double dy = Math.sin(radians) * speed;

        coordinates.moveX(dx * this.speed * diffSeconds);
        coordinates.moveY(dy * this.speed * diffSeconds);

        this.traveledDistance += (int)Math.sqrt(dx*dx + dy*dy) ;
    }

    public Boolean isTraveledMax(){
        return this.traveledDistance > this.maxTraveledDistance;
    }
}
