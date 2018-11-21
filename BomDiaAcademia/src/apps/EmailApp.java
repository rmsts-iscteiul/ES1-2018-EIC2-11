package apps;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class EmailApp {
	
	protected String user;
	protected String password;
	protected Folder emailFolder;

	protected List<Message> getTimeline() {
		List<Message> emails = new LinkedList<Message>();
		try {


			/**
			 * 
			 * Creating properties field to connect to outlook pop server
			 * 
			 */
			Properties properties = new Properties();

			properties.put("mail.pop3.host", "pop-mail.outlook.com");
			properties.put("mail.pop3.port", "995");
			properties.put("mail.pop3.starttls.enable", "true");
			Session emailSession = Session.getDefaultInstance(properties);
			// create the POP3 store object and connect with the pop server
			Store store = emailSession.getStore("pop3s");

			store.connect("pop-mail.outlook.com", user, password);

			/**
			 * create the folder object and open it
			 */
			emailFolder = store.getFolder("INBOX");
			emailFolder.open(Folder.READ_ONLY);

			/**
			 * create the messages array, retrieves the emails from the folder and places them on the messages` array
			 */
			Message[] messages = emailFolder.getMessages();
			System.out.println("messages.length---" + messages.length);
			for (int i = messages.length-1; i != messages.length-21; i--) {
				Message message = messages[i];
				emails.add(message);
			}

			// close the store and folder objects
			// emailFolder.close(false); //keeping it open
			// store.close();

		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return emails;
	}

	protected void sendEmail(String to, String text) {
		String host = "smtp-mail.outlook.com";

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", "587");

		/**
		 * This session object requires another connection because we are connection to another server who forwards the emails
		 */
		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(user, password);
			}
		});
		try {
			// Create a default MimeMessage object.
			Message message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(user));

			// Set To: header field of the header.
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));

			// Set Subject: header field
			message.setSubject("Testing Subject");

			// Now set the actual message
			message.setText(text);

			// Send message
			Transport.send(message);

			System.out.println("Sent message successfully....");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}

	}

	protected void sendEmailWithAttachment(String to, String text) {
		String host = "smtp-mail.outlook.com";

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", "587");

		// Get the Session object.
		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(user, password);
			}
		});
		try {
			// Create a default MimeMessage object.
			Message message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(user));

			// Set To: header field of the header.
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));

			// Set Subject: header field
			message.setSubject("Testing Subject");

			// Create the message part
			BodyPart messageBodyPart = new MimeBodyPart();

			// Now set the actual message
			messageBodyPart.setText(text);

			// Create a multipar message
			Multipart multipart = new MimeMultipart();

			// Set text message part
			multipart.addBodyPart(messageBodyPart);

			// Part two is attachment
			messageBodyPart = new MimeBodyPart();
			String filename = "C:\\Users\\matia\\Desktop/redes.txt"; /** Change to the desired a file directory */
			DataSource source = new FileDataSource(filename);
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName("redes.txt"); /** Change to the desired file name */
			multipart.addBodyPart(messageBodyPart);

			// Send the complete message parts
			message.setContent(multipart);

			// Send message
			Transport.send(message);

			System.out.println("Sent message successfully....");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * This method checks for content-type based on which, it processes and fetches
	 * the content of the message
	 */
	protected String writePart(Part p) throws Exception {
		String text = "";
		if (p instanceof Message) {
			text = writeEnvelope((Message) p);
		}

		/** Check if the content is plain text */
		if (p.isMimeType("text/plain")) {
			System.out.println("This is plain text");
			System.out.println("---------------------------");
			System.out.println((String) p.getContent());
			text += (String) p.getContent();
		}
		/** Check if the content has attachment */
		else if (p.isMimeType("multipart/*")) {
//			System.out.println("This is a Multipart");
//			System.out.println("---------------------------");
//			Multipart mp = (Multipart) p.getContent();
//			int count = mp.getCount();
//			for (int i = 0; i < count; i++)
//				writePart(mp.getBodyPart(i));
		}
		/** Check if the content is a nested message */
		else if (p.isMimeType("message/rfc822")) {
//			System.out.println("This is a Nested Message");
//			System.out.println("---------------------------");
			writePart((Part) p.getContent());
		}
		/** Check if the content is an inline image */
		else if (p.isMimeType("image/jpeg")) {
			// TO-DO
		} else if (p.getContentType().contains("image/")) {
			// TO-DO
		} else {
			Object o = p.getContent();
			if (o instanceof String) {
//				System.out.println("This is a string");
//				System.out.println("---------------------------");
				text += (String) o;
			} else if (o instanceof InputStream) {
				System.out.println("This is just an input stream");
				System.out.println("---------------------------");
				InputStream is = (InputStream) o;
				is = (InputStream) o;
				int c;
				while ((c = is.read()) != -1)
					text += c;
			} else {
//				System.out.println("This is an unknown type");
//				System.out.println("---------------------------");
				text += o;
			}
		}
		return text;
	}

	/**
	 * This method would print FROM,TO and SUBJECT of the message
	 */
	protected String writeEnvelope(Message m) throws Exception {
		String email_envelope = "";
		Address[] a;
		// FROM
		if ((a = m.getFrom()) != null) {
			email_envelope += "FROM: ";
			for (int j = 0; j < a.length; j++)
				email_envelope += a[j].toString();
		}
		// TO
		if ((a = m.getRecipients(Message.RecipientType.TO)) != null) {
			email_envelope += "\nTO: ";
			for (int j = 0; j < a.length; j++)
				email_envelope += a[j].toString();
		}
		// SUBJECT
		if (m.getSubject() != null) {
			email_envelope += "\nSUBJECT: " + m.getSubject();
		}
		return email_envelope;

	}

	/**
	 * Returns the user that is being displayed.
	 * 
	 * @return user(String) - (Getter) gets the user attribute.
	 */
	public String getUser() {
		return user;
	}

	/**
	 * Returns the folder that is opened.
	 * 
	 * @return emailFolder
	 */
	public Folder getEmailFolder() {
		return emailFolder;
	}
	
	public void setUser(String user) {
		this.user = user;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
//	public static void main(String[] args) {
//		EmailApp email = new EmailApp();
//		email.sendEmailWithAttachment("matiasfrazaocorreia@gmail.com", "test");
//	}
}
