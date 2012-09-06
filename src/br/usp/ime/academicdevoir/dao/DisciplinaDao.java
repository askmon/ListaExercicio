package br.usp.ime.academicdevoir.dao;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import br.com.caelum.vraptor.ioc.Component;
import br.usp.ime.academicdevoir.entidade.Disciplina;
import br.usp.ime.academicdevoir.entidade.Turma;

@Component
public class DisciplinaDao {

	private final Session session;
	
	private final TurmaDao turmaDao;
	
	public DisciplinaDao(Session session, TurmaDao turmaDao) {
		this.session = session;
		this.turmaDao = turmaDao;
	}

	/**
	 * Cadastra a disciplina fornecida no banco de dados.
	 * 
	 * @param disciplina
	 */
	@SuppressWarnings("unchecked")
	public void salvaDisciplina(Disciplina disciplina) {
		String nome = disciplina.getNome();
	    List<Disciplina> listaDeDisciplinas = session.createCriteria(Disciplina.class)
                .add(Restrictions.eq("nome", nome))
                .list();
        
	    if (listaDeDisciplinas.size() != 0) return;
	    
		Transaction tx = session.beginTransaction();
		session.save(disciplina);
		tx.commit();
	}

	/**
	 * Atualiza a disciplina fornecida no banco de dados. 
	 * 
	 * @param disciplina
	 */
	@SuppressWarnings("unchecked")
	public void atualiza(Disciplina disciplina) {
		String nome = disciplina.getNome();
	    List<Disciplina> listaDeDisciplinas = session.createCriteria(Disciplina.class)
                .add(Restrictions.like("nome", nome, MatchMode.EXACT))
                .list();
        
	    if (listaDeDisciplinas.size() != 0 && listaDeDisciplinas.get(0).getId() != disciplina.getId()) return;
	    
		Transaction tx = session.beginTransaction();
		session.update(disciplina);
		tx.commit();
	}
	
	public void remove(Disciplina disciplina) {
		disciplina.setStatus(false);
		atualiza(disciplina);
		for (Turma turma : disciplina.getTurmas()) {
			turma.setStatus(false);
			turmaDao.atualizaTurma(turma);
		}
	}
	
	public Disciplina carrega(Long id) {
		return (Disciplina) session.createCriteria(Disciplina.class).add(Restrictions.eq("id", id))
																	.add(Restrictions.eq("status", true))
																	.uniqueResult();
	}
	
    @SuppressWarnings("unchecked")
    /**
	 * Devolve uma lista com todas as disciplinas cadastradas no banco de dados.
	 * 
	 * @return List<Disciplina>
	 */
   /* public List<Disciplina> listaTudo() {
		String nome = "SELECT p FROM Disciplina p";
		Query query = session.createQuery(nome);
		List<Disciplina> listaDeDisciplinas = query.list();
		return listaDeDisciplinas;
	}*/
    
    public List<Disciplina> listaTudo() {
		return this.session.createCriteria(Disciplina.class).add(Restrictions.eq("status", true)).list();
	}

}
