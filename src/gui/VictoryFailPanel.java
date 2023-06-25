package gui;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.io.File;

public class VictoryFailPanel extends Panel{
    private final JTextPane textPane;
    private final JButton playButton;
    private final JLabel topLabel;
    final Color BACKGROUND_COLOR = new Color(51, 51, 51);
    final Color MAIN_COLOR = new Color(0, 254, 254);
    final int FONT_SIZE_TITLE = 27;
    final int FONT_SIZE_TEXT = 19;
    final String customFont;

    public VictoryFailPanel() {
        super();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setFocusable(true);
        requestFocusInWindow();
        setBackground(BACKGROUND_COLOR);
        setOpaque(true);
        setBorder(BorderFactory.createLineBorder(MAIN_COLOR, 2));

        // Calculate the position to center the panel
        int vicPanelWidth = (int) (getWidth() * 0.5);
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
        customFont = "TT Octosquares Trl Blc";

        topLabel = new JLabel();
        topLabel.setFont(new Font(customFont, Font.PLAIN, FONT_SIZE_TITLE));
        topLabel.setForeground(Color.WHITE);
        topLabel.setBackground(BACKGROUND_COLOR);
        topLabel.setOpaque(true);
        topLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        topLabel.setBorder(BorderFactory.createEmptyBorder(70, 10, 30, 10));

        textPane = new JTextPane();
        textPane.setEditable(false);
        textPane.setFont(new Font(customFont, Font.PLAIN, FONT_SIZE_TEXT));
        textPane.setForeground(Color.WHITE);
        textPane.setBackground(BACKGROUND_COLOR);
        textPane.setSize(new Dimension(vicPanelWidth,vicPanelHeight / 2));
        textPane.setMargin(new Insets(10, 10, 10, 10));

        // Center-align the text in the text pane
        StyledDocument doc = textPane.getStyledDocument();
        SimpleAttributeSet centerAlign = new SimpleAttributeSet();
        StyleConstants.setAlignment(centerAlign, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), centerAlign, false);

        JScrollPane scrollPane = new JScrollPane(textPane);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setBackground(BACKGROUND_COLOR);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setSize(new Dimension(vicPanelWidth,vicPanelHeight / 2));
        scrollPane.setViewportBorder(BorderFactory.createEmptyBorder(10, 10, 10, 0)); // Set the margin

        playButton = new JButton("PLAY AGAIN");
        playButton.setFont(new Font(customFont, Font.PLAIN, FONT_SIZE_TEXT));
        playButton.setForeground(Color.WHITE);
        playButton.setBackground(BACKGROUND_COLOR);
        playButton.setBorder(BorderFactory.createLineBorder(MAIN_COLOR, 2));
        playButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        playButton.setMaximumSize(new Dimension(180, 50));
        playButton.setPreferredSize(new Dimension(180, 50));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 70, 0));
        buttonPanel.add(playButton);

        this.add(topLabel);
        this.add(scrollPane);
        this.add(buttonPanel);
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
