package UI;

import models.Bullet;
import models.Enemy;
import models.NormalChicken;
import models.Plane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class GamePanel extends JPanel implements ActionListener {
    private GameMain gameMain;
    private Plane plane;
    private Timer gameTimer;
    private boolean upPressed, downPressed, leftPressed, rightPressed;
    private java.util.List<Bullet> bullets = new ArrayList<Bullet>();
    private java.util.List<Enemy> enemies = new java.util.ArrayList<>();
    public GamePanel(GameMain gameMain) {
        this.gameMain = gameMain;
        setBackground(Color.ORANGE);
        setFocusable(true);
        plane = new Plane(368, 500);
        addKeyListener(new MyKeyAdapter());
        gameTimer = new Timer(16, this);
//        gameTimer.start();
        for (int i = 0; i < 5; i++) {
            int startX = 100 + (i * 120);
            int startY = 50;
            enemies.add(new NormalChicken(startX, startY));
        }
    }

    public void startGame() {
        enemies.clear();
        for (int i = 0; i < 5; i++) {
            int startX = 100 + (i * 120);
            int startY = 50;
            enemies.add(new NormalChicken(startX, startY));
        }
        gameTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        plane.draw(g);
        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).draw(g);
        }
        for (int i = 0; i < enemies.size(); i++) {
            enemies.get(i).draw(g);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (leftPressed) plane.moveLeft();
        if (rightPressed) plane.moveRight();
        if (upPressed) plane.moveUp();
        if (downPressed) plane.moveDown();
        for (int i = 0; i < bullets.size(); i++) {
            Bullet b = bullets.get(i);
            b.move();
            if (b.getY() < 0) {
                bullets.remove(i);
                i--;
            }
        }
        for (int i = 0; i < enemies.size(); i++) {
            Enemy enemy = enemies.get(i);
            enemy.move();
            if (enemy.getY() > 600) {
                enemies.remove(i);
                i--;
            }
        }
        repaint();
    }

    private class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) leftPressed = true;
            if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) rightPressed = true;
            if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) upPressed = true;
            if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) downPressed = true;
            if (key == KeyEvent.VK_SPACE) {
                int bulletX = plane.getX() + 24;
                int bulletY = plane.getY();
                bullets.add(new Bullet(bulletX, bulletY));
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) leftPressed = false;
            if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) rightPressed = false;
            if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) upPressed = false;
            if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) downPressed = false;
        }
    }
}