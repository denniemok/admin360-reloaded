package com.battleasya.Admin360.entities;

import com.battleasya.Admin360.handler.Config;
import com.battleasya.Admin360.handler.Permission;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.UUID;

public class User {

    public static HashMap<UUID, Long> cooldownList = new HashMap<>();

    /* overloading method */
    public static void messagePlayer(CommandSender player, String message) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public static boolean hasPermission(CommandSender sender, Permission permission, Boolean sendMessage){
        if (!sender.hasPermission(permission.getString())) {
            if (sendMessage) {
                messagePlayer(sender, Config.noPermission);
            }
            return false;
        } else {
            return true;
        }
    }

    public static void removePlayer(UUID playerID) {
        cooldownList.remove(playerID);
    }

    public static long inCooldown(UUID playerID, int coolDownTime) {
        if (User.cooldownList.containsKey(playerID)) {
            long secondsLeft = ((cooldownList.get(playerID) / 1000) + coolDownTime) - (System.currentTimeMillis() / 1000);
            // Still cooling down
            if (secondsLeft > 0) { // Still cooling down
                return secondsLeft;
            }
        }
        User.cooldownList.put(playerID, System.currentTimeMillis());
        return -1; // not in cooldown
    }

}