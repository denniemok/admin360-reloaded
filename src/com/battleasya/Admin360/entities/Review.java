package com.battleasya.Admin360.entities;

import com.battleasya.Admin360.Admin360;
import com.battleasya.Admin360.handler.Config;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Review extends BukkitRunnable {

    private final UUID playerID;
    private final Admin360 plugin;

    /* Collection of Review Reminders which prompt the users to rate the service */
    public static HashMap<UUID, BukkitTask> reviewReminder = new HashMap<>();

    /**
     * Creates a new reminder for the specified player name
     */
    public Review(Admin360 plugin, UUID playerID){
        this.plugin = plugin;
        this.playerID = playerID;
    }

    /**
     * Schedules the reminder. Player starts receiving messages after executing this
     */
    public BukkitTask runReminder(){
        int frequency = Config.reminder_frequency * 20;
        return this.runTaskTimer(plugin, 20, frequency);
    }

    @Override
    public void run() {
        if (!plugin.getRequestHandler().promptFeedback(playerID)) {
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

    public static void add2Reminder(UUID playerID, BukkitTask reminder) {
        reviewReminder.put(playerID, reminder);
    }

    public static void clearReminderList() {
        for (Map.Entry<UUID, BukkitTask> entry : reviewReminder.entrySet()) {
            if (entry.getValue() != null){
                entry.getValue().cancel();
            }
        }
        reviewReminder.clear();
    }

}
