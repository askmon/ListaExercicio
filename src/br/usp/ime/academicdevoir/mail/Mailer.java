package br.usp.ime.academicdevoir.mail;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;

public interface Mailer {

	void send(Email email) throws EmailException;

}
