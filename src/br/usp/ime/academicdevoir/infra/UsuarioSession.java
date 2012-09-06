package br.usp.ime.academicdevoir.infra;

import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.SessionScoped;
import br.usp.ime.academicdevoir.entidade.Usuario;

@Component
@SessionScoped
public class UsuarioSession {

	private Usuario usuario;

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public boolean isLogged() {
		return usuario != null;
	}

	public void logout() {
		usuario = null;
	}
	
	public Long getId() {
		return usuario.getId();
	}

	public boolean isProfessor() {
		return usuario.getPrivilegio().equals(Privilegio.PROFESSOR);
	}
	
	public boolean isAluno() {
		return usuario.getPrivilegio().equals(Privilegio.ALUNO);
	}

}
