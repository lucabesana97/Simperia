package gui;

import game.AGameObject;

public class GamePanel extends APanel {

	public GamePanel() {
		super();
	}

	public void draw(AGameObject object) {
		object.draw(graphics);
	}



}
