package com.vidhucraft.Admin360.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.vidhucraft.Admin360.Permissions;
import com.vidhucraft.Admin360.entities.Admin;
import com.vidhucraft.Admin360.entities.Request;

public class PlayerListener implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent playerjoinevent){
		Player player = playerjoinevent.getPlayer();
		
		//Add player to admin list
		if(Permissions.hasPermission(player, Permissions.RespondToRequest, false)){
			Admin.adminsOnline.put(player.getName(), new Admin(player.getName()));
		}
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent playerquitevent){
		Player player = playerquitevent.getPlayer();
		
		//Remove player from admin list
		if(Permissions.hasPermission(player, Permissions.RespondToRequest, false)){
			Admin.adminsOnline.remove(player.getName());
		}
		
		//TODO:Remove any disconnected player's request from lists and queues
		Request.removePlayerRequest(player.getName());
	}
}
