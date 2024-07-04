package fr.poloxpn.epicjump.parkour;

import org.bukkit.Location;

public class Hologram {
    private String courseName, type;
    private int id;
    private Location location;
    private String[] lines;

    public Hologram(String courseName, String type, int id, Location location, String... lines) {
        this.courseName = courseName;
        this.type = type;
        this.id = id;
        this.location = location.getBlock().getLocation().add(0.5, 0.5, 0.5);
        this.lines = lines;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getType() {
        return type;
    }
    public int getId() {
        return id;
    }

    public Location getLocation() {
        return location;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setType(String type) {
        this.type = type;
    }
    public void setId(int id) {
        this.id = id;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setLine(int index, String line) {
        this.lines[index] = line;
    }
    public void setLines(String[] lines) {
        this.lines = lines;
    }

    public String[] getLines() {
        return lines;
    }


}
