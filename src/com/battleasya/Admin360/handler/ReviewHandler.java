package com.battleasya.Admin360.handler;

import com.battleasya.Admin360.Admin360;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;

public class ReviewHandler extends BukkitRunnable {

    private final UUID playerID;
    private final Admin360 plugin;

    /**
     * Creates a new reminder for the specified player name
     */
    public ReviewHandler(Admin360 plugin, UUID playerID){
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

}
