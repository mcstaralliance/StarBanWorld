package com.mcstaralliance.starbanworld.listener;

import com.mcstaralliance.starbanworld.StarBanWorld;
import com.mcstaralliance.starbanworld.manager.ConfigManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.List;

public class WorldAccessListener implements Listener {
    private final ConfigManager configManager;

    public WorldAccessListener(StarBanWorld plugin) {
        this.configManager = plugin.getConfigManager();
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        checkWorldAccess(event, event.getPlayer(), event.getTo());
    }

    @EventHandler
    public void onPlayerPortal(PlayerPortalEvent event) {
        checkWorldAccess(event, event.getPlayer(), event.getTo());
    }

    private void checkWorldAccess(PlayerTeleportEvent event, Player player, Location location) {
        if (location == null || location.getWorld() == null) return;

        String worldName = location.getWorld().getName();
        List<String> whitelist = configManager.getWhitelist();

        if (!whitelist.contains(worldName) && !player.hasPermission("starbanworld.world." + worldName)) {
            String message = configManager.getNoPermissionMessage().replace("%world%", worldName);
            player.sendMessage(message);
            event.setCancelled(true);
        }
    }
}