package gui;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class VictoryFailPanel extends Panel{
    private final JTextPane textPane;
    private final JButton playButton;
    private final JLabel topLabel;
    final Color BACKGROUND_COLOR = new Color(51, 51, 51);
    final Color MAIN_COLOR = new Color(0, 254, 254);
    final int FONT_SIZE = 19;
    final Font customFont;

    public VictoryFailPanel() {
        super();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setFocusable(true);
        requestFocusInWindow();
        setBackground(BACKGROUND_COLOR);
        setBorder(BorderFactory.createLineBorder(MAIN_COLOR, 2));

        // Calculate the position to center the panel
        int vicPanelWidth = (int) (getWidth() * 0.6);
        int vicPanelHeight = (int) (getHeight() * 0.6);
        int panelX = (getWidth() - vicPanelWidth) / 2;
        int panelY = (getHeight() - vicPanelHeight) / 2;

        setBounds(panelX, panelY, vicPanelWidth, vicPanelHeight);

        // Load new font
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("src/gui/fonts/TT Octosquares Trial Black.ttf")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        customFont = new Font("TT Octosquares Trl Blc", Font.PLAIN, FONT_SIZE);

        topLabel = new JLabel();
        topLabel.setFont(customFont);
        topLabel.setForeground(Color.WHITE);
        topLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        topLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        textPane = new JTextPane();
        textPane.setEditable(false);
        textPane.setFont(customFont);
        textPane.setForeground(Color.WHITE);
        textPane.setBackground(BACKGROUND_COLOR);
        textPane.setMargin(new Insets(10, 10, 10, 10));

//        JScrollPane scrollPane = new JScrollPane(textPane);
//        scrollPane.setBorder(BorderFactory.createEmptyBorder());
//        scrollPane.setBackground(BACKGROUND_COLOR);
//        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
//        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
//        scrollPane.setViewportBorder(BorderFactory.createEmptyBorder(10, 10, 10, 0)); // Set the margin

        playButton = new JButton("PLAY AGAIN");
        playButton.setFont(customFont);
        playButton.setForeground(Color.WHITE);
        playButton.setBackground(BACKGROUND_COLOR);
        playButton.setBorder(BorderFactory.createLineBorder(MAIN_COLOR, 2));
        playButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        playButton.setMaximumSize(new Dimension(200, 50));

        add(topLabel);
        add(textPane);
        add(playButton);
    }

    public void setTopText(String text) {
        topLabel.setText(text);
    }

    public void setBottomText(String text) {
        textPane.setText(text);
    }

    public JButton getPlayButton() {
        return playButton;
    }

}
