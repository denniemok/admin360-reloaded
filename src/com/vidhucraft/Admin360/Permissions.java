package com.vidhucraft.Admin360;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public enum Permissions {
	RespondToRequest ("Admin360.admin.aid"),
	PurgeRequest ("Admin360.admin.purge"),
	Stats ("Admin360.admin.stats"),
	Help ("Admin360.player.help"),
	Status ("Admin360.player.status"),
	Count ("Admin360.player.Count");
	
	
	private String permissionNode;
	private Permissions(String permissionNode){
		this.permissionNode = permissionNode;
	}
	
	public String getNode(){
		return this.permissionNode;
	}
	
	public static boolean hasPermission(CommandSender player, Permissions permission, Boolean sendmsg){
		if(player.isOp())
			return true;
		
		if(!player.hasPermission(permission.getNode())){
			if(sendmsg)
				player.sendMessage(ChatColor.RED + "Sorry, you need the permission: " + ChatColor.BLUE + permission.getNode());
			return false;
		}
		return true;
	}
}
