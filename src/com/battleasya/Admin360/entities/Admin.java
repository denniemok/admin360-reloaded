package com.battleasya.Admin360.entities;

import com.battleasya.Admin360.util.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;

public class Admin {

    /**
     * A list containing all the admins online in the server
     */
    public static ArrayList<String> adminsOnline = new ArrayList<>();

    /**
     * Send a message to all admins
     */
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

    /**
     * Reloads all the admins in the admin list
     */
    public static void refreshAdminList() {
        Collection<? extends Player> playerArray = Bukkit.getOnlinePlayers();
        for (Player player : playerArray) {
            if (player.hasPermission(Permissions.RESPOND_TICKET.getNode())) {
                adminsOnline.add(player.getName());
            }
        }
    }

}
