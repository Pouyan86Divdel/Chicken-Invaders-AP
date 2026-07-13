package models;

import javax.swing.*;
import java.awt.*;

public class NormalChicken extends Enemy {

    public NormalChicken(int x, int y) {
        super(x, y, 1, 1);
        ImageIcon icon = new ImageIcon("assets/images/chicken/normal_chicken.png");
        this.image = icon.getImage();
    }

    @Override
    public void move() {
        y += speed;

    }

    @Override
    public int getScore() { return 10; }
}