package game.entities;

import game.Coordinates;
import game.GameObject;
import game.Quest;
import game.inventory.HealthElixir;
import gui.GameFrame;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class NPC extends GameObject {

    public BufferedImage sprite;
    public Coordinates coordinates;
    Quest quest;

    public NPC(Coordinates coordinates) {
        super();
        this.coordinates = coordinates;
        try {
            sprite = ImageIO.read(Objects.requireNonNull(getClass().getResource("/sprites/npcs/npc1_64.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        quest = new Quest(
                "Kill the octopussies",
                "Well done. Here is your reward.",
                500,
                new HealthElixir(
                        new Coordinates(0, 0, 0, 0)
                ),
                100
        );

    }

    public void interact() {
        //TODO if quest.completed == false, shows the initial text,
        // if quest.completed == true, shows the final text


    }

    public void draw(Graphics graphics) {
        if(coordinates.inScreen()) {
            graphics.drawImage(sprite, (int) coordinates.screenX, (int) coordinates.screenY, null);
        }
    }
}
