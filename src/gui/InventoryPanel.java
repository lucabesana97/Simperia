package gui;

import game.inventory.Inventory;
import main.Gameplay;
import output.Sound;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;

public class InventoryPanel extends Panel {

    private JLabel selectedSlotLabel; // Displays the name of the selected item
    private JLabel selectedSlotDescriptionLabel; // Displays the description of the selected item
    private JLabel[] itemCountLabels;
    private boolean[] slotSelected; // Keeps track of which slot is selected

    private JButton closeButton;
    private JPanel slotsPanel;
    private JPanel buttonsPanel;
    private Sound soundEffect = new Sound();

    private Inventory inventory;
    final Color BACKGROUND_COLOR = new Color(51,51,51);
    final Color MAIN_COLOR = new Color(0, 254,254);
    final Color COMPLEMENTARY_COLOR = new Color(249,206,55);
    final Border SELECTED_BORDER = BorderFactory.createLineBorder(COMPLEMENTARY_COLOR, 2);
    final Border UNSELECTED_BORDER = BorderFactory.createLineBorder(MAIN_COLOR, 2);
    private final String LABEL_FONT;
    final int FONT_SIZE_BUTTONS = 16;
    final int FONT_SIZE_ITEM_QUANTITIES = 16;
    final int FONT_SIZE_TOP_LABEL = 20;
    final int FONT_SIZE_DESCRIPTION = 18;

    public InventoryPanel() {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);
        setOpaque(true); // Makes sure the background color is visible
        setVisible(false);

        // Load new font
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("src/gui/fonts/TT Octosquares Trial Black.ttf")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        LABEL_FONT = "TT Octosquares Trl Blc";

        EmptyBorder marginBorder = new EmptyBorder(15, 15, 15, 15);
        LineBorder lineBorder = new LineBorder(MAIN_COLOR, 2);
        CompoundBorder compoundBorder = new CompoundBorder(lineBorder, marginBorder);
        setBorder(compoundBorder);

        // Calculate the position to center the inventory panel
        int panelWidth = (int) (GameFrame.WIDTH * 0.5);
        int panelHeight = (int) (GameFrame.HEIGHT * 0.6);
        int panelX = (getWidth() - panelWidth) / 2;
        int panelY = (getHeight() - panelHeight) / 2;

        setBounds(panelX, panelY, panelWidth, panelHeight);
    }

    public void init(Inventory inv) {
        this.inventory = inv;
        itemCountLabels = new JLabel[12];

        // Label to display the selected slot
        selectedSlotLabel = new JLabel();
        selectedSlotLabel.setFont(new Font(LABEL_FONT, Font.PLAIN, FONT_SIZE_TOP_LABEL));
        selectedSlotLabel.setForeground(MAIN_COLOR);
        selectedSlotLabel.setHorizontalAlignment(SwingConstants.CENTER);

        selectedSlotDescriptionLabel = new JLabel();
        selectedSlotDescriptionLabel.setFont(new Font(LABEL_FONT, Font.PLAIN, FONT_SIZE_DESCRIPTION));
        selectedSlotDescriptionLabel.setForeground(MAIN_COLOR);
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
        constraints.insets = new Insets(0, 10, 5, 10); // Add spacing around the labels

        labelsPanel.add(selectedSlotLabel, constraints);

        constraints.gridy = 1;
        constraints.insets = new Insets(0, 10, 13, 10);
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
            slotButton.setBorder(UNSELECTED_BORDER);

            slotButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!Gameplay.isMuted) {
                        soundEffect.playSoundEffect(9);
                    }
                    clearSelectedSlot();
                    slotSelected[slotIndex] = true;
                    slotButton.setBorder(SELECTED_BORDER);
                    JLabel slotLabel = (JLabel) ((JPanel) slotsPanel.getComponent(slotIndex)).getComponent(1);
                    if (inventory.slots[slotIndex] != null) {
                        // Update the label with the selected item name
                        selectedSlotLabel.setText(inventory.slots[slotIndex].item.name.toUpperCase());
                        selectedSlotDescriptionLabel.setText(inventory.slots[slotIndex].item.description);
                        slotLabel.setForeground(COMPLEMENTARY_COLOR);
                    } else {
                        slotLabel.setForeground(BACKGROUND_COLOR);
                        updateLabelsToEmptyItems();
                    }

                }
            });

            // Label for displaying the item count
            itemCountLabels[i] = new JLabel(".");
            itemCountLabels[i].setFont(new Font(LABEL_FONT, Font.PLAIN, FONT_SIZE_ITEM_QUANTITIES));
            itemCountLabels[i].setHorizontalAlignment(SwingConstants.CENTER);
            if (inventory.slots[i] == null) {
                itemCountLabels[i].setForeground(BACKGROUND_COLOR);
            } else {
                itemCountLabels[i].setForeground(MAIN_COLOR);
            }

            // Panel to hold the button and the count label
            JPanel buttonAndLabelPanel = new JPanel();
            buttonAndLabelPanel.setLayout(new BorderLayout());
            buttonAndLabelPanel.setBackground(BACKGROUND_COLOR);
            buttonAndLabelPanel.add(slotButton, BorderLayout.CENTER);
            buttonAndLabelPanel.add(itemCountLabels[i], BorderLayout.SOUTH);

            slotsPanel.add(buttonAndLabelPanel);
        }

        buttonsPanel = new JPanel(new FlowLayout());
        buttonsPanel.setBackground(BACKGROUND_COLOR);
        add(buttonsPanel, BorderLayout.SOUTH);

        customizeButtons();
    }

    private void customizeButtons() {

        JButton useItemButton = new JButton("USE ITEM");
        useItemButton.setFont(new Font(LABEL_FONT, Font.PLAIN, FONT_SIZE_BUTTONS));
        useItemButton.setForeground(MAIN_COLOR);
        useItemButton.setBackground(BACKGROUND_COLOR);
        useItemButton.setBorder(UNSELECTED_BORDER);
        useItemButton.addActionListener(e -> {
            // Handle "Use Item" button click
            if (!Gameplay.isMuted) {
                soundEffect.playSoundEffect(9);
            }
            int selectedSlot = getSelectedSlot();
            if (selectedSlot != -1 && inventory.slots[selectedSlot] != null) {
                // Use the item in the selected slot
                System.out.println("Using item " + inventory.slots[selectedSlot].item.name);
                inventory.useItem(selectedSlot);
                updateSlot(selectedSlot);
                //Gameplay.healthBar.updateHealthBar();
                //Gameplay.healthBar.update(Gameplay.player.health);
                //System.out.println("Player health: " + Gameplay.player.health);
            } else {
                System.out.println("No slot selected");
            }
        });
        useItemButton.addMouseListener(new MouseAdapter() { // Change button border and text colors when pressed
            @Override
            public void mousePressed(MouseEvent e) {
                useItemButton.setBorder(SELECTED_BORDER);
                useItemButton.setForeground(COMPLEMENTARY_COLOR);
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                useItemButton.setBorder(UNSELECTED_BORDER);
                useItemButton.setForeground(MAIN_COLOR);
            }
        });

        JButton throwItemButton = new JButton("REMOVE ITEM");
        throwItemButton.setFont(new Font(LABEL_FONT, Font.PLAIN, FONT_SIZE_BUTTONS));
        throwItemButton.setForeground(MAIN_COLOR);
        throwItemButton.setBackground(BACKGROUND_COLOR);
        throwItemButton.setBorder(UNSELECTED_BORDER);
        throwItemButton.addActionListener(e -> {
            if (!Gameplay.isMuted) {
                soundEffect.playSoundEffect(9);
            }
            int selectedSlot = getSelectedSlot();
            if (selectedSlot != -1 && inventory.slots[selectedSlot] != null) {
                // Throw away the item in the selected slot
                System.out.println("Throwing away item " + inventory.slots[selectedSlot].item.name);
                inventory.removeItem(selectedSlot);
                updateSlot(selectedSlot);
            } else {
                System.out.println("No slot selected");
            }
        });
        throwItemButton.addMouseListener(new MouseAdapter() { // Change button border and text colors when pressed
            @Override
            public void mousePressed(MouseEvent e) {
                throwItemButton.setBorder(SELECTED_BORDER);
                throwItemButton.setForeground(COMPLEMENTARY_COLOR);
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                throwItemButton.setBorder(UNSELECTED_BORDER);
                throwItemButton.setForeground(MAIN_COLOR);
            }
        });

        closeButton = new JButton("CLOSE");
        closeButton.setFont(new Font(LABEL_FONT, Font.PLAIN, FONT_SIZE_BUTTONS));
        closeButton.setForeground(MAIN_COLOR);
        closeButton.setBackground(BACKGROUND_COLOR);
        closeButton.setBorder(UNSELECTED_BORDER);
        closeButton.addMouseListener(new MouseAdapter() { // Change button border and text colors when pressed
            @Override
            public void mousePressed(MouseEvent e) {
                closeButton.setBorder(SELECTED_BORDER);
                closeButton.setForeground(COMPLEMENTARY_COLOR);
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                closeButton.setBorder(UNSELECTED_BORDER);
                closeButton.setForeground(MAIN_COLOR);
            }
        });

        // Add the buttons to the panel with spacing
        buttonsPanel.add(useItemButton);
        buttonsPanel.add(Box.createHorizontalStrut(8)); // Add 8 pixels of horizontal spacing
        buttonsPanel.add(throwItemButton);
        buttonsPanel.add(Box.createHorizontalStrut(8));
        buttonsPanel.add(closeButton);
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
    }

    private void updateLabelsToEmptyItems() {
        selectedSlotLabel.setText("NO ITEM SELECTED");
        selectedSlotDescriptionLabel.setText(" ");
    }

    private void updateLabelsToExistingItems(int selectedSlot) {
        selectedSlotLabel.setText(inventory.slots[selectedSlot].item.name);
        selectedSlotDescriptionLabel.setText(inventory.slots[selectedSlot].item.description);
    }

    private int getSelectedSlot() {
        for (int i = 0; i < 12; i++) {
            if (slotSelected[i]) {
                return i;
            }
        }
        return -1; // No slot selected
    }

    public void clearSelectedSlot() {
        // Clear selection for all slots and clear border and label colors
        for (int j = 0; j < 12; j++) {
            JPanel buttonAndLabelPanel = (JPanel) slotsPanel.getComponent(j);
            JButton button = (JButton) buttonAndLabelPanel.getComponent(0);
            JLabel label = (JLabel) buttonAndLabelPanel.getComponent(1);
            button.setBorder(UNSELECTED_BORDER);
            if (inventory.slots[j] == null) {
                label.setForeground(BACKGROUND_COLOR);
            } else {
                label.setForeground(MAIN_COLOR);
            }
            slotSelected[j] = false;
            updateLabelsToEmptyItems();
        }
    }

    public JButton getCloseButton() {
        return closeButton;
    }

    // Update the item slots when the inventory is updated
    public void updateInventoryUI() {
        for (int i = 0; i < 12; i++) {
            //System.out.println("Slot " + i + ": " + inventory.slots[i].item.name + " x" + inventory.slots[i].amount);
            updateSlot(i);
        }
    }

    // Update image and item count label
    private void updateSlot(int i) {
        JPanel buttonAndLabelPanel = (JPanel) slotsPanel.getComponent(i);
        JButton button = (JButton) buttonAndLabelPanel.getComponent(0);

        if (inventory.slots[i] != null) { // Items exist in the stack
            itemCountLabels[i].setText("x" + inventory.slots[i].amount); // Update the item count label
            if (slotSelected[i]) {
                itemCountLabels[i].setForeground(COMPLEMENTARY_COLOR);
            } else {
                itemCountLabels[i].setForeground(MAIN_COLOR);
            }
            ImageIcon imageIcon = new ImageIcon(inventory.slots[i].item.sprite);
            button.setIcon(imageIcon);
            //updateLabelsToExistingItems(i);
        } else { // Empty stack
            button.setIcon(null);
            itemCountLabels[i].setText("."); // Clear the item count label
            itemCountLabels[i].setForeground(BACKGROUND_COLOR);
            updateLabelsToEmptyItems();
        }
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

}

