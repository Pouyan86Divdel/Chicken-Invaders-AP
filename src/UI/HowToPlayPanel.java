package UI;

import javax.swing.*;
import java.awt.*;

public class HowToPlayPanel extends JPanel {
    private Image backgroundImage;

    public HowToPlayPanel(GameMain gameMain) {
        setLayout(new BorderLayout(20, 20));
        backgroundImage = new ImageIcon("assets/images/background/background.jpg").getImage();

        JLabel titleLabel = new JLabel("HOW TO PLAY", JLabel.CENTER);
        titleLabel.setFont(new Font("Impact", Font.BOLD, 40));
        titleLabel.setForeground(Color.YELLOW);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 10, 10, 10));
        add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        String[] columnNames = {"Control Key", "Action Description"};
        Object[][] data = {
                {"\u2192 / D", "Move Right"},
                {"\u2190 / A", "Move Left"},
                {"\u2191 / W", "Move Up"},
                {"\u2193 / S", "Move Down"},
                {"Space", "Fire Bullet"},
                {"P", "Pause / Resume Game"},
                {"Esc", "Return to Main Menu (Game Over)"}
        };

        JTable table = new JTable(data, columnNames) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setFont(new Font("Arial", Font.BOLD, 16));
        table.setRowHeight(35);
        table.setForeground(Color.WHITE);
        table.setBackground(new Color(0, 0, 0, 150));
        table.setGridColor(Color.GRAY);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 18));
        table.getTableHeader().setBackground(Color.DARK_GRAY);
        table.getTableHeader().setForeground(Color.YELLOW);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(500, 280));
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2));

        JPanel tableWrapper = new JPanel();
        tableWrapper.setOpaque(false);
        tableWrapper.add(scrollPane);
        centerPanel.add(tableWrapper);

        add(centerPanel, BorderLayout.CENTER);

        JButton backButton = new JButton("BACK TO MENU");
        backButton.setFont(new Font("Arial", Font.BOLD, 18));
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(Color.RED);
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> gameMain.changePanel("MainMenu"));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 40, 10));
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        } else {
            g.setColor(Color.BLUE);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}