package com.vidhucraft.Admin360.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.vidhucraft.Admin360.Admin360;
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
			return true;
		}
		
		//a3 commands
		if(cmd.getName().equalsIgnoreCase("a3"))
			return a3Executer(sender, label, args);
		
		return false;
	}

	public boolean a3Executer(CommandSender sender, String label, String[] args){
		switch (A3Enum.valueOf(args[0].toUpperCase())) {
		case HELP:
			RequestHandler.addPlayerInQueue(sender.getName());
			return true;
		case NEXT:
			Admin admin = new Admin(sender.getName());
			RequestHandler.honorNextRequest(admin);
			return true;
		case CLOSE:
			RequestHandler.attemptCloseRequest(new Admin(sender.getName()));
			return true;
		case YES:
			RequestHandler.playerRequestRating((Player)sender, true);
			return true;
		case NO:
			RequestHandler.playerRequestRating((Player)sender, true);
			return true;
		case GET:
			String playername = sender.getName();
			if(args.length == 2)
				playername = args[1];
			int honorCount = Admin360.ds.getAdminHonorCount(new Admin(playername.toLowerCase()))[1];
			User.messagePlayer(sender, playername + " has " + ChatColor.RED + honorCount
					+ ChatColor.GREEN + " honors");
			return true;
		case TOP:
			int limit = 5;
			if(args.length == 2)
				limit = Integer.parseInt(args[1]);
			printTopHonors(sender, limit);
			return true;
		case PURGE:
			int ammountPurged = RequestHandler.purgeAllPendingRequests();
			User.messagePlayers("All " + ChatColor.RED + ammountPurged 
					+ ChatColor.GREEN + " pending requests have been purged");
			return true;
		case RELOAD:
			return true;
			
		default:
			return false;
		}
	}
	
	public static void printTopHonors(CommandSender sender, int limit){
		String[][] honors = Admin360.ds.getTopHonors(limit);
		
		User.messagePlayer(sender, "Top admin honors");
		for(int i=0;i<limit;i++){
			if(honors[i][0] != null)
				User.messagePlayer(sender, "    " + honors[i][0] + ": "
					+ ChatColor.RED + honors[i][1]);
		}
	}
}
