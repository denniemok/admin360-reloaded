package com.vidhucraft.Admin360.entities;

import java.util.List;
import java.util.ArrayList;

/**
 * This class represents the Admins in the server. The static member
 * <pre>adminsOnline</pre> lists all the admins online on the server at the moment
 * When an admin joins the server, an instance of this class is created
 * and is added the the <pre>adminsOnline</pre> List
 * @author Vidhu
 *
 */
public class Admin {
	//Static members
	/**
	 * A list containing all the admins online in the server
	 */
	public static List<Admin> adminsOnline = new ArrayList<Admin>();
	
	//non Static members
	private String adminName;

	/**
	 * Creates a new admin object
	 * @param name String Admin's name
	 */
	public Admin(String name){
		this.adminName = name;
	}
	
	/**
	 * @return String Admin's name
	 */
	public String getAdminName() {
		return adminName;
	}

	/**
	 * Sets the admin's name
	 * @param adminName Admin's name
	 * @throws Exception Thrown if admin's name is null
	 */
	public void setAdminName(String adminName) throws Exception {
		if(adminName.equals(""))
			throw new Exception("Admin name cannot be empty");
		this.adminName = adminName;
	}
}
