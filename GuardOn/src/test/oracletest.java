package test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class oracletest {
	public static Connection getConnection() throws ClassNotFoundException{
		
	Class.forName("oracle.jdbc.driver.OracleDriver");	
	Connection con = null;
	try {
	String dbUrl = "jdbc:oracle:thin:@192.168.0.123:1521:orcl";
	String dbId = "system";
	String dbPwd = "Osos14741";
	
	con = DriverManager.getConnection(dbUrl,dbId,dbPwd);	
	}catch(Exception e){
		e.printStackTrace();
		}
	return con;
	}

public static void main(String[] args) throws ClassNotFoundException{
	Connection con=oracletest.getConnection();
			if(con != null){
				System.out.println("success");
				try{
					con.close();
				}catch (SQLException e){
					e.printStackTrace();
			}
		}
	}
}