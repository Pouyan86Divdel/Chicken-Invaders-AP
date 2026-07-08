package models;

import java.awt.*;

public class Cell {
    private int row, col;
    private int x, y;
    private int hitCounter;
    private Enemy currentEnemy;
    private String enemyType;

    public Cell(int row, int col, int hitCounter, String enemyType) {
        this.row = row;
        this.col = col;
        this.hitCounter = hitCounter;
        this.enemyType = enemyType;

        this.x = 80 + col * 70;
        this.y = 50 + row * 60;

        spawnEnemy();
    }

    public void spawnEnemy() {
        if (hitCounter > 0) {
            switch (enemyType) {
                case "Fast":
                    currentEnemy = new FastChicken(x, y);
                    break;
                case "Zigzag":
                    currentEnemy = new ZigzagChicken(x, y);
                    break;
                case "Shooter":
                    currentEnemy = new ShooterChicken(x, y);
                    break;
                default:
                    currentEnemy = new NormalChicken(x, y);
                    break;
            }

            int startX = (Math.random() < 0.5) ? -50 : 850;
            int startY = -50;

            currentEnemy.startSpawnAnimation(startX, startY, this.x, this.y);
        } else {
            currentEnemy = null;
        }
    }

    public void enemyKilled() {
        this.hitCounter--;
        spawnEnemy();
    }

    public void updatePosition(int deltaX, int deltaY) {
        this.x += deltaX;
        this.y += deltaY;
        if (currentEnemy != null) {
            if (!currentEnemy.isSpawning()) {
                currentEnemy.x = this.x;
                currentEnemy.y = this.y;
            }
        }
    }

    public Enemy getCurrentEnemy() { return currentEnemy; }
    public int getHitCounter() { return hitCounter; }
}