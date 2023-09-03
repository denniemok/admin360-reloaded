package com.battleasya.Admin360.entities;

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
    public static int getStatus(UUID playerID) {

        // Check in requestAttending
        if (requestAttending.containsKey(playerID)) {
            return 3;
        }

        // Check in requestCompleted
        if (requestAwaiting.containsKey(playerID)) {
            return 4;
        }

        // Check in request Queue
        for (Request request : requestPending) {
            if (request.playerID.equals(playerID))
                return 1;
        }

        for (Map.Entry<UUID, Request> entry : requestAttending.entrySet()) {
            if (entry.getValue().playerID.equals(playerID)) {
                return 2;
            }
        }

        // Return 0 if not found
        return 0;

    }

    /* Removes any requests associated with the specified player */
    public static void removePlayer(UUID playerID){

        // Remove from requestPending
        removeFromPending(playerID);

        // Remove from requestAttending
        for (Map.Entry<UUID, Request> entry : requestAttending.entrySet()) {
            if (entry.getValue().playerID.equals(playerID)){
                requestAttending.remove(entry.getKey());
                break; // prevent concurrent modification error
            }
        }

        // Remove from requestAwaiting
        requestAwaiting.remove(playerID);

    }

    public static void removeAdmin(UUID adminID) {
        requestAttending.remove(adminID);
    }

    public static boolean inPending(UUID playerID) {
        for (Request request : requestPending) {
            if (request.getPlayerID().equals(playerID))
                return true;
        }
        return false;
    }

    public static boolean inAttending(UUID adminID) {
        return requestAttending.containsKey(adminID);
    }

    public static boolean inAwaiting(UUID playerID) {
        return requestAwaiting.containsKey(playerID);
    }

    public static Request getRequestPending(UUID playerID) {

        for (Request request : requestPending) {
            if (request.getPlayerID().equals(playerID)) {
                return request;
            }
        }

        return null;

    }

    public static Request getRequestAttending(UUID adminID) {
        return requestAttending.get(adminID);
    }

    public static boolean isPendingEmpty() {
        return requestPending.isEmpty();
    }

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

    public static int getPendingSize() {
        return requestPending.size();
    }

    public static int getAttendingSize() {
        return requestAttending.size();
    }

    public static int getAwaitingSize() {
        return requestAwaiting.size();
    }

    public static void addToPending(Request request) {
        requestPending.add(request);
    }

    public static Request getHeadOfPending() {
        return requestPending.poll();
    }

    public static void removeFromPending(UUID playerID){
        //Remove from request Queue
        requestPending.removeIf(request -> request.playerID.equals(playerID));
    }

    public static void removeFromPending(Request request) {
        requestPending.remove(request);
    }

    public static void clearPendingList() {
        requestPending.clear();
    }

    public static void clearAttendingList() {
        requestAttending.clear();
    }

    public static void clearAwaitingList() {
        requestAwaiting.clear();
    }

    public static void addToAttending(UUID adminID, Request request) {
        requestAttending.put(adminID, request);
    }

    public static Request removeFromAttending(UUID adminID) {
        return requestAttending.remove(adminID);
    }

    public static void addToAwaiting(UUID playerID, Request request) {
        requestAwaiting.put(playerID, request);
    }

    public static Request removeFromAwaiting(UUID playerID) {
        return requestAwaiting.remove(playerID);
    }

    public static int getCompletedToday() {
        return completedToday;
    }

    public static void addCompletedToday() {
        completedToday += 1;
    }

}
