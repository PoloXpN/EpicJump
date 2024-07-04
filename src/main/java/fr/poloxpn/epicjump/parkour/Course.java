package fr.poloxpn.epicjump.parkour;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class Course {
    private String name;
    private Location spawnLocation, startLocation, endLocation, lastCheckpoint;
    private List<Checkpoint> checkpoints;
    private int lives_per_checkpoint;

    public Course(String name) {
        this.name = name;
        this.checkpoints = new ArrayList<>();
    }

    public String getName() {
        return name;
    }
    public Location getSpawnLocation() {
        return spawnLocation;
    }
    public void setSpawnLocation(Location spawnLocation) {
        this.spawnLocation = spawnLocation;
    }

    public Location getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(Location startLocation) {
        this.startLocation = startLocation;
    }

    public Location getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(Location endLocation) {
        this.endLocation = endLocation;
    }

    public List<Checkpoint> getCheckpoints() {
        return checkpoints;
    }

    public void addCheckpoint(Checkpoint checkpoint) {
        checkpoints.add(checkpoint);
    }

    public Location getLastCheckpoint() {
        return lastCheckpoint;
    }
    public void setLastCheckpoint(Location lastCheckpoint) {
        this.lastCheckpoint = lastCheckpoint;
    }

    public int getLivesPerCheckpoint() {
        return lives_per_checkpoint;
    }
    public void setLivesPerCheckpoint(int lives_per_checkpoint) {
        this.lives_per_checkpoint = lives_per_checkpoint;
    }
    public int getStartingLives() {
        return lives_per_checkpoint;
    }
}
