package com.battleasya.Admin360.datasource;

import com.battleasya.Admin360.entities.Request;

public interface DataSource {
	
	boolean connect(String host, String port, String database, String username, String password);
	void disconnect();

	void setUp();
	void addColumn();
	
	void addRecord(Request request, boolean upvote);
	
	int getAdminTicketCount(String adminName, int situation);
	int getTotalTicketCount(int situation);

	boolean resetAdminsHonor(String adminName);

	String[][] getTopHonors(int limit);
	String[][] getHistory(int limit);

}
