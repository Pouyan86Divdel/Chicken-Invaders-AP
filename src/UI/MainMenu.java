package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class MainMenu extends JPanel {
    private GameMain gameMain;
    private Image backgroundImage;
    private JLabel userLabel;
    private JButton logoutBtn;
    private final int BUTTON_WIDTH = 220;
    private final int BUTTON_HEIGHT = 45;

    public MainMenu(GameMain gameMain) {
        this.gameMain = gameMain;
        sound.SoundManager.playBGM("assets/sounds/Chicken Invaders 2 Remastered OST - Main Theme.wav");
        backgroundImage = new ImageIcon("assets/images/background/background.jpg").getImage();
        setLayout(null);

        userLabel = new JLabel("User: Guest");
        userLabel.setFont(new Font("Arial", Font.BOLD, 16));
        userLabel.setForeground(Color.WHITE);
        userLabel.setBounds(20, 20, 300, 30);
        add(userLabel);

        logoutBtn = new JButton("Login") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (gameMain.getCurrentUsername().equals("Guest")) {
                    if (getModel().isPressed()) {
                        g2.setColor(new Color(20, 100, 40));
                    } else if (getModel().isRollover()) {
                        g2.setColor(new Color(40, 160, 80));
                    } else {
                        g2.setColor(new Color(30, 130, 60, 220));
                    }
                    g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                    g2.setColor(new Color(80, 220, 120));
                } else {
                    if (getModel().isPressed()) {
                        g2.setColor(new Color(150, 20, 20));
                    } else if (getModel().isRollover()) {
                        g2.setColor(new Color(220, 50, 50));
                    } else {
                        g2.setColor(new Color(180, 30, 30, 220));
                    }
                    g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                    g2.setColor(new Color(250, 80, 80));
                }
                g2.setStroke(new BasicStroke(1.5f));
                g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 10, 10));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        logoutBtn.setBounds(660, 18, 100, 35);
        logoutBtn.setContentAreaFilled(false);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setOpaque(false);
        logoutBtn.setFont(new Font("Arial", Font.BOLD, 14));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        });
        logoutBtn.addActionListener(e -> {
            if (gameMain.getCurrentUsername().equals("Guest")) {
                gameMain.changePanel("LoginPanel");
            } else {
                int response = JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to logout?",
                        "Logout",
                        JOptionPane.YES_NO_OPTION
                );
                if (response == JOptionPane.YES_OPTION) {
                    gameMain.logout();
                }
            }
        });
        add(logoutBtn);

        JLabel titleLabel = new JLabel("CHICKEN INVADERS", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Impact", Font.BOLD, 55));
        titleLabel.setForeground(new Color(255, 215, 0));
        titleLabel.setBounds(100, 70, 600, 70);
        add(titleLabel);

        JButton newGameBtn = createStyledButton("New Game");
        JButton highScoresBtn = createStyledButton("High Scores");
        JButton settingsBtn = createStyledButton("Settings");
        JButton howToPlayBtn = createStyledButton("How to Play");
        JButton storeBtn = createStyledButton("Store");
        JButton exitBtn = createStyledButton("Exit");

        newGameBtn.addActionListener(e -> {
            if (gameMain.getCurrentUsername().equals("Guest")) {
                gameMain.changePanel("LoginPanel");
            } else {
                gameMain.getGamePanel().startGame();
                gameMain.changePanel("GamePanel");
            }
        });
        highScoresBtn.addActionListener(e -> gameMain.changePanel("HighScorePanel"));
        settingsBtn.addActionListener(e -> gameMain.changePanel("Settings"));
        howToPlayBtn.addActionListener(e -> gameMain.changePanel("HowToPlay"));
        storeBtn.addActionListener(e -> gameMain.changePanel("Store"));
        exitBtn.addActionListener(e -> System.exit(0));

        JButton[] buttons = {newGameBtn, highScoresBtn, settingsBtn, howToPlayBtn, storeBtn, exitBtn};
        int startY = 160;
        int spacing = 55;

        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setBounds(290, startY + (i * spacing), BUTTON_WIDTH, BUTTON_HEIGHT);
            add(buttons[i]);
        }
    }

    public void updateMenuUI() {
        String username = gameMain.getCurrentUsername();
        userLabel.setText("User: " + username);

        if (username.equals("Guest")) {
            logoutBtn.setText("Login");
        } else {
            logoutBtn.setText("Logout");
        }
        repaint();
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
        button.setFont(new Font("Arial", Font.BOLD, 16));
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