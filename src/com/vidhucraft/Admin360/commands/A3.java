package com.vidhucraft.Admin360.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.vidhucraft.Admin360.Admin360;

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
			return true;
		case NEXT:
			return true;
		case CLOSE:
			return true;
		case YES:
			return true;
		case NO:
			return true;
		case GET:
			return true;
		case TOP:
			return true;
		case PURGE:
			return true;
		case RELOAD:
			return true;
			
		default:
			break;
		}
		return false;
	}
}
