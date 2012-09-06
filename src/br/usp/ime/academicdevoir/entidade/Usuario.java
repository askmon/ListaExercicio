package br.usp.ime.academicdevoir.entidade;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import br.usp.ime.academicdevoir.infra.Criptografia;
import br.usp.ime.academicdevoir.infra.Privilegio;

@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
public class Usuario {
	
	/**
	 * @uml.property  name="id"
	 */
	@Id @GeneratedValue
	private Long id;
	/**
	 * @uml.property  name="nome"
	 */
	@Length(min = 5, max = 50)
	private String nome;
	/**
	 * @uml.property  name="login"
	 */
	@Length(min = 2, max = 30)
	@Column(unique=true)
	private String login;
	/**
	 * @uml.property  name="senha"
	 */
	@Length(min = 5, max = 32)
	private String senha;
	/**
	 * @uml.property  name="email"
	 */
	@Email
	private String email;
	/**
	 * @uml.property  name="privilegio"
	 * @uml.associationEnd  
	 */
	private Privilegio privilegio;


	private String token;

	private boolean statusToken = false;

	private Date updateAt;

	
	public Long getId() {
		return id;
	}
	/**
	 * @param id
	 * @uml.property  name="id"
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return
	 * @uml.property  name="nome"
	 */
	public String getNome() {
		return nome;
	}
	/**
	 * @param nome
	 * @uml.property  name="nome"
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}
	/**
	 * @return
	 * @uml.property  name="login"
	 */
	public String getLogin() {
		return login;
	}
	/**
	 * @param login
	 * @uml.property  name="login"
	 */
	public void setLogin(String login) {
		this.login = login;
	}
	/**
	 * @return
	 * @uml.property  name="senha"
	 */
	public String getSenha() {
		return senha;
	}
	/**
	 * @param senha
	 * @uml.property  name="senha"
	 */
	public void setSenha(String senha) {
		this.senha = senha;
	}
	/**
	 * @return
	 * @uml.property  name="email"
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email
	 * @uml.property  name="email"
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return
	 * @uml.property  name="privilegio"
	 */
	public Privilegio getPrivilegio() {
		return privilegio;
	}
	/**
	 * @param privilegio
	 * @uml.property  name="privilegio"
	 */
	public void setPrivilegio(Privilegio privilegio) {
		this.privilegio = privilegio;
	}
	
	public Usuario hashToken() {
		this.token = String.valueOf(getId()) + UUID.randomUUID().toString();
		return this;
	}
	
	public Usuario withSenha(String senha) {
		this.senha = senha;
		return this;
	}

	public Usuario withStatusToken(boolean statusToken) {
		this.statusToken = statusToken;
		return this;
	}
	
	public Usuario updateAt() {
		this.updateAt = new Date();
		return this;
	}
	
	public Usuario hashPassword() {
		this.senha = new Criptografia().geraMd5(senha); 
	    return this;
		}



	public String getToken() {
		return token;
	}
	
	public void setToken(String token) {
		this.token = token;
	}

	public boolean isStatusToken() {
		return statusToken;
	}

	public void setStatusToken(boolean statusToken) {
		this.statusToken = statusToken;
	}
	
	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}
	
}
