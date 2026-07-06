package models;

import java.awt.*;

public abstract class Enemy {
    protected int x, y;
    protected int speed;
    protected int health;
    protected Image image;

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
        return new Rectangle(x, y, 48, 48);
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getHealth() { return health; }
}