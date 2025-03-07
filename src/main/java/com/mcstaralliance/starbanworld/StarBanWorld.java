package com.mcstaralliance.starbanworld;

import com.mcstaralliance.starbanworld.command.CommandHandler;
import com.mcstaralliance.starbanworld.listener.WorldAccessListener;
import com.mcstaralliance.starbanworld.manager.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class StarBanWorld extends JavaPlugin implements Listener {
    private ConfigManager configManager;

    @Override
    public void onEnable() {
        // 初始化配置管理器
        this.configManager = new ConfigManager(this);
        loadConfig();
        
        // 注册事件监听器
        Bukkit.getPluginManager().registerEvents(new WorldAccessListener(this), this);
        
        // 注册命令处理器
        getCommand("starbanworld").setExecutor(new CommandHandler(this));
        
        getLogger().info("StarBanWorld 插件已启用！");
    }
    
    @Override
    public void onDisable() {
        getLogger().info("StarBanWorld 插件已禁用！");
    }
    
    public ConfigManager getConfigManager() {
        return configManager;
    }
    
    /**
     * 加载配置文件
     */
    private String noPermissionMessage;

    private void loadConfig() {
        // 如果配置文件中没有whitelist节点，创建一个空列表
        if (!getConfig().contains("whitelist")) {
            getConfig().set("whitelist", new ArrayList<String>());
        }
        
        // 加载自定义消息
        this.noPermissionMessage = ChatColor.translateAlternateColorCodes('&', 
                getConfig().getString("messages.no-permission", "&c你没有前往 %world% 世界的权限。"));
        
        // 保存配置
        saveConfig();
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        checkWorldAccess(event, event.getPlayer(), event.getTo());
    }

    @EventHandler
    public void onPlayerPortal(PlayerPortalEvent event) {
        checkWorldAccess(event, event.getPlayer(), event.getTo());
    }
    
    /**
     * 检查玩家是否有权限前往目标世界
     * 
     * @param event 传送事件
     * @param player 玩家
     * @param location 目标位置
     */
    private void checkWorldAccess(PlayerTeleportEvent event, Player player, Location location) {
        // 安全检查
        if (location == null || location.getWorld() == null) {
            return;
        }
        
        List<String> whitelist = getConfig().getStringList("whitelist");
        String worldName = location.getWorld().getName();
        
        // 检查玩家是否有权限前往该世界
        if (!whitelist.contains(worldName) && !player.hasPermission("starbanworld.world." + worldName)) {
            String message = noPermissionMessage.replace("%world%", worldName);
            player.sendMessage(message);
            event.setCancelled(true);
        }
    }
}
