package models;

import javax.swing.*;
import java.awt.*;

public class Plane {
    private int x, y;
    private int speed = 5;
    private int health = 3;
    private int fireLevel = 1;
    private Image image;

    private int rapidFireTicks = 0;
    private int shieldTicks = 0;

    public Plane(int startX, int startY) {
        this.x = startX;
        this.y = startY;
        ImageIcon icon = new ImageIcon("assets/images/airplane/1.png");
        this.image = icon.getImage();
    }

    public void updateTimers() {
        if (rapidFireTicks > 0) {
            rapidFireTicks--;
        }
        if (shieldTicks > 0) {
            shieldTicks--;
        }
    }

    public void activateRapidFire() {
        this.rapidFireTicks = 8 * 60;
    }

    public void activateShield() {
        this.shieldTicks = 10 * 60;
    }

    public boolean isRapidFireActive() {
        return rapidFireTicks > 0;
    }

    public boolean isShieldActive() {
        return shieldTicks > 0;
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

    public void incrementFireLevel() {
        if (fireLevel < 3) {
            fireLevel++;
        }
    }

    public int getFireLevel() {
        return fireLevel;
    }

    public Rectangle getPos() {
        return new Rectangle(x, y, 54, 54);
    }

    public void takeDamage(int damage) {
        if (isShieldActive()) {
            return;
        }
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
        if (isShieldActive()) {
            g.setColor(new Color(0, 191, 255, 100));
            g.fillOval(x - 8, y - 8, 80, 80);
            g.setColor(Color.green);
            g.drawOval(x - 8, y - 8, 80, 80);
        }
    }

    public void resetFireLevel() {
        this.fireLevel = 1;
        this.rapidFireTicks = 0;
        this.shieldTicks = 0;
    }

    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }

    public int getX() { return x; }
    public int getY() { return y; }
}