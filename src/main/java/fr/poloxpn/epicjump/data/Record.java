package fr.poloxpn.epicjump.data;

public class Record {

    private String courseName;
    private long time;
    private String playerName;
    private int lives;

    public Record(String courseName,String playerName, long time, int lives) {
        this.courseName = courseName;
        this.playerName = playerName;
        this.time = time;
        this.lives = lives;
    }

    public String getCourse() {
        return courseName;
    }

    public long getTime() {
        return time;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getLives() {
        return lives;
    }

    public void setCourse(String courseName) {
        this.courseName = courseName;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }


}
