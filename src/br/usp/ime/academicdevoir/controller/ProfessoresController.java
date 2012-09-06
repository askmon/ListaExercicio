package br.usp.ime.academicdevoir.controller;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.usp.ime.academicdevoir.dao.ProfessorDao;
import br.usp.ime.academicdevoir.dao.TurmaDao;
import br.usp.ime.academicdevoir.entidade.Professor;
import br.usp.ime.academicdevoir.entidade.Usuario;
import br.usp.ime.academicdevoir.infra.Criptografia;
import br.usp.ime.academicdevoir.infra.Permission;
import br.usp.ime.academicdevoir.infra.Privilegio;
import br.usp.ime.academicdevoir.infra.UsuarioSession;

@Permission({ Privilegio.ADMINISTRADOR, Privilegio.PROFESSOR })
@Resource
/**
 * Controlador de professores.
 */
public class ProfessoresController {
	private final Result result;

	private ProfessorDao professorDao;
	
	private final TurmaDao turmaDao;
	
	private UsuarioSession usuarioSession;
	
	private Validator validator;


	public ProfessoresController(Result result, ProfessorDao professorDao,
			TurmaDao turmaDao, UsuarioSession usuarioSession,
			Validator validator) {
		this.result = result;
		this.professorDao = professorDao;
		this.turmaDao = turmaDao;
		this.usuarioSession = usuarioSession;
		this.validator = validator;
	}

	public void menu() {
	    
	}

	public void home() {
		result.redirectTo(ProfessoresController.class).listaTurmas(usuarioSession.getUsuario().getId());
	}

	public void lista() {
		result.include("listaDeProfessores", professorDao.listaTudo());
	}
	
	@Get("/professor/{idProfessor}/turmas")
	public void listaTurmas(Long idProfessor) {
	    result.include("turmas", turmaDao.buscarPor(idProfessor));   
	}

	@Permission(Privilegio.ADMINISTRADOR)
	/**
     * Método está associado ao .jsp do formulário de cadastro de um professor no sistema.
     */
	public void cadastro() {
	}


	@Permission(Privilegio.ADMINISTRADOR)
	public void cadastra(final Professor novo) {
		validator.validate(novo);
		validator.onErrorUsePageOf(ProfessoresController.class).cadastro();
		
		novo.setSenha(new Criptografia().geraMd5(novo.getSenha()));
		professorDao.salvaProfessor(novo);
		result.redirectTo(ProfessoresController.class).lista();
	}

	@Permission({ Privilegio.ADMINISTRADOR, Privilegio.PROFESSOR })
    public void alteracao(Long id) {
		Usuario u = usuarioSession.getUsuario();
		if ((u.getId().longValue() != id) && (u.getPrivilegio() == Privilegio.PROFESSOR)) {
			result.redirectTo(LoginController.class).acessoNegado();
			return;
		}
        result.include("professor", professorDao.carrega(id));
    }

	@Permission({ Privilegio.ADMINISTRADOR, Privilegio.PROFESSOR })
	public void altera(Long id, String novoNome, String novoEmail, String novaSenha) {
		Usuario u = usuarioSession.getUsuario();
		if ((u.getId().longValue() != id) && (u.getPrivilegio() == Privilegio.PROFESSOR)) {
			result.redirectTo(LoginController.class).acessoNegado();
			return;
		}
		
		Professor p;
		
		p = professorDao.carrega(id);
		p.setNome(novoNome);
		p.setEmail(novoEmail);
		p.setSenha(novaSenha);
		
		validator.validate(p);
		validator.onErrorUsePageOf(ProfessoresController.class).alteracao(id);
		
		p.setSenha(new Criptografia().geraMd5(novaSenha));		
		professorDao.atualizaProfessor(p);
		result.redirectTo(ProfessoresController.class).home();
	}

	@Permission(Privilegio.ADMINISTRADOR)
	public void remove(final Long id) {
		Professor professor;
		
		professor = professorDao.carrega(id);
		professorDao.removeProfessor(professor);
		result.redirectTo(ProfessoresController.class).lista();
	}
	
	@Permission(Privilegio.ADMINISTRADOR)
	public void mudarTipo(Long id) {
		
		professorDao.alteraTipo(id);
		result.redirectTo(ProfessoresController.class).lista();
	}
	
	public void mudanca () {
	}
	
}
