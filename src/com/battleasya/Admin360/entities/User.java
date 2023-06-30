package com.battleasya.Admin360.entities;

import com.battleasya.Admin360.util.Config;
import com.battleasya.Admin360.util.Permission;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class User {

    public static HashMap<String, Long> coolDown = new HashMap<>();

    public static void messagePlayer(String playerName, String msg) {
        try {
            Player player = Bukkit.getPlayer(playerName);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
        } catch (Exception e) { // catch null pointer exception when the reminder is running
            Request.removePlayerRequest(playerName);
        }
        
    }

    // overloading method
    public static void messagePlayer(CommandSender player, String msg) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
    }

    public static boolean hasPermission(CommandSender sender, Permission permission, Boolean sendMsg){
        if (!sender.hasPermission(permission.getString())) {
            if (sendMsg) {
                messagePlayer(sender, Config.noPermission);
            }
            return false;
        } else {
            return true;
        }
    }

}
