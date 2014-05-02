<%@ page contentType="text/html; charset=utf-8" language="java" import="java.sql.*" errorPage=""%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<% String cp = request.getContextPath(); %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<script LANGUAGE="javascript">
		var valPwd;

		function sub() {
			var form = document.forms['password_form'];
			
			if(valPwd==true){
			form.action = 'updateUserInfo.do';
			form.submit();
			}
			else{ 
				alert("비밀번호 또는 아이디 중복 체크를 확인하십시오");
			}
			
			}
		
		function verifynotify(field1, field2, result_id, match_html, nomatch_html) {
		  this.field1 = field1;
		  this.field2 = field2;
		  this.result_id = result_id;
		  this.match_html = match_html;
		  this.nomatch_html = nomatch_html;
		
		  this.check = function() {
		    // Make sure we don't cause an error
		    // for browsers that do not support getElementById
		    if (!this.result_id) { return false; }
		    if (!document.getElementById){ return false; }
		    r = document.getElementById(this.result_id);
		    if (!r){ return false; }
		
		    if (this.field1.value != "" && this.field1.value == this.field2.value) {
		      r.innerHTML = this.match_html;
		      valPwd=true;
		    } else {
		      r.innerHTML = this.nomatch_html;
		      valPwd=false;
		    }
		  }
		}
		
		function verifyInput() {
		  verify = new verifynotify();
		  verify.field1 = document.password_form.userPwd;
		  verify.field2 = document.password_form.confirmPwd;
		  verify.result_id = "password_result";
		  verify.match_html = "<span style=\"color:blue\">패스워드가 일치합니다!<\/span>";
		  verify.nomatch_html = "<span style=\"color:red\">귀하의 비밀 번호가 일치하는지 확인하십시오!<\/span>";
		
		  // Update the result message
		  verify.check();
		}
		
		// Multiple onload function created by: Simon Willison
		// http://simonwillison.net/2004/May/26/addLoadEvent/
		function addLoadEvent(func) {
		  var oldonload = window.onload;
		  if (typeof window.onload != 'function') {
		    window.onload = func;
		  } else {
		    window.onload = function() {
		      if (oldonload) {
		        oldonload();
		      }
		      func();
		    }
		  }
		}
		
		addLoadEvent(function() {
		  verifyInput();
		});
		
		</script>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>무제 문서</title>
		<link rel="stylesheet" href="<%=cp%>/style/basic.css" type="text/css"
			media="print, projection, screen" />
		
		
		</head>

<body>

	<div class="container">
		<div class="header">
			<strong> <!-- end .header --> Guard-ON
			</strong> 개인 정보 수정
		</div>
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
			<center>
				<br /> <br />
				<form action="updateUserInfo.do" method="post" name="password_form">					

					<table width="950" height="702" class="table" border="0">

		<tr>
          <td width="150" class="table2"><font class="h2">&nbsp;이 름</font></td>
          <td class="table3"><input type="text" name="userName" value=${userName } class="format" style="width:200px; height:20px;"/></td>
          <td width="150" rowspan="2" class="table3"></td>
        </tr>

		 <tr>
          <td class="table2"><font class="h2">&nbsp;비밀번호</font></td>
          <td class="table3"><input type="password" name="userPwd" class="format" style="width:200px; height:20px;"
          onkeyup="verify.check()"/></td>
          <td class="table3">&nbsp;</td>
        </tr>
        <tr>
          <td class="table2"><font class="h2">&nbsp;비밀번호 확인</font></td>
          <td class="table3"><input type="password" name="confirmPwd" class="format" style="width:200px; height:20px;"
          onkeyup="verify.check()"/></td>
          <td class="table3">&nbsp;</td>
        </tr>
        <tr>
        <td class="table2"></td>
        <td id="password_result"></td>
        </tr>
		 <tr>
          <td class="table2" ><font class="h2">&nbsp;부서</font></td>
          <td class="table3"><input type="text" name="userDepartment" value=${userDepartment } class="format"/></td>
        </tr>
        <tr>
          <td class="table2" ><font class="h2">&nbsp;직급</font></td>
          <td class="table3"><input type="text" name="userLevel" value=${userLevel } class="format"/></td>
        </tr>
        <tr>
         <td class="table2" ><font class="h2">&nbsp;사번</font></td>
         <td class="table3"><input type="text" name="companyNumber" value=${companyNumber } class="format" style="width:200px; height:20px;"/>
        </tr>
        <tr>
         <td class="table2" ><font class="h2">&nbsp;E-Mail</font></td>
         <td class="table3"><input type="text" name="userEmail" value=${userEmail } class="format" style="width:200px; height:20px;"/>
        </tr>
        <tr>
        <td class="table2"><font class="h2">&nbsp;전화번호</font></td>
          <td class="table3"><select name="phoneNumber" class="select" style="width:100px; height:25px;">
        <option value="010" >&nbsp; &nbsp;010&nbsp; &nbsp;</option>
        <option value="011" >&nbsp; &nbsp;011&nbsp; &nbsp;</option>
        <option value="016" >&nbsp; &nbsp;016&nbsp; &nbsp;</option>
        <option value="017" >&nbsp; &nbsp;017&nbsp; &nbsp;</option>
        <option value="018" >&nbsp; &nbsp;018&nbsp; &nbsp;</option>
        <option value="019" >&nbsp; &nbsp;019&nbsp; &nbsp;</option>
        </select>&nbsp; &nbsp;-&nbsp; &nbsp;
       <input name="userCp2" type="text" maxlength="4" style="width:100px; height:20px;" value=${userCp2 } class="format"/>
       &nbsp; &nbsp;-&nbsp; &nbsp; 
       <input name="userCp3" type="text" maxlength="4" style="width:100px; height:20px;" value=${userCp3 } class="format"/></td>
          <td class="table3">&nbsp;</td>
        </tr>
					</table>
					<br>
					<div id="apDiv1">
						<!-- 
    <input type="button" value="가입" class="button" onClick="sendIt;"/>
  <input type="button" value="취소" class="button" onclick="javascript:history.back()" />
   -->
						<input type="button" value="수정" class="button" onclick="sub();"/> <input
							type="button" value="취소" class="button"
							onclick="javascript:history.back()" />
					</div>
				</form>

				<br /> <br />



				<!-- end .content -->
			</center>
		</div>
		<div class="footer">
			<p>Trust. Technology. Service</p>
			<!-- end .footer -->
		</div>
		<!-- end .container -->
	</div>
</body>
</html>
