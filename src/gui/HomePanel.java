package gui;

import game.GameState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The home panel that appears when the game is started.
 */
public class HomePanel extends Panel {

    public HomePanel() {
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);
        setBounds(0, 0, GameFrame.WIDTH, GameFrame.HEIGHT);
        GameState gameState = GameState.HOME;

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Create the button panel
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBackground(Color.WHITE);

        // Create the buttons
        JButton newGameButton = new JButton("New Game");
        JButton loadGameButton = new JButton("Load Game");
        JButton settingsButton = new JButton("Settings");
        JButton muteButton = new JButton("Mute");

        // Set preferred size for the buttons
        Dimension buttonSize = new Dimension(200, 50);
        newGameButton.setPreferredSize(buttonSize);
        loadGameButton.setPreferredSize(buttonSize);
        settingsButton.setPreferredSize(buttonSize);
        muteButton.setPreferredSize(buttonSize);

        // Add the buttons to the button panel
        buttonPanel.add(newGameButton, gbc);
        gbc.gridy++;
        buttonPanel.add(loadGameButton, gbc);
        gbc.gridy++;
        buttonPanel.add(settingsButton, gbc);

        // Add the button panel to the main panel
        gbc.weighty = 1.0; // Make the button panel fill the remaining vertical space
        gbc.fill = GridBagConstraints.VERTICAL; // Set the fill to vertical
        add(buttonPanel, gbc);

        // Add the mute button to the bottom left corner
        gbc.gridy++;
        gbc.weighty = 0.0; // Reset the weighty to avoid filling the remaining space
        gbc.anchor = GridBagConstraints.SOUTHWEST; // Align to the bottom left
        add(muteButton, gbc);
    }

    public void newGameClicked() {
        // Handle new game button click
    }

    public void loadGameClicked() {
        // Handle load game button click
    }

    public void settingsClicked() {
        // Handle settings button click
    }

    public void muteClicked() {
        // Handle mute button click
    }
}

/*
public class HomePanel extends Panel {

    public HomePanel() {
        // set the bounds to have the size of the frame
        setBounds(0, 0, GameFrame.WIDTH, GameFrame.HEIGHT);
        setBackground(Color.white);
        GameState gameState = GameState.HOME;
    }

    public void newGameClicked() {

    }

    public void changeVolume() {

    }

    public void quitClicked() {

    }

    public void draw() {

    }

}
*/