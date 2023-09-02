package com.battleasya.Admin360.entities;

import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class Request {

    public static int completedToday = 0;

    /* Queue of all Requests that are pending for progression */
    public static Queue<Request> requestPending = new LinkedList<>();

    /*
     * Collection of Requests that are under progression
     * Key: UUID of Admin
     * Value: Request
     */
    public static HashMap<UUID, Request> requestAttending = new HashMap<>();

    /*
     * Collection of Requests that are awaiting user response
     * Key: UUID of Player
     * Value: Request
     */
    public static HashMap<UUID, Request> requestAwaiting = new HashMap<>();

    /* Collection of Review Reminders which prompt the users to rate the service */
    public static HashMap<UUID, BukkitTask> reviewReminder = new HashMap<>();

    /* Owner of Request */
    private final UUID playerID;
    private final String playerName;

    /* Comment of Request */
    private final String comment;

    /* Epoch time the request was created */
    private final long time;

    /* The admin who is handling this request */
    private UUID adminID;
    private String adminName;

    /* Create a new request */
    public Request(UUID playerID, String playerName, String comment){
        this.playerID = playerID;
        this.playerName = playerName;
        this.comment = comment;
        this.time = System.currentTimeMillis() / 1000;
    }

    /* Gets the player's name who create the request */
    public UUID getPlayerID() {
        return playerID;
    }

    public String getPlayerName() {
        return playerName;
    }

    /* Gets the reason the player set for this request */
    public String getComment() {
        return comment;
    }

    /* Gets the Epoch timestamp this request was created*/
    public Long getTime() {
        return time;
    }

    /* Gets the admin who handled this request */
    public UUID getHandledByID() {
        return adminID;
    }
    public String getHandledByName() {
        return adminName;
    }

    /* Sets the admin who handled this request */
    public void setHandledBy(UUID adminID) {
        this.adminID = adminID;
    }
    public void setHandledBy(String adminName) {
        this.adminName = adminName;
    }


    /*
     * 0: No request present
     * 1: Request is currently queued
     * 2: Request is being honored by admins
     * 3: Admin is already honoring a request
     * 4: Request is completed and awaiting review
     */
    public static int getStatus(UUID playerID){

        // Check in request Queue
        for (Request request : requestPending) {
            if (request.playerID.equals(playerID))
                return 1;
        }

        // Check in requestAttending
        if (requestAttending.containsKey(playerID)) {
            return 3;
        }

        for (Map.Entry<UUID, Request> entry : requestAttending.entrySet()) {
            if (entry.getValue().playerID.equals(playerID)){
                return 2;
            }
        }

        // Check in requestCompleted
        if (requestAwaiting.containsKey(playerID)) {
            return 4;
        }

        // Return 0 if not found
        return 0;

    }

    /* Cancels a request already in queue */
    public static void cancel(UUID playerID){
        //Remove from request Queue
        requestPending.removeIf(request -> request.playerID.equals(playerID));
    }

    /* Removes any requests associated with the specified player */
    public static void removePlayer(UUID playerID){

        // Remove from requestPending
        cancel(playerID);

        // Remove from requestAttending
        for (Map.Entry<UUID, Request> entry : requestAttending.entrySet()) {
            if (entry.getValue().playerID.equals(playerID)){
                requestAttending.remove(entry.getKey());
                break; // prevent concurrent modification error
            }
        }

        // Remove from requestAwaiting
        requestAwaiting.remove(playerID);

        // Remove from reviewReminder
        BukkitTask reminder = reviewReminder.remove(playerID);

        if (reminder != null) {
            reminder.cancel();
        }

    }

    public static void removeAdmin(UUID adminID) {
        requestAttending.remove(adminID);
    }

    /**
     * Gets the player's request position in the queue.
     * If request is not found it return -1
     */
    public static int getPositionInPending(UUID playerID){

        int i = 1;

        for (Request request : requestPending) {
            if (request.playerID.equals(playerID)) {
                return i;
            }
            i++;
        }

        return -1;

    }

    public static boolean isAttending(UUID adminID) {
        return requestAttending.containsKey(adminID);
    }

    public static Request getRequestAttending(UUID adminID) {
        return requestAttending.get(adminID);
    }

    public static boolean isPendingEmpty() {
        return requestPending.isEmpty();
    }

    public static Request getHeadOfPending() {
        return requestPending.poll();
    }

    public static boolean isCompleted(UUID playerID) {
        return requestAwaiting.containsKey(playerID);
    }

    public static void addToPending(Request request) {
        requestPending.add(request);
    }

}
