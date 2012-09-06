package br.usp.ime.academicdevoir.interceptor;
import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.resource.ResourceMethod;
import br.usp.ime.academicdevoir.dao.AlunoDao;
import br.usp.ime.academicdevoir.dao.TurmaDao;
import br.usp.ime.academicdevoir.entidade.Aluno;
import br.usp.ime.academicdevoir.infra.UsuarioSession;



@Intercepts
public class TurmaInterceptor implements Interceptor {

	private final TurmaDao turmaDao;
	
	private UsuarioSession usuarioSession;
	
	private final Result result;

	private final AlunoDao alunoDao;
	
	public TurmaInterceptor(TurmaDao turmaDao, AlunoDao alunoDao, UsuarioSession usuarioSession,
			Result result) {
		super();
		this.turmaDao = turmaDao;
		this.usuarioSession = usuarioSession;
		this.result = result;
		this.alunoDao = alunoDao;
	}

	public boolean accepts(ResourceMethod arg0) {
		return usuarioSession.isLogged();
	}

	public void intercept(InterceptorStack stack, ResourceMethod method,
			Object instance) throws InterceptionException {
		if (usuarioSession.isAluno()) {
			Aluno aluno = alunoDao.carrega(usuarioSession.getId());
			result.include("turmas", aluno.getTurmas());
		} else if (usuarioSession.isProfessor()) {
			result.include("turmas", turmaDao.listaPorProfessor(usuarioSession.getId()));
		}
		
		stack.next(method, instance);
	}
	


}
