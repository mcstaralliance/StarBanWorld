package com.mcstaralliance.starbanworld.command;

import com.mcstaralliance.starbanworld.StarBanWorld;
import com.mcstaralliance.starbanworld.manager.ConfigManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandHandler implements CommandExecutor{
    protected final ConfigManager configManager;
    
    public CommandHandler(StarBanWorld plugin) {
        this.configManager = plugin.getConfigManager();
    }
    
    protected boolean checkPermission(CommandSender sender, String permission) {
        if (!sender.hasPermission(permission)) {
            sender.sendMessage(ChatColor.RED + "你没有权限执行此命令！");
            return false;
        }
        return true;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "add": return handleAdd(sender, args);
            case "remove": return handleRemove(sender, args);
            case "list": return handleList(sender);
            case "tp": return handleTeleport(sender, args);
            case "info": return handleInfo(sender, args);
            case "reload": return handleReload(sender);
            default: sendHelp(sender);
        }
        return true;
    }

    private boolean handleAdd(CommandSender sender, String[] args) {
        if (!checkPermission(sender, "starbanworld.admin")) {
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "用法: /sbw add <世界名>");
            return true;
        }

        String worldName = args[1];
        World world = Bukkit.getWorld(worldName);

        if (world == null) {
            sender.sendMessage(ChatColor.RED + "世界 " + worldName + " 不存在！");
            return true;
        }

        List<String> whitelist = configManager.getWhitelist();

        if (whitelist.contains(worldName)) {
            sender.sendMessage(ChatColor.RED + "世界 " + worldName + " 已经在白名单中！");
            return true;
        }

        whitelist.add(worldName);
        configManager.updateWhitelist(whitelist);

        sender.sendMessage(ChatColor.GREEN + "已将世界 " + worldName + " 添加到白名单！");
        return true;
    }

    private boolean handleRemove(CommandSender sender, String[] args) {
        if (!checkPermission(sender, "starbanworld.admin")) {
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "用法: /sbw remove <世界名>");
            return true;
        }

        String worldName = args[1];
        List<String> whitelist = configManager.getWhitelist();

        if (!whitelist.contains(worldName)) {
            sender.sendMessage(ChatColor.RED + "世界 " + worldName + " 不在白名单中！");
            return true;
        }

        whitelist.remove(worldName);
        configManager.updateWhitelist(whitelist);

        sender.sendMessage(ChatColor.GREEN + "已将世界 " + worldName + " 从白名单中移除！");
        return true;
    }

    private boolean handleList(CommandSender sender) {
        List<String> whitelist = configManager.getWhitelist();

        if (whitelist.isEmpty()) {
            sender.sendMessage(ChatColor.YELLOW + "白名单中没有任何世界！");
            return true;
        }

        sender.sendMessage(ChatColor.GOLD + "==== 白名单世界列表 ====");
        for (String worldName : whitelist) {
            sender.sendMessage(ChatColor.YELLOW + "- " + worldName);
        }
        return true;
    }

    private boolean handleTeleport(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "该命令只能由玩家执行！");
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "用法: /sbw tp <世界名>");
            return true;
        }

        String worldName = args[1];
        World world = Bukkit.getWorld(worldName);

        if (world == null) {
            sender.sendMessage(ChatColor.RED + "世界 " + worldName + " 不存在！");
            return true;
        }

        Player player = (Player) sender;
        player.teleport(world.getSpawnLocation());
        sender.sendMessage(ChatColor.GREEN + "已传送至世界 " + worldName + " ！");
        return true;
    }

    private boolean handleInfo(CommandSender sender, String[] args) {
        World world;

        if (args.length < 2) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "请指定一个世界名！");
                return true;
            }
            world = ((Player) sender).getWorld();
        } else {
            world = Bukkit.getWorld(args[1]);
            if (world == null) {
                sender.sendMessage(ChatColor.RED + "世界 " + args[1] + " 不存在！");
                return true;
            }
        }

        List<String> whitelist = configManager.getWhitelist();
        String worldName = world.getName();

        sender.sendMessage(ChatColor.GOLD + "==== 世界信息 ====");
        sender.sendMessage(ChatColor.YELLOW + "名称: " + worldName);
        sender.sendMessage(ChatColor.YELLOW + "环境: " + world.getEnvironment().name());
        sender.sendMessage(ChatColor.YELLOW + "白名单状态: " + (whitelist.contains(worldName) ? "已启用" : "未启用"));
        return true;
    }

    private boolean handleReload(CommandSender sender) {
        if (!checkPermission(sender, "starbanworld.admin")) {
            return true;
        }

        configManager.reload();
        sender.sendMessage(ChatColor.GREEN + "配置文件已重新加载！");
        return true;
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage(ChatColor.GOLD + "==== StarBanWorld 帮助 ====");
        sender.sendMessage(ChatColor.YELLOW + "/sbw add <世界名>" + ChatColor.WHITE + " - 添加世界到白名单");
        sender.sendMessage(ChatColor.YELLOW + "/sbw remove <世界名>" + ChatColor.WHITE + " - 从白名单移除世界");
        sender.sendMessage(ChatColor.YELLOW + "/sbw list" + ChatColor.WHITE + " - 查看白名单世界列表");
        sender.sendMessage(ChatColor.YELLOW + "/sbw tp <世界名>" + ChatColor.WHITE + " - 传送到指定世界");
        sender.sendMessage(ChatColor.YELLOW + "/sbw info [世界名]" + ChatColor.WHITE + " - 查看世界信息");
        sender.sendMessage(ChatColor.YELLOW + "/sbw reload" + ChatColor.WHITE + " - 重载配置文件");
    }
}