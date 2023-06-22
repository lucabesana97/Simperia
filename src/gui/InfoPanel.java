package gui;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class InfoPanel extends Panel{
    private final JTextArea textArea;
    private final JButton closeButton;
    final Color BACKGROUND_COLOR = new Color(51,51,51);
    final Color MAIN_COLOR = new Color(0, 254,254);
    final Font customFont;

    public InfoPanel() {
        super();
        this.setLayout(new BorderLayout());
        this.setFocusable(true);
        this.requestFocusInWindow();

        // Calculate the position to center the info panel
        int infoPanelWidth = (int) (getWidth() * 0.6);
        int infoPanelHeight = (int) (getHeight() * 0.6);
        int panelX = (getWidth() - infoPanelWidth) / 2;
        int panelY = (getHeight() - infoPanelHeight) / 2;

        setBounds(panelX, panelY, infoPanelWidth, infoPanelHeight);

        // Load new font
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("src/gui/fonts/Computerfont.ttf")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        customFont = new Font("Computerfont", Font.PLAIN, 26);

        textArea = new JTextArea();
        textArea.setOpaque(false);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(customFont);
        textArea.setForeground(Color.WHITE);
        textArea.setBackground(new Color(0, 0, 0, 0));
        textArea.setMargin(new Insets(15, 18, 15, 22));
        add(textArea, BorderLayout.NORTH);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(new Color(0, 0, 0, 0));

        JPanel buttonContainer = new JPanel();
        buttonContainer.setLayout(new BorderLayout());
        buttonContainer.setBorder(BorderFactory.createEmptyBorder(10, 0, 3, 55));
        buttonContainer.setBackground(new Color(0, 0, 0, 0));

        closeButton = new JButton("CLOSE");
        closeButton.setFont(customFont);
        closeButton.setForeground(MAIN_COLOR);
        closeButton.setBackground(BACKGROUND_COLOR);
        //closeButton.setBorderPainted(false);
        //closeButton.setPreferredSize(new Dimension(290, 40));

        buttonContainer.add(closeButton, BorderLayout.CENTER);
        bottomPanel.add(buttonContainer);

        add(bottomPanel, BorderLayout.SOUTH);
    }
}
