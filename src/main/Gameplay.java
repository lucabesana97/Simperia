
package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import game.environment.AsteroidMap2;
import game.environment.CaveMap;
import game.inventory.CarKey;
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
    public List<GameObject> objects = new ArrayList<>();
    private List<GameObject> rocks = new ArrayList<>();
    private GameObject ship;

    private GameMap mapDestination;
    private Warp warpDestination;
    private final PausePanel pausePanel;
    private final InventoryPanel inventoryPanel;
    private final HomePanel homePanel;
    private final DialogPanel dialogPanel;
    private final InfoPanel infoPanel;
    private static VictoryFailPanel victoryFailPanel;
    private static GameState gameState;
    private Item deletedItem;
    private final Sound soundtrack = new Sound();
    Sound effects = new Sound();
    private HUD hudBackground;
    public static HUD healthBar;
    private HUD xpBar;
    private HUD levelLabel;
    private boolean firstLoop;
    public static boolean isMuted = false;
    private final int soundtrackVolume = -40;

    public Gameplay(Panel panel, KeyHandler keyHandler, GameFrame frame) {
        this.panel = (GamePanel) panel;
        this.pausePanel = frame.getPausePanel();
        this.inventoryPanel = frame.getInventoryPanel();
        this.dialogPanel = frame.getDialogPanel();
        this.homePanel = frame.getHomePanel();
        this.infoPanel = frame.getInfoPanel();
        victoryFailPanel = frame.getVictoryFailPanel();
        this.keyHandler = keyHandler;
        gameState = GameState.HOME;

        JButton newGameButton = homePanel.getNewGameButton();
        newGameButton.addActionListener(e -> {
            if (!isMuted) {
                effects.playSoundEffect(9);
            }
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
        hudBackground = new HUD(new Coordinates(10, 10, 361, 110), "/sprites/hud/hud.png");
        healthBar = new HUD(new Coordinates(121, 72, 230, 23), "/sprites/hud/healthbar.png");
        xpBar = new HUD(new Coordinates(112, 96, 95, 15), "/sprites/hud/xpbar.png");
        levelLabel = new HUD(new Coordinates(125, 63, 80, 30));
        ship = new Ship(new Coordinates(300, 500, 164, 91));

        panel.addKeyListener(keyHandler);

        inventory.addStack(new ItemStack(new CarKey(new Coordinates(100,100,32,32),ship), 1, true));
        inventoryPanel.updateInventoryUI();

        // Soundtrack
        soundtrack.stopMusic();
        soundtrack.playMusic(2);
        soundtrack.changeVolume(soundtrackVolume);

        createButtons();
        firstLoop = true;
        isMuted = false;
    }


    public void run_game() {

        long lastTick = System.currentTimeMillis();

        while (true) {
            long currentTick = System.currentTimeMillis();
            double diffSeconds = (currentTick - lastTick) / 100.0;
            lastTick = currentTick;

            this.panel.requestFocusInWindow();
            if (gameState == GameState.INFO) {
                infoPanel.setVisible(true);
                infoPanel.repaint();
            } else if (gameState == GameState.PAUSED) {
                pausePanel.setVisible(true);
                pausePanel.repaint();
            } else if (gameState == GameState.INVENTORY) {
                inventoryPanel.setVisible(true);
                inventoryPanel.repaint();
            } else if (gameState == GameState.DIALOG) {
                dialogPanel.setVisible(true);
                dialogPanel.repaint();
            } else if (gameState == GameState.WIN || gameState == GameState.GAMEOVER) {
                victoryFailPanel.setVisible(true);
                victoryFailPanel.repaint();
            } else if (gameState == GameState.PLAYING) {
                pausePanel.setVisible(false);
                inventoryPanel.setVisible(false);
                dialogPanel.setVisible(false);
                infoPanel.setVisible(false);
                victoryFailPanel.setVisible(false);

                update(diffSeconds);

                panel.clear();
                drawElements();
                panel.redraw();

                try {
                    handleUserInput();
                } catch (Exception e) {
                    System.out.println("Error: " + e.getCause());
                }

                if (firstLoop) {
                    displayInfo("Initial info about the game....... Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut " +
                            "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi " +
                            "ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum " +
                            "dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia " +
                            "deserunt mollit anim id est laborum.");
                    firstLoop = false;
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

                    bossKilled();

                }

            }
        }


        for (Enemy enemy : enemies) {
            enemy.move(diffSeconds, player);
        }
        if (beginnerNPC != null) {
            if (player.isColliding(beginnerNPC) && !(beginnerNPC.interacting)) {
                String text = beginnerNPC.interact(); // Player is talking to the NPC
                displayNPCDialog(text);
            } else if (!player.isColliding(beginnerNPC)) {
                beginnerNPC.stopInteracting(); // Player is not talking to the NPC
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
            if (bullet.coordinates.topLeftCorner_y < -100 || bullet.coordinates.topLeftCorner_y > 3000 || bullet.coordinates.topLeftCorner_x < -100 || bullet.coordinates.topLeftCorner_x > 3000) {
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
            } else if (bullet.coordinates.topLeftCorner_y < -100 || bullet.coordinates.topLeftCorner_y > 3000 || bullet.coordinates.topLeftCorner_x < -100 || bullet.coordinates.topLeftCorner_x > 3000) {
                try {
                    bulletIteratorEnemy.remove();
                } catch (Exception e) {
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

        healthBar.updateHealthBar();
        xpBar.updateXpBar();
        levelLabel.updateLevelLabel();

        // Don't delete this
//        if (player.health <= 0) {
//            fail();
//        } TODO

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

        if((map instanceof AsteroidMap) || (map instanceof AsteroidMap2)){
            panel.draw(ship);
        }

        panel.draw(player);
        panel.draw(hudBackground);
        panel.draw(healthBar);
        panel.draw(xpBar);
        panel.draw(levelLabel);
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
                            playerBullets.add(new Bullet(angle, player.coordinates, Bullet.PLAYER, 10, 15, "/sprites/player/Player-bullet.png"));
                            if (!isMuted) {
                                effects.playSoundEffect(7);
                            }
                            player.shootState = FightState.RELOADING;
                        }
                    } else if (player.currentWeapon == player.SWORD) {
                        if (player.shootState == FightState.READY) {
                            player.angle = Utility.getAimAngle(player);
                            for (Enemy enemy : enemies) {
                                if (player.inSlashRange(enemy)) {
                                    player.attack(enemy);
                                }
                            }
                            if (!isMuted) {
                                effects.playSoundEffect(8);
                            }
                        }
                        player.shootState = FightState.RELOADING;
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
        boolean vicVisible = victoryFailPanel.isVisible();
        inventoryPanel.clearSelectedSlot();
        inventoryPanel.setVisible(false);
        gameState = GameState.PLAYING;
        if (vicVisible) {
            victoryFailPanel.setVisible(true);
            gameState = GameState.WIN;
        }
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
        if (!isMuted) {
            soundtrack.stopMusic();
            muteButton.setIcon(new ImageIcon("resources/sprites/pause/music_muted.png"));
            isMuted = true;
        } else {
            soundtrack.playMusic(2);
            soundtrack.changeVolume(soundtrackVolume);
            muteButton.setIcon(new ImageIcon("resources/sprites/pause/music_playing.png"));
            isMuted = false;
        }
    }

    public void quitGame() { // TODO
//        gameState = GameState.HOME;
//        if (pausePanel.isVisible()) {
//            pausePanel.setVisible(false);
//        } else if (victoryFailPanel.isVisible()) {
//            victoryFailPanel.setVisible(false);
//        }
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

    public void displayInfo(String text) {
        infoPanel.setInfoText(text);
        infoPanel.setVisible(true);
        gameState = GameState.INFO;
    }

    public void closeInfo() {
        infoPanel.setVisible(false);
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
        // Button actions of pause/inventory/dialog/info panels
        JButton resumeButton = pausePanel.getResumeButton();
        resumeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isMuted) {
                    effects.playSoundEffect(9);
                }
                resume();
            }
        });


        JButton muteButton = pausePanel.getMuteButton();
        muteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isMuted) {
                    effects.playSoundEffect(9);
                }
                muteGame(muteButton);
            }
        });

        JButton quitGameButton = pausePanel.getQuitButton();
        quitGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isMuted) {
                    effects.playSoundEffect(9);
                }
                quitGame();
            }
        });


        JButton closeInventoryButton = inventoryPanel.getCloseButton();
        closeInventoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isMuted) {
                    effects.playSoundEffect(9);
                }
                closeInventory();
            }
        });

        JButton closeDialogButton = dialogPanel.getCloseDialogButton();
        closeDialogButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isMuted) {
                    effects.playSoundEffect(9);
                }
                closeNPCDialog();
            }
        });

        JButton closeInfoButton = infoPanel.getCloseInfoButton();
        closeInfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isMuted) {
                    effects.playSoundEffect(9);
                }
                closeInfo();
            }
        });

        JButton playAgainButton = victoryFailPanel.getPlayButton();
        playAgainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isMuted) {
                    effects.playSoundEffect(9);
                }
                quitGame();
            }
        });
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

    private void bossKilled() {

        System.out.println("Boss killed");

        try {
            BufferedImage floorSymbol = ImageIO.read(getClass().getResourceAsStream("/sprites/maps/cavern_floor_win.png"));
            objects.get(0).image = floorSymbol;
            beginnerNPC.quest.completed = true;

            //drops car key
            stacksOnWorld.add(new ItemStack(
                    new CarKey(
                            new Coordinates(450, 450, 48, 29),
                            ship
                    ),1, false));

        } catch (Exception e) {
            System.out.println("Couldn't load floor symbol: " + "\tReason: " + e.getCause());
        }

    }

    public static void victory() {
        gameState = GameState.WIN;

        String victoryTextUp = "Congratulations!";
        String victoryTextDown = "You have made it! You have found the car key and can now escape this planet! " +
                "You have won the game!";

        victoryFailPanel.setTopText(victoryTextUp);
        victoryFailPanel.setBottomText(victoryTextDown);
        victoryFailPanel.setVisible(true);
    }

    public void fail() {
        gameState = GameState.GAMEOVER;

        String failTextUp = "Oh no!";
        String failTextDown = "You have failed to complete the game. Try again!";

        victoryFailPanel.setTopText(failTextUp);
        victoryFailPanel.setBottomText(failTextDown);
        victoryFailPanel.setVisible(true);
    }

}

