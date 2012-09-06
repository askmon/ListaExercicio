package br.usp.ime.academicdevoir.entidade;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.google.common.collect.Lists;

@Entity
@Table(name="listagerada")
public class ListaGerada {
	
	@Id
	@GeneratedValue
	private Long id;
	
	private double nota;
	
	@ManyToOne(cascade=CascadeType.ALL)
	private ListaDeExercicios lista;
	
	@ManyToOne(fetch=FetchType.LAZY)
	private Aluno aluno;
	
	@OneToMany(mappedBy="listaGerada", cascade=CascadeType.ALL)
	private List<ListaQuestao> listaQuestoes = Lists.newArrayList();
	
	private Date dataGeracao = new Date();
	
	private Date dataFinalizacao;
	
	private String codigoIndentificador;

	public ListaGerada() {
	}

	public ListaGerada(ListaDeExercicios lista, Aluno aluno) {
		this.lista = lista;
		this.aluno = aluno;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public double getNota() {
		return nota;
	}

	public void setNota(double nota) {
		this.nota = nota;
	}

	public ListaDeExercicios getLista() {
		return lista;
	}

	public void setLista(ListaDeExercicios lista) {
		this.lista = lista;
	}

	public Date getDataGeracao() {
		return dataGeracao;
	}

	public void setDataGeracao(Date dataGeracao) {
		this.dataGeracao = dataGeracao;
	}

	public Date getDataFinalizacao() {
		return dataFinalizacao;
	}

	public void setDataFinalizacao(Date dataFinalizacao) {
		this.dataFinalizacao = dataFinalizacao;
	}

	public String getCodigoIndentificador() {
		return codigoIndentificador;
	}

	public void setCodigoIndentificador(String codigoIndentificador) {
		this.codigoIndentificador = codigoIndentificador;
	}

	public Aluno getAluno() {
		return aluno;
	}

	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}

	public List<ListaQuestao> getListaQuestoes() {
		return listaQuestoes;
	}
	
	public List<Questao> getQuestoes() {
		List<Questao> questoes = new ArrayList<Questao>();
		for (ListaQuestao listaQuestao : listaQuestoes) {
			questoes.add(listaQuestao.getQuestao());
		}
		return questoes;
	}

	public void setListaQuestoes(List<ListaQuestao> listaQuestoes) {
		this.listaQuestoes = listaQuestoes;
	}

}
