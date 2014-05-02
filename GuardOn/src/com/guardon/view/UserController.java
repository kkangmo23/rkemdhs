package com.guardon.view;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Timer;
import java.util.UUID;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.guardon.log.LogService;
import com.guardon.log.domain.Log;
import com.guardon.report.ReportService;
import com.guardon.request.RequestService;
import com.guardon.request.domain.Request;
import com.guardon.server.ServerService;
import com.guardon.server.domain.Server;
import com.guardon.user.UserService;
import com.guardon.user.domain.User;
import com.mysql.jdbc.Statement;


@Controller
@RequestMapping("/*")
@SessionAttributes("user")
public class UserController {
 
 @Inject
 @Named("userService") 
 UserService userService;
 
 @Inject
 @Named("serverService") 
 ServerService serverService;
 
 @Inject
 @Named("reportService") 
 ReportService reportService;
 
 @Inject
 @Named("requestService") 
 RequestService requestService;
 
 @Inject
 @Named("logService") 
 LogService logService;
 

 
 private boolean serverLock = false;
 
 public boolean isServerLock() {
	return serverLock;
}

public void setServerLock(boolean serverLock) {
	this.serverLock = serverLock;
}

@Async
 public String firstOtp() throws Exception{
  
	 boolean pwdEnhancement=true;
	 
	 
	 if(pwdEnhancement==false){
		 String otp;
		 otp = UUID.randomUUID().toString().replace("-", "").substring(0,10);
		 System.out.println(otp);
		 return otp;
	 }
	 else{
		 int pwdLevel=10;
		 String otp="";
		 for (int i = 0; i <pwdLevel ; i++) {
			 String buf;
			 buf = ""+(char)((Math.random() * 94) + 33);
			 otp = otp+buf;
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
		 return otp;
		} 
	 }
 
 public static int GetDifferenceOfDate ( int nYear1, int nMonth1, int nDate1, int nYear2, int nMonth2, int nDate2 )
 {
     Calendar cal = Calendar.getInstance ( );
     int nTotalDate1 = 0, nTotalDate2 = 0, nDiffOfYear = 0, nDiffOfDay = 0;
     if ( nYear1 > nYear2 )
     {
         for ( int i = nYear2; i < nYear1; i++ )
         {
             cal.set ( i, 12, 0 );
             nDiffOfYear += cal.get ( Calendar.DAY_OF_YEAR );
         }
         nTotalDate1 += nDiffOfYear;
     }
     else if ( nYear1 < nYear2 )
     {
         for ( int i = nYear1; i < nYear2; i++ )
         {
             cal.set ( i, 12, 0 );
             nDiffOfYear += cal.get ( Calendar.DAY_OF_YEAR );
         }
         nTotalDate2 += nDiffOfYear;
     }
      
     cal.set ( nYear1, nMonth1-1, nDate1 );
     nDiffOfDay = cal.get ( Calendar.DAY_OF_YEAR );
     nTotalDate1 += nDiffOfDay;
     cal.set ( nYear2, nMonth2-1, nDate2 );
     nDiffOfDay = cal.get ( Calendar.DAY_OF_YEAR );
     nTotalDate2 += nDiffOfDay;
      
     return (nTotalDate1-nTotalDate2)*60*60*24;
 }
 
 public int calculateDate(String startDate, String endDate) throws Exception{
	 int resultTime;
	 
	 startDate=startDate.replaceAll("-", "");
	 endDate=endDate.replaceAll("-", "");
	 
	 System.out.println(endDate);
	 System.out.println(startDate);
	 
	 int startYear, endYear;
	 int startMonth, endMonth;
	 int startDay, endDay;
	 
	 startYear = Integer.parseInt(startDate.substring(0, 4));
	 endYear = Integer.parseInt(endDate.substring(0, 4));
	 
	 startMonth = Integer.parseInt(startDate.substring(5, 6));
	 endMonth = Integer.parseInt(endDate.substring(5, 6));
	 
	 startDay = Integer.parseInt(startDate.substring(7, 8));
	 endDay = Integer.parseInt(endDate.substring(7, 8));
	 
	 resultTime=GetDifferenceOfDate(endYear, endMonth, endDay, startYear, startMonth, startDay);
	 
	 
	 return resultTime;
 }
 
 
 @RequestMapping("test.do")
 //@Scheduled(fixedDelay=5000)
 public void test(HttpServletRequest request) throws Exception{
 }
 
 @RequestMapping("option.do")
 public String option(HttpServletRequest request) throws Exception{
	 
	 
	 return "option";
 }
 
 @RequestMapping("updateOption.do")
 public String updateOption(HttpServletRequest request) throws Exception{
	 return "";
 }
 
 @RequestMapping("outSendRequest.do")
 public String outSendRequest(HttpServletRequest request, HttpSession session) throws Exception{
	 Request rq = new Request();
	 Map<String, String> map = new HashMap<>();
	 
	 String serverName, connectId, userId, requestDesc, pwdType;
	
	 serverName = request.getParameter("checkList");
	 connectId = request.getParameter("connectId");
	 userId = (String)session.getAttribute("userId");
	 requestDesc = request.getParameter("requestDesc");
	 pwdType = "OTP";
	 
	 
	 map.put("serverName", serverName);
	 map.put("connectId", connectId);
	 
	 if (!requestService.checkDuplReq(map).equals("0"))
	 {
		 request.setAttribute("message", "해당 서버와 아이디에 대해 이미 발급 된 비밀번호가 있거나 처리중인 요청이 있습니다.");
		 return "outUserOtpRequest";
	 }	 
	 
	 DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 Date now = new Date();
	 String requestDate = df.format(now);
	 
	 rq.setServerName(serverName);
	 rq.setConnectId(connectId);
	 rq.setRequestDate(requestDate);
	 rq.setUserId(userId);
	 rq.setRequestDesc(requestDesc);
	 rq.setPwdType(pwdType);
	 
	 requestService.insertRequest(rq);
	 
	 return "outUserOtpRequestDone";
 }
 
 @RequestMapping("outPeriodPwdList.do")
 public String outPeriodPwdList(HttpServletRequest request, HttpSession session) throws Exception{
	 String userId=session.getAttribute("userId").toString();
	 
	 DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	 Date now = new Date();
	 String requestDate = df.format(now);
	 
	 Map<String, String> map = new HashMap<>();
	 ArrayList<Request> periodPwdList = new ArrayList<>();
	 map.put("userId", userId);
	 map.put("endDate", requestDate);
	 periodPwdList = requestService.getPeriodPwd(map);
	 
	 request.setAttribute("periodPwdList", periodPwdList);
	 return "outPeriodPwdList";
 }
 
 @RequestMapping("periodPwdList.do")
 public String periodPwdList(HttpServletRequest request, HttpSession session) throws Exception{
	 String userId=session.getAttribute("userId").toString();
	 
	 DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	 Date now = new Date();
	 String requestDate = df.format(now);
	 
	 Map<String, String> map = new HashMap<>();
	 ArrayList<Request> periodPwdList = new ArrayList<>();
	 map.put("userId", userId);
	 map.put("endDate", requestDate);
	 periodPwdList = requestService.getPeriodPwd(map);
	 
	 request.setAttribute("periodPwdList", periodPwdList);
	 return "periodPwdList";
 }
 
 
 @RequestMapping("outPeriodPwdApproval.do")
 public String outPeriodPwdApproval(HttpServletRequest request, HttpSession session) throws Exception{
	 Request rq = new Request();
	 Map<String, String> map = new HashMap<>();
	 
	 DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 Date now = new Date();
	 String requestDate = df.format(now);
	 String serverName=request.getParameter("checkList");
	 String connectId=request.getParameter("connectId");
	 String startDate = request.getParameter("startDate");
	 String endDate = request.getParameter("endDate");
	 String pwdType = "period";
	 String userId = session.getAttribute("userId").toString();
	 String requestDesc = request.getParameter("requestDesc");
	 
	 map.put("serverName", serverName);
	 map.put("connectId", connectId);	
	 
	 if (!requestService.checkDuplReq(map).equals("0"))
	 {
		 request.setAttribute("message", "해당 서버와 아이디에 대해 이미 발급 된 비밀번호가 있거나 처리중인 요청이 있습니다.");
		 return "outPeriodPwdServerSelect";
	 }	 
	 rq.setServerName(serverName);
	 rq.setConnectId(connectId);
	 rq.setRequestDate(requestDate);
	 rq.setUserId(userId);
	 rq.setRequestDesc(requestDesc);
	 rq.setPwdType(pwdType);
	 rq.setStartDate(startDate);
	 rq.setEndDate(endDate);
	 
	 requestService.insertRequest(rq);
	 return "outUserOtpRequestDone";
 }
 
 @RequestMapping("periodPwdApproval.do")
 public String periodPwdApproval(HttpServletRequest request, HttpSession session) throws Exception{
	 Request rq = new Request();
	 Map<String, String> map = new HashMap<>();
	 
	 DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 Date now = new Date();
	 String requestDate = df.format(now);
	 String serverName=request.getParameter("checkList");
	 String connectId=request.getParameter("connectId");
	 String startDate = request.getParameter("startDate");
	 String endDate = request.getParameter("endDate");
	 String pwdType = "period";
	 String userId = session.getAttribute("userId").toString();
	 String requestDesc = request.getParameter("requestDesc");
	 
	 map.put("serverName", serverName);
	 map.put("connectId", connectId);	
	 
	 if (!requestService.checkDuplReq(map).equals("0"))
	 {
		 request.setAttribute("message", "해당 서버와 아이디에 대해 이미 발급 된 비밀번호가 있거나 처리중인 요청이 있습니다.");
		 return "periodPwdServerSelect";
	 }	 
	 
	 rq.setServerName(serverName);
	 rq.setConnectId(connectId);
	 rq.setRequestDate(requestDate);
	 rq.setUserId(userId);
	 rq.setRequestDesc(requestDesc);
	 rq.setPwdType(pwdType);
	 rq.setStartDate(startDate);
	 rq.setEndDate(endDate);
	 
	 requestService.insertRequest(rq);
	 return "userOtpRequestDone";
 }
 
 @RequestMapping("periodPwd.do")
 public String periodPwd(HttpServletRequest request, HttpSession session) throws Exception{

	 String startDate, endDate;

	 startDate = request.getParameter("startDate");
	 endDate = request.getParameter("endDate");
	 
	 System.out.println(calculateDate(startDate, endDate));
	 
	 String[] str = request.getParameterValues("checkList");
	 for (int i = 0; i < str.length; i++) {
		System.out.println(str[i]);
	}

	 return "periodPwd";
 }
 
 
 @RequestMapping ("outPeriodPwdServerSelect.do")
 public String outPeriodServerList(HttpServletRequest request) throws Exception{
	 
	 String pageParam = request.getParameter("page");
		if(pageParam==null || pageParam.equals("")){
			pageParam="1";
		}
		ArrayList<Server> serverList = serverService.getServerList(Integer.parseInt(pageParam));
		int serverCount = serverService.getServerListCount();
		
		request.setAttribute("serverListCnt", serverCount);
		request.setAttribute("serverList", serverList);
		request.setAttribute("page", Integer.parseInt(pageParam));
		
	 return "outPeriodPwdServerSelect";
 }
 
 @RequestMapping ("periodPwdServerSelect.do")
 public String periodServerList(HttpServletRequest request) throws Exception{
	 
	 String pageParam = request.getParameter("page");
		if(pageParam==null || pageParam.equals("")){
			pageParam="1";
		}
		ArrayList<Server> serverList = serverService.getServerList(Integer.parseInt(pageParam));
		int serverCount = serverService.getServerListCount();
		
		request.setAttribute("serverListCnt", serverCount);
		request.setAttribute("serverList", serverList);
		request.setAttribute("page", Integer.parseInt(pageParam));
		request.setAttribute("message", "1234asd");
		
	 return "periodPwdServerSelect";
 }
 
 /*
  * otp 한 번 만드는 함수
  * 
  * 
  * 
  */ 
  public String makeOtpAll(String serverName, String connectId) throws Exception{
	  String userOtp, ipAddress, serverId, serverPwd, connectType;
	  String serverOS = "windows";
	  
	  userOtp = firstOtp();	  
	  
	  ipAddress = serverService.getServerIpAddress(serverName);
	  serverId = serverService.getServerId(serverName);
	  serverPwd = serverService.getServerPwd(serverName);
	  connectType = serverService.getConnectType(serverName);
	  
	  System.out.println(serverName);
	  System.out.println(connectId);
	  System.out.println(connectType);
	  System.out.println(userOtp);
	  
	  switch (connectType) {
		
		case "oracle":			
	
			String sql = "alter user "+connectId + " identified by \""+ userOtp + "\"";;					
			
			Class.forName("oracle.jdbc.driver.OracleDriver");				
			
			Connection conn = null;
			conn = DriverManager.getConnection("jdbc:oracle:thin:@"+ipAddress+":orcl", serverId, serverPwd);
							
			java.sql.Statement smt = null;
			smt = conn.createStatement();
			smt.executeUpdate(sql);			

			smt.close();
			conn.close();	
			
			break;
		
		case "mssql":				
			
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			Connection mssqlCon = null;

			mssqlCon = DriverManager.getConnection("jdbc:sqlserver://"+ipAddress,	serverId, serverPwd);
			if (mssqlCon == null)
				System.out.println("fail");
			else
				System.out.println("success");
			
			java.sql.Statement mssqlSt = null;

			mssqlSt = mssqlCon.createStatement();

			mssqlSt.executeUpdate("SP_PASSWORD NULL,"+"'"+userOtp+"', '"+connectId+"'");
			
			mssqlSt.close();
			mssqlCon.close();			
			
			break;
			
		case "mysql":			
			
			Connection con = null;
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://"+ipAddress+"/mysql", serverId, serverPwd);
			
			String query;
			
			query = "update user set password=password('" + userOtp + "') where user='" + connectId + "';";
			Statement stmt = null;
			int rs;
			  
			stmt = (Statement) con.createStatement();
			rs = stmt.executeUpdate(query);
			rs = stmt.executeUpdate("flush privileges");			
			
			break;
			
		case "telnet":			
			
			if(serverOS.equals("windows")){
				TelnetSample telnet = new TelnetSample();					
				
				telnet.setHostPrompt(">");
				telnet.connect(ipAddress, serverId, serverPwd);
				telnet.changePwd(connectId, "1234", userOtp);			
				
				telnet.disconnect();				
			}
			break;

		default:
			break;
		}
	  return userOtp;
  }
  
  @RequestMapping("changeAllList.do")
  public String changeAllList(HttpServletRequest request) throws Exception{
	  ArrayList<Request> changeAllList = new ArrayList<Request>();
	  changeAllList=requestService.getApprovedReq();
	  
	  request.setAttribute("changeAllList", changeAllList);
	  return "changeAllList";
  }
  
  @RequestMapping("changeAllPwd.do")//checkbox 받아온 애들
  public String changeAllPwd(HttpServletRequest request) throws Exception {
	  
	  String temp[]=request.getParameterValues("temp");
	  String arr[];
	  for (int i = 0; i < temp.length; i++) {
		arr=temp[i].split(",");
		makeOtpAll(arr[0], arr[1]);
	}	  
	  
	  int cnt=temp.length;
	  System.out.println(cnt);		
	  
	  request.setAttribute("count", cnt);
	  
	  return "changeAllPwdResult";
	  
  }
  /* 
  * 
  * 
  * 
  * 
  */
 
 @RequestMapping("/userOtp.do")
 public String makeOtp(HttpServletRequest request, HttpSession session){

		 User user = new User();
		 MakeOTP makeOTP = new MakeOTP();
		 Timer timer = new Timer();
		 Map<String, String> map = new HashMap<>();
  
		 String userOtp;
		 String serverName;
		 String ipAddress;
		 String serverId;
		 String serverPwd;
		 String connectId;
		 String dbName;
		 String serverOS;
		 String connectType;
		 String userType;
		 String userId;
		 int otpTimeLimit=40;
		 			 
	  if(request.getParameter("connectId")==null){
		  connectId=session.getAttribute("connectId").toString();
		  System.out.println("session connectId : "+connectId);
	  }else {
		  session.setAttribute("connectId", request.getParameter("connectId"));
		  connectId=request.getParameter("connectId");
	  }
	  if(request.getParameter("checkList")==null){
		  serverName=session.getAttribute("serverName").toString();
		  System.out.println("session serverName : "+serverName);
	  }else {
		  session.setAttribute("serverName", request.getParameter("checkList"));
		  serverName=request.getParameter("checkList");
	  }
	  
	 try{
		 userId=session.getAttribute("userId").toString();
		 userType=userService.getUserType(userId);
		 
		 map.put("serverName", serverName);
		 map.put("connectId", connectId);
		 map.put("userId", userId);	
		 
		 System.out.println("serverName : "+ serverName);
		 System.out.println("connectId : "+ connectId);
		 System.out.println("userId : "+ userId);
		 //System.out.println(requestService.getOtpApproved(map));
		 System.out.println("userType : " + userType);
		 //System.out.println(requestService.getOtpApproved(map).toString());
		 
	  if ((userType.equals("outUser") && requestService.getOtpApproved(map).equals("approved")) || userType.equals("user")) {
		 
	  userOtp = firstOtp();  
	  System.out.println(userOtp);
	  	  
	  //dbName = serverService.getDbName(serverName);
	  //serverName = request.getParameter("checkList");
	  System.out.println(request.getParameter("checkList"));
	  ipAddress = serverService.getServerIpAddress(serverName);
	  serverId = serverService.getServerId(serverName);
	  serverPwd = serverService.getServerPwd(serverName);
	  connectType = serverService.getConnectType(serverName);
	  request.setAttribute("second", otpTimeLimit);
	  
	  System.out.println(connectType);
	

			switch (connectType) {
			
			case "oracle":			
		
				String sql = "alter user "+connectId + " identified by \""+ userOtp + "\"";;					
				
				System.out.println(sql);
				
				user.setUserOtp(userOtp);				
				request.setAttribute("userOtp", userOtp);
				
				Class.forName("oracle.jdbc.driver.OracleDriver");
				
				makeOTP.setIpAddress(serverService.getServerIpAddress(serverName));
				makeOTP.setServerId(serverService.getServerId(serverName));
				makeOTP.setServerPwd(serverService.getServerPwd(serverName));
				makeOTP.setConnectType(connectType);
				makeOTP.setconnectId(connectId);
				makeOTP.setDbName(serverName);
								
				
				System.out.println("jdbc:oracle:thin:@"+ipAddress+":orcl");				
				
				Connection conn = null;
				conn = DriverManager.getConnection("jdbc:oracle:thin:@"+ipAddress+":orcl", serverId, serverPwd);
								
				java.sql.Statement smt = null;
				smt = conn.createStatement();
				smt.executeUpdate(sql);
				/*
				java.sql.PreparedStatement psmt = null;
				
				int result;
				psmt = conn.prepareStatement(sql);
				result = psmt.executeUpdate();
				
				if(result==0) System.out.println("success");
				else System.out.println("failed");
				*/

				smt.close();
				conn.close();
				
				timer.schedule(makeOTP, otpTimeLimit*1000);				
				
				if(userType.equals("user"))
					return "userOtp";
				else if(userType.equals("outUser"))
					return "outUserOtp";
			
			case "mssql":		
				
				System.out.println("SP_PASSWORD NULL,"+"'"+userOtp+"', '"+connectId+"'");
				
				user.setUserOtp(userOtp);
				request.setAttribute("userOtp", userOtp);
				
				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
				Connection mssqlCon = null;
	
				mssqlCon = DriverManager.getConnection("jdbc:sqlserver://"+serverService.getServerIpAddress(serverName),
						serverService.getServerId(serverName), serverService.getServerPwd(serverName));
				if (mssqlCon == null)
					System.out.println("fail");
				else
					System.out.println("success");
				
				java.sql.Statement mssqlSt = null;
	
				mssqlSt = mssqlCon.createStatement();
	
				makeOTP.setIpAddress(serverService.getServerIpAddress(serverName));
				makeOTP.setServerId(serverService.getServerId(serverName));
				makeOTP.setServerPwd(serverService.getServerPwd(serverName));
				makeOTP.setConnectType(connectType);
				makeOTP.setconnectId(connectId);
				makeOTP.setDbName(serverName);
				
				mssqlSt.executeUpdate("SP_PASSWORD NULL,"+"'"+userOtp+"', '"+connectId+"'");
				
				mssqlSt.close();
				mssqlCon.close();
				
				timer.schedule(makeOTP, otpTimeLimit*1000);
				
				if(userType.equals("user"))
					return "userOtp";
				else if(userType.equals("outUser"))
					return "outUserOtp";
				
			case "mysql":			
				
				Connection con = null;
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection("jdbc:mysql://"+ipAddress+"/mysql", serverId, serverPwd);
				
				String query;
				
				query = "update user set password=password('" + userOtp + "') where user='" + connectId + "';";
				Statement stmt = null;
				int rs;
				  
				stmt = (Statement) con.createStatement();
				rs = stmt.executeUpdate(query);
				rs = stmt.executeUpdate("flush privileges");
				/*
				
				query= "alter tbl_user "+connectId + " identified by "+ userOtp;
				
				java.sql.PreparedStatement stmt = null;
				stmt = con.prepareStatement(query);
				stmt.executeUpdate();
				*/
				user.setUserOtp(userOtp);
				request.setAttribute("userOtp", userOtp);
				  
				makeOTP.setIpAddress(serverService.getServerIpAddress(serverName));
				makeOTP.setServerId(serverService.getServerId(serverName));
				makeOTP.setServerPwd(serverService.getServerPwd(serverName));
				makeOTP.setConnectType(connectType);
				makeOTP.setconnectId(connectId);
				makeOTP.setDbName(serverName);
				  
				timer.schedule(makeOTP, otpTimeLimit*1000);
				  
				if(userType.equals("user"))
					return "userOtp";
				else if(userType.equals("outUser"))
					return "outUserOtp";
				  
			case "telnet":			
				serverOS=serverService.getServerOS(serverName);
				if(serverOS.equals("windows")){
					TelnetSample telnet = new TelnetSample();					
					
					makeOTP.setIpAddress(serverService.getServerIpAddress(serverName));
					makeOTP.setServerId(serverService.getServerId(serverName));
					makeOTP.setServerPwd(serverService.getServerPwd(serverName));
					makeOTP.setConnectType(connectType);
					makeOTP.setconnectId(connectId);
					makeOTP.setServerOS(serverOS);
					
					telnet.setHostPrompt(">");
					telnet.connect(ipAddress, serverId, serverPwd);
					telnet.changePwd(connectId, "1234", userOtp);
					user.setUserOtp(userOtp);
					request.setAttribute("userOtp", userOtp);
					
					telnet.disconnect();
					
					timer.schedule(makeOTP, otpTimeLimit*1000);
					
					
					System.out.println(userOtp);
					
					if(userType.equals("user"))
						return "userOtp";
					else if(userType.equals("outUser"))
						return "outUserOtp";
				}
	
			default:
				break;
			}		 
	  }
	  else{
		if(userType.equals("user")) return "errorUserAuthority";
		if(userType.equals("outUser")) return "errorOutAuthority";
	  }
			
	  }
	  catch (Exception e) {
		  e.printStackTrace();
		  return "errorUserOtp";
	  }

	  
		System.out.println("error");
		return "errorUserOtp";
}

 
 @RequestMapping(method=RequestMethod.GET, value="/updateRejected.do")
 public String updateRejected(HttpSession session, HttpServletRequest request) throws Exception{
	 
	 Map<String, String> map = new HashMap<>();
	 String[] array = request.getParameterValues("temp");
	 String[] array2;
	 
	 DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 Date now = new Date();
	 
	 String approvalId = (String)session.getAttribute("userId");
	 String approvalDate = df2.format(now);
	 
	 for (int i = 0; i < array.length; i++) {
		 array2 = array[i].split(",");
		 map.put("serverName", array2[0]);
		 map.put("connectId", array2[1]);
		 map.put("userId", array2[2]);
		 map.put("approvalId", approvalId);
		 map.put("approvalDate", approvalDate);
		 requestService.updateRejected(map);
		 map.clear();
	 }
	 int approvalCount = array.length;
	 request.setAttribute("approvalCount", approvalCount);
	 
	 return "approvalResult";
 }
 
 @RequestMapping(method=RequestMethod.GET, value="/updateApproved.do")
 public String updateApproved(HttpSession session, HttpServletRequest request) throws Exception{
	 String userOtp=null, serverName, connectId;
	 Map<String, String> map = new HashMap<>();
	 Timer timer = new Timer();
	 MakeOTP makeOTP = new MakeOTP();
	 DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	 DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 Date now = new Date();
	 Date end = new Date();
	 ScheduledThreadPoolExecutor stp = new ScheduledThreadPoolExecutor(20);
	 
	 String[] array = request.getParameterValues("temp");
	 String[] array2;
	 
	 String approvalId = (String)session.getAttribute("userId");
	 String approvalDate = df2.format(now);
	 
	 for (int i = 0; i < array.length; i++) {
		 array2 = array[i].split(",");
		 map.put("serverName", array2[0]);
		 map.put("connectId", array2[1]);
		 map.put("userId", array2[2]);
		 map.put("approvalId", approvalId);
		 map.put("approvalDate", approvalDate);
		 
		 if(array2[3].equals("period"))
		 {
		 serverName=map.get("serverName");
		 connectId=map.get("connectId");
		 userOtp=makeOtpAll(serverName, connectId);
		 map.put("password", userOtp);
		 
		 /*
		 makeOTP.setconnectId(connectId);
		 makeOTP.setServerName(serverName);
		 makeOTP.setServerId(serverService.getServerId(serverName));
		 makeOTP.setServerPwd(serverService.getServerPwd(serverName));
		 makeOTP.setIpAddress(serverService.getServerIpAddress(serverName));
		 makeOTP.setConnectType(serverService.getConnectType(serverName));
		 */
		 
		 System.out.println("check : "+ userOtp);
		 
		 requestService.updatePassword(map);
		 //String endDate = requestService.getEndDate(map);
		 //System.out.println("endDate : "+endDate);
		 //requestDate = df.format(now);
		 requestService.updateApproved(map);
		 //System.out.println("reqDate : "+ requestDate);
		 //timer.schedule(makeOTP, calculateDate(requestDate, endDate)*1000);		 
		 
		 //end = df.parse(endDate);
		 //timer.schedule(makeOTP, end);
		 
		 userOtp=null;
		 
		 }
		 else if (array2[3].equals("OTP"))
		 {
			 requestService.updateApproved(map);
			 map.clear();
		 }
	 }
	 int approvalCount = array.length;
	 request.setAttribute("approvalCount", approvalCount);
	 
	 return "approvalResult";
 }
 
 
 @RequestMapping("/outUserOtpPretreatment.do")
 public String outUserOtpPretreatment(HttpServletRequest request) throws Exception{
	 
		String pageParam = request.getParameter("page");
		if(pageParam==null || pageParam.equals("")){
			pageParam="1";
		}
		
		ArrayList<Server> serverList = serverService.getServerList(Integer.parseInt(pageParam));
		int serverCount = serverService.getServerListCount();
		
		request.setAttribute("serverListCnt", serverCount);
		request.setAttribute("serverList", serverList);
		request.setAttribute("page", Integer.parseInt(pageParam));
		
	 
	 return "outUserOtpPretreatment";
 }
 
 @RequestMapping("/userOtpPretreatment.do")
 public String userOtpPretreatment(HttpServletRequest request) throws Exception{
	 
		String pageParam = request.getParameter("page");
		if(pageParam==null || pageParam.equals("")){
			pageParam="1";
		}
		
		ArrayList<Server> serverList = serverService.getServerList(Integer.parseInt(pageParam));
		int serverCount = serverService.getServerListCount();
		
		request.setAttribute("serverListCnt", serverCount);
		request.setAttribute("serverList", serverList);
		request.setAttribute("page", Integer.parseInt(pageParam));
		
	 
	 return "userOtpPretreatment";
 }
 
 

 
 
 /* 
  * 
  * 
  * 
  * 
  * 
  * 
  * 
  * */
 
 
 @RequestMapping("/dbList.do")
 public String dbList(HttpServletRequest request) throws Exception{
	String pageParam = request.getParameter("page");
	if(pageParam==null || pageParam.equals("")){
		pageParam="1";
	}
	ArrayList<Server> serverList = serverService.getServerList(Integer.parseInt(pageParam));
	int serverCount = serverService.getServerListCount();
	
	request.setAttribute("serverListCnt", serverCount);
	request.setAttribute("serverList", serverList);
	request.setAttribute("page", Integer.parseInt(pageParam));
	
	return "dbList";
 }
 
 @RequestMapping("/insertWorkflow.do") // 워크플로우 페이지 작성 
 public String insertWorkflow(HttpServletRequest request) throws Exception{
	 String step, workflowName, workflowDesc;
	 step = request.getParameter("step");
	 workflowName = request.getParameter("workflowName");
	 workflowDesc = request.getParameter("workflowDesc");
	 
	 System.out.println("ssssssssssssss : "+step);
	 System.out.println("ssssssssssssss : "+workflowName);
	 System.out.println("ssssssssssssss : "+workflowDesc);
	 return "";
 }
 
 @RequestMapping("/dbInstall.do")
 public String dbInstall(HttpServletRequest request) throws Exception{
	 
	 return "dbInstall";	 
 }
 
 @RequestMapping("/serverInstall.do")
 public String serverInstall(HttpServletRequest request) throws Exception{
	 
	 return "serverInstall";	 
 }

 @RequestMapping("/insertServer.do")
	public String insertServer(HttpServletRequest request, HttpSession session) throws Exception{
	 String serverName, ipAddress, serverDesc, serverId, serverPwd, connectType, serverOS, port;
	 Server server = new Server();
	 serverName=request.getParameter("serverName");
	 ipAddress = request.getParameter("ipAddress");
	 port = request.getParameter("port");
	 serverDesc = request.getParameter("serverDesc");
	 serverId = request.getParameter("serverId");
	 serverPwd = request.getParameter("serverPwd");
	 connectType = request.getParameter("connectType");
	 serverOS = request.getParameter("serverOS");
	 
	 server.setServerName(serverName);
	 server.setIpAddress(ipAddress);
	 server.setServerDesc(serverDesc);
	 server.setServerId(serverId);
	 server.setServerPwd(serverPwd);
	 server.setConnectType(connectType);
	 server.setServerOS(serverOS);
	 server.setPort(port);
	 	 
	 serverService.insertServer(server);
	 
	 ArrayList<Server> serverList = serverService.getServerList(1);
	 InetAddress pingCheck;
	 boolean checkResult;
	 for(int i=0; i<serverList.size(); i++){
		 pingCheck = InetAddress.getByName(serverList.get(i).getIpAddress().split(":")[0]);
		 checkResult = pingCheck.isReachable(1000);
		 if(checkResult){
			 serverList.get(i).setPingCheck("SUCCESS");
		 } else {
			 serverList.get(i).setPingCheck("FAILURE");
		 }
	 }
	 request.setAttribute("serverList", serverList);
	return "serverList";
	}
 
 @RequestMapping("/insertDBServer.do")
 public String insertDBServer(HttpServletRequest request) throws Exception{
	 
	 String serverName, ipAddress, serverDesc, serverId, serverPwd, connectType, port, dbName;
	 Server server = new Server();
	 serverName=request.getParameter("serverName");
	 ipAddress = request.getParameter("ipAddress");
	 serverDesc = request.getParameter("serverDesc");
	 serverId = request.getParameter("serverId");
	 serverPwd = request.getParameter("serverPwd");
	 connectType = request.getParameter("connectType");	
	 port = request.getParameter("port");
	 dbName = request.getParameter("dbName");
	 
	 server.setServerName(serverName);
	 server.setIpAddress(ipAddress);
	 server.setServerDesc(serverDesc);
	 server.setServerId(serverId);
	 server.setServerPwd(serverPwd);
	 server.setConnectType(connectType);
	 server.setPort(port);
	 server.setDbName(dbName);
	 
	 serverService.insertDBServer(server);
	 
	 ArrayList<Server> serverList = serverService.getServerList(1);
	 InetAddress pingCheck;
	 boolean checkResult;
	 for(int i=0; i<serverList.size(); i++){
		 pingCheck = InetAddress.getByName(serverList.get(i).getIpAddress().split(":")[0]);
		 checkResult = pingCheck.isReachable(1000);
		 if(checkResult){
			 serverList.get(i).setPingCheck("SUCCESS");
		 } else {
			 serverList.get(i).setPingCheck("FAILURE");
		 }
	 }
	 request.setAttribute("serverList", serverList);
	return "dbList";	
 }
 
 
 @RequestMapping("workflow.do")
 public String workflow(HttpServletRequest request) throws Exception{
	 //Integer.parseInt(request.getParameter("page"))
	 request.setAttribute("userList", userService.getUserList(1));
	 return "workflow";
 }
 
 @RequestMapping("email.do")
 public String email() throws Exception{
	  Properties properties = new Properties();
	  properties.put("mail.transport.protocol", "smtp");
	  properties.put("mail.smtp.host", "127.0.0.1");
	  properties.put("mail.smtp.port", "25");
	  
	  Session mailSession = Session.getInstance(properties);
	  Message message = new MimeMessage(mailSession);
	  
	  try {
	   message.setFrom(new InternetAddress("보내는 사람 주소"));
	   message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("받는 사람 주소"));
	   message.setSentDate(new Date());
	   message.setSubject("메일 제목이 들어갈 부분");
	   
	   message.setText("메일 내용이 들어갈 부분");
	   
	   Transport.send(message);
	   System.out.println("E-mail successfully sent!");
	  } catch (AddressException e) {
	   e.printStackTrace();
	   System.out.println("AddressException : " + e);
	  } catch (MessagingException e) {
	   e.printStackTrace();
	   System.out.println("MessagingException : " + e);
	  }
	  
	  return "email";
 }

 
 @RequestMapping("userOtpDBSelect.do")
 public String userOtpDBSelect(HttpServletRequest request) throws Exception{
	  ArrayList<User> DBlist = new ArrayList<User>();
	  
	  for (int i = 0; i < 6; i++) {
	   User user = new User();
	   DBlist.add(user);
	  }
	  
	  request.setAttribute("DBList",DBlist);
	 
	 return "userOtpDBSelect";
 }
 
 @RequestMapping("/adminLogin.do")
 public String adminLogin(HttpServletRequest request) throws Exception{
	 
	 return "adminLogin";

 }
 
 @RequestMapping("/approvalResult.do")
 public String approvalResult(HttpServletRequest request, HttpSession session)throws Exception{
  
  ArrayList<User> list = new ArrayList<User>();
  
  for (int i = 0; i < 4; i++) {
   User user = new User();
   user.setUserId("asd"+i);
   user.setUserName("김동진"+i);
   user.setUserDepartment("개발"+i+"부");
   list.add(user);
  }
  request.setAttribute("resultList", list);
  return "approvalResult";
 }
 @RequestMapping("/approvalUserList.do")
 public String approvalUserList(HttpServletRequest request) throws Exception{
  

  ArrayList<Request> requestList = new ArrayList<Request>();
  int page=1;
  try {
	page=Integer.parseInt(request.getParameter("page"));
  } catch(Exception e){
	page=1;  
  }
  requestList=requestService.getApprovedList(page);  
  request.setAttribute("requestList", requestList);
  return "approvalUserList";
 }
 
 @ResponseBody
 @RequestMapping("/checkApproval.do")
 public void checkApproval(HttpServletRequest request, HttpServletResponse response) throws Exception{
  ArrayList<Request> requestList = new ArrayList<Request>();
  int page=1;
  try {
	page=Integer.parseInt(request.getParameter("page"));
  } catch(Exception e){
	page=1;  
  }
  requestList=requestService.getApprovedList(page);
  
  if(requestList.size()>0){
	  response.getOutputStream().write("승인해야함".getBytes("UTF-8"));
  } else {
	  response.getOutputStream().write("승인완료됨".getBytes("UTF-8"));
  }
 }
 
 @RequestMapping("/outUserOtp.do")
 public String outUserOtp(HttpServletRequest request, HttpSession session) throws Exception{
  User user = new User();
  String userOtp;
  Boolean approval = false;
  String message="관리자 승인을 기다리십시오.";
  
  if(approval=true){
   userOtp = firstOtp(); //OTP set  
   user.setUserOtp(userOtp);
   request.setAttribute("userOtp", userOtp);
   System.out.println(userOtp);
   return "outUserOtp";
  }else if (approval=false) {
   request.setAttribute("message", message);
   return "outUserApproval";
  }
  
  
  return "outUserOtp";
 }
 
 
 @RequestMapping("/outUserApproval.do")
 public String outUserApproval(HttpServletRequest request) throws Exception{
  
  String message = "승인요청 되었습니다.";
  User user = new User();
  user.setApproval(true);
  request.setAttribute("message", message);
  
  return "outUserApproval";
 }
 
 @RequestMapping("/index.do")
 public String index(HttpServletRequest request, HttpSession session) throws Exception {	 
		
	 if (session.getAttribute("userId")!=null)
	 {
		 String userId = (String)session.getAttribute("userId");
		 String userType = userService.getUserType(userId);
		 
		 switch (userType) {
		case "admin":
			return "adminHome";
			
		case "user":
			return "nomalUserHome";
		
		case "outUser":
			return "outUserHome";	
			
		default:
			break;
		}
	 }
	 return "userLogin";
 }
 
 @RequestMapping("/CheckPeriodPwd.do")
 public String ChechPeriodPwd(HttpServletRequest request) throws Exception {
  User user = new User();
  String userOtp;
  String outEndPerdate;
  
  userOtp = firstOtp(); //OTP set
  //outEndPerdate = user.getOutEndPerDate();
  outEndPerdate = "2013년 12월 03일";
  
  user.setUserOtp(userOtp);
  request.setAttribute("userOtp", userOtp);
  request.setAttribute("periodPwd",outEndPerdate);
  System.out.println(userOtp);
  System.out.println(outEndPerdate);
  
  return "periodPwd";
 }
 
 
 @RequestMapping("/changePwd.do")
 public String changePwd(HttpServletRequest request, HttpSession session)throws Exception{
  
  ArrayList<User> list = new ArrayList<User>();
  
  for (int i = 0; i < 2; i++) {
   User user = new User();
   user.setUserName("김동진"+i);
   user.setUserDepartment("개발"+i+"부");
   user.setUserOtp(firstOtp());
   list.add(user);
  }
  request.setAttribute("resultList", list);
  return "changePwd";
 }
 
 @RequestMapping("/userJoin.do")
 public String userJoin(HttpServletRequest request) throws Exception{	 
	 return "joinUser";
 }
 
 @RequestMapping("/insertUser.do")
 public String insertUser(HttpServletRequest request) throws Exception{
	 String userName, userId, userPwd, userDepartment, userLevel, companyNumber, userEmail, phoneNumber, userType;
	 User user = new User();
	 userName=request.getParameter("userName");
	 userId = request.getParameter("userId");
	 userPwd = request.getParameter("userPwd");
	 userType = request.getParameter("userType");
	 userDepartment = request.getParameter("userDepartment");
	 userLevel = request.getParameter("userLevel");
	 companyNumber = request.getParameter("companyNumber");
	 userEmail = request.getParameter("userEmail");
	 phoneNumber = request.getParameter("phoneNumber")+request.getParameter("userCp2")+request.getParameter("userCp3");
	 
	 GregorianCalendar gc = new GregorianCalendar();
	 long now = gc.getTimeInMillis();
	 Timestamp joinDate = new Timestamp(now);
	 
	 user.setUserName(userName);
	 user.setUserId(userId);
	 user.setUserPwd(userPwd);
	 user.setUserType(userType);
	 user.setUserDepartment(userDepartment);
	 user.setUserLevel(userLevel);
	 user.setCompanyNumber(companyNumber);
	 user.setUserEmail(userEmail);
	 user.setPhoneNumber(phoneNumber);
	 user.setJoinDate(joinDate);
	 	 
	 userService.insertUser(user);
	 
	 return "userLogin";
 }
 
 @RequestMapping("/updateUser.do")
 public String updateUser(HttpServletRequest request, HttpSession session) throws Exception{	 
	 String userId, userType;
	 User user = new User();
	 
	 userId = session.getAttribute("userId").toString();
	 userType = userService.getUserType(userId);
	 
	 user = userService.getUserBasicInfo(userId);
	 
	 request.setAttribute("userName", user.getUserName());
	 request.setAttribute("userDepartment", user.getUserDepartment());
	 request.setAttribute("userLevel", user.getUserLevel());
	 request.setAttribute("companyNumber", user.getCompanyNumber());
	 request.setAttribute("userEmail", user.getUserEmail());
	 
	 String fullPhoneNumber = user.getPhoneNumber();
	 String phoneNumber = fullPhoneNumber.substring(0, 3);
	 System.out.println(phoneNumber);
	 String userCp2 = fullPhoneNumber.substring(3, 7);
	 String userCp3 = fullPhoneNumber.substring(7);
	 request.setAttribute("phoneNumber", phoneNumber);
	 request.setAttribute("userCp2", userCp2);
	 request.setAttribute("userCp3", userCp3);
	 
	 if (userType.equals("admin"))
		 return "updateAdmin";
	 else if (userType.equals("user"))
		 return "updateUser";
	 else if (userType.equals("outUser"))
		 return "updateOutUser";
	 else
		 return ""; //userType이 위 셋 중 아니면 어떻게 에러처리????
 }
 
 @RequestMapping("/updateUserInfo.do")
 public String updateUserInfo(HttpServletRequest request, HttpSession session) throws Exception{
	 String userId, userName, userPwd, userDepartment, userLevel, companyNumber, userEmail, phoneNumber;
	 User user = new User();
	 userId = session.getAttribute("userId").toString();	
	 userName = request.getParameter("userName");
	 userPwd = request.getParameter("userPwd");
	 userDepartment = request.getParameter("userDepartment");
	 userLevel = request.getParameter("userLevel");
	 companyNumber = request.getParameter("companyNumber");
	 userEmail = request.getParameter("userEmail");
	 phoneNumber = request.getParameter("phoneNumber")+request.getParameter("userCp2")+request.getParameter("userCp3");
	 	
	 user.setUserId(userId);
	 user.setUserName(userName);
	 user.setUserPwd(userPwd);
	 user.setUserDepartment(userDepartment);
	 user.setUserLevel(userLevel);
	 user.setCompanyNumber(companyNumber);
	 user.setUserEmail(userEmail);
	 user.setPhoneNumber(phoneNumber);	 	
	 
	 userService.updateUser(user);
	 return "nomalUserHome";
 }
 
 public void executeMakeOTP() throws Exception {
	 MakeOTP makeOTP = new MakeOTP();
	 String serverName = "onsol";
	 makeOTP.setconnectId("onsol");
	 makeOTP.setIpAddress(serverService.getServerIpAddress(serverName));
	 makeOTP.setServerId(serverService.getServerId(serverName));
	 makeOTP.setServerPwd(serverService.getServerPwd(serverName));
	 makeOTP.setconnectId(serverService.getConnectType(serverName));
	 makeOTP.run();
	 
 }
 
 @RequestMapping("outUserOtpRequest.do")
 public String outUserOtpRequest(HttpServletRequest request) throws Exception{
	 
		String pageParam = request.getParameter("page");
		if(pageParam==null || pageParam.equals("")){
			pageParam="1";
		}
		ArrayList<Server> serverList = serverService.getServerList(Integer.parseInt(pageParam));
		int serverCount = serverService.getServerListCount();
		
		request.setAttribute("serverListCnt", serverCount);
		request.setAttribute("serverList", serverList);
		request.setAttribute("page", Integer.parseInt(pageParam));

	 
	 return "outUserOtpRequest";
 }
 
 @RequestMapping("sendRequest.do")
 public String sendRequest(HttpServletRequest request, HttpSession session) throws Exception{
	 Request rq = new Request();
	 String serverName, connectId, userId, requestDesc, pwdType;
	
	 serverName = request.getParameter("checkList");
	 connectId = request.getParameter("connectId");
	 userId = (String)session.getAttribute("userId");
	 requestDesc = request.getParameter("requestDesc");
	 pwdType = "OTP";
	 
	 DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 Date now = new Date();
	 String requestDate = df.format(now);
	 
	 rq.setServerName(serverName);
	 rq.setConnectId(connectId);
	 rq.setRequestDate(requestDate);
	 rq.setUserId(userId);
	 rq.setRequestDesc(requestDesc);
	 rq.setPwdType(pwdType);
	 
	 requestService.insertRequest(rq);
	 
	 
	 return "";
 }
  
 @RequestMapping("userList.do")
 public String userList(HttpServletRequest request, HttpSession session) throws Exception{
    
  ArrayList<User> list = new ArrayList<User>();
  
  for (int i = 0; i < 6; i++) {
   User user = new User();
   user.setUserId("asd"+i);
   user.setUserName("김동진"+i);
   user.setUserDepartment("개발"+i+"팀");
   list.add(user);
  }
  
  request.setAttribute("userList",list);
  
  
  return "userList";
 }
 
 
 
 @RequestMapping("/userPeriodPwd.do")
 public String scheduled(HttpServletResponse response, HttpServletRequest request, HttpSession sesssion) throws Exception{
  String userPeriodPwd;
  userPeriodPwd = firstOtp(); //OTP set
  return userPeriodPwd;
 }
 
	@RequestMapping(method = RequestMethod.POST, value = "/userLogin.do")
	public String userLogin(HttpServletRequest request, HttpSession session) {
		String userPwd, userId, userType;
		String loginFailed = null;
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date now = new Date();
		String loginTime = df.format(now);

		userPwd = request.getParameter("userPwd");
		userId = request.getParameter("userId");

		try {
			if (userPwd == null || userId == null)
				return "errorLogin";
			
			/*
			if (userService.isActive(userId))
				return "errorLogin";
			*/
			
			userType = userService.getUserType(userId);
			
			Log log = new Log();
			log.setLoginTime(loginTime);
			log.setUserId(userId);

			if (userType.equals("admin")) {
				if (userPwd.equals(userService.getUserPwd(userId).toString())) {
					System.out.println("success");

					userService.setActive(userId);
					
					logService.setLoginTime(log);

					System.out.println(userType);
					// ArrayList<String> loginInfo = new ArrayList<>();

					//String userIdType = userId + "," + userType;
					// loginInfo.add(userIdType);

					session.setAttribute("userId", userId);
					return "adminHome";
				} else {
					System.out.println("failure");
				}

			} else if (userType.equals("user")) {
				if (userPwd.equals(userService.getUserPwd(userId))) {
					System.out.println("success");
					//userService.setActive(userId);	
					
					logService.setLoginTime(log);
					
					session.setAttribute("userId", userId);

					return "nomalUserHome";
				} else {
					System.out.println("failure");
				}

			} else if (userType.equals("outUser")) {
				if (userPwd.equals(userService.getUserPwd(userId))) {
					System.out.println("success");
					//userService.setActive(userId);		
					
					logService.setLoginTime(log);
					
					session.setAttribute("userId", userId);
					return "outUserHome";
				} else {
					System.out.println("failure");
				}
			}

		} catch (NullPointerException e) {
			loginFailed = "failure";
			request.setAttribute("failure", loginFailed);
			return "errorLogin";
		} catch (Exception e) {
			e.printStackTrace();
			return "errorLogin";
		}
		return "errorLogin";
	}
 
 @RequestMapping("/userLogout.do")
 public String userLogout(HttpServletRequest request, HttpSession session){
	 
	 String userId;
	 
	 userId = (String)session.getAttribute("userId");
	 session.invalidate();
	 //request.setAttribute("message", "정상적으로 로그아웃 되었습니다.");
	 
	 try {
		 userService.setDeactive(userId);		 
	 }
	 catch (Exception e) {
		 e.printStackTrace();
	 }
	 return "logoutSuccess";
	 
 }

 @ResponseBody
 @RequestMapping(value="/checkUserId.do",method=RequestMethod.POST)
 public void checkUserId (String userId, HttpServletResponse response) throws Exception{
	 System.out.println("userId : "+userId);
	 System.out.println(userService.getUserId(userId));
	 //userId = userService.getUserId(userId);
  
  
  
  StringBuffer buffer = new StringBuffer();
  buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
  buffer.append("<result>");
  if("0".equals(userService.getUserId(userId))){
	  buffer.append("사용가능");
  }else{
	  buffer.append("사용불가");
  }
  
  buffer.append("</result>");
  response.setContentType("text/xml;charset=utf-8");
  response.getWriter().write(buffer.toString());
 }
 

 
 /*
 @ResponseBody
 @RequestMapping(value="/checkUserId.do",method=RequestMethod.GET)
 public void checkUserId (HttpServletRequest request, HttpServletResponse response,String uid) throws Exception{
  
	 		 uid = request.getParameter("userId");
	 
	 		 System.out.println(uid);

 	        StringBuffer str = new StringBuffer();

	        str.append("<?xml version='1.0' encoding='utf-8'?>");

	        str.append("<root>");

	        if(uid.equals(userService.getUserId(uid))) {

	            str.append("true");

	        } else {

	           str.append("false");

	       }

	       str.append("<id>" + uid + "</id>");

	       str.append("</root>");

	       

	       response.setContentType("text/xml;charset=utf-8");

	       response.getWriter().write(str.toString());
	  
	  
	  

	  StringBuffer buffer = new StringBuffer();
	  buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
	  buffer.append("<result>");
	  if(uid != null){
		  buffer.append("no");
	  }else{
		  buffer.append("ok");
	  }
	  
	  buffer.append("</result>");
	  response.setContentType("text/xml;charset=utf-8");
	  response.getWriter().write(buffer.toString());

 }
 */
 @ResponseBody
 @RequestMapping(value="tcpCheck", method=RequestMethod.GET)
 public void tcpCheck(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, IOException{
	 String ip = request.getParameter("ip");
	 boolean checkResult=false;
	 JSONObject jsonObj = new JSONObject();
	 
	 try {
		if (ip!=null && !ip.equals("")) {
			InetAddress pingCheck;
			pingCheck = InetAddress.getByName(ip);
			checkResult = pingCheck.isReachable(1000);
			
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	 if(checkResult){
		 jsonObj.put("result", "SUCCESS");
	 }else {
		jsonObj.put("result", "FAILURE");
	}
	 response.getOutputStream().write(jsonObj.toString().getBytes("UTF-8"));
 }
 
 
 

} // end of class