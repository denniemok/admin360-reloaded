package com.vidhucraft.Admin360.datasource;

public interface DataSource {

	/**
	 * Modifies an admin's honor points. Honor points can be incremented or
	 * decremented by specifying a positive or negative value respectively
	 * @param adminName Admin's name
	 * @param ammount Amount to increment or decrement
	 * @return true if modification was successful
	 */
	public boolean modifyAdminHonor(String adminName, double ammount);
	
	/**
	 * Returns the number of honors the specified admin has
	 * @param adminName Admin's name
	 * @return double which represent the number of honor points
	 */
	public double getAdminHonor(String adminName);
	
	/**
	 * Sets an admin's honor back to zero
	 * @param adminName Admin's name
	 * @return true if reset was successful
	 */
	public boolean resetAdminsHonor(String adminName);
	
	/**
	 * Sets all admin's honor to zero
	 * @return true of reset was successful
	 */
	public boolean resetAllHonors();
	
	/**
	 * Gets a list of the top admins along with their honro points
	 * @param limit top x admins
	 * @return a string 2d array in the form of String[name, points]
	 */
	public String[][] topHonors(int limit);
}
