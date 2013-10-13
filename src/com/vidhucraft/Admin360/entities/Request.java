package com.vidhucraft.Admin360.entities;

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
	
	//non-Static members
	private String playerName;
	private String reason;
	private boolean isAttended;
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
		this.time = System.currentTimeMillis();
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

	public boolean isAttended() {
		return isAttended;
	}

	public void setAttended(boolean isAttended) {
		this.isAttended = isAttended;
	}

}
