package game;

import objState.MovingState;

import java.awt.image.BufferedImage;

public class Sprites {
    public BufferedImage[] idle;
    public BufferedImage[] up_left;
    public BufferedImage[] up_right;
    public BufferedImage[] down_left;
    public BufferedImage[] down_right;
    public BufferedImage[] attack;
    public BufferedImage dead;

    public BufferedImage current;
    public double timeSinceLastFrame;
    public double timePerFrame;
    public MovingState lastMovingState;
    public int lastIndexFrame;

    public Sprites() {
        this.timeSinceLastFrame = 0;
        this.timePerFrame = 0.5;
        this.lastMovingState = MovingState.STILL;
        this.lastIndexFrame = 1;

        this.current = null;
    }
//        this.side = new BufferedImage[1];
//        this.side[0] = side;
//        this.current = side;


    // calculateSprite is called every frame from the Entity class (Octopus for example)
    // It is responsible for changing the sprite based on the current movingState and time passed since last changed sprite
    public void calculateSprite(MovingState movingState, double diffSeconds) {
        if (movingState == lastMovingState) {
            timeSinceLastFrame += diffSeconds;
            if (timeSinceLastFrame > timePerFrame) {
                if (movingState == MovingState.STILL) {
                    if (lastIndexFrame == 0) {
                        current = idle[1];
                        lastIndexFrame = 1;
                    } else {
                        current = idle[0];
                        lastIndexFrame = 0;
                    }
                } else if (movingState == MovingState.UP_LEFT) {
                    if (lastIndexFrame == 0) {
                        current = up_left[1];
                        lastIndexFrame = 1;
                    } else {
                        current = up_left[0];
                        lastIndexFrame = 0;
                    }
                } else if (movingState == MovingState.UP_RIGHT) {
                    if (lastIndexFrame == 0) {
                        current = up_right[1];
                        lastIndexFrame = 1;
                    } else {
                        current = up_right[0];
                        lastIndexFrame = 0;
                    }
                } else if (movingState == MovingState.DOWN_LEFT) {
                    if (lastIndexFrame == 0) {
                        current = down_left[1];
                        lastIndexFrame = 1;
                    } else {
                        current = down_left[0];
                        lastIndexFrame = 0;
                    }
                } else if (movingState == MovingState.DOWN_RIGHT) {
                    if (lastIndexFrame == 0) {
                        current = down_right[1];
                        lastIndexFrame = 1;
                    } else {
                        current = down_right[0];
                        lastIndexFrame = 0;
                    }
                }
                timeSinceLastFrame = 0;
            }
        } else {
//            timeSinceLastFrame = 0;
            if (movingState == MovingState.STILL) {
                current = idle[0];
            } else if (movingState == MovingState.UP_LEFT) {
                current = up_left[0];
            } else if (movingState == MovingState.UP_RIGHT) {
                current = up_right[0];
            } else if (movingState == MovingState.DOWN_LEFT) {
                current = down_left[0];
            } else if (movingState == MovingState.DOWN_RIGHT) {
                current = down_right[0];
            }
            lastMovingState = movingState;
        }
    }


    //TODO add implementation of sprite changing
    /* I assume it can be done here. the problem is that the enemies have different positions than the player.
    Another sprite class can be used for that purpose. talk to Julia if there's too many problems accessing classes or
    with the animations.
     */


}
