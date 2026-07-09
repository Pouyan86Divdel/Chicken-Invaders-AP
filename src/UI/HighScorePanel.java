package UI;

import database.DatabaseManager;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.List;

public class HighScorePanel extends JPanel {
    private Image backgroundImage;
    private JTable scoreTable;
    private DefaultTableModel tableModel;
    private final int BUTTON_WIDTH = 200;
    private final int BUTTON_HEIGHT = 40;

    public HighScorePanel(GameMain gameMain) {
        backgroundImage = new ImageIcon("assets/images/background/background.jpg").getImage();
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("TOP 5 HIGH SCORES", SwingConstants.CENTER);
        titleLabel.setForeground(new Color(255, 215, 0));
        titleLabel.setFont(new Font("Impact", Font.PLAIN, 42));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 10, 20, 10));
        add(titleLabel, BorderLayout.NORTH);

        String[] columns = {"Player Name", "Top Score", "Last Level", "Game Date"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        scoreTable = new JTable(tableModel);
        scoreTable.setFont(new Font("Arial", Font.PLAIN, 16));
        scoreTable.setRowHeight(35);
        scoreTable.setForeground(Color.WHITE);
        scoreTable.setBackground(new Color(20, 30, 60, 200));
        scoreTable.setGridColor(new Color(70, 130, 250, 100));
        scoreTable.setOpaque(false);
        scoreTable.setSelectionBackground(new Color(40, 80, 160));

        JTableHeader header = scoreTable.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 16));
        header.setBackground(new Color(28, 54, 115));
        header.setForeground(Color.YELLOW);
        header.setReorderingAllowed(false);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        centerRenderer.setOpaque(false);
        for (int i = 0; i < scoreTable.getColumnCount(); i++) {
            scoreTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(scoreTable);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 40, 10));

        JButton backBtn = createStyledButton("Back to Menu");
        backBtn.addActionListener(e -> gameMain.changePanel("MainMenu"));
        bottomPanel.add(backBtn);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void updateScores() {
        tableModel.setRowCount(0);
        List<String[]> topScores = DatabaseManager.getTopScores();
        for (String[] row : topScores) {
            tableModel.addRow(row);
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