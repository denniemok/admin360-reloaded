package com.battleasya.Admin360.handler;

import com.battleasya.Admin360.Admin360;
import com.battleasya.Admin360.util.Config;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class ReviewHandler extends BukkitRunnable {

    private final String username;
    private final boolean showReminder;
    private final Admin360 plugin;

    /**
     * Creates a new reminder for the specified player name
     */
    public ReviewHandler(Admin360 plugin, String username){
        this.plugin = plugin;
        this.username = username;
        this.showReminder = Config.show_reminder;
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
        plugin.getRequestHandler().promptFeedback(this.username);
        if(!showReminder) {
            this.cancel();
        }
    }

}
