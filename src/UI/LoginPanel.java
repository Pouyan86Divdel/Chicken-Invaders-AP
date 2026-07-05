package UI;

import database.DatabaseManager;
import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {
    public LoginPanel(GameMain gameMain) {

        setBackground(Color.DARK_GRAY);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titleLabel = new JLabel("Login to Game");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(titleLabel, gbc);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setForeground(Color.WHITE);
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        add(userLabel, gbc);

        JTextField userField = new JTextField(15);
        gbc.gridx = 1; gbc.gridy = 1;
        add(userField, gbc);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setForeground(Color.WHITE);
        gbc.gridx = 0; gbc.gridy = 2;
        add(passLabel, gbc);

        JPasswordField passField = new JPasswordField(15);
        gbc.gridx = 1; gbc.gridy = 2;
        add(passField, gbc);

        JButton loginBtn = new JButton("Login");
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        add(loginBtn, gbc);

        JButton goToRegisterBtn = new JButton("Don't have an account? Register");
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        add(goToRegisterBtn, gbc);

        JButton backToMenuBtn = new JButton("Back to Main Menu");
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        add(backToMenuBtn, gbc);

        loginBtn.addActionListener(e -> {
            String username = userField.getText().trim();
            String password = new String(passField.getPassword()).trim();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean loggedIn = DatabaseManager.loginUser(username, password);
            if (loggedIn) {
                JOptionPane.showMessageDialog(this, "Welcome " + username + "!", "Success", JOptionPane.INFORMATION_MESSAGE);
                gameMain.changePanel("GamePanel");
                gameMain.getGamePanel().startGame();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Username or Password!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        goToRegisterBtn.addActionListener(e -> gameMain.changePanel("RegisterPanel"));
        backToMenuBtn.addActionListener(e -> gameMain.changePanel("MainMenu"));
    }
}