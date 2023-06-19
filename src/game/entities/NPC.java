package game.entities;

import game.Coordinates;
import game.GameObject;
import game.Quest;
import game.inventory.HealthElixir;
import game.inventory.ItemStack;
import gui.GameFrame;
import main.Gameplay;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class NPC extends GameObject {

    public BufferedImage sprite;
    public Quest quest;
    public boolean rewardGiven = false;
    private boolean npcDone = false;
    public boolean interacting = false;

    public NPC(Coordinates coordinates) {
        super(coordinates);
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

    public String interact() {
        this.interacting = true;
        String toBeReturned = "";
        if (quest.completed) {
            if (!rewardGiven) {
                rewardGiven = true;
                Gameplay.player.gainXp(quest.xpReward);
                Gameplay.player.coins += quest.coinsReward;
                Gameplay.inventory.addStack(new ItemStack(quest.itemReward, 1, true));
                toBeReturned = quest.finalText;
            }else if (rewardGiven && !npcDone){
                toBeReturned = quest.finalText;
            }else if (npcDone){
                toBeReturned = "I have nothing more to say to you.";
            }
        }else{
            toBeReturned = quest.initialText;
        }
        return toBeReturned;

    }

    public void stopInteracting(){
        this.interacting = false;
        if (quest.completed && rewardGiven) {
            npcDone = true;
        }
    }

    public void draw(Graphics graphics) {
        if(coordinates.inScreen()) {
            graphics.drawImage(sprite, (int) coordinates.screenX, (int) coordinates.screenY, null);
        }
        // draw circle around player
//        graphics.setColor(Color.RED);
//        graphics.drawRect((int) coordinates.topLeftCorner_x, (int) coordinates.topLeftCorner_y, (int) coordinates.size_X,(int)  coordinates.size_Y);
    }
}
