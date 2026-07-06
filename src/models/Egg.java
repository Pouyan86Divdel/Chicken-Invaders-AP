package models;

import javax.swing.*;
import java.awt.*;

public class Egg {
    private int x, y;
    private int speed = 8;
    private Image image;

    public Egg(int startX, int startY) {
        this.x = startX;
        this.y = startY;
        ImageIcon icon = new ImageIcon("assets/images/chicken/egg.png");
        this.image = icon.getImage();
    }

    public void move() {
        y += speed;
    }

    public void draw(Graphics g) {
        g.drawImage(image, x, y, 36, 36, null);
    }

    public Rectangle getPos() {
        return new Rectangle(x, y, 36, 36);
    }

    public int getY() { return y; }
}