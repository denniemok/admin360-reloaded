package com.battleasya.Admin360.handler;

import com.battleasya.Admin360.Admin360;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.List;

public class Config {

    private final Admin360 plugin;

    /* Config Constructor */
    public Config(Admin360 plugin) {
        this.plugin = plugin;
    }

    public static boolean version_set;
    public static boolean version_latest;
    public static String version;
    public static String noPermission;
    public static String incorrectSyntax;
    public static String reloadConfig;
    public static boolean useMysql;
    public static String host;
    public static String port;
    public static String database;
    public static String username;
    public static String password;
    public static List<String> playerCommandList;
    public static List<String> staffCommandList;
    public static boolean cooldownEnable;
    public static int cooldownInterval;
    public static String cooldownMessage;
    public static boolean check_staff_availability;
    public static String create_failed_no_staff;
    public static String create_failed_in_queue;
    public static String create_failed_in_progress;
    public static String create_failed_await_feedback;
    public static String create_failed_restricted;
    public static List<String> create_succeeded_notify_player;
    public static boolean create_succeeded_trigger_enable;
    public static String create_succeeded_trigger_command;
    public static List<String> create_succeeded_notify_staff;
    public static String cancel_failed_no_ticket;
    public static String cancel_failed_in_progress;
    public static String cancel_failed_attending;
    public static String cancel_failed_await_feedback;
    public static String cancel_succeeded_notify_player;
    public static String status_no_ticket;
    public static String status_in_queue;
    public static String status_in_progress;
    public static String status_attending;
    public static String status_await_feedback;
    public static List<String> stats_message;
    public static String list_message;
    public static String next_failed_no_ticket;
    public static String next_failed_attending;
    public static boolean use_auto_teleport;
    public static List<String> ticket_in_progress_notify_player;
    public static boolean ticket_in_progress_trigger_custom_command;
    public static String ticket_in_progress_custom_command;
    public static List<String> ticket_in_progress_notify_staff;
    public static String teleport_failed;
    public static String teleport_succeeded;
    public static String info_failed;
    public static List<String> info_message;
    public static String redirect_failed;
    public static String redirect_succeeded;
    public static String drop_failed;
    public static String drop_message;
    public static String drop_notify_player;
    public static String close_failed;
    public static String close_succeeded;
    public static boolean show_reminder;
    public static int reminder_frequency;
    public static String feedback_required;
    public static boolean feedback_trigger_custom_command;
    public static String feedback_custom_command;
    public static String feedback_not_required;
    public static String feedback_received;
    public static String upvote_rating_notify_staff;
    public static String downvote_rating_notify_staff;
    public static List<String> purge_message;
    public static String delete_message;
    public static List<String> hpstats_message;
    public static int default_leaderboard_output;
    public static List<String> leaderboard_title;
    public static String leaderboard_body;
    public static List<String> leaderboard_footer;
    public static String reset_hpstats_failed;
    public static String reset_hpstats_succeeded;
    public static int default_history_output;
    public static List<String> history_title;
    public static String history_body;
    public static String upvote_indicator;
    public static String downvote_indicator;
    public static List<String> history_footer;

    /* Init checking */
    public void initConfig() {

        FileConfiguration config = plugin.getConfig();

        version_set = config.isSet("version");
        version_latest = config.getString("version").equalsIgnoreCase(plugin.getDescription().getVersion());

    }

    public void checkConfig() {

        if (!version_set || !version_latest) {

            File configFile = new File(plugin.getDataFolder(), "config.yml");

            boolean rename = configFile.renameTo(new File(plugin.getDataFolder(), "config_old.yml"));

            if (rename) {
                Bukkit.getLogger().info("[Admin360-Reloaded] Renamed the old config file to config_old.yml.");
            } else {
                Bukkit.getLogger().severe("[Admin360-Reloaded] Failed to rename the old config file to config_old.yml.");
            }

            plugin.saveDefaultConfig();

        } else {
            Bukkit.getLogger().info("[Admin360-Reloaded] config.yml is at the latest version.");
        }

    }

    /* Load config into memory */
    public void fetchConfig() {

        FileConfiguration config = plugin.getConfig();

        version = config.getString("version");

        noPermission = config.getString("general.no-permission");
        incorrectSyntax = config.getString("general.incorrect-syntax");
        reloadConfig = config.getString("general.reload-config");

        useMysql = config.getBoolean("datasource.mysql");
        host = config.getString("datasource.host");
        port = config.getString("datasource.port");
        database = config.getString("datasource.database");
        username = config.getString("datasource.username");
        password = config.getString("datasource.password");

        playerCommandList = config.getStringList("help.player-commands");
        staffCommandList = config.getStringList("help.staff-commands");

        cooldownEnable = config.getBoolean("create.cooldown.enable");
        cooldownInterval = config.getInt("create.cooldown.interval");
        cooldownMessage = config.getString("create.cooldown.message");

        check_staff_availability = config.getBoolean("create.check-staff-availability");

        create_failed_no_staff = config.getString("create.failed.message.no-staff");
        create_failed_in_queue = config.getString("create.failed.message.in-queue");
        create_failed_in_progress = config.getString("create.failed.message.in-progress");
        create_failed_await_feedback = config.getString("create.failed.message.await-feedback");
        create_failed_restricted = config.getString("create.failed.message.restricted");

        create_succeeded_notify_player = config.getStringList("create.succeeded.message.notify-player");
        create_succeeded_notify_staff = config.getStringList("create.succeeded.message.notify-staff");
        create_succeeded_trigger_enable = config.getBoolean("create.succeeded.trigger.enable");
        create_succeeded_trigger_command = config.getString("create.succeeded.trigger.command");

        cancel_failed_no_ticket = config.getString("cancel.failed.message.no-ticket");
        cancel_failed_in_progress = config.getString("cancel.failed.message.in-progress");
        cancel_failed_attending = config.getString("cancel.failed.message.attending");
        cancel_failed_await_feedback = config.getString("cancel.failed.message.await-feedback");
        cancel_succeeded_notify_player = config.getString("cancel.succeeded.message.notify-player");

        status_no_ticket = config.getString("status.message.no-ticket");
        status_in_queue = config.getString("status.message.in-queue");
        status_in_progress = config.getString("status.message.in-progress");
        status_attending = config.getString("status.message.attending");
        status_await_feedback = config.getString("status.message.await-feedback");

        stats_message = config.getStringList("stats-message");

        list_message = config.getString("list-message");

        next_failed_no_ticket = config.getString("next-failed-no-ticket");
        next_failed_attending = config.getString("next-failed-attending");

        use_auto_teleport = config.getBoolean("use-auto-teleport");

        ticket_in_progress_notify_player = config.getStringList("ticket-in-progress-notify-player");
        ticket_in_progress_trigger_custom_command = config.getBoolean("ticket-in-progress-trigger-custom-command");
        ticket_in_progress_custom_command = config.getString("ticket-in-progress-custom-command");

        ticket_in_progress_notify_staff = config.getStringList("ticket-in-progress-notify-staff");

        teleport_failed = config.getString("teleport-failed");
        teleport_succeeded = config.getString("teleport-succeeded");

        info_failed = config.getString("info-failed");
        info_message = config.getStringList("info-message");

        redirect_failed = config.getString("redirect-failed");
        redirect_succeeded = config.getString("redirect-succeeded");

        drop_failed = config.getString("drop-failed");
        drop_message = config.getString("drop-message");
        drop_notify_player = config.getString("drop-notify-player");

        close_failed = config.getString("close-failed");
        close_succeeded = config.getString("close-succeeded");

        show_reminder = config.getBoolean("show-reminder");
        reminder_frequency = config.getInt("reminder-frequency");

        feedback_required = config.getString("feedback-required");
        feedback_trigger_custom_command = config.getBoolean("feedback-trigger-custom-command");
        feedback_custom_command = config.getString("feedback-custom-command");

        feedback_not_required = config.getString("feedback-not-required");
        feedback_received = config.getString("feedback-received");

        upvote_rating_notify_staff = config.getString("upvote-rating-notify-staff");
        downvote_rating_notify_staff = config.getString("downvote-rating-notify-staff");

        purge_message = config.getStringList("purge-message");
        delete_message = config.getString("delete-message");

        hpstats_message = config.getStringList("hpstats-message");
        default_leaderboard_output = config.getInt("default-leaderboard-output");

        leaderboard_title = config.getStringList("leaderboard-title");
        leaderboard_body = config.getString("leaderboard-body");
        leaderboard_footer = config.getStringList("leaderboard-footer");

        reset_hpstats_failed = config.getString("reset-hpstats-failed");
        reset_hpstats_succeeded = config.getString("reset-hpstats-succeeded");

        default_history_output = config.getInt("default-history-output");
        history_title = config.getStringList("history-title");
        history_body = config.getString("history-body");
        upvote_indicator = config.getString("upvote-indicator");
        downvote_indicator = config.getString("downvote-indicator");
        history_footer = config.getStringList("history-footer");

    }

}
