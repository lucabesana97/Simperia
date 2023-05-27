package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Panel extends JPanel {

    public static final int WIDTH = Frame.WIDTH;
    public static final int HEIGHT = Frame.HEIGHT;

    //Output objects
    public Graphics graphics;
    final private BufferedImage imageBuffer;

    public Panel() {

        //Initiate panel variables
        this.setSize(WIDTH, HEIGHT);
        this.setBackground(Color.BLUE);

        //Initiate graphics system
        GraphicsConfiguration graphicsConf = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        imageBuffer = graphicsConf.createCompatibleImage(this.getWidth(), this.getHeight());
        graphics = imageBuffer.getGraphics();
    }

    public void clear() {
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, WIDTH, HEIGHT);
    }

    public void redraw() {
        this.getGraphics().drawImage(imageBuffer, 0, 0, this);
    }

}