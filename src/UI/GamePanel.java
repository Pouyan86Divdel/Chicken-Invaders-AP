package UI;

import models.*;

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
    private int score = 0;
    private java.util.List<Bullet> bullets = new ArrayList<Bullet>();
    private java.util.List<Enemy> enemies = new java.util.ArrayList<>();
    private java.util.List<models.Egg> eggs = new java.util.ArrayList<>();
    private java.util.Random random = new java.util.Random();
    private java.util.List<models.Cell> gridCells = new java.util.ArrayList<>();
    private int frameCounter = 0;
    private int currentLevel = 1;
    private int gridSpeedX = 2;
    private int gridDirection = 1;
    private int gridStepY = 20;

    private void generateGrid() {
        gridCells.clear();
        int hitCounter = 2;
        String enemyType = "Normal";

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 8; col++) {
                gridCells.add(new models.Cell(row, col, hitCounter, enemyType));
            }
        }
    }

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
        bullets.clear();
        eggs.clear();
        currentLevel = 1;
        gridDirection = 1;
        gridSpeedX = 2;
        score = 0;
        plane.setHealth(3);
//        for (int i = 0; i < 5; i++) {
//            int startX = 100 + (i * 120);
//            int startY = 50;
//            enemies.add(new NormalChicken(startX, startY));
//        }
        generateGrid();
        gameTimer.start();
    }

    private void checkCollision() {
        for (int i = 0; i < bullets.size(); i++) {
            Bullet b = bullets.get(i);
            for (models.Cell cell : gridCells) {
                Enemy enemy = cell.getCurrentEnemy();
                if (enemy != null && b.getPos().intersects(enemy.getPos())) {
                    bullets.remove(i);
                    i--;
                    enemy.takeDamage(1);
                    if (enemy.getHealth() <= 0) {
                        cell.enemyKilled();
                        score += 10;
                    }
                    break;
                }
            }
        }

        for (models.Cell cell : gridCells) {
            Enemy enemy = cell.getCurrentEnemy();
            if (enemy != null && plane.getPos().intersects(enemy.getPos())) {
                plane.takeDamage(1);
                cell.enemyKilled();
                if (plane.getHealth() <= 0) {
                    gameTimer.stop();
                }
            }
        }

        for (int i = 0; i < eggs.size(); i++) {
            models.Egg egg = eggs.get(i);
            if (egg.getPos().intersects(plane.getPos())) {
                plane.takeDamage(1);
                eggs.remove(i);
                i--;
                if (plane.getHealth() <= 0) {
                    gameTimer.stop();
                }
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        plane.draw(g);

        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).draw(g);
        }

        for (models.Cell cell : gridCells) {
            Enemy enemy = cell.getCurrentEnemy();
            if (enemy != null) {
                enemy.draw(g);
            }
        }

        for (int i = 0; i < eggs.size(); i++) {
            eggs.get(i).draw(g);
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString("Lives: " + plane.getHealth(), 20, 30);
        g.drawString("Score: " + score, 650, 30);
        g.drawString("Level: " + currentLevel, 370, 30);

        if (plane.getHealth() <= 0) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 50));
            g.drawString("GAME OVER", 250, 300);
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.setColor(Color.WHITE);
            g.drawString("Press ESC to return to Main Menu", 240, 350);
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

        boolean hitEdge = false;
        int deltaX = gridSpeedX * gridDirection;

        for (models.Cell cell : gridCells) {
            Enemy enemy = cell.getCurrentEnemy();
            if (enemy != null && !enemy.isSpawning()) {
                if ((enemy.getX() + deltaX > 750 && gridDirection == 1) ||
                        (enemy.getX() + deltaX < 10 && gridDirection == -1)) {
                    hitEdge = true;
                    break;
                }
            }
        }

        int actualDeltaX = deltaX;
        int actualDeltaY = 0;

        if (hitEdge) {
            gridDirection *= -1;
            actualDeltaX = 0;
            actualDeltaY = gridStepY;
        }

        for (models.Cell cell : gridCells) {
            cell.updatePosition(actualDeltaX, actualDeltaY);
        }

        for (models.Cell cell : gridCells) {
            Enemy enemy = cell.getCurrentEnemy();
            if (enemy != null && enemy.isSpawning()) {
                enemy.updateSpawnMovement();
            }
        }

        frameCounter++;
        if (frameCounter >= 180) {
            frameCounter = 0;
            for (models.Cell cell : gridCells) {
                Enemy enemy = cell.getCurrentEnemy();
                if (enemy != null && !enemy.isSpawning()) {
                    if (random.nextInt(10) < 3) {
                        int eggX = enemy.getX() + 24;
                        int eggY = enemy.getY() + 40;
                        eggs.add(new models.Egg(eggX, eggY));
                    }
                }
            }
        }

        for (int i = 0; i < eggs.size(); i++) {
            models.Egg egg = eggs.get(i);
            egg.move();
            if (egg.getY() > 600) {
                eggs.remove(i);
                i--;
            }
        }

        checkCollision();
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
            if (key == KeyEvent.VK_ESCAPE) {
                if (plane.getHealth() <= 0) {
                    gameMain.changePanel("MainMenu");
                }
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