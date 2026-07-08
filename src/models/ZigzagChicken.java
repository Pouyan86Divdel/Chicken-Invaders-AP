package models;

import javax.swing.*;
import java.awt.*;

public class ZigzagChicken extends Enemy {
    private double offsetX = 0;
    private double angle = 0;

    public ZigzagChicken(int x, int y) {
        super(x, y, 2, 1);
        ImageIcon icon = new ImageIcon("assets/images/chicken/zigzag_chicken.png");
        this.image = icon.getImage();
    }

    @Override
    public void move() {
        y += speed;
    }

    @Override
    public void updateSpawnMovement() {
        super.updateSpawnMovement();
        if (!isSpawning()) {
            angle += 0.1;
            offsetX = Math.sin(angle) * 15;
        }
    }

    @Override
    public int getX() {
        return (int) (x + offsetX);
    }

    @Override
    public int getScore() { return 20; }
}