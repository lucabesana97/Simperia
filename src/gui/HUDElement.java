package gui;

import java.awt.*;

public class HUDElement {
    private int x;
    private int y;
    private int width;
    private int height;
    private Image image;
    private String text;
    private Font font = new Font("Helvetica", Font.PLAIN, 17);

    public HUDElement(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public HUDElement() {
    }

    public HUDElement(int x, int y, String text) {
        this.x = x;
        this.y = y;
        this.text = text;
    }

    public HUDElement(int x, int y, Image image) {
        this.x = x;
        this.y = y;
        this.image = image;
    }

    // Getters and setters
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public void draw(Graphics graphics) {
        if (image != null) {
            graphics.drawImage(image, x, y, null);
        }
        if (text != null) {
            graphics.setColor(Color.white);
            graphics.setFont(font);
            graphics.drawString(text, x, y);
        }
    }
}

