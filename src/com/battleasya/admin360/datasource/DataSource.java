package com.battleasya.admin360.datasource;

import com.battleasya.admin360.entities.Request;

public interface DataSource {
	
	void connect(String host, String port, String database, String username, String password);

	void disconnect();

	void setUp();
	
	void addRecord(Request request, boolean upvote);
	
	int getAdminTicketCount(String adminName, int situation);

	int getTotalTicketCount(int situation);

	boolean resetAdminsHonor(String adminName);

	String[][] getTopHonors(int limit);

	String[][] getHistory(int limit);

}
