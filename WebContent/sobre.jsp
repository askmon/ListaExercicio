<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<!-- Website template by freewebsitetemplates.com -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"
	language="java" import="java.sql.*" errorPage=""%>

<html>
<head>
<meta charset="UTF-8" />
<title>Academic Devoir - IME USP</title>
<script type="text/javascript" src="javascript/jquery-1.7.1.min.js"></script>
<link rel="stylesheet" type="text/css" charset="utf-8" media="screen"
	href="<c:url value="/css/style.css"/>" />
<link rel="stylesheet" type="text/css" charset="utf-8" media="screen"
	href="<c:url value="/css/menu.css"/>" />
<!--[if IE 9]>
		<link rel="stylesheet" type="text/css" charset="utf-8" media="screen" href="<c:url value="/css/ie9.css"/>"/>
	<![endif]-->
<!--[if IE 8]>
		<link rel="stylesheet" type="text/css" charset="utf-8" media="screen" href="<c:url value="/css/ie8.css"/>"/>
	<![endif]-->
<!--[if IE 7]>
		<link rel="stylesheet" type="text/css" charset="utf-8" media="screen" href="<c:url value="/css/ie7.css"/>"/>
	<![endif]-->

<script>
	var jQ = jQuery.noConflict();

	function desliza(id) {

		jQ('html,body').animate({
			scrollTop : jQ("#" + id).offset().top
		}, 'slow');
	}
</script>
</head>

<body>
	<%@ include file="/layout/header.jsp"%>
	<div id="content">
		<div id="body" style="height: 8001;">

			<h1>Índice:</h1>
			<ul>
				<li><a href="javascript:void(0);" onclick="desliza(1);">Sobre</a></li>
				<li><a href="javascript:void(0);" onclick="desliza(2);">Integrantes atuais</a></li>
				<li><a href="javascript:void(0);" onclick="desliza(3);">Tecnologias usadas</a></li>
				<li><a href="javascript:void(0);" onclick="desliza(4);">Repositório</a></li>
			</ul>
			<hr/>
			<div id="1">
				<h1>Sobre:</h1>
				<br /> <br /> <br /> <br /> <br /> <br /> <br /> <br /> <br />
				<br /> <br /> <br /> <br /> <br /> <br /> <br /> <br /> <br />
				<br /> <br /> <br />
				<p></p>
			</div>
			<hr/>
			<div id="2">
				<h1>Integrantes atuais:</h1>

				<ul>
					<li style="padding-bottom:10px">Alessandro Calò</li>
					<li style="padding-bottom:10px">André Mesquita Pereira</li>
					<li style="padding-bottom:10px">André Spanguero Kanayama</li>
					<li style="padding-bottom:10px">Daniel Cortez</li>
					<li style="padding-bottom:10px">Eduardo Dias Filho</li>
					<li style="padding-bottom:10px">Graziela Simone Tonin</li>
					<li><a target=0
						href="http://www.linux.ime.usp.br/~ask/ren.gif">Renato Scaroni</a></li>
				</ul>
			</div>
			<hr/>
			<div id="3">
				<h1>Tecnologias usadas:</h1>
				<br />
			</div>
			<hr/>
			<div id="4">
				<h1>Repositório:</h1>
				<br />
			</div>
			<hr/>
		</div>
	</div>
	<%@ include file="/layout/footer.jsp"%>
</body>
</html>