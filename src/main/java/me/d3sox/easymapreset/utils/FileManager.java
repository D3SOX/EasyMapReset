package me.d3sox.easymapreset.utils;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class FileManager {

    private File file;
    @Getter
    private FileConfiguration config;
    private Plugin plugin;

    public FileManager(Plugin plugin, String config) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), config);
        this.config = YamlConfiguration.loadConfiguration(this.file);
        this.config.options().header("Configuration for " + this.plugin.getDescription().getName() + " version " + this.plugin.getDescription().getVersion());
        this.saveConfig();
    }

    private void saveConfig() {
        try {
            this.config.save(this.file);
        } catch (IOException ignored) {
        }
    }

    private String getPluginName() {
        return this.plugin.getDescription().getName();
    }

    public List<String> getStringList(String name) {
        return this.config.getStringList(this.getPluginName() + "." + name);
    }

    public void set(String valueName, Object value) {
        this.config.set(this.getPluginName() + "." + valueName, value);
        this.saveConfig();
    }

    public boolean is(String valueName) {
        return this.config.contains(this.getPluginName() + "." + valueName);
    }

    public void setBoolean(String valueName, boolean value) {
        this.config.set(this.getPluginName() + "." + valueName, value);
        this.saveConfig();
    }

    public boolean getBoolean(String valueName) {
        return this.config.getBoolean(this.getPluginName() + "." + valueName);
    }

    public void setString(String valueName, String value) {
        this.config.set(this.getPluginName() + "." + valueName, value);
        this.saveConfig();
    }

    public String getString(String valueName) {
        return this.config.getString(this.getPluginName() + "." + valueName);
    }

    public void setInt(String valueName, int value) {
        this.config.set(this.getPluginName() + "." + valueName, value);
        this.saveConfig();
    }

    public int getInt(String valueName) {
        return this.config.getInt(this.getPluginName() + "." + valueName);
    }

    public void setDouble(String valueName, double value) {
        this.config.set(this.getPluginName() + "." + valueName, value);
        this.saveConfig();
    }

    public double getDouble(String valueName) {
        return this.config.getDouble(this.getPluginName() + "." + valueName);
    }

    public void setLocation(String locName, World w, double x, double y, double z, float yaw, float pitch) {
        this.config.set(this.getPluginName() + "." + locName + ".w", w.getName());
        this.config.set(this.getPluginName() + "." + locName + ".x", x);
        this.config.set(this.getPluginName() + "." + locName + ".y", y);
        this.config.set(this.getPluginName() + "." + locName + ".z", z);
        this.config.set(this.getPluginName() + "." + locName + ".yaw", yaw);
        this.config.set(this.getPluginName() + "." + locName + ".pitch", pitch);
        this.saveConfig();
    }

    public void deleteLocation(String locName) {
        this.config.set(this.getPluginName() + "." + locName + ".w", null);
        this.config.set(this.getPluginName() + "." + locName + ".x", null);
        this.config.set(this.getPluginName() + "." + locName + ".y", null);
        this.config.set(this.getPluginName() + "." + locName + ".z", null);
        this.config.set(this.getPluginName() + "." + locName + ".yaw", null);
        this.config.set(this.getPluginName() + "." + locName + ".pitch", null);
        this.saveConfig();
    }

    public void setLocation(String locName, Player p) {
        this.config.set(this.getPluginName() + "." + locName + ".w", p.getWorld().getName());
        this.config.set(this.getPluginName() + "." + locName + ".x", p.getLocation().getX());
        this.config.set(this.getPluginName() + "." + locName + ".y", p.getLocation().getY());
        this.config.set(this.getPluginName() + "." + locName + ".z", p.getLocation().getZ());
        this.config.set(this.getPluginName() + "." + locName + ".yaw", p.getLocation().getYaw());
        this.config.set(this.getPluginName() + "." + locName + ".pitch", p.getLocation().getPitch());
        this.saveConfig();
    }

    public void setBlockLocation(String locName, Location loc) {
        this.config.set(this.getPluginName() + "." + locName + ".w", loc.getWorld().getName());
        this.config.set(this.getPluginName() + "." + locName + ".x", loc.getBlockX());
        this.config.set(this.getPluginName() + "." + locName + ".y", loc.getBlockY());
        this.config.set(this.getPluginName() + "." + locName + ".z", loc.getBlockZ());
        this.saveConfig();
    }

    public Location getBlockLocation(String locName) {
        return new Location(Bukkit.getWorld(this.config.getString(this.getPluginName() + "." + locName + ".w")),
                this.config.getInt(this.getPluginName() + "." + locName + ".x"), this.config.getInt(this.getPluginName() + "." + locName + ".y"),
                this.config.getInt(this.getPluginName() + "." + locName + ".z"));
    }

    public Location getLocation(String locName) {
        return new Location(Bukkit.getWorld(this.config.getString(this.getPluginName() + "." + locName + ".w")),
                this.config.getDouble(this.getPluginName() + "." + locName + ".x"), this.config.getDouble(this.getPluginName() + "." + locName + ".y"),
                this.config.getDouble(this.getPluginName() + "." + locName + ".z"), Float.valueOf(this.config.getString(this.getPluginName() + "." + locName + ".yaw")),
                Float.valueOf(this.config.getString(this.getPluginName() + "." + locName + ".pitch")));
    }

    public boolean isLocation(String locName) {
        return this.config.contains(this.getPluginName() + "." + locName + ".x");
    }

    public void delete(String locName) {
        this.config.set(this.getPluginName() + "." + locName, null);
        this.saveConfig();
    }

}
