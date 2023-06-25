package com.battleasya.Admin360.listener;

import com.battleasya.Admin360.util.Permissions;
import com.battleasya.Admin360.entities.Admin;
import com.battleasya.Admin360.entities.Request;
import com.battleasya.Admin360.entities.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent playerjoinevent){
        Player player = playerjoinevent.getPlayer();
        if(Permissions.hasPermission(player, Permissions.RESPOND_TICKET, false)){
            Admin.adminsOnline.add(player.getName());
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent playerquitevent){
        Player player = playerquitevent.getPlayer();
        Admin.adminsOnline.remove(player.getName());
        Request.removePlayerRequest(player.getName());
        Request.requestInProgress.remove(player.getName());
        User.coolDown.remove(player.getName());
    }
}
