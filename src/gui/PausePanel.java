package gui;

import javax.swing.*;
import java.awt.*;

/**
 * The pause panel that appears when the game is paused.
 */
public class PausePanel extends Panel {

    private JButton resumeButton;
    private JButton quitButton;
    private JButton muteButton;


    public PausePanel() {

        // Use GridBagLayout for precise positioning
        setLayout(new GridBagLayout());
        setBackground(Color.pink);
        setVisible(false);

        // Calculate the position to center the pause panel
        int pausePanelWidth = (int) (getWidth() * 0.6);
        int pausePanelHeight = (int) (getHeight() * 0.6);
        int panelX = (getWidth() - pausePanelWidth) / 2;
        int panelY = (getHeight() - pausePanelHeight) / 2;

        setBounds(panelX, panelY, pausePanelWidth, pausePanelHeight);

        // Buttons
        resumeButton = new JButton("Resume");
        quitButton = new JButton("Quit");
        muteButton = new JButton("Mute");

        // Set button sizes
        Dimension buttonSize = new Dimension(150, 50); // Adjust the width and height as needed
        resumeButton.setPreferredSize(buttonSize);
        quitButton.setPreferredSize(buttonSize);
        muteButton.setPreferredSize(buttonSize);

        // Add buttons to the panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 0, 10, 0); // Add spacing between buttons
        gbc.anchor = GridBagConstraints.CENTER;
        add(resumeButton, gbc);

        gbc.gridy = 1;
        add(quitButton, gbc);

        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.PAGE_END; // Align mute button to the bottom
        gbc.insets = new Insets(50, 0, 0, 0); // Add spacing between buttons and bottom edge
        add(muteButton, gbc);
    }

    public void draw() {

    }

    public JButton getResumeButton() {
        return resumeButton;
    }

    public JButton getQuitButton() {
        return quitButton;
    }

    public JButton getMuteButton() {
        return muteButton;
    }
}
