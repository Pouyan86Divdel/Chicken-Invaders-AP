package models;

import javax.swing.*;
import java.awt.*;

public class Coin {
    private int x;
    private int y;
    private int speedY = 3;
    private int width = 15;
    private int height = 12;
    private Image texture;

    public Coin(int x, int y) {
        this.x = x;
        this.y = y;
        this.texture = new ImageIcon("assets/images/powerup/coinn.png").getImage();
    }

    public void move() {
        this.y += speedY;
    }

    public void draw(Graphics g) {
        if (texture != null) {
            g.drawImage(texture, x, y, width, height, null);
        } else {
            g.setColor(Color.YELLOW);
            g.fillOval(x, y, width, height);
        }
    }

    public Rectangle getPos() {
        return new Rectangle(x, y, width, height);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}