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
                "んのﾚﾑ √ﾉﾑﾌ乇尺の.\n ｷﾉ刀ﾑﾚﾚﾘ 丂のﾶ乇の刀乇 ᄃのﾶ乇丂. ｲん乇ﾘ ｲののズ ﾶﾘ ᄃﾑ刀乇, ﾉ ᄃﾑ刀'ｲ ｷﾉムんｲ ｲん乇ﾶ. ᄃﾑ刀 ﾘのひ ん乇ﾚｱ ﾶ乇 尺乇ｲ尺ﾉ乇√乇 ﾉｲ? ﾉ'ﾚﾚ ムﾉ√乇 ﾘのひ 丂のﾶ乇ｲんﾉ刀ム ﾉ刀 乇ﾒᄃんﾑ刀ム乇.",
                "ｲんﾑ刀ズ ﾘのひ ｷの尺 ﾘのひ尺 ん乇ﾚｱ. ん乇尺乇 ﾉ丂 ﾘのひ尺 尺乇Wﾑ尺り.",
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
