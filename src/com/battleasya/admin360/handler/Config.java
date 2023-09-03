package com.battleasya.admin360.handler;

import com.battleasya.admin360.Admin360;
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
    public static String next_failed_not_exist;
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
    public static String delete_succeeded;
    public static String delete_failed;
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

        noPermission = config.getString("general.message.no-permission");
        incorrectSyntax = config.getString("general.message.incorrect-syntax");
        reloadConfig = config.getString("general.message.reload-config");

        useMysql = config.getBoolean("datasource.mysql");
        host = config.getString("datasource.host");
        port = config.getString("datasource.port");
        database = config.getString("datasource.database");
        username = config.getString("datasource.username");
        password = config.getString("datasource.password");

        playerCommandList = config.getStringList("help.message.player-commands");
        staffCommandList = config.getStringList("help.message.staff-commands");

        cooldownEnable = config.getBoolean("create.cooldown.enable");
        cooldownInterval = config.getInt("create.cooldown.interval");
        cooldownMessage = config.getString("create.cooldown.message");

        check_staff_availability = config.getBoolean("create.check-staff-availability");

        create_failed_no_staff = config.getString("create.failed.message.no-staff");
        create_failed_in_queue = config.getString("create.failed.message.pending");
        create_failed_in_progress = config.getString("create.failed.message.attending");
        create_failed_await_feedback = config.getString("create.failed.message.completing");
        create_failed_restricted = config.getString("create.failed.message.restricted");

        create_succeeded_notify_player = config.getStringList("create.passed.message.notify-player");
        create_succeeded_notify_staff = config.getStringList("create.passed.message.notify-staff");

        create_succeeded_trigger_enable = config.getBoolean("create.passed.trigger.enable");
        create_succeeded_trigger_command = config.getString("create.passed.trigger.command");

        cancel_failed_no_ticket = config.getString("cancel.failed.message.no-ticket");
        cancel_failed_in_progress = config.getString("cancel.failed.message.attending");
        cancel_failed_await_feedback = config.getString("cancel.failed.message.completing");
        cancel_failed_attending = config.getString("cancel.failed.message.restricted");

        cancel_succeeded_notify_player = config.getString("cancel.passed.message");

        status_no_ticket = config.getString("status.message.no-ticket");
        status_in_queue = config.getString("status.message.pending");
        status_in_progress = config.getString("status.message.attending");
        status_await_feedback = config.getString("status.message.completing");
        status_attending = config.getString("status.message.restricted");

        stats_message = config.getStringList("stats.message");

        list_message = config.getString("list.message");

        use_auto_teleport = config.getBoolean("attend.auto-teleport");

        next_failed_no_ticket = config.getString("attend.failed.message.no-ticket");
        next_failed_not_exist = config.getString("attend.failed.message.not-exist");
        next_failed_attending = config.getString("attend.failed.message.attending");

        ticket_in_progress_notify_player = config.getStringList("attend.passed.message.notify-player");
        ticket_in_progress_notify_staff = config.getStringList("attend.passed.message.notify-staff");

        ticket_in_progress_trigger_custom_command = config.getBoolean("attend.passed.trigger.enable");
        ticket_in_progress_custom_command = config.getString("attend.passed.trigger.command");

        teleport_failed = config.getString("teleport.failed.message");
        teleport_succeeded = config.getString("teleport.passed.message");

        info_failed = config.getString("info.failed.message");
        info_message = config.getStringList("info.passed.message");

        redirect_failed = config.getString("transfer.failed.message");
        redirect_succeeded = config.getString("transfer.passed.message");

        drop_failed = config.getString("drop.failed.message");
        drop_message = config.getString("drop.passed.message.notify-handler");
        drop_notify_player = config.getString("drop.passed.message.notify-player");

        close_failed = config.getString("close.failed.message");
        close_succeeded = config.getString("close.passed.message");

        show_reminder = config.getBoolean("review.reminder.enable");
        reminder_frequency = config.getInt("review.reminder.interval");

        feedback_required = config.getString("review.prompt.message");

        feedback_trigger_custom_command = config.getBoolean("review.prompt.trigger.enable");
        feedback_custom_command = config.getString("review.prompt.trigger.command");

        feedback_not_required = config.getString("review.failed.message");
        feedback_received = config.getString("review.passed.message.received");

        upvote_rating_notify_staff = config.getString("review.passed.message.upvote-notify-handler");
        downvote_rating_notify_staff = config.getString("review.passed.message.downvote-notify-handler");

        purge_message = config.getStringList("purge.message");

        delete_failed = config.getString("remove.failed.message");
        delete_succeeded = config.getString("remove.passed.message");

        hpstats_message = config.getStringList("hpstats.message");

        default_leaderboard_output = config.getInt("hptop.default-limit");

        leaderboard_title = config.getStringList("hptop.message.title");
        leaderboard_body = config.getString("hptop.message.body");
        leaderboard_footer = config.getStringList("hptop.message.footer");

        reset_hpstats_failed = config.getString("hpreset.failed.message");
        reset_hpstats_succeeded = config.getString("hpreset.passed.message");

        default_history_output = config.getInt("history.default-limit");

        history_title = config.getStringList("history.message.title");
        history_body = config.getString("history.message.body");
        history_footer = config.getStringList("history.message.footer");

        upvote_indicator = config.getString("history.upvote-indicator");
        downvote_indicator = config.getString("history.downvote-indicator");

    }

}
