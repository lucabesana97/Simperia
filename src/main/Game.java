
package main;
import gui.*;
import input.KeyHandler;

import javax.swing.*;

public class Game {
	final private GameFrame frame;
	final private KeyHandler keyHandler;

	private Gameplay gameplay;
	private Panel panel;

	public Game() {
		this.frame = new GameFrame("Simperia");
		this.keyHandler = new KeyHandler();
		this.frame.addKeyListener(keyHandler);
	}

	public void init() {
		this.panel = new HomePanel();
		//frame.setPanel(panel);

		JButton newGameButton = ((HomePanel) panel).getNewGameButton();
		newGameButton.addActionListener(e -> { start(); });
	}

	public void start() {
		this.panel = new GamePanel();
		this.panel.requestFocusInWindow();
		frame.setPanel(panel);

		gameplay = new Gameplay(panel, keyHandler, frame);

		gameplay.init();
		gameplay.run();
	}
}
