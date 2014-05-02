package test;

import java.sql.*;

public class mssqltest {
	public static void main(String[] args) throws ClassNotFoundException {

		try {
			
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

		Connection con = null;


		con = DriverManager.getConnection("jdbc:sqlserver://192.168.0.112:1433",

		"sa", "14741");
		
		if (con == null)
			System.out.println("fail");
		else
			System.out.println("success");


		java.sql.Statement st = null;

		ResultSet rs = null;

		st = con.createStatement();

		//rs = st.executeQuery("select * from onsol");

		rs = st.executeQuery("SP_PASSWORD NULL, '1234', 'test' ");


		while (rs.next()) {
			String field1 = rs.getString(1);
			System.out.println(field1);

		}

		} catch (SQLException sqex) {

		System.out.println("SQLException: " + sqex.getMessage());

		System.out.println("SQLState: " + sqex.getSQLState());

		}


		}

}
