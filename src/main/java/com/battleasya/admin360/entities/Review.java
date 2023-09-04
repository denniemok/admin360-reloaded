package com.battleasya.admin360.entities;

import com.battleasya.admin360.Admin360;
import com.battleasya.admin360.handler.Config;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Review extends BukkitRunnable {

    private final UUID playerID;

    private final String adminName;

    private final Admin360 plugin;

    /* Collection of Review Reminders which prompt the users to rate the service */
    public static HashMap<UUID, BukkitTask> reviewReminder = new HashMap<>();

    /**
     * Creates a new reminder for the specified player name
     */
    public Review(Admin360 plugin, UUID playerID, String adminName) {
        this.plugin = plugin;
        this.playerID = playerID;
        this.adminName = adminName;
    }

    /**
     * Schedules the reminder. Player starts receiving messages after executing this
     */
    public BukkitTask runReminder(){
        int frequency = Config.review_reminder_interval * 20;
        return this.runTaskTimerAsynchronously(plugin, 20, frequency);
    }

    @Override
    public void run() {
        if (!plugin.getRequestHandler().promptFeedback(playerID, adminName)) {
            this.cancel();
        }
    }

    public static void removePlayer(UUID playerID) {

        // Remove from reviewReminder
        BukkitTask reminder = reviewReminder.remove(playerID);

        if (reminder != null) {
            reminder.cancel();
        }

    }

    public static void addToRmdLst(UUID playerID, BukkitTask reminder) {
        reviewReminder.put(playerID, reminder);
    }

    public static void clearRmdLst() {
        for (Map.Entry<UUID, BukkitTask> entry : reviewReminder.entrySet()) {
            if (entry.getValue() != null){
                entry.getValue().cancel();
            }
        }
        reviewReminder.clear();
    }

}
