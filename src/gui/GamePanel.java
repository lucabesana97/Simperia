package gui;

import game.GameObject;

import java.awt.*;

public class GamePanel extends Panel {

	public GamePanel() {
		super();
		setBackground(Color.green);
		setLayout(new FlowLayout());
	}

	public void draw(GameObject object) {
		object.draw(graphics);
	}
}
