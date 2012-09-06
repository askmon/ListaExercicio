package br.usp.ime.academicdevoir.dao;


import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import junit.framework.Assert;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.usp.ime.academicdevoir.entidade.Aluno;
import br.usp.ime.academicdevoir.entidade.Disciplina;
import br.usp.ime.academicdevoir.entidade.Turma;



public class TurmasDaoTest{
	
	TurmaDao turmaDao;
	Disciplina disciplina;
	Aluno aluno;
	Collection<Aluno> alunos;
	Turma turmaMatriculada;
	Turma turmaNaoMatriculada;
	List<Turma> turmas;
	
	private @Mock Session session;
	private @Mock Transaction tx;
	private @Mock Criteria criteria;
	private @Mock Query query;
	
	@Before
	public void setUp(){
		MockitoAnnotations.initMocks(this);
		when(session.beginTransaction()).thenReturn(tx);
		turmaDao = new TurmaDao(session);

		disciplina = new Disciplina();
		disciplina.setId(1L);
		disciplina.setNome("MAC110");
		aluno = new Aluno();
		aluno.setId(10L);
		aluno.setNome("renato");
		
		alunos = new ArrayList<Aluno>();
		alunos.add(aluno);
		
		turmaMatriculada = new Turma();
		turmaMatriculada.setId(0L);
		turmaMatriculada.setNome("MAC110");
		turmaMatriculada.setDisciplina(disciplina);
		turmaMatriculada.setAlunos(alunos);
		
		turmaNaoMatriculada = new Turma();
		turmaNaoMatriculada.setId(1L);
		turmaNaoMatriculada.setNome("MAC110");
		turmaNaoMatriculada.setDisciplina(disciplina);
		
		turmas = new ArrayList<Turma>();
		turmas.add(turmaMatriculada);
		turmas.add(turmaNaoMatriculada);
		
		when(session.createCriteria(Turma.class)).thenReturn(criteria);
		when(session.createQuery("From Turma turma Where turma.status = true")).thenReturn(query);
		when(criteria.list()).thenReturn(turmas);
		
	}
		
	@Test
	public void listaTudo(){
		when(session.createQuery("From Turma turma Where turma.status = true")).thenReturn(query);
		when(query.list()).thenReturn(turmas);
		List<Turma> t = turmaDao.listaTudo();
		Assert.assertEquals(turmas, t);
		verify(query).list();
	}
	@Test
	public void salvaTurma(){
		turmaDao.salvaTurma(turmaMatriculada);
		verify(session).beginTransaction();
		verify(session).save(turmaMatriculada);
		verify(tx).commit();
	}
	
	@Test
	public void removeTurma(){
		turmaDao.remove(turmaMatriculada);
		verify(session).beginTransaction();
		verify(session).update(turmaMatriculada);
		verify(tx).commit();
	}
	
	@Test
	public void atualizaTurma(){
		turmaDao.atualizaTurma(turmaMatriculada);
		verify(session).beginTransaction();
		verify(session).update(turmaMatriculada);
		verify(tx).commit();
	}
	
	@Test
	public void recarregaTurma(){
		turmaDao.recarrega(turmaMatriculada);
		verify(session).refresh(turmaMatriculada);
	}
}