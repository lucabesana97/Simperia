package game;

import objState.EnemyState;
import objState.MovingState;

import java.awt.image.BufferedImage;

public class Sprites {
    public BufferedImage[] friendly_idle;
    public BufferedImage[] friendly_up_left;
    public BufferedImage[] friendly_up_right;
    public BufferedImage[] friendly_down_left;
    public BufferedImage[] friendly_down_right;
    public BufferedImage[] hostile_idle;
    public BufferedImage[] hostile_up_left;
    public BufferedImage[] hostile_up_right;
    public BufferedImage[] hostile_down_left;
    public BufferedImage[] hostile_down_right;
    public BufferedImage[] attack;
    public BufferedImage dead;

    public BufferedImage current;
    public double timeSinceLastFrame;
    public double timePerFrame;
    public MovingState lastMovingState;
    public int lastIndexFrame;

    public Sprites() {
        this.timeSinceLastFrame = 0;
        this.timePerFrame = 1.75;
        this.lastMovingState = MovingState.STILL;
        this.lastIndexFrame = 1;

        this.current = null;
    }
//        this.side = new BufferedImage[1];
//        this.side[0] = side;
//        this.current = side;


    // calculateSprite is called every frame from the Entity class (Octopus for example)
    // It is responsible for changing the sprite based on the current movingState and time passed since last changed sprite
    public void calculateSprite(EnemyState enemyState, MovingState movingState, double diffSeconds) {
        if (movingState == lastMovingState) {
            timeSinceLastFrame += diffSeconds;
            if (timeSinceLastFrame > timePerFrame) {
                if (movingState == MovingState.STILL) {
                    chooseSprite(enemyState, friendly_idle, hostile_idle);
                } else if (movingState == MovingState.UP_LEFT) {
                    chooseSprite(enemyState, friendly_up_left, hostile_up_left);
                } else if (movingState == MovingState.UP_RIGHT) {
                    chooseSprite(enemyState, friendly_up_right, hostile_up_right);
                } else if (movingState == MovingState.DOWN_LEFT) {
                    chooseSprite(enemyState, friendly_down_left, hostile_down_left);
                } else if (movingState == MovingState.DOWN_RIGHT) {
                    chooseSprite(enemyState, friendly_down_right, hostile_down_right);
                }
                timeSinceLastFrame = 0;
            }
        } else {
//            timeSinceLastFrame = 0;
            if (movingState == MovingState.STILL) {
                if(enemyState == EnemyState.FRIENDLY){
                    current = friendly_idle[0];
                } else {
                    current = hostile_idle[0];
                }
            } else if (movingState == MovingState.UP_LEFT) {
                if(enemyState == EnemyState.FRIENDLY){
                    current = friendly_up_left[0];
                } else {
                    current = hostile_up_left[0];
                }
            } else if (movingState == MovingState.UP_RIGHT) {
                if(enemyState == EnemyState.FRIENDLY){
                    current = friendly_up_right[0];
                } else {
                    current = hostile_up_right[0];
                }
            } else if (movingState == MovingState.DOWN_LEFT) {
                if(enemyState == EnemyState.FRIENDLY){
                    current = friendly_down_left[0];
                } else {
                    current = hostile_down_left[0];
                }
            } else if (movingState == MovingState.DOWN_RIGHT) {
                if(enemyState == EnemyState.FRIENDLY){
                    current = friendly_down_right[0];
                } else {
                    current = hostile_down_right[0];
                }
            }
            lastMovingState = movingState;
        }
    }

    private void chooseSprite(EnemyState enemyState, BufferedImage[] friendlyDownRight, BufferedImage[] hostileDownRight) {
        if (lastIndexFrame == 0) {
            if (enemyState == EnemyState.FRIENDLY) {
                current = friendlyDownRight[1];
            } else {
                current = hostileDownRight[1];
            }
            lastIndexFrame = 1;
        } else {
            if (enemyState == EnemyState.FRIENDLY) {
                current = friendlyDownRight[0];
            } else {
                current = hostileDownRight[0];
            }
            lastIndexFrame = 0;
        }
    }


    //TODO add implementation of sprite changing
    /* I assume it can be done here. the problem is that the enemies have different positions than the player.
    Another sprite class can be used for that purpose. talk to Julia if there's too many problems accessing classes or
    with the animations.
     */


}
