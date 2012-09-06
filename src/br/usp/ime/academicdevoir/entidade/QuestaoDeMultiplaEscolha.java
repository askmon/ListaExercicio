package br.usp.ime.academicdevoir.entidade;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import br.usp.ime.academicdevoir.dao.TagDao;
import br.usp.ime.academicdevoir.infra.TipoDeQuestao;

@Entity
public class QuestaoDeMultiplaEscolha extends Questao {

	private Boolean respostaUnica = false;

	/**
	 * @uml.property name="alternativas"
	 * @uml.associationEnd multiplicity="(0 -1)" elementType="java.lang.String"
	 */
	/*
	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(name = "alternativasDasQuestoes")
	private List<String> alternativas;
	*/
	@OneToMany(mappedBy = "questao", fetch=FetchType.LAZY)
	@Cascade(CascadeType.ALL)
	private List<AlternativasMultiplaEscolha> alternativas;

	/**
	 * @uml.property name="resposta"
	 */
	//@NotNull
	private Integer resposta;

	public Boolean getRespostaUnica() {
		return respostaUnica;
	}

	public void setRespostaUnica(Boolean respostaUnica) {
		this.respostaUnica = respostaUnica;
	}

	public List<AlternativasMultiplaEscolha> getAlternativas() {
		return alternativas;
	}

	public void setAlternativas(List<AlternativasMultiplaEscolha> alternativas) {
		this.alternativas = alternativas;
	}

	/**
	 * @return
	 * @uml.property name="resposta"
	 */
	public Integer getResposta() {
		return resposta;
	}

	/**
	 * @param resposta
	 * @uml.property name="resposta"
	 */
	public void setResposta(Integer resposta) {
		this.resposta = resposta;
	}
	
	public void setResposta(List<Integer> resposta) {
		this.resposta = 0;
		if (resposta == null) return;
				
		for (Integer valor : resposta) {
			this.resposta += valor;
		}
	}

	public TipoDeQuestao getTipo() {
		return TipoDeQuestao.MULTIPLAESCOLHA;
	}

	public String getRenderizacao() {
		String htmlResult = "";
		StringBuffer buffer = new StringBuffer();

		buffer.append("<table>");
		for (int i = 0, valorResposta = 1; i < alternativas.size(); i++, valorResposta *= 2) {
			if (this.getRespostaUnica())
				buffer.append("<tr><td><input type=\"radio\" name=\"resposta.valor\" value=\"");
			else
				buffer.append("<tr><td><input type=\"checkbox\" name=\"resposta2\" value=\"");
			buffer.append(valorResposta);
			buffer.append("\" /></td><td>");
			buffer.append(alternativas.get(i));
			buffer.append("</td></tr>");
		}

		
		buffer.append("<input type=\"hidden\" name=\"idDaQuestao\" value=\"");
		buffer.append(this.getId());
		buffer.append("\" />");

		buffer.append("</table>");

		htmlResult = buffer.toString();

		return htmlResult;
	}

	public String getRenderAlteracao(Resposta resposta) {
		if (resposta == null || resposta.getValor() == null || resposta.getValor().isEmpty()) return getRenderizacao();
		

		String htmlResult = "";
		StringBuffer buffer = new StringBuffer();

		buffer.append("<table>");
		for (int i = 0, valorResposta = 1; i < alternativas.size(); i++, valorResposta *= 2) {
			
			if (this.getRespostaUnica())
				buffer.append("<tr><td><input type=\"radio\"");
			else
				buffer.append("<tr><td><input type=\"checkbox\"");
			
			if (this.getRespostaUnica()){
				if (Integer.parseInt(resposta.getValor()) == valorResposta) 
					buffer.append(" checked=\"checked\"");
			}
			else {
				String binario = Integer.toBinaryString(Integer.parseInt(resposta.getValor()));
				if (i < binario.length()){
					int i2 = Integer.parseInt(binario.substring(i, i+1));
					if (i2 == 1) 
						buffer.append(" checked=\"checked\"");
				}
			}
			
			
			if(this.getRespostaUnica())
				buffer.append(" name=\"resposta.valor\" value=\"");
			else
				buffer.append(" name=\"resposta2\" value=\"");
			buffer.append(valorResposta);
			buffer.append("\" /></td><td>");
			buffer.append(alternativas.get(i));
			buffer.append("</td></tr>");
		}

		buffer.append("<input type=\"hidden\" name=\"idDaQuestao\" value=\"");
		buffer.append(this.getId());
		buffer.append("\" />");

		buffer.append("</table>");

		htmlResult = buffer.toString();

		return htmlResult;
	}
	
	public String getRenderCorrecao (Resposta resposta) {
	    int i, valorResposta;
	    if (resposta == null)
            resposta = new Resposta();
        
        String htmlResult = "";
        StringBuffer buffer = new StringBuffer();
        buffer.append("<p>");
        if (resposta.getValor() != null) {
            for (i = 0, valorResposta = 1; i < alternativas.size(); i++,
                 valorResposta *= 2) 
                if (Integer.parseInt(resposta.getValor()) == valorResposta)
                    break;
            if (i < alternativas.size())
                buffer.append(alternativas.get(i));

        }
        buffer.append("</p>");
        
        if (resposta.getQuestao().getComentario() != null && !resposta.getQuestao().getComentario().equals("")) {
        	buffer.append("<p> Comentários: ");
            buffer.append(resposta.getQuestao().getComentario());
            buffer.append("</p>");
        }
        buffer.append("<p> Nota: ");
        if (resposta.getNota() != null)
            buffer.append(resposta.getNota());
        buffer.append("</p>");

        htmlResult = buffer.toString();

        return htmlResult;
        
    }
	
	public String getAlternativasCorretas() {
		int valor, resposta = this.resposta;
		Boolean primeira = true;
		StringBuffer alternativas = new StringBuffer();
		
		for (int i = 1; resposta != 0; resposta /= 2, i++) {
			valor = resposta % 2;
			if (valor == 1) {
				if (!primeira) {
					if (resposta == 1)	alternativas.append(" e ");
					else alternativas.append(", ");
				}
				primeira = false;
				alternativas.append(i);
			}
		}
		
		return alternativas.toString(); 
	}

	public Boolean respostaDoAlunoEhCorreta(Resposta respostaAluno) {
		return respostaAluno.getValor().equals(this.resposta.toString());
	}
	
	public QuestaoDeMultiplaEscolha copia(TagDao dao) {
		QuestaoDeMultiplaEscolha questao = new QuestaoDeMultiplaEscolha();    	
    	questao.enunciado = this.enunciado;
    	questao.respostaUnica = this.respostaUnica;
    	questao.resposta = this.resposta;
    	/*
    	questao.alternativas = new ArrayList<String>();
    	
    	for (String alternativa : this.alternativas) {
    		questao.alternativas.add(alternativa);
    	}
    	*/
    			
    	questao.setTags(this.getTagsEmString(), dao);
    	
    	return questao;
	}
}
