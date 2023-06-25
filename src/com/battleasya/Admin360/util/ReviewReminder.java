package com.battleasya.Admin360.util;

import com.battleasya.Admin360.Admin360;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class ReviewReminder extends BukkitRunnable {

    private final String username;
    private final boolean showReminder;
    private final static Admin360 plugin = Admin360.getInstance();

    /**
     * Creates a new reminder for the specified player name
     */
    public ReviewReminder(String username){
        this.username = username;
        this.showReminder = plugin.getConfig().getBoolean("show-reminder");
    }

    /**
     * Schedules the reminder. Player starts receiving messages after executing this
     */
    public BukkitTask runReminder(){
        int frequency = plugin.getConfig().getInt("reminder-frequency")*20;
        return this.runTaskTimer(plugin, 20, frequency);
    }

    @Override
    public void run() {
        RequestHandler.promptFeedback(this.username);
        if(!showReminder) {
            this.cancel();
        }
    }

}
