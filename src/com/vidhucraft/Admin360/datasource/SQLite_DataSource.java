package com.vidhucraft.Admin360.datasource;

public class SQLite_DataSource implements DataSource{

	@Override
	public boolean setUp() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean connect() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean modifyAdminHonor(String adminName, double ammount){
		return false;
	}

	@Override
	public double getAdminHonor(String adminName) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean resetAdminsHonor(String adminName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean resetAllHonors() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String[][] topHonors(int limit) {
		// TODO Auto-generated method stub
		return null;
	}
}
