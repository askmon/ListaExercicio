package br.usp.ime.academicdevoir.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import br.com.caelum.vraptor.ioc.Component;
import br.usp.ime.academicdevoir.entidade.Disciplina;
import br.usp.ime.academicdevoir.entidade.Turma;

@Component
public class TurmaDao {

	/**
	 * @uml.property  name="session"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private final Session session;

	public TurmaDao(Session session) {
		this.session = session;
	}

	/**
	 * Cadastra a turma no banco de dados.
	 * 
	 * @param turma
	 */
	public void salvaTurma(Turma turma) {
		Transaction tx = session.beginTransaction();
		session.save(turma);
		tx.commit();
		
	}
	
	/**
	 * Atualiza a turma fornecida no banco de dados.
	 *  
	 * @param turma
	 */
	public void atualizaTurma(Turma turma) {
		Transaction tx = session.beginTransaction();
		session.merge(turma);
		tx.commit();
	}

	/**
	 * Remove a turma fornecida do banco de dados.
	 * 
	 * @param turma
	 */
	public void remove(Turma turma) {
		turma.setStatus(false);
		atualizaTurma(turma);
	}

	/**
	 * Devolve uma turma com o id fornecido.
	 * 
	 * @param id
	 * @return Turma
	 */
	public Turma carrega(Long id) {
		return (Turma) session.createCriteria(Turma.class).add(Restrictions.eq("id", id))
				                                          .add(Restrictions.eq("status", true)).uniqueResult();
	}
	
    @SuppressWarnings("unchecked")
	public List<Turma> listaTudo() {
    	return session.createQuery("From Turma turma Where turma.status = true").list();
		
	}
    
    @SuppressWarnings("unchecked")
	public List<Turma> listaTurmasNoPrazo(){
    	return session.createQuery("From Turma turma Where turma.prazoDeMatricula = null or turma.prazoDeMatricula >= :prazo and turma.status = true")
    			.setParameter("prazo", new Date())
    			.list();
    }

    @SuppressWarnings("unchecked")
	public List<Turma> listaPorProfessor(Long idProfessor){
    	return session.createQuery("From Turma turma Where turma.professor.id = :idProfessor and turma.status = true")
    			.setParameter("idProfessor", idProfessor)
    			.list();
    }

    public List<Turma> listaTurmasFiltradas(Long idAluno) {
    	List<Turma> listaTudo = listaTurmasNoPrazo();
    	List<Turma> listaFinal = new ArrayList<Turma>();
    	
    	for(int i = 0; i < listaTudo.size(); i++){
    		if(!listaTudo.get(i).alunoMatriculado(idAluno))
    			listaFinal.add(listaTudo.get(i));
    	}
    	
    	return listaFinal;
    }

	public void recarrega(Turma turma) {
		session.refresh(turma);
	}

	@SuppressWarnings("unchecked")
	public List<Turma> buscarPor(Disciplina disciplina) {
		return session.createQuery("From Turma turma Where turma.disciplina.id = :disciplina_id and turma.status = true")
						.setParameter("disciplina_id", disciplina.getId())
						.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Turma> buscarPor(Long idProfessor) {
		return session.createQuery("From Turma turma Where turma.professor.id = :professor_id and turma.status = true")
						.setParameter("professor_id", idProfessor)
						.list();
	}

	@SuppressWarnings("unchecked")
	public List<Turma> buscarPorAluno(Long idAluno) {
		return session.createQuery("From Turma turma Where turma.professor.id = :professor_id and turma.status = true")
				.setParameter("professor_id", idAluno)
				.list();
	}

}
