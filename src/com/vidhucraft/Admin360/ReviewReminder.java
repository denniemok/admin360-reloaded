package com.vidhucraft.Admin360;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.vidhucraft.Admin360.entities.User;

public class ReviewReminder extends BukkitRunnable{
	private Admin360 plugin;
	private String username;
	private boolean hasRan = false;
	private boolean showReminder = true;
	
	/**
	 * Creates a new reminder for the specified player name
	 * @param username
	 */
	public ReviewReminder(String username){
		this.username = username;
		plugin = (Admin360) Bukkit.getPluginManager().getPlugin("Admin360");
		this.showReminder = plugin.getConfig().getBoolean("showReminder");
	}
	
	/** 
	 * Schedules the reminder. Player starts receiving messages after executing this
	 * @return BukkitTask
	 */
	public BukkitTask runReminder(){
		int frequency = plugin.getConfig().getInt("reminderFrequency")*20;
		return this.runTaskTimer(plugin, 20, frequency);
	}
	
	@Override
	public void run() {		
		//Prompt user
		User.messagePlayer(this.username, "Your request is now closed. Was the admin helpful?");
		RequestHandler.promtPlayerToRateAdmin(this.username);
		
		//marks that the user has been reminded atleast once
		this.hasRan = true;
		
		//Check if user should be reminded
		if(!showReminder && hasRan)
			this.cancel();
		
	}

}
