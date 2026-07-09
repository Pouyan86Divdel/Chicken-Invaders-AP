package models;

import javax.swing.*;
import java.awt.*;

public class PowerUp {
    private int x, y;
    private int speed = 2;
    private String type;
    private Image image;

    public PowerUp(int x, int y, String type) {
        this.x = x;
        this.y = y;
        this.type = type;

        String imgPath = "assets/images/powerup/";
        switch (type) {
            case "AddFire":
                imgPath += "add_shot.png";
                break;
            case "RapidFire":
                imgPath += "fast_shot.png";
                break;
            case "ExtraLife":
                imgPath += "heal.png";
                break;
            case "Shield":
                imgPath += "sheild.png";
                break;
            case "FreezeBomb":
                imgPath += "freeze.png";
                break;
            default:
                imgPath += "add_shot.png";
                break;
        }
        this.image = new ImageIcon(imgPath).getImage();
    }

    public void move() {
        y += speed;
    }

    public void draw(Graphics g) {
        if (image != null) {
            g.drawImage(image, x, y, 30, 30, null);
        }
    }

    public Rectangle getPos() {
        return new Rectangle(x, y, 30, 30);
    }

    public String getType() { return type; }
    public int getY() { return y; }
}