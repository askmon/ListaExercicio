package br.usp.ime.academicdevoir.controller;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.util.Calendar;
import java.util.List;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.validator.Validations;
import br.usp.ime.academicdevoir.dao.AlunoDao;
import br.usp.ime.academicdevoir.dao.DisciplinaDao;
import br.usp.ime.academicdevoir.dao.TurmaDao;
import br.usp.ime.academicdevoir.entidade.Aluno;
import br.usp.ime.academicdevoir.entidade.Disciplina;
import br.usp.ime.academicdevoir.entidade.Turma;
import br.usp.ime.academicdevoir.entidade.Usuario;
import br.usp.ime.academicdevoir.infra.Permission;
import br.usp.ime.academicdevoir.infra.Privilegio;
import br.usp.ime.academicdevoir.infra.UsuarioSession;

@Resource
/**
 * Controlador de turmas.
 */
public class TurmasController {
    private final Result result;
    
    private final Validator validator;
    
    private TurmaDao turmaDao;

    private DisciplinaDao disciplinaDao;

    private AlunoDao alunoDao;

    private UsuarioSession usuarioSession;
	
    public TurmasController(Result result, Validator validator, TurmaDao turmaDao,
            DisciplinaDao disciplinaDao, AlunoDao alunoDao,
            UsuarioSession usuarioSession) {
        this.result = result;
        this.validator = validator;
        this.turmaDao = turmaDao;
        this.disciplinaDao = disciplinaDao;
        this.alunoDao = alunoDao;
        this.usuarioSession = usuarioSession;
    }

    /**
     * Método associado à home page da turma com id fornecido.
     * 
     * @param id
     */
    @Path("/turmas/home/{id}")
    public void home(Long id) {
        Turma turma = turmaDao.carrega(id);
        result.include("turma", turma);
        result.include("listaDeListas", turma.getListaDeExercicios());
    }

    /**
     * Método associado ao menu da pagina da turma.
     */
    public void menu() {

    }

    /**
     * Método associado ao .jsp que lista as turmas cadastradas no banco de
     * dados.
     */
    public void lista() {
        result.include("listaDeTurmas", turmaDao.listaTudo());
    }

    /**
     * Método associado ao .jsp que lista os alunos inscritos na turma
     * 
     * @param idTurma
     *            id da turma
     */
    public void listaAlunos(long idTurma) {
        result.include("turma", turmaDao.carrega(idTurma));
    }
    
    /**
     * Método associado ao .jsp que lista os alunos e listas de uma turma
     * 
     * @param idTurma
     *            id da turma
     */
    public void listaAlunosEListas(long idTurma) {
        result.include("turma", turmaDao.carrega(idTurma));
    }

    @Permission({ Privilegio.ADMINISTRADOR, Privilegio.PROFESSOR })
    @Get("/turmas/nova")    
    public void cadastro() {    	
        
        Calendar dataDeHoje = Calendar.getInstance();
        result.include("diaAtual", dataDeHoje.get(Calendar.DAY_OF_MONTH));
        result.include("mesAtual", dataDeHoje.get(Calendar.MONTH) + 1);
        result.include("anoAtual", dataDeHoje.get(Calendar.YEAR));
        result.include("disciplinas", disciplinaDao.listaTudo());
    }

    @Permission({ Privilegio.ADMINISTRADOR, Privilegio.PROFESSOR })
    public void cadastra(final Turma nova, final List<Integer> prazoDeMatricula) {	
    	
    	
    	validator.checking(new Validations() {{
    		that(nova.getDisciplina().getId(), is(notNullValue()), "disciplina", "disciplina.notNull");
    	}});
    	validator.validate(nova);
    	validator.onErrorForwardTo(this).cadastro();
		
		nova.setPrazoDeMatricula(prazoDeMatricula);
		
        turmaDao.salvaTurma(nova);

        result.redirectTo(ProfessoresController.class).listaTurmas(nova.getProfessor().getId());
    }

    @Permission({ Privilegio.ADMINISTRADOR, Privilegio.PROFESSOR })
    public void alteracao(Long id) {
        Turma turma = turmaDao.carrega(id);
        Usuario u = usuarioSession.getUsuario();
		if(u.getPrivilegio() == Privilegio.PROFESSOR && u.getId() != turma.getProfessor().getId()) {
			result.redirectTo(LoginController.class).acessoNegado();
			return;
		}
		
        result.include("turma", turma);
        
        Calendar dataDeHoje = Calendar.getInstance();
        result.include("diaAtual", dataDeHoje.get(Calendar.DAY_OF_MONTH));
        result.include("mesAtual", dataDeHoje.get(Calendar.MONTH) + 1);
        result.include("anoAtual", dataDeHoje.get(Calendar.YEAR));
        result.include("disciplinas", disciplinaDao.listaTudo());
        
        
    }

    @Permission({ Privilegio.ADMINISTRADOR, Privilegio.PROFESSOR })
    public void altera(final Turma turma, List<Integer> prazoDeMatricula) {
        Usuario u = usuarioSession.getUsuario();
        Turma turmaEncontrada = turmaDao.carrega(turma.getId());

		if(u.getPrivilegio() == Privilegio.PROFESSOR && u.getId() != turmaEncontrada.getProfessor().getId()) {
			result.redirectTo(LoginController.class).acessoNegado();
			return;
		}

		validator.checking(new Validations() {{
			that(turma.getDisciplina().getId(), is(notNullValue()), "disciplina", "disciplina.notNull");
		}});
		validator.validate(turma);
		validator.onErrorRedirectTo(this).alteracao(turma.getId());
		turma.setProfessor(turmaEncontrada.getProfessor());
    	turma.setPrazoDeMatricula(prazoDeMatricula);
        turmaDao.atualizaTurma(turma);
        result.redirectTo(this).home(turma.getId());
    }

    @Permission({ Privilegio.ADMINISTRADOR, Privilegio.PROFESSOR })
    /**
     * Método associado ao .jsp com formulário para remoção de cadastro de
     * turma.
     */
    public void remocao() {
    	
    }

    @Permission({ Privilegio.ADMINISTRADOR, Privilegio.PROFESSOR })
    /**
     * Remove uma turma do banco de dados com o id fornecido.
     * 
     * @param id
     */
    public void remove(final Long id) {
        Turma turma = turmaDao.carrega(id);
        Usuario u = usuarioSession.getUsuario();
		if(u.getPrivilegio() == Privilegio.PROFESSOR && u.getId() != turma.getProfessor().getId()) {
			result.redirectTo(LoginController.class).acessoNegado();
			return;
		}

        turmaDao.remove(turma);
        result.redirectTo(ProfessoresController.class).listaTurmas(u.getId());    
    }

    /**
     * Remove a matricula do aluno na turma.
     * 
     * @param idAluno
     *            id do aluno
     * @param idTurma
     *            id da turma
     */
    public void removeMatricula(Long idAluno, Long idTurma) {
        Aluno aluno = alunoDao.carrega(idAluno);
        Turma turma = turmaDao.carrega(idTurma);
        Usuario u = usuarioSession.getUsuario();
		if(u.getPrivilegio() != Privilegio.ADMINISTRADOR && !(u.getId() == turma.getProfessor().getId() || u.getId() == aluno.getId())) {
			result.redirectTo(LoginController.class).acessoNegado();
			return;
		}
        
        alunoDao.removeMatricula(aluno, turma);
        if(u.getPrivilegio() == Privilegio.ALUNO)
        	usuarioSession.setUsuario(aluno);
        if(usuarioSession.getUsuario().getPrivilegio() == Privilegio.ALUNO)
        	result.redirectTo(AlunosController.class).listaTurmas(idAluno);
        else
        	result.redirectTo(TurmasController.class).listaAlunos(idTurma);
    }


	@Get("/turmas/disciplina/{id}")
	public void turmas(Long id) {
		
		Disciplina disciplina = disciplinaDao.carrega(id);
		
		result.include("turmas", turmaDao.buscarPor(disciplina));
		result.include("tags", disciplina.getTags());
		
	}




}
