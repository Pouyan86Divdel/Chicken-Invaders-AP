package models;

import java.awt.*;

public abstract class Enemy {
    protected int x, y;
    protected int speed;
    protected int health;
    protected Image image;
    public int targetX, targetY;
    private boolean isSpawning = false;

    public void startSpawnAnimation(int startX, int startY, int targetX, int targetY) {
        this.x = startX;
        this.y = startY;
        this.targetX = targetX;
        this.targetY = targetY;
        this.isSpawning = true;
    }

    public void updateSpawnMovement() {
        if (!isSpawning) return;
        int speed = 6;
        if (x < targetX)
            x = Math.min(x + speed, targetX);
        else if (x > targetX)
            x = Math.max(x - speed, targetX);
        if (y < targetY)
            y = Math.min(y + speed, targetY);
        else if (y > targetY)
            y = Math.max(y - speed, targetY);
        if (x == targetX && y == targetY) {
            isSpawning = false;
        }
    }

    public boolean isSpawning() {
        return isSpawning;
    }

    public Enemy(int x, int y, int speed, int health) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.health = health;
    }

    public abstract void move();

    public abstract int getScore();

    public void draw(Graphics g) {
        g.drawImage(image, x, y, 48, 48, null);
    }

    public void takeDamage(int damage) {
        this.health -= damage;
    }

    public Rectangle getPos() {
        return new Rectangle(x + 8, y + 5, 33, 38);
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public int getHealth() { return health; }
}