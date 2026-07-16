package UI;

import database.DatabaseManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StorePanel extends JPanel {
    private GameMain gameMain;
    private JLabel coinsLabel;
    private JLabel speedLevelLabel, fireLevelLabel, healthLevelLabel;
    private JButton buySpeedBtn, buyFireBtn, buyHealthBtn;
    private Image backgroundImage;

    private int speedCost = 100;
    private int fireCost = 250;
    private int healthCost = 150;

    public StorePanel(GameMain gameMain) {
        this.gameMain = gameMain;
        setLayout(null);
        backgroundImage = new ImageIcon("assets/images/background/background.jpg").getImage();


        JLabel titleLabel = new JLabel("SPACE SHOP", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Impact", Font.BOLD, 48));
        titleLabel.setForeground(Color.YELLOW);
        titleLabel.setBounds(200, 30, 400, 60);
        add(titleLabel);

        coinsLabel = new JLabel("Coins: 0", SwingConstants.CENTER);
        coinsLabel.setFont(new Font("Arial", Font.BOLD, 22));
        coinsLabel.setForeground(Color.WHITE);
        coinsLabel.setBounds(300, 100, 200, 30);
        add(coinsLabel);

        createShopItem("Upgrade Speed", 150, "speed_level", speedCost);
        createShopItem("Upgrade Firepower", 260, "fire_level", fireCost);
        createShopItem("Upgrade Max Health", 370, "max_health", healthCost);

        JButton backBtn = new JButton("Back to Menu");
        backBtn.setFont(new Font("Arial", Font.BOLD, 16));
        backBtn.setBounds(300, 500, 200, 40);
        backBtn.addActionListener(e -> gameMain.changePanel("MainMenu"));
        add(backBtn);
    }

    private void createShopItem(String name, int y, String dbColumn, int baseCost) {
        JLabel itemLabel = new JLabel(name);
        itemLabel.setFont(new Font("Arial", Font.BOLD, 18));
        itemLabel.setForeground(Color.WHITE);
        itemLabel.setBounds(100, y, 200, 30);
        add(itemLabel);

        JLabel levelLabel = new JLabel("Lvl: 1");
        levelLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        levelLabel.setForeground(Color.GREEN);
        levelLabel.setBounds(320, y, 80, 30);
        add(levelLabel);

        if (dbColumn.equals("speed_level")) speedLevelLabel = levelLabel;
        else if (dbColumn.equals("fire_level")) fireLevelLabel = levelLabel;
        else if (dbColumn.equals("max_health")) healthLevelLabel = levelLabel;

        JButton buyBtn = new JButton("Buy (" + baseCost + " Coins)");
        buyBtn.setFont(new Font("Arial", Font.BOLD, 14));
        buyBtn.setBounds(450, y, 180, 30);
        buyBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = gameMain.getCurrentUsername();
                if (username.equals("Guest")) {
                    JOptionPane.showMessageDialog(StorePanel.this, "Guest accounts cannot buy upgrades!", "Shop Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int currentCoins = DatabaseManager.getCoins(username);
                int currentLvl = DatabaseManager.getUpgradeLevel(username, dbColumn);
                int cost = baseCost * currentLvl;

                if (currentCoins >= cost) {
                    DatabaseManager.upgradeAttribute(username, dbColumn, cost);
                    sound.SoundManager.playSFX("assets/sounds/mixkit-short-laser-gun-shot-1670.wav");
                    updateStoreUI();
                } else {
                    JOptionPane.showMessageDialog(StorePanel.this, "Not enough coins!", "Shop Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        add(buyBtn);

        if (dbColumn.equals("speed_level")) buySpeedBtn = buyBtn;
        else if (dbColumn.equals("fire_level")) buyFireBtn = buyBtn;
        else if (dbColumn.equals("max_health")) buyHealthBtn = buyBtn;
    }

    public void updateStoreUI() {
        String username = gameMain.getCurrentUsername();
        int coins = DatabaseManager.getCoins(username);
        coinsLabel.setText("Coins: " + coins);

        int speedLvl = DatabaseManager.getUpgradeLevel(username, "speed_level");
        int fireLvl = DatabaseManager.getUpgradeLevel(username, "fire_level");
        int healthLvl = DatabaseManager.getUpgradeLevel(username, "max_health");

        speedLevelLabel.setText("Lvl: " + speedLvl);
        fireLevelLabel.setText("Lvl: " + fireLvl);
        healthLevelLabel.setText("Lvl: " + healthLvl);

        buySpeedBtn.setText("Buy (" + (speedCost * speedLvl) + " Coins)");
        buyFireBtn.setText("Buy (" + (fireCost * fireLvl) + " Coins)");
        buyHealthBtn.setText("Buy (" + (healthCost * healthLvl) + " Coins)");
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, 800, 600, this);
        }
    }
}