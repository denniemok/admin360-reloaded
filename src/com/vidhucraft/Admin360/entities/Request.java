package com.vidhucraft.Admin360.entities;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import org.bukkit.scheduler.BukkitTask;

import com.vidhucraft.Admin360.ReviewReminder;

/**
 * This class represents the requests created by users. 
 * When a request is created, an instance of this class is created
 * along with the necessary information and is then queued in the 
 * request queue.
 * @author Vidhu
 */
public class Request {
	/**
	 * The queue of all requests that are made and still pending
	 */
	public static Queue<Request> requestQueue = new LinkedList<Request>();
	
	/**
	 * Collection of requests that are being handled by an admin at the moment
	 * Key: name of admin
	 * Value: Request object
	 */
	public static HashMap<String, Request> requestInProgress = new HashMap<String, Request>();
	
	/**
	 * Collection of request that are closed by the admin but are awaiting use response
	 * of /a3 yes or /a3 no
	 * Key: name of player
	 * Value: Request object
	 */
	public static HashMap<String, Request> requestCompleted = new HashMap<String, Request>();
	
	/**
	 * A collection of ReviewReminers which prompt the users to rates an admins help by doing
	 * (/a3 yes) or (/a3 no)
	 */
	public static HashMap<String, BukkitTask> reminders = new HashMap<String, BukkitTask>();
	
	//non-Static members
	/**
	 * The player's name who created this request
	 */
	private String playerName;
	
	/**
	 * The player's specified reason for this request
	 */
	private String reason;
	
	/**
	 * If this request has been attended
	 */
	private boolean isAttended;
	
	/**
	 * The admin who is handling this request
	 */
	private Admin handledBy;
	
	/**
	 * The epoch time the request was created
	 */
	private long time;
	
	/**
	 * Create a new request
	 * @param playername String player's name
	 */
	public Request(String playername){
		new Request(playername, null);
	}
	
	/**
	 * Create a new request
	 * @param playername String player's name
	 * @param reason String request's reason
	 */
	public Request(String playername, String reason){
		this.playerName = playername;
		this.reason = reason;
		this.isAttended = false;
		this.time = System.currentTimeMillis()/1000;
	}
	
	/**
	 * Gets the player's name who create the request
	 * @return
	 */
	public String getPlayerName() {
		return playerName;
	}

	/**
	 * Gets the reason the player set for this request
	 * @return
	 */
	public String getReason() {
		return reason;
	}
	
	/**
	 * Gets the Epoch timestame this request was created
	 * @return
	 */
	public Long getTime() {
		return time;
	}

	/**
	 * Gets if this request has been handles
	 * @return <b>bool</b> TRUE if this request has been handled or not
	 */
	public boolean isAttended() {
		return isAttended;
	}

	/**
	 * Sets whether this request has been handled or not
	 * @param isAttended <b>bool</b> TRUE if request has been handled
	 */
	public void setAttended(boolean isAttended) {
		this.isAttended = isAttended;
	}

	/**
	 * Gets the admin who handled this request
	 * @return <b>Admin</b>
	 */
	public Admin getHandledBy() {
		return handledBy;
	}

	/**
	 * Sets the admin who handled this request
	 * @param handledBy <b>Admin</b> admin who handled this request
	 */
	public void setHandledBy(Admin handledBy) {
		this.handledBy = handledBy;
	}
	
	/**
	 * Cancels a request already in queue
	 */
	public static void cancel(String playername){
		//Remove from request Queue
		Iterator<Request> it = requestQueue.iterator();
		while(it.hasNext()){
			Request request = it.next();
			if(request.playerName.equals(playername)){
				requestQueue.remove(request);
			}		
		}
	}

	/**
	 * Returns a int depending on request status<br/>
	 * 0: No request present<br/>
	 * 1: Request is currently queued<br/>
	 * 2: Request is being honored by admins<br/>
	 * 3: Admin is already honoring a request<br/>
	 * 4: Request is completed and awaiting review<br/>
	 * @return int - Status of any request that exists
	 */
	public static int requestStatus(String playerName){

		//Check in request Queue
		Iterator<Request> it = requestQueue.iterator();
		while(it.hasNext()){
			if(it.next().playerName.equals(playerName))
				return 1;
		}
		
		
		//Check in requestInProgress
		for (Map.Entry<String, Request> entry : requestInProgress.entrySet())
		{
			if(entry.getKey().equals(playerName)){
		    	return 3;
		    }
		    if(entry.getValue().playerName.equals(playerName)){
		    	return 2;
		    } 
		}
		
		//Check in requestCompleted
		for (Map.Entry<String, Request> entry : requestCompleted.entrySet())
		{
		    if(entry.getKey().equals(playerName)){
		    	return 4;
		    }
		}
		
		//Return 0 if player not found
		return 0;
	}
	
	/**
	 * Removes any requests associated with the specified player
	 * @param playername
	 */
	public static void removePlayerRequest(String playername){
		//Remove from request Queue
		Iterator<Request> it = requestQueue.iterator();
		while(it.hasNext()){
			Request request = it.next();
			if(request.playerName.equals(playername)){
				requestQueue.remove(request);
			}		
		}
		
		//Remove from requestInProgress
		for (Map.Entry<String, Request> entry : requestInProgress.entrySet())
		{
		    if(entry.getValue().playerName.equals(playername)){
		    	requestInProgress.remove(entry.getKey());
		    }
		}
		
		//Remove from requestCompleted
		requestCompleted.remove(playername);
		
		//Remove any reminders
		BukkitTask reminder = reminders.remove(playername);
		if(reminder != null)
			reminder.cancel();
	}
	
	/**
	 * Gets the player's request postion in the queue.
	 * If request is not found it return -1
	 * @param playername
	 * @return int - Request's position
	 */
	public static int getPlayerRequestPosition(String playername){
		int i = 1;
		
		Iterator<Request> it = requestQueue.iterator();
		
		while(it.hasNext()){
			Request request = it.next();
			if(request.playerName.equals(playername)){
				return i;
			}
			i++;
		}
		
		return -1;
	}

}
