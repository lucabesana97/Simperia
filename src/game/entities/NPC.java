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

    public int screenX = GameFrame.WIDTH / 2 - 24;
    public int screenY = GameFrame.HEIGHT / 2 - 24;
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
                        true,
                        5,
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
        int x = (int) (coordinates.topLeftCorner_x);
        int y = (int) (coordinates.topLeftCorner_y);
        graphics.drawImage(sprite, screenX, screenY, null);

    }
}
