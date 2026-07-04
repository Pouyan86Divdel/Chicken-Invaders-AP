import UI.GameMain;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameMain game = new GameMain();
            game.setVisible(true);
        });
    }
}