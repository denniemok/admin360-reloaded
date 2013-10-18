package com.vidhucraft.Admin360.datasource;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

import org.bukkit.Bukkit;

import com.vidhucraft.Admin360.entities.Admin;
import com.vidhucraft.Admin360.entities.Request;

public class SQLite_DataSource implements DataSource{
	private Connection con;
	

	@Override
	public boolean connect() {
		try {
			//Create Data folder
			new File("plugins/Admin360").mkdir();
			
			//Connect to database and create if not exist
			Class.forName("org.sqlite.JDBC");
			this.con = DriverManager.getConnection("jdbc:sqlite:plugins/Admin360/honors.db");
		} catch (ClassNotFoundException e) {
			Bukkit.getLogger().log(Level.SEVERE, "[Admin360] Couldn't find SQL Driver");
			e.printStackTrace();
			return false;
		} catch (SQLException e) {
			Bukkit.getLogger().log(Level.SEVERE, "[Admin360] Error connecting to db");
			e.printStackTrace();
			return false;
		}
		
		System.out.println("[Admin360] Connected to SQLite Database");
		return true;
	}
	
	@Override
	public boolean setUp() {
		Statement st = null;
        
        try{
        	
        	st = con.createStatement();
        	st.executeUpdate(
        			"CREATE TABLE IF NOT EXISTS Honors ("
        			+ "ID_Honor INTEGER PRIMARY KEY AUTOINCREMENT, "
        			+ "HonorFrom TEXT,"
        			+ "HonorTo TEXT,"
        			+ "Request_TimeStamp NUMERIC DEFAULT 0,"
        			+ "Honor_TimeStamp NUMERIC DEFAULT 0)"
    			);
        	
        }catch(SQLException e){
        	
        	e.printStackTrace();
        	return false;
        	
        }finally{
        	
        	close(st);
        	
        }
        
        System.out.println("[Admin360] Finished setting up Admin360 Database");
		return true;
	}
	
	@Override
	public boolean addAdminHonor(Request request){
		PreparedStatement pst = null;
		String sql = null;
		
		try{
			//Construct the SQL
			sql = "INSERT INTO Honors (HonorFrom, HonorTo, Request_TimeStamp, Honor_TimeStamp) "
			+ "VALUES (?, ?, ?, ?)";
			pst = con.prepareStatement(sql);
			pst.setString(1, request.getHandledBy().getAdminName().toLowerCase());
			pst.setString(2, request.getPlayerName().toLowerCase());
			pst.setLong(3, request.getTime());
			pst.setLong(4, System.currentTimeMillis()/1000);
			

			pst.executeUpdate();
			
			//DEBUG:check sql generated
			//System.out.println(sql);
			
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			this.close(pst);
		}
		return true;
	}

	@Override
	public int[] getAdminHonorCount(Admin admin) {
		int honor[] = new int[2];
		PreparedStatement pst = null;
        ResultSet rs = null;
        
		try{
			pst = con.prepareStatement("SELECT COUNT(ID_Honor) as count FROM Honors WHERE HonorTo = ?");
			pst.setString(1, admin.getAdminName());
			rs = pst.executeQuery();
			while(rs.next()){
				honor[1] = rs.getInt("count");
			}
			
			//Indication that query was successful
			honor[0] = 1;
		}catch(SQLException ex){
			ex.printStackTrace();
		}finally{
			close(pst);
			close(rs);
		}
		
		return honor;
	}

	@Override
	public boolean resetAdminsHonor(Admin admin) {
		PreparedStatement pst = null;
		
		try{
			pst = con.prepareStatement("DELETE FROM Honors WHERE honorTo = ?");
			pst.setString(1, admin.getAdminName());
			int rowsAffected = pst.executeUpdate();
			if(rowsAffected == 1)
				return true;
			return false;
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			close(pst);
		}
		
		return false;
	}

	@Override
	public String[][] getTopHonors(int limit) {
		String honor[][] = new String[limit][2];
		PreparedStatement pst = null;
        ResultSet rs = null;
        
		try{
			pst = con.prepareStatement("SELECT HonorTo, COUNT(HonorTo) as honorCount "
					+ "FROM Honors GROUP BY upper(HonorTo) ORDER BY honorCount DESC LIMIT ? ");
			pst.setInt(1, limit);
			rs = pst.executeQuery();
			
			int i=0;
			while(rs.next()){
				honor[i][0] = rs.getString("HonorTo");
				honor[i][1] = rs.getString("honorCount");
				i++;
			}
		}catch(SQLException ex){
			ex.printStackTrace();
		}finally{
			close(pst);
			close(rs);
		}
		
		return honor;
	}
	
	/**
     * Closes a connection to the SQL statement
     * @param con
     */
	private void close(Statement st) {
        if (st != null) {
            try {
                st.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

	/**
     * Closes a connection to the ResultSet
     * @param con
     */
    private void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException ex) {
            	ex.printStackTrace();
            }
        }
    }

    /**
     * Closes a connection to the SQL database
     * @param con
     */
    private void close(Connection con) {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException ex) {
            	ex.printStackTrace();
            }
        }
    }
}
