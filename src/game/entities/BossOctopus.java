package game.entities;

import javax.imageio.ImageIO;
import java.awt.*;
import java.net.URL;
import java.awt.image.BufferedImage;


public class BossOctopus extends Octopus{

    public BossOctopus(int x, int y) {
        super(x, y);
        health = 1;
        speed = 3;

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

}
