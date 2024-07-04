package fr.poloxpn.epicjump.manager;

import fr.poloxpn.epicjump.EpicJump;
import fr.poloxpn.epicjump.parkour.Checkpoint;
import fr.poloxpn.epicjump.parkour.Course;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

public class CourseManager {
    private final ConfigManager configManager;
    private final Map<String, Course> courses = new HashMap<>();
    private final EpicJump plugin;

    public CourseManager(EpicJump plugin) {
        this.plugin = plugin;
        this.configManager = plugin.getConfigManager();
    }

    public void createCourse(String name) {
        courses.put(name, new Course(name));
        saveCourse(name);
    }
    public boolean courseExists(String name) {
        return courses.containsKey(name);
    }

    public Course getCourse(String name) {
        return courses.get(name);
    }

    public Map<String, Course> getCourses() {
        return courses;
    }

    public void setSpawn(String name, Location location) {
        Course course = courses.get(name);
        if (course != null) {
            course.setSpawnLocation(location);
            saveCourse(name);
        }
    }
    public void setStart(String name, Location location) {
        Course course = courses.get(name);
        if (course != null) {
            course.setStartLocation(location);
            saveCourse(name);
        }
    }
    public void setEnd(String name, Location location) {
        Course course = courses.get(name);
        if (course != null) {
            course.setEndLocation(location);
            saveCourse(name);
        }
    }

    public void addCourse(Course course) {
        courses.put(course.getName(), course);
    }

    public void addCheckpoint(String name, Location location) {
        Course course = courses.get(name);
        if (courseExists(name)) {
            course.addCheckpoint(new Checkpoint(location));
            saveCourse(name);
        }
    }

    public void removeCheckpoint(String name, Location location) {
        Course course = courses.get(name);
        if (course != null) {
            course.getCheckpoints().removeIf(checkpoint -> checkpoint.getLocation().equals(location));
            saveCourse(name);
        }
    }

    public void removeAllCheckpoints(String name) {
        Course course = courses.get(name);
        if (course != null) {
            course.getCheckpoints().clear();
            saveCourse(name);
        }
    }

    public void deleteCourse(String name) {
        FileConfiguration coursesConfig = configManager.loadConfig("courses.yml");
        plugin.getLeaderboardManager().clearLeaderboard(name);
        plugin.getHologramManager().removeAllCourseHolograms(name);
        courses.remove(name);
        coursesConfig.set("courses." + name, null);
        configManager.saveConfig(coursesConfig, "courses.yml");
    }

    public boolean hasCheckpoints(String name) {
        Course course = courses.get(name);
        return course != null && !course.getCheckpoints().isEmpty();
    }

    public boolean isCheckpoint(String name, Location location) {
        Course course = courses.get(name);
        if (course != null) {
            for (Checkpoint checkpoint : course.getCheckpoints()) {
                if (checkpoint.getLocation().equals(location)) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<String> getCourseNames() {
        return new ArrayList<>(courses.keySet());
    }

    /**
     * Load all courses from the courses.yml file
     */
    public void loadCourses() {
        FileConfiguration coursesConfig = configManager.loadConfig("courses.yml");

        if(coursesConfig.contains("courses")) {
            for (String courseName : coursesConfig.getConfigurationSection("courses").getKeys(false)) {
                Course course = new Course(courseName);

                String path = "courses." + courseName;
                String startLocationString = coursesConfig.getString(path + ".start");
                if (startLocationString != null) {
                    course.setStartLocation(configManager.deserializeLocation(startLocationString));
                }
                // Check and set end location
                String endLocationString = coursesConfig.getString(path + ".end");
                if (endLocationString != null) {
                    course.setEndLocation(configManager.deserializeLocation(endLocationString));
                }

                // Check and set spawn location
                String spawnLocationString = coursesConfig.getString(path + ".spawn");
                if (spawnLocationString != null) {
                    course.setSpawnLocation(configManager.deserializeLocation(spawnLocationString));
                }

                // Set lives per checkpoint, with a default of 1
                course.setLivesPerCheckpoint(coursesConfig.getInt(path + ".lives-per-checkpoint", 1));


                if (coursesConfig.contains(path + ".checkpoints")) {
                    List<String> checkpointList = coursesConfig.getStringList(path + ".checkpoints");
                    for (String checkpoint : checkpointList) {
                        course.addCheckpoint(new Checkpoint(configManager.deserializeLocation(checkpoint)));
                    }
                }
                this.addCourse(course);
            }
        }
    }

    /**
     * Save a course to the courses.yml file
     * @param courseName The name of the course to save
     */
    private void saveCourse(String courseName) {
        FileConfiguration coursesConfig = configManager.loadConfig("courses.yml");
        Course course = courses.get(courseName);
        if (course != null) {
            String path = "courses." + courseName;

            coursesConfig.set(path + ".live-per-checkpoint", course.getLivesPerCheckpoint());
            coursesConfig.set(path + ".spawn", plugin.getConfigManager().serializeLocation(course.getSpawnLocation()));
            coursesConfig.set(path + ".start", plugin.getConfigManager().serializeLocation(course.getStartLocation()));
            coursesConfig.set(path + ".end", plugin.getConfigManager().serializeLocation(course.getEndLocation()));

            List<String> checkpointList = new ArrayList<>();
            for (Checkpoint checkpoint : course.getCheckpoints()) {
                checkpointList.add(plugin.getConfigManager().serializeLocation(checkpoint.getLocation()));
            }

            coursesConfig.set(path + ".checkpoints", checkpointList);
            configManager.saveConfig(coursesConfig, "courses.yml");
        }
    }

}