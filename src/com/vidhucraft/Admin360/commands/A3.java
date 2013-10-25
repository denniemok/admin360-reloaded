package com.vidhucraft.Admin360.commands;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.vidhucraft.Admin360.Admin360;
import com.vidhucraft.Admin360.Permissions;
import com.vidhucraft.Admin360.RequestHandler;
import com.vidhucraft.Admin360.entities.Admin;
import com.vidhucraft.Admin360.entities.Request;
import com.vidhucraft.Admin360.entities.User;

/**
 * This class is responsible for executing all commands
 * that start with <pre>a3</pre> eg <pre>/a3 request</pre>
 * @author Vidhu
 *
 */
public class A3 implements CommandExecutor{
	private Admin360 admin360;
	
	/**
	 * Creates a new Command executer for commands <pre>a3</pre>
	 * @param admin360 Admin360 this plugin
	 */
	public A3(Admin360 admin360){
		this.admin360 = admin360;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		//helpme command
		if(cmd.getName().equalsIgnoreCase("helpme")){
			ArrayList<String> newargs = new ArrayList<>();
			newargs.add("HELP");
			for(String arg: args){
				newargs.add(arg);
			}
			a3Executer(sender, label, newargs.toArray(new String[newargs.size()-1]));
			return true;
		}
		
		//a3 commands
		if(cmd.getName().equalsIgnoreCase("a3") && args.length > 0)
			return a3Executer(sender, label, args);
		
		return false;
	}

	public boolean a3Executer(CommandSender sender, String label, String[] args){
		A3Enum arg = A3Enum.getEnum(args[0]);

		
		if(arg == A3Enum.HELP){
			//Check if player has permission
			if(!Permissions.hasPermission(sender, Permissions.Help, true))
				return true;
			
			//Check if reason exists
			String reason = null;
			if(args.length > 1){
				reason = "";
				for(int i=1;i<args.length;i++){
					reason += " " + args[i];
				}
			}
			//Should be a player
			if(!isPlayer(sender, true))
				return true;
			
			
			RequestHandler.addPlayerInQueue(sender.getName(), reason);
			return true;
		}else if(arg == A3Enum.NEXT){
			//Check if player has permission
			if(!Permissions.hasPermission(sender, Permissions.Help, true))
				return true;
			//Should be a player
			if(!isPlayer(sender, true))
				return true;
			
			RequestHandler.honorNextRequest(new Admin(sender.getName()));
			return true;
		}else if(arg == A3Enum.CLOSE){
			//Check if player has permission
			if(!Permissions.hasPermission(sender, Permissions.Help, true))
				return true;
			//Should be a player
			if(!isPlayer(sender, true))
				return true;
			
			RequestHandler.attemptCloseRequest(new Admin(sender.getName()));
			return true;
		}else if(arg == A3Enum.YES){
			//Check if player has permission
			if(!Permissions.hasPermission(sender, Permissions.Help, true))
				return true;
			//Should be a player
			if(!isPlayer(sender, true))
				return true;
			
			RequestHandler.playerRequestRating((Player)sender, true);
			return true;
		}else if(arg == A3Enum.NO){
			//Check if player has permission
			if(!Permissions.hasPermission(sender, Permissions.Help, true))
				return true;
			//Should be a player
			if(!isPlayer(sender, true))
				return true;
			
			RequestHandler.playerRequestRating((Player)sender, false);
			return true;
		}else if(arg == A3Enum.GET){
			//Check if player has permission
			if(!Permissions.hasPermission(sender, Permissions.Stats, true))
				return true;
			
			String playername = sender.getName();
			if(args.length == 2)
				playername = args[1];
			int honorCount = Admin360.ds.getAdminHonorCount(new Admin(playername.toLowerCase()))[1];
			User.messagePlayer(sender, playername + " has " + ChatColor.RED + honorCount
					+ ChatColor.GREEN + " honors");
			return true;
		}else if(arg == A3Enum.TOP){
			//Check if player has permission
			if(!Permissions.hasPermission(sender, Permissions.Stats, true))
				return true;
			
			int limit = 5;
			if(args.length == 2)
				limit = Integer.parseInt(args[1]);
			printTopHonors(sender, limit);
			return true;
		}else if(arg == A3Enum.PURGE){
			//Check if player has permission
			if(!Permissions.hasPermission(sender, Permissions.PurgeRequest, true))
				return true;
			
			int ammountPurged = RequestHandler.purgeAllPendingRequests();
			User.messagePlayers("All " + ChatColor.RED + ammountPurged 
					+ ChatColor.GREEN + " pending requests have been purged");
			return true;
		}else if(arg == A3Enum.CANCEL){
			//Check if player has permission
			if(!Permissions.hasPermission(sender, Permissions.Help, true))
				return true;
			
			RequestHandler.cancelRequest(sender.getName());
			return true;
		}else if(arg == A3Enum.STATUS){
			//Check if player has permission
			if(!Permissions.hasPermission(sender, Permissions.Status, true))
				return true;
			
			RequestHandler.informPlayerRequestStatus(sender.getName());
			return true;
		}else if(arg == A3Enum.COUNT){
			//Check if player has permission
			if(!Permissions.hasPermission(sender, Permissions.Count, true))
				return true;
			
			RequestHandler.getRequestsCount(sender.getName());
			return true;
		}
		
		//If no commands a recognized. Return false
		return false;
	}
	
	/**
	 * Prints the top admins honors in ascending order
	 * @param sender
	 * @param limit
	 */
	public static void printTopHonors(CommandSender sender, int limit){
		String[][] honors = Admin360.ds.getTopHonors(limit);
		
		User.messagePlayer(sender, "Top admin honors");
		for(int i=0;i<limit;i++){
			if(honors[i][0] != null)
				User.messagePlayer(sender, "    " + honors[i][0] + ": "
					+ ChatColor.RED + honors[i][1]);
		}
	}
	
	/**
	 * Checks to see if the CommandSender is a player is not
	 * @param sender CommandSender
	 * @param sendMsg Send a message telling the CommandSender that he need to be a player to
	 * execute the command
	 * @return
	 */
	public static boolean isPlayer(CommandSender sender, boolean sendMsg){
		boolean isplayer = sender instanceof Player;
		if(!isplayer)
			sender.sendMessage("You have to be a player to execute this command");
		return isplayer;
	}
}
