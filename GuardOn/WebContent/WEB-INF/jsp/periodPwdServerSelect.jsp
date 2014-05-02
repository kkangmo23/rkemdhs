<%@ page contentType="text/html; charset=utf-8" language="java" import="java.sql.*" errorPage="" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<% String cp=request.getContextPath(); %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>무제 문서</title>
<link rel="stylesheet" href="<%=cp%>/style/basic.css" type="text/css" media="print, projection, screen" />
<link rel="stylesheet" href="<%=cp%>/js/__jquery.tablesorter/themes/blue/style.css" type="text/css" media="print, projection, screen" />

<script type="text/javascript" src="<%=cp%>/js/plugin/jquery-2.1.0.min.js"></script>
<script type="text/javascript" src="<%=cp%>/js/__jquery.tablesorter/jquery-latest.js"></script>
<script type="text/javascript" src="<%=cp%>/js/__jquery.tablesorter/jquery.tablesorter.js"></script>
<script type="text/javascript" src="<%=cp%>/js/__jquery.tablesorter/jquery.tablesorter.min.js"></script>
<script type="text/javascript" src="<%=cp%>/js/__jquery.tablesorter/jquery.metadata.js"></script>
<script type="text/javascript" src="<%=cp%>/js/__jquery.tablesorter/addons/pager/jquery.tablesorter.pager.js"></script>

<link rel="stylesheet" href="<%=cp%>/js/jquery/1.10.4/jquery-ui.css" />

<script src="<%=cp%>/js/jquery/1.10.4/jquery.js"></script>
<script src="<%=cp%>/js/jquery/1.10.4/jquery-ui.js"></script>


<script language="javascript">
$(function() {
  var dates = $( "#from, #to " ).datepicker({
  prevText: '이전 달',
  nextText: '다음 달',
  monthNames: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'],
  monthNamesShort: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'],
  dayNames: ['일','월','화','수','목','금','토'],
  dayNamesShort: ['일','월','화','수','목','금','토'],
  dayNamesMin: ['일','월','화','수','목','금','토'],
  dateFormat: 'yy-mm-dd',
  showMonthAfterYear: true,
  yearSuffix: '년',
  onSelect: function( selectedDate ) {
    var option = this.id == "from" ? "minDate" : "maxDate",
      instance = $( this ).data( "datepicker" ),
      date = $.datepicker.parseDate(
        instance.settings.dateFormat ||
        $.datepicker._defaults.dateFormat,
        selectedDate, instance.settings );
    dates.not( this ).datepicker( "option", option, date );
  }
  });
});
</script>
<!-- 
<script type="text/javascript">
	$(document).ready(function(){

		asyncPaging.title.previous = "<";
		asyncPaging.title.next = ">";
		asyncPaging.onclick = "pageMove()";
		asyncPaging.init("paging", Number($("#serverListCount").val()), 15, Number($("#currentPage").val()));

		var serverIps = $(".server-ip");
		for(var i=0; i<serverIps.length; i++){
			tcpCheck({ ip: serverIps[i].innerHTML.split(":")[0] }, function(resp, index){
				var result = JSON.parse(resp).result;
				$(".tcp-check")[index].innerHTML = result;
			}, i);
		}
		
		
	});
	
	var tcpCheckCallCount = 10;
	function tcpCheck(data, callback, index){
		setTimeout(function(){
			$.ajax({
				type: "GET",
				cache: false,
				url: "./tcpCheck.do",
				contentType : "application/x-www-form-urlencoded; charset=UTF-8",
				data: data,
				success: function(resp, textStatus, jqXHR){
					callback(resp, index);
				}, 
				failure: function(resp, textStatus, jqXHR){
					$(".tcp-check")[index].innerHTML = "FAILURE(ERROR)";
				}
			});	
		}, index*500);
	}
	
	function pageMove(){
		location.href = "./dbList.do?page="+asyncPaging.options.currentPage;
	}
</script>

<script type="text/javascript">
$(function() {		
	$("#tablesorter-demo").tablesorter({sortList:[[0,0],[2,1]], widgets: ['zebra']});
	$("#options").tablesorter({sortList: [[0,0]], headers: { 3:{sorter: false}, 4:{sorter: false}}});
});	

</script>
 -->
</head>

<body>
     <input type="hidden" id="message" value="${message}" name="message"/>
<div class="container">
  <div class="header"><strong>
    <!-- end .header -->
  Guard-ON</strong> 주기적 비밀번호 발급</div>
  <div class="sidebar1">
    <ul class="nav">
      <li><a href="userOtpPretreatment.do">일회용 비밀번호 발급</a></li>
      <li><a href="periodPwdServerSelect.do">주기적 비밀번호 발급요청</a></li>
      <li><a href="periodPwdList.do">주기적 비밀번호 발급현황</a></li>
      <li><a href="updateUser.do">개인정보 수정</a></li>
    </ul>
    <p>&nbsp;</p>
  <!-- end .sidebar1 --></div>
  <div class="content">
 
  <form action="periodPwdApproval.do" method="post">
   <center>
  <table id="tablesorter-demo" class="tablesorter" border="0" cellpadding="0" cellspacing="1">
 <thead>
     	<tr>
        	<th></th>
            <th>서버명</th>
            <th>IP주소</th>
            <th>서버설명</th>
            <th>Health Check</th>
        </tr>
 </thead>
         <c:forEach var="i" items="${serverList}">
         	<tr>
              <td><input type="radio" name="checkList" value="${i.serverName}" /></td>
              <td>${i.serverName}</td>
              <td class="server-ip">${i.ipAddress}</td>
              <td>${i.serverDesc}</td>
              <td class="tcp-check"></td>
           </tr>   
         </c:forEach>
     </table>
     <br/>
	<br/>
          </center>
          
    <br/>
    <center>
         <table>
     
     <tr>
     	<th>접속 아이디 </th>
     	<td><input type="text" id="connectId" name="connectId"/></td>
     </tr>  
     <tr>
     	<th>요청  사유 </th>
     	<td>
     	<textarea name="requestDesc" id="requestDesc" rows="3" cols="60" onkeyup="fc_chk_byte(this,100);" onkeyup="fc_chk2()" onkeypress="fc_chk2()"></textarea>
     	<span id='z'>0/100</span>
     	</td>
     	</tr>
     	<tr>
     	<th>사용   기간: </th>
     	<td><input type="text" id="from" name="startDate"> ~ <input type="text" id="to" name="endDate"></td>
     </tr>      
		
	</table>
     <p> </p>
     <br/>
     <br/>
      <input type="submit"  value="주기적 비밀번호 얻기"/>
		</center>
          </form>

     <div id="paging" style="height:100px;"></div>
     <input id="currentPage" type="hidden" value="${page}" />
     <input id="serverListCount" type="hidden" value="${serverListCnt}" />
   	<br/>
	<br/>
	 <center>

 	<br/>
	<br/>
   
   </center>
    <br/>
	<br/>

	
    <!-- end .content --></div>
  <div class="footer">
    <p>Trust. Technology. Service</p>
    <!-- end .footer --></div>
  <!-- end .container --></div>
  <!-- 
    <script language="javascript">
	$(document).ready(function(){

		asyncPaging.title.previous = "<";
		asyncPaging.title.next = ">";
		asyncPaging.onclick = "pageMove()";
		asyncPaging.init("paging", Number($("#serverListCount").val()), 15, Number($("#currentPage").val()));

		var serverIps = $(".server-ip");
		for(var i=0; i<serverIps.length; i++){
			tcpCheck({ ip: serverIps[i].innerHTML.split(":")[0] }, function(resp, index){
				var result = JSON.parse(resp).result;
				$(".tcp-check")[index].innerHTML = result;
			}, i);
		}
		
		
	});
	
	var tcpCheckCallCount = 10;
	function tcpCheck(data, callback, index){
		setTimeout(function(){
			$.ajax({
				type: "GET",
				cache: false,
				url: "./tcpCheck.do",
				contentType : "application/x-www-form-urlencoded; charset=UTF-8",
				data: data,
				success: function(resp, textStatus, jqXHR){
					callback(resp, index);
				}, 
				failure: function(resp, textStatus, jqXHR){
					$(".tcp-check")[index].innerHTML = "FAILURE(ERROR)";
				}
			});	
		}, index*500);
	}
	
	function pageMove(){
		location.href = "./dbList.do?page="+asyncPaging.options.currentPage;
	}
</script>
 -->
</body>
</html>
