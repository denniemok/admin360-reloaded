package com.battleasya.Admin360.entities;

import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class Request {

    public static int completedToday = 0;

    /* The queue of all requests that are made and still pending */
    public static Queue<Request> requestQueue = new LinkedList<>();

    /*
     * Collection of requests that are being handled by an admin at the moment
     * Key: name of admin
     * Value: Request object
     */
    public static HashMap<String, Request> requestInProgress = new HashMap<>();

    /*
     * Collection of request that are closed by the admin but are awaiting user response
     * Key: name of player
     * Value: Request object
     */
    public static HashMap<String, Request> requestCompleted = new HashMap<>();

    /* A collection of Reviews which prompt the users to rates an admins help by doing */
    public static HashMap<String, BukkitTask> reminders = new HashMap<>();

    /* The player's name who created this request */
    private final String playerName;

    /* The player's specified reason for this request */
    private final String reason;

    /* The admin who is handling this request */
    private String handledBy;

    /* The epoch time the request was created */
    private final long time;

    /* Create a new request */
    public Request(String playerName, String reason){
        this.playerName = playerName;
        this.reason = reason;
        this.time = System.currentTimeMillis()/1000;
    }

    /* Gets the player's name who create the request */
    public String getPlayerName() {
        return playerName;
    }

    /* Gets the reason the player set for this request */
    public String getReason() {
        return reason;
    }

    /* Gets the Epoch timestamp this request was created*/
    public Long getTime() {
        return time;
    }

    /* Gets the admin who handled this request */
    public String getHandledBy() {
        return handledBy;
    }

    /* Sets the admin who handled this request */
    public void setHandledBy(String handledBy) {
        this.handledBy = handledBy;
    }

    /* Cancels a request already in queue */
    public static void cancel(String playerName){
        //Remove from request Queue
        requestQueue.removeIf(request -> request.playerName.equals(playerName));
    }


    /*
     * Returns an int depending on request status
     * 0: No request present
     * 1: Request is currently queued
     * 2: Request is being honored by admins
     * 3: Admin is already honoring a request
     * 4: Request is completed and awaiting review
     */
    public static int requestStatus(String playerName){

        //Check in request Queue
        for (Request request : requestQueue) {
            if (request.playerName.equals(playerName))
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

    /* Removes any requests associated with the specified player */
    public static void removePlayerRequest(String playerName){
        //Remove from request Queue
        requestQueue.removeIf(request -> request.playerName.equals(playerName));

        //Remove from requestInProgress
        for (Map.Entry<String, Request> entry : requestInProgress.entrySet())
        {
            if(entry.getValue().playerName.equals(playerName)){
                requestInProgress.remove(entry.getKey());
                break;
                //prevent concurrent modification error
            }
        }

        //Remove from requestCompleted
        requestCompleted.remove(playerName);

        //Remove any reminders
        BukkitTask reminder = reminders.remove(playerName);
        if(reminder != null)
            reminder.cancel();
    }

    /*
     * Gets the player's request position in the queue.
     * If request is not found it return -1
     */
    public static int getPlayerRequestPosition(String playerName){
        int i = 1;

        for (Request request : requestQueue) {
            if (request.playerName.equals(playerName)) {
                return i;
            }
            i++;
        }

        return -1;
    }

}
