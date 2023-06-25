package com.battleasya.Admin360.commands;

import com.battleasya.Admin360.Admin360;
import com.battleasya.Admin360.util.Permissions;
import com.battleasya.Admin360.util.RequestHandler;
import com.battleasya.Admin360.entities.Request;
import com.battleasya.Admin360.entities.User;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class A3 implements CommandExecutor {

    //public static Admin360 plugin = Admin360.getPlugin(Admin360.class);
    private final Admin360 plugin;

    public A3 (Admin360 plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("is-player-check")));
            return true;
        }

        if (args.length == 0 || (args[0].equalsIgnoreCase("help") && args.length == 1)) {
            for (String commands_player : plugin.getConfig().getStringList("command-list-player")) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', commands_player));
            }
            if (Permissions.hasPermission(sender, Permissions.RESPOND_TICKET, false)) {
                for (String commands_staff : plugin.getConfig().getStringList("command-list-staff")) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', commands_staff));
                }
            }
            return true;
        }

        if (args.length == 1) {

            if (args[0].equalsIgnoreCase("create")) {
                if (Permissions.hasPermission(sender, Permissions.CREATE_TICKET, true)) {
                    if (Permissions.hasPermission(sender, Permissions.RESPOND_TICKET, false)) {
                        User.messagePlayer(sender.getName(), plugin.getConfig().getString("create-failed-anti-exploit"));
                        return true;
                    }
                    RequestHandler.createTicket(sender.getName(), "NULL");
                }
                return true;
            }

            if (args[0].equalsIgnoreCase("cancel")) {
                if (Permissions.hasPermission(sender, Permissions.CREATE_TICKET, true)) {
                    RequestHandler.cancelTicket(sender.getName());
                }
                return true;
            }

            if (args[0].equalsIgnoreCase("status")) {
                if (Permissions.hasPermission(sender, Permissions.VIEW_STATUS, true)) {
                    RequestHandler.printTicketStatus(sender.getName());
                }
                return true;
            }

            if (args[0].equalsIgnoreCase("stats")) {
                if (Permissions.hasPermission(sender, Permissions.VIEW_STATS, true)) {
                    RequestHandler.printTicketStats(sender.getName());
                }
                return true;
            }

            if (args[0].equalsIgnoreCase("list")) {
                if (Permissions.hasPermission(sender, Permissions.RESPOND_TICKET, true)) {
                    RequestHandler.printQueueList(sender.getName());
                }
                return true;
            }

            if (args[0].equalsIgnoreCase("next")) {
                if (Permissions.hasPermission(sender, Permissions.RESPOND_TICKET, true)) {
                    RequestHandler.nextTicket(sender.getName());
                }
                return true;
            }

            if (args[0].equalsIgnoreCase("tp")) {
                if (Permissions.hasPermission(sender, Permissions.TELEPORT, true)) {
                    RequestHandler.teleportPlayer(sender.getName());
                }
                return true;
            }

            if (args[0].equalsIgnoreCase("info")) {
                if (Permissions.hasPermission(sender, Permissions.VIEW_INFO, true)) {
                    RequestHandler.printTicketDetails(sender.getName());
                }
                return true;
            }

            if (args[0].equalsIgnoreCase("drop")) {
                if (Permissions.hasPermission(sender, Permissions.DROP_TICKET, true)) {
                    RequestHandler.dropTicket(sender.getName());
                }
                return true;
            }

            if (args[0].equalsIgnoreCase("close")) {
                if (Permissions.hasPermission(sender, Permissions.RESPOND_TICKET, true)) {
                    RequestHandler.closeTicket(sender.getName());
                }
                return true;
            }

            if (args[0].equalsIgnoreCase("yes")) {
                if (Permissions.hasPermission(sender, Permissions.CREATE_TICKET, true)) {
                    RequestHandler.requestFeedback(sender.getName(), true);
                }
                return true;
            }

            if (args[0].equalsIgnoreCase("no")) {
                if (Permissions.hasPermission(sender, Permissions.CREATE_TICKET, true)) {
                    RequestHandler.requestFeedback(sender.getName(), false);
                }
                return true;
            }

            if (args[0].equalsIgnoreCase("purge")) {
                if (Permissions.hasPermission(sender, Permissions.PURGE_TICKET, true)) {
                    RequestHandler.purgeTicket(sender.getName());
                }
                return true;
            }

            if (args[0].equalsIgnoreCase("hpstats")) {
                if (Permissions.hasPermission(sender, Permissions.VIEW_HP_STATS, true)) {
                    String playerName = sender.getName();
                    RequestHandler.printHonorStats(playerName, sender.getName());
                }
                return true;
            }

            if (args[0].equalsIgnoreCase("hptop")) {
                if (Permissions.hasPermission(sender, Permissions.VIEW_HP_TOP, true)) {
                    RequestHandler.printHonorTop(sender.getName(), plugin.getConfig().getInt("default-leaderboard-output"));
                }
                return true;
            }

            if (args[0].equalsIgnoreCase("history")) {
                if (Permissions.hasPermission(sender, Permissions.VIEW_HISTORY, true)) {
                    RequestHandler.printHonorHistory(sender.getName(), plugin.getConfig().getInt("default-history-output"));
                }
                return true;
            }

        }

        //simple commands with 2 arguments
        if (args.length == 2) {

            if (args[0].equalsIgnoreCase("pick")) {
                if (Permissions.hasPermission(sender, Permissions.RESPOND_TICKET, true)) {
                    RequestHandler.pickTicket(sender.getName(), args[1]);
                }
                return true;
            }

            if (args[0].equalsIgnoreCase("redirect")) {
                if (Permissions.hasPermission(sender, Permissions.REDIRECT_TICKET, true)) {
                    RequestHandler.redirectTicket(sender.getName(), args[1]);
                }
                return true;
            }

            if (args[0].equalsIgnoreCase("delete")) {
                if (Permissions.hasPermission(sender, Permissions.DELETE_TICKET, true)) {
                    Request.removePlayerRequest(args[1]);
                    User.messagePlayer(sender.getName(), plugin.getConfig().getString("delete-message")
                            .replaceAll("<PLAYERNAME>", args[1]));
                }
                return true;
            }

            if (args[0].equalsIgnoreCase("hpreset")) {
                if (Permissions.hasPermission(sender, Permissions.RESET_HP_STATS, true)) {
                    if (Admin360.ds.resetAdminsHonor(args[1])) {
                        User.messagePlayer(sender.getName(), plugin.getConfig().getString("reset-hpstats-succeeded")
                                .replaceAll("<ADMINNAME>", args[1]));
                    } else {
                        User.messagePlayer(sender.getName(), plugin.getConfig().getString("reset-hpstats-failed"));
                    }
                }
                return true;
            }

            if (args[0].equalsIgnoreCase("hpstats")) {
                if (Permissions.hasPermission(sender, Permissions.VIEW_HP_STATS, true)) {
                    String playerName = args[1];
                    RequestHandler.printHonorStats(playerName, sender.getName());
                }
                return true;
            }

            if (args[0].equalsIgnoreCase("hptop")) {
                if (Permissions.hasPermission(sender, Permissions.VIEW_HP_TOP, true)) {
                    try {
                        RequestHandler.printHonorTop(sender.getName(), Integer.parseInt(args[1]));
                    } catch (Exception e) {
                        User.messagePlayer(sender.getName(), plugin.getConfig().getString("incorrect-syntax"));
                    }
                }
                return true;
            }

            if (args[0].equalsIgnoreCase("history")) {
                if (Permissions.hasPermission(sender, Permissions.VIEW_HISTORY, true)) {
                    try {
                        RequestHandler.printHonorHistory(sender.getName(), Integer.parseInt(args[1]));
                    } catch (Exception e) {
                        User.messagePlayer(sender.getName(), plugin.getConfig().getString("incorrect-syntax"));
                    }
                }
                return true;
            }

        }

        if (args[0].equalsIgnoreCase("create")) {
            if (Permissions.hasPermission(sender, Permissions.CREATE_TICKET, true)) {
                if (Permissions.hasPermission(sender, Permissions.RESPOND_TICKET, false)) {
                    User.messagePlayer(sender.getName(), plugin.getConfig().getString("create-failed-anti-exploit"));
                    return true;
                }
                StringBuilder reason = new StringBuilder();
                for (int i = 1; i < args.length; i++) {
                    reason.append(" ").append(args[i]);
                }
                RequestHandler.createTicket(sender.getName(), reason.toString());
            }
            return true;
        }

        User.messagePlayer(sender.getName(), plugin.getConfig().getString("incorrect-syntax"));
        return true;

    }

}
