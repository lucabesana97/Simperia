package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Panel extends JLayeredPane {

    public static final int WIDTH = GameFrame.WIDTH;
    public static final int HEIGHT = GameFrame.HEIGHT;
    final Color BACKGROUND_COLOR = new Color(51,51,51);

    //Output objects
    public Graphics graphics;
    final private BufferedImage imageBuffer;

    public Panel() {

        //Initiate panel variables
        this.setSize(WIDTH, HEIGHT);
        this.setBackground(BACKGROUND_COLOR);

        //Initiate graphics system
        GraphicsConfiguration graphicsConf = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        imageBuffer = graphicsConf.createCompatibleImage(this.getWidth(), this.getHeight());
        graphics = imageBuffer.getGraphics();
    }

    public void clear() {
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, WIDTH, HEIGHT);
    }

    public void redraw() { this.getGraphics().drawImage(imageBuffer, 0, 0, this); }

}
