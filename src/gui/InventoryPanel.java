package gui;

import javax.swing.*;
import java.awt.*;

public class InventoryPanel extends Panel {

    public InventoryPanel() {
        setBackground(Color.orange);
        setVisible(false);
        //setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); // Make the buttons appear vertically

        // Calculate the position to center the panel
        int panelWidth = (int) (this.getWidth() * 0.5);
        int panelHeight = (int) (this.getHeight() * 0.5);
        int panelX = (this.getWidth() - panelWidth) / 2;
        int panelY = (this.getHeight() - panelHeight) / 2;

        setBounds(panelX, panelY, panelWidth, panelHeight);

        // Buttons
        JButton useItemButton = new JButton("Use Item");
        useItemButton.addActionListener(e -> { useItemClicked(); });
        add(useItemButton);

        JButton dropItemButton = new JButton("Drop Item");
        dropItemButton.addActionListener(e -> { dropItemClicked(); });
        add(dropItemButton);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> { closeInventory(); });
        add(closeButton);
        
        JButton throwAwayButton = new JButton("Throw Away");
        throwAwayButton.addActionListener(e -> { throwAwayClicked(); });
        add(throwAwayButton);

        // Set the buttons to the middle of the panel
        useItemButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        dropItemButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        closeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    private void throwAwayClicked() {
    }

    public void useItemClicked() {

    }

    public void dropItemClicked() {

    }

    public void closeInventory() {
        setVisible(false);
    }

    public void draw() {

    }
}
