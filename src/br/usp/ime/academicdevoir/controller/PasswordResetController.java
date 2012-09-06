package br.usp.ime.academicdevoir.controller;

import static br.usp.ime.academicdevoir.util.Days.daysBetween;
import static org.hamcrest.Matchers.is;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.validator.ValidationMessage;
import br.com.caelum.vraptor.validator.Validations;
import br.com.caelum.vraptor.view.Results;
import br.usp.ime.academicdevoir.dao.UsuarioDao;
import br.usp.ime.academicdevoir.entidade.Usuario;
import br.usp.ime.academicdevoir.infra.Public;
import br.usp.ime.academicdevoir.mail.EmailThreadPoolExecutor;
import br.usp.ime.academicdevoir.mail.Mailer;


@Resource @Public
public class PasswordResetController {
	
	private UsuarioDao usuarioDao;
	private final Mailer mailer;
	private final Result result;
	private final Validator validator;
	private EmailThreadPoolExecutor emailThreadPoolExecutor;
	private final HttpServletRequest request;
	
	public PasswordResetController(UsuarioDao usuarioDao, Result result, Validator validator, Mailer mailer, 
			EmailThreadPoolExecutor emailThreadPoolExecutor, HttpServletRequest request) {
		this.result = result;
		this.validator = validator;
		this.mailer = mailer;
		this.emailThreadPoolExecutor = emailThreadPoolExecutor;
		this.usuarioDao = usuarioDao;
		this.request = request;
	}

	@Get("/nova/senha")
	public void passwordReset() {
	}

	@Post("/passwords")
	public void passwordResetEmail(String email) {
		Usuario usuario = usuarioDao.buscarPor(email);
		if (exist(usuario)) {
			enviarEmailAndAtualizarUsuario(usuario);
			criarValidacao("Em instantes você receberá no seu email instruções de como alterar a sua senha.", "login.fail");
		} else {
			criarValidacao("Não foi possível encontrar a sua conta. Por favor, verifique seu e-mail.", "login.fail");
		}
	}

	private void criarValidacao(String mensagem, String categoria) {
		validator.add(new ValidationMessage(mensagem, categoria));
		validator.onErrorUse(Results.logic()).redirectTo(LoginController.class).login();
	}

	private void enviarEmailAndAtualizarUsuario(final Usuario usuario) {
			usuario.hashToken().withStatusToken(true).updateAt();
			sendEmailThreadPoolExecutor(usuario);
			usuarioDao.atualiza(usuario);
	}

	private void sendEmailThreadPoolExecutor(final Usuario user) {
		emailThreadPoolExecutor.runTask(new Runnable() {
		  public void run() {
				try {
					mailer.send(emailTo(user));
				} catch (EmailException e) {
					criarValidacao("Problemas ao enviar e-mail.", "email.problem");
				}
		  }
		 });
		emailThreadPoolExecutor.shutDown();
	}

	private Email emailTo(Usuario user) throws EmailException {
		Email emailParaResetarSenha = new SimpleEmail();
		emailParaResetarSenha.setSubject("Você solicitou uma nova senha");
		emailParaResetarSenha.setMsg(mensagem(user));
		emailParaResetarSenha.addTo(user.getEmail());
		return emailParaResetarSenha;
	}

	private String mensagem(Usuario userParaReceberEmail) {
		StringBuilder mensagemEmail = new StringBuilder();
		String contexto = request.getRequestURL().toString();
		mensagemEmail.append("Olá " + userParaReceberEmail.getNome() + "\n\n")
								.append("Recebemos um pedido de redefinição de senha para sua conta. \n\n")
								.append("Passo 1 - Vá para " + contexto+ "/" + userParaReceberEmail.getToken() + " dentro de 48 horas \n")
								.append("Passo 2 - Escolha uma nova senha\n\n")
								.append("Se * você * não solicitou a redefinição de senha, simplesmente desconsiderar\n")
								.append("este e-mail, e vamos deixar tudo como está!\n\n")
								.append("Os melhores cumprimentos,\n")
								.append("+++++.");
		return mensagemEmail.toString();
	}

	private boolean exist(Usuario usuario) {
		return usuario != null ;
	}

	@Get("/passwords/{token}")
	public void passwordResetToken(String token) {
		Usuario usuario = usuarioDao.buscarPorToken(token); 
		if (exist(usuario)) {
			if (tokenNaoExpirou(usuario)) {
				result.include("token", token);
			} 
			else {
				criarValidacao("O seu pedido de senha expirou.", "login.fail");
			} 
		} else {
			result.redirectTo(LoginController.class).login();
		}
	}

	private boolean tokenNaoExpirou(Usuario usuario) {
		long dias = daysBetween(usuario.getUpdateAt());
		if (dias <= 2 && usuario.isStatusToken()) return true;
		return false;
	}

	@Post("/passwords/{token}")
	public void passwordReset(final String token, final String senha, final String confirmation) {
		Usuario userParaAlterarSenha = usuarioDao.buscarPorToken(token);
		if (exist(userParaAlterarSenha)) {
			if (tokenNaoExpirou(userParaAlterarSenha)) {
				validate(token, senha, confirmation);
				userParaAlterarSenha.withStatusToken(false).withSenha(senha).hashPassword();
				usuarioDao.atualiza(userParaAlterarSenha);
				criarValidacao("Sua senha foi redefinida. Faça o login com a nova senha", "reset");
			} else {
				criarValidacao("O seu pedido de senha expirou.", "login.fail");
			}
		} else {
			criarValidacao("Não foi possivel redefinir sua senha.", "reset");
		}

	}

	private void validate(final String token, final String senha, final String confirmation) {
		validator.checking(new Validations() {{
			if (that(senha != null && confirmation != null && !senha.isEmpty() && !confirmation.isEmpty(), "", "senha.confirmacao")) {
				that(senha, is(confirmation), "", "user.confirmation");
				that(!senha.contains(" "), "", "password.white");
			}
		}});
		validator.onErrorRedirectTo(this).passwordResetToken(token);
	}

}
