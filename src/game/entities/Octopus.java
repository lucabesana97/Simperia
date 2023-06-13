package game.entities;
import game.Coordinates;
import main.Game;
import main.Gameplay;
import objState.EnemyState;
import objState.MovingState;

import java.awt.*;
import java.lang.Math;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static helperFunctions.Utility.createFlipped;
import static helperFunctions.Utility.distanceBetweenCoordinates;

public class Octopus extends Enemy{
    public double shootingCooldown = 30;
    public double timeSinceLastShot = 30;
    public Octopus(int x, int y) {
        super();
        this.speed = 5;
        this.name = "Octopus";
        this.attack = 20;
        this.health = 45;
        this.coordinates = new Coordinates(x, y, 48, 31);
        this.whereToMove = getNewCoordinates(); // Random coordinates to move to when roaming around

        // Loading the sprites
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
//        for (Bullet bullet : this.bullets) {
//            bullet.draw(g);
//        }
    }

    @Override
    public void move(double diffSeconds, Player player) {
        super.move(diffSeconds, player);
        // Calculate the distance between the player and the enemy
//        Iterator<Bullet> iterator = Gameplay.enemyBullets.iterator();
//        while (iterator.hasNext()) {
//            Bullet bullet = iterator.next();
//            bullet.move(diffSeconds, player);
//            if (bullet.enemyState == EnemyState.DEAD) {
//                iterator.remove();
//            }
//        }
        // Recalculate which sprite to show
        this.sprites.calculateSprite(this.enemyState, this.movingState, diffSeconds);
        this.timeSinceLastShot += diffSeconds;


        if(this.isColliding(player)){
            if (this.timeSinceLastShot > this.shootingCooldown){
                this.timeSinceLastShot = 0;
                this.shoot();
            }
            this.attack(player);
            return;
        }

        int distance = distanceBetweenCoordinates(this.coordinates, player.coordinates);


        // If the player is within x pixels, the enemy is hostile
        // If the player is within y pixels, the enemy is shooting
        // Otherwise the enemy is friendly
        if (distance < 200) {
            this.speed = 7;
            if (distance < 150 && this.timeSinceLastShot > this.shootingCooldown){
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
            runTowardsCoordinates(diffSeconds, this.whereToMove);
            if (distanceBetweenCoordinates(this.coordinates, this.whereToMove) < 3){
                this.whereToMove = getNewCoordinates();
            }
        }else{
            if (this.timeSinceLastShot > this.shootingCooldown){
                this.timeSinceLastShot = 0;
                this.shoot();
            }
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

    public void shoot() {
        Gameplay.enemyBullets.add(new Bullet(0, this.coordinates, Bullet.ENEMY, 3, 6));
        Gameplay.enemyBullets.add(new Bullet(45, this.coordinates, Bullet.ENEMY, 3, 6));
        Gameplay.enemyBullets.add(new Bullet(90, this.coordinates, Bullet.ENEMY, 3, 6));
        Gameplay.enemyBullets.add(new Bullet(135, this.coordinates, Bullet.ENEMY, 3, 6));
        Gameplay.enemyBullets.add(new Bullet(180, this.coordinates, Bullet.ENEMY, 3, 6));
        Gameplay.enemyBullets.add(new Bullet(225, this.coordinates, Bullet.ENEMY, 3, 6));
        Gameplay.enemyBullets.add(new Bullet(270, this.coordinates, Bullet.ENEMY, 3, 6));
        Gameplay.enemyBullets.add(new Bullet(315, this.coordinates, Bullet.ENEMY, 3, 6));
    }

}
