package gui;

import game.GameObject;
import game.environment.GameMap;

public class GamePanel extends Panel {

	public GamePanel() {
		super();
	}

	public void draw(GameObject object) {
		object.draw(graphics);
	}
	public void draw(GameMap object) {
		object.draw(graphics);
	}



}
