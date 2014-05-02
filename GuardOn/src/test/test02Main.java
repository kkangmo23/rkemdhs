package test;

public class test02Main {

	public static void main(String[] args) {

		String serverIp, userId, currentPwd, newPwd;

		serverIp = "192.168.0.124";
		userId = "administrator";
		currentPwd = "1234";
		newPwd = "4321";

		TelnetSample telnet = new TelnetSample();

		// When server is based on linux
		// telnet.setHostPrompt("$");

		// When server is based on windows
		telnet.setHostPrompt(">");
		
		telnet.connect(serverIp, userId, currentPwd);
		telnet.changePwd(userId, currentPwd, newPwd);
		telnet.disconnect();

	}

}
