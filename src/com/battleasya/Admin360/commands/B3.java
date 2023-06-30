package com.battleasya.Admin360.commands;

import com.battleasya.Admin360.Admin360;
import com.battleasya.Admin360.entities.User;
import com.battleasya.Admin360.util.Config;
import com.battleasya.Admin360.util.Permission;
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

            if (args[0].equalsIgnoreCase("help")) {
                for (String commands_player : Config.commandListPlayer) {
                    User.messagePlayer(sender, commands_player);
                }
                if (User.hasPermission(sender, Permission.RESPOND_TICKET, false)) {
                    for (String commands_staff : Config.commandListStaff) {
                        User.messagePlayer(sender, commands_staff);
                    }
                }
                return true;
            }

            if (args[0].equalsIgnoreCase("reload")) {
                if (User.hasPermission(sender, Permission.RELOAD_CONFIG, true)) {
                    plugin.reloadConfig();
                    plugin.getConfig2().fetchConfig();
                    User.messagePlayer(sender, Config.reloadMessage);
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
        User.messagePlayer(sender, "&f[&6ADMIN360&f] Authors: &7Vidhu, Dennie");
        User.messagePlayer(sender, "&f[&6ADMIN360&f] Project: &7https://cutt.ly/bjo1zpy");
        User.messagePlayer(sender, "");
    }

}
