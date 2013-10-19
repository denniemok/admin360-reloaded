package com.vidhucraft.Admin360.entities;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Sound;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.util.Vector;

import com.vidhucraft.Admin360.Admin360;

/**
 * This class represents the Admins in the server. The static member
 * <pre>adminsOnline</pre> lists all the admins online on the server at the moment
 * When an admin joins the server, an instance of this class is created
 * and is added the the <pre>adminsOnline</pre> List
 * @author Vidhu
 *
 */
public class Admin{
	//Static members
	/**
	 * A list containing all the admins online in the server
	 */
	public static HashMap<String, Admin> adminsOnline = new HashMap<String, Admin>();
	
	
	//non Static members
	private String adminName;

	/**
	 * Creates a new admin object
	 * @param name String Admin's name
	 */
	public Admin(String name){
		this.adminName = name;
	}
	
	/**
	 * @return String Admin's name
	 */
	public String getAdminName() {
		return adminName;
	}

	/**
	 * Sets the admin's name
	 * @param adminName Admin's name
	 * @throws Exception Thrown if admin's name is null
	 */
	public void setAdminName(String adminName) throws Exception {
		if(adminName.equals(""))
			throw new Exception("Admin name cannot be empty");
		this.adminName = adminName;
	}
	
	/**
	 * Sends a message to the admin
	 * @param message <b>String</b> message
	 */
	public void sendMessage(String message){
		Bukkit.getPlayer(this.adminName).sendMessage(ChatColor.GREEN + message);
	}
	
	/**
	 * Gives an admin an honor
	 * @param request The request the admin is getting honored for
	 */
	public void giveHonor(Request request){
		Admin360.ds.addAdminHonor(request);
		User.messagePlayer(this.adminName, "You have recieved an honor point!");
		
		//Fireworks test
		Player pl = Bukkit.getPlayer(this.adminName);	
		Firework fw = pl.getWorld().spawn(pl.getLocation(), Firework.class);
		FireworkMeta fwm = fw.getFireworkMeta();
		FireworkEffect effect = FireworkEffect.builder().trail(true).withColor(Color.LIME).with(Type.CREEPER).build();
		fwm.addEffect(effect);
		fwm.setPower(1);
		fw.setFireworkMeta(fwm);
		
		//Sound Test
		pl.getWorld().playSound(pl.getLocation(), Sound.LEVEL_UP, 1, 1);
	}

	public static void messageAdmins(String msg){
		for (Map.Entry<String, Admin> entry : adminsOnline.entrySet()){
			User.messagePlayer(entry.getKey(), 
					ChatColor.DARK_GREEN + "[Admin360]" + ChatColor.GREEN + msg);
		}
	}
}
