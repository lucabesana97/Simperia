package gui;

import game.GameObject;
import game.environment.GameMap;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends Panel {

	public GamePanel() {
		super();
		setBackground(Color.green);
		//setLayout(new FlowLayout());

		setLayout(null);

		JLayeredPane layeredPane = new JLayeredPane();
		//layeredPane.setBounds(0, 0, 800, 500);

		//HomePanel homePanel = new HomePanel();
		//PausePanel pausePanel = new PausePanel();
		//InventoryPanel inventoryPanel = new InventoryPanel();

		//layeredPane.add(homePanel, Integer.valueOf(0));
		//layeredPane.add(pausePanel, Integer.valueOf(1));
		//layeredPane.add(inventoryPanel, Integer.valueOf(2));

		add(layeredPane);
	}

	public void draw(GameObject object) {
		object.draw(graphics);
	}
	public void draw(GameMap object) {
		object.draw(graphics);
	}
	public void draw(JButton object) {
		object.paint(graphics);
	}


}
