package com.battleasya.Admin360.entities;

import com.battleasya.Admin360.Admin360;
import com.battleasya.Admin360.handler.Config;
import com.battleasya.Admin360.handler.Permission;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class User {

    public static HashMap<UUID, Long> cooldownList = new HashMap<>();

    /* overloading method */
    public static void messagePlayer(CommandSender player, String message) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public static boolean hasPermission(CommandSender sender, Permission permission, Boolean sendMessage) {
        if (sender.hasPermission(permission.getString())) {
            return true;
        } else {
            if (sendMessage) {
                messagePlayer(sender, Config.noPermission);
            }
            return false;
        }
    }

    public static void removePlayer(UUID playerID) {
        cooldownList.remove(playerID);
    }

    public static long inCooldown(UUID playerID, int duration, Admin360 plugin) {

        if (User.cooldownList.containsKey(playerID)) {

            long secondsLeft = ((cooldownList.get(playerID) / 1000) + duration) - (System.currentTimeMillis() / 1000);

            // Still cooling down
            if (secondsLeft > 0) { // still in cool down
                return secondsLeft;
            }

        }

        cooldownList.put(playerID, System.currentTimeMillis()); // put in list

        (new BukkitRunnable() {
            public void run() {
                cooldownList.remove(playerID);
            }
        }).runTaskLaterAsynchronously(plugin, 20L * duration);

        return -1; // not in cooldown

    }

}