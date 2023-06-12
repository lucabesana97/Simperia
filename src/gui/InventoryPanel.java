package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InventoryPanel extends Panel {

    private JLabel selectedSlotLabel;
    private boolean[] slotSelected;

    JButton useItemButton;
    JButton throwItemButton;
    JButton closeButton;

    public InventoryPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setOpaque(true); // Makes sure the background color is visible
        setVisible(false);

        // Calculate the position to center the inventory panel
        int panelWidth = (int) (GameFrame.WIDTH * 0.6);
        int panelHeight = (int) (GameFrame.HEIGHT * 0.6);
        int panelX = (getWidth() - panelWidth) / 2;
        int panelY = (getHeight() - panelHeight) / 2;

        setBounds(panelX, panelY, panelWidth, panelHeight);

        selectedSlotLabel = new JLabel("No slot selected");
        selectedSlotLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(selectedSlotLabel, BorderLayout.NORTH);

        JPanel slotsPanel = new JPanel(new GridLayout(3, 4, 10, 10));
        slotsPanel.setBackground(Color.WHITE);
        add(slotsPanel, BorderLayout.CENTER);

        slotSelected = new boolean[12];
        for (int i = 0; i < 12; i++) {
            JButton slotButton = new JButton("Slot " + (i + 1));

            //slotButton.setSize(40, 40);

            final int slotIndex = i;
            slotButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Clear selection for all slots
                    for (int j = 0; j < 12; j++) {
                        slotSelected[j] = false;
                    }

                    // Set the selected slot
                    slotSelected[slotIndex] = true;

                    // Update the label with the selected slot name
                    selectedSlotLabel.setText("Selected Slot: " + (slotIndex + 1));
                }
            });

            slotsPanel.add(slotButton);
        }

        JPanel buttonsPanel = new JPanel(new FlowLayout());
        buttonsPanel.setBackground(Color.WHITE);
        add(buttonsPanel, BorderLayout.SOUTH);

        useItemButton = new JButton("Use Item");
        useItemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle "Use Item" button click
                int selectedSlot = getSelectedSlot();
                if (selectedSlot != -1) {
                    // Use the item in the selected slot
                    System.out.println("Using item in slot " + selectedSlot);
                } else {
                    System.out.println("No slot selected");
                }
            }
        });
        buttonsPanel.add(useItemButton);

        throwItemButton = new JButton("Throw Item Away");
        throwItemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle "Throw Item Away" button click
                int selectedSlot = getSelectedSlot();
                if (selectedSlot != -1) {
                    // Throw away the item in the selected slot
                    System.out.println("Throwing away item in slot " + selectedSlot);
                } else {
                    System.out.println("No slot selected");
                }
            }
        });
        buttonsPanel.add(throwItemButton);

        closeButton = new JButton("Close");
        buttonsPanel.add(closeButton);
    }

    private int getSelectedSlot() {
        for (int i = 0; i < 12; i++) {
            if (slotSelected[i]) {
                return i + 1;
            }
        }
        return -1; // No slot selected
    }

    // Getters
    public JLabel getSelectedSlotLabel() {
        return selectedSlotLabel;
    }

    public boolean[] getSlotSelected() {
        return slotSelected;
    }

    public JButton getUseItemButton() {
        return useItemButton;
    }

    public JButton getThrowItemButton() {
        return throwItemButton;
    }

    public JButton getCloseButton() {
        return closeButton;
    }

    public JButton throwItemButton() {
        return throwItemButton;
    }

    public JButton useItemButton() {
        return useItemButton;
    }
}


/*
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

        // Set the buttons to the middle of the panel
        useItemButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        dropItemButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        closeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
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
*/