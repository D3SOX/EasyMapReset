package me.d3sox.easymapreset;

import me.d3sox.easymapreset.utils.FileManager;
import me.d3sox.easymapreset.utils.WorldUtils;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

public class EasyMapReset extends JavaPlugin implements Listener {

    private final String PREFIX = "§5EasyMapReset §8» §7";
    private File mapFolder;
    private FileManager fileManager;
    private WorldUtils worldUtils;

    @Override
    public void onEnable() {
        this.fileManager = new FileManager(this, "settings.yml");

        this.mapFolder = new File(this.getDataFolder(), "mapsBackup");
        if (!this.mapFolder.exists()) {
            if (!this.mapFolder.mkdir()) {
                Bukkit.getConsoleSender().sendMessage(PREFIX + "Could not create mapsBackup folder! Check permissions!");
            }
        }

        if (!this.fileManager.is("worlds")) {
            this.fileManager.set("worlds", Collections.singletonList("changeme"));
        }
        if (!this.fileManager.is("kickPlayers")) {
            this.fileManager.set("kickPlayers", false);
        }
        if (!this.fileManager.is("kickReason")) {
            this.fileManager.set("kickReason", "&b[World unloaded]");
        }
        if (!this.fileManager.is("teleportMessage")) {
            this.fileManager.set("teleportMessage", "&b[World unloaded]");
        }
        if (!this.fileManager.is("forceBackup")) {
            this.fileManager.set("forceBackup", false);
        }

        boolean forceBackup = this.fileManager.getBoolean("forceBackup");

        this.worldUtils = new WorldUtils(this.fileManager.getBoolean("kickPlayers"),
                this.fileManager.getString("kickReason").replace('&', '§'),
                this.fileManager.getString("teleportMessage").replace('&', '§'));

        int loaded = 0;
        for (String s : this.fileManager.getStringList("worlds")) {
            File world = new File(Bukkit.getWorldContainer(), s);
            File backupExisting = new File(this.mapFolder, s);
            if (!forceBackup && backupExisting.exists()) {
                Bukkit.getConsoleSender().sendMessage(PREFIX + "Found a backup for world §b" + s + "§7! If you want to backup the map every time set §bforceBackup §7to true");
            } else {
                try {
                    FileUtils.copyDirectoryToDirectory(world, this.mapFolder);
                    loaded++;
                } catch (IOException e) {
                    Bukkit.getConsoleSender().sendMessage(PREFIX + "World §b" + s + " §7couldn't be saved. You deleted the mapsBackup folder or the given world does not exist.");
                }
            }

            this.worldUtils.rollback(s);
        }
        this.getServer().getPluginManager().registerEvents(this, this);
        if (loaded > 0)
            Bukkit.getConsoleSender().sendMessage(PREFIX + "Successfully saved §b" + loaded + " §7world" + (loaded > 1 ? "s" : "") + "!");
    }

    @Override
    public void onDisable() {
        int restored = 0;
        for (String s : this.fileManager.getStringList("worlds")) {
            if (Bukkit.getWorld(s) != null) {
                File world = new File(this.mapFolder, s);
                if (world.exists()) {
                    try {
                        FileUtils.copyDirectoryToDirectory(world, Bukkit.getWorldContainer());
                        restored++;
                    } catch (IOException e) {
                        Bukkit.getConsoleSender().sendMessage(PREFIX + "World §b" + s + " §7couldn't be restored.");
                    }
                } else {
                    Bukkit.getConsoleSender().sendMessage(PREFIX + "No save found for world §b" + s + "§7! Please restart the server once.");
                }
                this.worldUtils.unloadMap(s);
            } else {
                Bukkit.getConsoleSender().sendMessage(PREFIX + "World §b" + s + " §7does not exist.");
            }
        }
        if (restored > 0)
            Bukkit.getConsoleSender().sendMessage(PREFIX + "Successfully restored §b" + restored + " §7world" + (restored > 1 ? "s" : "") + "!");
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void on(ServerCommandEvent e) {
        if (e.getCommand().toLowerCase().startsWith("easymapreset")) {
            e.setCancelled(true);
            e.getSender().sendMessage(PREFIX + "This server is running §bEasyMapReset §7version §b" +
                    this.getDescription().getVersion() + " §7by §b" + this.getDescription().getAuthors().get(0));
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void on(PlayerCommandPreprocessEvent e) {
        if (e.getMessage().toLowerCase().startsWith("/easymapreset")) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(PREFIX + "This server is running §bEasyMapReset §7version §b" +
                    this.getDescription().getVersion() + " §7by §b" + this.getDescription().getAuthors().get(0));
        }
    }

}
