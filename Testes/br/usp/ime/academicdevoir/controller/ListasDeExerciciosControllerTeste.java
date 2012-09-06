package br.usp.ime.academicdevoir.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import br.com.caelum.vraptor.util.test.JSR303MockValidator;
import br.com.caelum.vraptor.util.test.MockResult;
import br.com.caelum.vraptor.validator.ValidationException;
import br.usp.ime.academicdevoir.componete.Lista;
import br.usp.ime.academicdevoir.dao.DisciplinaDao;
import br.usp.ime.academicdevoir.dao.ListaDeExerciciosDao;
import br.usp.ime.academicdevoir.dao.ListaDeRespostasDao;
import br.usp.ime.academicdevoir.dao.ProfessorDao;
import br.usp.ime.academicdevoir.dao.QuestaoDao;
import br.usp.ime.academicdevoir.dao.TagsDaListaDao;
import br.usp.ime.academicdevoir.dao.TurmaDao;
import br.usp.ime.academicdevoir.entidade.Disciplina;
import br.usp.ime.academicdevoir.entidade.ListaDeExercicios;
import br.usp.ime.academicdevoir.entidade.Professor;
import br.usp.ime.academicdevoir.entidade.PropriedadesDaListaDeExercicios;
import br.usp.ime.academicdevoir.entidade.QuestaoDaLista;
import br.usp.ime.academicdevoir.entidade.QuestaoDeMultiplaEscolha;
import br.usp.ime.academicdevoir.entidade.TagsDaLista;
import br.usp.ime.academicdevoir.entidade.Turma;
import br.usp.ime.academicdevoir.infra.Privilegio;
import br.usp.ime.academicdevoir.infra.UsuarioSession;

public class ListasDeExerciciosControllerTeste {

	// @SuppressWarnings("unused")
	private ListasDeExerciciosController listasDeExerciciosController;

	private MockResult result;

	private ListaDeExerciciosDao dao;
	private ListaDeRespostasDao respostasDao;

	private QuestaoDao questaoDao;

	private TurmaDao turmaDao;
	private JSR303MockValidator validator;

	private ListaDeExercicios listaDeExercicios;

	private PropriedadesDaListaDeExercicios propriedadesDaListaDeExercicios;

	private List<Integer> prazoDeEntrega;

	private Calendar prazoProvisorio = Calendar.getInstance();

	private QuestaoDeMultiplaEscolha questao;

	private Turma turma;

	private ProfessorDao professorDao;

	private UsuarioSession usuarioSession;
	private ArrayList<Turma> turmas;
	private DisciplinaDao disciplinaDao;
	private TagsDaListaDao tagsDaListaDao;
	private TagsDaLista tagsDaLista;

	private Lista lista;

	@Before
	public void SetUp() {
		Professor professor = new Professor();
		professor.setId(0L);
		professor.setPrivilegio(Privilegio.ADMINISTRADOR);

		usuarioSession = new UsuarioSession();
		usuarioSession.setUsuario(professor);

		result = spy(new MockResult());
		dao = mock(ListaDeExerciciosDao.class);
		respostasDao = mock(ListaDeRespostasDao.class);
		questaoDao = mock(QuestaoDao.class);
		professorDao = mock(ProfessorDao.class);
		turmaDao = mock(TurmaDao.class);
		validator = spy(new JSR303MockValidator());
		lista = mock(Lista.class);
		disciplinaDao = mock(DisciplinaDao.class);
		tagsDaListaDao = mock(TagsDaListaDao.class);
		tagsDaLista = mock(TagsDaLista.class);

		listasDeExerciciosController = new ListasDeExerciciosController(result,
				dao, respostasDao, questaoDao, professorDao, turmaDao,
				validator, usuarioSession, lista, disciplinaDao, tagsDaListaDao);

		listaDeExercicios = new ListaDeExercicios();
		listaDeExercicios.setId(0L);

		propriedadesDaListaDeExercicios = new PropriedadesDaListaDeExercicios();
		propriedadesDaListaDeExercicios.setNome("nome");

		Calendar prazoProvisorio = Calendar.getInstance();
		prazoProvisorio.setTimeInMillis(System.currentTimeMillis());

		prazoDeEntrega = new ArrayList<Integer>();
		prazoDeEntrega.add(prazoProvisorio.get(Calendar.DAY_OF_MONTH));
		prazoDeEntrega.add(prazoProvisorio.get(Calendar.MONTH) + 1);
		prazoDeEntrega.add(prazoProvisorio.get(Calendar.YEAR));
		prazoDeEntrega.add(prazoProvisorio.get(Calendar.HOUR_OF_DAY));

		questao = new QuestaoDeMultiplaEscolha();
		questao.setId(0L);

		turma = new Turma();
		turma.setId(0L);

		turmas = new ArrayList<Turma>();
		turmas.add(turma);
		professor.setTurmas(turmas);

		when(dao.carrega(listaDeExercicios.getId())).thenReturn(
				listaDeExercicios);
		when(dao.listaTudo()).thenReturn(new ArrayList<ListaDeExercicios>());

		when(questaoDao.carrega(questao.getId())).thenReturn(questao);

		when(professorDao.carrega(professor.getId())).thenReturn(professor);
		when(turmaDao.carrega(turma.getId())).thenReturn(turma);
	}

	private void prazoFuturo(List<Integer> prazoDeEntrega) {
		prazoDeEntrega.add(prazoProvisorio.get(Calendar.MINUTE) + 1
				+ new Random().nextInt(100000));
	}

	private Date prazoFuturo() {
		Date prazoDeEntrega = new Date(System.currentTimeMillis() + 1L
				+ (long) (new Random().nextInt(1000000)));
		return prazoDeEntrega;
	}

	private void prazoPassado(List<Integer> prazoDeEntrega) {
		prazoDeEntrega.add(prazoProvisorio.get(Calendar.MINUTE)
				- new Random().nextInt(100000));
	}

	@Test
	public void deveCadastrarUmaListaDeExercicios() {
		prazoFuturo(prazoDeEntrega);

		List<Long> turmas = new ArrayList<Long>();
		List<TagsDaLista> tagsDaListas = new ArrayList<TagsDaLista>();
		propriedadesDaListaDeExercicios.setNome("Nome da lista");
		tagsDaListas.add(tagsDaLista);
		turmas.add(turma.getId());
		listasDeExerciciosController.cadastra(propriedadesDaListaDeExercicios, turmas, tagsDaListas, new Date());

		verify(validator).validate(propriedadesDaListaDeExercicios);
		verify(validator).onErrorForwardTo(listasDeExerciciosController);
		verify(dao).salva(any(ListaDeExercicios.class));
		verify(result).redirectTo(listasDeExerciciosController);
	}

	@Test(expected = ValidationException.class)
	public void naoDeveCadastrarListaDeExerciciosSemNome() {
		List<Long> turmas = new ArrayList<Long>();
		List<TagsDaLista> tagsDaListas = new ArrayList<TagsDaLista>();
		tagsDaListas.add(tagsDaLista);
				turmas.add(turma.getId());
		prazoFuturo(prazoDeEntrega);
		propriedadesDaListaDeExercicios.setNome("");
		listasDeExerciciosController.cadastra(propriedadesDaListaDeExercicios, turmas, tagsDaListas, new Date());

		verify(validator).validate(propriedadesDaListaDeExercicios);
		verify(validator).onErrorRedirectTo(listasDeExerciciosController);
		verify(dao).salva(any(ListaDeExercicios.class));
		verify(result).redirectTo(listasDeExerciciosController);
	}

	@Test(expected = ValidationException.class)
	public void cadastraFalhaDeTurmaId() {
		propriedadesDaListaDeExercicios.setNome("Teste");
		propriedadesDaListaDeExercicios.setEnunciado("Lista que deve falhar");
		propriedadesDaListaDeExercicios.setPeso(1);
		

		listasDeExerciciosController.cadastra(propriedadesDaListaDeExercicios, null, null, new Date());

	}

	@Test
	public void testeVerListaIncluiListaEDataEmResult() {
		Date prazoDeEntrega = prazoFuturo();
		propriedadesDaListaDeExercicios.setPrazoDeEntrega(prazoDeEntrega);
		listaDeExercicios.setPropriedades(propriedadesDaListaDeExercicios);
		listaDeExercicios.setQuestoes(new ArrayList<QuestaoDaLista>());
		listasDeExerciciosController.verLista(listaDeExercicios.getId());

		ListaDeExercicios lista = result.included("listaDeExercicios");
		String prazo = result.included("prazo");
		List<Turma> turmas = result.included("turmasDoProfessor");

		assertNotNull(lista);
		assertNotNull(prazo);
		assertNotNull(turmas);
	}

	@Test
	public void testeAlteracao() {
		Date prazoDeEntrega = prazoFuturo();
		propriedadesDaListaDeExercicios.setPrazoDeEntrega(prazoDeEntrega);
		listaDeExercicios.setPropriedades(propriedadesDaListaDeExercicios);
		listasDeExerciciosController.alteracao(listaDeExercicios.getId());

		ListaDeExercicios lista = result.included("listaDeExercicios");
		List<Integer> prazo = result.included("prazo");

		assertNotNull(lista);
		assertNotNull(prazo);
	}

	@Test
	public void testeAltera() {
		prazoFuturo(prazoDeEntrega);
		listasDeExerciciosController.altera(listaDeExercicios,
				propriedadesDaListaDeExercicios, new Date());

		verify(validator).validate(listaDeExercicios);
		verify(validator).onErrorUsePageOf(ListasDeExerciciosController.class);
		verify(dao).atualiza(listaDeExercicios);
		verify(result).redirectTo(listasDeExerciciosController);
	}

	@Test(expected = ValidationException.class)
	public void testeNaoDeveAlterarQuestaoComPrazoPassado() {
		prazoPassado(prazoDeEntrega);
		listasDeExerciciosController.altera(listaDeExercicios,
				propriedadesDaListaDeExercicios, new Date());

		verify(validator).validate(listaDeExercicios);
		verify(validator).onErrorUsePageOf(ListasDeExerciciosController.class);
		verify(dao).atualiza(listaDeExercicios);
		verify(result).redirectTo(listasDeExerciciosController);
	}

	@Test
	public void testeRemove() {
		listasDeExerciciosController.remove(listaDeExercicios.getId());
		verify(dao).remove(listaDeExercicios);
		verify(result).redirectTo(listasDeExerciciosController);
	}

	@Test
	public void testeIncluiQuestao() {
		listaDeExercicios.setQuestoes(new ArrayList<QuestaoDaLista>());
		listasDeExerciciosController.incluiQuestao(listaDeExercicios,
				questao.getId(), 1);

		assertEquals(questao, listaDeExercicios.getQuestoesDaLista().get(0)
				.getQuestao());
		verify(dao).atualiza(listaDeExercicios);
		verify(result).redirectTo(listasDeExerciciosController);
	}

	// @Test
	// public void testeTrocaQuestao() {
	// List<QuestaoDaLista> questoesDaLista = new ArrayList<QuestaoDaLista>();
	// questoesDaLista.add(new QuestaoDaLista());
	// listaDeExercicios.setQuestoes(questoesDaLista);
	// listasDeExerciciosController.trocaQuestao(listaDeExercicios.getId(), 0,
	// questao.getId(), 0);
	//
	// assertEquals(questao, listaDeExercicios.getQuestoes().get(0)
	// .getQuestao());
	// verify(dao).atualiza(listaDeExercicios);
	// verify(result).redirectTo(listasDeExerciciosController);
	// }

	@Test(expected = IndexOutOfBoundsException.class)
	public void testeRemoveQuestao() {
		QuestaoDaLista questao0 = new QuestaoDaLista();
		questao0.setOrdem(1);
		QuestaoDaLista questao1 = new QuestaoDaLista();
		questao1.setOrdem(2);

		List<QuestaoDaLista> questoesDaLista = new ArrayList<QuestaoDaLista>();
		questoesDaLista.add(questao0);
		questoesDaLista.add(questao1);

		listaDeExercicios.setQuestoes(questoesDaLista);
		int numeroInicialDeQuestoes = listaDeExercicios.getQuestoesDaLista()
				.size();
		listasDeExerciciosController
				.removeQuestao(listaDeExercicios.getId(), 0);

		assertEquals(questao1, listaDeExercicios.getQuestoesDaLista().get(0));
		listaDeExercicios.getQuestoesDaLista().get(numeroInicialDeQuestoes);

		verify(dao).atualiza(listaDeExercicios);
		verify(result).redirectTo(listasDeExerciciosController);
	}

	@Test
	public void testeCadastro() {
		listasDeExerciciosController.cadastro();
		List<Disciplina> disciplinas = result.included("disciplinas");
		assertNotNull(disciplinas);
	}

	@Test
	public void testeLista() {
		listasDeExerciciosController.lista();
		List<ListaDeExercicios> listaDeListas = result
				.included("listaDeListas");
		assertNotNull(listaDeListas);
	}

	@Test
	public void testeListasTurma() {
		listasDeExerciciosController.listasTurma(this.turma.getId());
		List<ListaDeExercicios> listaDeListas = result
				.included("listaDeListas");
		assertNull(listaDeListas);
	}

	// TODO Teste para Autocorreção
}
