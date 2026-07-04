package UI;

import javax.swing.*;
import java.awt.*;

public class GameMain extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    public GameMain() {
        setTitle("Chicken Invaders");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        MainMenu mainMenu = new MainMenu(this);
        SettingsPanel settingsPanel = new SettingsPanel(this);
        HowToPlayPanel howToPlayPanel = new HowToPlayPanel(this);
        JPanel mockGamePanel = new JPanel();
        mockGamePanel.setBackground(Color.GREEN);
        JButton backFromGame = new JButton("End Game (Back to Menu)");
        backFromGame.addActionListener(e -> changePanel("MainMenu"));
        mockGamePanel.add(backFromGame);
        mainPanel.add(mainMenu, "MainMenu");
        mainPanel.add(mockGamePanel, "GamePanel");
        mainPanel.add(settingsPanel, "Settings");
        mainPanel.add(howToPlayPanel, "HowToPlay");
        add(mainPanel);
        cardLayout.show(mainPanel, "MainMenu");
    }
    public void changePanel(String panelName) {
        cardLayout.show(mainPanel, panelName);
    }
}