package gui;

import javax.swing.*;

public class GameFrame extends JFrame{

	//Dimensions of the window
	public static final int WIDTH = 1024;
	public static final int HEIGHT = 768;
	public static final int TILE_SIZE = 64;


	private GamePanel gamePanel;
	private PausePanel pausePanel;
	private InventoryPanel inventoryPanel;
	private JLayeredPane layeredPane;
	
	public GameFrame(String title) {
		// Set window properties
		setTitle(title);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(WIDTH, HEIGHT);
		setResizable(false);

		// Create a layered pane to hold different panels
		layeredPane = new JLayeredPane();

		pausePanel = new PausePanel();
		pausePanel.setVisible(false);
//		layeredPane.add(pausePanel, 1);

		inventoryPanel = new InventoryPanel();
		inventoryPanel.setVisible(false);
//		layeredPane.add(inventoryPanel, 2);

		// Add the layered pane to the main frame's content pane
		getContentPane().add(layeredPane);

		setLocationRelativeTo(null); // Center the window
		setVisible(true);

	}
	
    public void setPanel(GamePanel gamePanel) {
        if (this.gamePanel != null) {
            remove(this.gamePanel);
        }
        this.gamePanel = gamePanel;

		//this.add(panel);

		// Add the game panel to the bottom layer
		layeredPane.add(gamePanel, JLayeredPane.DEFAULT_LAYER);
		layeredPane.add(pausePanel, JLayeredPane.PALETTE_LAYER);
		layeredPane.add(inventoryPanel, JLayeredPane.PALETTE_LAYER);
//		pausePanel.setVisible(false);


//		layeredPane.setComponentZOrder(pausePanel, JLayeredPane.PALETTE_LAYER);
//		layeredPane.setComponentZOrder(inventoryPanel, JLayeredPane.PALETTE_LAYER);

		layeredPane.revalidate();
//		layeredPane.repaint();
    }

	public PausePanel getPausePanel() { return pausePanel; }
	public InventoryPanel getInventoryPanel() { return inventoryPanel; }


}
