package game;

import objState.EnemyState;
import objState.MovingState;
import static helperFunctions.Utility.createFlipped;
import java.awt.image.BufferedImage;

public class Sprites {
    public BufferedImage[] friendly_down;
    public BufferedImage[] friendly_up;
    public BufferedImage[] hostile_down;
    public BufferedImage[] hostile_up;
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



    // calculateSprite is called every frame from the Entity class (Octopus for example)
    // It is responsible for changing the sprite based on the current movingState and time passed since last changed sprite
    public void calculateSprite(EnemyState enemyState, MovingState movingState, double diffSeconds) {
        if (movingState == lastMovingState) {
            timeSinceLastFrame += diffSeconds;

            if (timeSinceLastFrame > timePerFrame) {
                this.lastIndexFrame = (this.lastIndexFrame + 1) % this.friendly_down.length;
                if (movingState == MovingState.STILL) {
                    if (enemyState == EnemyState.FRIENDLY) {
                        this.current = this.friendly_down[this.lastIndexFrame];
                    } else {
                        this.current = this.hostile_down[this.lastIndexFrame];
                    }
                } else if (movingState == MovingState.UP_LEFT) {
                    if (enemyState == EnemyState.FRIENDLY) {
                        this.current = this.friendly_up[this.lastIndexFrame];
                    } else {
                        this.current = this.hostile_up[this.lastIndexFrame];
                    }
                } else if (movingState == MovingState.UP_RIGHT) {
                    if (enemyState == EnemyState.FRIENDLY) {
                        this.current = createFlipped(this.friendly_up[this.lastIndexFrame]);
                    } else {
                        this.current = createFlipped(this.hostile_up[this.lastIndexFrame]);
                    }
                } else if (movingState == MovingState.DOWN_LEFT) {
                    if (enemyState == EnemyState.FRIENDLY) {
                        this.current = this.friendly_down[this.lastIndexFrame];
                    } else {
                        this.current = this.hostile_down[this.lastIndexFrame];
                    }
                } else if (movingState == MovingState.DOWN_RIGHT) {
                    if (enemyState == EnemyState.FRIENDLY) {
                        this.current = createFlipped(this.friendly_down[this.lastIndexFrame]);
                    } else {
                        this.current = createFlipped(this.hostile_down[this.lastIndexFrame]);
                    }
                }
                timeSinceLastFrame = 0;
            }

        } else {
            if (movingState == MovingState.STILL) {
                if(enemyState == EnemyState.FRIENDLY){
                    current = friendly_down[0];
                } else {
                    current = hostile_down[0];
                }
            } else if (movingState == MovingState.UP_LEFT) {
                if(enemyState == EnemyState.FRIENDLY){
                    current = friendly_up[0];
                } else {
                    current = hostile_up[0];
                }
            } else if (movingState == MovingState.UP_RIGHT) {
                if(enemyState == EnemyState.FRIENDLY){
                    current = createFlipped(friendly_up[0]);
                } else {
                    current = createFlipped(hostile_up[0]);
                }
            } else if (movingState == MovingState.DOWN_LEFT) {
                if(enemyState == EnemyState.FRIENDLY){
                    current = friendly_down[0];
                } else {
                    current = hostile_down[0];
                }
            } else if (movingState == MovingState.DOWN_RIGHT) {
                if(enemyState == EnemyState.FRIENDLY){
                    current = createFlipped(friendly_down[0]);
                } else {
                    current = createFlipped(hostile_down[0]);
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

    public void speedUp(){
        timePerFrame = 1.4;
    }

    public void speedDown(){
        timePerFrame = 1.75;
    }

}
