package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class MainMenu extends JPanel {
    private Image backgroundImage;
    private final int BUTTON_WIDTH = 220;
    private final int BUTTON_HEIGHT = 45;

    public MainMenu(GameMain gameMain) {
        sound.SoundManager.playBGM("assets/sounds/main_theme.wav");
        backgroundImage = new ImageIcon("assets/images/background/background.jpg").getImage();
        setLayout(new GridBagLayout());

        JLabel titleLabel = new JLabel("CHICKEN INVADERS", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Impact", Font.BOLD, 55));
        titleLabel.setForeground(new Color(255, 215, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 40, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        add(titleLabel, gbc);

        JButton newGameBtn = createStyledButton("New Game");
        JButton highScoresBtn = createStyledButton("High Scores");
        JButton settingsBtn = createStyledButton("Settings");
        JButton howToPlayBtn = createStyledButton("How to Play");
        JButton storeBtn = createStyledButton("Store");
        JButton exitBtn = createStyledButton("Exit");

        newGameBtn.addActionListener(e -> gameMain.changePanel("LoginPanel"));
        highScoresBtn.addActionListener(e -> gameMain.changePanel("HighScorePanel"));
        settingsBtn.addActionListener(e -> gameMain.changePanel("Settings"));
        howToPlayBtn.addActionListener(e -> gameMain.changePanel("HowToPlay"));
        storeBtn.addActionListener(e -> gameMain.changePanel("Store"));
        exitBtn.addActionListener(e -> System.exit(0));

        JButton[] buttons = {newGameBtn, highScoresBtn, settingsBtn, howToPlayBtn, storeBtn, exitBtn};
        int row = 1;

        for (JButton btn : buttons) {
            gbc.gridy = row++;
            gbc.insets = new Insets(8, 0, 8, 0);
            add(btn, gbc);
        }
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