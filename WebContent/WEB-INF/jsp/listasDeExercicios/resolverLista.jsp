<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<!-- Website template by freewebsitetemplates.com -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8" language="java"
import="java.sql.*" errorPage="" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
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
	
<script type="text/javascript" charset="utf-8" src="<c:url value="/javascript/jquery-1.7.1.min.js"/>"></script>
<script type="text/javascript" charset="utf-8" src="<c:url value="/javascript/jquery.form.js"/>"></script>

<script type="text/javascript" charset="utf-8">	
	function redireciona() {		
		if ($('#acao0').val() == 1) {
			window.location.href = '<c:url value="/alunos/home"/>';	
		}
		else {
			window.location.href = '<c:url value="/respostas/autocorrecao/${listaDeRespostas.id}/turma/${turma.id}"/>';
		}
			
	}

	$(document).ready(function () {
		var restantes = ${numeroDeQuestoes };
		
		<c:forEach items="${questoes}" varStatus="iteracao">
			$('#questao' + ${iteracao.index}).ajaxForm({
				success: function() {
					<c:choose>
						<c:when test="${iteracao.index eq numeroDeQuestoes - 1}">redireciona();</c:when>
						<c:otherwise>$('#questao' + ${iteracao.index + 1}).submit();</c:otherwise>
					</c:choose>	
		        }
			});		
		</c:forEach>
		
		 $('#salvaRespostas').click(function() {
				$(this).attr("disabled", "disabled").empty().append("Salvando...");
				$('#enviaRespostas').hide();
				$('#acao0').val(1);
				$('#questao0').submit();
			});
	       
			$('#enviaRespostas').click(function() {
				$(this).attr("disabled", "disabled").empty().append("Enviando...");
				$('#salvaRespostas').hide();
				$('#acao0').val(2);
				$('#questao0').submit();
			});
			
			$('#finalizar').click(function() {
				$(this).attr("disabled", "disabled").empty().append("Enviando...");
				$('#salvaRespostas').hide();
				$('#enviaRespostas').hide();
				$('#acao0').val(3);
				$('#questao0').submit();
			});

		});
</script>

</head>

<body>
<%@ include file="/layout/header.jsp" %>
<div id="content">
	<div id="body">
  	<table border="0">
    	<tr>
      		<td width="200" align="center" valign="top">	
				<%@ include file="/layout/menu.jsp" %>
			</td>
			<td width="750" align="left" valign="top">    
				<div class="welcome">Você acessou como ${usuarioSession.usuario.nome } (<a href="<c:url value='/logout'/>">Sair</a>)</div>
				<h1>Resolver lista de exerc&iacute;cios</h1>
	
	<table>
		<tr>
			<td>${listaDeExercicios.propriedades.nome}</td>
		</tr>
		<tr>
			<td>${listaDeExercicios.propriedades.enunciado}</td>
		</tr>
	</table>
	
	<h1>Questões</h1>
	
	<div>
		<c:forEach items="${questoes}" var="questao" varStatus="iteracao">
			<form id="questao${iteracao.index }" class="respostaForm" action="<c:url value="/respostas/${listaDeRespostas.id }/turma/${turma.id}/cadastra"/>" method="post" accept-charset="utf-8" enctype="multipart/form-data">
				<input type="hidden" name="acao" id="acao${iteracao.index}" />
				<fieldset>
						<p>${iteracao.index + 1} )
								${questao.enunciado}</p>
						${questao.renderizacao}
				</fieldset>
			</form>
		</c:forEach>
		<button id="salvaRespostas" type="button">Salvar</button>
		<button id="enviaRespostas" type="button">Salvar e Testar</button>
		<button id="finalizar" type="button">Finalizar</button>
		
	</div>


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