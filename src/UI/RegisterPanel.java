package UI;

import database.DatabaseManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class RegisterPanel extends JPanel {
    private Image backgroundImage;
    private final int BUTTON_WIDTH = 260;
    private final int BUTTON_HEIGHT = 40;

    public RegisterPanel(GameMain gameMain) {
        backgroundImage = new ImageIcon("assets/images/background/background.jpg").getImage();
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("REGISTER NEW USER", SwingConstants.CENTER);
        titleLabel.setForeground(new Color(255, 215, 0));
        titleLabel.setFont(new Font("Impact", Font.PLAIN, 40));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 10, 30, 10);
        add(titleLabel, gbc);

        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.gridwidth = 1;

        JLabel userLabel = new JLabel("Username:");
        userLabel.setForeground(Color.WHITE);
        userLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0; gbc.gridy = 1;
        add(userLabel, gbc);

        JTextField userField = new JTextField(15);
        userField.setFont(new Font("Arial", Font.PLAIN, 14));
        userField.setPreferredSize(new Dimension(180, 30));
        gbc.gridx = 1; gbc.gridy = 1;
        add(userField, gbc);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setForeground(Color.WHITE);
        passLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0; gbc.gridy = 2;
        add(passLabel, gbc);

        JPasswordField passField = new JPasswordField(15);
        passField.setFont(new Font("Arial", Font.PLAIN, 14));
        passField.setPreferredSize(new Dimension(180, 30));
        gbc.gridx = 1; gbc.gridy = 2;
        add(passField, gbc);

        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;

        JButton registerBtn = createStyledButton("Register");
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.insets = new Insets(20, 8, 8, 8);
        add(registerBtn, gbc);

        JButton backBtn = createStyledButton("Already have an account? Login");
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.insets = new Insets(8, 8, 8, 8);
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
                JOptionPane.showMessageDialog(this, "Registration Successful! Entering game...", "Success", JOptionPane.INFORMATION_MESSAGE);
                gameMain.setCurrentUsername(username);
                gameMain.changePanel("GamePanel");
                gameMain.getGamePanel().startGame();
            } else {
                JOptionPane.showMessageDialog(this, "Username already exists!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        backBtn.addActionListener(e -> gameMain.changePanel("LoginPanel"));
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2.setColor(new Color(20, 40, 85));
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(40, 80, 160));
                } else {
                    g2.setColor(new Color(28, 54, 115, 220));
                }

                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 15, 15));

                g2.setColor(new Color(70, 130, 250));
                g2.setStroke(new BasicStroke(1.5f));
                g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 15, 15));

                g2.dispose();
                super.paintComponent(g);
            }
        };

        button.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        });

        return button;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}