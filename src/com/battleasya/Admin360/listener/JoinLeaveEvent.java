package com.battleasya.Admin360.listener;

import com.battleasya.Admin360.entities.Review;
import com.battleasya.Admin360.handler.Permission;
import com.battleasya.Admin360.entities.Admin;
import com.battleasya.Admin360.entities.Request;
import com.battleasya.Admin360.entities.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class JoinLeaveEvent implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event){

        Player player = event.getPlayer();

        if (User.hasPermission(player, Permission.ATTEND_TICKET, false)){
            Admin.addAdmin(player.getUniqueId());
        }

    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event){

        UUID playerID = event.getPlayer().getUniqueId();

        Request.removeAdmin(playerID);
        Admin.removeAdmin(playerID);

        Request.removePlayer(playerID);
        // User.removePlayer(playerID);

        Review.removePlayer(playerID);

    }

}
