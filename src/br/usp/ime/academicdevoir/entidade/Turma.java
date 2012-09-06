package br.usp.ime.academicdevoir.entidade;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class Turma {

	@Id
	@GeneratedValue
	private Long id;

	@NotEmpty(message="Informe o nome da turma")
	private String nome;

	@ManyToOne
	private Professor professor;

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "Turma_Aluno", joinColumns = @JoinColumn(name = "turma_id"), inverseJoinColumns = @JoinColumn(name = "aluno_id"))
	@OrderBy("nome")
	private Collection<Aluno> alunos = new ArrayList<Aluno>();

	@ManyToOne
	private Disciplina disciplina;
	
	@ManyToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinTable(name = "ListaDeExercicios_Turma", joinColumns = @JoinColumn(name = "turmas_id"), inverseJoinColumns = @JoinColumn(name = "ListaDeExercicios_id"))
	private List<ListaDeExercicios> listaDeExercicios;

	@Temporal(TemporalType.DATE)
	private Date prazoDeMatricula;
	
	private String temPrazo;
	
	private boolean status = true;
		
	public String getTemPrazo() {
		return temPrazo;
	}

	public void setTemPrazo(String temPrazo) {
		this.temPrazo = temPrazo;
	}

	public Disciplina getDisciplina() {
		return disciplina;
	}
	
	public void setDisciplina(Disciplina disciplina) {
		this.disciplina = disciplina;
	}

	public Collection<Aluno> getAlunos() {
		return alunos;
	}

	public void setAlunos(Collection<Aluno> alunos) {
		this.alunos = alunos;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Professor getProfessor() {
		return professor;
	}

	public void setProfessor(Professor professor) {
		this.professor = professor;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getPrazoDeMatricula() {
		return prazoDeMatricula;
	}
	
	public void setPrazoDeMatricula(List<Integer> prazoDeMatricula) {
		Calendar data = Calendar.getInstance();
		if(prazoDeMatricula == null || prazoDeMatricula.size() != 3) {
			System.out.println("Erro!! Parametro invalido. -- Turma.setPrazoDeMatricula.");
			return;
		}
		data.set(prazoDeMatricula.get(2).intValue(), prazoDeMatricula.get(1).intValue() - 1, prazoDeMatricula.get(0).intValue());
		this.setTemPrazo("sim");
		this.prazoDeMatricula = new Date(data.getTimeInMillis());
	}
	
	public boolean alunoMatriculado(Long idAluno) {
		Iterator<Aluno> it = alunos.iterator(); 
		while(it.hasNext()){
			Aluno a = it.next();
			if(a.getId() == idAluno)
				return true;
		}
		return false;
	}

	public List<ListaDeExercicios> getListaDeExercicios() {
		return listaDeExercicios;
	}

	public void setListaDeExercicios(List<ListaDeExercicios> listaDeExercicios) {
		this.listaDeExercicios = listaDeExercicios;
	}

	public void setPrazoDeMatricula(Date prazoDeMatricula) {
		this.prazoDeMatricula = prazoDeMatricula;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
	
}
