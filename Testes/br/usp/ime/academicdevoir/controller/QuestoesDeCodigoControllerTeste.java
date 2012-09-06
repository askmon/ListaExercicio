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
import br.usp.ime.academicdevoir.dao.DisciplinaDao;
import br.usp.ime.academicdevoir.dao.QuestaoDeCodigoDao;
import br.usp.ime.academicdevoir.dao.TagDao;
import br.usp.ime.academicdevoir.entidade.Disciplina;
import br.usp.ime.academicdevoir.entidade.Professor;
import br.usp.ime.academicdevoir.entidade.QuestaoDeCodigo;
import br.usp.ime.academicdevoir.entidade.Tag;
import br.usp.ime.academicdevoir.infra.Privilegio;
import br.usp.ime.academicdevoir.infra.UsuarioSession;
import br.usp.ime.academicdevoir.util.Given;

public class QuestoesDeCodigoControllerTeste {

    private QuestoesDeCodigoController questoesC;
    private QuestaoDeCodigoDao dao;

    private MockResult result;

    private JSR303MockValidator validator;

    private UsuarioSession usuarioSession;
    private TagDao tagDao;
    private DisciplinaDao disciplinaDao;
    private Disciplina disciplina;

    @Before
    public void SetUp() {       
        Professor professor = new Professor();
        professor.setPrivilegio(Privilegio.ADMINISTRADOR);
        
        disciplina = new Disciplina();
        
        usuarioSession = new UsuarioSession();
        usuarioSession.setUsuario(professor);
    
        dao = mock(QuestaoDeCodigoDao.class);
        disciplinaDao = mock(DisciplinaDao.class);
        tagDao = mock(TagDao.class);
        result = spy(new MockResult());
        validator = spy(new JSR303MockValidator());
        questoesC = new QuestoesDeCodigoController(dao, tagDao, result,
                validator, usuarioSession, disciplinaDao);
        
        when(tagDao.buscaPeloNome(any(String.class))).thenReturn(new Tag("tagQualquer"));
        when(disciplinaDao.carrega(1l)).thenReturn(Given.novaDisciplina());
    }

    @Test
    public void cadastra() {
        QuestaoDeCodigo questao = new QuestaoDeCodigo();
        questao.setId(10L);
        questao.setCodigoDeTeste("Codigo de teste");
        questao.setEnunciado("Enuciado");
        questao.setDisciplina(Given.novaDisciplina());
        questoesC.cadastra(questao, new String("tagQualquer"));
    
        verify(validator).validate(questao);
        verify(validator).onErrorRedirectTo(QuestoesController.class);
        verify(dao).salva(questao);
        verify(result).redirectTo(questoesC);
    }

    @Test
    public void altera() {
        QuestaoDeCodigo questao = new QuestaoDeCodigo();
        questao.setCodigoDeTeste("Codigo de teste");
        questao.setEnunciado("Enuciado");

        questoesC.altera(questao, new String("tagQualquer"));
        
        verify(validator).validate(questao);
        verify(validator).onErrorUsePageOf(QuestoesDeCodigoController.class);
        verify(dao).atualiza(questao);
        verify(result).redirectTo(questoesC);
    }
    
    @Test
    public void remove() {
        QuestaoDeCodigo temp = new QuestaoDeCodigo();
        when(dao.carrega(0L)).thenReturn(temp);
        questoesC.remove(0L);
        
        verify(dao).remove(temp);
        verify(result).redirectTo(questoesC);
    }
}