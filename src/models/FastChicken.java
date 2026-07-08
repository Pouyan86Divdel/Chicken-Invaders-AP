package models;

import javax.swing.*;
import java.awt.*;

public class FastChicken extends Enemy {

    public FastChicken(int x, int y) {
        super(x, y, 1, 2);
        ImageIcon icon = new ImageIcon("assets/images/chicken/fast_chicken.png");
        this.image = icon.getImage();
    }

    @Override
    public void move() {
        y += speed;
    }

    @Override
    public int getScore() { return 15; }
}