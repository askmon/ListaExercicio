package br.usp.ime.academicdevoir.controller;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import br.com.caelum.vraptor.util.test.JSR303MockValidator;
import br.com.caelum.vraptor.util.test.MockResult;
import br.usp.ime.academicdevoir.dao.ProfessorDao;
import br.usp.ime.academicdevoir.dao.TurmaDao;
import br.usp.ime.academicdevoir.entidade.Professor;
import br.usp.ime.academicdevoir.entidade.Turma;
import br.usp.ime.academicdevoir.infra.Privilegio;
import br.usp.ime.academicdevoir.infra.UsuarioSession;

public class ProfessoresControllerTeste {
    /**
	 * @uml.property  name="profC"
	 * @uml.associationEnd  
	 */
    private ProfessoresController profC;
    /**
	 * @uml.property  name="result"
	 * @uml.associationEnd  
	 */
    private MockResult result;
    /**
	 * @uml.property  name="professordao"
	 * @uml.associationEnd  
	 */
    private ProfessorDao professordao;

    private TurmaDao turmaDao;
    
    /**
	 * @uml.property  name="usuarioSession"
	 * @uml.associationEnd  readOnly="true"
	 */
    private UsuarioSession usuarioSession;
	private Professor professor;
	private JSR303MockValidator validator;

    @Before
    public void SetUp() {
		Professor admin = new Professor();
		admin.setId(0L);
		admin.setPrivilegio(Privilegio.ADMINISTRADOR);
		
		usuarioSession = new UsuarioSession();
		usuarioSession.setUsuario(admin);
		
        result = spy(new MockResult());
        validator = spy(new JSR303MockValidator());
        professordao = mock(ProfessorDao.class);
        turmaDao = mock(TurmaDao.class);
        profC = new ProfessoresController(result, professordao, turmaDao, usuarioSession, validator);
        
        professor = new Professor();
        professor.setId(0L);
        professor.setNome("professor");
        professor.setLogin("professor");
        professor.setSenha("senha");
        professor.setEmail("email@usp.br");
        
        when(professordao.carrega(professor.getId().longValue())).thenReturn(professor);
    }

    @Test
    public void testeCadastra() {
    	professor.setSenha("senhadoprofessor");
        profC.cadastra(professor);
        verify(professordao).salvaProfessor(professor);
        verify(result).redirectTo(ProfessoresController.class);
    }
    
    @Test
    public void testeListaTurmas() {
    	profC.listaTurmas(this.professor.getId());
    	
    	List<Turma> turmas = result.included("turmas");
    	assertNotNull(turmas);
    }

    @Test
    public void testeAlteracao() {
    	profC.alteracao(this.professor.getId());
    	
    	Professor professor = result.included("professor");
    	assertNotNull(professor);
    }
    
    @Test
    public void testeAltera() {        
        profC.altera(professor.getId(), "novo nome", "novoeamil@usp.br", "nova senha");
        verify(professordao).atualizaProfessor(professor);
        verify(result).redirectTo(ProfessoresController.class);
    }

    @Test
    public void testeRemove() {
        profC.remove(professor.getId());
        verify(professordao).removeProfessor(professor);
        verify(result).redirectTo(ProfessoresController.class);
    }
    
    @Test
    public void testeMudartipo() {
    	profC.mudarTipo(professor.getId());
    	
    	verify(professordao).alteraTipo(professor.getId());
    	verify(result).redirectTo(ProfessoresController.class);
    }
}
