package game.entities;
import game.Coordinates;
import objState.EnemyState;
import objState.MovingState;

import java.awt.*;
import java.lang.Math;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;

import static helperFunctions.Utility.distanceBetweenCoordinates;

public class Maurice extends Enemy{
    public double attackingCooldown = 30;
    public double timeSinceLastAttack = 30;
    public Maurice(int x, int y) {
        super();
        this.speed = 5;
        this.name = "Maurice";
        this.attack = 20;
        this.health = 70;
        this.coordinates = new Coordinates(x, y, 37, 64);
        this.whereToMove = getNewCoordinates(); // Random coordinates to move to when roaming around

        // Loading the sprites
        URL idle_down_1 = getClass().getResource("/sprites/enemies/monster/Monster-down-1.png");
        URL idle_down_2 = getClass().getResource("/sprites/enemies/monster/Monster-down-2.png");
        URL idle_up_1 = getClass().getResource("/sprites/enemies/monster/Monster-up-1.png");
        URL idle_up_2 = getClass().getResource("/sprites/enemies/monster/Monster-up-2.png");
        URL mad_down_1 = getClass().getResource("/sprites/enemies/monster/Monster-down-1.png");
        URL mad_down_2 = getClass().getResource("/sprites/enemies/monster/Monster-down-2.png");
        URL mad_up_1 = getClass().getResource("/sprites/enemies/monster/Monster-up-1.png");
        URL mad_up_2 = getClass().getResource("/sprites/enemies/monster/Monster-up-2.png");

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

        sprites.friendly_down = new BufferedImage[2];
        sprites.friendly_down[0] = image_idle_down_1;
        sprites.friendly_down[1] = image_idle_down_2;

        sprites.friendly_up = new BufferedImage[2];
        sprites.friendly_up[0] = image_idle_up_1;
        sprites.friendly_up[1] = image_idle_up_2;

        sprites.hostile_down = new BufferedImage[2];
        sprites.hostile_down[0] = image_mad_down_1;
        sprites.hostile_down[1] = image_mad_down_2;

        sprites.hostile_up = new BufferedImage[2];
        sprites.hostile_up[0] = image_mad_up_1;
        sprites.hostile_up[1] = image_mad_up_2;

        sprites.current = image_idle_down_1;
    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);
    }

    @Override
    public void move(double diffSeconds, Player player) {
        super.move(diffSeconds, player);
        // Calculate the distance between the player and the enemy
        // Recalculate which sprite to show
        this.sprites.calculateSprite(this.enemyState, this.movingState, diffSeconds);
        this.timeSinceLastAttack += diffSeconds;


        if(this.isColliding(player)){
            this.attack(player);
            return;
        }

        int distance = distanceBetweenCoordinates(this.coordinates, player.coordinates);

        // If the player is within x pixels, the enemy is hostile
        // If the player is within y pixels, the enemy is shooting
        // Otherwise the enemy is friendly
        if (distance < 700) {
            this.speed = 9;
            if (distance < 2 && this.timeSinceLastAttack > this.attackingCooldown){
                this.enemyState = EnemyState.ATTACKING;
            }else{
                this.enemyState = EnemyState.HOSTILE;
            }
        } else {
            this.speed = 2;
            this.enemyState = EnemyState.FRIENDLY;
        }

        // If the enemy is hostile, run towards the player
        // If the enemy is friendly, run towards a random point

        if (this.enemyState == EnemyState.HOSTILE){
            runTowardsCoordinates(diffSeconds, player.coordinates);
            this.whereToMove = getNewCoordinates();
        } else if (this.enemyState == EnemyState.FRIENDLY){
//            runTowardsCoordinates(diffSeconds, this.whereToMove);
//            if (distanceBetweenCoordinates(this.coordinates, this.whereToMove) < 3){
//                this.whereToMove = getNewCoordinates();
//            }
        }else{
        }

    }


    private void runTowardsCoordinates(double diffSeconds, Coordinates goalCoordinates){
        int aCenterX = (int) (this.coordinates.topLeftCorner_x + this.coordinates.bottomRightCorner_x) / 2;
        int aCenterY = (int) (this.coordinates.topLeftCorner_y + this.coordinates.bottomRightCorner_y) / 2;

        int bCenterX = (int) (goalCoordinates.topLeftCorner_x + goalCoordinates.bottomRightCorner_x) / 2;
        int bCenterY = (int) (goalCoordinates.topLeftCorner_y + goalCoordinates.bottomRightCorner_y) / 2;

        double xDistance = aCenterX - bCenterX;
        double yDistance = aCenterY - bCenterY;
        double totalDistance = Math.sqrt(Math.pow(xDistance, 2) + Math.pow(yDistance, 2));
        double xSpeed = -1 * (xDistance / totalDistance);
        double ySpeed = -1 * (yDistance / totalDistance);

        calculateOrientation(xSpeed, ySpeed);

        this.coordinates.moveX(xSpeed * this.speed * diffSeconds);
        this.coordinates.moveY(ySpeed * this.speed * diffSeconds);
    }

    private void calculateOrientation(double xSpeed, double ySpeed) {
        if (xSpeed < 0 && ySpeed < 0) {
            this.movingState = MovingState.UP_LEFT;
        } else if (xSpeed > 0 && ySpeed < 0) {
            this.movingState = MovingState.UP_RIGHT;
        } else if (xSpeed < 0 && ySpeed > 0) {
            this.movingState = MovingState.DOWN_LEFT;
        } else if (xSpeed > 0 && ySpeed > 0) {
            this.movingState = MovingState.DOWN_RIGHT;
        }
    }

}
