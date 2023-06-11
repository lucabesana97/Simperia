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
        super();
        //setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); // Make the buttons appear vertically
        setBackground(Color.pink);
        setVisible(false);

        // Calculate the position to center the pause panel
        int pausePanelWidth = (int) (getWidth() * 0.5);
        int pausePanelHeight = (int) (getHeight() * 0.5);
        int panelX = (getWidth() - pausePanelWidth) / 2;
        int panelY = (getHeight() - pausePanelHeight) / 2;

        setBounds(panelX, panelY, pausePanelWidth, pausePanelHeight);

        // Buttons
        resumeButton = new JButton("Resume");
        add(resumeButton);

        quitButton = new JButton("Quit");
        add(quitButton);

        muteButton = new JButton("Mute");
        add(muteButton);

        // Set the buttons to the middle of the panel
        resumeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        quitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        muteButton.setAlignmentX(Component.CENTER_ALIGNMENT);
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


//    @Override
//    protected void paintComponent(Graphics g) {
//        super.paintComponent(g);
//
//        // Calculate the position to center the pause panel
//        int pausePanelWidth = (int) (getWidth() * 0.5);
//        int pausePanelHeight = (int) (getHeight() * 0.5);
//        int panelX = (getWidth() - pausePanelWidth) / 2;
//        int panelY = (getHeight() - pausePanelHeight) / 2;
//
//        setBounds(panelX, panelY, pausePanelWidth, pausePanelHeight);
//
//        g.setColor(getBackground());
//        g.fillRect(0, 0, getWidth(), getHeight());
//    }

}
