package game.entities;

import game.Movable;
import game.inventory.InventoryItem;
import gui.GameFrame;
import main.Gameplay;
import objState.EnemyState;
import objState.MovingState;
import game.Coordinates;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Enemy extends Entity implements Movable {
    InventoryItem loot;
    int experienceOnKill;
    int moneyOnKill;
    public EnemyState enemyState; // Whether the enemy is friendly or hostile
    public MovingState movingState; // Direction the enemy is moving in (helpful for sprites)
    public Coordinates whereToMove; // Random coordinates to move to when roaming around
    public double screenX;
    public double screenY;

    public Enemy() {
        super();
        this.enemyState = EnemyState.FRIENDLY;
        this.movingState = MovingState.STILL;
    }
    @Override
    public void draw(Graphics g) {
        screenX = GameFrame.WIDTH/2 - (Gameplay.player.coordinates.topLeftCorner_x - coordinates.topLeftCorner_x);
        screenY = GameFrame.HEIGHT/2 - (Gameplay.player.coordinates.topLeftCorner_y - coordinates.topLeftCorner_y);
        if(screenX >= -coordinates.size_X &&
            screenX <= GameFrame.WIDTH + coordinates.size_X &&
            screenY >= -coordinates.size_Y &&
            screenY <= GameFrame.HEIGHT + coordinates.size_Y
        ) {
            BufferedImage image = sprites.current;
            g.drawImage(image, (int) screenX, (int)screenY, null);
        }
    }
    // Get a random coordinate to move to. This is used when the enemy is roaming around. It is calculated as a random position
    // within 150 pixels of the enemy's current position.
    protected Coordinates getNewCoordinates(){
        Random rand = new Random();
        int pixelsToMove = 150;
        int x = rand.nextInt(2 * pixelsToMove) - pixelsToMove + (int) (this.coordinates.topLeftCorner_x + this.coordinates.bottomRightCorner_x)/2;
        int y = rand.nextInt(2 * pixelsToMove) - pixelsToMove + (int) (this.coordinates.topLeftCorner_y + this.coordinates.bottomRightCorner_y)/2;
        return new Coordinates(x, y, 0, 0);
    }
    @Override
    public void die(){
        super.die();
        this.enemyState = EnemyState.DEAD;
    }

    public void move(double diffSeconds, Player player) {
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

