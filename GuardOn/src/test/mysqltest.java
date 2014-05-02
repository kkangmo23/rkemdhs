package test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;


public class mysqltest {

public static void main(String[] args) throws ClassNotFoundException {

try {

Connection con = null;

Class.forName("com.mysql.jdbc.Driver");
con = DriverManager.getConnection("jdbc:mysql://192.168.0.118:3306/kkm",

"root", "14741");


java.sql.Statement st = null;

ResultSet rs = null;

st = con.createStatement();

rs = st.executeQuery("SHOW DATABASES");


if (st.execute("SHOW DATABASES")) {

rs = st.getResultSet();

}


while (rs.next()) {

String str = rs.getNString(1);

System.out.println(str);

}

} catch (SQLException sqex) {

System.out.println("SQLException: " + sqex.getMessage());

System.out.println("SQLState: " + sqex.getSQLState());

}


}

}