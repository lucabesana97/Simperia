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
//        this.coordinates = new Coordinates(coordinates.topLeftCorner_x - 24, coordinates.topLeftCorner_y - 24, 37, 64); // (x, y, width, height
        try {
            sprite = ImageIO.read(Objects.requireNonNull(getClass().getResource("/sprites/npcs/npc1_64.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        quest = new Quest(
                "Hi plis help mi. I need 2 find mi dog. He is lost. I will give u 100 coins if u find him. He is in the forest. I'll give" +
                        " u a health elixir if u find him. And also 500 xp. My dog is scared, plis hurry.",
                "Thank u for finding mi dog. Here is ur reward.",
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
            graphics.drawImage(sprite, (int) coordinates.screenX - 24, (int) coordinates.screenY - 24, null);
        }
        // draw circle around player
//        graphics.setColor(Color.RED);
//        graphics.drawRect((int) coordinates.topLeftCorner_x, (int) coordinates.topLeftCorner_y, (int) coordinates.size_X,(int)  coordinates.size_Y);
    }
}
