package database;

public class User {

    private String username;
    private String password;
    private int highScore;
    private int lastLevel;
    private int bgmOn;
    private int sfxShotOn;
    private int sfxCrashOn;
    private int sfxGameOverOn;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.highScore = 0;
        this.lastLevel = 1;
        this.bgmOn = 1;
        this.sfxShotOn = 1;
        this.sfxCrashOn = 1;
        this.sfxGameOverOn = 1;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public int getHighScore() { return highScore; }
    public void setHighScore(int highScore) { this.highScore = highScore; }

}