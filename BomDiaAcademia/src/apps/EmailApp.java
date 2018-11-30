package apps;

import java.io.File;
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

	private String user;
	public String password;

	private Folder emailFolder;

	private final static String TEMP_DIRECTORY = "C:/temp";

	public List<Message> getTimeline() {
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

			// create the folder object and open it
			emailFolder = store.getFolder("INBOX");
			emailFolder.open(Folder.READ_ONLY);

			/**
			 * create the messages array, retrieves the emails from the folder and places
			 * them on the messages` array
			 */
			Message[] messages = emailFolder.getMessages();
			System.out.println("messages.length---" + messages.length);
			for (int i = messages.length - 1; i != messages.length - 21; i--) {
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
		 * This session object requires another connection because we are connection to
		 * another server who forwards the emails
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

	/*
	 * This method checks for content-type based on which, it processes and fetches
	 * the content of the message
	 */
	public String writePart(Part message) throws Exception {
		System.out.println(message.getDescription());
		System.out.println(message.toString());
		String messageContent = "";
		String contentType = message.getContentType();

		// store attachment file name, separated by comma
		String attachFiles = "";

		if (contentType.contains("multipart")) {
			// content may contain attachments
			Multipart multiPart = (Multipart) message.getContent();
			int numberOfParts = multiPart.getCount();
			for (int partCount = 0; partCount < numberOfParts; partCount++) {
				MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
				if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
					// this part is attachment
					String fileName = part.getFileName();
					attachFiles += fileName + ", ";
					File dir = new File(TEMP_DIRECTORY + File.separator + message.toString());
					if (!dir.exists()) {
						dir.mkdirs();
					}
					part.saveFile(dir + File.separator + fileName);
				} else {
					// this part may be the message content
					messageContent = part.getContent().toString();
				}
			}

			if (attachFiles.length() > 1) {
				attachFiles = attachFiles.substring(0, attachFiles.length() - 2);
			}
		} else if (contentType.contains("text/plain") || contentType.contains("text/html")) {
			Object content = message.getContent();
			if (content != null) {
				messageContent = content.toString();
			}
		}
		return messageContent;
	}

	/*
	 * This method would print FROM,TO and SUBJECT of the message
	 */
	public String[] writeEnvelope(Message m) throws Exception {
		String[] email_envelope = new String[3];
		Address[] a;
		String aux = "";
		// FROM
		if ((a = m.getFrom()) != null) {
			for (int j = 0; j < a.length; j++)
				aux += a[j].toString();
		}
		email_envelope[0] = aux;
		aux = "";
		// TO
		if ((a = m.getRecipients(Message.RecipientType.TO)) != null) {
			for (int j = 0; j < a.length; j++)
				aux += a[j].toString();
		}
		email_envelope[1] = aux;
		aux = "";
		// SUBJECT
		if (m.getSubject() != null) {
			aux += m.getSubject();
		}
		email_envelope[2] = aux;
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

}
