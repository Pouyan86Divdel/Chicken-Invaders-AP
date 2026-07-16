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
    private boolean spacePressed = false;
    private int score = 0;
    private Image backgroundImage;
    private Image bossLevel4Image;
    private Image bossLevel8Image;
    private int freezeTicks = 0;
    private java.util.List<Bullet> bullets = new ArrayList<Bullet>();
    private java.util.List<Enemy> enemies = new java.util.ArrayList<>();
    private java.util.List<models.Egg> eggs = new java.util.ArrayList<>();
    private java.util.Random random = new java.util.Random();
    private java.util.List<models.Cell> gridCells = new java.util.ArrayList<>();
    private java.util.List<models.PowerUp> powerUps = new java.util.ArrayList<>();
    private java.util.List<models.Coin> coins = new java.util.ArrayList<>();
    private DatabaseManager databaseManager = new DatabaseManager();
    private int frameCounter = 0;
    private int currentLevel = 1;
    private int gridSpeedX = 2;
    private int gridDirection = 1;
    private int gridStepY = 20;

    private int bossHealth = 0;
    private int bossMaxHealth = 50;
    private int bossX = 350;
    private int bossY = 50;
    private int bossSpeedX = 2;
    private int bossDirection = 1;
    private boolean isOutOfBounds = false;
    private boolean isPaused = false;

    private void generateGrid() {
        gridCells.clear();
        eggs.clear();
        bullets.clear();
        powerUps.clear();
        coins.clear();

        if (currentLevel != 4 && currentLevel != 8) {
            int hitCounter = 2;
            gridDirection = 1;

            switch (currentLevel) {
                case 1:
                    hitCounter = 2; gridSpeedX = 2; gridStepY = 20; break;
                case 2:
                    hitCounter = 2; gridSpeedX = 3; gridStepY = 20; break;
                case 3:
                    hitCounter = 3; gridSpeedX = 2; gridStepY = 25; break;
                case 5:
                    hitCounter = 3; gridSpeedX = 3; gridStepY = 25; break;
                case 6:
                    hitCounter = 4; gridSpeedX = 3; gridStepY = 30; break;
                case 7:
                    hitCounter = 4; gridSpeedX = 4; gridStepY = 30; break;
            }

            for (int row = 0; row < 5; row++) {
                for (int col = 0; col < 8; col++) {
                    String enemyType = "Normal";

                    if (currentLevel == 1) {
                        enemyType = "Normal";
                    } else if (currentLevel == 2) {
                        enemyType = (col % 2 == 0) ? "Normal" : "Fast";
                    } else if (currentLevel == 3) {
                        enemyType = (col % 2 == 0) ? "Normal" : "Zigzag";
                    } else if (currentLevel == 5) {
                        enemyType = (col % 2 == 0) ? "Shooter" : "Fast";
                    } else if (currentLevel == 6) {
                        enemyType = (col % 2 == 0) ? "Zigzag" : "Shooter";
                    } else if (currentLevel == 7) {
                        int typeIndex = (row * 8 + col) % 4;
                        if (typeIndex == 0) enemyType = "Normal";
                        else if (typeIndex == 1) enemyType = "Fast";
                        else if (typeIndex == 2) enemyType = "Zigzag";
                        else enemyType = "Shooter";
                    }

                    gridCells.add(new models.Cell(row, col, hitCounter, enemyType));
                }
            }
        } else {
            if (currentLevel == 4) {
                bossMaxHealth = 50;
                bossHealth = 50;
                bossSpeedX = 2;
            } else {
                bossMaxHealth = 100;
                bossHealth = 100;
                bossSpeedX = 2;
            }
            bossX = 350;
            bossY = 50;
            bossDirection = 1;
        }
    }

    public GamePanel(GameMain gameMain) {
        this.gameMain = gameMain;
        setBackground(Color.ORANGE);
        backgroundImage = new ImageIcon("assets/images/background/background.jpg").getImage();
        bossLevel4Image = new ImageIcon("assets/images/chicken/boss1.png").getImage();
        bossLevel8Image = new ImageIcon("assets/images/chicken/boss2.png").getImage();
        setFocusable(true);
        plane = new Plane(368, 500);
        addKeyListener(new MyKeyAdapter());
        gameTimer = new Timer(16, this);
    }

    public void startGame() {
        isPaused = false;
        enemies.clear();
        bullets.clear();
        eggs.clear();
        isOutOfBounds = false;
        powerUps.clear();
        coins.clear();
        currentLevel = 1;
        score = 0;

        String username = gameMain.getCurrentUsername();
        int initialHealth = 3;
        int speedBonus = 0;
        int defaultFire = 1;

        if (!username.equals("Guest")) {
            int healthLvl = DatabaseManager.getUpgradeLevel(username, "max_health");
            initialHealth = healthLvl + 2;
            int speedLvl = DatabaseManager.getUpgradeLevel(username, "speed_level");
            speedBonus = (speedLvl - 1) * 2;
            defaultFire = DatabaseManager.getUpgradeLevel(username, "fire_level");
        }

        plane.setHealth(initialHealth);
        plane.setSpeedBonus(speedBonus);
        plane.setFireLevel(defaultFire);

        generateGrid();
        gameTimer.start();
    }

    private void checkCollision() {
        for (int i = 0; i < bullets.size(); i++) {
            Bullet b = bullets.get(i);
            Rectangle bulletBounds = b.getPos();

            if (currentLevel == 4 || currentLevel == 8) {
                Rectangle bossBounds = new Rectangle(bossX, bossY, 150, 150);
                if (bulletBounds.intersects(bossBounds)) {
                    bullets.remove(i);
                    i--;
                    bossHealth--;
                    sound.SoundManager.playSFX("assets/sounds/mixkit-epic-impact-afar-explosion-2782.wav");
                    if (bossHealth <= 0) {
                        score += (currentLevel == 4) ? 500 : 1000;
                        checkLevelUp();
                    }
                    continue;
                }
            } else {
                for (models.Cell cell : gridCells) {
                    Enemy enemy = cell.getCurrentEnemy();
                    if (enemy != null && bulletBounds.intersects(enemy.getPos())) {
                        bullets.remove(i);
                        i--;
                        enemy.takeDamage(1);
                        sound.SoundManager.playSFX("assets/sounds/mixkit-epic-impact-afar-explosion-2782.wav");
                        if (enemy.getHealth() <= 0) {
                            score += enemy.getScore();
                            double rand = Math.random();
                            if (rand < 0.10) {
                                String[] types = {"AddFire", "RapidFire", "ExtraLife", "Shield", "FreezeBomb"};
                                String randomType = types[(int)(Math.random() * types.length)];
                                powerUps.add(new models.PowerUp(enemy.getX(), enemy.getY(), randomType));
                            } else if (rand < 0.20) {
                                coins.add(new models.Coin(enemy.getX(), enemy.getY()));
                            }
                            cell.enemyKilled();
                        }
                        break;
                    }
                }
            }
        }

        if (currentLevel == 4 || currentLevel == 8) {
            Rectangle bossBounds = new Rectangle(bossX, bossY, 30, 30);
            if (plane.getPos().intersects(bossBounds)) {
                if (!plane.hasShield()) {
                    plane.takeDamage(1);
                    plane.activateHitEffect();
                    sound.SoundManager.playSFX("assets/sounds/mixkit-epic-impact-afar-explosion-2782.wav");
                    checkGameOver();
                }
            }
        } else {
            for (models.Cell cell : gridCells) {
                Enemy enemy = cell.getCurrentEnemy();
                if (enemy != null && plane.getPos().intersects(enemy.getPos())) {
                    if (!plane.hasShield()) {
                        plane.takeDamage(1);
                        plane.activateHitEffect();
                        sound.SoundManager.playSFX("assets/sounds/mixkit-epic-impact-afar-explosion-2782.wav");
                        checkGameOver();
                    }
                    cell.enemyKilled();
                }
            }
        }

        for (int i = 0; i < eggs.size(); i++) {
            models.Egg egg = eggs.get(i);
            if (egg.getPos().intersects(plane.getPos())) {
                if (!plane.hasShield()) {
                    plane.takeDamage(1);
                    plane.activateHitEffect();
                    sound.SoundManager.playSFX("assets/sounds/mixkit-epic-impact-afar-explosion-2782.wav");
                    checkGameOver();
                }
                eggs.remove(i);
                i--;
            }
        }

        for (int i = 0; i < powerUps.size(); i++) {
            models.PowerUp p = powerUps.get(i);
            if (p.getPos().intersects(plane.getPos())) {
                switch (p.getType()) {
                    case "ExtraLife":
                        int maxHealthLimit = 3;
                        if (!gameMain.getCurrentUsername().equals("Guest")) {
                            int healthLvl = DatabaseManager.getUpgradeLevel(gameMain.getCurrentUsername(), "max_health");
                            maxHealthLimit = healthLvl + 2;
                        }
                        if (plane.getHealth() < maxHealthLimit) {
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

        for (int i = 0; i < coins.size(); i++) {
            models.Coin c = coins.get(i);
            if (c.getPos().intersects(plane.getPos())) {
                String currentUsername = gameMain.getCurrentUsername();
                if (!currentUsername.equals("Guest")) {
                    DatabaseManager.updateCoins(currentUsername, 1);
                }
                sound.SoundManager.playSFX("assets/sounds/coin-drop.wav");
                coins.remove(i);
                i--;
            }
        }

        if (currentLevel != 4 && currentLevel != 8) {
            checkLevelUp();
        }
    }

    private void checkGameOver() {
        if (plane.getHealth() <= 0 || isOutOfBounds) {
            gameTimer.stop();
            sound.SoundManager.stopBGM();
            sound.SoundManager.playSFX("assets/sounds/mixkit-retro-arcade-game-over-470.wav");
            String currentUsername = gameMain.getCurrentUsername();
            DatabaseManager.saveGameRecord(currentUsername, score, currentLevel, "Music:On,SFX:On");
        }
    }

    private void checkLevelUp() {
        boolean levelCleared = true;
        if (currentLevel != 4 && currentLevel != 8) {
            for (models.Cell cell : gridCells) {
                if (cell.getHitCounter() > 0) {
                    levelCleared = false;
                    break;
                }
            }
        } else {
            levelCleared = (bossHealth <= 0);
        }

        if (levelCleared) {
            score += 200;
            bullets.clear();
            eggs.clear();
            powerUps.clear();
            coins.clear();
            plane.setX(368);
            plane.setY(500);

            if (currentLevel < 8) {
                currentLevel++;
                generateGrid();
                sound.SoundManager.playSFX("assets/sounds/mixkit-short-laser-gun-shot-1670.wav");
            } else {
                gameTimer.stop();
                sound.SoundManager.stopBGM();
                sound.SoundManager.playBGM("assets/sounds/Chicken Invaders 2 Remastered OST - Ending Theme.wav");
                String currentUsername = gameMain.getCurrentUsername();
                DatabaseManager.saveGameRecord(currentUsername, score, currentLevel, "Music:On,SFX:On");
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

        for (Bullet b : bullets) {
            b.draw(g);
        }

        if (currentLevel == 4 || currentLevel == 8) {
            Image currentBossImage = (currentLevel == 4) ? bossLevel4Image : bossLevel8Image;
            if (currentBossImage != null) {
                g.drawImage(currentBossImage, bossX, bossY, 150, 150, this);
            } else {
                g.setColor(Color.RED);
                g.fillRect(bossX, bossY, 150, 150);
            }
            g.setColor(Color.GRAY);
            g.fillRect(250, 20, 300, 15);
            g.setColor(currentLevel == 4 ? Color.ORANGE : Color.RED);
            int barWidth = (int) (((double) bossHealth / bossMaxHealth) * 300);
            g.fillRect(250, 20, barWidth, 15);
        } else {
            for (models.Cell cell : gridCells) {
                Enemy enemy = cell.getCurrentEnemy();
                if (enemy != null) {
                    enemy.draw(g);
                }
            }
        }

        for (models.Egg egg : eggs) {
            egg.draw(g);
        }

        for (models.PowerUp p : powerUps) {
            p.draw(g);
        }

        for (models.Coin c : coins) {
            c.draw(g);
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 18));

        g.drawString("Lives: " + plane.getHealth(), 20, 40);

        int currentCoins = 0;
        String username = gameMain.getCurrentUsername();
        if (!username.equals("Guest")) {
            currentCoins = DatabaseManager.getCoins(username);
        }
        g.setColor(Color.YELLOW);
        g.drawString("Coins: " + currentCoins, 20, 70);

        g.setColor(Color.WHITE);
        g.drawString("Score: " + score, 650, 40);
        g.drawString("Level: " + currentLevel, 370, 40);

        if (plane.getHealth() <= 0 || isOutOfBounds) {
            g.setColor(Color.RED);
            g.setFont(new Font("Impact", Font.BOLD, 55));
            g.drawString("GAME OVER", 270, 280);
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.setColor(Color.WHITE);
            g.drawString("Press ESC to return to Main Menu", 250, 340);
        } else if (!gameTimer.isRunning() && currentLevel == 8 && bossHealth <= 0) {
            g.setColor(Color.GREEN);
            g.setFont(new Font("Impact", Font.BOLD, 55));
            g.drawString("VICTORY! YOU WIN", 210, 280);
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.setColor(Color.WHITE);
            g.drawString("Score: " + score + " | Press ESC to return to Menu", 220, 340);
        }

        if (isPaused) {
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 0, 800, 600);

            g.setColor(Color.YELLOW);
            g.setFont(new Font("Impact", Font.BOLD, 60));
            g.drawString("GAME PAUSED", 250, 260);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.drawString("Press 'P' to Resume", 310, 310);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (isPaused) {
            repaint();
            return;
        }

        for (int i = 0; i < gridCells.size(); i++) {
            Cell gc = gridCells.get(i);
            if (gc.getY() >= 515 && gc.getHitCounter() != 0) {
                isOutOfBounds = true;
                checkGameOver();
            }
        }
        if (leftPressed) plane.moveLeft();
        if (rightPressed) plane.moveRight();
        if (upPressed) plane.moveUp();
        if (downPressed) plane.moveDown();

        plane.updateTimers();
        plane.updateHitTimer();

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
            if (currentLevel == 4 || currentLevel == 8) {
                bossX += bossSpeedX * bossDirection;
                if (bossX > 630 || bossX < 20) {
                    bossDirection *= -1;
                }
            } else {
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
            }

            for (models.Cell cell : gridCells) {
                Enemy enemy = cell.getCurrentEnemy();
                if (enemy != null && enemy.isSpawning()) {
                    enemy.updateSpawnMovement();
                }
            }

            frameCounter++;
            if (frameCounter >= 60) {
                frameCounter = 0;

                if (currentLevel == 4) {
                    eggs.add(new Egg(bossX + 75, bossY + 120, 0, 1, 4));
                    eggs.add(new Egg(bossX + 75, bossY + 120, 0, -1, 4));
                    eggs.add(new Egg(bossX + 75, bossY + 120, -1, 0, 4));
                    eggs.add(new Egg(bossX + 75, bossY + 120, 1, 0, 4));
                } else if (currentLevel == 8) {
                    eggs.add(new Egg(bossX + 75, bossY + 120, 0, 1, 5));
                    eggs.add(new Egg(bossX + 75, bossY + 120, 0, -1, 5));
                    eggs.add(new Egg(bossX + 75, bossY + 120, -1, 0, 5));
                    eggs.add(new Egg(bossX + 75, bossY + 120, 1, 0, 5));
                    eggs.add(new Egg(bossX + 75, bossY + 120, -1, 1, 5));
                    eggs.add(new Egg(bossX + 75, bossY + 120, 1, 1, 5));
                    eggs.add(new Egg(bossX + 75, bossY + 120, -1, -1, 5));
                    eggs.add(new Egg(bossX + 75, bossY + 120, 1, -1, 5));
                } else {
                    for (models.Cell cell : gridCells) {
                        Enemy enemy = cell.getCurrentEnemy();
                        if (enemy != null && !enemy.isSpawning()) {
                            int spawnChance = random.nextInt(100);
                            if (enemy instanceof ShooterChicken) {
                                if (spawnChance < 20) {
                                    int type = random.nextInt(3);
                                    if (type == 0) {
                                        eggs.add(new Egg(enemy.getX() + 24, enemy.getY() + 40, 0, 1, 4));
                                    } else if (type == 1) {
                                        eggs.add(new Egg(enemy.getX() + 24, enemy.getY() + 40, -1, 0, 5));
                                    } else {
                                        eggs.add(new Egg(enemy.getX() + 24, enemy.getY() + 40, 1, 0, 5));
                                    }
                                }
                            } else {
                                if (spawnChance < 4) {
                                    eggs.add(new Egg(enemy.getX() + 24, enemy.getY() + 40, 0, 1, 4));
                                }
                            }
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

        for (int i = 0; i < coins.size(); i++) {
            models.Coin c = coins.get(i);
            c.move();
            if (c.getY() > 600) {
                coins.remove(i);
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

            if (key == KeyEvent.VK_P) {
                if (plane.getHealth() > 0 && !isOutOfBounds && (gameTimer.isRunning() || isPaused)) {
                    isPaused = !isPaused;
                    if (isPaused) {
                        leftPressed = false;
                        rightPressed = false;
                        upPressed = false;
                        downPressed = false;
                        spacePressed = false;
                    }
                    repaint();
                }
                return;
            }

            if (isPaused) {
                return;
            }

            if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) leftPressed = true;
            if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) rightPressed = true;
            if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) upPressed = true;
            if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) downPressed = true;
            if (key == KeyEvent.VK_SPACE) {
                if (!spacePressed) {
                    spacePressed = true;
                    int fireLevel = plane.getFireLevel();
                    int pX = plane.getX();
                    int pY = plane.getY();

                    for (int i = 0; i < fireLevel; i++) {
                        bullets.add(new Bullet(pX + 24 + (i * 10) - ((fireLevel - 1) * 5), pY));
                    }
                    sound.SoundManager.playSFX("assets/sounds/mixkit-short-laser-gun-shot-1670.wav");
                }
            }
            if (key == KeyEvent.VK_ESCAPE) {
                if (plane.getHealth() <= 0 || (!gameTimer.isRunning() && currentLevel == 8) || isOutOfBounds) {
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
            if (key == KeyEvent.VK_SPACE) spacePressed = false;
        }
    }

    public void activateFreezeBomb() {
        this.freezeTicks = 3 * 60;
    }
}