package br.usp.ime.academicdevoir.controller;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import br.com.caelum.vraptor.util.test.JSR303MockValidator;
import br.com.caelum.vraptor.util.test.MockResult;
import br.usp.ime.academicdevoir.controller.QuestoesDeTextoController;
import br.usp.ime.academicdevoir.dao.DisciplinaDao;
import br.usp.ime.academicdevoir.dao.QuestaoDeTextoDao;
import br.usp.ime.academicdevoir.dao.TagDao;
import br.usp.ime.academicdevoir.entidade.Professor;
import br.usp.ime.academicdevoir.entidade.QuestaoDeTexto;
import br.usp.ime.academicdevoir.entidade.Tag;
import br.usp.ime.academicdevoir.infra.Privilegio;
import br.usp.ime.academicdevoir.infra.UsuarioSession;
import br.usp.ime.academicdevoir.util.Given;

public class QuestoesDeTextoControllerTeste {
    /**
	 * @uml.property  name="questoesC"
	 * @uml.associationEnd  
	 */
    private QuestoesDeTextoController questoesC;
    /**
	 * @uml.property  name="dao"
	 * @uml.associationEnd  
	 */
    private QuestaoDeTextoDao dao;
    /**
	 * @uml.property  name="result"
	 * @uml.associationEnd  
	 */
    private MockResult result;
    /**
	 * @uml.property  name="validator"
	 * @uml.associationEnd  
	 */
    private JSR303MockValidator validator;
    /**
	 * @uml.property  name="usuarioSession"
	 * @uml.associationEnd
	 */
	private UsuarioSession usuarioSession;
	private TagDao tagDao;
	private DisciplinaDao disciplinaDao;


    @Before
    public void SetUp() {		
		Professor professor = new Professor();
		professor.setPrivilegio(Privilegio.ADMINISTRADOR);
		
		usuarioSession = new UsuarioSession();
		usuarioSession.setUsuario(professor);

        dao = mock(QuestaoDeTextoDao.class);
		tagDao = mock(TagDao.class);
		disciplinaDao = mock(DisciplinaDao.class);

        result = spy(new MockResult());
        validator = spy(new JSR303MockValidator());
        questoesC = new QuestoesDeTextoController(dao, tagDao, disciplinaDao, result,
                validator, usuarioSession);
        
		when(tagDao.buscaPeloNome(any(String.class))).thenReturn(new Tag("tagQualquer"));
    }

    @Test
    public void testeAdiciona() {
        QuestaoDeTexto questao = Given.novaQuestaoDeTexto();
        when(disciplinaDao.carrega(questao.getDisciplina().getId())).thenReturn(questao.getDisciplina());
        questoesC.cadastra(questao, new String("tagQualquer"));

        verify(validator).validate(questao);
        verify(validator).onErrorRedirectTo(QuestoesController.class);
        verify(dao).salva(questao);
        verify(result).redirectTo(questoesC);
    }
    
    @Test
    public void testeAtualiza() {
        QuestaoDeTexto questao = Given.novaQuestaoDeTexto();
        questoesC.altera(questao, new String("tagQualquer"));
        
        verify(validator).validate(questao);
        verify(validator).onErrorUsePageOf(QuestoesDeTextoController.class);
        verify(dao).atualiza(questao);
        verify(result).redirectTo(questoesC);
    }
    
    @Test
    public void testeRemove() {
        QuestaoDeTexto temp = new QuestaoDeTexto();
        when(dao.carrega(0L)).thenReturn(temp);
        questoesC.remove(0L);
        
        verify(dao).remove(temp);
        verify(result).redirectTo(questoesC);
    }
}
