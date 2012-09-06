package br.usp.ime.academicdevoir.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;

import br.com.caelum.vraptor.ioc.Component;
import br.usp.ime.academicdevoir.entidade.TagsDaLista;

@Component
public class TagsDaListaDao {

	
	private Session session;

	public TagsDaListaDao(Session session) {
		this.session = session;
	}
	
	public void salvar(TagsDaLista tagsDaLista) {
	    Transaction tx = session.beginTransaction();
		session.save(tagsDaLista);
		tx.commit();
	}

}
