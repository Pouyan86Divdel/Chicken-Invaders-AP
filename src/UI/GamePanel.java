package UI;

import database.DatabaseManager;
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
    private Image backgroundImage;
    private int freezeTicks = 0;
    private java.util.List<Bullet> bullets = new ArrayList<Bullet>();
    private java.util.List<Enemy> enemies = new java.util.ArrayList<>();
    private java.util.List<models.Egg> eggs = new java.util.ArrayList<>();
    private java.util.Random random = new java.util.Random();
    private java.util.List<models.Cell> gridCells = new java.util.ArrayList<>();
    private java.util.List<models.PowerUp> powerUps = new java.util.ArrayList<>();
    private DatabaseManager databaseManager = new DatabaseManager();
    private int frameCounter = 0;
    private int currentLevel = 1;
    private int gridSpeedX = 2;
    private int gridDirection = 1;
    private int gridStepY = 20;

    private void generateGrid() {
        gridCells.clear();
        int hitCounter = 1;
        String enemyType = "Normal";

        if (currentLevel == 1) {
            hitCounter = 1;
            enemyType = "Normal";
            gridSpeedX = 2;
            gridStepY = 20;
        } else if (currentLevel == 2) {
            hitCounter = 1;
            enemyType = "Fast";
            gridSpeedX = 4;
            gridStepY = 25;
        } else if (currentLevel == 3) {
            hitCounter = 2;
            enemyType = "Zigzag";
            gridSpeedX = 3;
            gridStepY = 30;
        }

        gridDirection = 1;

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 8; col++) {
                gridCells.add(new models.Cell(row, col, hitCounter, enemyType));
            }
        }
    }

    public GamePanel(GameMain gameMain) {
        this.gameMain = gameMain;
        setBackground(Color.ORANGE);
        backgroundImage = new ImageIcon("assets/images/background/background.jpg").getImage();
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
        plane.resetFireLevel();
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
                    sound.SoundManager.playSFX("assets/sounds/mixkit-epic-impact-afar-explosion-2782.wav");
                    if (enemy.getHealth() <= 0) {
                        score += enemy.getScore();

                        if (Math.random() < 0.20) {
                            String[] types = {"AddFire", "RapidFire", "ExtraLife", "Shield", "FreezeBomb"};
                            String randomType = types[(int)(Math.random() * types.length)];
                            powerUps.add(new models.PowerUp(enemy.getX(), enemy.getY(), randomType));
                        }

                        cell.enemyKilled();
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
                    sound.SoundManager.stopBGM();
                    sound.SoundManager.playSFX("assets/sounds/mixkit-retro-arcade-game-over-470.wav");
                    String currentUsername = gameMain.getCurrentUsername();
                    database.DatabaseManager.saveGameRecord(currentUsername, score, currentLevel, "Music:On,SFX:On");
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
                    sound.SoundManager.stopBGM();
                    sound.SoundManager.playSFX("assets/sounds/mixkit-retro-arcade-game-over-470.wav");
                    String currentUsername = gameMain.getCurrentUsername();
                    databaseManager.saveGameRecord(currentUsername, score, currentLevel, "Music:On,SFX:On");
                }
            }
        }

        for (int i = 0; i < powerUps.size(); i++) {
            models.PowerUp p = powerUps.get(i);
            if (p.getPos().intersects(plane.getPos())) {
                switch (p.getType()) {
                    case "ExtraLife":
                        if (plane.getHealth() < 5) {
                            plane.setHealth(plane.getHealth() + 1);
                        }
                        break;
                    case "AddFire":
                        plane.incrementFireLevel();
                        break;
                    case "RapidFire":
                        plane.activateRapidFire();
                        break;
                    case "Shield":
                        plane.activateShield();
                        break;
                    case "FreezeBomb":
                        this.activateFreezeBomb();
                        break;
                }
                powerUps.remove(i);
                i--;
            }
        }

        checkLevelUp();
    }

    private void checkLevelUp() {
        boolean levelCleared = true;
        for (models.Cell cell : gridCells) {
            if (cell.getHitCounter() > 0) {
                levelCleared = false;
                break;
            }
        }

        if (levelCleared) {
            score += 200;
            bullets.clear();
            eggs.clear();
            powerUps.clear();

            plane.setX(368);
            plane.setY(500);

            if (currentLevel < 3) {
                currentLevel++;
                generateGrid();
                sound.SoundManager.playSFX("assets/sounds/mixkit-short-laser-gun-shot-1670.wav");
            } else {
                gameTimer.stop();
                sound.SoundManager.stopBGM();

                sound.SoundManager.playBGM("assets/sounds/Chicken Invaders 2 Remastered OST - Ending Theme.wav");

                String currentUsername = gameMain.getCurrentUsername();
                database.DatabaseManager.saveGameRecord(currentUsername, score, currentLevel, "Music:On,SFX:On");
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, 800, 600, this);
        }

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

        for (int i = 0; i < powerUps.size(); i++) {
            powerUps.get(i).draw(g);
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString("Lives: " + plane.getHealth(), 20, 30);
        g.drawString("Score: " + score, 650, 30);
        g.drawString("Level: " + currentLevel, 370, 30);

        if (plane.getHealth() <= 0) {
            g.setColor(Color.RED);
            g.setFont(new Font("Impact", Font.BOLD, 55));
            g.drawString("GAME OVER", 270, 280);
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.setColor(Color.WHITE);
            g.drawString("Press ESC to return to Main Menu", 250, 340);
        }
        else if (!gameTimer.isRunning() && currentLevel == 3) {
            g.setColor(Color.GREEN);
            g.setFont(new Font("Impact", Font.BOLD, 55));
            g.drawString("VICTORY! YOU WIN", 210, 280);
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.setColor(Color.WHITE);
            g.drawString("Score: " + score + " | Press ESC to return to Menu", 220, 340);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (leftPressed) plane.moveLeft();
        if (rightPressed) plane.moveRight();
        if (upPressed) plane.moveUp();
        if (downPressed) plane.moveDown();

        plane.updateTimers();
        if (freezeTicks > 0) {
            freezeTicks--;
        }

        for (int i = 0; i < bullets.size(); i++) {
            Bullet b = bullets.get(i);
            b.move();
            if (b.getY() < 0) {
                bullets.remove(i);
                i--;
            }
        }

        if (freezeTicks <= 0) {
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

                int shootChance = 3;
                if (currentLevel == 2) {
                    shootChance = 1;
                } else if (currentLevel == 3) {
                    shootChance = 2;
                }

                for (models.Cell cell : gridCells) {
                    Enemy enemy = cell.getCurrentEnemy();
                    if (enemy != null && !enemy.isSpawning()) {
                        if (random.nextInt(10) < shootChance) {
                            int eggX = enemy.getX() + 24;
                            int eggY = enemy.getY() + 40;
                            eggs.add(new Egg(eggX, eggY));
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
        }

        for (int i = 0; i < powerUps.size(); i++) {
            models.PowerUp p = powerUps.get(i);
            p.move();
            if (p.getY() > 600) {
                powerUps.remove(i);
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
                int level = plane.getFireLevel();
                int pX = plane.getX();
                int pY = plane.getY();

                if (level == 1) {
                    bullets.add(new Bullet(pX + 24, pY));
                } else if (level == 2) {
                    bullets.add(new Bullet(pX + 10, pY));
                    bullets.add(new Bullet(pX + 38, pY));
                } else if (level == 3) {
                    bullets.add(new Bullet(pX + 24, pY));
                    bullets.add(new Bullet(pX + 6, pY));
                    bullets.add(new Bullet(pX + 42, pY));
                }
                sound.SoundManager.playSFX("assets/sounds/mixkit-short-laser-gun-shot-1670.wav");
            }
            if (key == KeyEvent.VK_ESCAPE) {
                if (plane.getHealth() <= 0 || (!gameTimer.isRunning() && currentLevel == 3)) {
                    sound.SoundManager.stopBGM();
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

    public void activateFreezeBomb() {
        this.freezeTicks = 3 * 60;
    }
}