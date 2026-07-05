package UI;

import javax.swing.*;
import java.awt.*;

public class MainMenu extends JPanel {
    public MainMenu(GameMain gameMain) {
        setBackground(Color.DARK_GRAY);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(Box.createRigidArea(new Dimension(0, 50)));
        JLabel titleLabel = new JLabel("CHICKEN INVADERS");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(titleLabel);
        add(Box.createRigidArea(new Dimension(0, 50)));
        JButton newGameBtn = new JButton("New Game");
        JButton settingsBtn = new JButton("Settings");
        JButton howToPlayBtn = new JButton("How to Play");
        JButton exitBtn = new JButton("Exit");
        newGameBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        settingsBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        howToPlayBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
//        newGameBtn.addActionListener(e -> gameMain.changePanel("GamePanel"));
        newGameBtn.addActionListener(e -> gameMain.changePanel("LoginPanel"));
        settingsBtn.addActionListener(e -> gameMain.changePanel("Settings"));
        howToPlayBtn.addActionListener(e -> gameMain.changePanel("HowToPlay"));
        exitBtn.addActionListener(e -> System.exit(0));
        add(newGameBtn);
        add(Box.createRigidArea(new Dimension(0, 15)));
        add(settingsBtn);
        add(Box.createRigidArea(new Dimension(0, 15)));
        add(howToPlayBtn);
        add(Box.createRigidArea(new Dimension(0, 15)));
        add(exitBtn);
    }
}