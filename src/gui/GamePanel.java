package gui;

import game.GameObject;
import game.environment.GameMap;
import game.inventory.Item;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends Panel {

	public GamePanel() {
		super();
		setBackground(Color.green);
		setFocusable(true);
	}
	public void draw(GameObject object) {
		object.draw(graphics);
		// draw red rectangle around object
		graphics.setColor(Color.red);
		graphics.drawRect((int) object.coordinates.topLeftCorner_x, (int) object.coordinates.topLeftCorner_y, (int) object.coordinates.size_X, (int) object.coordinates.size_Y);
	}

	public void draw(GameMap object) {
		object.draw(graphics);
	}
	public void draw(JButton object) {
		object.paint(graphics);
	}

	public void draw(Item object) {
		object.draw(graphics);
	}


}
