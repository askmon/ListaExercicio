package br.usp.ime.academicdevoir.controller;

import javax.servlet.http.HttpSession;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.usp.ime.academicdevoir.dao.UsuarioDao;
import br.usp.ime.academicdevoir.entidade.Usuario;
import br.usp.ime.academicdevoir.infra.Privilegio;
import br.usp.ime.academicdevoir.infra.Public;
import br.usp.ime.academicdevoir.infra.UsuarioSession;

@Public
@Resource
/**
 * Controlador de login.
 */
public class LoginController {

	private final Result result;

	private UsuarioSession usuarioSession;

	private UsuarioDao usuarioDao;

	private final HttpSession session;
	
	public LoginController(Result result, UsuarioDao usuarioDao,
			UsuarioSession alunodao, HttpSession session) {
		this.result = result;
		this.usuarioDao = usuarioDao;
		this.usuarioSession = alunodao;
		this.session = session;
	}

	@Path("/")
	public void index() {
		result.redirectTo(LoginController.class).login();
	}

	@Get
	@Path("/login")
	public void login() {
	}

	@Post("/autenticar")
	public void login(Usuario usuario) {
		Usuario user = usuarioDao.fazLogin(usuario.getLogin(),
				usuario.getSenha());

		if (user != null) {
			usuarioSession.setUsuario(user);
			Privilegio pr = user.getPrivilegio();
			if (pr.equals(Privilegio.PROFESSOR)) {
				result.redirectTo(ProfessoresController.class).home();
			} else if (pr.equals(Privilegio.ADMINISTRADOR)) {
				result.redirectTo(AdministradorController.class).home();
			} else {
				result.redirectTo(AlunosController.class).home();
			}
		} else {
			result.include("error", "Login ou senha incorreta!")
					.redirectTo(this).login();
		}

	}

	@Get("/logout")
	public void logout() {
		session.invalidate();
		usuarioSession.logout();
		result.redirectTo(this).login();
	}

	public void acessoNegado() {

	}

}
