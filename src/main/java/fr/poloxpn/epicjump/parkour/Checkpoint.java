package fr.poloxpn.epicjump.parkour;

import org.bukkit.Location;

public class Checkpoint {
    private final Location location;
    private final boolean isReached;

    public Checkpoint(Location location) {
        this.location = location;
        this.isReached = false;
    }

    public Checkpoint(Location location, boolean isReached) {
        this.location = location;
        this.isReached = isReached;
    }

    public Location getLocation() {
        return location;
    }

    public boolean isReached() {
        return isReached;
    }
}