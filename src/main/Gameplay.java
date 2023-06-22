
package main;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.imageio.ImageIO;
import javax.swing.*;

import game.Coordinates;
import game.GameObject;
import game.environment.AsteroidMap;
import game.environment.CaveMap;
import game.inventory.Item;
import game.GameState;
import game.entities.*;
import game.environment.GameMap;
import game.inventory.Inventory;
import game.inventory.ItemStack;
import gui.*;
import gui.Panel;
import gui.elements.HUD;
import helperFunctions.Utility;
import input.KeyHandler;
import input.Keys;
import objState.EnemyState;
import objState.FightState;
import objState.MovingState;
import output.Sound;

public class Gameplay {
    private final GamePanel panel;
    final KeyHandler keyHandler;

    public static Player player;
    public static Inventory inventory;
    public static GameMap map;
    private boolean changeMap = false;
    private NPC beginnerNPC;
    private List<ItemStack> stacksOnWorld = new ArrayList<>();
    private List<Enemy> enemies = new ArrayList<>();
    private List<Bullet> playerBullets = new ArrayList<>();
    public static List<Bullet> enemyBullets = new ArrayList<>();
    private List<Warp> warps = new ArrayList<>();
    private List<Warp> mapWarps = new ArrayList<>();
    private List<GameObject> objects = new ArrayList<>();
    private List<GameObject> rocks = new ArrayList<>();

    private GameMap mapDestination;
    private Warp warpDestination;
    private final PausePanel pausePanel;
    private final InventoryPanel inventoryPanel;
    private final HomePanel homePanel;
    private final DialogPanel dialogPanel;
    private GameState gameState;
    private Item deletedItem;
    private final Sound soundtrack = new Sound();
    Sound effects = new Sound();
    private JButton newGameButton;
    private HUD hud;
    private HUD healthBar;
    private HUD xpBar;

    public Gameplay(Panel panel, KeyHandler keyHandler, GameFrame frame) {
        this.panel = (GamePanel) panel;
        this.pausePanel = frame.getPausePanel();
        this.inventoryPanel = frame.getInventoryPanel();
        this.dialogPanel = frame.getDialogPanel();
        this.homePanel = frame.getHomePanel();
        this.keyHandler = keyHandler;
        this.gameState = GameState.HOME;

        newGameButton = homePanel.getNewGameButton();
        newGameButton.addActionListener(e -> {
            gameState = GameState.PLAYING;
            homePanel.setVisible(false);
            init();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    run_game();
                }
            }).start();
        });
    }


    public void init() {

        // Don't change the order
        map = new AsteroidMap();
        player = new Player();
        map.init(player);

        loadObjects();
        inventory = new Inventory();
        inventoryPanel.init(inventory);
        hud = new HUD(new Coordinates(10, 10, 361, 110), "/sprites/hud/hud.png");
        healthBar = new HUD(new Coordinates(121, 72, 230, 23), "/sprites/hud/healthbar.png");
        xpBar = new HUD(new Coordinates(112, 96, 95, 15), "/sprites/hud/xpbar.png");

        panel.addKeyListener(keyHandler);

        // Soundtrack
        soundtrack.stopMusic();
        soundtrack.playMusic(2);
        soundtrack.changeVolume(-20);

        createButtons();
    }


    public void run_game() {

        long lastTick = System.currentTimeMillis();

        while (true) {
            long currentTick = System.currentTimeMillis();
            double diffSeconds = (currentTick - lastTick) / 100.0;
            lastTick = currentTick;

            this.panel.requestFocusInWindow();
            if (gameState == GameState.PAUSED) {
                pausePanel.setVisible(true);
                pausePanel.repaint();
            } else if (gameState == GameState.INVENTORY) {
                inventoryPanel.setVisible(true);
                inventoryPanel.repaint();
            } else if (gameState == GameState.DIALOG) {
                dialogPanel.setVisible(true);
                dialogPanel.repaint();
            } else if (gameState == GameState.PLAYING) {
                pausePanel.setVisible(false);
                inventoryPanel.setVisible(false);
                dialogPanel.setVisible(false);

                update(diffSeconds);

                panel.clear();
                drawElements();
                panel.redraw();

                try {
                    handleUserInput();
                } catch (Exception e) {
                    System.out.println("Error: " + e.getCause());
                }

            }
            System.out.flush();
        }
    }

    private void update(double diffSeconds) {
        // Player
        player.move(diffSeconds);

        //Enemies
        Iterator<Enemy> enemyIter = enemies.iterator();
        while (enemyIter.hasNext()) {
            Enemy enemy = enemyIter.next();
            if (enemy != null && enemy.enemyState == EnemyState.DEAD) {
                enemyIter.remove();

                //boss killed
                if (map instanceof CaveMap && enemies.isEmpty()) {

                    try {
                        BufferedImage floorSymbol = ImageIO.read(getClass().getResourceAsStream("/sprites/maps/cavern_floor_win.png"));
                        objects.get(0).image = floorSymbol;
                        beginnerNPC.quest.completed = true;
                    } catch (Exception e) {
                        System.out.println("Couldn't load floor symbol: " + "\tReason: " + e.getCause());
                    }

                    //TODO display victory text

                }

            }
        }

        for (Enemy enemy : enemies) {
            enemy.move(diffSeconds, player);
        }
        if (beginnerNPC != null) {
            if (player.isColliding(beginnerNPC) && !(beginnerNPC.interacting)) {
                // Player is talking to the NPC
                String text = beginnerNPC.interact();
                displayNPCDialog(text);
                //System.out.println("Text: " + text);
            } else if (!player.isColliding(beginnerNPC)) {
                // Player is not talking to the NPC
                beginnerNPC.stopInteracting();
                closeNPCDialog();
            }

        }
        for (Warp warp : warps) {
            if (player.isColliding(warp)) {
                player.teleport(warp);
            }
        }
        Iterator<Bullet> bulletIterator = playerBullets.iterator();
        while (bulletIterator.hasNext()) {
            Bullet bullet = bulletIterator.next();
            bullet.move(diffSeconds, player);
            if (bullet.enemyState == EnemyState.DEAD) {
                bulletIterator.remove();
                continue;
            }
            for (Enemy enemy : enemies) {
                if (enemy.isColliding(bullet)) {
//                    System.out.println("Enemy health: " + enemy.health);
                    bullet.attack(enemy);
                    if (enemy.enemyState != EnemyState.DEAD) {
                        enemy.enemyState = EnemyState.DAMAGED;
                    }
                    bulletIterator.remove();
                    break;
                }
            }
            if(bullet.coordinates.topLeftCorner_y < -100 || bullet.coordinates.topLeftCorner_y > 3000 || bullet.coordinates.topLeftCorner_x < -100 || bullet.coordinates.topLeftCorner_x > 3000){
                bulletIterator.remove();
            }
        }

        Iterator<Bullet> bulletIteratorEnemy = enemyBullets.iterator();
        while (bulletIteratorEnemy.hasNext()) {
            Bullet bullet = bulletIteratorEnemy.next();
            bullet.move(diffSeconds, player);
            if (bullet.enemyState == EnemyState.DEAD) {
                bulletIteratorEnemy.remove();
            }
            if (player.isColliding(bullet)) {
//                System.out.println("Player health: " + player.health);
                bullet.attack(player);
                bulletIteratorEnemy.remove();
            }else if(bullet.coordinates.topLeftCorner_y < -100 || bullet.coordinates.topLeftCorner_y > 3000 || bullet.coordinates.topLeftCorner_x < -100 || bullet.coordinates.topLeftCorner_x > 3000) {
                try {
                    bulletIteratorEnemy.remove();
                }catch (Exception e){
                    System.out.println("Error: " + e.getCause());
                }
            }
        }
		/*
		for(Item item : items){
			if(player.isColliding(item)){
				player.pickUp(item);
				deletedItem = item;
			}
		}
		if(deletedItem != null){
			items.remove(deletedItem);
			deletedItem = null;
		}
		*/
        for (Warp warp : mapWarps) {
            if (player.isColliding(warp)) {
                changeMap = true;
                mapDestination = GameMap.warpTo(warp.mapID);
                warpDestination = warp;
            }
        }
        if (changeMap) {
            mapDestination.player = player;
            map = mapDestination;
            loadObjects();
            player.teleport(warpDestination);
            player.mapHeight = map.mapImage.getHeight();
            player.mapWidth = map.mapImage.getWidth();
            mapDestination = null;
            changeMap = false;
        }

        //Items
        for (int i = 0; i < stacksOnWorld.size(); i++) {
            if (stacksOnWorld.get(i) != null) {
                if (player.isColliding(stacksOnWorld.get(i))) {
                    ItemStack remainder = inventory.addStack(stacksOnWorld.get(i));

                    if (remainder == null) { //The item was added, it has to be deleted from the world
                        stacksOnWorld.remove(i);
                    } else { //There are items that didn't fit in the inventory.
                        stacksOnWorld.get(i).amount = remainder.amount;
                    }
                    inventoryPanel.updateInventoryUI();
                }
            }
        }

        //Map
        if (map instanceof CaveMap) {
            updateCaveMap();
        }


        System.gc();
    }

    private void drawElements() {

        panel.draw(map);
        for (GameObject object : objects) {
            panel.draw(object);
        }
        for (GameObject rock : rocks) {
            panel.draw(rock);
        }
        if (beginnerNPC != null) {
            panel.draw(beginnerNPC);
        }
        for (Enemy enemy : enemies) {
            panel.draw(enemy);
        }
        for (Bullet bullet : playerBullets) {
            panel.draw(bullet);
        }
        for (Bullet bullet : enemyBullets) {
            panel.draw(bullet);
        }
        for (Warp warp : warps) {
            panel.draw(warp);
        }
        for (Warp warp : mapWarps) {
            panel.draw(warp);
        }
        for (ItemStack item : stacksOnWorld) {
            panel.draw(item.item);
        }

        panel.draw(player);
        panel.draw(hud);
        panel.draw(healthBar);
        panel.draw(xpBar);
    }

    private void handleUserInput() {
        final Set<Keys> pressedKeys = keyHandler.getKeys();
        boolean horStill = true;
        boolean verStill = true;
        boolean sprintStill = true;
        boolean npcTalk = false;
        for (Keys keyCode : pressedKeys) {
            //System.out.println("Key pressed: " + keyCode);
            switch (keyCode) {
                case ATTACK:
                    if (player.currentWeapon == player.GUN) {
                        if (player.shootState == FightState.READY) {
                            int angle = Utility.getAimAngle(player);
                            playerBullets.add(new Bullet(angle, player.coordinates, Bullet.PLAYER, 10, 15));
                            player.shootState = FightState.RELOADING;
                        }
                    } else if (player.currentWeapon == player.SWORD) {
                        for (Enemy enemy : enemies) {
                            if (player.inSlashRange(enemy)) {
                                player.attack(enemy);
                            }
                        }
                    }
                    break;
                case LEFT:
                    player.xState = MovingState.LEFT;
                    horStill = false;
                    break;
                case RIGHT:
                    player.xState = MovingState.RIGHT;
                    horStill = false;
                    break;
                case UP:
                    player.yState = MovingState.UP;
                    verStill = false;
                    break;
                case DOWN:
                    player.yState = MovingState.DOWN;
                    verStill = false;
                    break;
                case SWITCH:
                    player.switchWeapon();
                    break;
                case PAUSE:
                    if (!pausePanel.isVisible()) {
                        pause();
                    } else {
                        resume();
                    }
                    break;
                case INVENTORY:
                    if (!inventoryPanel.isVisible()) {
                        openInventory();
                    } else {
                        closeInventory();
                    }
                    break;
                case SPRINT:
                    player.sprint();
                    sprintStill = false;
                    break;
                case NPC_TALK:
                    // Leave it empty for now
                    break;
                default:
                    break;
            }
        }
//        System.out.println(player.yState + "\t" + player.xState);
        if (horStill) {
            player.xState = MovingState.STILL;
        }
        if (verStill) {
            player.yState = MovingState.STILL;
        }
        if (sprintStill) {
            player.slowDown();
        }
    }

    private void openInventory() {
        inventoryPanel.setVisible(true);
        gameState = GameState.INVENTORY;
    }

    private void closeInventory() {
        inventoryPanel.clearSelectedSlot();
        inventoryPanel.setVisible(false);
        gameState = GameState.PLAYING;
    }

    public void pause() {
        pausePanel.setVisible(true);
        gameState = GameState.PAUSED;
    }

    public void resume() {
        pausePanel.setVisible(false);
        gameState = GameState.PLAYING;
    }

    public void muteGame(JButton muteButton) {
        if (soundtrack.isMusicPlaying()) {
            soundtrack.stopMusic();
            muteButton.setIcon(new ImageIcon("resources/sprites/pause/music_muted.png"));
        } else {
            soundtrack.playMusic(2);
            soundtrack.changeVolume(-20);
            muteButton.setIcon(new ImageIcon("resources/sprites/pause/music_playing.png"));
        }
    }

    public void quitGame() {
//        gameState = GameState.HOME;
//        pausePanel.setVisible(false);
//        homePanel.setVisible(true);
    }

    public void displayNPCDialog(String text) {
        dialogPanel.setDialogText(text);
        dialogPanel.setVisible(true);
        gameState = GameState.DIALOG;
    }

    public void closeNPCDialog() {
        dialogPanel.setVisible(false);
        gameState = GameState.PLAYING;
    }

    public void loadObjects() {
        enemies.removeAll(enemies);
        stacksOnWorld.removeAll(stacksOnWorld);
        warps.removeAll(warps);
        mapWarps.removeAll(mapWarps);
        objects.removeAll(objects);
        rocks.removeAll(rocks);
        enemyBullets.removeAll(enemyBullets);
        playerBullets.removeAll(playerBullets);
        beginnerNPC = null;

        enemies.addAll(map.enemies);
        stacksOnWorld.addAll(map.stacksOnWorld);
        warps.addAll(map.warps);
        mapWarps.addAll(map.mapWarps);
        objects.addAll(map.objects);
        rocks.addAll(map.rocks);
        beginnerNPC = map.beginnerNPC;
    }

    private void createButtons() {
        // Button actions of pause/inventory/dialog panels
        JButton resumeButton = pausePanel.getResumeButton();
        resumeButton.addActionListener(e -> resume());

        JButton muteButton = pausePanel.getMuteButton();
        muteButton.addActionListener(e -> {
            muteGame(muteButton);
        });

        JButton quitGameButton = pausePanel.getQuitButton();
        quitGameButton.addActionListener(e -> quitGame());

        JButton closeInventoryButton = inventoryPanel.getCloseButton();
        closeInventoryButton.addActionListener(e -> closeInventory());

        JButton closeDialogButton = dialogPanel.getCloseDialogButton();
        closeDialogButton.addActionListener(e -> closeNPCDialog());
    }

    private void updateCaveMap() {

        // DROP ROCKS
        GameObject rock;
        BufferedImage rockImage = null;

        try {
            rockImage = ImageIO.read(getClass().getResourceAsStream("/sprites/maps/cavern_rock.png"));
        } catch (Exception e) {
            System.out.println("Couldn't load rock image: " + "\tReason: " + e.getCause());
        }

        //down left section
        if (rocks.size() == 0 && player.coordinates.topLeftCorner_y < 2510 && player.coordinates.topLeftCorner_x < 650) {
            rocks = new ArrayList<>();
            rock = new GameObject(new Coordinates(310, 2530, 104, 106), rockImage);
            rocks.add(rock);
            rock = new GameObject(new Coordinates(400, 2530, 104, 106), rockImage);
            rocks.add(rock);
        }
        //down right section
        else if (rocks.size() == 0 && player.coordinates.topLeftCorner_y < 2510 && player.coordinates.topLeftCorner_x > 650) {
            rocks = new ArrayList<>();
            rock = new GameObject(new Coordinates(710, 2530, 104, 106), rockImage);
            rocks.add(rock);
            rock = new GameObject(new Coordinates(810, 2530, 104, 106), rockImage);
            rocks.add(rock);
        }
        //center section
        else if (rocks.size() <= 6 && player.coordinates.topLeftCorner_y < 1820) {
            rock = new GameObject(new Coordinates(200, 1839, 104, 106), rockImage);
            rocks.add(rock);
            rock = new GameObject(new Coordinates(320, 1839, 104, 106), rockImage);
            rocks.add(rock);
            rock = new GameObject(new Coordinates(570, 1839, 104, 106), rockImage);
            rocks.add(rock);
            rock = new GameObject(new Coordinates(711, 1839, 104, 106), rockImage);
            rocks.add(rock);
        }
        //top left section
        else if (rocks.size() <= 10 && player.coordinates.topLeftCorner_y < 1600 && player.coordinates.topLeftCorner_x < 650) {
            rock = new GameObject(new Coordinates(320, 1620, 104, 106), rockImage);
            rocks.add(rock);
            rock = new GameObject(new Coordinates(400, 1620, 104, 106), rockImage);
            rocks.add(rock);
        }
        //top right section
        else if (rocks.size() <= 10 && player.coordinates.topLeftCorner_y < 1600 && player.coordinates.topLeftCorner_x > 650) {
            rock = new GameObject(new Coordinates(670, 1620, 104, 106), rockImage);
            rocks.add(rock);
        }
        //boss section
        else if (rocks.size() <= 15 && player.coordinates.topLeftCorner_y < 700) {
            rock = new GameObject(new Coordinates(335, 730, 104, 106), rockImage);
            rocks.add(rock);
            rock = new GameObject(new Coordinates(440, 730, 104, 106), rockImage);
            rocks.add(rock);
            rock = new GameObject(new Coordinates(680, 730, 104, 106), rockImage);
            rocks.add(rock);
            rock = new GameObject(new Coordinates(760, 730, 104, 106), rockImage);
            rocks.add(rock);
        }

        // ACTIVATE FINAL BOSS
        if (player.coordinates.topLeftCorner_y < 820 && player.coordinates.topLeftCorner_y > 750) {
            try {
                BufferedImage floorSymbol = ImageIO.read(getClass().getResourceAsStream("/sprites/maps/cavern_floor_fight.png"));
                objects.get(0).image = floorSymbol;

                enemies.removeAll(enemies);
                this.enemies.add(new BossOctopus(500, 500));

            } catch (Exception e) {
                System.out.println("Couldn't load floor symbol: " + "\tReason: " + e.getCause());

            }
        }
    }

}

