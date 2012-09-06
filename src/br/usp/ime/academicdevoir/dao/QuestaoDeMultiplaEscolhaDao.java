package br.usp.ime.academicdevoir.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import br.usp.ime.academicdevoir.entidade.QuestaoDeMultiplaEscolha;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class QuestaoDeMultiplaEscolhaDao {

	/**
	 * @uml.property  name="session"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="br.usp.ime.academicdevoir.entidade.QuestaoDeMultiplaEscolha"
	 */
	private final Session session;

	public QuestaoDeMultiplaEscolhaDao(Session session) {
		this.session = session;
	}

    @SuppressWarnings("unchecked")
	/**
	 * Devolve uma lista com todas as questões de múltipla escolha cadastradas
	 * no banco de dados.
	 * 
	 * @return List<QuestaoDeMultiplaEscolha>
	 */
	public List<QuestaoDeMultiplaEscolha> listaTudo() {
		List<QuestaoDeMultiplaEscolha> a = null;
		a = this.session.createQuery("FROM QuestaoDeMultiplaEscolha").list();
		return  a;
	}

	/**
	 * Cadastra a questão fornecida no banco de dados.
	 * 
	 * @param questao
	 */
	public void salva(QuestaoDeMultiplaEscolha questao) {
		Transaction tx = session.beginTransaction();
		session.save(questao);
		tx.commit();
	}

	/**
	 * Devolve uma questão de múltipla escolha com o id fornecido.
	 * 
	 * @param id
	 * @return QuestaoDeMultiplaEscolha
	 */
	public QuestaoDeMultiplaEscolha carrega(Long id) {
		return (QuestaoDeMultiplaEscolha) this.session.load(
				QuestaoDeMultiplaEscolha.class, id);
	}

	/**
	 * Atualiza a questão fornecida no banco de dados.
	 * 
	 * @param questao
	 */
	public void atualiza(QuestaoDeMultiplaEscolha questao) {
		Transaction tx = session.beginTransaction();
		this.session.update(questao);
		tx.commit();
	}

	/**
	 * Remove a questão fornecida do banco de dados.
	 * 
	 * @param questao
	 */
	public void remove(QuestaoDeMultiplaEscolha questao) {
		Transaction tx = session.beginTransaction();
		this.session.delete(questao);
		tx.commit();
	}
	
	/**
	 * Devolve uma QuestaoDeMultiplaEscolha com o id fornecido, se existir. Caso contrário, retorna null. 
	 * 
	 * @param id
	 * @return QuestaoDeMultiplaEscolha
	 */
	public QuestaoDeMultiplaEscolha buscaPorId(Long id) {
		return (QuestaoDeMultiplaEscolha) session
				.createCriteria(QuestaoDeMultiplaEscolha.class)
				.add(Restrictions.idEq(id))
				.uniqueResult();
	}

	public void recarrega(QuestaoDeMultiplaEscolha questao) {
		session.refresh(questao);
	}
}
