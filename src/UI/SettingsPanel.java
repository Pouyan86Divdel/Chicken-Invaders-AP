package UI;

import sound.SoundManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class SettingsPanel extends JPanel {
    private Image backgroundImage;
    private final int BUTTON_WIDTH = 240;
    private final int BUTTON_HEIGHT = 45;

    private JButton musicBtn;
    private JButton sfxBtn;

    public SettingsPanel(GameMain gameMain) {
        backgroundImage = new ImageIcon("assets/images/background/background.jpg").getImage();
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel titleLabel = new JLabel("SETTINGS", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Impact", Font.BOLD, 50));
        titleLabel.setForeground(new Color(255, 215, 0));
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 40, 0);
        add(titleLabel, gbc);

        musicBtn = createStyledButton(getMusicButtonText(), SoundManager.isBgmEnabled());
        musicBtn.addActionListener(e -> {
            boolean currentState = SoundManager.isBgmEnabled();
            SoundManager.setBgmEnabled(!currentState);

            if (!currentState) {
                SoundManager.playBGM("assets/sounds/Chicken Invaders 2 Remastered OST - Main Theme.wav");
            }

            updateButtonStates();
        });
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 0, 10, 0);
        add(musicBtn, gbc);

        sfxBtn = createStyledButton(getSFXButtonText(), SoundManager.isSfxEnabled());
        sfxBtn.addActionListener(e -> {
            boolean currentState = SoundManager.isSfxEnabled();
            SoundManager.setSfxEnabled(!currentState);

            if (!currentState) {
                SoundManager.playSFX("assets/sounds/mixkit-short-laser-gun-shot-1670.wav");
            }

            updateButtonStates();
        });
        gbc.gridy = 2;
        add(sfxBtn, gbc);

        JButton backButton = createStyledButton("Back to Menu", true);
        backButton.addActionListener(e -> gameMain.changePanel("MainMenu"));
        gbc.gridy = 3;
        gbc.insets = new Insets(30, 0, 0, 0);
        add(backButton, gbc);
    }

    private void updateButtonStates() {
        musicBtn.setText(getMusicButtonText());
        sfxBtn.setText(getSFXButtonText());
        repaint();
    }

    private String getMusicButtonText() {
        return SoundManager.isBgmEnabled() ? "Music: ON" : "Music: OFF";
    }

    private String getSFXButtonText() {
        return SoundManager.isSfxEnabled() ? "Sound Effects: ON" : "Sound Effects: OFF";
    }

    private JButton createStyledButton(String text, boolean isEnabled) {
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

                if (getText().contains("OFF")) {
                    g2.setColor(new Color(220, 50, 50));
                } else {
                    g2.setColor(new Color(70, 130, 250));
                }
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
        button.setFont(new Font("Arial", Font.BOLD, 15));
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