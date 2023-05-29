package gui;

import javax.swing.*;
import java.awt.*;

/**
 * The pause panel that appears when the game is paused.
 */
public class PausePanel extends Panel {

    private JButton resumeButton;
    private JPanel panel;
    private JButton quitButton;
    private JButton muteButton;

    public PausePanel() {
        super();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); // Make the buttons appear vertically
        //panel.setSize(400, 400);
        this.add(panel);

        resumeButton.addActionListener(e -> {
            setVisible(false);
        });
        add(resumeButton);

        quitButton.addActionListener(e -> {
            // TODO: Go back to the home panel
        });
        add(quitButton);

        muteButton.addActionListener(e -> {
            // TODO: Mute the game
        });
        add(muteButton);
    }


    public void resumeClicked() {

    }

    public void quitClicked() {

    }

    public void draw() {

    }


}
