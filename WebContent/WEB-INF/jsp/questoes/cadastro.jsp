<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<!-- Website template by freewebsitetemplates.com -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"
	language="java" import="java.sql.*" errorPage=""%>

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
	<![endif]-->>

<!-- <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>-->
<link rel="stylesheet" type="text/css" charset="utf-8" media="screen"
	href="<c:url value="/css/jquery.ui.core.css"/>" />
<link rel="stylesheet" type="text/css" charset="utf-8" media="screen"
	href="<c:url value="/css/jquery.ui.theme.css"/>" />
<link rel="stylesheet" type="text/css" charset="utf-8" media="screen"
	href="<c:url value="/css/jquery.ui.autocomplete.css"/>" />
<script type="text/javascript" charset="utf-8"
	src="<c:url value="/javascript/jquery-1.7.1.min.js"/>"></script>
<script type="text/javascript" charset="utf-8"
	src="<c:url value="/javascript/jquery-ui/jquery.ui.core.min.js"/>"></script>
<script type="text/javascript" charset="utf-8"
	src="<c:url value="/javascript/jquery-ui/jquery.ui.position.min.js"/>"></script>
<script type="text/javascript" charset="utf-8"
	src="<c:url value="/javascript/jquery-ui/jquery.ui.widget.min.js"/>"></script>
<script type="text/javascript" charset="utf-8"
	src="<c:url value="/javascript/jquery-ui/jquery.ui.autocomplete.min.js"/>"></script>
<script type="text/javascript" charset="utf-8">
	function liberaAlternativas(respostaUnica, numeroDeAlternativas) {
		var i = 0;
		var tipo, outro;

		if (respostaUnica == 'checked') {
			tipo = 'Unica';
			outro = 'Multipla';
		}

		else {
			tipo = 'Multipla';
			outro = 'Unica';
		}

		while (i < numeroDeAlternativas) {
			$('#resposta' + tipo + i).removeAttr('disabled').show();
			$('#alternativa' + i).removeAttr('disabled').show();
			$('#peso' + i).removeAttr('disabled').show();
			$('#espacoAlternativas' + i).show();
			$('#espacoAlternativas2_' + i).show();
			$('#resposta' + outro + i).attr('disabled', 'disabled').hide();
			i++;
		}

		while (i < 10) {
			$('#resposta' + tipo + i).attr('disabled', 'disabled').hide();
			$('#alternativa' + i).attr('disabled', 'disabled').hide();
			$('#peso' + i).attr('disabled', 'disabled').hide();
			$('#espacoAlternativas' + i).hide();
			$('#espacoAlternativas2_' + i).hide();
			$('#resposta' + outro + i).attr('disabled', 'disabled').hide();
			i++;
		}
	}

	function habilitaMultiplaEscolha(numeroDeAlternativas) {
		$('form').attr('action', '<c:url value="/questoes/mult"/>');
		$('#tipoDeResposta').removeAttr('disabled', 'disabled');
		liberaAlternativas($('#tipoDeResposta').attr('checked'),
				numeroDeAlternativas);
		$('#questaoDeMultiplaEscolhaContainer').show();
	}

	function habilitaTexto() {
		$('form').attr('action', '<c:url value="/questoes/texto"/>');
		$('#respostaTexto').removeAttr('disabled');
		$('#questaoDeTextoContainer').show();
	}

	function habilitaCodigo() {
		$('form').attr('action', '<c:url value="/questoes/codigo"/>');
		$('#codigoDeTeste').removeAttr('disabled');
		$('#questaoDeCodigoContainer').show();
	}

	function habilitaVouF() {
		$('form').attr('action', '<c:url value="/questoes/vouf"/>');
		$('#respostaVouFVerdadeiro').removeAttr('disabled').attr('checked',
				'checked');
		$('#respostaVouFFalso').removeAttr('disabled');
		$('#questaoDeVouFContainer').show();
	}

	function desabilitaMultiplaEscolha() {
		$('#tipoDeQuestao').attr('disabled', 'disabled');
		$('.respostaDasAlternativas').attr('disabled', 'disabled');
		$('.alternativa').attr('disabled', 'disabled');
		$('#questaoDeMultiplaEscolhaContainer').hide();
	}

	function desabilitaVouF() {
		$('.respostaVouF').removeAttr('checked', 'checked').attr('disabled',
				'disabled');
		$('#questaoDeVouFContainer').hide();
	}

	function desabilitaTexto() {
		$('#respostaTexto').attr('disabled', 'disabled');
		$('#questaoDeTextoContainer').hide();
	}

	function desabilitaCodigo() {
		$('#codigoDeTeste').attr('disabled', 'disabled');
		$('#questaoDeCodigoContainer').hide();
	}

	function split(val) {
		return val.split(/,\s*/);
	}

	function extractLast(term) {
		return split(term).pop();
	}

	$(document)
			.ready(
					function() {
						var numeroDeAlternativas = 5;

						$('#questaoDeMultiplaEscolhaContainer').hide();
						$('#questaoDeVouFContainer').hide();
						$('#questaoDeTextoContainer').hide();

						$('#seletorDeTipoDeQuestao')
								.change(
										function() {
											var idSeletor = $(this).val();
											if (idSeletor == 'multiplaEscolha') {
												desabilitaTexto();
												desabilitaVouF();
												desabilitaCodigo();
												habilitaMultiplaEscolha(numeroDeAlternativas);
											} else if (idSeletor == 'codigo') {
												desabilitaMultiplaEscolha();
												desabilitaVouF();
												desabilitaTexto();
												habilitaCodigo();
											} else if (idSeletor == 'texto') {
												desabilitaMultiplaEscolha();
												desabilitaVouF();
												desabilitaCodigo();
												habilitaTexto();
											} else if (idSeletor == 'VouF') {
												desabilitaMultiplaEscolha();
												desabilitaTexto();
												desabilitaCodigo();
												habilitaVouF();
											} else {
												desabilitaMultiplaEscolha();
												desabilitaTexto();
												desabilitaVouF();
												desabilitaCodigo();
												$('form')
														.attr('action',
																'<c:url value="/questoes/submissao"/>');
											}
											$(this)
													.attr('selected',
															'selected');
										});

						$('#seletorDeAlternativas').change(
								function() {
									var valor = $(this).val();
									numeroDeAlternativas = parseInt(valor);
									liberaAlternativas($('#tipoDeResposta')
											.attr('checked'),
											numeroDeAlternativas);
								});

						$('#tipoDeResposta').change(
								function() {
									liberaAlternativas($(this).attr('checked'),
											numeroDeAlternativas);
								});

						//Adaptado do exemplo do JQuery UI
						$('#tags')
								.bind(
										'keydown',
										function(event) {
											if (event.keyCode === $.ui.keyCode.TAB
													&& $(this).data(
															'autocomplete').menu.active) {
												event.preventDefault();
											}
										})
								.autocomplete(
										{
											source : function(request, response) {
												$
														.getJSON(
																'<c:url value="/questoes/tags/autocompletar.json"/>',
																{
																	term : extractLast(request.term)
																},
																function(result) {
																	response($
																			.map(
																					result,
																					function(
																							item) {
																						return item.nome;
																					}));
																});
											},
											search : function() {
												var term = extractLast(this.value);
												if (term.length < 2) {
													return false;
												}
											},
											focus : function() {
												return false;
											},
											select : function(event, ui) {
												var terms = split(this.value);
												terms.pop();
												terms.push(ui.item.value);
												terms.push("");
												this.value = terms.join(", ");
												return false;
											}
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
				<h1><a href="index.html">Cadastro de Questões</a></h1>
			<div>
				<%@ include file="../questoes/menu.jsp"%>
			</div>
			<div>
				<br />
				<form style="width: 700px"
					action="<c:url value="/questoes/codigo"/>" method="post"
					accept-charset="utf-8">
					<br />
					<p>
				
			<c:forEach items="${errors}" var="msg">
		    	<b>${msg.message}</b><br/>
		    </c:forEach>
		    <br><br>
					
					<label>Disciplinas:</label>
					<select name="questao.disciplina.id">
						<option value="">Selecione uma disciplina</option>
						<c:forEach items="${disciplinas}" var="disciplina">
						<option value="${disciplina.id}" <c:if test="${displina.id == disciplina_id}"> selected="selected" </c:if>>${disciplina.nome }</option>
						</c:forEach>
					</select>
					</p>					
					<label for="enunciado">Enunciado:</label>
					<br />
					<textarea id="enunciado" rows="5" cols="80"
						name="questao.enunciado">${questao.enunciado }</textarea>
					<br />
					<br />

					<p>Tipo de Questão:</p>
					<select id="seletorDeTipoDeQuestao" name="tipoDeQuestao">
						<option selected="selected" value="codigo">Código</option>
						<option value="texto">Texto</option>
						<option value="multiplaEscolha">Múltipla Escolha</option>
						<option value="VouF">V ou F</option>
						<option value="submissao">Submissão de Arquivo</option>
					</select>

					<br />
					<br />
					<label for="tags">Tags:</label>
					<input id="tags" type="text" name="tags" />

					<div id="questaoDeMultiplaEscolhaContainer">
						<br /> <br />
						<p>Número de Alternativas:</p>
						<select id="seletorDeAlternativas" name="numeroDeAlternativas">
							<c:forEach begin="2" end="10" step="1" varStatus="iteracao">
								<c:choose>
									<c:when test="${iteracao.index eq 5 }">
										<option selected="selected" value="${iteracao.index }">${iteracao.index
											}</option>
									</c:when>
									<c:otherwise>
										<option value="${iteracao.index }">${iteracao.index }</option>
									</c:otherwise>
								</c:choose>
							</c:forEach>
						</select> <br />
						<p>Resposta única:</p>
						<input id="tipoDeResposta" type="checkbox" checked="checked"
							name="questao.respostaUnica" value="true" disabled="disabled" />
						<br /> <br /> <label>Alternativas:</label>
						<c:set var="valorResposta" value="1" />
						<c:forEach begin="0" end="9" step="1" varStatus="iteracao">
							<br id="espacoAlternativas${iteracao.index }" />
							<br id="espacoAlternativas2_${iteracao.index }" />
							<c:choose>
								<c:when test="${iteracao.index eq 0 }">
									<input id="respostaUnica${iteracao.index }"
										class="respostaDasAlternativas" type="radio" checked="checked"
										name="resposta[]" value="${valorResposta }"
										disabled="disabled" />
									<input id="respostaMultipla${iteracao.index }"
										class="respostaDasAlternativas" type="checkbox"
										checked="checked" name="resposta[]" value="${valorResposta }"
										disabled="disabled" />

								</c:when>
								<c:otherwise>
									<input id="respostaUnica${iteracao.index }"
										class="respostaDasAlternativas" type="radio" name="resposta[]"
										value="${valorResposta }" disabled="disabled" />
									<input id="respostaMultipla${iteracao.index }"
										class="respostaDasAlternativas" type="checkbox"
										name="resposta[]" value="${valorResposta }"
										disabled="disabled" />
								</c:otherwise>
							</c:choose>


							<c:set var="valorResposta" value="${valorResposta*2 }" />
							<input id="alternativa${iteracao.index }" class="alternativa"
								type="text" size="100" name="questao.alternativas[].alternativa"
								disabled="disabled" />
							<br />

<div id="peso${iteracao.index }" class="peso">
Peso:
<input id="peso${iteracao.index }" class="peso" type="text" 
								size="3" name="questao.alternativas[].peso" /> 
</div>

							






						</c:forEach>
					</div>

					<div id="questaoDeTextoContainer">
						<br /> <br /> <label for="respostaTexto">Resposta:</label> <br />
						<textarea id="respostaTexto" rows="5" cols="80"
							name="questao.resposta" disabled="disabled"></textarea>
					</div>

					<div id="questaoDeCodigoContainer">
						<br /> <br /> <label for="codigoDeTeste">Código de
							teste:</label> <br />
						<textarea id="codigoDeTeste" rows="5" cols="80"
							name="questao.codigoDeTeste"></textarea>
					</div>


					<div id="questaoDeVouFContainer">
						<br /> <label for="resposta">Resposta:</label> <br />
						<p>
							<input id="respostaVouFVerdadeiro" class="respostaVouF"
								type="radio" name="questao.resposta" value="true"
								disabled="disabled" />Verdadeiro
						</p>
						<p>
							<input id="respostaVouFFalso" class="respostaVouF" type="radio"
								name="questao.resposta" value="false" disabled="disabled" />Falso
						</p>
					</div>

					<div id="comentario">
						<br /> <br /> <label for="comentario">Comentário (
							feedback para o aluno ):</label> <br />
						<textarea id="comentario" rows="5" cols="80"
							name="questao.comentario"></textarea>
					</div>



					<br />
					<br />
					<button type="submit">Enviar</button>
				</form>
				<br />
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
