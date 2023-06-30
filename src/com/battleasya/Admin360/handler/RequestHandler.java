package com.battleasya.Admin360.handler;

import com.battleasya.Admin360.Admin360;
import com.battleasya.Admin360.entities.Admin;
import com.battleasya.Admin360.entities.Request;
import com.battleasya.Admin360.entities.User;
import com.battleasya.Admin360.util.Config;
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

    private final Admin360 plugin;

    /* RequestHandler Constructor */
    public RequestHandler(Admin360 plugin) {
        this.plugin = plugin;
    }

    /*
     * Adds a player in the request queue with a reason they specify
     * Sends an error message to player if no staff are available
     */
    public void createTicket(String playerName, String details) {


        //Check if staff are available
        if (Config.staffOnlineRequired && Admin.adminsOnline.size() == 0) {
            User.messagePlayer(playerName, Config.createFailedNoStaff);
            return;
        }

        //Check if user already has a pending request(status)
        switch (Request.requestStatus(playerName)) {
            case 1:
                User.messagePlayer(playerName, Config.createFailedInQueue);
                return;
            case 2:
                User.messagePlayer(playerName, Config.createFailedInProgress);
                return;
            case 3:
                //User.messagePlayer(playerName, Config.createFailedAntiExploit);
                return;
            case 4:
                User.messagePlayer(playerName, Config.create_failed_require_feedback);
                return;
            default:
                //Add request into queue
                break;
        }

        // Get number of seconds from wherever you want
        if (Config.useCooldown){
            int coolDownTime = Config.cooldownTimer;
            if(User.coolDown.containsKey(playerName)) {
                long secondsLeft = ((User.coolDown.get(playerName)/1000)+coolDownTime) - (System.currentTimeMillis()/1000);
                if(secondsLeft>0) {
                    // Still cooling down
                    User.messagePlayer(playerName, Config.cooldownMessage
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

        for (String created_succeeded : Config.create_succeeded) {
            User.messagePlayer(playerName, created_succeeded.replaceAll("<POSITION>", positionInQueue));
        }

        if (Config.ticket_created_trigger_custom_command) {
        	Bukkit.dispatchCommand(Bukkit.getConsoleSender(), Config.ticket_created_custom_command
                    .replaceAll("<PLAYERNAME>", playerName).replaceAll("<POSITION>", positionInQueue));
        }

        for (String ticket_created_notify_staff : Config.ticket_created_notify_staff) {
            Admin.messageAdmins(ticket_created_notify_staff.replaceAll("<PLAYERNAME>", playerName)
                    .replaceAll("<DETAILS>", details).replaceAll("<TICKETSREMAIN>", positionInQueue));
        }

    }


    public void printQueueList(String adminName) {
        int i = 1;
        for (Request request : Request.requestQueue) {
            User.messagePlayer(adminName, Config.list_message
                    .replaceAll("<INDEX>", Integer.toString(i))
                    .replaceAll("<PLAYERNAME>", request.getPlayerName())
                    .replaceAll("<DETAILS>", request.getReason())
                    .replaceAll("<TIME>", new SimpleDateFormat("MM/dd/yy HH:mm").format(new Date(request.getTime() * 1000))));
            i++;
        }
    }


    public void pickTicket(String adminName, String playerName) {
        //Check if a staff is already handling a player, this must go first to avoid problems
        if (Request.requestStatus(adminName) == 3) {
            User.messagePlayer(adminName, Config.next_failed_attending);
            return;
        }

        //Sanity Check
        if (Request.requestQueue.peek() == null) {
            User.messagePlayer(adminName, Config.next_failed_no_ticket);
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
            User.messagePlayer(adminName, Config.next_failed_no_ticket);
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
            if(Config.use_auto_teleport) {
                try {
                    admin.teleport(user);
                } catch (Exception e) {
                    return;
                }
            }

            //Message to the respective player and staff team
            String tickets = Integer.toString(Request.requestQueue.size());

            for (String ticket_user_notify : Config.ticket_in_progress_notify_player) {
                User.messagePlayer(playerName, ticket_user_notify.replaceAll("<ADMINNAME>", adminName));
            }

            for (String ticket_staff_notify : Config.ticket_in_progress_notify_staff) {
                Admin.messageAdmins(ticket_staff_notify.replaceAll("<ADMINNAME>", adminName)
                        .replaceAll("<PLAYERNAME>", playerName).replaceAll("<TICKETSREMAIN>", tickets));
            }

            if(Config.ticket_in_progress_trigger_custom_command) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), Config.ticket_in_progress_custom_command
                        .replaceAll("<PLAYERNAME>", playerName).replaceAll("<ADMINNAME>", adminName));
            }
        }
    }


    public void nextTicket(String adminName) {

        //Check if a staff is already handling a player, this must go first to avoid problems
        if (Request.requestStatus(adminName) == 3) {
            User.messagePlayer(adminName, Config.next_failed_attending);
            return;
        }

        //Sanity Check
        if (Request.requestQueue.peek() == null) {
            User.messagePlayer(adminName, Config.next_failed_no_ticket);
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
            if(Config.use_auto_teleport) {
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

            for (String ticket_user_notify : Config.ticket_in_progress_notify_player) {
            	User.messagePlayer(playerName, ticket_user_notify.replaceAll("<ADMINNAME>", adminName));
        	}
            
            for (String ticket_staff_notify : Config.ticket_in_progress_notify_staff) {
            	Admin.messageAdmins(ticket_staff_notify.replaceAll("<ADMINNAME>", adminName)
                        .replaceAll("<PLAYERNAME>", playerName).replaceAll("<TICKETSREMAIN>", tickets));
        	}

            if(Config.ticket_in_progress_trigger_custom_command) {
            	Bukkit.dispatchCommand(Bukkit.getConsoleSender(), Config.ticket_in_progress_custom_command
                        .replaceAll("<PLAYERNAME>", playerName).replaceAll("<ADMINNAME>", adminName));
            }
        }

    }


    public void teleportPlayer(String adminName) {

        //Check if a staff is already handling a player
        if (Request.requestStatus(adminName) == 3) {

            Request request = Request.requestInProgress.get(adminName);

            try {
                Bukkit.getPlayer(adminName).teleport(Bukkit.getPlayer(request.getPlayerName()));
                User.messagePlayer(adminName, Config.teleport_succeeded
                        .replaceAll("<PLAYERNAME>", request.getPlayerName()));
            } catch (Exception e) {
                //empty catch line suppressing errors
            }

        } else {
            User.messagePlayer(adminName, Config.teleport_failed);
        }

    }


    public void printTicketDetails(String adminName) {

        if (Request.requestStatus(adminName) == 3) {

            Request request = Request.requestInProgress.get(adminName);
            String details = request.getReason();
            String playerName = request.getPlayerName();

            String time = new SimpleDateFormat("MM/dd/yy HH:mm").format(new Date(request.getTime()*1000));

            for (String info_message : Config.info_message) {
                User.messagePlayer(adminName, info_message.replaceAll("<PLAYERNAME>", playerName)
                        .replaceAll("<DETAILS>", details).replaceAll("<TIME>", time));
            }

        } else {

            User.messagePlayer(adminName, Config.info_failed);

        }

    }


    public void dropTicket(String adminName) {
        if (Request.requestStatus(adminName) == 3) {
            Request request = Request.requestInProgress.get(adminName);
            User.messagePlayer(adminName, Config.drop_message
                    .replaceAll("<PLAYERNAME>", request.getPlayerName()));
            User.messagePlayer(request.getPlayerName(), Config.drop_notify_player
                    .replaceAll("<ADMINNAME>", adminName));
            Request.removePlayerRequest(request.getPlayerName());
        } else {
            User.messagePlayer(adminName, Config.drop_failed);
        }
    }


    public void redirectTicket(String adminName, String adminRedirectTo) {
        if (Admin.adminsOnline.contains(adminRedirectTo) && Request.requestStatus(adminRedirectTo) != 3 && Request.requestStatus(adminName) == 3) {
            Request request = Request.requestInProgress.get(adminName);
            Request.requestInProgress.remove(adminName);
            Request.requestInProgress.put(adminRedirectTo, request);
            request.setHandledBy(adminRedirectTo);
            String tickets = Integer.toString(Request.requestQueue.size());
            String playerName = request.getPlayerName();

            User.messagePlayer(adminName, Config.redirect_succeeded
                    .replaceAll("<ADMINNAME>",adminRedirectTo));

            for (String ticket_user_notify : Config.ticket_in_progress_notify_player) {
                User.messagePlayer(playerName, ticket_user_notify.replaceAll("<ADMINNAME>", adminRedirectTo));
            }

            for (String ticket_staff_notify : Config.ticket_in_progress_notify_staff) {
                Admin.messageAdmins(ticket_staff_notify.replaceAll("<ADMINNAME>", adminRedirectTo)
                        .replaceAll("<PLAYERNAME>", playerName)
                        .replaceAll("<TICKETSREMAIN>", tickets));
            }

            if(Config.ticket_in_progress_trigger_custom_command) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), Config.ticket_in_progress_custom_command
                        .replaceAll("<PLAYERNAME>", playerName).replaceAll("<ADMINNAME>", adminRedirectTo));
            }
        } else {
            User.messagePlayer(adminName, Config.redirect_failed
                    .replaceAll("<ADMINNAME>",adminRedirectTo));
        }
    }


    /**
     * Called when an admin attempt to set a request as solved.
     * The user is then asked if the help was satisfactory
     */

    public void closeTicket(String adminName) {
        //Get request being handled by staff
        Request request = Request.requestInProgress.remove(adminName);
        
        //Check if there was a request in progress
        if (request == null) {
            User.messagePlayer(adminName, Config.close_failed);
            return;
        }

        //Mark request as closed
        //request.setAttended(true);

        //Move request to Completed request queue
        Request.requestCompleted.put(request.getPlayerName(), request);

        //Message staff that request has been closes
        User.messagePlayer(adminName, Config.close_succeeded);

        //Set automated message for feedback
        BukkitTask reminder = new ReviewHandler(plugin, request.getPlayerName()).runReminder();
        Request.reminders.put(request.getPlayerName(), reminder);
    }


    /**
     * Closes a request and disposes it. It also gives an admin an honor point based on
     * what the player rated the help received
     */

    public void requestFeedback(String playerName, Boolean isSatisfactory) {
        //Check if a player has a request in the completed list awaiting a rating
        if (Request.requestStatus(playerName) != 4) {
            User.messagePlayer(playerName, Config.feedback_not_required);
            return;
        }

        //Remove request from completed request list
        Request completedRequest = Request.requestCompleted.remove(playerName);

        //check if admin is online
        Player admin = Bukkit.getPlayer(completedRequest.getHandledBy());

        if (admin == null) {
        	Request.removePlayerRequest(playerName);
            User.messagePlayer(playerName, Config.feedback_received);
        	return;
        }

        //Give admin honor point based on ans
        if (isSatisfactory) {
            plugin.getDataSource().addRecord(completedRequest, true);
            User.messagePlayer(completedRequest.getHandledBy(), Config.upvote_rating_notify_staff
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
            plugin.getDataSource().addRecord(completedRequest, false);
            User.messagePlayer(completedRequest.getHandledBy(), Config.downvote_rating_notify_staff
                    .replaceAll("<PLAYERNAME>", playerName));
        }

        Request.completedToday = Request.completedToday + 1;

        //Stop the reminders
        Request.reminders.remove(playerName).cancel();

        //Send player a message
        User.messagePlayer(completedRequest.getPlayerName(), Config.feedback_received);
    }


    /**
     * Prompts a player to rate a request after an admin has handled it
     */

    public void promptFeedback(String playerName) {

        User.messagePlayer(playerName, Config.feedback_required);

        if (Config.feedback_trigger_custom_command) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), Config.feedback_custom_command
                    .replaceAll("<PLAYERNAME>", playerName));
        }

    }


    /**
     * Removes all requests from the request queue
     * NOTE:This doesn't remove any requests that are being handled
     * by and admin or any requests that are pending a user feedback
     */

    public void purgeTicket(String adminName) {
        String amountPurged = Integer.toString(Request.requestQueue.size());
        Request.requestQueue.clear();

        for (String purge_message : Config.purge_message) {
            User.messagePlayer(adminName, purge_message
                    .replaceAll("<AMOUNTPURGED>", amountPurged));
        }
    }


    /**
     * Attempts to cancel a request made my a player
     */

    public void cancelTicket(String playerName) {
        int requestStatus = Request.requestStatus(playerName);
        switch (requestStatus) {
            case 0:
                User.messagePlayer(playerName, Config.cancel_failed_no_ticket);
                return;
            case 1:
                Request.cancel(playerName);
                User.messagePlayer(playerName, Config.cancel_succeeded);
                return;
            case 2:
                User.messagePlayer(playerName, Config.cancel_failed_in_progress);
                return;
            case 3:
                User.messagePlayer(playerName, Config.cancel_failed_attending);
                return;
            case 4:
                User.messagePlayer(playerName, Config.cancel_failed_require_feedback);
                return;
            default:
                break;
        }
    }


    /**
     * Queries and tells the player the position/status of his request
     */

    public void printTicketStatus(String playerName) {
        int requestStatus = Request.requestStatus(playerName);
        switch (requestStatus) {
            case 0:
                User.messagePlayer(playerName, Config.status_no_ticket);
                return;
            case 1:
                int position = Request.getPlayerRequestPosition(playerName);
                User.messagePlayer(playerName, Config.status_in_queue
                        .replaceAll("<POSITION>", Integer.toString(position)));
                return;
            case 2:
                User.messagePlayer(playerName, Config.status_in_progress);
                return;
            case 3:
                User.messagePlayer(playerName, Config.status_attending);
                return;
            case 4:
                User.messagePlayer(playerName, Config.status_require_feedback);
                return;
            default:
                break;
        }
    }


    /**
     * Get all requests count. Group by statuses
     * Sends the message to the specified player
     */

    public void printTicketStats(String playerName) {

        String inQueue = Integer.toString(Request.requestQueue.size());
        String inProgress = Integer.toString(Request.requestInProgress.size());
        String awaiting = Integer.toString(Request.requestCompleted.size());
        String completed = Integer.toString(Request.completedToday);
        int total = plugin.getDataSource().getTotalTicketCount(1);
        int upVote = plugin.getDataSource().getTotalTicketCount(2);
        int percent;
        if (total == 0 || upVote == 0) {
            percent = 0;
        } else {
            percent = (upVote*100/(total));
        }
        String totalS = Integer.toString(total);
        String percentS = Integer.toString(percent);

        for (String stats_message : Config.stats_message) {
        	User.messagePlayer(playerName, stats_message.replaceAll("<AWAITING>", awaiting)
                    .replaceAll("<INPROGRESS>", inProgress).replaceAll("<INQUEUE>", inQueue)
                    .replaceAll("<COMPLETED>", completed).replaceAll("<TOTAL>", totalS)
                    .replaceAll("<PERCENT>", percentS));
    	}

    }


    //print hptop module
    public void printHonorTop(String adminName, int limit) {
        String[][] honors = plugin.getDataSource().getTopHonors(limit);
        for (String leaderboard_title : Config.leaderboard_title) {
            User.messagePlayer(adminName, leaderboard_title);
        }
        for (int i=0;i<limit;i++) {
            if(honors[i][0] != null) {
                User.messagePlayer(adminName, Config.leaderboard_body
                        .replaceAll("<ADMINNAME>", honors[i][0]).replaceAll("<UPVOTE>", honors[i][1])
                        .replaceAll("<DOWNVOTE>", honors[i][2]).replaceAll("<TOTAL>", honors[i][3])
                        .replaceAll("<PERCENT>", honors[i][4]));
            }
        }
        for (String leaderboard_footer : Config.leaderboard_footer) {
            User.messagePlayer(adminName, leaderboard_footer);
        }
    }


    public void printHonorStats(String adminName, String sender) {
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
        for (String hpstats_message : Config.hpstats_message) {
            User.messagePlayer(sender, hpstats_message.replaceAll("<UPVOTE>", upVoteS)
                    .replaceAll("<DOWNVOTE>", downVoteS).replaceAll("<TOTAL>", totalS)
                    .replaceAll("<PERCENT>", percentS).replaceAll("<ADMINNAME>", adminName));
        }
    }


    //print history module
    public void printHonorHistory(String adminName, int limit) {
        String[][] history = plugin.getDataSource().getHistory(limit);
        String rating;
        for (String history_title : Config.history_title) {
            User.messagePlayer(adminName, history_title);
        }
        for (int i=0;i<limit;i++) {
            if(history[i][0] != null) {
                if (Long.parseLong(history[i][4])==0) {
                    rating = Config.downvote_indicator;
                } else {
                    rating = Config.upvote_indicator;
                }
                User.messagePlayer(adminName, Config.history_body
                        .replaceAll("<PLAYERNAME>", history[i][0]).replaceAll("<ADMINNAME>", history[i][1])
                        .replaceAll("<DETAILS>", history[i][2])
                        .replaceAll("<TIME>", new SimpleDateFormat("MM/dd/yy HH:mm").format(new Date(Long.parseLong(history[i][3])*1000)))
                        .replaceAll("<RATING>", rating));
            }
        }
        for (String history_footer : Config.history_footer) {
            User.messagePlayer(adminName, history_footer);
        }
    }


}
