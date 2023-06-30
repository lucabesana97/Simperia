package game.entities;

import game.Coordinates;
import objState.EnemyState;

import javax.imageio.ImageIO;
import java.awt.*;
import java.net.URL;
import java.awt.image.BufferedImage;

import static helperFunctions.Utility.distanceBetweenCoordinates;


public class BossOctopus extends Octopus{

    public BossOctopus(int x, int y) {
        super(x, y,69);
        health = 500;
        speed = 3;
        this.experienceOnKill = 150;
        this.coordinates = new Coordinates(x, y, 293, 191);

        // Loading the sprites
        URL idle_down_1 = getClass().getResource("/sprites/enemies/480/Octopus-idle-down-1.png");
        URL idle_down_2 = getClass().getResource("/sprites/enemies/480/Octopus-idle-down-2.png");
        URL idle_up_1 = getClass().getResource("/sprites/enemies/480/Octopus-idle-up-1.png");
        URL idle_up_2 = getClass().getResource("/sprites/enemies/480/Octopus-idle-up-2.png");
        URL mad_down_1 = getClass().getResource("/sprites/enemies/480/Octopus-mad-down-1.png");
        URL mad_down_2 = getClass().getResource("/sprites/enemies/480/Octopus-mad-down-2.png");
        URL mad_up_1 = getClass().getResource("/sprites/enemies/480/Octopus-mad-up-1.png");
        URL mad_up_2 = getClass().getResource("/sprites/enemies/480/Octopus-mad-up-2.png");

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
        sprites.friendly_down[0] = image_mad_down_1;
        sprites.friendly_down[1] = image_mad_down_2;

        sprites.friendly_up = new BufferedImage[2];
        sprites.friendly_up[0] = image_mad_up_1;
        sprites.friendly_up[1] = image_mad_up_2;

        sprites.hostile_down = new BufferedImage[2];
        sprites.hostile_down[0] = image_idle_down_1;
        sprites.hostile_down[1] = image_idle_down_2;

        sprites.hostile_up = new BufferedImage[2];
        sprites.hostile_up[0] = image_idle_up_1;
        sprites.hostile_up[1] = image_idle_up_2;

        sprites.current = image_idle_down_1;

    }

    @Override
    public void draw(Graphics graphics) {
        super.draw(graphics);
    }

    @Override
    public void move(double diffSeconds, Player player) {
        this.invincibilityTimer += diffSeconds;
//        super.move(diffSeconds, player);
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
            this.enemyState = EnemyState.HOSTILE;
            return;
        }

        int distance = distanceBetweenCoordinates(this.coordinates, player.coordinates);


        // If the player is within x pixels, the enemy is hostile
        // If the player is within y pixels, the enemy is shooting
        // Otherwise the enemy is friendly
        if (distance < 900) {
            this.speed = 7;
            if (distance < 400 && this.timeSinceLastShot > this.shootingCooldown){
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
            this.runTowardsCoordinates(diffSeconds, player.coordinates);
            this.whereToMove = getNewCoordinates();
        } else if (this.enemyState == EnemyState.FRIENDLY){
//            runTowardsCoordinates(diffSeconds, this.whereToMove);
//            if (distanceBetweenCoordinates(this.coordinates, this.whereToMove) < 3){
//                this.whereToMove = getNewCoordinates();
//            }
        }else{
            if (this.timeSinceLastShot > this.shootingCooldown){
                this.timeSinceLastShot = 0;
                this.shoot();
            }
        }

    }


}
