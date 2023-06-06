package gui;

import java.awt.BorderLayout;
import javax.swing.JFrame;

public class GameFrame extends JFrame{

	//Dimensions of the window
	public static final int WIDTH = 1024;
	public static final int HEIGHT = 768;

	private Panel panel;
	
	public GameFrame(String title) {
		// Set window properties
		setTitle(title);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(WIDTH, HEIGHT);
		setResizable(false);

		// Set window to be visible
		setVisible(true);
	}
	
    public void setPanel(Panel panel) {
        if (this.panel != null) {
            remove(this.panel);
        }
        this.panel = panel;
        this.add(panel, BorderLayout.SOUTH);
    }
}
