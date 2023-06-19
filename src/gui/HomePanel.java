//package gui;
//
//import javax.swing.*;
//import java.awt.*;
//
///**
// * The home panel that appears when the game is started.
// */
//public class HomePanel extends Panel {
//
//    final JButton newGameButton;
//
//    final Color BACKGROUND_COLOR = new Color(51,51,51);
//    final Color TEXT_COLOR = new Color(0, 254,254);
//    final Color BORDER_COLOR = new Color(0, 254,254);
//    final String LABEL_FONT = "Helvetica";
//
//    public HomePanel() {
//        super();
//        setLayout(new GridBagLayout());
//        setFocusable(true);
//        setBackground(BACKGROUND_COLOR);
//        setPreferredSize(new Dimension(GameFrame.WIDTH, GameFrame.HEIGHT));
//
//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.gridx = 0;
//        gbc.gridy = 0;
//        gbc.insets = new Insets(10, 10, 10, 10);
//        gbc.anchor = GridBagConstraints.NORTHWEST;
//
//        // Create the button panel
//        JPanel buttonPanel = new JPanel(new GridBagLayout());
//        buttonPanel.setBackground(BACKGROUND_COLOR);
//
//        // Set the button
//        newGameButton = new JButton("NEW GAME");
//        newGameButton.setFont(new Font(LABEL_FONT, Font.PLAIN, 18));
//        newGameButton.setForeground(TEXT_COLOR);
//        newGameButton.setBackground(BACKGROUND_COLOR);
//        newGameButton.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 2));
//        newGameButton.setPreferredSize(new Dimension(200, 50));
//
//        buttonPanel.add(newGameButton, gbc);
//
//        gbc.weightx = 1.0; // Make the button panel fill the remaining horizontal space
//        gbc.weighty = 1.0; // Make the button panel fill the remaining vertical space
//        gbc.fill = GridBagConstraints.BOTH;
//
//        add(buttonPanel, gbc);
//    }
//
//    public JButton getNewGameButton() { return newGameButton; }
//}

package gui;

import javax.swing.*;
import java.awt.*;

/**
 * The home panel that appears when the game is started.
 */
public class HomePanel extends Panel {

    final JButton newGameButton;

    final Color BACKGROUND_COLOR = new Color(51, 51, 51);
    final Color TEXT_COLOR = new Color(0, 254, 254);
    final Color BORDER_COLOR = new Color(0, 254, 254);
    final String LABEL_FONT = "Helvetica";

    public HomePanel() {
        setLayout(new BorderLayout());
        setFocusable(true);
        setBackground(BACKGROUND_COLOR);

        // Create the button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        // Create empty component for vertical centering
        JPanel topPanel = new JPanel();
        topPanel.setPreferredSize(new Dimension(0, 359));
        topPanel.setBackground(BACKGROUND_COLOR);

        // Set the button
        newGameButton = new JButton("NEW GAME");
        newGameButton.setFont(new Font(LABEL_FONT, Font.PLAIN, 18));
        newGameButton.setForeground(TEXT_COLOR);
        newGameButton.setBackground(BACKGROUND_COLOR);
        newGameButton.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 2));
        newGameButton.setPreferredSize(new Dimension(200, 50));

        buttonPanel.add(newGameButton);

        add(topPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
    }

    public JButton getNewGameButton() {
        return newGameButton;
    }
}

