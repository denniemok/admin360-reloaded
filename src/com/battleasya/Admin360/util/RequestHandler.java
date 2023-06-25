package com.battleasya.Admin360.util;

import com.battleasya.Admin360.Admin360;
import com.battleasya.Admin360.entities.Admin;
import com.battleasya.Admin360.entities.Request;
import com.battleasya.Admin360.entities.User;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitTask;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RequestHandler {

    /**
     * Adds a player in the request queue
     * Sends an error message to player if no staff are available
     */

    //public static Admin360 plugin = Admin360.getPlugin(Admin360.class);
    private static final Admin360 plugin = Admin360.getInstance();

    /**
     * Adds a player in the request queue with a reason they specify
     */
    public static void createTicket(String playerName, String details) {


        //Check if staff are available
        if (plugin.getConfig().getBoolean("staff-online-required") && Admin.adminsOnline.size() == 0) {
            User.messagePlayer(playerName, plugin.getConfig().getString("create-failed-no-staff"));
            return;
        }


        //Check if user already has a pending request(status)
        switch (Request.requestStatus(playerName)) {
            case 1:
                User.messagePlayer(playerName, plugin.getConfig().getString("create-failed-in-queue"));
                return;
            case 2:
                User.messagePlayer(playerName, plugin.getConfig().getString("create-failed-in-progress"));
                return;
            case 3:
                //User.messagePlayer(playerName, plugin.getConfig().getString("create-failed-anti-exploit"));
                return;
            case 4:
                User.messagePlayer(playerName, plugin.getConfig().getString("create-failed-require-feedback"));
                return;
            default:
                //Add request into queue
                break;
        }

        // Get number of seconds from wherever you want
        if (plugin.getConfig().getBoolean("use-cooldown")){
            int coolDownTime = plugin.getConfig().getInt("cooldown-timer");
            if(User.coolDown.containsKey(playerName)) {
                long secondsLeft = ((User.coolDown.get(playerName)/1000)+coolDownTime) - (System.currentTimeMillis()/1000);
                if(secondsLeft>0) {
                    // Still cooling down
                    User.messagePlayer(playerName, plugin.getConfig().getString("cooldown-message")
                            .replaceAll("<SECONDSLEFT>", String.valueOf(secondsLeft)));
                    return;
                }
            }
            User.coolDown.put(playerName, System.currentTimeMillis());
        }

        Request request = new Request(playerName, details);
        Request.requestQueue.add(request);

        //Notify user about request
        String positionInQueue = Integer.toString(Request.requestQueue.size());

        for (String created_succeeded : plugin.getConfig().getStringList("create-succeeded")) {
            User.messagePlayer(playerName, created_succeeded.replaceAll("<POSITION>", positionInQueue));
        }

        if (plugin.getConfig().getBoolean("ticket-created-trigger-custom-command")) {
        	Bukkit.dispatchCommand(Bukkit.getConsoleSender(), plugin.getConfig().getString("ticket-created-custom-command")
                    .replaceAll("<PLAYERNAME>", playerName).replaceAll("<POSITION>", positionInQueue));
        }

        for (String ticket_created_notify_staff : plugin.getConfig().getStringList("ticket-created-notify-staff")) {
            Admin.messageAdmins(ticket_created_notify_staff.replaceAll("<PLAYERNAME>", playerName)
                    .replaceAll("<DETAILS>", details).replaceAll("<TICKETSREMAIN>", positionInQueue));
        }

    }


    public static void printQueueList(String adminName) {
        int i = 1;
        for (Request request : Request.requestQueue) {
            User.messagePlayer(adminName, plugin.getConfig().getString("list-message")
                    .replaceAll("<INDEX>", Integer.toString(i))
                    .replaceAll("<PLAYERNAME>", request.getPlayerName())
                    .replaceAll("<DETAILS>", request.getReason())
                    .replaceAll("<TIME>", new SimpleDateFormat("MM/dd/yy HH:mm").format(new Date(request.getTime() * 1000))));
            i++;
        }
    }


    public static void pickTicket(String adminName, String playerName) {
        //Check if a staff is already handling a player, this must go first to avoid problems
        if (Request.requestStatus(adminName) == 3) {
            User.messagePlayer(adminName, plugin.getConfig().getString("next-failed-attending"));
            return;
        }

        //Sanity Check
        if (Request.requestQueue.peek() == null) {
            User.messagePlayer(adminName, plugin.getConfig().getString("next-failed-no-ticket"));
            return;
        }

        //condition check for whether player can be found
        int k = 0;

        for (Request request : Request.requestQueue) {
            if (request.getPlayerName().equals(playerName)) {
                Request.requestQueue.remove(request);
                //Move request to in progress list
                Request.requestInProgress.put(adminName, request);
                //Set the staff who honored the request
                request.setHandledBy(adminName);
                k = 1;
            }
        }

        //cannot find player
        if (k == 0) {
            User.messagePlayer(adminName, plugin.getConfig().getString("next-failed-no-ticket"));
            return;
        }

        Player admin = Bukkit.getPlayer(adminName);
        Player user = Bukkit.getPlayer(playerName);

        //this may be can remove
        if (user == null || admin == null) {
            Request.removePlayerRequest(playerName);
            Request.removePlayerRequest(adminName);
        } else {
            //Teleport staff to player
            if(plugin.getConfig().getBoolean("use-auto-teleport")) {
                try {
                    admin.teleport(user);
                } catch (Exception e) {
                    return;
                }
            }

            //Message to the respective player and staff team
            String tickets = Integer.toString(Request.requestQueue.size());

            for (String ticket_user_notify : plugin.getConfig().getStringList("ticket-in-progress-notify-player")) {
                User.messagePlayer(playerName, ticket_user_notify.replaceAll("<ADMINNAME>", adminName));
            }

            for (String ticket_staff_notify : plugin.getConfig().getStringList("ticket-in-progress-notify-staff")) {
                Admin.messageAdmins(ticket_staff_notify.replaceAll("<ADMINNAME>", adminName)
                        .replaceAll("<PLAYERNAME>", playerName).replaceAll("<TICKETSREMAIN>", tickets));
            }

            if(plugin.getConfig().getBoolean("ticket-in-progress-trigger-custom-command")) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), plugin.getConfig().getString("ticket-in-progress-custom-command")
                        .replaceAll("<PLAYERNAME>", playerName).replaceAll("<ADMINNAME>", adminName));
            }
        }
    }


    public static void nextTicket(String adminName) {

        //Check if a staff is already handling a player, this must go first to avoid problems
        if (Request.requestStatus(adminName) == 3) {
            User.messagePlayer(adminName, plugin.getConfig().getString("next-failed-attending"));
            return;
        }

        //Sanity Check
        if (Request.requestQueue.peek() == null) {
            User.messagePlayer(adminName, plugin.getConfig().getString("next-failed-no-ticket"));
            return;
        }

        //Get Objects to work with
        Request request = Request.requestQueue.poll();

        Player admin = Bukkit.getPlayer(adminName);
        Player user = Bukkit.getPlayer(request.getPlayerName());

        //see if this need to remove
        if (user == null || admin == null) {
            Request.removePlayerRequest(request.getPlayerName());
        	Request.removePlayerRequest(adminName);
        } else {
            //Teleport staff to player
            if(plugin.getConfig().getBoolean("use-auto-teleport")) {
                try {
                    admin.teleport(user);
                } catch (Exception e) {
                    return;
                }
            }


            //Move request to in progress list
            Request.requestInProgress.put(adminName, request);

            //Set the staff who honored the request
            request.setHandledBy(adminName);

            //Message to the respective player and staff team
            String playerName = user.getName();
            String tickets = Integer.toString(Request.requestQueue.size());

            for (String ticket_user_notify : plugin.getConfig().getStringList("ticket-in-progress-notify-player")) {
            	User.messagePlayer(playerName, ticket_user_notify.replaceAll("<ADMINNAME>", adminName));
        	}
            
            for (String ticket_staff_notify : plugin.getConfig().getStringList("ticket-in-progress-notify-staff")) {
            	Admin.messageAdmins(ticket_staff_notify.replaceAll("<ADMINNAME>", adminName)
                        .replaceAll("<PLAYERNAME>", playerName).replaceAll("<TICKETSREMAIN>", tickets));
        	}

            if(plugin.getConfig().getBoolean("ticket-in-progress-trigger-custom-command")) {
            	Bukkit.dispatchCommand(Bukkit.getConsoleSender(), plugin.getConfig().getString("ticket-in-progress-custom-command")
                        .replaceAll("<PLAYERNAME>", playerName).replaceAll("<ADMINNAME>", adminName));
            }
        }

    }


    public static void teleportPlayer(String adminName) {

        //Check if a staff is already handling a player
        if (Request.requestStatus(adminName) == 3) {

            Request request = Request.requestInProgress.get(adminName);

            try {
                Bukkit.getPlayer(adminName).teleport(Bukkit.getPlayer(request.getPlayerName()));
                User.messagePlayer(adminName, plugin.getConfig().getString("teleport-succeeded")
                        .replaceAll("<PLAYERNAME>", request.getPlayerName()));
            } catch (Exception e) {
                //empty catch line suppressing errors
            }

        } else {
            User.messagePlayer(adminName, plugin.getConfig().getString("teleport-failed"));
        }

    }


    public static void printTicketDetails(String adminName) {

        if (Request.requestStatus(adminName) == 3) {

            Request request = Request.requestInProgress.get(adminName);
            String details = request.getReason();
            String playerName = request.getPlayerName();

            String time = new SimpleDateFormat("MM/dd/yy HH:mm").format(new Date(request.getTime()*1000));

            for (String info_message : plugin.getConfig().getStringList("info-message")) {
                User.messagePlayer(adminName, info_message.replaceAll("<PLAYERNAME>", playerName)
                        .replaceAll("<DETAILS>", details).replaceAll("<TIME>", time));
            }

        } else {

            User.messagePlayer(adminName, plugin.getConfig().getString("info-failed"));

        }

    }


    public static void dropTicket(String adminName) {
        if (Request.requestStatus(adminName) == 3) {
            Request request = Request.requestInProgress.get(adminName);
            User.messagePlayer(adminName, plugin.getConfig().getString("drop-message")
                    .replaceAll("<PLAYERNAME>", request.getPlayerName()));
            User.messagePlayer(request.getPlayerName(), plugin.getConfig().getString("drop-notify-player")
                    .replaceAll("<ADMINNAME>", adminName));
            Request.removePlayerRequest(request.getPlayerName());
        } else {
            User.messagePlayer(adminName, plugin.getConfig().getString("drop-failed"));
        }
    }


    public static void redirectTicket(String adminName, String adminRedirectTo) {
        if (Admin.adminsOnline.contains(adminRedirectTo) && Request.requestStatus(adminRedirectTo) != 3 && Request.requestStatus(adminName) == 3) {
            Request request = Request.requestInProgress.get(adminName);
            Request.requestInProgress.remove(adminName);
            Request.requestInProgress.put(adminRedirectTo, request);
            request.setHandledBy(adminRedirectTo);
            String tickets = Integer.toString(Request.requestQueue.size());
            String playerName = request.getPlayerName();

            User.messagePlayer(adminName, plugin.getConfig().getString("redirect-succeeded")
                    .replaceAll("<ADMINNAME>",adminRedirectTo));

            for (String ticket_user_notify : plugin.getConfig().getStringList("ticket-in-progress-notify-player")) {
                User.messagePlayer(playerName, ticket_user_notify.replaceAll("<ADMINNAME>", adminRedirectTo));
            }

            for (String ticket_staff_notify : plugin.getConfig().getStringList("ticket-in-progress-notify-staff")) {
                Admin.messageAdmins(ticket_staff_notify.replaceAll("<ADMINNAME>", adminRedirectTo)
                        .replaceAll("<PLAYERNAME>", playerName)
                        .replaceAll("<TICKETSREMAIN>", tickets));
            }

            if(plugin.getConfig().getBoolean("ticket-in-progress-trigger-custom-command")) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), plugin.getConfig().getString("ticket-in-progress-custom-command")
                        .replaceAll("<PLAYERNAME>", playerName).replaceAll("<ADMINNAME>", adminRedirectTo));
            }
        } else {
            User.messagePlayer(adminName, plugin.getConfig().getString("redirect-failed")
                    .replaceAll("<ADMINNAME>",adminRedirectTo));
        }
    }


    /**
     * Called when an admin attempt to set a request as solved.
     * The user is then asked if the help was satisfactory
     */

    public static void closeTicket(String adminName) {
        //Get request being handled by staff
        Request request = Request.requestInProgress.remove(adminName);
        
        //Check if there was a request in progress
        if (request == null) {
            User.messagePlayer(adminName, plugin.getConfig().getString("close-failed"));
            return;
        }

        //Mark request as closed
        //request.setAttended(true);

        //Move request to Completed request queue
        Request.requestCompleted.put(request.getPlayerName(), request);

        //Message staff that request has been closes
        User.messagePlayer(adminName, plugin.getConfig().getString("close-succeeded"));

        //Set automated message for feedback
        BukkitTask reminder = new ReviewReminder(request.getPlayerName()).runReminder();
        Request.reminders.put(request.getPlayerName(), reminder);
    }


    /**
     * Closes a request and disposes it. It also give an admin an honor point based on
     * what the player rated the help received
     */

    public static void requestFeedback(String playerName, Boolean isSatisfactory) {
        //Check if a player has a request in the completed list awaiting a rating
        if (Request.requestStatus(playerName) != 4) {
            User.messagePlayer(playerName, plugin.getConfig().getString("feedback-not-required"));
            return;
        }

        //Remove request from completed request list
        Request completedRequest = Request.requestCompleted.remove(playerName);

        //check if admin is online
        Player admin = Bukkit.getPlayer(completedRequest.getHandledBy());

        if (admin == null) {
        	Request.removePlayerRequest(playerName);
            User.messagePlayer(playerName, plugin.getConfig().getString("feedback-received"));
        	return;
        }

        //Give admin honor point based on ans
        if (isSatisfactory) {
            Admin360.ds.addRecord(completedRequest, true);
            User.messagePlayer(completedRequest.getHandledBy(), plugin.getConfig().getString("upvote-rating-notify-staff")
                    .replaceAll("<PLAYERNAME>", completedRequest.getPlayerName()));

            //Fireworks test
            Player pl = Bukkit.getPlayer(completedRequest.getHandledBy());
            Firework fw = pl.getWorld().spawn(pl.getLocation(), Firework.class);
            FireworkMeta fwm = fw.getFireworkMeta();
            FireworkEffect effect = FireworkEffect.builder().trail(true).withColor(Color.LIME).with(FireworkEffect.Type.CREEPER).build();
            fwm.addEffect(effect);
            fwm.setPower(1);
            fw.setFireworkMeta(fwm);
        } else {
            Admin360.ds.addRecord(completedRequest, false);
            User.messagePlayer(completedRequest.getHandledBy(), plugin.getConfig().getString("downvote-rating-notify-staff")
                    .replaceAll("<PLAYERNAME>", playerName));
        }

        Admin360.completedToday = Admin360.completedToday + 1;

        //Stop the reminders
        Request.reminders.remove(playerName).cancel();

        //Send player a message
        User.messagePlayer(completedRequest.getPlayerName(), plugin.getConfig().getString("feedback-received"));
    }


    /**
     * Prompts a player to rate a request after an admin has handled it
     */

    public static void promptFeedback(String playerName) {

        User.messagePlayer(playerName, plugin.getConfig().getString("feedback-required"));

        if (plugin.getConfig().getBoolean("feedback-trigger-custom-command")) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), plugin.getConfig().getString("feedback-custom-command")
                    .replaceAll("<PLAYERNAME>", playerName));
        }

    }


    /**
     * Removes all requests from the request queue
     * NOTE:This doesn't remove any requests that are being handled
     * by and admin or any requests that are pending a user feedback
     */

    public static void purgeTicket(String adminName) {
        String amountPurged = Integer.toString(Request.requestQueue.size());
        Request.requestQueue.clear();

        for (String purge_message : plugin.getConfig().getStringList("purge-message")) {
            User.messagePlayer(adminName, purge_message
                    .replaceAll("<AMOUNTPURGED>", amountPurged));
        }
    }


    /**
     * Attempts to cancel a request made my a player
     */

    public static void cancelTicket(String playerName) {
        int requestStatus = Request.requestStatus(playerName);
        switch (requestStatus) {
            case 0:
                User.messagePlayer(playerName, plugin.getConfig().getString("cancel-failed-no-ticket"));
                return;
            case 1:
                Request.cancel(playerName);
                User.messagePlayer(playerName, plugin.getConfig().getString("cancel-succeeded"));
                return;
            case 2:
                User.messagePlayer(playerName, plugin.getConfig().getString("cancel-failed-in-progress"));
                return;
            case 3:
                User.messagePlayer(playerName, plugin.getConfig().getString("cancel-failed-attending"));
                return;
            case 4:
                User.messagePlayer(playerName, plugin.getConfig().getString("cancel-failed-require-feedback"));
                return;
            default:
                break;
        }
    }


    /**
     * Queries and tells the player the position/status of his request
     */

    public static void printTicketStatus(String playerName) {
        int requestStatus = Request.requestStatus(playerName);
        switch (requestStatus) {
            case 0:
                User.messagePlayer(playerName, plugin.getConfig().getString("status-no-ticket"));
                return;
            case 1:
                int position = Request.getPlayerRequestPosition(playerName);
                User.messagePlayer(playerName, plugin.getConfig().getString("status-in-queue")
                        .replaceAll("<POSITION>", Integer.toString(position)));
                return;
            case 2:
                User.messagePlayer(playerName, plugin.getConfig().getString("status-in-progress"));
                return;
            case 3:
                User.messagePlayer(playerName, plugin.getConfig().getString("status-attending"));
                return;
            case 4:
                User.messagePlayer(playerName, plugin.getConfig().getString("status-require-feedback"));
                return;
            default:
                break;
        }
    }


    /**
     * Get all requests count. Group by statuses
     * Sends the message to the specified player
     */

    public static void printTicketStats(String playerName) {

        String inQueue = Integer.toString(Request.requestQueue.size());
        String inProgress = Integer.toString(Request.requestInProgress.size());
        String awaiting = Integer.toString(Request.requestCompleted.size());
        String completed = Integer.toString(Admin360.completedToday);
        int total = Admin360.ds.getTotalTicketCount(1);
        int upVote = Admin360.ds.getTotalTicketCount(2);
        int percent;
        if (total == 0 || upVote == 0) {
            percent = 0;
        } else {
            percent = (upVote*100/(total));
        }
        String totalS = Integer.toString(total);
        String percentS = Integer.toString(percent);

        for (String stats_message : plugin.getConfig().getStringList("stats-message")) {
        	User.messagePlayer(playerName, stats_message.replaceAll("<AWAITING>", awaiting)
                    .replaceAll("<INPROGRESS>", inProgress).replaceAll("<INQUEUE>", inQueue)
                    .replaceAll("<COMPLETED>", completed).replaceAll("<TOTAL>", totalS)
                    .replaceAll("<PERCENT>", percentS));
    	}

    }


    //print hptop module
    public static void printHonorTop(String adminName, int limit) {
        String[][] honors = Admin360.ds.getTopHonors(limit);
        for (String leaderboard_title : plugin.getConfig().getStringList("leaderboard-title")) {
            User.messagePlayer(adminName, leaderboard_title);
        }
        for (int i=0;i<limit;i++) {
            if(honors[i][0] != null) {
                User.messagePlayer(adminName, plugin.getConfig().getString("leaderboard-body")
                        .replaceAll("<ADMINNAME>", honors[i][0]).replaceAll("<UPVOTE>", honors[i][1])
                        .replaceAll("<DOWNVOTE>", honors[i][2]).replaceAll("<TOTAL>", honors[i][3])
                        .replaceAll("<PERCENT>", honors[i][4]));
            }
        }
        for (String leaderboard_footer : plugin.getConfig().getStringList("leaderboard-footer")) {
            User.messagePlayer(adminName, leaderboard_footer);
        }
    }


    public static void printHonorStats(String adminName, String sender) {
        int upVote = Admin360.ds.getAdminTicketCount(adminName,1);
        int downVote = Admin360.ds.getAdminTicketCount(adminName,2);
        int total = upVote + downVote;
        int percent;
        if (total == 0 || upVote == 0) {
            percent = 0;
        } else {
             percent = (upVote*100/(total));
        }
        String upVoteS = Integer.toString(upVote);
        String downVoteS = Integer.toString(downVote);
        String totalS = Integer.toString(total);
        String percentS = Integer.toString(percent);
        for (String hpstats_message : plugin.getConfig().getStringList("hpstats-message")) {
            User.messagePlayer(sender, hpstats_message.replaceAll("<UPVOTE>", upVoteS)
                    .replaceAll("<DOWNVOTE>", downVoteS).replaceAll("<TOTAL>", totalS)
                    .replaceAll("<PERCENT>", percentS).replaceAll("<ADMINNAME>", adminName));
        }
    }


    //print history module
    public static void printHonorHistory(String adminName, int limit) {
        String[][] history = Admin360.ds.getHistory(limit);
        String rating;
        for (String history_title : plugin.getConfig().getStringList("history-title")) {
            User.messagePlayer(adminName, history_title);
        }
        for (int i=0;i<limit;i++) {
            if(history[i][0] != null) {
                if (Long.parseLong(history[i][4])==0) {
                    rating = plugin.getConfig().getString("downvote-indicator");
                } else {
                    rating = plugin.getConfig().getString("upvote-indicator");
                }
                User.messagePlayer(adminName, plugin.getConfig().getString("history-body")
                        .replaceAll("<PLAYERNAME>", history[i][0]).replaceAll("<ADMINNAME>", history[i][1])
                        .replaceAll("<DETAILS>", history[i][2])
                        .replaceAll("<TIME>", new SimpleDateFormat("MM/dd/yy HH:mm").format(new Date(Long.parseLong(history[i][3])*1000)))
                        .replaceAll("<RATING>", rating));
            }
        }
        for (String history_footer : plugin.getConfig().getStringList("history-footer")) {
            User.messagePlayer(adminName, history_footer);
        }
    }


}
