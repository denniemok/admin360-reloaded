package com.battleasya.admin360.commands;

import com.battleasya.admin360.Admin360;
import com.battleasya.admin360.entities.User;
import com.battleasya.admin360.handler.Config;
import com.battleasya.admin360.handler.Permission;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/* This class handles everything about the /admin360 command. */

public class B3 implements CommandExecutor {

    private final Admin360 plugin;

    /* B3 Constructor */
    public B3(Admin360 plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length == 0) {
            printInfo(sender);
            return true;
        }

        if (args.length == 1) {

            switch (args[0].toLowerCase()) {

                case "info":
                    printInfo(sender);
                    return true;

                case "help":
                    for (String message : Config.playerCommandList) {
                        User.messagePlayer(sender, message);
                    }
                    if (User.hasPermission(sender, Permission.ATTEND_TICKET, false)) {
                        for (String message : Config.staffCommandList) {
                            User.messagePlayer(sender, message);
                        }
                    }
                    return true;

                case "reload":
                    if (User.hasPermission(sender, Permission.RELOAD_CONFIG, true)) {
                        plugin.reloadConfig();
                        plugin.config.fetchConfig();
                        User.messagePlayer(sender, Config.reloadConfig);
                    }
                    return true;

            }

        }

        User.messagePlayer(sender, Config.incorrectSyntax);
        return true;

    }

    public void printInfo(CommandSender sender) {

        User.messagePlayer(sender, "");
        User.messagePlayer(sender, "&f[&6ADMIN360&f] Version: &7" + plugin.getDescription().getVersion());
        User.messagePlayer(sender, "&f[&6ADMIN360&f] Authors: &7" + plugin.getDescription().getAuthors().get(0));
        User.messagePlayer(sender, "&f[&6ADMIN360&f] Project: &7" + plugin.getDescription().getWebsite());
        User.messagePlayer(sender, "");

    }

}
