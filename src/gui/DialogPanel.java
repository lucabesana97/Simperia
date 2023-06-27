package gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class DialogPanel extends Panel {

    private static final int DIALOG_PANEL_HEIGHT = 200;
    private final JTextPane dialogArea;
    private final JButton closeDialogButton;
    final Color BACKGROUND_COLOR = new Color(51,51,51);
    final Color MAIN_COLOR = new Color(0, 254,254);
    Image background;
    final int FONT_SIZE = 19;

    public DialogPanel() {
        super();
        this.setLayout(new BorderLayout());
        this.setBounds(0, HEIGHT - DIALOG_PANEL_HEIGHT -38, WIDTH, DIALOG_PANEL_HEIGHT);
        this.setFocusable(true);
        this.requestFocusInWindow();

        try {
            background = ImageIO.read(new File("resources/sprites/dialogue_panel.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Load new font
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("src/gui/fonts/TT Octosquares Trial Black.ttf")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Font customFont = new Font("TT Octosquares Trl Blc", Font.PLAIN, FONT_SIZE);

        dialogArea = new JTextPane();
        dialogArea.setOpaque(false);
        dialogArea.setEditable(false);
        dialogArea.setFont(customFont);
        dialogArea.setForeground(Color.WHITE);
        dialogArea.setBackground(new Color(0, 0, 0, 0));
        dialogArea.setMargin(new Insets(15, 18, 15, 24));

        StyledDocument doc = dialogArea.getStyledDocument();
        SimpleAttributeSet style = new SimpleAttributeSet();
        StyleConstants.setAlignment(style, StyleConstants.ALIGN_JUSTIFIED);
        doc.setParagraphAttributes(0, doc.getLength(), style, false);

        JScrollPane scrollPane = new JScrollPane(dialogArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setBackground(new Color(0, 0, 0, 0));
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        add(scrollPane, BorderLayout.NORTH);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(new Color(0, 0, 0, 0));

        JPanel buttonContainer = new JPanel();
        buttonContainer.setLayout(new BorderLayout());
        buttonContainer.setBorder(BorderFactory.createEmptyBorder(10, 0, 3, 55));
        buttonContainer.setBackground(new Color(0, 0, 0, 0));

        closeDialogButton = new JButton("CLOSE");
        closeDialogButton.setFont(customFont);
        closeDialogButton.setForeground(MAIN_COLOR);
        closeDialogButton.setBackground(BACKGROUND_COLOR);
        closeDialogButton.setBorderPainted(false);
        closeDialogButton.setPreferredSize(new Dimension(290, 40));

        buttonContainer.add(closeDialogButton, BorderLayout.CENTER);
        bottomPanel.add(buttonContainer);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, getWidth() -14, getHeight(), null); // image scaled
    }

    public void setDialogText(String s) {
        dialogArea.setText(s);
    }
    public JButton getCloseDialogButton() { return closeDialogButton; }
}
