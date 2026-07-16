package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

public class DatabaseManager {

    private static final String URL = "jdbc:sqlite:game.db";

    private static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void createNewTable() {
        String userTableSql = "CREATE TABLE IF NOT EXISTS users ("
                + " username TEXT PRIMARY KEY,"
                + " password TEXT NOT NULL,"
                + " high_score INTEGER DEFAULT 0,"
                + " last_level INTEGER DEFAULT 1,"
                + " bgm INTEGER DEFAULT 1,"
                + " sfx_shot INTEGER DEFAULT 1,"
                + " sfx_crash INTEGER DEFAULT 1,"
                + " sfx_gameover INTEGER DEFAULT 1"
                + ");";

        String recordsTableSql = "CREATE TABLE IF NOT EXISTS game_records ("
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " username TEXT,"
                + " score INTEGER,"
                + " last_level INTEGER,"
                + " game_date TEXT,"
                + " sound_settings TEXT"
                + ");";

        String storeTableSql = "CREATE TABLE IF NOT EXISTS store ("
                + " username TEXT PRIMARY KEY,"
                + " coins INTEGER DEFAULT 0,"
                + " speed_level INTEGER DEFAULT 1,"
                + " fire_level INTEGER DEFAULT 1,"
                + " max_health INTEGER DEFAULT 1"
                + ");";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(userTableSql);
            stmt.execute(recordsTableSql);
            stmt.execute(storeTableSql);
            System.out.println("Database tables initialized successfully.");
        } catch (SQLException e) {
            System.out.println("Error creating tables: " + e.getMessage());
        }
    }

    public static boolean registerUser(String username, String password) {
        String sql = "INSERT INTO users(username, password) VALUES(?, ?)";
        String storeSql = "INSERT OR IGNORE INTO store(username, coins, speed_level, fire_level, max_health) VALUES(?, 0, 1, 1, 1)";

        try (Connection conn = connect()) {
            conn.setAutoCommit(false);
            try (PreparedStatement pstmt = conn.prepareStatement(sql);
                 PreparedStatement pstmtStore = conn.prepareStatement(storeSql)) {

                pstmt.setString(1, username);
                pstmt.setString(2, password);
                pstmt.executeUpdate();

                pstmtStore.setString(1, username);
                pstmtStore.executeUpdate();

                conn.commit();
                System.out.println("User " + username + " registered successfully.");
                return true;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            System.out.println("Registration failed: " + e.getMessage());
            return false;
        }
    }

    public static boolean loginUser(String username, String password) {
        String sql = "SELECT password FROM users WHERE username = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String storedPassword = rs.getString("password");
                return storedPassword.equals(password);
            }
            return false;
        } catch (SQLException e) {
            System.out.println("Login error: " + e.getMessage());
            return false;
        }
    }

    public static void saveGameRecord(String username, int score, int lastLevel, String soundSettings) {
        String sql = "INSERT INTO game_records(username, score, last_level, game_date, sound_settings) VALUES(?,?,?,?,?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setInt(2, score);
            pstmt.setInt(3, lastLevel);
            pstmt.setString(4, LocalDateTime.now().toString());
            pstmt.setString(5, soundSettings);
            pstmt.executeUpdate();

            updateHighScoreIfNeeded(username, score);
        } catch (SQLException e) {
            System.out.println("Error saving game record: " + e.getMessage());
        }
    }

    public static List<String[]> getTopScores() {
        List<String[]> list = new ArrayList<>();
        String sql = "SELECT username, MAX(score) as top_score, last_level, game_date FROM game_records GROUP BY username ORDER BY top_score DESC LIMIT 5";

        try (Connection conn = connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            while (rs.next()) {
                String[] row = new String[4];
                row[0] = rs.getString("username");
                row[1] = String.valueOf(rs.getInt("top_score"));
                row[2] = String.valueOf(rs.getInt("last_level"));
                String rawDate = rs.getString("game_date");
                row[3] = (rawDate != null && rawDate.length() >= 10) ? rawDate.substring(0, 10) : rawDate;
                list.add(row);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching top scores: " + e.getMessage());
        }
        return list;
    }

    private static void updateHighScoreIfNeeded(String username, int score) {
        String sqlSelect = "SELECT high_score FROM users WHERE username = ?";
        String sqlUpdate = "UPDATE users SET high_score = ? WHERE username = ?";
        try (Connection conn = connect();
             PreparedStatement pstmtSelect = conn.prepareStatement(sqlSelect)) {
            pstmtSelect.setString(1, username);
            ResultSet rs = pstmtSelect.executeQuery();
            if (rs.next()) {
                int currentMax = rs.getInt("high_score");
                if (score > currentMax) {
                    try (PreparedStatement pstmtUpdate = conn.prepareStatement(sqlUpdate)) {
                        pstmtUpdate.setInt(1, score);
                        pstmtUpdate.setString(2, username);
                        pstmtUpdate.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error updating high score: " + e.getMessage());
        }
    }

    public static int getCoins(String username) {
        String sql = "SELECT coins FROM store WHERE username = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("coins");
            }
        } catch (SQLException e) {
            System.out.println("Error getting coins: " + e.getMessage());
        }
        return 0;
    }

    public static void updateCoins(String username, int amount) {
        String sql = "INSERT INTO store(username, coins) VALUES(?, ?) "
                + "ON CONFLICT(username) DO UPDATE SET coins = coins + ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setInt(2, amount);
            pstmt.setInt(3, amount);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating coins: " + e.getMessage());
        }
    }

    public static int getUpgradeLevel(String username, String column) {
        String sql = "SELECT " + column + " FROM store WHERE username = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(column);
            }
        } catch (SQLException e) {
            System.out.println("Error getting upgrade level: " + e.getMessage());
        }
        return 1;
    }

    public static void upgradeAttribute(String username, String column, int cost) {
        String updateCoinsSql = "UPDATE store SET coins = coins - ? WHERE username = ?";
        String upgradeSql = "UPDATE store SET " + column + " = " + column + " + 1 WHERE username = ?";

        try (Connection conn = connect()) {
            conn.setAutoCommit(false);
            try (PreparedStatement pstmt1 = conn.prepareStatement(updateCoinsSql);
                 PreparedStatement pstmt2 = conn.prepareStatement(upgradeSql)) {

                pstmt1.setInt(1, cost);
                pstmt1.setString(2, username);
                pstmt1.executeUpdate();

                pstmt2.setString(1, username);
                pstmt2.executeUpdate();

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            System.out.println("Error upgrading attribute: " + e.getMessage());
        }
    }
}