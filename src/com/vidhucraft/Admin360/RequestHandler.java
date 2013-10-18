package com.vidhucraft.Admin360;


import java.util.Queue;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.vidhucraft.Admin360.datasource.DataSource;
import com.vidhucraft.Admin360.datasource.SQLite_DataSource;
import com.vidhucraft.Admin360.entities.Admin;
import com.vidhucraft.Admin360.entities.Request;
import com.vidhucraft.Admin360.entities.User;

/**
 * Static functions to handle player requests
 * @author Vidhu
 *
 */
public class RequestHandler {

	/**
	 * Adds a player in the request queue
	 * @param name The player's name
	 */
	public static void addPlayerInQueue(String name){
		addPlayerInQueue(name, null);
	}
	
	/**
	 * Adds a player in the request queue with a reason they specify
	 * @param name The player's name
	 * @param reason The player's reason
	 */
	public static void addPlayerInQueue(String name, String reason){
		Request request = new Request(name, reason);
		Request.requestQueue.add(request);
	}

	/**
	 * Teleports an admin to the next player in the request queue
	 * @param staff <b>Admin</b> admins to be teleported
	 */
	public static void honorNextRequest(Admin staff){
		//Get request queue in variable to make it easier to work with
		Queue<Request> requests = Request.requestQueue;
		
		//Sanity Check
		if(requests.peek() == null){
			staff.sendMessage("There arent any request remaining");
		}
		
		//Get Objects to work with
		Request request = requests.poll();
		Player admin = Bukkit.getPlayer(staff.getAdminName());
		Player user = Bukkit.getPlayer(request.getPlayerName());
		
		//Teleport admin to player
		admin.teleport(user);
		
		//Move request to inprogress list
		Request.requestInProgresss.put(staff.getAdminName(), request);
		
		//Set the admin who honored the request
		request.setHandledBy(staff);
		
		//Message all players
		User.messagePlayers(ChatColor.RED + admin.getDisplayName() + ChatColor.GREEN + " is now attending " + ChatColor.RED + user.getDisplayName());
		User.messagePlayers("There are now " + ChatColor.RED + requests.size() + ChatColor.GREEN + " requests remaining");
	}

	/**
	 * Called when an admin attempt to set a request as solved.
	 * The user is then asked if the help was satisfactory
	 */
	public static void attemptCloseRequest(Admin admin){
		//Get request being handeled by admin
		Request request = Request.requestInProgresss.remove(admin.getAdminName());
		
		//Ask player if help provided was satisfactory
		User.messagePlayer(request.getPlayerName(), "Your request is now closed. Was the admin helpful?");
		User.messagePlayer(request.getPlayerName(), "Type:");
		User.messagePlayer(request.getPlayerName(), ChatColor.RED + "    /a3 yes" + ChatColor.GREEN + " if helpfull");
		User.messagePlayer(request.getPlayerName(), ChatColor.RED + "    /a3 no" + ChatColor.GREEN + " if not helpfull");
		
		//Set request as closed
		request.setAttended(true);
		
		//Move request to Completed request queue
		Request.requestCompleted.put(request.getPlayerName(), request);
		
		//TODO:Set automated message every 10 seconds asking for help rating
	}
	
	/**
	 * Closes a request and disposes it. It also give an admin an honor point based on
	 * what the player rated the help received
	 * @param player
	 * @param isSatisfactory
	 */
	public static void playerRequestRating(Player player, Boolean isSatisfactory){
		//Remove request from completed request list
		Request completedRequest = Request.requestCompleted.remove(player.getName());
		
		//Give admin honor point based on ans
		if(isSatisfactory){
			completedRequest.getHandledBy().giveHonor(completedRequest);
		}
		
		//Send player a message
		User.messagePlayer(completedRequest.getPlayerName(), "Thank you for your feedback!");
	}
	
	/**
	 * Removes all requests from the request queue
	 * NOTE:This doesn't remove any requests that are being handled
	 * by and admin or any requests that are pending a user feedback
	 * @return int - The amount of request purged
	 */
	public static int purgeAllPendingRequests(){
		int ammountPurged = Request.requestQueue.size();
		Request.requestQueue.clear();
		return ammountPurged;
	}
}
