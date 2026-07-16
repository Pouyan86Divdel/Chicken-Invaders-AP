package UI;

import javax.swing.*;
import java.awt.*;

public class GameMain extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private GamePanel gamePanel;
    private StorePanel storePanel;
    private String currentUsername = "Guest";

    public GameMain() {
        setTitle("Chicken Invaders");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        gamePanel = new GamePanel(this);
        storePanel = new StorePanel(this);
        MainMenu mainMenu = new MainMenu(this);
        SettingsPanel settingsPanel = new SettingsPanel(this);
        HowToPlayPanel howToPlayPanel = new HowToPlayPanel(this);
        HighScorePanel highScorePanel = new HighScorePanel(this);

        JPanel mockGamePanel = new JPanel();
        mockGamePanel.setBackground(Color.GREEN);
        JButton backFromGame = new JButton("End Game (Back to Menu)");
        backFromGame.addActionListener(e -> changePanel("MainMenu"));
        mockGamePanel.add(backFromGame);

        mainPanel.add(mainMenu, "MainMenu");
        mainPanel.add(gamePanel, "GamePanel");
        mainPanel.add(storePanel, "Store");
        mainPanel.add(settingsPanel, "Settings");
        mainPanel.add(howToPlayPanel, "HowToPlay");
        mainPanel.add(highScorePanel, "HighScorePanel");

        LoginPanel loginPanel = new LoginPanel(this);
        RegisterPanel registerPanel = new RegisterPanel(this);
        mainPanel.add(loginPanel, "LoginPanel");
        mainPanel.add(registerPanel, "RegisterPanel");

        add(mainPanel);
        cardLayout.show(mainPanel, "MainMenu");
    }

    public String getCurrentUsername() {
        return currentUsername;
    }

    public void setCurrentUsername(String username) {
        this.currentUsername = username;
    }

    public GamePanel getGamePanel() {
        return this.gamePanel;
    }

    public void changePanel(String panelName) {
        if (panelName.equals("HighScorePanel")) {
            for (Component comp : mainPanel.getComponents()) {
                if (comp instanceof HighScorePanel) {
                    ((HighScorePanel) comp).updateScores();
                }
            }
        }

        if (panelName.equals("Store")) {
            storePanel.updateStoreUI();
        }

        cardLayout.show(mainPanel, panelName);

        if (panelName.equals("GamePanel")) {
            for (Component comp : mainPanel.getComponents()) {
                if (comp instanceof UI.GamePanel) {
                    comp.requestFocusInWindow();
                }
            }
        }
    }
}