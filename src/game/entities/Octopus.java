package game.entities;
import game.Coordinates;
import objState.EnemyState;

import java.lang.Math;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;

import static helperFunctions.utility.distanceBetweenCoordinates;

public class Octopus extends Enemy{
    public Octopus() {
        super();
        this.speed = 10;
        this.name = "Octopus";
        this.coordinates = new Coordinates(300, 650, 48, 31);

        URL idle_down_1 = getClass().getResource("/sprites/enemies/Octopus-idle-down-1.png");
        URL idle_down_2 = getClass().getResource("/sprites/enemies/Octopus-idle-down-2.png");
        URL idle_up_1 = getClass().getResource("/sprites/enemies/Octopus-idle-up-1.png");
        URL idle_up_2 = getClass().getResource("/sprites/enemies/Octopus-idle-up-2.png");
        URL mad_down_1 = getClass().getResource("/sprites/enemies/Octopus-mad-down-1.png");
        URL mad_down_2 = getClass().getResource("/sprites/enemies/Octopus-mad-down-2.png");
        URL mad_up_1 = getClass().getResource("/sprites/enemies/Octopus-mad-up-1.png");
        URL mad_up_2 = getClass().getResource("/sprites/enemies/Octopus-mad-up-2.png");

        BufferedImage image_idle_down_1;
        BufferedImage image_idle_down_2;
        BufferedImage image_idle_up_1;
        BufferedImage image_idle_up_2;
        BufferedImage image_mad_down_1;
        BufferedImage image_mad_down_2;
        BufferedImage image_mad_up_1;
        BufferedImage image_mad_up_2;

        try {
            image_idle_down_1 = ImageIO.read(idle_down_1);
            image_idle_down_2 = ImageIO.read(idle_down_2);
            image_idle_up_1 = ImageIO.read(idle_up_1);
            image_idle_up_2 = ImageIO.read(idle_up_2);
            image_mad_down_1 = ImageIO.read(mad_down_1);
            image_mad_down_2 = ImageIO.read(mad_down_2);
            image_mad_up_1 = ImageIO.read(mad_up_1);
            image_mad_up_2 = ImageIO.read(mad_up_2);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        sprites.idle = new BufferedImage[4];
        sprites.idle[0] = image_idle_down_1;
        sprites.idle[1] = image_idle_down_2;
        sprites.idle[2] = image_idle_up_1;
        sprites.idle[3] = image_idle_up_2;

        sprites.up = new BufferedImage[4];
        sprites.up[0] = image_idle_up_1;
        sprites.up[1] = image_idle_up_2;
        sprites.up[2] = image_mad_up_1;
        sprites.up[3] = image_mad_up_2;

        sprites.down = new BufferedImage[4];
        sprites.down[0] = image_idle_down_1;
        sprites.down[1] = image_idle_down_2;
        sprites.down[2] = image_mad_down_1;
        sprites.down[3] = image_mad_down_2;

        sprites.current = sprites.idle[0];
    }

    public void move(double diffSeconds, Coordinates playerCoordinates) {
        //TODO edit so player moves with keys pressed
        int distance = distanceBetweenCoordinates(this.coordinates, playerCoordinates);

        if (distance < 2) {
            if (distance < 2){
                this.enemyState = EnemyState.ATTACKING;
            }else{
                this.enemyState = EnemyState.HOSTILE;
            }
        } else {
            this.enemyState = EnemyState.FRIENDLY;
        }

        if (this.enemyState != EnemyState.HOSTILE){
            runTowardsPlayer(diffSeconds, playerCoordinates);
        }
    }

    public void runTowardsPlayer(double diffSeconds, Coordinates playerCoordinates){

        int aCenterX = (int) (this.coordinates.topLeftCorner_x + this.coordinates.bottomRightCorner_x) / 2;
        int aCenterY = (int) (this.coordinates.topLeftCorner_y + this.coordinates.bottomRightCorner_y) / 2;

        int bCenterX = (int) (playerCoordinates.topLeftCorner_x + playerCoordinates.bottomRightCorner_x) / 2;
        int bCenterY = (int) (playerCoordinates.topLeftCorner_y + playerCoordinates.bottomRightCorner_y) / 2;

        double xDistance = aCenterX - bCenterX;
        double yDistance = aCenterY - bCenterY;
        double totalDistance = Math.sqrt(Math.pow(xDistance, 2) + Math.pow(yDistance, 2));
        double xSpeed = -1 * (xDistance / totalDistance);
        double ySpeed = -1 * (yDistance / totalDistance);

        this.coordinates.moveX(xSpeed * this.speed * diffSeconds);
        this.coordinates.moveY(ySpeed * this.speed * diffSeconds);
    }
}
