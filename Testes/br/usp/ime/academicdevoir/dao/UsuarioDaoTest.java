package br.usp.ime.academicdevoir.dao;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.usp.ime.academicdevoir.entidade.Usuario;
import br.usp.ime.academicdevoir.util.Given;

public class UsuarioDaoTest {

	private @Mock Session session;
	private @Mock UsuarioDao usuarioDao;
	private @Mock Transaction transaction;
	private @Mock Criteria criteria;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		when(session.beginTransaction()).thenReturn(transaction);
		usuarioDao = new UsuarioDao(session);
	}

	

	@Test
	public void naoDeveFazerLogin() {
		when(session.createCriteria(Usuario.class)).thenReturn(criteria);
		when(criteria.add(any(Criterion.class))).thenReturn(criteria);
		when(criteria.uniqueResult()).thenReturn(null);
		Usuario aluno = usuarioDao.fazLogin("aluno", "aluno");
		Assert.assertNull("Aluno encontrado na base de dados", aluno);
	}
	
	@Test
	public void deveFazerLogin() {
		Usuario usuario = Given.novoUsuario();
		when(session.createCriteria(Usuario.class)).thenReturn(criteria);
		when(criteria.add(any(Criterion.class))).thenReturn(criteria);
		when(criteria.uniqueResult()).thenReturn(usuario);			
		Usuario aluno = usuarioDao.fazLogin(usuario.getLogin(), usuario.getSenha());
		Assert.assertNotNull("Aluno não encontrado na base de dados", aluno);
	}
	
	
}
