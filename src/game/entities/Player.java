package game.entities;

import game.Coordinates;
import game.Movable;
import game.Quest;
import game.inventory.ItemStack;
import gui.GameFrame;
import helperFunctions.Utility;
import main.Gameplay;
import objState.FightState;
import objState.MovingState;
import output.Sound;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static helperFunctions.Utility.createFlipped;
import static helperFunctions.Utility.rotateImageByDegrees;

public class Player extends Entity implements Movable {
    public static final int SWORD = 0;
    public static final int GUN = 1;
    public static final double RELOAD_TIMER = 2;
    public static final double SWITCH_WEAPON_TIMER = 2;
    public int coins;
    public int xp;
    public int xpToNextLevel;
    public int levelXp;
    public int currentWeapon = GUN;
    public double switchCounter = 0;
    public FightState switchState = FightState.READY;
    public FightState reloadState = FightState.READY;
    int slashRange;
    public MovingState xState;
    public MovingState yState;
    public int screenX = GameFrame.WIDTH / 2 - 24;
    public int screenY = GameFrame.HEIGHT / 2 - 24;
    public int mapHeight;
    public int mapWidth;
    public boolean sword;
    public FightState shootState;
    private double shootCounter = 0;
    private double switchWeaponCounter = 0;


    private BufferedImage[] spritesDown;
    private BufferedImage[] spritesUp;
    private BufferedImage[] spritesSide;
    public int angle;
    private Sound footstep = new Sound();
    private double footstepCounter = 3.5;
    private BufferedImage[] spritesAttackSlash;
    public MovingState previousStateX, previousStateY;
    public List<Quest> quests = new ArrayList<>();

    public Player() {
        spritesDown = loadSprites("/sprites/player/Down-gun-", 2);
        spritesUp = loadSprites("/sprites/player/Up-gun-", 2);
        spritesSide = loadSprites("/sprites/player/Side-gun-", 4);
        spritesAttackSlash = loadSprites("/sprites/player/attack/Attack-glare-", 4);
        URL url = getClass().getResource("/sprites/player/Down-gun-1.png");
        BufferedImage image;
        try{
            image = ImageIO.read(url);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        speed = 30;
        attack = 25;
        xState = MovingState.STILL;
        previousStateX = previousStateY = MovingState.STILL;
        yState = MovingState.STILL;
        shootState = FightState.READY;
        slashRange = 65;
        sprites.current = image;
        this.level = 1;
        this.name = "Player";
        this.coins = 0;
        this.xp = 0;
        levelXp = (int)((double)(level * level)/2 * 100);
        this.xpToNextLevel = levelXp;
        mapHeight = Gameplay.map.mapImage.getHeight();
        mapWidth = Gameplay.map.mapImage.getWidth();
        this.coordinates = new Coordinates(186,340 , 29, 48);
    }

    public void draw(Graphics graphics) {
        BufferedImage currentSprite;

        if (xState == MovingState.LEFT) {
            currentSprite = createFlipped(spritesSide[(int) (System.currentTimeMillis() / 200 % 4)]);
            sprites.current= currentSprite;
        } else if (xState == MovingState.RIGHT) {
            currentSprite = spritesSide[(int) (System.currentTimeMillis() / 200 % 4)];
            sprites.current= currentSprite;
        } else if (yState == MovingState.DOWN) {
            currentSprite = spritesDown[(int) (System.currentTimeMillis() / 200 % 2)];
            sprites.current= currentSprite;
        } else if (yState == MovingState.UP) {
            currentSprite = spritesUp[(int) (System.currentTimeMillis() / 200 % 2)];
            sprites.current= currentSprite;
        } else {
            currentSprite = sprites.current;
        }


        if (closeToLeftWall()) {
            screenX = (int)coordinates.topLeftCorner_x - 24;
        } else if (closeToRightWall()) {
            screenX = (int)(coordinates.topLeftCorner_x - mapWidth + GameFrame.WIDTH) - 24;
        } else {
            screenX = GameFrame.WIDTH / 2 - 24;
        }
        if (closeToUpWall()) {
            screenY = (int)coordinates.topLeftCorner_y - 24;
        } else if (closeToDownWall()) {
            screenY = (int)(coordinates.topLeftCorner_y - mapHeight + GameFrame.HEIGHT) - 24;
        } else {
            screenY = GameFrame.HEIGHT / 2 - 24;
        }

        graphics.drawImage(currentSprite, screenX, screenY, null);

        if(shootState == FightState.RELOADING && currentWeapon == SWORD) {

            int offsetX = 0;
            int offsetY = 0;

            if (previousStateX == MovingState.LEFT) {
                offsetX = -15;
            } else if (previousStateX == MovingState.RIGHT) {
                offsetX = 20;
            }

            if (previousStateY == MovingState.UP) {
                offsetY = -15;
            } else if (previousStateY == MovingState.DOWN) {
                offsetY = 30;
            }

            graphics.drawImage(rotateImageByDegrees(spritesAttackSlash[(int) (switchCounter*4/RELOAD_TIMER+1)], angle),
                    screenX + offsetX, screenY + offsetY, null);

        }

        System.out.println("x: " + coordinates.topLeftCorner_x + " y: " + coordinates.topLeftCorner_y);


    }

    @Override
    public void move(double diffSeconds) {
        //System.out.println(coordinates.topLeftCorner_x + "\t" + (int)(mapWidth - GameFrame.WIDTH/2));
        setShooting(diffSeconds);
        this.invincibilityTimer += diffSeconds;

        double moveBy = diffSeconds * speed;

        double lastX = coordinates.topLeftCorner_x;
        double lastY = coordinates.topLeftCorner_y;

        if(xState != MovingState.STILL && yState != MovingState.STILL){
            moveBy *= Math.sqrt(2) / 2;
            coordinates.moveX((xState == MovingState.RIGHT) ? moveBy : -moveBy);
            if(Gameplay.map.mapCollision(this)){
                coordinates.topLeftCorner_x = lastX;
            }
            coordinates.moveY((yState == MovingState.DOWN) ? moveBy : -moveBy);
            if(Gameplay.map.mapCollision(this)){
                coordinates.topLeftCorner_y = lastY;
            }
        } else if (xState != MovingState.STILL){
            coordinates.moveX((xState == MovingState.RIGHT) ? moveBy : -moveBy);
            if(Gameplay.map.mapCollision(this)){
                coordinates.topLeftCorner_x = lastX;
            }
        } else if (yState != MovingState.STILL){
            coordinates.moveY((yState == MovingState.DOWN) ? moveBy : -moveBy);
            if(Gameplay.map.mapCollision(this)){
                coordinates.topLeftCorner_y = lastY;
            }
        }

        if(xState == MovingState.STILL && yState == MovingState.STILL) {footstepCounter = 3.5;}
        else {
            previousStateX = xState;
            previousStateY = yState;

            if (footstepCounter <= 0) {
                if (!Gameplay.isMuted) {
                    footstep.playSoundEffect(6);
                }
                footstepCounter = 3.5;
            } else {
                footstepCounter -= diffSeconds;
                //System.out.print(footstepCounter + "\t");
            }
        }

        if (coordinates.topLeftCorner_x <= 0) {
            coordinates.topLeftCorner_x = 0;
        }
        if (coordinates.topLeftCorner_y <= 0) {
            coordinates.topLeftCorner_y = 0;
        }
        if (coordinates.topLeftCorner_x > mapWidth) {
            coordinates.topLeftCorner_x = mapWidth;
        }
        if (coordinates.topLeftCorner_y > mapHeight) {
            coordinates.topLeftCorner_y = mapHeight;
        }
        setSwitching(diffSeconds);
        //System.out.println(switchCounter + "\t" + switchState);
    }

    private BufferedImage[] loadSprites(String prefix, int count) {
        BufferedImage[] sprites = new BufferedImage[count];
        try {
            for (int i = 0; i < count; i++) {
                URL url = getClass().getResource(prefix + (i + 1) + ".png");
                sprites[i] = ImageIO.read(url);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return sprites;
    }

    public void teleport(Warp warp){
        coordinates.topLeftCorner_x = warp.exit.topLeftCorner_x;
        coordinates.topLeftCorner_y = warp.exit.topLeftCorner_y;
    }

    public void loadSword() {
        spritesDown = loadSprites("/sprites/player/Down-sword-", 2);
        spritesSide = loadSprites("/sprites/player/Side-sword-", 4);
        spritesUp = loadSprites("/sprites/player/Up-sword-", 2);
    }

    public void loadGun() {
        spritesDown = loadSprites("/sprites/player/Down-gun-", 2);
        spritesSide = loadSprites("/sprites/player/Side-gun-", 4);
        spritesUp = loadSprites("/sprites/player/Up-gun-", 2);
    }

    @Override
    public void gainXp(int xp){
        this.xp += xp;
        if(this.xp >= xpToNextLevel){

            int xpLeft = this.xp - xpToNextLevel;
            this.xp = 0;

            levelUp(xpLeft);
        }
    }

    public void levelUp(int xpLeft){
        level++;
        levelXp = (level * level)/2 * 100;
        xpToNextLevel = levelXp;
        gainXp(xpLeft);
    }

    public boolean isColliding(ItemStack itemStack){
        return this.coordinates.intersects(itemStack.item.coordinates);
    }

    public boolean isColliding(NPC npc){
        return this.coordinates.intersects(npc.coordinates);
    }



    private void setShooting(double diffSeconds) {
        if(shootState == FightState.RELOADING && shootCounter < RELOAD_TIMER) {
            shootCounter += diffSeconds;
        }
        else if (shootState == FightState.RELOADING && shootCounter >= RELOAD_TIMER) {
            shootCounter = 0;
            shootState = FightState.READY;
        }
    }

    private void setSwitching(double diffSeconds) {
        if(switchState == FightState.RELOADING && shootCounter < RELOAD_TIMER) {
            switchCounter += diffSeconds;
        }
        else if (switchState == FightState.RELOADING && shootCounter >= RELOAD_TIMER) {
            shootCounter = 0;
            switchState = FightState.READY;
        }
        switchWeaponCounter += diffSeconds;
        if (switchWeaponCounter >= SWITCH_WEAPON_TIMER) {
            reloadState = FightState.READY;
        }else{
            reloadState = FightState.RELOADING;
        }
    }
    public void switchWeapon(){
        if(reloadState == FightState.READY){
            switchWeaponCounter = 0;
            if (currentWeapon == Player.GUN) {
                currentWeapon = Player.SWORD;
                loadSword();
            } else if (currentWeapon == Player.SWORD) {
                currentWeapon = Player.GUN;
                loadGun();
            }
        }
    }
    public boolean inSlashRange(Enemy enemy) {
        if (Utility.distanceBetweenCoordinates(coordinates, enemy.coordinates) <= slashRange) {
            double alpha;
            if (enemy.coordinates.centerY == coordinates.centerY) {
                alpha = 1.6;
            } else {
                alpha = -Math.atan2(enemy.coordinates.centerY - coordinates.centerY, enemy.coordinates.centerX - coordinates.centerX);
            }
            switch (previousStateX) {
                case RIGHT:
                    switch (previousStateY) {
                        case UP:
                            if (alpha <= Math.PI / 2 && alpha >= 0) return true;
                            break;
                        case DOWN:
                            if (alpha >= -Math.PI / 2 && alpha <= 0) return true;
                            break;
                        case STILL:
                            if (alpha <= Math.PI / 4 && alpha >= -Math.PI / 4) return true;
                            break;
                    }
                    break;
                case LEFT:
                    switch (previousStateY) {
                        case UP:
                            if (alpha <= Math.PI && alpha >= Math.PI / 2) return true;
                            break;
                        case DOWN:
                            if (alpha <= -Math.PI / 2 && alpha >= -Math.PI) return true;
                            break;
                        case STILL:
                            if (alpha >= Math.PI *3 / 4 || alpha <= -Math.PI * 3 / 4) return true;
                            break;
                    }
                    break;
                case STILL:
                    switch (previousStateY) {
                        case UP:
                            if (alpha <= Math.PI * 3 / 4 && alpha >= Math.PI / 4) return true;
                            break;
                        case DOWN:
                        case STILL:
                            if (alpha >= -Math.PI * 3 / 4 && alpha <= -Math.PI / 4) return true;
                            break;
                    }
                    break;
            }
        }
        return false;
    }

    public void sprint() {
        speed = 45;
        sprites.speedUp();
    }
    public void slowDown(){
        speed = 30;
        sprites.speedDown();
    }

    /*public void pickUp(Item pickup){
        for(Item item : items){
            if(item.name == pickup.name){
                item.stackAmount += pickup.stackAmount;
            }
            else{
                items.add(pickup);
                break;
            }
        }
    }*/

    public boolean closeToLeftWall(){
        if(coordinates.topLeftCorner_x < (int)(GameFrame.WIDTH/2)){return true;}
        return false;
    }

    public boolean closeToRightWall(){
        if(coordinates.topLeftCorner_x > (int)(mapWidth - GameFrame.WIDTH/2)){return true;}
        return false;
    }

    public boolean closeToUpWall(){
        if(coordinates.topLeftCorner_y < (int)(GameFrame.HEIGHT/2)){return true;}
        return false;
    }
    public boolean closeToDownWall(){
        if(coordinates.topLeftCorner_y > mapHeight - (int)(GameFrame.HEIGHT/2)){return true;}
        return false;
    }
}
