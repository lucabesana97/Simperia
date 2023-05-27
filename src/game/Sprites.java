package game;

import java.awt.image.BufferedImage;

public class Sprites {
    public BufferedImage[] idle;
    public BufferedImage[] up;
    public BufferedImage[] down;
    public BufferedImage[] side;
    public BufferedImage[] attack;
    public BufferedImage dead;

    public BufferedImage current;

    public Sprites() {
//        this.side = new BufferedImage[1];
//        this.side[0] = side;
//        this.current = side;
    }

    //TODO add implementation of sprite changing
    /* I assume it can be done here. the problem is that the enemies have different positions than the player.
    Another sprite class can be used for that purpose. talk to Julia if there's too many problems accessing classes or
    with the animations.
     */


}
