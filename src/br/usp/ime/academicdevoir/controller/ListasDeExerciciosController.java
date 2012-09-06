package br.usp.ime.academicdevoir.controller;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.caelum.vraptor.Delete;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Put;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.validator.Validations;
import br.usp.ime.academicdevoir.componete.Lista;
import br.usp.ime.academicdevoir.dao.DisciplinaDao;
import br.usp.ime.academicdevoir.dao.ListaDeExerciciosDao;
import br.usp.ime.academicdevoir.dao.ListaDeRespostasDao;
import br.usp.ime.academicdevoir.dao.ProfessorDao;
import br.usp.ime.academicdevoir.dao.QuestaoDao;
import br.usp.ime.academicdevoir.dao.TagsDaListaDao;
import br.usp.ime.academicdevoir.dao.TurmaDao;
import br.usp.ime.academicdevoir.entidade.Aluno;
import br.usp.ime.academicdevoir.entidade.AutoCorrecao;
import br.usp.ime.academicdevoir.entidade.EstadoDaListaDeRespostas;
import br.usp.ime.academicdevoir.entidade.ListaDeExercicios;
import br.usp.ime.academicdevoir.entidade.ListaDeRespostas;
import br.usp.ime.academicdevoir.entidade.ListaGerada;
import br.usp.ime.academicdevoir.entidade.Professor;
import br.usp.ime.academicdevoir.entidade.PropriedadesDaListaDeExercicios;
import br.usp.ime.academicdevoir.entidade.PropriedadesDaListaDeRespostas;
import br.usp.ime.academicdevoir.entidade.Questao;
import br.usp.ime.academicdevoir.entidade.QuestaoDaLista;
import br.usp.ime.academicdevoir.entidade.Resposta;
import br.usp.ime.academicdevoir.entidade.TagsDaLista;
import br.usp.ime.academicdevoir.entidade.Turma;
import br.usp.ime.academicdevoir.entidade.Usuario;
import br.usp.ime.academicdevoir.infra.Constantes;
import br.usp.ime.academicdevoir.infra.Permission;
import br.usp.ime.academicdevoir.infra.Privilegio;
import br.usp.ime.academicdevoir.infra.TipoDeQuestao;
import br.usp.ime.academicdevoir.infra.UsuarioSession;
import br.usp.ime.academicdevoir.infra.VerificadorDePrazos;

import com.google.common.collect.Lists;

@Resource
/**
 * Controlador de listas de exercicios.
 */
public class ListasDeExerciciosController {

	private final Result result;

	private final ListaDeExerciciosDao dao;

	private final ListaDeRespostasDao listaDeRespostasDao;

	private final QuestaoDao questaoDao;

	private final ProfessorDao professorDao;

	private final TurmaDao turmaDao;

	private final Validator validator;

	private final UsuarioSession usuarioSession;
	
	private final Lista lista;

	private final DisciplinaDao disciplinaDao;
	
	private final TagsDaListaDao tagsDaListaDao;
	
	public ListasDeExerciciosController(Result result,
			ListaDeExerciciosDao dao, ListaDeRespostasDao listaDeRespostasDao,
			QuestaoDao questaoDao, ProfessorDao professorDao,
			TurmaDao turmaDao, Validator validator,
			UsuarioSession usuarioSession, Lista lista, DisciplinaDao disciplinaDao,
			TagsDaListaDao tagsDaListaDao) {

		this.result = result;
		this.dao = dao;
		this.listaDeRespostasDao = listaDeRespostasDao;
		this.questaoDao = questaoDao;
		this.professorDao = professorDao;
		this.turmaDao = turmaDao;
		this.validator = validator;
		this.usuarioSession = usuarioSession;
		this.lista = lista;
		this.disciplinaDao = disciplinaDao;
		this.tagsDaListaDao = tagsDaListaDao;
	}

	@Post
	@Path("/listasDeExercicios")
	@Permission({ Privilegio.ADMINISTRADOR, Privilegio.PROFESSOR })
	/**
	 * Verifica se a lista de exercícios fornecida é válida e adiciona no banco de dados.
	 * 
	 * @param listaDeExercicios
	 * @param prazoDeEntrega
	 * @param idDasTurmas
	 */
	public void cadastra(final PropriedadesDaListaDeExercicios propriedades, final List<Long> idsDaTurma, List<TagsDaLista> tags,
			Date data) {

		List<Turma> turmas = new ArrayList<Turma>();

		if (propriedades.getGeracaoAutomatica() == null) {
			propriedades.setGeracaoAutomatica(false);
		}
		propriedades.setPrazoDeEntrega(data);
			
		validator.checking(new Validations() {
			{
				that(!propriedades.getNome().isEmpty(), "propriedade.nome",
						"propriedade.nome.notEmpty");
				that(idsDaTurma, is(notNullValue()), "turmas.id",
						"turmas.id.notEmpty");
			}
		});

		validator.validate(propriedades);
		validator.onErrorForwardTo(this).cadastro();

		ListaDeExercicios listaDeExercicios = new ListaDeExercicios();
		for (Long idDaTurma : idsDaTurma) {
			turmas.add(turmaDao.carrega(idDaTurma));
		}
		listaDeExercicios.setTurmas(turmas);
		List<TagsDaLista> tagsDaLista = Lists.newArrayList();
		for (TagsDaLista tag : tags) {
			if(tag.getTag() != null && tag.getTag().getId() != null){
				tagsDaListaDao.salvar(tag);
				tagsDaLista.add(tag);
			}
		}
		listaDeExercicios.setTags(tagsDaLista);
		listaDeExercicios.setPropriedades(propriedades);

		dao.salva(listaDeExercicios);
		result.redirectTo(this).verLista(listaDeExercicios.getId());
	}

	@Get
	@Path("/listasDeExercicios/{id}")
	/** 
	 * Devolve uma lista de exercícios com o id fornecido.
	 * 
	 * @param id
	 * */
	public void verLista(Long id) {
		Usuario u = usuarioSession.getUsuario();
		if (!(u.getPrivilegio() == Privilegio.ADMINISTRADOR || u
				.getPrivilegio() == Privilegio.PROFESSOR)) {
			result.redirectTo(LoginController.class).acessoNegado();
			return;
		}

		ListaDeExercicios listaDeExercicios = dao.carrega(id);
		Professor professor = professorDao.carrega(usuarioSession.getUsuario()
				.getId());

		result.include("listaDeExercicios", listaDeExercicios);
		result.include("prazo", listaDeExercicios.getPropriedades()
				.getPrazoDeEntregaFormatado());
		result.include("turmasDoProfessor", professor.getTurmas());
		result.include("numeroDeQuestoes", listaDeExercicios.getQuestoesDaLista()
				.size());
	}

	@Get
	@Path("/listasDeExercicios/resolver/{id}/turma/{turma.id}")
	@Permission({ Privilegio.ALUNO, Privilegio.MONITOR })
	/** 
	 * Devolve uma lista de exercícios com o id fornecido.
	 * 
	 * @param id
	 * */
	public void resolverLista(Long id, Turma turma) {
		ListaDeExercicios listaDeExercicios = dao.carrega(id);

		Aluno aluno = (Aluno) usuarioSession.getUsuario();
		
		ListaGerada listaGerada = null;
		if(listaDeExercicios.getPropriedades().getGeracaoAutomatica()){
			Aluno novoAluno = new Aluno();
			novoAluno.setId(aluno.getId());
			listaGerada = lista.gerar(listaDeExercicios, novoAluno);
		}
		
		ListaDeRespostas listaDeRespostas = listaDeRespostasDao
				.getRespostasDoAluno(id, aluno);

		if (listaDeRespostas == null) {
			PropriedadesDaListaDeRespostas propriedades = new PropriedadesDaListaDeRespostas();

			propriedades.setEstado(EstadoDaListaDeRespostas.SALVA);
			propriedades.setNumeroDeEnvios(0);

			listaDeRespostas = new ListaDeRespostas();
			listaDeRespostas.setPropriedades(propriedades);
			listaDeRespostas.setListaDeExercicios(listaDeExercicios);
			listaDeRespostas.setAluno(aluno);
			listaDeRespostas.setPropriedades(propriedades);
			if (!VerificadorDePrazos.estaNoPrazo(listaDeExercicios
					.getPropriedades().getPrazoDeEntrega())) {
				listaDeRespostas.getPropriedades().setEstado(
						EstadoDaListaDeRespostas.FINALIZADA);
				listaDeRespostas.setRespostas(new ArrayList<Resposta>());
				listaDeRespostasDao.salva(listaDeRespostas);
				result.redirectTo(this).autoCorrecaoRespostas(
						listaDeRespostas.getId(), turma);
				return;
			}
		}

		else if (listaDeRespostas.getRespostas() != null
				&& listaDeRespostas.getRespostas().size() > 0) {
			result.redirectTo(this).alterarRespostas(listaDeRespostas, turma);
			return;
		}

		listaDeRespostas.setRespostas(new ArrayList<Resposta>());
		listaDeRespostasDao.salva(listaDeRespostas);
		listaDeRespostas = listaDeRespostasDao
				.getRespostasDoAluno(id, aluno);

		if (listaDeExercicios.getPropriedades() != null)
			result.include("prazo", listaDeExercicios.getPropriedades()
					.getPrazoDeEntregaFormatado());
		if(listaDeExercicios.getPropriedades().getGeracaoAutomatica()) {
			result.include("questoes", listaGerada.getQuestoes());
			result.include("numeroDeQuestoes", listaGerada.getQuestoes().size());
		}
		else {
			result.include("questoes", listaDeExercicios.getQuestoes());
			result.include("numeroDeQuestoes", listaDeExercicios.getQuestoes().size());
		}
		result.include("listaDeRespostas", listaDeRespostas);
		result.include("turma", turma);
	}

	@Get
	@Path("/respostas/alterar/{listaDeRespostas.id}/turma/{turma.id}")
	@Permission({ Privilegio.ALUNO, Privilegio.MONITOR })
	public void alterarRespostas(ListaDeRespostas listaDeRespostas, Turma turma) {
		listaDeRespostas = listaDeRespostasDao
				.carrega(listaDeRespostas.getId());

		Aluno aluno = (Aluno) usuarioSession.getUsuario();
		List<Questao> questoes;
		ListaGerada listaGerada;
		
		ListaDeExercicios listaDeExercicios = listaDeRespostas
				.getListaDeExercicios();

		if (listaDeRespostas.getPropriedades().getEstado() == EstadoDaListaDeRespostas.SALVA
				&& !VerificadorDePrazos.estaNoPrazo(listaDeRespostas
						.getListaDeExercicios().getPropriedades()
						.getPrazoDeEntrega())) {
			listaDeRespostas.getPropriedades().setEstado(
					EstadoDaListaDeRespostas.FINALIZADA);
			listaDeRespostasDao.atualiza(listaDeRespostas);
			result.redirectTo(ListasDeExerciciosController.class)
					.autoCorrecaoRespostas(listaDeRespostas.getId(), turma);
			return;
		}

		if (listaDeRespostas.getPropriedades().getEstado() == EstadoDaListaDeRespostas.CORRIGIDA
				|| listaDeRespostas.getPropriedades().getEstado() == EstadoDaListaDeRespostas.FINALIZADA) {
			result.redirectTo(ListasDeExerciciosController.class).verCorrecao(
					listaDeRespostas);
			return;
		}

		List<String> renders = new ArrayList<String>();

		if(listaDeExercicios.getPropriedades().getGeracaoAutomatica()){
			listaGerada = lista.gerar(listaDeExercicios, aluno);
			questoes = listaGerada.getQuestoes();
		}
		else{
			questoes = listaDeExercicios.getQuestoes();
		}
		
		
		List<Resposta> respostas = listaDeRespostas.getRespostas();
		boolean achouResposta;

		for (Questao questao : questoes) {

			achouResposta = false;
			for (Resposta resposta : respostas) {
				if (resposta.getQuestao().getId() == questao.getId()) {
					renders.add(resposta.getQuestao().getRenderAlteracao(
							resposta));
					respostas.remove(resposta);
					achouResposta = true;
					break;
				}
			}
			if (achouResposta)
				continue;
			renders.add(questao.getRenderizacao());
		}

		result.include("renderizacao", renders);
		result.include("listaDeRespostas", listaDeRespostas);
		result.include("questoes", questoes);
		result.include("listaDeExercicios", listaDeExercicios);
		result.include("numeroDeQuestoes", questoes.size());
		result.include("VerificadorDePrazos", VerificadorDePrazos.class);
		result.include("turma", turma);

	}

	@Get
	@Path("/respostas/verCorrecao/{listaDeRespostas.id}")
	public void verCorrecao(ListaDeRespostas listaDeRespostas) {
		listaDeRespostas = listaDeRespostasDao
				.carrega(listaDeRespostas.getId());

		List<Questao> questoes;
		ListaGerada listaGerada;
		Aluno aluno = (Aluno) usuarioSession.getUsuario();

		ListaDeExercicios listaDeExercicios = listaDeRespostas
				.getListaDeExercicios();
		List<String> renders = new ArrayList<String>();

		List<QuestaoDaLista> questoesDaLista = listaDeExercicios.getQuestoesDaLista();
		List<Resposta> respostas = listaDeRespostas.getRespostas();
		boolean achouResposta;

		if(listaDeExercicios.getPropriedades().getGeracaoAutomatica()){
			listaGerada = lista.gerar(listaDeExercicios, aluno);
			questoes = listaGerada.getQuestoes();
		}
		else{
			questoes = listaDeExercicios.getQuestoes();
		}
		for (Questao questao: questoes) {

			achouResposta = false;
			for (Resposta resposta : respostas) {

				if (resposta.getQuestao().getId() == questao.getId()) {

					renders.add(renderCorrecao(resposta));

					respostas.remove(resposta);
					achouResposta = true;
					break;
				}
			}

			if (achouResposta)
				continue;			
			renders.add(questao.getRenderCorrecao(
					new Resposta()));
		}
		
		


		result.include("renderizacao", renders);
		result.include("questoes", questoes);
		result.include("listaDeRespostas", listaDeRespostas);
		result.include("listaDeExercicios", listaDeExercicios);
		result.include("numeroDeQuestoes", questoesDaLista.size());
	}

	public String renderCorrecao(Resposta resposta) {

		if (resposta == null)
			resposta = new Resposta();

		String htmlResult = "";
		StringBuffer buffer = new StringBuffer();

		buffer.append("<p>");
		if (resposta.getValor() != null)
			buffer.append("Resposta: " + resposta.getValor());
		buffer.append("</p>");
		buffer.append("<p> Comentários:<br/> ");
		
		if (resposta.getQuestao().getTipo() == TipoDeQuestao.CODIGO) {
			buffer.append(resposta.getComentario());
		}
		else {
			if (resposta.getQuestao().getComentario() != null) {
				buffer.append(resposta.getQuestao().getComentario());
			}
		}
		buffer.append("</p>");
		buffer.append("<p> Nota: ");
		if (resposta.getNota() != null)
			buffer.append(resposta.getNota());
		else
			buffer.append(0.0);
		buffer.append("</p>");

		htmlResult = buffer.toString();

		return htmlResult;
	}

	@Get
	@Path("/listasDeExercicios/altera/{id}")
	@Permission({ Privilegio.ADMINISTRADOR, Privilegio.PROFESSOR })
	/** 
	 * Retorna uma lista de exercícios com o id fornecido.
	 * 
	 * @param id
	 * @return ListaDeExercicios 
	 * */
	public void alteracao(Long id) {

		ListaDeExercicios listaDeExercicios = dao.carrega(id);

		result.include("listaDeExercicios", listaDeExercicios);
		result.include("prazo", listaDeExercicios.getPropriedades()
				.getPrazoPrazoDeEntregaEmLista());
	}

	@Put
	@Path("/listasDeExercicios/{listaDeExercicios.id}")
	@Permission({ Privilegio.ADMINISTRADOR, Privilegio.PROFESSOR })
	/**
	 * Verifica se a lista de exercícios fornecida é válida e atualiza no banco de dados.
	 * 
	 * @param listaDeExercicios
	 * @param prazoDeEntrega
	 */
	public void altera(ListaDeExercicios listaDeExercicios,
			PropriedadesDaListaDeExercicios propriedades, Date data) {

		ListaDeExercicios listaDoBD = dao.carrega(listaDeExercicios.getId());
		
		propriedades.setPrazoDeEntrega(data);
		
		listaDoBD.setPropriedades(propriedades);

		validator.validate(listaDeExercicios);
		validator.onErrorUsePageOf(ListasDeExerciciosController.class)
				.alteracao(listaDeExercicios.getId());

		dao.atualiza(listaDoBD);
		result.redirectTo(this).verLista(listaDoBD.getId());
	}

	@Delete
	@Path("/listasDeExercicios/{id}")
	@Permission({ Privilegio.ADMINISTRADOR, Privilegio.PROFESSOR })
	/**
	 * Remove uma lista de exercícios do banco de dados com o id fornecido.
	 * 
	 * @param id
	 */
	public void remove(Long id) {
		ListaDeExercicios lista = dao.carrega(id);
		dao.remove(lista);
		result.redirectTo(this).lista();
	}

	@Put
	@Path("/listasDeExercicios/{listaDeExercicios.id}/questoes/inclui")
	@Permission({ Privilegio.ADMINISTRADOR, Privilegio.PROFESSOR })
	/**
	 * Inclui a questão com o id fornecido na lista de exercícios.
	 * 
	 * @param listaDeExercicios
	 * @param idDaQuestao
	 */
	public void incluiQuestao(ListaDeExercicios listaDeExercicios,
			Long idDaQuestao, Integer pesoDaQuestao) {

		QuestaoDaLista novaQuestao = new QuestaoDaLista();
		novaQuestao.setPeso(pesoDaQuestao);
		Questao questao = (Questao) questaoDao.carrega(idDaQuestao);
		novaQuestao.setQuestao(questao);

		dao.recarrega(listaDeExercicios);
		List<QuestaoDaLista> questoes = listaDeExercicios.getQuestoesDaLista();

		novaQuestao.setOrdem(questoes.size());
		questoes.add(novaQuestao);
		listaDeExercicios.setQuestoes(questoes);

		dao.atualiza(listaDeExercicios);
		result.redirectTo(this).verLista(listaDeExercicios.getId());
	}

	// @Put
	// @Path("/listasDeExercicios/{id}/questoes/{indice}")
	// @Permission({ Privilegio.ADMINISTRADOR, Privilegio.PROFESSOR })
	// /**
	// * Altera a questão com o indice fornecido (na lista de exercícios com o
	// id fornecido)
	// * para a questão com id fornecido.
	// *
	// * @param id
	// * @param indice
	// * @param idDaNovaQuestao
	// * @param ordemDaQuestao
	// */
	// public void trocaQuestao(Long id, Integer indice, Long idDaNovaQuestao,
	// Integer ordemDaQuestao) {
	//
	// ListaDeExercicios listaDeExercicios = dao.carrega(id);
	// List<QuestaoDaLista> questoesDaLista = listaDeExercicios.getQuestoes();
	// QuestaoDaLista questaoDaLista = listaDeExercicios.getQuestoes().get(
	// indice.intValue());
	// Questao questao = (Questao) questaoDao.carrega(idDaNovaQuestao);
	//
	// questaoDaLista.setQuestao(questao);
	// questaoDaLista.setOrdem(ordemDaQuestao);
	// questoesDaLista.set(indice.intValue(), questaoDaLista);
	// listaDeExercicios.setQuestoes(questoesDaLista);
	//
	// dao.atualiza(listaDeExercicios);
	// result.redirectTo(this).verLista(listaDeExercicios.getId());
	// }

	@Delete
	@Path("/listasDeExercicios/{id}/questoes/{indice}")
	@Permission({ Privilegio.ADMINISTRADOR, Privilegio.PROFESSOR })
	/**
	 * Remove a questão com o indice fornecido na lista de exercícios com o id fornecido.
	 * 
	 * @param id
	 * @param indice
	 */
	public void removeQuestao(Long id, Integer indice) {
		ListaDeExercicios listaDeExercicios = dao.carrega(id);
		List<QuestaoDaLista> questoes = listaDeExercicios.getQuestoesDaLista();

		questoes.remove(indice.intValue());
		listaDeExercicios.setQuestoes(questoes);

		dao.atualiza(listaDeExercicios);
		result.redirectTo(this).verLista(listaDeExercicios.getId());
	}

	@Get
	@Path("/listasDeExercicios/cadastro")
	@Permission({ Privilegio.ADMINISTRADOR, Privilegio.PROFESSOR })
	/**
	 * Permite acesso à página com formulário para cadastro de uma nova lista de exercícios.
	 */
	public void cadastro() {
//		Professor professor = professorDao.carrega(usuarioSession.getUsuario()
//				.getId());
//		result.include("turmasDoProfessor", professor.getTurmas());
		result.include("disciplinas", disciplinaDao.listaTudo());
	}

	@Get
	@Path("/listasDeExercicios")
	@Permission({ Privilegio.ADMINISTRADOR, Privilegio.PROFESSOR })
	/**
	 * Devolve uma lista com todas as listas de exercícios cadastradas no banco de dados.
	 */
	public void lista() {
		result.include("listaDeListas", dao.listaTudo());
	}

	@Get
	@Path("/listasDeExercicios/listasTurma/{idTurma}")
	/**
	 * Devolve uma lista com todas as listas de exercícios de uma determinada turma.
	 */
	public void listasTurma(Long idTurma) {
		Turma turma = turmaDao.carrega(idTurma);
		result.include("listaDeListas", turma.getListaDeExercicios());
	}

	@Get
	@Path("/respostas/autocorrecao/{id}/turma/{turma.id}")
	/** 
	 * Corrige todas as respostas da lista de exercícios com o id fornecido.
	 * 
	 * @param id
	 * */
	public void autoCorrecaoRespostas(Long id, Turma turma) {
		// Carrega a lista de exercícios com esse id
		ListaDeRespostas listaDeRespostas = listaDeRespostasDao.carrega(id);

		AutoCorrecao autoCorrecao = listaDeRespostas.getListaDeExercicios()
				.getPropriedades().getAutoCorrecao();

		if (listaDeRespostas.getPropriedades().getEstado() == EstadoDaListaDeRespostas.SALVA
				&& !VerificadorDePrazos.estaNoPrazo(listaDeRespostas
						.getListaDeExercicios().getPropriedades()
						.getPrazoDeEntrega())) {
			listaDeRespostas.getPropriedades().setEstado(
					EstadoDaListaDeRespostas.FINALIZADA);
			listaDeRespostasDao.atualiza(listaDeRespostas);
		}

		// Não corrige se autocorreção estiver desativada para esse lista
		if (autoCorrecao == AutoCorrecao.ATIVADA) {


			
			if(listaDeRespostas.getListaDeExercicios().getPropriedades().getGeracaoAutomatica()){
				Aluno aluno = (Aluno) usuarioSession.getUsuario();
				ListaGerada listaGerada = lista.gerar(listaDeRespostas.getListaDeExercicios(), aluno);
				listaDeRespostas.autocorrecao(listaGerada);
			} else {
				listaDeRespostas.autocorrecao();
			}
			
			
			listaDeRespostasDao.atualiza(listaDeRespostas);
			// Redireciona para o menu de listas
			result.redirectTo(this).verCorrecao(listaDeRespostas);
			return;
		} 
		result.redirectTo(TurmasController.class).home(turma.getId());
	}

	@Get
	@Path("/listasDeExercicios/autocorrecao/{id}")
	/** 
	 * Corrige todas as respostas da lista de exercícios com o id fornecido.
	 * 
	 * @param id
	 * */
	public void autoCorrecaoLista(Long id) {
		// Carrega a lista de exercícios com esse id
		ListaDeExercicios listaDeExercicios = dao.carrega(id);

		// Carrega as propriedades da lista de exercícios
		PropriedadesDaListaDeExercicios propriedades = listaDeExercicios
				.getPropriedades();

		// Não corrige se autocorreção estiver desativada para esse lista
		if (propriedades.getAutoCorrecao() == AutoCorrecao.DESATIVADA) {
			result.redirectTo(this).lista();
			return;
		}

		// Pegando todas as listas de respostas. Cada elemento corresponde a
		// Lista de um aluno
		List<ListaDeRespostas> listasDeRespostas = listaDeRespostasDao
				.listaRespostasDaLista(listaDeExercicios);

		// Para cada Lista de Resposta (Aluno)
		for (ListaDeRespostas listaDeRespostas : listasDeRespostas) {
			listaDeRespostas.autocorrecao();
			listaDeRespostas.getPropriedades().setEstado(
					EstadoDaListaDeRespostas.CORRIGIDA);

			listaDeRespostasDao.atualiza(listaDeRespostas);
		}

		// Redireciona para o menu de listas
		result.redirectTo(this).lista();

	}

	@Get
	@Path("/listasDeExercicios/{id}/inclusaoQuestoes")
	@Permission({ Privilegio.ADMINISTRADOR, Privilegio.PROFESSOR })
	/** 
	 * Devolve a lista de questões que poderão ser inseridas na lista com o id fornecido.
	 * 
	 * @param id
	 * */
	public void inclusaoQuestoes(Long id, Integer proxPagina, String filtro) {
		List<Questao> listaDeQuestoesPaginadas;
		Integer primeiroReg, ultimaPagina;
		Usuario u = usuarioSession.getUsuario();
		if (!(u.getPrivilegio() == Privilegio.ADMINISTRADOR || u
				.getPrivilegio() == Privilegio.PROFESSOR)) {
			result.redirectTo(LoginController.class).acessoNegado();
			return;
		}

		primeiroReg = (proxPagina - 1) * Constantes.NUM_REGISTROS_PAGINA;

		listaDeQuestoesPaginadas = questaoDao.listaPaginada(primeiroReg, 
				Constantes.NUM_REGISTROS_PAGINA, filtro, id);
		ultimaPagina = questaoDao.tamanhoDaLista(filtro, id)
				/ Constantes.NUM_REGISTROS_PAGINA;
		if (listaDeQuestoesPaginadas.size() % Constantes.NUM_REGISTROS_PAGINA != 0)
			ultimaPagina++;

		result.include("idDaListaDeExercicios", id);
		result.include("listaDeQuestoes", listaDeQuestoesPaginadas);
		result.include("pagina", proxPagina);
		result.include("ultimaPagina", ultimaPagina);
		result.include("filtroAtual", filtro);
	} 	

	@Get
	@Path("/listasDeExercicios/trocaOrdem/{id}")
	public void trocaOrdem(Long id, List<Integer> novaOrdem) {
		ListaDeExercicios lista = dao.carrega(id);
		List<QuestaoDaLista> questoes = lista.getQuestoesDaLista();
		Integer ordem;
		QuestaoDaLista questao;

		for (int i = 0; i < questoes.size(); i++) {
			ordem = novaOrdem.get(i);
			questao = questoes.get(i);
			questao.setOrdem(ordem);
			questoes.set(i, questao);
		}

		lista.setQuestoes(questoes);
		dao.atualiza(lista);
		result.redirectTo(ListasDeExerciciosController.class).verLista(id);
	}
	

}
