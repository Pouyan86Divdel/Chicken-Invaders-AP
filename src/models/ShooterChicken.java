package models;

import javax.swing.*;
import java.awt.*;

public class ShooterChicken extends Enemy {

    public ShooterChicken(int x, int y) {
        super(x, y, 2, 1);
        ImageIcon icon = new ImageIcon("assets/images/chicken/shooter.png");
        this.image = icon.getImage();
    }

    @Override
    public void move() {
        y += speed;
    }

    @Override
    public int getScore() { return 25; }
}