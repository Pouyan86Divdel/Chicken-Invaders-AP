package models;

import java.awt.*;
import javax.swing.*;

public class Egg {
    private int x, y;
    private int dx, dy;
    private int speed;
    private Image image;

    public Egg(int x, int y) {
        this.x = x;
        this.y = y;
        this.dx = 0;
        this.dy = 1;
        this.speed = 4;
        this.image = new ImageIcon("assets/images/chicken/egg.png").getImage();
    }

    public Egg(int x, int y, int dx, int dy, int speed) {
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        this.speed = speed;
        this.image = new ImageIcon("assets/images/chicken/egg.png").getImage();
    }

    public void move() {
        x += dx * speed;
        y += dy * speed;
    }

    public void draw(Graphics g) {
        if (image != null) {
            g.drawImage(image, x, y, 20, 25, null);
        } else {
            g.setColor(Color.YELLOW);
            g.fillOval(x, y, 15, 20);
        }
    }

    public Rectangle getPos() {
        return new Rectangle(x, y, 20, 25);
    }

    public int getX() { return x; }
    public int getY() { return y; }
}