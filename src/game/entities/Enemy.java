package game.entities;

import game.Movable;
import game.Sprites;
import game.inventory.InventoryItem;
import objState.EnemyState;
import objState.MovingState;
import game.Coordinates;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;

public class Enemy extends Entity implements Movable {
    InventoryItem loot;
    int experienceOnKill;
    int moneyOnKill;
    public EnemyState enemyState;
    public MovingState movingState;

    public Enemy() {
        super();
        this.enemyState = EnemyState.FRIENDLY;
        this.movingState = MovingState.STILL;
    }
    @Override
    public void draw(Graphics g) {
        BufferedImage image = sprites.current;
        g.drawImage(image, (int) coordinates.topLeftCorner_x, (int) coordinates.topLeftCorner_y, null);
    }

    public void move(double diffSeconds, Coordinates playerCoordinates) {
        //TODO add implementation of movement
        // check the distance between the player and the enemy
        // if the distance is less than 100 pixels, the enemy becomes hostile
        // if the enemy is hostile, it moves towards the player

//        if (enemyState == EnemyState.HOSTILE) {
//            if (movingState == MovingState.STILL) {
//                movingState = MovingState.;
//            }
//            if (movingState == MovingState.MOVING) {
//                if (coordinates.topLeftCorner_x < playerCoordinates.topLeftCorner_x) {
//                    coordinates.moveX(speed * diffSeconds);
//                }
//                if (coordinates.topLeftCorner_x > playerCoordinates.topLeftCorner_x) {
//                    coordinates.moveX(-speed * diffSeconds);
//                }
//                if (coordinates.topLeftCorner_y < playerCoordinates.topLeftCorner_y) {
//                    coordinates.moveY(speed * diffSeconds);
//                }
//                if (coordinates.topLeftCorner_y > playerCoordinates.topLeftCorner_y) {
//                    coordinates.moveY(-speed * diffSeconds);
//                }
//            }
//    }

    }
}

//    @Override
//    public void stopMovement() {
//
//    }
//
//    @Override
//    public void continueMovement() {
//
//    }

