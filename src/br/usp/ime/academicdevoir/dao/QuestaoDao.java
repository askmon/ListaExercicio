package br.usp.ime.academicdevoir.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import br.com.caelum.vraptor.ioc.Component;
import br.usp.ime.academicdevoir.entidade.Disciplina;
import br.usp.ime.academicdevoir.entidade.Questao;
import br.usp.ime.academicdevoir.entidade.Tag;

@Component
public class QuestaoDao {

	/**
	 * @uml.property name="session"
	 * @uml.associationEnd multiplicity="(0 -1)"
	 *                     elementType="br.usp.ime.academicdevoir.entidade.Questao"
	 */
	private final Session session;
	private ListaDeExerciciosDao listaDeExercicioDao;
	

	public QuestaoDao(Session session, ListaDeExerciciosDao listaDeExerciciosDao) {
		this.session = session;
		this.listaDeExercicioDao = listaDeExerciciosDao;
	}

	@SuppressWarnings("unchecked")
	/**
	 * Devolve uma lista com todas as questões cadastradas no banco de dados.
	 * 
	 * @return List<Questao>
	 */
	public List<Questao> listaTudo() {
		return this.session.createQuery("from Questao").list();
	}
	
	/**
	 * Devolve uma lista com todas as questões cadastradas de acordo com a página.
	 * 
	 * @return List<Questao>
	 */
	private Criteria listaFiltrada(String filtro, Long idLista) {
		Criteria criteria = this.session.createCriteria(Questao.class);
		/**
		 * filtra por disciplina
		*/
		
		Disciplina disciplina = listaDeExercicioDao.carrega(idLista).getTurma(0).getDisciplina();
		criteria.createCriteria("disciplina").add(Restrictions.eq("id", disciplina.getId()));
		
		if (filtro != null && !filtro.equals(""))
			criteria.createCriteria("tags").add(Restrictions.ilike("nome", filtro, MatchMode.ANYWHERE));
		return criteria;
	}

	/**
	 * Devolve o tamanho da lista com todas as questões cadastradas no banco de
	 * dados.
	 * 
	 * @return List<Questao>
	 */
	public Integer tamanhoDaLista(String filtro, Long idLista) {
		return listaFiltrada(filtro, idLista).list().size();
	}

	@SuppressWarnings("unchecked")
	/**
	 * Devolve uma lista com todas as questões cadastradas de acordo com a página.
	 * 
	 * @return List<Questao>
	 */
	public List<Questao> listaPaginada(Integer primeiro, Integer numRegistros,
			String filtro, Long idLista) {
		Criteria criteria = listaFiltrada(filtro, idLista);
		criteria.setFirstResult(primeiro);
		criteria.setMaxResults(numRegistros);
		return criteria.list();
	}

	/**
	 * Devolve uma questão com o id fornecido.
	 * 
	 * @param id
	 * @return Questao
	 */
	public Questao carrega(Long id) {
		return (Questao) this.session.get(Questao.class, id);
	}

	/**
	 * Remove a questão fornecida do banco de dados.
	 * 
	 * @param questao
	 */
	public void remove(Questao questao) {
		Transaction tx = session.beginTransaction();
		this.session.delete(questao);
		tx.commit();
	}

	/**
	 * Devolve uma Questao com o id fornecido, se existir. Caso contrário,
	 * retorna null.
	 * 
	 * @param id
	 * @return Questao
	 */
	public Questao busca(Long id) {
		return (Questao) session.createCriteria(Questao.class)
				.add(Restrictions.idEq(id)).uniqueResult();
	}

	public void recarrega(Questao questao) {
		session.refresh(questao);
	}

	public List<Questao> listaFiltradas() {
		return listaTudo();
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Questao> buscarQuestoesPor(Tag tag, int quantidade) {
		return session.createQuery("select distinct q From Questao q, Tag tag Where tag = :tag and q in elements(tag.questoes) ORDER BY RAND()")
				.setParameter("tag", tag)
				.setMaxResults(quantidade)
				.list();
//		return session.createSQLQuery("select distinct t.id_questao From tags_questoes t Where t.id_tag = :id_tag ORDER BY RAND()")
//				.setParameter("id_tag", tag.getId())
//				.setMaxResults(quantidade)
//				.list();
	}
	
	
	
}
