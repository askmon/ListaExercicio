<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


	<label for="turmas">Turmas:</label>
	<br />
	<c:forEach items="${turmas}" var="turma">
		<input type="checkbox" name="idsDaTurma[]" value="${turma.id }" />
		 ${turma.nome }
		<br />
	</c:forEach>

	
	<label for="tags">Tags:</label>
	<br />
	<c:forEach items="${tags}" var="tag">
		<input type="checkbox" name="tags[].tag.id" value="${tag.id }" />
		 ${tag.nome }<br/>
		<input type="text" name="tags[].quantidade" maxlength="4" value="0" />
		Disponiveis: ${fn:length(tag.questoes)}
		<br />
	</c:forEach>
	