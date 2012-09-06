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
	
<script type="text/javascript" charset="utf-8" src="<c:url value="/javascript/jquery-1.7.1.min.js"/>"></script>

<script type="text/javascript" charset="utf-8">

	function trocaOrdem(numero1, numero2, ordemIni1, ordemIni2) {
		var container1;
		var container2;
		
		container1 = $('#questaoContainer' + numero1).children().detach();
		container2 = $('#questaoContainer' + numero2).children().detach();		
		
		var ordem1 = $('#ordem' + ordemIni1).val();
		var ordem2 = $('#ordem' + ordemIni2).val();
		
		container1.appendTo('#questaoContainer' + numero2);
		container2.appendTo('#questaoContainer' + numero1);
		
		$('#ordem' + ordemIni1).val(ordem2);
		$('#ordem' + ordemIni2).val(ordem1);
	}
	
	$(document).ready(function() {
		var aux;
		var numero1;
		var numero2;
		var ordemIni1;
		var ordemIni2;
		var numeroDeQuestoes = ${numeroDeQuestoes };
		
		$('#aux').hide();
				
		$('.sobe').click(function() {
			aux = $(this).parent().parent().attr('id');
			numero1 = parseInt( aux.match(/\d+/g) );
			numero2 = numero1 - 1;			
			
			aux = $(this).parent().attr('id');
			ordemIni1 = parseInt( aux.match(/\d+/g) );
			aux = $('#questaoContainer' + numero2 + ' td').last().attr('id');
			ordemIni2 = parseInt( aux.match(/\d+/g) );
			
			if (numero1 != 0 && numeroDeQuestoes > 1)
				trocaOrdem(numero1, numero2, ordemIni1, ordemIni2);
		});
		
		$('.desce').click(function() {
			aux = $(this).parent().parent().attr('id');
			numero1 = parseInt( aux.match(/\d+/g) );
			numero2 = numero1 + 1;			
			
			aux = $(this).parent().attr('id');
			ordemIni1 = parseInt( aux.match(/\d+/g) );
			aux = $('#questaoContainer' + numero2 + ' td').last().attr('id'); 
			ordemIni2 = parseInt( aux.match(/\d+/g) );
			
			if (numero1 != numeroDeQuestoes - 1)
				trocaOrdem(numero1, numero2, ordemIni1, ordemIni2);
		});
		
		$('#salvar').click(function() {
			$('#trocaOrdem').submit();
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
      		<td width="200" align="center">	
				<%@ include file="/layout/menu.jsp" %>
			</td>
			<td width="750" align="left" valign="top">    


				<div class="welcome">Você acessou como ${usuarioSession.usuario.nome } (<a href="<c:url value='/logout'/>">Sair</a>)</div>
				<h1>Propriedades da Lista</h1>

	<table>
		<tr>
			<td>Nome:</td>
			<td>${listaDeExercicios.propriedades.nome }</td>
		</tr>
		<tr>
			<td>Enunciado:</td>
			<td>${listaDeExercicios.propriedades.enunciado }</td>
		</tr>
		<tr>
			<td>Turmas:</td>
			<td>
			<c:forEach items="${listaDeExercicios.turmas}" var="turma">
				 ${turma.nome }
				<br />
			</c:forEach>
			</td>
		</tr>
		<tr>
			<td>Visível:</td>
			<td>
				<c:choose>
					<c:when test="${listaDeExercicios.propriedades.visivel }">Sim</c:when>
					<c:otherwise>Não</c:otherwise>
				</c:choose>
			</td>
		</tr>
		<tr>
			<td>Peso:</td>
			<td>${listaDeExercicios.propriedades.peso }</td>
		</tr>
		<tr>
			<td>Prazo de Entrega:</td>
			<td>${prazo }</td>
		</tr>
		<tr>
			<td>Número Máximo de Envios:</td>
			<td>
				<c:choose>
					<c:when test="${empty (listaDeExercicios.propriedades.numeroMaximoDeEnvios) or (listaDeExercicios.propriedades.numeroMaximoDeEnvios eq 0)}">
						Ilimitado
					</c:when>
					<c:otherwise>
						${listaDeExercicios.propriedades.numeroMaximoDeEnvios }
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
		<tr>
			<td>Auto Correção:</td>
			<td>
				<c:choose>
					<c:when test="${listaDeExercicios.propriedades.autoCorrecao eq 'DESATIVADA' }">
						Desabilitada
					</c:when>
					<c:otherwise>
						Habilitada
					</c:otherwise>
				</c:choose>
			</td>
		</tr>		
	</table>
	<form action="<c:url value="/listasDeExercicios/altera/${listaDeExercicios.id }"/>">
		<fieldset class="fieldsetSemFormatacao">
			<button type="submit">Alterar</button><br/>
			<a href="<c:url value='/listasDeExercicios/autocorrecao/${listaDeExercicios.id }'/>">Correção Automática</a>
		</fieldset>
	</form>
	
	<form action="<c:url value="/listasDeExercicios/${listaDeExercicios.id }"/>" method="post">
		<fieldset class="fieldsetSemFormatacao">
			<button type="submit" name="_method" value="delete">Excluir Lista</button>
		</fieldset>
	</form>
	
	<c:if test="${!listaDeExercicios.propriedades.geracaoAutomatica}">
	<h3>Questões</h3>
	
	<div>
		<table>
			<c:forEach items="${listaDeExercicios.questoes }" var="questao" varStatus="status">							
				<tr id="questaoContainer${status.index }">
					<td>${status.index+1 }. ${questao.enunciado }</td>
					<td>
						<form action="<c:url value="/listasDeExercicios/${listaDeExercicios.id }/questoes/${status.index }"/>" method="post">
							<fieldset class="fieldsetSemFormatacao">
								<button type="submit" name="_method" value="delete">Remover</button>
							</fieldset>
						</form>
					</td>
					<td id="questao${status.index }">
						<button class="sobe" ></button><br/>
						<button class="desce" ></button>						
					</td>
				</tr>
			</c:forEach>
		</table>
		<a href="<c:url value='${listaDeExercicios.id }/inclusaoQuestoes?proxPagina=1&filtro='/>">Incluir nova Questão</a>
		</c:if>
	</div>
	</div>
	</div>
	
	<div id="aux" style="display: none">
		<form id="trocaOrdem" action="<c:url value="/listasDeExercicios/trocaOrdem/${listaDeExercicios.id }"/>">
			<c:forEach items="${listaDeExercicios.questoes }" var="questaoDaLista" varStatus="status">
				<input id="ordem${status.index }" name="novaOrdem[${status.index }]" value="${status.index }"/>
			</c:forEach>	
		</form>


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