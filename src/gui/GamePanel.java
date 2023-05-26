package gui;

import game.characters.Entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class GamePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	public static final int WIDTH = GameFrame.WIDTH;
	public static final int HEIGHT = GameFrame.HEIGHT;

	public Graphics graphics;
	final private BufferedImage imageBuffer;

	public GamePanel() {

		this.setSize(WIDTH, HEIGHT + 28);
		this.setBackground(Color.darkGray);

		GraphicsConfiguration graphicsConf = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
				.getDefaultConfiguration();
		imageBuffer = graphicsConf.createCompatibleImage(this.getWidth(), this.getHeight());
		graphics = imageBuffer.getGraphics();
	}

	public void clear() {
		graphics.setColor(Color.darkGray);
		graphics.fillRect(0, 0, WIDTH, HEIGHT);
	}

	public void redraw() {
		this.getGraphics().drawImage(imageBuffer, 0, 0, this);
	}

	public void draw(Entity entity){
		entity.draw(graphics);
	}

}
