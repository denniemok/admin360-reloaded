package com.battleasya.Admin360.util;

import com.battleasya.Admin360.Admin360;
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
    public static String isPlayerCheck;
    public static String reloadMessage;
    public static boolean useMysql;
    public static String host;
    public static String port;
    public static String database;
    public static String username;
    public static String password;
    public static List<String> commandListPlayer;
    public static List<String> commandListStaff;
    public static boolean useCooldown;
    public static int cooldownTimer;
    public static String cooldownMessage;
    public static boolean staffOnlineRequired;
    public static String createFailedNoStaff;
    public static String createFailedInQueue;
    public static String createFailedInProgress;
    public static String create_failed_require_feedback;
    public static String create_failed_anti_exploit;
    public static List<String> create_succeeded;
    public static boolean ticket_created_trigger_custom_command;
    public static String ticket_created_custom_command;
    public static List<String> ticket_created_notify_staff;
    public static String cancel_failed_no_ticket;
    public static String cancel_failed_in_progress;
    public static String cancel_failed_attending;
    public static String cancel_failed_require_feedback;
    public static String cancel_succeeded;
    public static String status_no_ticket;
    public static String status_in_queue;
    public static String status_in_progress;
    public static String status_attending;
    public static String status_require_feedback;
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
        if (!Config.version_set || !Config.version_latest) {
            File configFile = new File(plugin.getDataFolder(), "config.yml");
            boolean rename = configFile.renameTo(new File(plugin.getDataFolder(), "config_old.yml"));
            if (rename) {
                System.out.println("[Admin360-Reloaded] Renamed the old config file to config_old.yml.");
            } else {
                System.out.println("[Admin360-Reloaded] Failed to rename the old config file to config_old.yml.");
            }
            plugin.saveDefaultConfig();
        } else {
            System.out.println("[Admin360-Reloaded] config.yml is at the latest version.");
        }
    }

    /* Load config into memory */
    public void fetchConfig() {

        FileConfiguration config = plugin.getConfig();

        version = config.getString("version");

        noPermission = config.getString("no-permission");
        incorrectSyntax = config.getString("incorrect-syntax");
        isPlayerCheck = config.getString("is-player-check");
        reloadMessage = config.getString("reload-message");

        useMysql = config.getBoolean("use-mysql");
        host = config.getString("host");
        port = config.getString("port");
        database = config.getString("database");
        username = config.getString("username");
        password = config.getString("password");

        commandListPlayer = config.getStringList("command-list-player");
        commandListStaff = config.getStringList("command-list-staff");

        useCooldown = config.getBoolean("use-cooldown");
        cooldownTimer = config.getInt("cooldown-timer");
        cooldownMessage = config.getString("cooldown-message");

        staffOnlineRequired = config.getBoolean("staff-online-required");
        createFailedNoStaff = config.getString("create-failed-no-staff");

        createFailedInQueue = config.getString("create-failed-in-queue");
        createFailedInProgress = config.getString("create-failed-in-progress");
        create_failed_require_feedback = config.getString("create-failed-require-feedback");
        create_failed_anti_exploit = config.getString("create-failed-anti-exploit");

        create_succeeded = config.getStringList("create-succeeded");
        ticket_created_trigger_custom_command = config.getBoolean("ticket-created-trigger-custom-command");
        ticket_created_custom_command = config.getString("ticket-created-custom-command");

        ticket_created_notify_staff = config.getStringList("ticket-created-notify-staff");

        cancel_failed_no_ticket = config.getString("cancel-failed-no-ticket");
        cancel_failed_in_progress = config.getString("cancel-failed-in-progress");
        cancel_failed_attending = config.getString("cancel-failed-attending");
        cancel_failed_require_feedback = config.getString("cancel-failed-require-feedback");
        cancel_succeeded = config.getString("cancel-succeeded");

        status_no_ticket = config.getString("status-no-ticket");
        status_in_queue = config.getString("status-in-queue");
        status_in_progress = config.getString("status-in-progress");
        status_attending = config.getString("status-attending");
        status_require_feedback = config.getString("status-require-feedback");

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
