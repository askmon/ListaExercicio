package br.usp.ime.academicdevoir.controller;

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
import br.usp.ime.academicdevoir.dao.DisciplinaDao;
import br.usp.ime.academicdevoir.dao.QuestaoDeMultiplaEscolhaDao;
import br.usp.ime.academicdevoir.dao.TagDao;
import br.usp.ime.academicdevoir.entidade.Disciplina;
import br.usp.ime.academicdevoir.entidade.QuestaoDeMultiplaEscolha;
import br.usp.ime.academicdevoir.infra.Permission;
import br.usp.ime.academicdevoir.infra.Privilegio;
import br.usp.ime.academicdevoir.infra.TipoDeQuestao;
import br.usp.ime.academicdevoir.infra.UsuarioSession;

@Permission({ Privilegio.ADMINISTRADOR, Privilegio.PROFESSOR })
@Resource
/**
 * Controlador de questões de múltipla escolha.
 */
public class QuestoesDeMultiplaEscolhaController {

	/**
	 * @uml.property name="dao"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	private QuestaoDeMultiplaEscolhaDao dao;
	
	/**
	 * @uml.property name="result"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	private final Result result;
	/**
	 * @uml.property name="validator"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	private Validator validator;
	private TagDao tagDao;
	private DisciplinaDao disciplinaDao;

	/**
	 * @param result
	 *            para interação com o jsp da questão.
	 * @param validator
	 * @param usuarioSession
	 *            para controle de permissões
	 * @param disciplinaDao 
	 * @param turmaDao
	 *            para interação com o banco de dados
	 */
	public QuestoesDeMultiplaEscolhaController(QuestaoDeMultiplaEscolhaDao dao,
			TagDao tagDao, Result result, Validator validator,
			UsuarioSession usuarioSession, DisciplinaDao disciplinaDao) {
		this.dao = dao;
		this.tagDao = tagDao;
		this.disciplinaDao = disciplinaDao;
		this.result = result;
		this.validator = validator;
	}

	@Post
	@Path("/questoes/mult")	
	/**
	 * Verifica se a questão de múltipla escolha fornecida é válida e adiciona 
	 * no banco de dados.
	 * @param questao
	 */
	public void cadastra(final QuestaoDeMultiplaEscolha questao,
			List<Integer> resposta, String tags, int numeroDeAlternativas) {

		validator.checking(new Validations() {{
			that(questao.getDisciplina().getId() != null, "questao.id", "questao.id.notNull");
		}});
		
		validator.validate(questao);
		validator.onErrorRedirectTo(QuestoesController.class).cadastro();

		for (int i=0; i<numeroDeAlternativas; i++) {
			questao.getAlternativas().get(i).setQuestao(questao);			
		}
			
		for (int i=numeroDeAlternativas; i<10; i++) {
			questao.getAlternativas().remove(numeroDeAlternativas);
		}
		

		questao.setTags(tags, tagDao);
		
		Disciplina disciplina = disciplinaDao.carrega(questao.getDisciplina().getId());
		disciplina.setTags(tags, tagDao);
		questao.setResposta(resposta);
		
		disciplinaDao.atualiza(disciplina);
		dao.salva(questao);
		result.redirectTo(this).lista();
	}

	@Get
	@Path("/questoes/mult/{id}")
	/** 
	 * Devolve uma questão de múltipla escolha com o id fornecido.
	 * 
	 * @param id
	 */
	public void alteracao(Long id) {
		QuestaoDeMultiplaEscolha questao = dao.carrega(id);		
		result.include("questao", questao);
		result.include("tags", questao.getTagsEmString());
		result.include("numeroDeAlternativas", questao.getAlternativas().size());
		
		int[] respostas = new int[questao.getAlternativas().size()];
		if (questao.getRespostaUnica()) {
			int i, j;
			for (i=0, j=1;i<questao.getAlternativas().size(); i++, j=j*2)
				if (questao.getResposta() == j)
					respostas[i] = 1;
				else
					respostas[i] = 0;
		}
		else {
			int i, j;
			String binario = Integer.toBinaryString(questao.getResposta());
			for (i=0, j=1;i<questao.getAlternativas().size(); i++, j=j*2)
				if (i < binario.length())
					if (Integer.parseInt(binario.substring(i, i+1)) == 1)
						respostas[i] = 1;
					else
						respostas[i] = 0;
				else
					respostas[i] = 0;
		}
		
	
		result.include("respostas", respostas);
			
	}

	@Put
	@Path("/questoes/mult/{questao.id}")
	/**
	 * Verifica se a questão de múltipla escolha fornecida é válida e atualiza no banco de dados.
	 * 
	 * @param questao
	 */
	public void altera(QuestaoDeMultiplaEscolha questao,
			List<Integer> resposta, String tags, int numeroDeAlternativas) {

		questao.setTags(tags, tagDao);
		questao.setResposta(resposta);

		validator.validate(questao);
		validator.onErrorUsePageOf(this).alteracao(questao.getId());
		
		
		for (int i=0; i<numeroDeAlternativas; i++) {
			questao.getAlternativas().get(i).setQuestao(questao);			
		}
			
		for (int i=numeroDeAlternativas; i<10; i++) {
			questao.getAlternativas().remove(numeroDeAlternativas);
		}

		dao.atualiza(questao);

		result.redirectTo(this).lista();
	}

	@Delete
	@Path("/questoes/mult/{id}")
	/**
	 * Remove uma questão de múltipla escolha do banco de dados com o id fornecido.
	 * 
	 * @param id
	 */
	public void remove(Long id) {
		QuestaoDeMultiplaEscolha questao = dao.carrega(id);
		dao.remove(questao);
		result.redirectTo(this).lista();
	}

	@Get
	@Path("/questoes/mult")
	/**
	 * Devolve uma lista com todas as questões de múltipla escolha cadastradas 
	 * no banco de dados.
	 */
	public void lista() {
		result.include("tipoDaQuestao", TipoDeQuestao.MULTIPLAESCOLHA);
		result.include("lista", dao.listaTudo());
	}

	public void copia(QuestaoDeMultiplaEscolha questao) {
		dao.salva(questao);
		result.redirectTo(this).alteracao(questao.getId());	
	}
}
