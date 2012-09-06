<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<!-- Website template by freewebsitetemplates.com -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8" language="java"
import="java.sql.*" errorPage="" %>

<html>
<head>
	<meta charset="UTF-8" />
	<title>Academic Devoir - IME USP</title>
	<link rel="stylesheet" type="text/css" charset="utf-8" media="screen" href="<c:url value="/css/style.css"/>"/>
	<link rel="stylesheet" type="text/css" charset="utf-8" media="screen" href="<c:url value="/css/menu.css"/>"/>
	<!--[if IE 9]>
		<link rel="stylesheet" type="text/css" charset="utf-8" media="screen" href="<c:url value="/css/ie9.css"/>"/>
	<![endif]-->
	<!--[if IE 8]>
		<link rel="stylesheet" type="text/css" charset="utf-8" media="screen" href="<c:url value="/css/ie8.css"/>"/>
	<![endif]-->
	<!--[if IE 7]>
		<link rel="stylesheet" type="text/css" charset="utf-8" media="screen" href="<c:url value="/css/ie7.css"/>"/>
	<![endif]-->
</head>

<body>
<%@ include file="/layout/header.jsp" %>
<div id="content">
	<div id="body">	
	
		<div id="login">
				<form id="form_login" action="<c:url value="/passwords"/>" method="post">   
		                <fieldset> 
				<legend>Digite seu e-mail e nós enviaremos instruções de redefinição de senha</legend><br/>
						<table>
						<tr>
		                    <td><label for="usuario.login">e-mail:</label></td> 
							<td><input type="text" size="20" name="email" value="${usuario.login}" /></td>
		                </tr>
	        
						<tr>
							<td>&nbsp;</td>
							<td><input type="submit" value="Enviar" id="send" class="loginbuttom"/> </td>
						</tr>  						
						</table>
         	</fieldset>
				<c:forEach var="error" items="${errors}">
					<li style="color:red"> ${error.message}</li>
				</c:forEach>
				</form> 
		</div>
	</div>				
</div>   
<%@ include file="/layout/footer.jsp" %>
</body>
</html>
