package com.battleasya.Admin360.commands;

import com.battleasya.Admin360.Admin360;
import com.battleasya.Admin360.util.Config;
import com.battleasya.Admin360.util.Permission;
import com.battleasya.Admin360.entities.Request;
import com.battleasya.Admin360.entities.User;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/* This class handles everything about the /ticket command. */

public class A3 implements CommandExecutor {

    private final Admin360 plugin;

    /* A3 Constructor */
    public A3 (Admin360 plugin) {
        this.plugin = plugin;
    }

    public void messageCommandList(CommandSender sender) {
        for (String commands_player : Config.commandListPlayer) {
            User.messagePlayer(sender, commands_player);
        }
        if (User.hasPermission(sender, Permission.RESPOND_TICKET, false)) {
            for (String commands_staff : Config.commandListStaff) {
                User.messagePlayer(sender, commands_staff);
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        // block command usage if the sender is not a player
        if(!(sender instanceof Player)) {
            User.messagePlayer(sender, Config.isPlayerCheck);
            return true;
        }

        if (args.length == 0) {
            messageCommandList(sender);
            return true;
        }

        if (args.length == 1) {

            if (args[0].equalsIgnoreCase("help")) {
                messageCommandList(sender);
                return true;
            }

            if (args[0].equalsIgnoreCase("create")) {
                if (User.hasPermission(sender, Permission.CREATE_TICKET, true)) {
                    if (User.hasPermission(sender, Permission.RESPOND_TICKET, false)) {
                        User.messagePlayer(sender, Config.create_failed_anti_exploit);
                        return true;
                    }
                    plugin.getRequestHandler().createTicket(sender.getName(), "NULL");
                }
                return true;
            }

            if (args[0].equalsIgnoreCase("cancel")) {
                if (User.hasPermission(sender, Permission.CREATE_TICKET, true)) {
                    plugin.getRequestHandler().cancelTicket(sender.getName());
                }
                return true;
            }

            if (args[0].equalsIgnoreCase("status")) {
                if (User.hasPermission(sender, Permission.VIEW_STATUS, true)) {
                    plugin.getRequestHandler().printTicketStatus(sender.getName());
                }
                return true;
            }

            if (args[0].equalsIgnoreCase("stats")) {
                if (User.hasPermission(sender, Permission.VIEW_STATS, true)) {
                    plugin.getRequestHandler().printTicketStats(sender.getName());
                }
                return true;
            }

            if (args[0].equalsIgnoreCase("list")) {
                if (User.hasPermission(sender, Permission.RESPOND_TICKET, true)) {
                    plugin.getRequestHandler().printQueueList(sender.getName());
                }
                return true;
            }

            if (args[0].equalsIgnoreCase("next")) {
                if (User.hasPermission(sender, Permission.RESPOND_TICKET, true)) {
                    plugin.getRequestHandler().nextTicket(sender.getName());
                }
                return true;
            }

            if (args[0].equalsIgnoreCase("tp")) {
                if (User.hasPermission(sender, Permission.TELEPORT, true)) {
                    plugin.getRequestHandler().teleportPlayer(sender.getName());
                }
                return true;
            }

            if (args[0].equalsIgnoreCase("info")) {
                if (User.hasPermission(sender, Permission.VIEW_INFO, true)) {
                    plugin.getRequestHandler().printTicketDetails(sender.getName());
                }
                return true;
            }

            if (args[0].equalsIgnoreCase("drop")) {
                if (User.hasPermission(sender, Permission.DROP_TICKET, true)) {
                    plugin.getRequestHandler().dropTicket(sender.getName());
                }
                return true;
            }

            if (args[0].equalsIgnoreCase("close")) {
                if (User.hasPermission(sender, Permission.RESPOND_TICKET, true)) {
                    plugin.getRequestHandler().closeTicket(sender.getName());
                }
                return true;
            }

            if (args[0].equalsIgnoreCase("yes")) {
                if (User.hasPermission(sender, Permission.CREATE_TICKET, true)) {
                    plugin.getRequestHandler().requestFeedback(sender.getName(), true);
                }
                return true;
            }

            if (args[0].equalsIgnoreCase("no")) {
                if (User.hasPermission(sender, Permission.CREATE_TICKET, true)) {
                    plugin.getRequestHandler().requestFeedback(sender.getName(), false);
                }
                return true;
            }

            if (args[0].equalsIgnoreCase("purge")) {
                if (User.hasPermission(sender, Permission.PURGE_TICKET, true)) {
                    plugin.getRequestHandler().purgeTicket(sender.getName());
                }
                return true;
            }

            if (args[0].equalsIgnoreCase("hpstats")) {
                if (User.hasPermission(sender, Permission.VIEW_HP_STATS, true)) {
                    String playerName = sender.getName();
                    plugin.getRequestHandler().printHonorStats(playerName, sender.getName());
                }
                return true;
            }

            if (args[0].equalsIgnoreCase("hptop")) {
                if (User.hasPermission(sender, Permission.VIEW_HP_TOP, true)) {
                    plugin.getRequestHandler().printHonorTop(sender.getName(), Config.default_leaderboard_output);
                }
                return true;
            }

            if (args[0].equalsIgnoreCase("history")) {
                if (User.hasPermission(sender, Permission.VIEW_HISTORY, true)) {
                    plugin.getRequestHandler().printHonorHistory(sender.getName(), Config.default_history_output);
                }
                return true;
            }

        }

        //simple commands with 2 arguments
        if (args.length == 2) {

            if (args[0].equalsIgnoreCase("pick")) {
                if (User.hasPermission(sender, Permission.RESPOND_TICKET, true)) {
                    plugin.getRequestHandler().pickTicket(sender.getName(), args[1]);
                }
                return true;
            }

            if (args[0].equalsIgnoreCase("redirect")) {
                if (User.hasPermission(sender, Permission.REDIRECT_TICKET, true)) {
                    plugin.getRequestHandler().redirectTicket(sender.getName(), args[1]);
                }
                return true;
            }

            if (args[0].equalsIgnoreCase("delete")) {
                if (User.hasPermission(sender, Permission.DELETE_TICKET, true)) {
                    Request.removePlayerRequest(args[1]);
                    User.messagePlayer(sender, Config.delete_message
                            .replaceAll("<PLAYERNAME>", args[1]));
                }
                return true;
            }

            if (args[0].equalsIgnoreCase("hpreset")) {
                if (User.hasPermission(sender, Permission.RESET_HP_STATS, true)) {
                    if (plugin.getDataSource().resetAdminsHonor(args[1])) {
                        User.messagePlayer(sender, Config.reset_hpstats_succeeded
                                .replaceAll("<ADMINNAME>", args[1]));
                    } else {
                        User.messagePlayer(sender, Config.reset_hpstats_failed);
                    }
                }
                return true;
            }

            if (args[0].equalsIgnoreCase("hpstats")) {
                if (User.hasPermission(sender, Permission.VIEW_HP_STATS, true)) {
                    String playerName = args[1];
                    plugin.getRequestHandler().printHonorStats(playerName, sender.getName());
                }
                return true;
            }

            if (args[0].equalsIgnoreCase("hptop")) {
                if (User.hasPermission(sender, Permission.VIEW_HP_TOP, true)) {
                    try {
                        plugin.getRequestHandler().printHonorTop(sender.getName(), Integer.parseInt(args[1]));
                    } catch (Exception e) {
                        User.messagePlayer(sender, Config.incorrectSyntax);
                    }
                }
                return true;
            }

            if (args[0].equalsIgnoreCase("history")) {
                if (User.hasPermission(sender, Permission.VIEW_HISTORY, true)) {
                    try {
                        plugin.getRequestHandler().printHonorHistory(sender.getName(), Integer.parseInt(args[1]));
                    } catch (Exception e) {
                        User.messagePlayer(sender, Config.incorrectSyntax);
                    }
                }
                return true;
            }

        }

        if (args[0].equalsIgnoreCase("create")) {
            if (User.hasPermission(sender, Permission.CREATE_TICKET, true)) {
                if (User.hasPermission(sender, Permission.RESPOND_TICKET, false)) {
                    User.messagePlayer(sender, Config.create_failed_anti_exploit);
                    return true;
                }
                StringBuilder reason = new StringBuilder();
                for (int i = 1; i < args.length; i++) {
                    reason.append(" ").append(args[i]);
                }
                plugin.getRequestHandler().createTicket(sender.getName(), reason.toString());
            }
            return true;
        }

        User.messagePlayer(sender, Config.incorrectSyntax);
        return true;

    }

}
