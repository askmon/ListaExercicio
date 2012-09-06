package br.usp.ime.academicdevoir.controller;

//import java.util.List;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.usp.ime.academicdevoir.dao.DisciplinaDao;
import br.usp.ime.academicdevoir.entidade.Disciplina;
import br.usp.ime.academicdevoir.infra.Permission;
import br.usp.ime.academicdevoir.infra.Privilegio;
import br.usp.ime.academicdevoir.infra.UsuarioSession;

@Permission({ Privilegio.ADMINISTRADOR, Privilegio.PROFESSOR })
@Resource
public class DisciplinasController {

	private final Result result;

	private DisciplinaDao disciplinaDao;

	public DisciplinasController(Result result, DisciplinaDao disciplinaDao,
			UsuarioSession usuarioSession) {
		this.result = result;
		this.disciplinaDao = disciplinaDao;
	}

	// FIXME Arrumar home da disciplina
	@Get
	@Path("/disciplinas/home/{id}")
	public void home(Long id) {
		Disciplina d = disciplinaDao.carrega(id);
		result.include("disciplina", d);
		result.include("listaDeTurmas", d.getTurmas());

	}

	public void lista() {
		result.include("lista", disciplinaDao.listaTudo());
	}

	public void cadastro() {
	}

	public void cadastra(final Disciplina nova) {
		disciplinaDao.salvaDisciplina(nova);
		result.redirectTo(DisciplinasController.class).lista();
	}

	@Get
	@Path("/disciplinas/alteracao/{id}")
	public void alteracao(Long id) {
		result.include("disciplina", disciplinaDao.carrega(id));
	}

	public void altera(Long id, String novoNome) {
		Disciplina d;

		d = disciplinaDao.carrega(id);
		if (!novoNome.equals(""))
			d.setNome(novoNome);
		disciplinaDao.atualiza(d);
		result.redirectTo(DisciplinasController.class).lista();
	}

	public void remocao() {
	}

	public void remove(final Long id) {
		Disciplina disciplina = disciplinaDao.carrega(id);
		disciplinaDao.remove(disciplina);
		result.redirectTo(DisciplinasController.class).lista();
	}
}
