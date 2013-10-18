package com.vidhucraft.Admin360.entities;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

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
	public static HashMap<String, Request> requestInProgresss = new HashMap<String, Request>();
	
	/**
	 * Collection of request that are closed by the admin but are awaiting use response
	 * of /a3 yes or /a3 no
	 * Key: name of player
	 * Value: Request object
	 */
	public static HashMap<String, Request> requestCompleted = new HashMap<String, Request>();
	
	
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

}
