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
		<div id="body">
			<div id="0"></div>
			<h1>Índice:</h1>
			<ul>
				<li><a href="javascript:void(0);" onclick="desliza(1);">Sobre</a></li>
				<li><a href="javascript:void(0);" onclick="desliza(2);">Módulos
						do sistema</a></li>
				<li><a href="javascript:void(0);" onclick="desliza(3);">Integrantes
						atuais</a></li>
				<li><a href="javascript:void(0);" onclick="desliza(4);">Tecnologias
						usadas</a></li>
				<li><a href="javascript:void(0);" onclick="desliza(5);">Repositório</a></li>
			</ul>
			<hr />
			<div id="1">
				<h1>Sobre:</h1>
				<p>As disciplinas de Introdução à Computação (MAC0110 e MAC0115)
					são oferecidas para diversos institutos da USP, como IAG, IF, IGc,
					IO e IME. Os alunos em geral apresentam muitas dificuldades no
					entendimento da lógica de programação, que requer uma prática
					intensiva para ser desenvolvida. As turmas oferecidas geralmente
					possuem uma quantidade muito grande de alunos (65 alunos em média),
					o que impossibilita ao professor acompanhar individualmente cada
					aluno. Visando reduzir estes problemas, propõem-se a utilização de
					um sistema que receba e teste automaticamente os exercícios dos
					alunos. O sistema recebe o código, compila e roda uma bateria de
					testes, informando ao aluno em quais testes seu código falhou. Uma
					versão legada do sistema já está em uso em algumas turmas com bons
					resultados. Espera-se agora gerar uma nova versão mais moderna e
					completa do sistema. Este é um protótipo dessa nova versão.</p>
				<br /> <a href="javascript:void(0);" onclick="desliza(0);">Voltar
					ao índice</a>
			</div>
			<hr />
			<div id="2">
				<h1>Módulos do sistema:</h1>
				<p>O sistema está dividido em 3 grandes módulos:</p>
				<ul>
					<li>Aluno</li>
					<li>Professor</li>
					<li>Admin</li>
				</ul>
				<p>Abaixo será discorrido sobre o que está funcionando hoje em
					cada módulo.</p>
				<ul>
					<li>Aluno
						<ul>
							<li>É possível criar, alterar e excluir uma nova conta de
								usuário aluno.</li>
							<li>Imediatamente após criada o aluno consegue efetuar o
								login.</li>
							<li>Na página inicial do aluno são listas as turmas e
								disciplinas que ele está cadastrado.</li>
							<li>Usuário pode se inscrever em uma nova turma.</li>
							<li>Usuário pode sair de uma turma cadastrada.</li>
						</ul>
					</li>
					<li>Professor
						<ul>
							<li>É possível criar, alterar e excluir turmas.</li>
							<li>É possível criar, alterar e excluir disciplinas.</li>
							<li>É possível criar, alterar e excluir questões do tipo:
								<ul>
									<li>Código Java (somente corrige código java).</li>
									<li>Arquivo</li>
									<li>Múltipla Escolha.</li>
									<li>V ou F.</li>
									<li>Texto.</li>
								</ul>
								<li>É possível listar todas as questões cadastradas.</li>
								<li>É possível listar as questões cadastradas por tipo.</li>
								<li>É possível criar, editar e excluir uma lista de
									questões.</li>
								<li>É possível buscar questões por tags.</li>
								<li>É possível gerar a lista automaticamente.</li>
							</li>
						</ul>
					</li>
					<li>Admin</li>
					<ul>
						<li>É possível logar, porém módulo precisa ser refatorado.</li>
					</ul>
				</ul>
				<a href="javascript:void(0);" onclick="desliza(0);">Voltar ao
					índice</a>
			</div>
			<hr />
			<div id="3">
				<h1>Integrantes atuais:</h1>

				<ul>
					<li style="padding-bottom: 10px">Alessandro Calò</li>
					<li style="padding-bottom: 10px">André Mesquita Pereira</li>
					<li style="padding-bottom: 10px"><a target=0
						href="http://www.linux.ime.usp.br/~ask/">André Spanguero
							Kanayama</a></li>
					<li style="padding-bottom: 10px">Daniel Cortez</li>
					<li style="padding-bottom: 10px">Eduardo Dias Filho</li>
					<li style="padding-bottom: 10px">Graziela Simone Tonin</li>
					<li>Renato Scaroni</a></li>
				</ul>
				<a href="javascript:void(0);" onclick="desliza(0);">Voltar ao
					índice</a>
			</div>
			<hr />
			<div id="4">
				<h1>Tecnologias usadas:</h1>
				<ul>
					<li><a target=0 href="http://www.mysql.com/">MySQL</a></li>
					<li><a target=0 href="http://www.hibernate.org/">Hibernate</a></li>
					<li><a target=0 href="http://vraptor.caelum.com.br/pt/">VRaptor</a></li>
					<li><a target=0 href="http://jquery.com/">JQuery</a></li>
					<li><a target=0 href="http://www.eclipse.org/">Eclipse</a></li>
				</ul>
				<br /> <a href="javascript:void(0);" onclick="desliza(0);">Voltar
					ao índice</a>
			</div>
			<hr />
			<div id="5">
				<h1>Repositório:</h1>
				<a target=0 href="https://github.com/academicdevoir/ListaExercicio">Github</a>
				<br/>
				<br /> <a href="javascript:void(0);" onclick="desliza(0);">Voltar
					ao índice</a>
			</div>
			<hr />
		</div>
	</div>
	<%@ include file="/layout/footer.jsp"%>
</body>
</html>