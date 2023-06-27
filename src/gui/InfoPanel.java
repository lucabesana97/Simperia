package gui;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.io.File;

public class InfoPanel extends Panel {
    private final JTextPane textPane;
    private final JButton closeButton;
    final Color BACKGROUND_COLOR = new Color(51, 51, 51);
    final Color MAIN_COLOR = new Color(0, 254, 254);
    final int FONT_SIZE = 18;
    final Font customFont;

    public InfoPanel() {
        super();
        setLayout(new BorderLayout());
        setFocusable(true);
        requestFocusInWindow();
        setBackground(BACKGROUND_COLOR);
        setBorder(BorderFactory.createLineBorder(MAIN_COLOR, 2));

        // Calculate the position to center the info panel
        int infoPanelWidth = (int) (getWidth() * 0.6);
        int infoPanelHeight = (int) (getHeight() * 0.7);
        int panelX = (getWidth() - infoPanelWidth) / 2;
        int panelY = (getHeight() - infoPanelHeight) / 2;

        setBounds(panelX, panelY, infoPanelWidth, infoPanelHeight);

        // Load new font
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("src/gui/fonts/TT Octosquares Trial Black.ttf")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        customFont = new Font("TT Octosquares Trl Blc", Font.PLAIN, FONT_SIZE);

        textPane = new JTextPane();
        textPane.setEditable(false);
        textPane.setFont(customFont);
        textPane.setForeground(Color.WHITE);
        textPane.setBackground(BACKGROUND_COLOR);

        StyledDocument doc = textPane.getStyledDocument();
        SimpleAttributeSet style = new SimpleAttributeSet();
        StyleConstants.setAlignment(style, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), style, false);

        JScrollPane scrollPane = new JScrollPane(textPane);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setBackground(BACKGROUND_COLOR);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setViewportBorder(BorderFactory.createEmptyBorder(10, 10, 5, 0)); // Set the margin

        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(BACKGROUND_COLOR);

        JPanel buttonContainer = new JPanel();
        buttonContainer.setLayout(new BorderLayout());
        buttonContainer.setBorder(BorderFactory.createEmptyBorder(10, 0, 3, 0));
        buttonContainer.setBackground(BACKGROUND_COLOR);

        closeButton = new JButton("CLOSE");
        closeButton.setFont(customFont);
        closeButton.setForeground(MAIN_COLOR);
        closeButton.setBackground(BACKGROUND_COLOR);
        closeButton.setBorderPainted(false);

        buttonContainer.add(closeButton, BorderLayout.CENTER);
        bottomPanel.add(buttonContainer);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void setInfoText(String s) {
        textPane.setText(s);
    }

    public JButton getCloseInfoButton() {
        return closeButton;
    }
}
