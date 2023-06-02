package gui;

import javax.swing.*;
import java.awt.*;

/**
 * The pause panel that appears when the game is paused.
 */
public class PausePanel extends Panel {

    public PausePanel() {
        super();
        //setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); // Make the buttons appear vertically
        setBackground(Color.pink);
        setVisible(false);

        // Calculate the position to center the pause panel
        int pausePanelWidth = (int) (this.getWidth() * 0.5);
        int pausePanelHeight = (int) (this.getHeight() * 0.5);
        int panelX = (this.getWidth() - pausePanelWidth) / 2;
        int panelY = (this.getHeight() - pausePanelHeight) / 2;

        setBounds(panelX, panelY, pausePanelWidth, pausePanelHeight);

        // Buttons
        JButton resumeButton = new JButton("Resume");
        resumeButton.addActionListener(e -> { resumeClicked(); });
        add(resumeButton);

        JButton quitButton = new JButton("Quit");
        quitButton.addActionListener(e -> { quitClicked(); });
        add(quitButton);

        JButton muteButton = new JButton("Mute");
        muteButton.addActionListener(e -> { muteClicked(); });
        add(muteButton);

        // Set the buttons to the middle of the panel
        resumeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        quitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        muteButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    public void resumeClicked() {
        setVisible(false);
    }

    public void quitClicked() {
        // TODO: Go back to the home panel
    }

    public void muteClicked() {
        // TODO: Mute the game
    }

    public void draw() {

    }


}
