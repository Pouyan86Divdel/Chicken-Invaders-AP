package UI;

import javax.swing.*;
import java.awt.*;

public class SettingsPanel extends JPanel {
    public SettingsPanel(GameMain gameMain) {
        setBackground(Color.MAGENTA);
        setLayout(new GridBagLayout());
        JLabel label = new JLabel("Settings Screen");
        label.setForeground(Color.WHITE);
        add(label);
        JButton backButton = new JButton("Back to Menu");
        backButton.addActionListener(e -> gameMain.changePanel("MainMenu"));
        add(backButton);
    }
}