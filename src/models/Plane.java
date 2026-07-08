package models;
import javax.swing.*;
import java.awt.*;

public class Plane {
    private int x, y;
    private int speed = 5;
    private int health = 3;
    private Image image;

    public Plane(int startX, int startY) {
        this.x = startX;
        this.y = startY;
        ImageIcon icon = new ImageIcon("assets/images/airplane/1.png");
        this.image = icon.getImage();
    }

    public void moveLeft() {
        if (x > 0) {
            x -= speed;
        }
    }

    public void moveRight() {
        if (x < 800 - 64) {
            x += speed;
        }
    }

    public void moveUp() {
        if (y > 0) {
            y -= speed;
        }
    }

    public void moveDown() {
        if (y < 600 - 64) {
            y += speed;
        }
    }

    public Rectangle getPos() {
        return new Rectangle(x, y, 54, 54);
    }

    public void takeDamage(int damage) {
        this.health -= damage;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void draw(Graphics g) {
        g.drawImage(image, x, y, 64, 64, null);
    }

    public int getX() { return x; }
    public int getY() { return y; }
}