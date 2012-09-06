<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<!-- Website template by freewebsitetemplates.com -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8" language="java"
import="java.sql.*" errorPage="" %>

<html>
<head>
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
  	<table border="0">
    	<tr>
      		<td width="200" align="center">	
				<%@ include file="/layout/menu.jsp" %>
			</td>
			<td width="750" align="left" valign="top">    


				<div class="welcome">Você acessou como ${usuarioSession.usuario.nome } (<a href="<c:url value='/logout'/>">Sair</a>)</div>
				<h1><a href="index.html">Lista de Professores</a></h1>

    <form action='cadastro'>
       	<a href="<c:url value='/professores/cadastro'/>">Inserir</a><br/>
    </form>

	
	<table>
    <thead>
        <tr>
        <th>id</th>
        <th>Nome</th>
        <th>E-mail</th>
        <th>Login</th>
        <th>Senha</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach items="${listaDeProfessores}" var="professor">
            <tr>
                <td>${professor.id}</td>
                <td>${professor.nome}</td>
                <td>${professor.email}</td>
                <td>${professor.login}</td>
                <td>${professor.senha}</td>
                <td><a href="./alteracao?id=${professor.id}">  Alterar</a> <a href="./remove?id=${professor.id}">  Excluir</a></td>
            </tr>
        </c:forEach>
        </form>
    </tbody>
</table>
<a href="<c:url value='/login'/>">Sair</a>
<a href="<c:url value='/professores/home'/>">Voltar</a><br/>

	

			</td>
		</tr>
	</table>    			
	<!-- body -->
	</div>
<!-- content -->
</div>
<%@ include file="/layout/footer.jsp" %>
</body>
</html>