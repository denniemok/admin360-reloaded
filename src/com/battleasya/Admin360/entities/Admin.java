package com.battleasya.Admin360.entities;

import com.battleasya.Admin360.handler.Permission;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

public class Admin {

    /* List of admins online */
    public static HashSet<UUID> adminList = new HashSet<>();

    /* Send a message to all the admins online */
    public static void messageAdmins(String message) {

        for (UUID adminID : adminList) {

            Player admin = Bukkit.getPlayer(adminID);

            if (admin == null) {
                Request.removeAdmin(adminID);
                Admin.removeAdmin(adminID);
            } else {
                User.messagePlayer(admin, message);
            }

        }
    }

    /* Refresh all the admins in the list */
    public static void refreshList() {
        Collection<? extends Player> playerList = Bukkit.getOnlinePlayers();
        for (Player player : playerList) {
            if (User.hasPermission(player, Permission.RESPOND_TICKET, false)) {
                Admin.addAdmin(player.getUniqueId());
            }
        }
    }

    public static void removeAdmin(UUID adminID) {
        adminList.remove(adminID);
    }

    public static void addAdmin(UUID adminID) {
        adminList.add(adminID);
    }

    public static boolean isListEmpty() {
        return adminList.isEmpty();
    }

    public static boolean isAdmin(UUID adminID) {
        return adminList.contains(adminID);
    }

}
