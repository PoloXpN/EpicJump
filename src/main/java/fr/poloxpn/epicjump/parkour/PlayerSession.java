package fr.poloxpn.epicjump.parkour;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayerSession {

    private final Player player;
    private final Course course;
    private int lives;
    private boolean unlimitedLives = false;
    private Location spawnLocation, startLocation, endLocation, lastCheckpoint;
    private List<Checkpoint> checkpoints = new ArrayList<>();
    private long startTime;

    public PlayerSession(Player player, Course course) {
        this.player = player;
        this.course = course;
        this.lives = course.getStartingLives();
        this.startTime = System.currentTimeMillis();
        this.checkpoints.addAll(course.getCheckpoints());
        this.spawnLocation = course.getSpawnLocation();
        this.startLocation = course.getStartLocation();
        this.endLocation = course.getEndLocation();
        this.lastCheckpoint = this.spawnLocation;
        this.unlimitedLives = course.getLivesPerCheckpoint() == -1;
    }

    public Player getPlayer() {
        return player;
    }

    public Course getCourse() {
        return course;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public Location getStartLocation() {
        return startLocation;
    }

    public Location getEndLocation() {
        return endLocation;
    }

    public Location getLastCheckpoint() {
        return lastCheckpoint;
    }

    public void setLastCheckpoint(Location lastCheckpoint) {
        this.lastCheckpoint = lastCheckpoint;
    }

    public List<Checkpoint> getCheckpoints() {
        return checkpoints;
    }
    public Checkpoint getCheckpoint(Location location) {
        for (Checkpoint checkpoint : checkpoints) {
            if (checkpoint.getLocation().equals(location)) {
                return checkpoint;
            }
        }
        return null;
    }

    public long getStartTime() {
        return startTime;
    }
    public void setStartTime() {
        this.startTime = System.currentTimeMillis();
    }

    public boolean hasUnlimitedLives() {
        return unlimitedLives;
    }


}
