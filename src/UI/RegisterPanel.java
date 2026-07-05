package UI;

import database.DatabaseManager;
import javax.swing.*;
import java.awt.*;

public class RegisterPanel extends JPanel {
    public RegisterPanel(GameMain gameMain) {
        setBackground(Color.DARK_GRAY);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titleLabel = new JLabel("Register New User");
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

        JButton registerBtn = new JButton("Register");
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        add(registerBtn, gbc);

        JButton backBtn = new JButton("Already have an account? Login");
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        add(backBtn, gbc);

        registerBtn.addActionListener(e -> {
            String username = userField.getText().trim();
            String password = new String(passField.getPassword()).trim();
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            boolean success = DatabaseManager.registerUser(username, password);
            if (success) {
                JOptionPane.showMessageDialog(this, "Registration Successful! Please Login.", "Success", JOptionPane.INFORMATION_MESSAGE);
                gameMain.changePanel("LoginPanel"); // هدایت به صفحه لاگین
            } else {
                JOptionPane.showMessageDialog(this, "Username already exists!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        backBtn.addActionListener(e -> gameMain.changePanel("LoginPanel"));
    }
}