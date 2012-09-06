package br.usp.ime.academicdevoir.entidade;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;


@Entity
public class TagsDaLista {

	@Id
	@GeneratedValue
	private Long id;
	
	@ManyToOne
	private Tag tag;
	
	private int quantidade;
	
	@ManyToMany
	private List<ListaDeExercicios> listaDeExercicios;

	
	public Tag getTag() {
		return tag;
	}

	public void setTag(Tag tag) {
		this.tag = tag;
	}

	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<ListaDeExercicios> getListaDeExercicios() {
		return listaDeExercicios;
	}

	public void setListaDeExercicios(List<ListaDeExercicios> listaDeExercicios) {
		this.listaDeExercicios = listaDeExercicios;
	}
}
