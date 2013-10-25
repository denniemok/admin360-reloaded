package com.vidhucraft.Admin360.entities;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class User {
	
	/**
	 * Sends a message to all players in the server.
	 * The message is preformated in this function so any message to
	 * be sent can be done simply like this: <code>messagePlayers("hello world")</code>
	 * @param msg <b>String</b> message to be sent
	 */
	public static void messagePlayers(String msg){
		Bukkit.broadcastMessage(ChatColor.DARK_GREEN + "[Admin360] " + ChatColor.GREEN + msg);
	}
	
	/**
	 * Sends a message to the specified player with some preformating done
	 * @param playerName
	 * @param msg
	 */
	public static void messagePlayer(String playerName, String msg){
		//Player giving null pointer exception when the reminger is schedual and is running
		Player player = Bukkit.getPlayer(playerName);
		player.sendMessage(ChatColor.GREEN + msg);
	}
	
	/**
	 * Sends a message to the specified Command sender (console/player) 
	 * with some preformating done
	 * @param commandsender
	 * @param msg
	 */
	public static void messagePlayer(CommandSender commandsender, String msg){
		commandsender.sendMessage(ChatColor.GREEN + msg);
	}
}
