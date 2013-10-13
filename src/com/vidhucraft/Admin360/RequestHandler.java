package com.vidhucraft.Admin360;

import java.util.List;
import java.util.Queue;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.vidhucraft.Admin360.entities.Admin;
import com.vidhucraft.Admin360.entities.Request;
import com.vidhucraft.Admin360.entities.User;

/**
 * Static functions to handle player requests
 * @author Vidhu
 *
 */
public class RequestHandler {

	
	public static void addPlayerInQueue(String name){
		addPlayerInQueue(name, null);
	}
	
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
		Request request = requests.peek();
		Player admin = Bukkit.getPlayer(staff.getAdminName());
		Player user = Bukkit.getPlayer(request.getPlayerName());
		
		//Teleport admin to player
		admin.teleport(user);
		
		//Set request to attended
		request.setAttended(true);
		
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
	public static void attemptCloseRequest(Request request){
		//TODO:Ask player if help provided was satisfactory
		//TODO:Set request as closed
		//TODO:Move request to Completed request queue
		//TODO:Set automated message every 10 seconds asking for help rating
	}
	
	
	public static void playerRequestRating(String ans){
		//TODO:Give admin honor point based on ans
		//TODO:Remove request from completed request list
	}
}
