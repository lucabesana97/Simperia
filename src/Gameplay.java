import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import game.entities.Bullet;
import game.entities.Enemy;
import game.entities.Octopus;
import game.entities.Player;
import gui.GamePanel;
import gui.InventoryPanel;
import gui.Panel;
import gui.PausePanel;
import input.Command;
import input.KeyHandler;
import input.Keys;
import objState.EnemyState;

import javax.swing.*;

public class Gameplay {

    int pos_x, pos_y;

    final GamePanel panel;
    final KeyHandler keyHandler;

    private Player player;
	private List<Enemy> enemies = new ArrayList<>();
	private PausePanel pausePanel;
	private InventoryPanel inventoryPanel;
	private JButton pauseButton;
	private JButton inventoryButton;
	private boolean paused;


    public Gameplay(Panel panel, KeyHandler keyHandler) {
        this.panel = (GamePanel) panel;
        this.keyHandler = keyHandler;
		paused = false;
    }

    public void init() {
		player = new Player();

		panel.addKeyListener(keyHandler);
		panel.addMouseListener(keyHandler);

		// Temporary buttons for testing
		initializePauseButton();
		initializeInventoryButton();

		// Pause panel
		pausePanel = new PausePanel();
		panel.add(pausePanel);

		// Inventory panel
		inventoryPanel = new InventoryPanel();
		panel.add(inventoryPanel);
	}

	private void initializeInventoryButton() {
		inventoryButton = new JButton("Inventory");
		inventoryButton.setBounds(panel.getWidth() - 210, 10, 100, 50);
		inventoryButton.setBackground(new Color(0X593DB5));
		inventoryButton.addActionListener(e -> {
			inventoryPanel.setVisible(true);
		});
		panel.add(inventoryButton);
	}

	private void initializePauseButton() {
		pauseButton = new JButton("Pause");
		pauseButton.setBounds(panel.getWidth() - 90, 10, 70, 50);
		pauseButton.setBackground(new Color(0X593DB5));
		pauseButton.addActionListener(e -> {
			pausePanel.setVisible(true);
			pause(); // Pause the game
		});
		panel.add(pauseButton);
	}

	public void run() {

        long lastTick = System.currentTimeMillis();
		enemies.add(new Octopus());
        while (true) {
            long currentTick = System.currentTimeMillis();
            double diffSeconds = (currentTick - lastTick) / 100.0;
            lastTick = currentTick;


			try {
				handleUserInput();
			} catch (Exception e) {
				System.out.println(e.getCause());
			}

			update(diffSeconds);

			panel.clear();
			drawElements();
			panel.redraw();
			System.out.flush();

        }
    }

	private void update(double diffSeconds) {
		// Workaround to resume the game
		if (!pausePanel.isVisible()) {
			resume();
		}

		if (paused) {
			return;
		}

		player.move(diffSeconds);
		Iterator<Enemy> enemyIter = enemies.iterator();
		while (enemyIter.hasNext()) {
			Enemy enemy = enemyIter.next();
			if (enemy != null && enemy.enemyState == EnemyState.DEAD){
				enemyIter.remove();
			}
		}
		for (Enemy enemy : enemies) {
			enemy.move(diffSeconds, player);
		}
		System.gc();
	}

    private void drawElements() {
		for (Enemy enemy : enemies) {
			panel.draw(enemy);
		}
		panel.draw(player);
		pauseButton.repaint();
		inventoryButton.repaint();
		pausePanel.repaint();
		inventoryPanel.repaint();
    }

    private void handleUserInput() {
        final Set<Keys> pressedKeys = keyHandler.getKeys();
		final Set<Keys> pressedMouseButtons = keyHandler.getMouseButtons();

		for (Keys keyCode : pressedKeys) {
			Command command = keyCode.getCommand();
			if (command != null) {
				command.execute(keyCode);
			}
		}

		for (Keys mouseCode : pressedMouseButtons) {
			Command command = mouseCode.getCommand();
			if (command != null) {
				command.execute(mouseCode, keyHandler.mouseX, keyHandler.mouseY);
			}
		}
	}

	public void pause() {
		paused = true;
	}

	public void resume() {
		paused = false;
	}

	public boolean isPaused() {
		return paused;
	}
}
