package com.mcstaralliance.starbanworld;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class StarBanWorld extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        List<String> list = getConfig().getStringList("white-list");
        Player player = event.getPlayer();
        String worldName = event.getTo().getWorld().getName();
        if (worldName.equalsIgnoreCase(player.getName())) {
            return;
        }

        if (list.contains(worldName)) {
            if (!player.hasPermission("world." + worldName)) {
                String message = ChatColor.RED + "你没有前往 " + worldName + " 世界的权限";
                player.sendMessage(message);
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerPortal(PlayerPortalEvent event) {
        List<String> list = getConfig().getStringList("white-list");
        Player player = event.getPlayer();
        String worldName = event.getTo().getWorld().getName();
        if (worldName.equalsIgnoreCase(player.getName())) {
            return;
        }

        if (list.contains(worldName)) {
            if (!player.hasPermission("world." + worldName)) {
                String message = ChatColor.RED + "你没有前往 " + worldName + " 世界的权限";
                player.sendMessage(message);
                event.setCancelled(true);
            }
        }
    }
}
