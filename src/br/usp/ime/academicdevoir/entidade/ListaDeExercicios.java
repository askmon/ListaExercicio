package br.usp.ime.academicdevoir.entidade;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.Valid;


@Entity
public class ListaDeExercicios {

	@Id
	@GeneratedValue
	private Long id;

	@Embedded
	@Valid
	private PropriedadesDaListaDeExercicios propriedades;	

	@ManyToMany
	@JoinTable(name="ListaDeExercicios_Turma")
	private List<Turma> turmas;
	
	@ManyToMany
	private List<TagsDaLista> tagsDaLista;

	@ElementCollection
	@CollectionTable(name = "questoesDaLista") 
	private List<QuestaoDaLista> questoes;

	@OneToMany(mappedBy = "listaDeExercicios", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<ListaDeRespostas> respostas;
	
	@OneToMany(mappedBy="lista", cascade=CascadeType.ALL)
	private List<ListaGerada> listasGeradas;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Turma> getTurmas() {
		return turmas;
	}
	
	public Turma getTurma(int id){
		return turmas.get(id);
	}

	public void setTurmas(List<Turma> turmas) {
		this.turmas = turmas;
	}

	public List<QuestaoDaLista> getQuestoesDaLista() {
		Collections.sort(questoes, new Comparator<QuestaoDaLista>() {  
            public int compare(QuestaoDaLista q1, QuestaoDaLista q2) {  
                return q1.getOrdem() < q2.getOrdem() ? -1 : (q1.getOrdem() > q2.getOrdem() ? +1 : 0);  
            }  
        }); 
		return questoes;
	}

	public List<Questao> getQuestoes() {
		List<QuestaoDaLista> questoesDaLista = getQuestoesDaLista();
		List<Questao> questoes = new ArrayList<Questao>();
		
		for (QuestaoDaLista questaoDaLista : questoesDaLista) {
			questoes.add(questaoDaLista.getQuestao());
		}
		return questoes;
	}
	
	public void setQuestoes(List<QuestaoDaLista> questoes) {
		this.questoes = questoes;
	}

	public List<ListaDeRespostas> getRespostas() {
		return respostas;
	}

	public void setRespostas(List<ListaDeRespostas> respostas) {
		this.respostas = respostas;
	}

	public PropriedadesDaListaDeExercicios getPropriedades() {
		return propriedades;
	}

	public void setPropriedades(PropriedadesDaListaDeExercicios propriedades) {
		this.propriedades = propriedades;
	}
	
	public void getPesos() {
		List<Integer> pesos = new ArrayList<Integer>();
		for(QuestaoDaLista questao : questoes) {
			pesos.add(questao.getPeso());
		}
	}

	public List<TagsDaLista> getTags() {
		return tagsDaLista;
	}

	public void setTags(List<TagsDaLista> tags) {
		this.tagsDaLista = tags;
	}

	public List<ListaGerada> getListasGeradas() {
		return listasGeradas;
	}

	public void setListasGeradas(List<ListaGerada> listasGeradas) {
		this.listasGeradas = listasGeradas;
	}

}
