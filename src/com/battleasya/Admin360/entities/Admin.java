package com.battleasya.Admin360.entities;

import com.battleasya.Admin360.util.Permission;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;

public class Admin {

    /* A list containing all the admins online */
    public static ArrayList<String> adminsOnline = new ArrayList<>();

    /* Send a message to all the admins online */
    public static void messageAdmins(String msg) {
        for (String s : adminsOnline) {
            Player player = Bukkit.getPlayer(s);
            if (player == null) {
                Request.removePlayerRequest(s);
                continue;
            }
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
        }
    }

    /* Refresh all the admins in the list */
    public static void refreshAdminList() {
        Collection<? extends Player> playerArray = Bukkit.getOnlinePlayers();
        for (Player player : playerArray) {
            if (player.hasPermission(Permission.RESPOND_TICKET.getString())) {
                adminsOnline.add(player.getName());
            }
        }
    }

}
