<%@ page contentType="text/html; charset=UTF-8"%>
<%
 String cp = request.getContextPath();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>무제 문서</title>
<link rel="stylesheet" type="text/css" href="<%=cp %>/style/portal/magic_common.css" />
<link rel="stylesheet" href="<%=cp%>/style/basic.css" type="text/css" media="print, projection, screen" />

</head>

<body>

<div class="container">
  <div class="header"><strong>
    <!-- end .header -->
  Guard-ON</strong> LogIn</div>

  <div>
  <center>
<form action="index.do" method="post">
<br/>
<br/>
<br/>
<br/>
<center>
성공적으로 로그아웃 되었습니다.
<br/>
<br/>
<br/>
<br/>
<input type="submit" value="홈으로" style="height:50px; width:100px;"/>
</center>
<br/>
<br/>
</form>
<br/>
<br/>
</center>
    <!-- end .content --></div>
  <div class="footer">
    <p>Trust. Technology. Service</p>
    <!-- end .footer --></div>
  <!-- end .container --></div>
</body>
</html>