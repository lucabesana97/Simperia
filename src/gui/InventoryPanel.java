package gui;

import game.inventory.Inventory;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class InventoryPanel extends Panel {

    private JLabel selectedSlotLabel;
    private JLabel selectedSlotDescriptionLabel;
    private boolean[] slotSelected;

    private JButton closeButton;
    private JPanel slotsPanel;
    private JPanel buttonsPanel;

    private Inventory inventory;
    final Color BACKGROUND_COLOR = new Color(51,51,51);
    final Color TEXT_COLOR = new Color(0, 254,254);
    final Color BORDER_COLOR = new Color(0, 254,254);
    final String LABEL_FONT = "Helvetica";
    final int FONT_SIZE = 17;

    public InventoryPanel() {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);
        setOpaque(true); // Makes sure the background color is visible
        setVisible(false);

        Border border = BorderFactory.createLineBorder(BORDER_COLOR, 2);
        setBorder(border);

        // Calculate the position to center the inventory panel
        int panelWidth = (int) (GameFrame.WIDTH * 0.6);
        int panelHeight = (int) (GameFrame.HEIGHT * 0.6);
        int panelX = (getWidth() - panelWidth) / 2;
        int panelY = (getHeight() - panelHeight) / 2;

        setBounds(panelX, panelY, panelWidth, panelHeight);
    }

    public void init(Inventory inv) {
        this.inventory = inv;

        // Label to display the selected slot
        selectedSlotLabel = new JLabel();
        selectedSlotLabel.setFont(new Font(LABEL_FONT, Font.PLAIN, 18));
        selectedSlotLabel.setForeground(TEXT_COLOR);
        selectedSlotLabel.setHorizontalAlignment(SwingConstants.CENTER);

        selectedSlotDescriptionLabel = new JLabel();
        selectedSlotDescriptionLabel.setFont(new Font(LABEL_FONT, Font.PLAIN, 15));
        selectedSlotDescriptionLabel.setForeground(TEXT_COLOR);
        selectedSlotDescriptionLabel.setHorizontalAlignment(SwingConstants.CENTER);

        updateLabelsToEmptyItems();

        // Panel to hold the labels
        JPanel labelsPanel = new JPanel(new GridBagLayout());
        labelsPanel.setBackground(BACKGROUND_COLOR);

        // GridBagConstraints to set the alignment to center
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(15, 10, 10, 10); // Add spacing around the labels

        labelsPanel.add(selectedSlotLabel, constraints);

        constraints.gridy = 1;
        constraints.insets = new Insets(2, 10, 5, 10);
        labelsPanel.add(selectedSlotDescriptionLabel, constraints);

        add(labelsPanel, BorderLayout.NORTH);

        // Panel to hold the inventory slots
        slotsPanel = new JPanel(new GridLayout(3, 4, 10, 10));
        slotsPanel.setBackground(BACKGROUND_COLOR);
        add(slotsPanel, BorderLayout.CENTER);

        slotSelected = new boolean[12];

        for (int i = 0; i < 12; i++) {

            JButton slotButton = new JButton();
            final int slotIndex = i;

            slotButton.setContentAreaFilled(false);
            slotButton.setBorderPainted(false);

            // If items exists
            if (inventory.slots[i] != null) {
                BufferedImage itemSprite = inventory.slots[i].item.sprite;

                // Scale the image to fit the button
                ImageIcon scaledImageIcon = scaleImage(itemSprite, slotButton);
                slotButton.setIcon(scaledImageIcon);
            }

            slotButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Clear selection for all slots
                    for (int j = 0; j < 12; j++) {
                        slotSelected[j] = false;
                    }

                    // Set the selected slot
                    slotSelected[slotIndex] = true;

                    if (inventory.slots[slotIndex] != null) {
                        // Update the label with the selected item name
                        selectedSlotLabel.setText(inventory.slots[slotIndex].item.name.toUpperCase());
                        selectedSlotDescriptionLabel.setText(inventory.slots[slotIndex].item.description);
                    } else {
                        updateLabelsToEmptyItems();
                    }

                }
            });
            slotsPanel.add(slotButton);
        }

        buttonsPanel = new JPanel(new FlowLayout());
        buttonsPanel.setBackground(BACKGROUND_COLOR);
        add(buttonsPanel, BorderLayout.SOUTH);

        customizeButtons();
    }

    private void customizeButtons() {
        Border border = BorderFactory.createLineBorder(BORDER_COLOR, 2);

        JButton useItemButton = new JButton("USE ITEM");
        useItemButton.setFont(new Font(LABEL_FONT, Font.PLAIN, FONT_SIZE));
        useItemButton.setForeground(TEXT_COLOR);
        useItemButton.setBackground(BACKGROUND_COLOR);
        useItemButton.setBorder(border);
        useItemButton.setPreferredSize(new Dimension(100, 25));
        useItemButton.addActionListener(e -> {
            // Handle "Use Item" button click
            int selectedSlot = getSelectedSlot();
            if (selectedSlot != -1 && inventory.slots[selectedSlot - 1] != null) { // -1 because the slot numbers start from 1
                // Use the item in the selected slot
                System.out.println("Using item " + inventory.slots[selectedSlot - 1].item.name);
                inventory.useItem(selectedSlot - 1);
                updateInventoryUI();

                // Update the slot button image and labels
                if (inventory.slots[selectedSlot - 1] == null) {
                    ((JButton) slotsPanel.getComponent(selectedSlot - 1)).setIcon(null);
                    updateLabelsToEmptyItems();
                } else {
                    updateButton(selectedSlot - 1);
                    updateLabelsToExistingItems(selectedSlot - 1);
                }
            } else {
                System.out.println("No slot selected");
            }
        });

        JButton throwItemButton = new JButton("REMOVE ITEM");
        throwItemButton.setFont(new Font(LABEL_FONT, Font.PLAIN, FONT_SIZE));
        throwItemButton.setForeground(TEXT_COLOR);
        throwItemButton.setBackground(BACKGROUND_COLOR);
        throwItemButton.setBorder(border);
        throwItemButton.setPreferredSize(new Dimension(135, 25));
        throwItemButton.addActionListener(e -> {
            int selectedSlot = getSelectedSlot();
            if (selectedSlot != -1 && inventory.slots[selectedSlot - 1] != null) {
                // Throw away the item in the selected slot
                System.out.println("Throwing away item " + inventory.slots[selectedSlot - 1].item.name);
                inventory.removeItem(selectedSlot - 1);

                // Update the slot button image and labels
                if (inventory.slots[selectedSlot - 1] == null) {
                    ((JButton) slotsPanel.getComponent(selectedSlot - 1)).setIcon(null);
                    updateLabelsToEmptyItems();
                } else {
                    updateButton(selectedSlot - 1);
                    updateLabelsToExistingItems(selectedSlot - 1);
                }
            } else {
                System.out.println("No slot selected");
            }
        });

        closeButton = new JButton("CLOSE");
        closeButton.setFont(new Font(LABEL_FONT, Font.PLAIN, FONT_SIZE));
        closeButton.setForeground(TEXT_COLOR);
        closeButton.setBackground(BACKGROUND_COLOR);
        closeButton.setBorder(border);
        closeButton.setPreferredSize(new Dimension(75, 25));

        // Add the buttons to the panel with spacing
        buttonsPanel.add(useItemButton);
        buttonsPanel.add(Box.createHorizontalStrut(8)); // Add 8 pixels of horizontal spacing
        buttonsPanel.add(throwItemButton);
        buttonsPanel.add(Box.createHorizontalStrut(8));
        buttonsPanel.add(closeButton);
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
    }

    private void updateButton(int selectedSlot) {
        ImageIcon scaledImageIcon = scaleImage(inventory.slots[selectedSlot - 1].item.sprite, (JButton) slotsPanel.getComponent(selectedSlot - 1));
        ((JButton) slotsPanel.getComponent(selectedSlot - 1)).setIcon(scaledImageIcon);
    }

    private void updateLabelsToEmptyItems() {
        selectedSlotLabel.setText("NO ITEM SELECTED");
        selectedSlotDescriptionLabel.setText(" ");
    }

    private void updateLabelsToExistingItems(int selectedSlot) {
        selectedSlotLabel.setText(inventory.slots[selectedSlot - 1].item.name);
        selectedSlotDescriptionLabel.setText(inventory.slots[selectedSlot - 1].item.description);
    }

    private int getSelectedSlot() {
        for (int i = 0; i < 12; i++) {
            if (slotSelected[i]) {
                return i + 1;
            }
        }
        return -1; // No slot selected
    }

    public JButton getCloseButton() {
        return closeButton;
    }

    private ImageIcon scaleImage (BufferedImage itemSprite, JButton slotButton) {
        // Load the image
        ImageIcon imageIcon = new ImageIcon(itemSprite);
        Image image = imageIcon.getImage();

        // Get the image dimensions
        int imageWidth = imageIcon.getIconWidth();
        int imageHeight = imageIcon.getIconHeight();

        // Calculate the desired button size based on image dimensions
        int buttonSize = Math.max(imageWidth, imageHeight);
        slotButton.setSize(new Dimension(buttonSize, buttonSize));

        // Resize the image to match the button's size
        Image scaledImage = image.getScaledInstance(buttonSize, buttonSize, Image.SCALE_SMOOTH);

        // Create an image icon with the scaled image
        return new ImageIcon(scaledImage);
    }

    // Update the item slots when the inventory is updated
    public void updateInventoryUI() {
        for (int i = 0; i < 12; i++) {
            System.out.println("Slot " + i + ": " + inventory.slots[i]);
            if (inventory.slots[i] != null) {
                updateButton(i + 1);
            } else {
                ((JButton) slotsPanel.getComponent(i)).setIcon(null);
            }
        }
    }
}

