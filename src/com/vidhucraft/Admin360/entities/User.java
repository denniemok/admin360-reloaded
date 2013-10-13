package com.vidhucraft.Admin360.entities;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;


public class User {
	
	/**
	 * Sends a message to all players in the server.
	 * The message is preformated in this function so any message to
	 * be sent can be done simply like this: <code>messagePlayers("hello world")</code>
	 * @param msg <b>String</b> message to be sent
	 */
	public static void messagePlayers(String msg){
		Bukkit.broadcastMessage(ChatColor.GREEN + msg);
	}
}
