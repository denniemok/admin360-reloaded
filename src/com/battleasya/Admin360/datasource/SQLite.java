package com.battleasya.Admin360.datasource;

import com.battleasya.Admin360.entities.Request;
import org.bukkit.Bukkit;

import java.sql.*;
import java.util.logging.Level;

public class SQLite implements DataSource {

	private Connection con = null;

	@Override
	public boolean connect(String host, String port, String database, String username, String password) {

		if (con != null) {
			return true;
		}

		try {
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection("jdbc:sqlite:plugins/Admin360-Reloaded/database.db");
		} catch (ClassNotFoundException e) {
			Bukkit.getLogger().log(Level.SEVERE, "[Admin360-Reloaded] Couldn't find the SQLite driver.");
			return false;
		} catch (SQLException e) {
			Bukkit.getLogger().log(Level.SEVERE, "[Admin360-Reloaded] Failed to connect to the SQLite database.");
			return false;
		}

		System.out.println("[Admin360-Reloaded] Connected to the SQLite database.");
		return true;

	}

	@Override
	public void disconnect() {
		if (con == null) {
			return;
		}

		try {
			con.close();
		} catch (SQLException e) {
			System.out.println("[Admin360-Reloaded] Failed to disconnect from the SQLite database.");
			return;
		}

		con = null;
		System.out.println("[Admin360-Reloaded] Disconnected from the SQLite database.");
	}

	@Override
	public void setUp() {
		Statement st = null;
        
        try {
        	st = con.createStatement();
        	st.executeUpdate("CREATE TABLE IF NOT EXISTS Honors ("
        			+ "ID_Honor INTEGER PRIMARY KEY AUTOINCREMENT, "
        			+ "HonorFrom TEXT,"
        			+ "HonorTo TEXT,"
        			+ "Request_TimeStamp NUMERIC DEFAULT 0,"
        			+ "Honor_TimeStamp NUMERIC DEFAULT 0,"
					+ "Reason TEXT DEFAULT NULL)");
        } catch(SQLException e) {
			System.out.println("[Admin360-Reloaded] Failed to setup the SQLite database.");
			close(st);
        	return;
        } finally {
        	close(st);
        }

        System.out.println("[Admin360-Reloaded] Finished setting up the SQLite database.");
	}

	@Override
	public void addColumn() {
		Statement st = null;

		try {
			st = con.createStatement();
			st.executeUpdate("ALTER TABLE Honors ADD COLUMN Reason TEXT DEFAULT NULL");
		} catch(SQLException e) {
			System.out.println("[Admin360-Reloaded] The database structure is at the latest version.");
			close(st);
			return;
		} finally {
			close(st);
		}

		System.out.println("[Admin360-Reloaded] Finished updating the database structure.");
	}
	
	@Override
	public void addRecord(Request request, boolean upvote) {
		PreparedStatement pst = null;
		
		try {
			pst = con.prepareStatement("INSERT INTO Honors (HonorFrom, HonorTo, Request_TimeStamp, Honor_TimeStamp, Reason) "
					+ "VALUES (?, ?, ?, ?, ?)");
			pst.setString(1, request.getPlayerName());
			pst.setString(2, request.getHandledByName());
			pst.setLong(3, request.getTimestamp());
			if (upvote) {
				pst.setLong(4, System.currentTimeMillis()/1000);
			} else {
				pst.setLong(4, 0);
			}
			pst.setString(5, request.getComment());
			pst.executeUpdate();
		} catch(SQLException e) {
			//e.printStackTrace();
		} finally {
			close(pst);
		}

	}

	@Override
	public int getAdminTicketCount(String adminName, int situation) {

		int honor = 0;
		PreparedStatement pst = null;
        ResultSet rs = null;
        
		try {
			if (situation == 1) {

				pst = con.prepareStatement("SELECT COUNT(ID_Honor) AS Total FROM Honors "
						+ "WHERE Honor_TimeStamp != 0 AND HonorTo = ?");
			} else {
				pst = con.prepareStatement("SELECT COUNT(ID_Honor) AS Total FROM Honors "
						+"WHERE Honor_TimeStamp = 0 AND HonorTo = ?");
			}
			pst.setString(1, adminName);
			rs = pst.executeQuery();
			honor = rs.getInt("Total");
		} catch(SQLException e) {
			//e.printStackTrace();
		} finally {
			close(pst);
			close(rs);
		}

		return honor;
	}

	@Override
	public int getTotalTicketCount(int situation) {
		int count = 0;
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			if (situation == 1) {

				pst = con.prepareStatement("SELECT COUNT(ID_Honor) AS Total FROM Honors");
			} else {
				pst = con.prepareStatement("SELECT COUNT(ID_Honor) AS Total FROM Honors WHERE Honor_TimeStamp != 0");
			}
			rs = pst.executeQuery();
			count = rs.getInt("Total");
		} catch(SQLException e) {
			//e.printStackTrace();
		} finally {
			close(pst);
			close(rs);
		}

		return count;
	}

	@Override
	public boolean resetAdminsHonor(String adminName) {
		PreparedStatement pst = null;
		int rowsAffected;
		
		try {
			pst = con.prepareStatement("DELETE FROM Honors WHERE honorTo = ?");
			pst.setString(1, adminName);
			rowsAffected = pst.executeUpdate();
		} catch(SQLException e) {
			//e.printStackTrace();
			close(pst);
			return false;
		} finally {
			close(pst);
		}

		return rowsAffected >= 1;
	}

	@Override
	public String[][] getTopHonors(int limit) {
		String[][] honor = new String[limit][5];
		PreparedStatement pst = null;
        ResultSet rs = null;
        
		try {
			pst = con.prepareStatement("SELECT A.HonorTo, IFNULL(j,0) AS UpVote, IFNULL(k,0) AS DownVote, (IFNULL(j,0)+IFNULL(k,0)) AS Total, (IFNULL(j,0)*100/(IFNULL(j,0)+IFNULL(k,0))) AS Percent "
					+"FROM (SELECT HonorTo, COUNT(HonorTo) AS j FROM Honors WHERE Honor_TimeStamp != 0 GROUP BY HonorTo) AS A "
					+"LEFT OUTER JOIN (SELECT HonorTo, COUNT(HonorTo) AS k FROM Honors WHERE Honor_TimeStamp = 0 GROUP BY HonorTo) AS B "
					+"ON A.HonorTo=B.HonorTo ORDER BY UpVote DESC LIMIT ?");
			pst.setInt(1, limit);
			rs = pst.executeQuery();
			int i=0;
			while(rs.next()){
				honor[i][0] = rs.getString("HonorTo");
				honor[i][1] = rs.getString("UpVote");
				honor[i][2] = rs.getString("DownVote");
				honor[i][3] = rs.getString("Total");
				honor[i][4] = rs.getString("Percent");
				i++;
			}
		} catch(SQLException e) {
			//e.printStackTrace();
		} finally {
			close(pst);
			close(rs);
		}
		
		return honor;
	}

	@Override
	public String[][] getHistory(int limit) {
		String[][] history = new String[limit][5];
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			pst = con.prepareStatement("SELECT HonorFrom, HonorTo, IFNULL(Reason, 'NULL') AS Details, Request_TimeStamp, Honor_TimeStamp FROM Honors ORDER BY Request_TimeStamp DESC LIMIT ?");
			pst.setInt(1, limit);
			rs = pst.executeQuery();
			int i=0;
			while(rs.next()){
				history[i][0] = rs.getString("HonorFrom");
				history[i][1] = rs.getString("HonorTo");
				history[i][2] = rs.getString("Details");
				history[i][3] = rs.getString("Request_TimeStamp");
				history[i][4] = rs.getString("Honor_TimeStamp");
				i++;
			}
		} catch(SQLException e){
			//e.printStackTrace();
		} finally{
			close(pst);
			close(rs);
		}

		return history;
	}

	private void close(Statement st) {
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                //e.printStackTrace();
            }
        }
    }

    private void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
            	//e.printStackTrace();
            }
        }
    }

}
