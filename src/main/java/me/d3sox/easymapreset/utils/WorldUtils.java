package me.d3sox.easymapreset.utils;

import lombok.AllArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class WorldUtils {

    private boolean kickPlayers;
    private String kickReason;
    private String teleportMessage;

    public void unloadMap(String mapName) {
        World w = Bukkit.getServer().getWorld(mapName);
        for (Entity en : w.getEntities()) {
            if (en instanceof Player) {
                Player p = (Player) en;
                if (this.kickPlayers) {
                    p.kickPlayer(this.kickReason);
                } else {
                    p.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
                    if (this.teleportMessage == null || this.teleportMessage.equals("")) {
                        continue;
                    }
                    if (!StringUtils.isEmpty(this.teleportMessage))
                        p.sendMessage(this.teleportMessage);
                }
            }
        }
        Bukkit.getServer().unloadWorld(w, false);
    }

    private void loadMap(String mapName) {
        World w;
        if (Bukkit.getWorld(mapName) == null) {
            w = Bukkit.getServer().createWorld(new WorldCreator(mapName));
        } else {
            w = Bukkit.getWorld(mapName);
        }
        w.setAutoSave(false);
    }

    public void rollback(String mapName) {
        this.unloadMap(mapName);
        this.loadMap(mapName);
    }
}
