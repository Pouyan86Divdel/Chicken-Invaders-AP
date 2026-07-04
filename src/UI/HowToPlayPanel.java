package UI;

import javax.swing.*;
import java.awt.*;

public class HowToPlayPanel extends JPanel {
    public HowToPlayPanel(GameMain gameMain) {
        setBackground(Color.BLUE);
        setLayout(new GridBagLayout());
        JLabel label = new JLabel("How to Play Screen");
        label.setForeground(Color.WHITE);
        add(label);
        JButton backButton = new JButton("Back to Menu");
        backButton.addActionListener(e -> gameMain.changePanel("MainMenu"));
        add(backButton);
    }
}