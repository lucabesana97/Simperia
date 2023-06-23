package gui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.File;

/**
 * The pause panel that appears when the game is paused.
 */
public class PausePanel extends Panel {

    private JButton resumeButton;
    private JButton quitButton;
    private JButton muteButton;

    final Color BACKGROUND_COLOR = new Color(51,51,51);
    final Color TEXT_COLOR = new Color(0, 254,254);
    final Color BORDER_COLOR = new Color(0, 254,254);
    final int FONT_SIZE = 22;
    final Font customFont;

    public PausePanel() {
        setLayout(new GridBagLayout());
        setBackground(BACKGROUND_COLOR);
        setOpaque(true); // Makes sure the background color is visible
        setVisible(false);

        Border border = BorderFactory.createLineBorder(BORDER_COLOR, 2);
        setBorder(border);

        // Load new font
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("src/gui/fonts/TT Octosquares Trial Black.ttf")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        customFont = new Font("TT Octosquares Trl Blc", Font.PLAIN, FONT_SIZE);

        // Calculate the position to center the pause panel
        int pausePanelWidth = (int) (getWidth() * 0.6);
        int pausePanelHeight = (int) (getHeight() * 0.6);
        int panelX = (getWidth() - pausePanelWidth) / 2;
        int panelY = (getHeight() - pausePanelHeight) / 2;

        setBounds(panelX, panelY, pausePanelWidth, pausePanelHeight);

        // Create and customize buttons
        customizeButtons(border);

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

    private void customizeButtons(Border border) {
        // Buttons
        resumeButton = new JButton("RESUME");
        quitButton = new JButton("QUIT");
        muteButton = new JButton();

        // Set mute button icon
        ImageIcon muteIcon = new ImageIcon("resources/sprites/pause/music_playing.png");
        muteButton.setIcon(muteIcon);

        // Set button sizes
        Dimension buttonSize = new Dimension(150, 50);
        resumeButton.setPreferredSize(buttonSize);
        quitButton.setPreferredSize(buttonSize);
        muteButton.setPreferredSize(new Dimension(muteIcon.getIconWidth(), muteIcon.getIconHeight()));

        // Set button colors
        resumeButton.setBackground(BACKGROUND_COLOR);
        quitButton.setBackground(BACKGROUND_COLOR);
        muteButton.setBackground(BACKGROUND_COLOR);

        // Set button text colors
        resumeButton.setForeground(TEXT_COLOR);
        quitButton.setForeground(TEXT_COLOR);
        muteButton.setForeground(TEXT_COLOR);

        // Set button fonts
        resumeButton.setFont(customFont);
        quitButton.setFont(customFont);

        // Set button borders
        resumeButton.setBorder(border);
        quitButton.setBorder(border);
        muteButton.setBorder(border);
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
