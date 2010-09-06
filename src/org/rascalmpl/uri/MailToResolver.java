package org.rascalmpl.uri;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.rascalmpl.interpreter.asserts.ImplementationError;

public class MailToResolver implements IURIOutputStreamResolver {

	public String scheme() {
		return "mailto";
	}

	public boolean exists(URI uri) {
		return false;
	}

	public OutputStream getOutputStream(URI uri, boolean append) throws IOException {
		String server = System.getProperty("rascal.smtp.server");
//		if (server == null) {
//			throw new IOException("SMTP server is not configured: rascal.smtp.server");
//		}
		String from = System.getProperty("rascal.smtp.from");
//		if (from == null) {
//			throw new IOException("Email send is not configured: rascal.smtp.from");
//		}
		String password = System.getProperty("rascal.smtp.from");

		return new BufferedOutputStream(new MailToStream(server, from, uri, password));
	}

	private class MailToStream extends OutputStream {
		private final URI uri;
		private final String server;
		private final String from;
		private final ByteArrayOutputStream buffer = new ByteArrayOutputStream(5000);
		private final String to;
		private final String subject;
		private final String password;

		public MailToStream(String server, String from, URI uri, String password) {
			super();
			this.uri = uri;
			this.server = "smtp.cwi.nl"; // server;
			this.from = "jurgen.vinju@cwi.nl"; // from;
			this.password = password;
			this.to = "jurgen@vinju.org";
			this.subject = "test";
		}

		@Override
		public void write(int b) throws IOException {
			buffer.write(b);
		}
		
		@Override
		public void write(byte[] b) throws IOException {
			buffer.write(b);
		}
		
		@Override
		public void write(byte[] b, int off, int len) throws IOException {
			buffer.write(b, off, len);
		}

		@Override
		public void close() throws IOException {
//			try {
				throw new ImplementationError("not yet implemented");
//				sendMail(server, from, to, subject, buffer.toString("8859_1"));
//			} catch (MessagingException e) {
//				throw new IOException(e.getMessage(), e);
//			}
//			finally {
//				buffer.close();
//			}
		}
		
		public void sendMail(String mailServer, String from, String recipient, String subject, String body) throws UnknownHostException, IOException, MessagingException {
			Properties props = new Properties();
			props.setProperty("mail.transport.protocol", "smtp");
			props.setProperty("mail.host", mailServer);
			props.setProperty("mail.user", from);
			props.setProperty("mail.password", "");

			Session mailSession = Session.getDefaultInstance(props, null);
			Transport transport = mailSession.getTransport();

			MimeMessage message = new MimeMessage(mailSession);
			message.setSubject(subject);
			message.setContent(body, "text/plain");
			message.addFrom(new Address[] {new InternetAddress(from)});
			message.addRecipient(Message.RecipientType.TO,
					new InternetAddress(recipient));

			transport.connect();
			transport.sendMessage(message,
					message.getRecipients(Message.RecipientType.TO));
			transport.close();
		}
	}

	public boolean mkDirectory(URI uri) {
		return false;
	}
}


