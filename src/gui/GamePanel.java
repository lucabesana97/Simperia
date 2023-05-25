package gui;

import game.GameObject;

public class GamePanel extends Panel {

	public GamePanel() {
		super();
	}

	public void draw(GameObject object) {
		object.draw(graphics);
	}



}
