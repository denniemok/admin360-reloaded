package com.battleasya.Admin360.entities;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class User {

    public static HashMap<String, Long> coolDown = new HashMap<>();

    public static void messagePlayer(String playerName, String msg) {
        //null pointer exception when the reminder is running
        try {
            Player player = Bukkit.getPlayer(playerName);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
        } catch (Exception e) {
            Request.removePlayerRequest(playerName);
        }

        
//        if (player == null) {
//        	Request.removePlayerRequest(playerName);
//        	return;
//        }
//
//        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
        
    }

}
