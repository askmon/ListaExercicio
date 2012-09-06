package br.usp.ime.academicdevoir.mail;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;

@Component
@ApplicationScoped
public class DefaultMailer implements Mailer {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultMailer.class);
	private static final String EMAIL_LOG_TEMPLATE = "Sending message \"%s\" from %s to %s using server %s:%s (using TLS: %b)";


	public void send(Email email) throws EmailException{
		if (email.getFromAddress() == null) {
			email.setFrom("academic.devoir@gmail.com");
		}
		email.setHostName("smtp.gmail.com");
		email.setCharset("ISO-8859-1");
		email.setSmtpPort(587);
		email.setTLS(true);
		email.setAuthenticator(new DefaultAuthenticator("academic.devoir@gmail.com", "i8m7e3u7s2p0"));
		wrapUpAndSend(email);
	}

	protected void wrapUpAndSend(Email email) throws EmailException {
		LOGGER.debug(String.format(emailLogTemplate(),
				email.getSubject(),
				email.getFromAddress(),
				email.getToAddresses(),
				email.getHostName(),
				email.getSmtpPort(),
				email.isTLS()));
		email.send();
	}

	protected String emailLogTemplate() {
		return EMAIL_LOG_TEMPLATE;
	}

}
