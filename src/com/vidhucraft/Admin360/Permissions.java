package com.vidhucraft.Admin360;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public enum Permissions {
	RespondToRequest ("Admin360.aid");
	
	private String permissionNode;
	private Permissions(String permissionNode){
		this.permissionNode = permissionNode;
	}
	
	public String getNode(){
		return this.permissionNode;
	}
	
	public static boolean hasPermission(Player player, Permissions permission, Boolean sendmsg){
		if(player.isOp())
			return true;
		
		if(!player.hasPermission(permission.toString())){
			if(sendmsg)
				player.sendMessage(ChatColor.RED + "Sorry, you need the permission: " + ChatColor.BLUE + permission);
			return false;
		}
		return true;
	}
}
