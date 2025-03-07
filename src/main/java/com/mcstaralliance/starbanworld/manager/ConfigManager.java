package com.mcstaralliance.starbanworld.manager;

import com.mcstaralliance.starbanworld.StarBanWorld;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class ConfigManager {
    private final StarBanWorld plugin;
    private String noPermissionMessage;
    private List<String> whitelist;

    public ConfigManager(StarBanWorld plugin) {
        this.plugin = plugin;
        loadConfig();
    }

    public void loadConfig() {
        plugin.saveDefaultConfig();
        FileConfiguration config = plugin.getConfig();
        
        // 初始化白名单
        if (!config.contains("whitelist")) {
            config.set("whitelist", new ArrayList<String>());
        }
        this.whitelist = config.getStringList("whitelist");
        
        // 加载消息配置
        this.noPermissionMessage = ChatColor.translateAlternateColorCodes('&',
                config.getString("messages.no-permission", "&c你没有前往 %world% 世界的权限。"));
        
        plugin.saveConfig();
    }

    public void reload() {
        plugin.reloadConfig();
        loadConfig();
    }

    public List<String> getWhitelist() {
        return new ArrayList<>(whitelist);
    }

    public void updateWhitelist(List<String> newList) {
        this.whitelist = new ArrayList<>(newList);
        plugin.getConfig().set("whitelist", whitelist);
        plugin.saveConfig();
    }

    public String getNoPermissionMessage() {
        return noPermissionMessage;
    }
}