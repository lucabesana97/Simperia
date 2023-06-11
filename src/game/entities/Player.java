package game.entities;

import game.Coordinates;
import game.Movable;
import gui.GameFrame;
import main.Gameplay;
import objState.MovingState;
import objState.ShootingState;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import static helperFunctions.Utility.resize;
import static helperFunctions.Utility.createFlipped;

public class Player extends Entity implements Movable {
    public static final double MAX_TIMER = 2;
    public int coins;
    public int xp;
    public int xpToNextLevel;
    public int levelXp;
    public MovingState xState;
    public MovingState yState;
    public int screenX = GameFrame.WIDTH / 2 - 24;
    public int screenY = GameFrame.HEIGHT / 2 - 24;
    public int mapHeight;
    public int mapWidth;
    public boolean sword;
    public MovingState aimX,aimY;
    public ShootingState shootState;
    private double shootCounter = 0;
    private BufferedImage[] spritesDown;
    private BufferedImage[] spritesUp;
    private BufferedImage[] spritesSide;

    public Player() {
        spritesDown = loadSprites("/sprites/player/Down-", 2);
        spritesUp = loadSprites("/sprites/player/Up-", 2);
        spritesSide = loadSprites("/sprites/player/Side-", 4);
        URL url = getClass().getResource("/sprites/player/Idle-1.png");
        BufferedImage image;
        try{
            image = ImageIO.read(url);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        speed = 30;
        xState = MovingState.STILL;
        yState = MovingState.STILL;
        shootState = ShootingState.READY;
        sprites.current = resize(image);
        this.name = "Player";
        this.coins = 0;
        this.xp = 0;
        levelXp = (level * level)/2 * 100;
        this.xpToNextLevel = levelXp;
        mapHeight = Gameplay.map.mapImage.getHeight();
        mapWidth = Gameplay.map.mapImage.getWidth();
        this.coordinates = new Coordinates((int)(mapWidth/2), (int)(mapHeight/2), 48, 48);
    }

    public void draw(Graphics graphics) {
        BufferedImage currentSprite;

        if (xState == MovingState.LEFT) {
            currentSprite = createFlipped(spritesSide[(int) (System.currentTimeMillis() / 200 % 4)]);
        } else if (xState == MovingState.RIGHT) {
            currentSprite = spritesSide[(int) (System.currentTimeMillis() / 200 % 4)];
        } else if (yState == MovingState.DOWN) {
            currentSprite = spritesDown[(int) (System.currentTimeMillis() / 200 % 2)];
        } else if (yState == MovingState.UP) {
            currentSprite = spritesUp[(int) (System.currentTimeMillis() / 200 % 2)];
        } else {
            currentSprite = sprites.current;
        }
        currentSprite = resize(currentSprite);

        // Draw the current sprite
        int x = (int) (coordinates.topLeftCorner_x);
        int y = (int) (coordinates.topLeftCorner_y);
        graphics.drawImage(currentSprite, screenX, screenY, null);

        // draw circle around player
        graphics.setColor(Color.RED);
        graphics.drawRect(screenX, screenY, 48, 48);
        graphics.drawLine(0, GameFrame.HEIGHT/2, GameFrame.WIDTH, GameFrame.HEIGHT/2);
        graphics.drawLine(GameFrame.WIDTH/2, 0, GameFrame.WIDTH/2, GameFrame.HEIGHT);
        graphics.drawRect(0, 0, GameFrame.WIDTH-1, GameFrame.HEIGHT-1);
//        graphics.drawRect(0, 0, Frame.WIDTH, Frame.HEIGHT);

    }

    @Override
    public void move(double diffSeconds) {
        //System.out.println(coordinates.topLeftCorner_x + "\t" + (int)(mapWidth - GameFrame.WIDTH/2));
        setShooting(diffSeconds);
        this.invincibilityTimer += diffSeconds;

        double moveBy = diffSeconds * speed;

        //TODO edit so player moves with keys pressed
        if(xState != MovingState.STILL && yState != MovingState.STILL){
            moveBy *= Math.sqrt(2) / 2;
            coordinates.moveX((xState == MovingState.RIGHT) ? moveBy : -moveBy);
            coordinates.moveY((yState == MovingState.DOWN) ? moveBy : -moveBy);
        } else if (xState != MovingState.STILL){
            coordinates.moveX((xState == MovingState.RIGHT) ? moveBy : -moveBy);
        } else if (yState != MovingState.STILL){
            coordinates.moveY((yState == MovingState.DOWN) ? moveBy : -moveBy);
        }

        if (coordinates.topLeftCorner_x < (int)(GameFrame.WIDTH/2)) {
            coordinates.topLeftCorner_x = (int)(GameFrame.WIDTH/2);
        }
        if (coordinates.topLeftCorner_y < (int)(GameFrame.HEIGHT/2)) {
            coordinates.topLeftCorner_y = (int)(GameFrame.HEIGHT/2);
        }
        if (coordinates.topLeftCorner_x > (int)(mapWidth - GameFrame.WIDTH/2)) {
            coordinates.topLeftCorner_x = (int)(mapWidth - GameFrame.WIDTH/2);
        }
        if (coordinates.topLeftCorner_y > mapHeight - (int)(GameFrame.HEIGHT/2)) {
            coordinates.topLeftCorner_y = mapHeight - (int)(GameFrame.HEIGHT/2);
        }
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
    }

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


    private void setShooting(double diffSeconds) {
        if(shootState == ShootingState.RELOADING && shootCounter < MAX_TIMER) {
            shootCounter += diffSeconds;
        }
        else if (shootState == ShootingState.RELOADING && shootCounter >= MAX_TIMER) {
            shootCounter = 0;
            shootState = ShootingState.READY;
        }
    }
}
