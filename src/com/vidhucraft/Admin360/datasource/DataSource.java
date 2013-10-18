package com.vidhucraft.Admin360.datasource;

import java.sql.ResultSet;

import com.vidhucraft.Admin360.entities.Admin;
import com.vidhucraft.Admin360.entities.Request;

public interface DataSource {

	/**
	 * Sets up the database
	 * @return
	 */
	public boolean setUp();
	
	/**
	 * Connects to the database
	 * @return true if successful
	 */
	public boolean connect();
	
	/**
	 * Modifies an admin's honor points. Honor points can be incremented or
	 * decremented by specifying a positive or negative value respectively
	 * @param adminName Admin's name
	 * @param ammount Amount to increment or decrement
	 * @return true if modification was successful
	 */
	public boolean addAdminHonor(Request request);
	
	/**
	 * Returns the number of honors the specified admin has
	 * @param adminName Admin's name
	 * @return double array. If first element is 1 then query was successful<br/>
	 *  second element contains honor count
	 */
	public int[] getAdminHonorCount(Admin admin);
	
	/**
	 * Sets an admin's honor back to zero
	 * @param adminName Admin's name
	 * @return true if reset was successful
	 */
	public boolean resetAdminsHonor(Admin admin);
	
	
	/**
	 * Gets a list of the top admins along with their honro points
	 * @param limit top x admins
	 * @return a string 2d array in the form of String[name, points]
	 */
	public String[][] getTopHonors(int limit);
}
