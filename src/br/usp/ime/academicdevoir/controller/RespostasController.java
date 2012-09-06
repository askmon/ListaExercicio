package br.usp.ime.academicdevoir.controller;

import br.com.caelum.vraptor.Delete;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.interceptor.multipart.UploadedFile;
import br.usp.ime.academicdevoir.arquivos.Arquivos;
import br.usp.ime.academicdevoir.dao.ListaDeRespostasDao;
import br.usp.ime.academicdevoir.dao.QuestaoDao;
import br.usp.ime.academicdevoir.entidade.EstadoDaListaDeRespostas;
import br.usp.ime.academicdevoir.entidade.ListaDeRespostas;
import br.usp.ime.academicdevoir.entidade.Questao;
import br.usp.ime.academicdevoir.entidade.Resposta;
import br.usp.ime.academicdevoir.entidade.Turma;
import br.usp.ime.academicdevoir.infra.TipoDeQuestao;
import br.usp.ime.academicdevoir.infra.UsuarioSession;
import br.usp.ime.academicdevoir.infra.VerificadorDePrazos;

@Resource
public class RespostasController {

	private final ListaDeRespostasDao dao;
	private final UsuarioSession usuario;
	private final Arquivos arquivos;
	private final Result result;
	private final QuestaoDao questaoDao;

	public RespostasController(ListaDeRespostasDao dao,
			UsuarioSession usuario, Arquivos arquivos, Result result, QuestaoDao questaoDao) {
		this.dao = dao;
		this.questaoDao = questaoDao;
		this.usuario = usuario;
		this.arquivos = arquivos;
		this.result = result;
	}
	
	/**
	 * Salva uma resposta referente na lista de respostas fornecida.
	 * @param listaDeRespostas
	 * @param listaDeExercicios
	 */
	@Post
	@Path("/respostas/{listaDeRespostas.id}/turma/{turma.id}/cadastra")
	public void salvaResposta(ListaDeRespostas listaDeRespostas, Turma turma, Resposta resposta, String[] resposta2, Long idDaQuestao, 
			UploadedFile arquivo, int acao) {
	    String caminho;
	    int nmaxenvios, nenvios;
	    if (resposta == null) 
	    	resposta = new Resposta();
		
	    dao.recarrega(listaDeRespostas);
	    
	    
        if ( !VerificadorDePrazos.estaNoPrazo(listaDeRespostas.getListaDeExercicios().getPropriedades().getPrazoDeEntrega())) {
            listaDeRespostas.getPropriedades().setEstado(EstadoDaListaDeRespostas.FINALIZADA);
            result.redirectTo(TurmasController.class).home(turma.getId());            
            return;
        }
	    
		Questao questao = questaoDao.carrega(idDaQuestao);		
			
		if (questao.getTipo() == TipoDeQuestao.SUBMISSAODEARQUIVO && arquivo != null) {
			arquivos.salva(arquivo, idDaQuestao);
			resposta.setValor(arquivo.getFileName());
		}
		if (questao.getTipo() == TipoDeQuestao.CODIGO) {
		    caminho = arquivos.getPastaDaQuestao(questao.getId()).getAbsolutePath();
		    resposta.setCaminhoParaDiretorioDeTeste(caminho);
		}
		if(questao.getTipo() == TipoDeQuestao.MULTIPLAESCOLHA){
			if (resposta2 != null) {
				Integer valor = new Integer(0);
				for(int i = 0; i < resposta2.length; i++)
					valor += Integer.parseInt(resposta2[i]);
				resposta.setValor(valor.toString());
			}
		}
		
		resposta.setQuestao(questao);
		
		if(acao == 2) {/*Enviando*/
			if (listaDeRespostas.getListaDeExercicios().getPropriedades().
			    getNumeroMaximoDeEnvios() != null) {
		        nmaxenvios =  listaDeRespostas.getListaDeExercicios().getPropriedades().getNumeroMaximoDeEnvios();
			    nenvios = listaDeRespostas.getPropriedades().getNumeroDeEnvios();
			    if (listaDeRespostas.getRespostas().isEmpty())
			        listaDeRespostas.getPropriedades().setNumeroDeEnvios(nenvios + 1);
	        
			    if (listaDeRespostas.adiciona(resposta) == 0) {
			        listaDeRespostas.getPropriedades().setNumeroDeEnvios(nenvios + 1);
			    }
			    
			    if (listaDeRespostas.getPropriedades().getNumeroDeEnvios() >= nmaxenvios){
			        listaDeRespostas.getPropriedades().setEstado(EstadoDaListaDeRespostas.FINALIZADA);
			        dao.atualiza(listaDeRespostas);
		            result.redirectTo(TurmasController.class).home(usuario.getId());            
			        return;
			    }
			}
		}
		
		if (acao == 3) {
			listaDeRespostas.getPropriedades().setEstado(EstadoDaListaDeRespostas.FINALIZADA);
		}
        
        listaDeRespostas.adiciona(resposta);
        if (listaDeRespostas.getPropriedades().getEstado() == null) {
        	listaDeRespostas.getPropriedades().setEstado(EstadoDaListaDeRespostas.SALVA);
        }
		dao.atualiza(listaDeRespostas);
       
        result.redirectTo(TurmasController.class).home(turma.getId());            
	}
	
	/**
	 * Remove a lista de respostas fornecida do banco de dados.
	 * @param id
	 */
	@Delete
	@Path("/respostas/{id}")
	public void removeRespostas(Long id) {
		ListaDeRespostas listaDeRespostas = dao.carrega(id);
		dao.remove(listaDeRespostas);		
		/*result.redirectTo;*/
	}
}






















