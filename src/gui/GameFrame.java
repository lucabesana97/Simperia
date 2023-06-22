package gui;

import javax.swing.*;

public class GameFrame extends JFrame{

	//Dimensions of the window
	public static final int WIDTH = 1024;
	public static final int HEIGHT = 768;
	public static final int TILE_SIZE = 64;


	private Panel panel;
	private final HomePanel homePanel;
	private final PausePanel pausePanel;
	private final InventoryPanel inventoryPanel;
	private final DialogPanel dialogPanel;
	private final JLayeredPane layeredPane;
	
	public GameFrame(String title) {

		// Set window properties
		setTitle(title);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(WIDTH, HEIGHT);
		setResizable(false);

		// Layered pane to hold different panels
		layeredPane = new JLayeredPane();

		homePanel = new HomePanel();
		homePanel.setVisible(true);

		pausePanel = new PausePanel();
		pausePanel.setVisible(false);

		inventoryPanel = new InventoryPanel();
		inventoryPanel.setVisible(false);

		dialogPanel = new DialogPanel();
		dialogPanel.setVisible(false);

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

		// Add the game panel to the bottom layer and the others to the top layer
		layeredPane.add(panel, JLayeredPane.DEFAULT_LAYER);
		layeredPane.add(dialogPanel, JLayeredPane.MODAL_LAYER);
		layeredPane.add(pausePanel, JLayeredPane.POPUP_LAYER);
		layeredPane.add(inventoryPanel, JLayeredPane.POPUP_LAYER);
		layeredPane.add(homePanel, JLayeredPane.POPUP_LAYER);

		layeredPane.revalidate();
    }

	public PausePanel getPausePanel() { return pausePanel; }
	public InventoryPanel getInventoryPanel() { return inventoryPanel; }
	public DialogPanel getDialogPanel() { return dialogPanel; }
	public HomePanel getHomePanel() { return homePanel; }
}
