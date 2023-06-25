package com.battleasya.Admin360.commands;

import com.battleasya.Admin360.Admin360;
import com.battleasya.Admin360.util.Permissions;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class B3 implements CommandExecutor {

    private final Admin360 plugin;

    public B3(Admin360 plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length == 0) {
            printInfo(sender);
            return true;
        }

        if (args.length == 1) {

            if (args[0].equalsIgnoreCase("help")) {
                for (String commands_player : plugin.getConfig().getStringList("command-list-player")) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', commands_player));
                }
                if (Permissions.hasPermission(sender, Permissions.RESPOND_TICKET, false)) {
                    for (String commands_staff : plugin.getConfig().getStringList("command-list-staff")) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', commands_staff));
                    }
                }
                return true;
            }

            if (args[0].equalsIgnoreCase("reload")) {
                if (Permissions.hasPermission(sender, Permissions.RELOAD_CONFIG, true)) {
                    plugin.reloadConfig();
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            plugin.getConfig().getString("reload-message")));
                }
                return true;
            }

        }

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("incorrect-syntax")));
        return true;

    }

    public static void printInfo(CommandSender sender) {
        sender.sendMessage("");
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f[&6ADMIN360&f] Version: &78.1.2"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f[&6ADMIN360&f] Authors: &7Dennie, vidhu1911"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f[&6ADMIN360&f] Project: &7https://cutt.ly/bjo1zpy"));
        sender.sendMessage("");
    }

}
