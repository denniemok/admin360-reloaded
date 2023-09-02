package com.battleasya.Admin360.handler;

import com.battleasya.Admin360.Admin360;
import com.battleasya.Admin360.entities.Admin;
import com.battleasya.Admin360.entities.Request;
import com.battleasya.Admin360.entities.User;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class RequestHandler {

    private final Admin360 plugin;

    /* RequestHandler Constructor */
    public RequestHandler(Admin360 plugin) {
        this.plugin = plugin;
    }

    /**
     * Adds a player to queue with a comment they specify
     */
    public void createTicket(CommandSender sender, String comment) {

        String playerName = sender.getName();
        UUID playerID = ((Player) sender).getUniqueId();

        if (Config.check_staff_availability && Admin.isListEmpty()) {
            User.messagePlayer(sender, Config.create_failed_no_staff);
            return;
        }

        // Check user status
        switch (Request.getStatus(playerID)) {
            case 1:
                User.messagePlayer(sender, Config.create_failed_in_queue);
                return;
            case 2:
                User.messagePlayer(sender, Config.create_failed_in_progress);
                return;
            case 4:
                User.messagePlayer(sender, Config.create_failed_await_feedback);
                return;
        }

        // Get number of seconds from wherever you want
        if (Config.cooldownEnable){
            long secondsLeft = User.inCooldown(playerID, Config.cooldownInterval);
            if (secondsLeft != -1) { // in cooldown
                User.messagePlayer(sender, Config.cooldownMessage
                        .replaceAll("<SECONDSLEFT>", String.valueOf(secondsLeft)));
                return;
            }
        }

        Request request = new Request(playerID, playerName, comment);
        Request.addToPending(request); // add to queue

        // Notify user about request
        String positionInQueue = Integer.toString(Request.requestPending.size());

        for (String message : Config.create_succeeded_notify_player) {
            User.messagePlayer(sender, message
                    .replaceAll("<POSITION>", positionInQueue));
        }

        if (Config.create_succeeded_trigger_enable) {
        	Bukkit.dispatchCommand(Bukkit.getConsoleSender(), Config.create_succeeded_trigger_command
                    .replaceAll("<PLAYERNAME>", playerName)
                    .replaceAll("<POSITION>", positionInQueue));
        }

        for (String message : Config.create_succeeded_notify_staff) {
            Admin.messageAdmins(message
                    .replaceAll("<PLAYERNAME>", playerName)
                    .replaceAll("<DETAILS>", comment)
                    .replaceAll("<TICKETSREMAIN>", positionInQueue));
        }

    }


    public void printQueueList(CommandSender admin) {

        int i = 1;

        for (Request request : Request.requestPending) {
            User.messagePlayer(admin, Config.list_message
                    .replaceAll("<INDEX>", Integer.toString(i))
                    .replaceAll("<PLAYERNAME>", request.getPlayerName())
                    .replaceAll("<DETAILS>", request.getComment())
                    .replaceAll("<TIME>", new SimpleDateFormat("MM/dd/yy HH:mm").format(new Date(request.getTime() * 1000))));
            i++;
        }

    }


    public void attendTicket(CommandSender admin, String playerName) {

        String adminName = admin.getName();
        UUID adminID = ((Player) admin).getUniqueId();
        UUID playerID = null;

        // This must go first to avoid problems
        if (Request.isAttending(adminID)) {
            User.messagePlayer(admin, Config.next_failed_attending);
            return;
        }

        if (Request.isPendingEmpty()) {
            User.messagePlayer(admin, Config.next_failed_no_ticket);
            return;
        }

        if (playerName.isEmpty()) {

            //Get Objects to work with
            Request request = Request.getHeadOfPending(); // removes head of queue

            playerName = request.getPlayerName(); // get real name
            playerID = request.getPlayerID();

            //Set the staff who honored the request
            request.setHandledBy(adminName);
            request.setHandledBy(adminID);

            //Move request to in progress list
            Request.requestAttending.put(adminID, request);

        } else {

            // whether request exists
            int k = 0;

            for (Request request : Request.requestPending) {
                if (request.getPlayerName().equalsIgnoreCase(playerName)) {
                    playerName = request.getPlayerName(); // get real name
                    playerID = request.getPlayerID();
                    request.setHandledBy(adminName);
                    request.setHandledBy(adminID);
                    Request.requestAttending.put(adminID, request);
                    Request.requestPending.remove(request);
                    k = 1;
                    break;
                }
            }

            // cannot find request
            if (k == 0) {
                User.messagePlayer(admin, Config.next_failed_no_ticket);
                return;
            }

        }

        Player player = Bukkit.getPlayer(playerID);

        if (player == null) {
            Request.removePlayer(playerID);
            User.removePlayer(playerID);
            Request.removeAdmin(playerID); // remove previous assignment
            return;
        }

        // Teleport admin to player
        if (Config.use_auto_teleport) {
            try {
                ((Player) admin).teleport(player);
                User.messagePlayer(admin, Config.teleport_succeeded
                        .replaceAll("<PLAYERNAME>", playerName));
            } catch (Exception e) {
                plugin.getLogger().severe("An error occurred when trying to teleport " + adminName + " to " + playerName + ".");
                User.messagePlayer(admin, Config.teleport_failed);
                return;
            }
        }

        // Message to the respective player and staff team
        String ticketsRemain = Integer.toString(Request.requestPending.size());

        for (String message : Config.ticket_in_progress_notify_player) {
            User.messagePlayer(player, message
                    .replaceAll("<ADMINNAME>", adminName));
        }

        for (String message : Config.ticket_in_progress_notify_staff) {
            Admin.messageAdmins(message
                    .replaceAll("<ADMINNAME>", adminName)
                    .replaceAll("<PLAYERNAME>", playerName)
                    .replaceAll("<TICKETSREMAIN>", ticketsRemain));
        }

        if (Config.ticket_in_progress_trigger_custom_command) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), Config.ticket_in_progress_custom_command
                    .replaceAll("<PLAYERNAME>", playerName)
                    .replaceAll("<ADMINNAME>", adminName));
        }

    }


    public void teleport2Player(CommandSender admin) {

        String adminName = admin.getName();
        UUID adminID = ((Player) admin).getUniqueId();

        // Check if a staff is already handling a player
        if (Request.isAttending(adminID)) {

            Request request = Request.getRequestAttending(adminID);

            String playerName = request.getPlayerName();
            UUID playerID = request.getPlayerID();

            try {
                ((Player) admin).teleport(Bukkit.getPlayer(playerID));
                User.messagePlayer(admin, Config.teleport_succeeded
                        .replaceAll("<PLAYERNAME>", playerName));
            } catch (Exception e) {
                plugin.getLogger().severe("An error occurred when trying to teleport " + adminName + " to " + playerName + ".");
                User.messagePlayer(admin, Config.teleport_failed);
            }

        } else {
            User.messagePlayer(admin, Config.teleport_failed);
        }

    }


    public void printTicketDetails(CommandSender admin) {

        UUID adminID = ((Player) admin).getUniqueId();

        if (Request.isAttending(adminID)) {

            Request request = Request.getRequestAttending(adminID);

            String comment = request.getComment();
            String playerName = request.getPlayerName();

            String time = new SimpleDateFormat("MM/dd/yy HH:mm")
                    .format(new Date(request.getTime()*1000));

            for (String message : Config.info_message) {
                User.messagePlayer(admin, message
                        .replaceAll("<PLAYERNAME>", playerName)
                        .replaceAll("<DETAILS>", comment)
                        .replaceAll("<TIME>", time));
            }

        } else {

            User.messagePlayer(admin, Config.info_failed);

        }

    }


    public void dropTicket(CommandSender sender) {

        String adminName = sender.getName();
        UUID adminID = ((Player) sender).getUniqueId();

        if (Request.isAttending(adminID)) {

            Request request = Request.getRequestAttending(adminID);

            String playerName = request.getPlayerName();
            UUID playerID = request.getPlayerID();

            User.messagePlayer(sender, Config.drop_message
                    .replaceAll("<PLAYERNAME>", playerName));

            Player player = Bukkit.getPlayer(playerID);

            assert player != null;

            User.messagePlayer(player, Config.drop_notify_player
                    .replaceAll("<ADMINNAME>", adminName));

            Request.removePlayer(playerID);

        } else {
            User.messagePlayer(sender, Config.drop_failed);
        }
    }


    public void transferTicket(CommandSender sender, String admin2Name) {

        UUID admin1ID = ((Player) sender).getUniqueId();

        Player admin2 = Bukkit.getPlayer(admin2Name);

        if (admin2 == null) {
            return;
        }

        admin2Name = admin2.getName(); // get real name

        UUID admin2ID = admin2.getUniqueId();

        if (Admin.isAdmin(admin2ID) && Request.isAttending(admin1ID) && !Request.isAttending(admin2ID)) {

            Request request = Request.getRequestAttending(admin1ID);

            request.setHandledBy(admin2Name);
            request.setHandledBy(admin2ID);

            Request.requestAttending.remove(admin1ID);
            Request.requestAttending.put(admin2ID, request);

            String tickets = Integer.toString(Request.requestPending.size());
            String playerName = request.getPlayerName();

            Player player = Bukkit.getPlayer(request.getPlayerID());

            assert player != null;

            User.messagePlayer(sender, Config.redirect_succeeded
                    .replaceAll("<ADMINNAME>",admin2Name));

            for (String message : Config.ticket_in_progress_notify_player) {
                User.messagePlayer(player, message
                        .replaceAll("<ADMINNAME>", admin2Name));
            }

            for (String message : Config.ticket_in_progress_notify_staff) {
                Admin.messageAdmins(message
                        .replaceAll("<ADMINNAME>", admin2Name)
                        .replaceAll("<PLAYERNAME>", playerName)
                        .replaceAll("<TICKETSREMAIN>", tickets));
            }

            if (Config.ticket_in_progress_trigger_custom_command) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), Config.ticket_in_progress_custom_command
                        .replaceAll("<PLAYERNAME>", playerName)
                        .replaceAll("<ADMINNAME>", admin2Name));
            }

        } else {
            User.messagePlayer(sender, Config.redirect_failed
                    .replaceAll("<ADMINNAME>",admin2Name));
        }
    }


    /**
     * Called when an admin attempt to set a request as solved.
     * The user is then asked if the help was satisfactory
     */

    public void closeTicket(CommandSender sender) {

        UUID adminID = ((Player) sender).getUniqueId();

        if (!Request.isAttending(adminID)) {
            User.messagePlayer(sender, Config.close_failed);
            return;
        }

        //Get request being handled by staff
        Request request = Request.requestAttending.remove(adminID);

        UUID playerID = request.getPlayerID();

        //Move request to Completed request queue
        Request.requestAwaiting.put(playerID, request);

        //Message staff that request has been closes
        User.messagePlayer(sender, Config.close_succeeded);

        //Set automated message for feedback
        if (Config.show_reminder) {
            BukkitTask reminder = new ReviewHandler(plugin, playerID).runReminder();
            Request.reviewReminder.put(playerID, reminder);
        } else {
            promptFeedback(playerID);
        }

    }


    /**
     * Closes a request and disposes it. It also gives an admin an honor point based on
     * what the player rated the help received
     */

    public void giveFeedback(CommandSender sender, Boolean isSatisfactory) {

        String playerName = sender.getName();
        UUID playerID = ((Player) sender).getUniqueId();

        // Check if a player has a request in the completed list awaiting a rating
        if (!Request.isCompleted(playerID)) {
            User.messagePlayer(sender, Config.feedback_not_required);
            return;
        }

        // Remove request from completed request list
        Request completedRequest = Request.requestAwaiting.remove(playerID);

        Request.completedToday += 1;

        // Stop the reminders
        Request.reviewReminder.remove(playerID).cancel();

        // add to database
        plugin.getDataSource().addRecord(completedRequest, isSatisfactory);

        //Send player a message
        User.messagePlayer(sender, Config.feedback_received);

        // check if admin is online
        Player admin = Bukkit.getPlayer(completedRequest.getHandledByID());

        if (admin == null) {
            return;
        }

        // Give admin honor point based on ans
        if (isSatisfactory) {

            User.messagePlayer(admin, Config.upvote_rating_notify_staff
                    .replaceAll("<PLAYERNAME>", playerName));

            // Fireworks
            Firework fw = admin.getWorld().spawn(admin.getLocation(), Firework.class);
            FireworkMeta fwm = fw.getFireworkMeta();
            FireworkEffect effect = FireworkEffect.builder().trail(true).withColor(Color.LIME).with(FireworkEffect.Type.CREEPER).build();
            fwm.addEffect(effect);
            fwm.setPower(1);
            fw.setFireworkMeta(fwm);

        } else {

            User.messagePlayer(admin, Config.downvote_rating_notify_staff
                    .replaceAll("<PLAYERNAME>", playerName));

        }

    }


    /**
     * Prompts a player to rate a request after an admin has handled it
     */

    public boolean promptFeedback(UUID playerID) {

        Player player = Bukkit.getPlayer(playerID);

        if (player == null) {
            return false;
        }

        String playerName = player.getName();

        User.messagePlayer(player, Config.feedback_required);

        if (Config.feedback_trigger_custom_command) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), Config.feedback_custom_command
                    .replaceAll("<PLAYERNAME>", playerName));
        }

        return true;

    }


    /**
     * Removes all requests from the request queue
     * NOTE:This doesn't remove any requests that are being handled
     * by and admin or any requests that are pending a user feedback
     */

    public void purgeTicket(CommandSender sender) {

        String amountPurged = Integer.toString(Request.requestPending.size());

        Request.requestPending.clear();

        for (String message : Config.purge_message) {
            User.messagePlayer(sender, message
                    .replaceAll("<AMOUNTPURGED>", amountPurged));
        }

    }

    public void deleteTicket(CommandSender sender, String playerName) {

        Player player = Bukkit.getPlayer(playerName);

        if (player == null) {
            return;
        }

        playerName = player.getName(); // get real name

        UUID playerID = player.getUniqueId();

        Request.removePlayer(playerID);

        User.messagePlayer(sender, Config.delete_message
                .replaceAll("<PLAYERNAME>", playerName));

    }


    /**
     * Attempts to cancel a request made my a player
     */

    public void cancelTicket(CommandSender sender) {

        UUID playerID = ((Player) sender).getUniqueId();

        int requestStatus = Request.getStatus(playerID);

        switch (requestStatus) {

            case 0:
                User.messagePlayer(sender, Config.cancel_failed_no_ticket);
                return;
            case 1:
                Request.cancel(playerID);
                User.messagePlayer(sender, Config.cancel_succeeded_notify_player);
                return;
            case 2:
                User.messagePlayer(sender, Config.cancel_failed_in_progress);
                return;
            case 3:
                User.messagePlayer(sender, Config.cancel_failed_attending);
                return;
            case 4:
                User.messagePlayer(sender, Config.cancel_failed_await_feedback);

        }

    }


    /**
     * Queries and tells the player the position/status of his request
     */

    public void printTicketStatus(CommandSender sender) {

        UUID playerID = ((Player) sender).getUniqueId();

        int requestStatus = Request.getStatus(playerID);

        switch (requestStatus) {

            case 0:
                User.messagePlayer(sender, Config.status_no_ticket);
                return;
            case 1:
                int position = Request.getPositionInPending(playerID);
                User.messagePlayer(sender, Config.status_in_queue
                        .replaceAll("<POSITION>", Integer.toString(position)));
                return;
            case 2:
                User.messagePlayer(sender, Config.status_in_progress);
                return;
            case 3:
                User.messagePlayer(sender, Config.status_attending);
                return;
            case 4:
                User.messagePlayer(sender, Config.status_await_feedback);

        }
    }


    /**
     * Get all requests count. Group by statuses
     * Sends the message to the specified player
     */

    public void printTicketStats(CommandSender sender) {

        String inQueue = Integer.toString(Request.requestPending.size());
        String inProgress = Integer.toString(Request.requestAttending.size());
        String awaiting = Integer.toString(Request.requestAwaiting.size());
        String completed = Integer.toString(Request.completedToday);

        int total = plugin.getDataSource().getTotalTicketCount(1);
        int upVote = plugin.getDataSource().getTotalTicketCount(2);
        int percent;

        if (total == 0 || upVote == 0) {
            percent = 0;
        } else {
            percent = upVote * 100 / total;
        }

        String totalS = Integer.toString(total);
        String percentS = Integer.toString(percent);

        for (String message : Config.stats_message) {
        	User.messagePlayer(sender, message.replaceAll("<AWAITING>", awaiting)
                    .replaceAll("<INPROGRESS>", inProgress)
                    .replaceAll("<INQUEUE>", inQueue)
                    .replaceAll("<COMPLETED>", completed)
                    .replaceAll("<TOTAL>", totalS)
                    .replaceAll("<PERCENT>", percentS));
    	}

    }


    // print hptop module
    public void printHonorTop(CommandSender sender, int limit) {

        String[][] honors = plugin.getDataSource().getTopHonors(limit);

        for (String message : Config.leaderboard_title) {
            User.messagePlayer(sender, message);
        }

        for (int i = 0; i < limit ; i++) {
            if (honors[i][0] != null) {
                User.messagePlayer(sender, Config.leaderboard_body
                        .replaceAll("<ADMINNAME>", honors[i][0])
                        .replaceAll("<UPVOTE>", honors[i][1])
                        .replaceAll("<DOWNVOTE>", honors[i][2])
                        .replaceAll("<TOTAL>", honors[i][3])
                        .replaceAll("<PERCENT>", honors[i][4]));
            }
        }

        for (String message : Config.leaderboard_footer) {
            User.messagePlayer(sender, message);
        }

    }


    public void printHonorStats(CommandSender sender, String adminName) {

        int upVote = plugin.getDataSource().getAdminTicketCount(adminName,1);
        int downVote = plugin.getDataSource().getAdminTicketCount(adminName,2);
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

        for (String message : Config.hpstats_message) {
            User.messagePlayer(sender, message
                    .replaceAll("<UPVOTE>", upVoteS)
                    .replaceAll("<DOWNVOTE>", downVoteS)
                    .replaceAll("<TOTAL>", totalS)
                    .replaceAll("<PERCENT>", percentS)
                    .replaceAll("<ADMINNAME>", adminName));
        }

    }


    //print history module
    public void printHonorHistory(CommandSender sender, int limit) {

        String[][] history = plugin.getDataSource().getHistory(limit);
        String rating;

        for (String history_title : Config.history_title) {
            User.messagePlayer(sender, history_title);
        }

        for (int i = 0; i < limit; i++) {
            if (history[i][0] != null) {
                if (Long.parseLong(history[i][4])==0) {
                    rating = Config.downvote_indicator;
                } else {
                    rating = Config.upvote_indicator;
                }
                User.messagePlayer(sender, Config.history_body
                        .replaceAll("<PLAYERNAME>", history[i][0])
                        .replaceAll("<ADMINNAME>", history[i][1])
                        .replaceAll("<DETAILS>", history[i][2])
                        .replaceAll("<TIME>", new SimpleDateFormat("MM/dd/yy HH:mm").format(new Date(Long.parseLong(history[i][3])*1000)))
                        .replaceAll("<RATING>", rating));
            }
        }

        for (String history_footer : Config.history_footer) {
            User.messagePlayer(sender, history_footer);
        }

    }


}
