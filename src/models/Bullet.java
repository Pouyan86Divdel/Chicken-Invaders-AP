package models;
import javax.swing.*;
import java.awt.*;

public class Bullet {
    private int x, y;
    private int speed = 8;
    private Image image;

    public Bullet(int startX, int startY) {
        this.x = startX;
        this.y = startY;
        ImageIcon icon = new ImageIcon("assets/images/airplane/shot.png");
        this.image = icon.getImage();
    }

    public void move() {
        y -= speed;
    }

    public void draw(Graphics g) {
        g.drawImage(image, x, y, 16, 32, null);
    }

    public int getX() { return x; }
    public int getY() { return y; }
}