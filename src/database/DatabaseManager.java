package database;

import java.sql.*;

public class DatabaseManager {

    private static final String URL = "jdbc:sqlite:game.db";

    private static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void createNewTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users ("
                + " username TEXT PRIMARY KEY,"
                + " password TEXT NOT NULL,"
                + " high_score INTEGER DEFAULT 0,"
                + " last_level INTEGER DEFAULT 1,"
                + " bgm INTEGER DEFAULT 1,"
                + " sfx_shot INTEGER DEFAULT 1,"
                + " sfx_crash INTEGER DEFAULT 1,"
                + " sfx_gameover INTEGER DEFAULT 1"
                + ");";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Database table initialized successfully.");
        } catch (SQLException e) {
            System.out.println("Error creating table: " + e.getMessage());
        }
    }
}