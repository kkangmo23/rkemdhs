package com.guardon.view;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.TimerTask;

import com.guardon.user.domain.User;
import com.mysql.jdbc.Statement;

public class MakeOTP extends TimerTask {

	UserController uc = new UserController();
	private String userOtp;
	private String serverName;
	private String ipAddress;
	private String serverId;
	private String serverPwd;
	private String connectId;
	private String dbName;
	private String connectType;
	private String serverOS;

	public String getUserOtp() {
		return userOtp;
	}

	public void setUserOtp(String userOtp) {
		this.userOtp = userOtp;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	public String getServerPwd() {
		return serverPwd;
	}

	public void setServerPwd(String serverPwd) {
		this.serverPwd = serverPwd;
	}

	public String getconnectId() {
		return connectId;
	}

	public void setconnectId(String connectId) {
		this.connectId = connectId;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getConnectType() {
		return connectType;
	}

	public void setConnectType(String connectType) {
		this.connectType = connectType;
	}

	public String getConnectId() {
		return connectId;
	}

	public void setConnectId(String connectId) {
		this.connectId = connectId;
	}

	public String getServerOS() {
		return serverOS;
	}

	public void setServerOS(String serverOS) {
		this.serverOS = serverOS;
	}

	public void run() {

		User user = new User();

		userOtp = firstOtp();
		System.out.println(userOtp);
		System.out.println(connectId);
		try {
			switch (connectType) {

			case "oracle":

				Class.forName("oracle.jdbc.driver.OracleDriver");

				Connection conn = null;
				conn = DriverManager.getConnection("jdbc:oracle:thin:@"
						+ ipAddress + ":orcl", serverId, serverPwd);

				java.sql.PreparedStatement psmt = null;
				psmt = conn.prepareStatement("alter user " + connectId
						+ " identified by \"" + userOtp + "\"");
				System.out.println("alter user " + connectId
						+ " identified by \"" + userOtp + "\"");

				psmt.executeUpdate();

				psmt.close();
				conn.close();
				break;

			case "mssql":

				System.out.println("SP_PASSWORD NULL," + "'" + userOtp + "', '"
						+ connectId + "'");

				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
				Connection mssqlCon = null;

				mssqlCon = DriverManager.getConnection("jdbc:sqlserver://"
						+ ipAddress, serverId, serverPwd);

				if (mssqlCon == null)
					System.out.println("fail");
				else
					System.out.println("success");

				java.sql.Statement mssqlSt = null;
				mssqlSt = mssqlCon.createStatement();
				mssqlSt.executeUpdate("SP_PASSWORD NULL," + "'" + userOtp
						+ "', '" + connectId + "'");
				mssqlSt.close();
				mssqlCon.close();
				break;

			case "mysql":
				Connection con = null;
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection("jdbc:mysql://" + ipAddress
						+ "/mysql", serverId, serverPwd);

				String query;
				query = "update user set password=password('" + userOtp + "') where user='" + connectId + "';";

				Statement stmt = null;
				int rs;

				stmt = (Statement) con.createStatement();
				rs = stmt.executeUpdate(query);
				rs = stmt.executeUpdate("flush privileges");

				break;

			case "telnet":				
				if (serverOS.equals("windows")) {
					TelnetSample telnet = new TelnetSample();					
					telnet.setHostPrompt(">");
					telnet.connect(ipAddress, serverId, serverPwd);
					telnet.changePwd(connectId, "1234", userOtp);
					telnet.disconnect();
					System.out.println(userOtp);
					break;
				} else if (serverOS == "linux") {

				}
			default:
				break;
			}
			uc.setServerLock(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String firstOtp() {

		int pwdLevel = 10;
		String otp = "";
		for (int i = 0; i < pwdLevel; i++) {
			String buf;
			buf = "" + (char) ((Math.random() * 94) + 33);
			otp = otp + buf;
		}
		if (otp.contains("@"))
			otp = otp.replaceAll("@", "\\$");
		if (otp.contains("/"))
			otp = otp.replaceAll("/", "#");
		if (otp.contains("\""))
			otp = otp.replaceAll("\"", "_");
		if (otp.contains("<"))
			otp = otp.replaceAll("<", "#");
		if (otp.contains("'"))
			otp = otp.replaceAll("'", "#");
		System.out.println(otp);
		return otp;

	}

}
