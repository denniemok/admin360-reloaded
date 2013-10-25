package com.vidhucraft.Admin360;


import java.util.Iterator;
import java.util.Queue;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

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
	 * Sends an error message to player if no admins are available
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
		//Check if admins are available
		if(Admin.adminsOnline.size() == 0){
			User.messagePlayer(name, "Sorry, there are no admin online the the moment");
			return;
		}

		
		//Check if user already has a pending request
		int status = Request.requestStatus(name);
		if(status == 1){
			User.messagePlayer(name, "You already have a request pending");
			return;
		}else if(status == 2){
			User.messagePlayer(name, "An admin is already helping with your current request");
			return;
		}else if(status == 4){
			User.messagePlayer(name, "You have a request which is pending feedback");
			RequestHandler.promtPlayerToRateAdmin(name);
			return;
		}else{
			//Add request into queue
			Request request = new Request(name, reason);
			Request.requestQueue.add(request);		
		}
		
		//Notify user about request
		User.messagePlayer(name, "Please wait for your turn");
		User.messagePlayer(name, "You are currently number " 
				+ ChatColor.RED
				+ Request.requestQueue.size()
				+ ChatColor.GREEN
				+ " in the request queue");
		
		//Notify all admins about new request
		Admin.messageAdmins("A new request has been created by " + ChatColor.RED + name);
		if(reason != null)
			Admin.messageAdmins("Reason: " + ChatColor.RED + reason);
		Admin.messageAdmins("There are now " + ChatColor.RED + Request.requestQueue.size() 
				+ ChatColor.GREEN + " remaining");
	}

	/**
	 * Teleports an admin to the next player in the request queue
	 * @param staff <b>Admin</b> admins to be teleported
	 */
	public static void honorNextRequest(Admin staff){
		//Check if an admin is already honoring a player
		if(Request.requestStatus(staff.getAdminName()) == 3){
			User.messagePlayer(staff.getAdminName(), "You're already attending a player");
			User.messagePlayer(staff.getAdminName(), "Type" + ChatColor.RED + " /a3 close" + ChatColor.GREEN + " to end this request");
			return;
		}
		
		//Get request queue in variable to make it easier to work with
		Queue<Request> requests = Request.requestQueue;
		
		//Sanity Check
		if(requests.peek() == null){
			staff.sendMessage("There arent any request remaining");
			return;
		}
		
		//Get Objects to work with
		Request request = requests.poll();
		Player admin = Bukkit.getPlayer(staff.getAdminName());
		Player user = Bukkit.getPlayer(request.getPlayerName());
		
		//Teleport admin to player
		admin.teleport(user);
		
		//Move request to inprogress list
		Request.requestInProgress.put(staff.getAdminName(), request);
		
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
		//Get request being handled by admin
		Request request = Request.requestInProgress.remove(admin.getAdminName());
		
		//Check if there was a request in progress
		if(request == null){
			User.messagePlayer(admin.getAdminName(), "You arent attending any requests");
			return;
		}		
		
		//Set request as closed
		request.setAttended(true);
		
		//Move request to Completed request queue
		Request.requestCompleted.put(request.getPlayerName(), request);
		
		
		//Message admin that request has been closes
		User.messagePlayer(admin.getAdminName(), "You have closed a request");
		
		//Set automated message every 10 seconds asking for help rating
		BukkitTask reminder = new ReviewReminder(request.getPlayerName()).runReminder();
		Request.reminders.put(request.getPlayerName(), reminder);
	}
	
	/**
	 * Closes a request and disposes it. It also give an admin an honor point based on
	 * what the player rated the help received
	 * @param player
	 * @param isSatisfactory
	 */
	public static void playerRequestRating(Player player, Boolean isSatisfactory){
		//Check if a player has a request in the completed list awaiting a rating
		if(Request.requestStatus(player.getName()) != 4){
			User.messagePlayer(player.getName(), "You have no requests that are awaiting your review");
			return;
		}
		
		//Remove request from completed request list
		Request completedRequest = Request.requestCompleted.remove(player.getName());
		
		//Give admin honor point based on ans
		if(isSatisfactory){
			//prevent trolls admins from giving themselves honor points
			if(!completedRequest.getHandledBy().getAdminName().equalsIgnoreCase(player.getName()))
				completedRequest.getHandledBy().giveHonor(completedRequest);
		}
		
		//Stop the reminders
		Request.reminders.remove(player.getName()).cancel();
		
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
	
	/**
	 * Attempts to cancel a request made my a player
	 * @param playername Player's name
	 * @return boolean True is cancellation was successful
	 */
	public static boolean cancelRequest(String playername){
		int requestStatus = Request.requestStatus(playername);
		switch (requestStatus){
			case 0:
				User.messagePlayer(playername, "You dont have any request currently");
				return false;
			case 1:
				Request.cancel(playername);
				User.messagePlayer(playername, "Your request has been successfuly canceled");
				return true;
			case 3:
				User.messagePlayer(playername, "Its too late to cancel your request. An admin is attending it now");
				return false;
			case 4:
				User.messagePlayer(playername, "Its too late to cancel your request. You have a request awating your review");
				promtPlayerToRateAdmin(playername);
				return false;
		}
		return false;
	}
	
	/**
	 * Queries and tells the player the position/status of his request
	 * @param playername
	 */
	public static void informPlayerRequestStatus(String playername){
		int requestStatus = Request.requestStatus(playername);
		if(requestStatus == 0){
			User.messagePlayer(playername, "You don't have any requests");
		}else if(requestStatus == 1){
			int position = Request.getPlayerRequestPosition(playername);
			User.messagePlayer(playername, "Your request is on position " + ChatColor.RED + position);		
		}else if(requestStatus == 2){
			User.messagePlayer(playername, "An admin is already attending your request right now");
		}else if(requestStatus == 4){
			User.messagePlayer(playername, "Your last request is awaiting a rating from you");
		}
	}
	
	/**
	 * Prompts a player to rate a request after an admin has handled it
	 * @param playername
	 */
	public static void promtPlayerToRateAdmin(String playername){
		User.messagePlayer(playername, "Type:");
		User.messagePlayer(playername, ChatColor.RED + "    /a3 yes" + ChatColor.GREEN + " if helpful");
		User.messagePlayer(playername, ChatColor.RED + "    /a3 no" + ChatColor.GREEN + " if not helpful");
	}
	
	/**
	 * Get all requests count. Group by statuses
	 * Sends the message to the specified player
	 */
	public static void getRequestsCount(String playername){
		int inQueue = Request.requestQueue.size();
		int inProgress = Request.requestInProgress.size();
		int inCompleted = Request.requestCompleted.size();
		
		//Send the message
		User.messagePlayer(playername, "There are:");
		User.messagePlayer(playername, "  " + ChatColor.RED + Integer.toString(inQueue) + ChatColor.BLUE + " requests in queue");
		User.messagePlayer(playername, "  " + ChatColor.RED + Integer.toString(inProgress) + ChatColor.BLUE + " requests in in progress");
		User.messagePlayer(playername, "  " + ChatColor.RED + Integer.toString(inCompleted) + ChatColor.BLUE + " requests awaiting rating");
	}
}
