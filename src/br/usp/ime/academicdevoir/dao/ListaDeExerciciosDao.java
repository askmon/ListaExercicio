package br.usp.ime.academicdevoir.dao;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import br.com.caelum.vraptor.ioc.Component;
import br.usp.ime.academicdevoir.entidade.ListaDeExercicios;
import br.usp.ime.academicdevoir.entidade.Turma;

@Component
public class ListaDeExerciciosDao {

	/**
	 * @uml.property name="session"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	private final Session session;

	public ListaDeExerciciosDao(Session session) {
		this.session = session;
	}

	@SuppressWarnings("unchecked")
	/**
	 * Devolve uma lista com todas as listas de exercícios cadastradas no banco
	 * de dados.
	 * 
	 * @return List<ListaDeExercicios>
	 */
	public List<ListaDeExercicios> listaTudo() {
		return this.session.createCriteria(ListaDeExercicios.class).list();
	}

	public void salva(ListaDeExercicios lista) {
		Transaction tx = session.beginTransaction();
		session.saveOrUpdate(lista);
		tx.commit();
	}

	public ListaDeExercicios carrega(Long id) {
		return (ListaDeExercicios) this.session.load(ListaDeExercicios.class,
				id);
	}

	public void atualiza(ListaDeExercicios lista) {
		Transaction tx = session.beginTransaction();
		this.session.update(lista);
		tx.commit();
	}

	public void remove(ListaDeExercicios lista) {
		try {
		Transaction tx = session.beginTransaction();
		this.session.delete(lista);
		tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void recarrega(ListaDeExercicios lista) {
		session.refresh(lista);
	}

	@SuppressWarnings("unchecked")
	public List<BigInteger> buscaListasQueContemQuestao(Long idDaQuestao) {
		return session.createSQLQuery(
				"select ListaDeExercicios_id from questoesDaLista where questao_id="
						+ idDaQuestao).list();
	}
}
