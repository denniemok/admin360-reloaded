package com.battleasya.Admin360.util;

import com.battleasya.Admin360.Admin360;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public enum Permissions {
    CREATE_TICKET("admin360.player.basic"),
    VIEW_STATUS("admin360.player.status"),
    VIEW_STATS("admin360.player.stats"),
    RESPOND_TICKET("admin360.staff.basic"),
    TELEPORT("admin360.staff.tp"),
    VIEW_INFO("admin360.staff.info"),
    REDIRECT_TICKET("admin360.staff.redirect"),
    DROP_TICKET("admin360.staff.drop"),
    PURGE_TICKET("admin360.staff.purge"),
    DELETE_TICKET("admin360.staff.delete"),
    VIEW_HP_STATS("admin360.staff.hpstats"),
    VIEW_HP_TOP("admin360.staff.hptop"),
    VIEW_HISTORY("admin360.staff.history"),
    RESET_HP_STATS("admin360.staff.hpreset"),
    RELOAD_CONFIG("admin360.staff.reload");

    private final String permissionNode;
    public static Admin360 plugin = Admin360.getInstance();

    Permissions(String permissionNode) {
        this.permissionNode = permissionNode;
    }

    public String getNode(){
        return this.permissionNode;
    }

    public static boolean hasPermission(CommandSender player, Permissions permission, Boolean sendMsg){
        if (!player.hasPermission(permission.getNode())) {
            if (sendMsg) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',plugin.getConfig().getString("no-permission")));
            }
            return false;
        } else {
            return true;
        }
    }

}
