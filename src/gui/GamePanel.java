package gui;

import game.GameObject;
import game.environment.GameMap;
import game.inventory.Item;

import javax.swing.*;
import java.awt.*;


public class GamePanel extends Panel {
	private Image backgroundHUDImage;
	private Image playerImage;
//	private HUDElement levelLabel;
//	private HUDElement healthLabel;
//	private HUDElement profilePicture;

	public GamePanel() {
		super();
		setBackground(Color.green);
		setFocusable(true);

		backgroundHUDImage = Toolkit.getDefaultToolkit().getImage("resources/sprites/player_info_hud.png");
		playerImage = Toolkit.getDefaultToolkit().getImage("resources/sprites/player/Idle-1.png");
//		levelLabel = new HUDElement(20, 20 , "Level 1");
//		healthLabel = new HUDElement(50, 20, "Health: 100");
//		profilePicture = new HUDElement(5, 20, playerImage);
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
	public void draw(HUDElement object) { object.draw(graphics); }
	public void draw(Image backgroundHUDImage) {
		this.backgroundHUDImage = backgroundHUDImage;
		repaint();
	}

//	@Override
//	protected void paintComponent(Graphics g) {
//		super.paintComponent(g);
//
//		// Draw the background image
//		g.drawImage(backgroundHUDImage, 0, 0, null);
//
//		// Draw HUD elements
//		g.setColor(Color.white);
//		g.setFont(levelLabel.getFont());
//		g.drawString(levelLabel.getText(), levelLabel.getX(), levelLabel.getY());
//		g.drawString(healthLabel.getText(), healthLabel.getX(), healthLabel.getY());
//		g.drawImage(profilePicture.getImage(), profilePicture.getX(), profilePicture.getY(), null);
//	}


}

