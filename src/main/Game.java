
package main;
import gui.*;
import input.KeyHandler;

import javax.swing.*;

public class Game {
	final private GameFrame frame;
	final private KeyHandler keyHandler;
	private HomePanel homePanel;
	private Gameplay gameplay;
	private Panel panel;

	public Game() {
		this.frame = new GameFrame("Simperia");
		this.keyHandler = new KeyHandler();
		this.frame.addKeyListener(keyHandler);
	}

	public void init() {
		//this.homePanel = frame.getHomePanel();
//		this.panel = new HomePanel();
		//frame.setPanel(homePanel);
//
//		JButton newGameButton = ((HomePanel) panel).getNewGameButton();
//		newGameButton.addActionListener(e -> { this.start(); });
//		this.start();
	}

	public void start() {
		this.panel = new GamePanel();
		this.panel.setFocusable(true);

		frame.setPanel(panel);

//		JButton newGameButton = homePanel.getNewGameButton();
//		newGameButton.addActionListener(e -> {
//			homePanel.setVisible(false);
//
//
//		});

		gameplay = new Gameplay(panel, keyHandler, frame);
		//gameplay.init();
		this.panel.requestFocusInWindow();
		//gameplay.run();

	}
}
