<%@ page contentType="text/html; charset=utf-8" language="java" import="java.sql.*" errorPage="" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<% String cp=request.getContextPath(); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>무제 문서</title>

<link rel="stylesheet" href="<%=cp%>/js/__jquery.tablesorter/themes/blue/style.css" type="text/css" media="print, projection, screen" />
<link rel="stylesheet" href="<%=cp%>/style/basic.css" type="text/css" media="print, projection, screen" />

<script type="text/javascript" src="<%=cp%>/js/__jquery.tablesorter/jquery-latest.js"></script>
<script type="text/javascript" src="<%=cp%>/js/__jquery.tablesorter/jquery.tablesorter.js"></script>
<script type="text/javascript" src="<%=cp%>/js/__jquery.tablesorter/jquery.tablesorter.min.js"></script>
<script type="text/javascript" src="<%=cp%>/js/__jquery.tablesorter/jquery.metadata.js"></script>
<script type="text/javascript" src="<%=cp%>/js/__jquery.tablesorter/addons/pager/jquery.tablesorter.pager.js"></script>

<script type="text/javascript">
$(function() {		
	$("#tablesorter-demo").tablesorter({sortList:[[0,0],[2,1]], widgets: ['zebra']});
	$("#options").tablesorter({sortList: [[0,0]], headers: { 3:{sorter: false}, 4:{sorter: false}}});
});	

</script>
</head>

<body>

<div class="container">
  <div class="header"><strong>
    <!-- end .header -->
  Guard-ON</strong> 주기적 비밀번호 리스 리스트</div>

<div class="sidebar1">
    <ul class="nav">
      <li><a href="outUserOtpRequest.do">일회용 비밀번호 발급요청</a></li>
      <li><a href="outUserOtpPretreatment.do">일회용 비밀번호 발급</a></li>
      <li><a href="outPeriodPwdServerSelect.do">주기적 비밀번호 발급요청</a></li>
      <li><a href="outPeriodPwdList.do">주기적 비밀번호 발급현황</a></li>
      <li><a href="updateUser.do">개인정보 수정</a></li>
    </ul>
    <p>&nbsp;</p>
  <!-- end .sidebar1 --></div>

  <div class="content">

  	 <center>
  	 
  	      <table id="tablesorter-demo" class="tablesorter" border="0" cellpadding="0" cellspacing="1">
     	<thead>
     	<tr>
            <th>서버명</th>
            <th>승인여부</th>
            <th>접속아이디</th>
            <th>사용자아이디</th>
            <th>시작일</th>
            <th>종료일</th>
            <th>비밀번호</th>
        </tr>
        </thead>
         <c:forEach var="i" items="${periodPwdList}">
         	<tr>
              <td>${i.serverName}</td>
              <td>${i.approved}</td>
              <td>${i.connectId}</td>
              <td>${i.userId}</td>
              <td>${i.startDate}</td>
              <td>${i.endDate}</td>
              <td>${i.password}</td>
           </tr>   
         </c:forEach>
     </table>
 
 
     </center>
     <!-- end .content --></div>
  <div class="footer">
    <p>Trust. Technology. Service</p>
    <!-- end .footer --></div>
  <!-- end .container --></div>
</body>
</html>
