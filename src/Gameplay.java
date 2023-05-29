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
	private JButton pauseButton;

    public Gameplay(Panel panel, KeyHandler keyHandler) {
        this.panel = (GamePanel) panel;
        this.keyHandler = keyHandler;
    }

    public void init() {
		player = new Player();

		panel.addKeyListener(keyHandler);
		panel.addMouseListener(keyHandler);

		initializePauseButton();
		initializePausePanel();

		//panel.setFocusable(true);
    }

	private void initializePauseButton() {
		pauseButton = new JButton("Pause");
		pauseButton.setBounds(panel.getWidth() - 90, 10, 70, 50);
		pauseButton.setBackground(new Color(0X593DB5));
		pauseButton.addActionListener(e -> {
			pausePanel.setVisible(true);
			//pauseButton.setVisible(false);
		});
		panel.add(pauseButton);
	}

	private void initializePausePanel() {
		pausePanel = new PausePanel();
		pausePanel.setBackground(Color.pink);
		pausePanel.setVisible(false);

		// Calculate the position to center the pause panel
		int pausePanelWidth = (int) (panel.getWidth() * 0.5);
		int pausePanelHeight = (int) (panel.getHeight() * 0.5);
		int panelX = (panel.getWidth() - pausePanelWidth) / 2;
		int panelY = (panel.getHeight() - pausePanelHeight) / 2;

		pausePanel.setBounds(panelX, panelY, pausePanelWidth, pausePanelHeight);
		panel.add(pausePanel, BorderLayout.CENTER);
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
		pausePanel.repaint();
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
}
