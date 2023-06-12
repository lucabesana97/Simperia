package gui;

import javax.swing.*;

public class GameFrame extends JFrame{

	//Dimensions of the window
	public static final int WIDTH = 1024;
	public static final int HEIGHT = 768;
	public static final int TILE_SIZE = 64;


	private Panel panel;
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

		inventoryPanel = new InventoryPanel();
		inventoryPanel.setVisible(false);

		// Add the layered pane to the main frame's content pane
		getContentPane().add(layeredPane);

		setLocationRelativeTo(null); // Center the window
		setVisible(true);

	}
	
    public void setPanel(Panel panel) {
        if (this.panel != null) {
            remove(this.panel);
        }
        this.panel = panel;

		// Add the game panel to the bottom layer
		layeredPane.add(panel, JLayeredPane.DEFAULT_LAYER);
		layeredPane.add(pausePanel, JLayeredPane.PALETTE_LAYER);
		layeredPane.add(inventoryPanel, JLayeredPane.PALETTE_LAYER);

		// layeredPane.setComponentZOrder(pausePanel, JLayeredPane.PALETTE_LAYER);
		// layeredPane.setComponentZOrder(inventoryPanel, JLayeredPane.PALETTE_LAYER);

		layeredPane.revalidate();
		// layeredPane.repaint();
    }

	public PausePanel getPausePanel() { return pausePanel; }
	public InventoryPanel getInventoryPanel() { return inventoryPanel; }


}
