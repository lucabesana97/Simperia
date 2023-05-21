package gui;

import java.awt.BorderLayout;
import javax.swing.JFrame;

public class GameFrame extends JFrame{

	private static final long serialVersionUID = 1L;
	public static final int TILE_SIZE = 32;
	public static final int TILE_NUM_X = 40;
	public static final int TILE_NUM_Y = 24;
	public static final int WIDTH = TILE_SIZE * TILE_NUM_X;
	public static final int HEIGHT = TILE_SIZE * TILE_NUM_Y;
	
	private GamePanel panel;
	
	public GameFrame() {
		setTitle("Catch my balls");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(WIDTH+16, HEIGHT+40);
		setFocusable(true);
		setResizable(false);
		setVisible(true);
	}
	
    public void setPanel(GamePanel panel) {
        if (this.panel != null) {
            remove(this.panel);
        }
        this.panel = panel;
        this.add(panel, BorderLayout.SOUTH);
    }
}
