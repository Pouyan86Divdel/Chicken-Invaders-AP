package models;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class ShooterChicken extends Enemy {

    public ShooterChicken(int x, int y) {
        super(x, y, 2, 1);
        File imgFile = new File("assets/images/chicken/shooter_chicken.png");
        if (imgFile.exists()) {
            this.image = new ImageIcon("assets/images/chicken/shooter_chicken.png").getImage();
        } else {
            this.image = new ImageIcon("assets/images/chicken/shooter_chicken.png").getImage();
        }
    }

    @Override
    public void move() {
        y += speed;
    }

    @Override
    public int getScore() { return 25; }
}