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

	/**
	 * user credential for the outlook login
	 */
	private String user;

	/**
	 * password credential for the outlook login
	 */
	private String password;

	/**
	 * emailFolder folder were the email are stored remotly on the web cloud
	 */
	private Folder emailFolder;

	/**
	 * timeFilter uses our enum to filter the email time
	 */
	private TimeFilter timeFilter;

	/**
	 * mLenght defines the number of emails that we will be retrieving
	 */
	private int mLenght = 20;

	/**
	 * emails List of email messages
	 */
	private List<Message> emails = new LinkedList<Message>();

	/**
	 * Temporary directory where the attaches in a message are downloaded to
	 */
	private final static String TEMP_DIRECTORY = "C:/temp";

	/**
	 * Constructor
	 */
	public EmailApp() {
		timeFilter = TimeFilter.ALL_TIME;
		mLenght = 21;
	}

	/**
	 * 
	 * @param filter gives a String that define the search of a word inside the
	 *               email messages
	 * @return returns a List of email Messages
	 */
	public List<Message> getTimeline(String filter) {
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
			Session emailSession = Session.getInstance(properties);
			// create the POP3 store object and connect with the pop server

			Store store = emailSession.getStore("pop3s");

			store.connect("pop-mail.outlook.com", user, password);

			/**
			 * create the folder object and open it
			 */
			emailFolder = store.getFolder("INBOX");
			emailFolder.open(Folder.READ_ONLY);

			/**
			 * create the messages array, retrieves the emails from the folder and places
			 * them on the messages` array
			 */
			Message[] messages = emailFolder.getMessages();

			if (!timeFilter.equals(TimeFilter.ALL_TIME)) {
				notAllTime(messages, filter);
			} else {
				for (int i = messages.length - 1; i != messages.length - mLenght; i--) { // s is 40 by Default user
																							// should be
					// able to aks for more. Then i +=
					// 40
					if (!filter.equals("")
							&& (messages[i].getSubject().contains(filter) || writePart(messages[i]).contains(filter)))
						emails.add(messages[i]);
					else
						emails.add(messages[i]);
				}
			}
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return emails;
	}

	/**
	 * 
	 * used as an auxiliar funcion to the main function getTimeLine()
	 * 
	 * @param messages Email message array
	 * @param filter   word filter in the messages
	 * @return a list of emails filtered by a timeFilter
	 * @throws Exception
	 */
	private void notAllTime(Message[] messages, String filter) throws Exception {
		for (int i = 0; i != messages.length - 1; i--) {
			long date = ((timeFilter.getDate() / (24 * 60 * 60 * 1000))
					- (messages[i].getSentDate().getTime() / (24 * 60 * 60 * 1000)));
			if (date >= 0 && date <= timeFilter.getDif()) {
				if (!filter.equals(null)
						&& (messages[i].getSubject().contains(filter) || writePart(messages[i]).contains(filter))) {
					emails.add(messages[i]);
				} else {
					emails.add(messages[i]);
				}
			}
		}
	}

	/**
	 * Sends and email to a specific destination with a specific text
	 * 
	 * @param to   Address which the email will be sent to
	 * @param text Content of the email
	 */
	public boolean sendEmail(String to, String text) {
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
			return true;

		} catch (MessagingException e) {
			return false;
		}

	}

	/**
	 * Sends and email like sendEmail function but this time allows to send
	 * attachments
	 * 
	 * @param to   Address which the email will be sent to
	 * @param text Content of the email
	 */
	public void sendEmailWithAttachment(String to, String text) {
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

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * This method checks for content-type based on which, it processes and fetches
	 * the content of the message
	 */
	public String writePart(Part message) throws Exception {
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

	/**
	 * 
	 * @param m email Message
	 * @return
	 * @throws Exception
	 */
	public String[] writeEnvelope(Message m) throws Exception {
		String[] email_envelope = new String[4];
		Address[] a;
		String aux = "";
		// FROM: EMAIL 0, NAME 1
		if ((a = m.getFrom()) != null) {
			for (int j = 0; j < a.length; j++)
				aux += a[j].toString();
		}
		email_envelope[0] = getOnlyEmail(aux);
		email_envelope[1] = getOnlyName(aux);
		aux = "";
		// TO 2
		if ((a = m.getRecipients(Message.RecipientType.TO)) != null) {
			for (int j = 0; j < a.length; j++)
				aux += a[j].toString();
		}
		email_envelope[2] = aux;
		aux = "";
		// SUBJECT 3
		if (m.getSubject() != null) {
			aux += m.getSubject();
		}
		email_envelope[3] = aux;
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
	 * Returns the password of the user that is being displayed.
	 * 
	 * @return password (String) - (Getter) gets the user attribute.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Returns the folder that is opened.
	 * 
	 * @return emailFolder
	 */
	public Folder getEmailFolder() {
		return emailFolder;
	}

	/**
	 * 
	 * @param user
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * 
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Change time filter
	 * 
	 * @param timeFilter time filter
	 */
	public void setTimeFilter(TimeFilter timeFilter) {
		this.timeFilter = timeFilter;
	}

	/**
	 * timefilter getter
	 * 
	 * @return timefilter
	 */
	public TimeFilter getTimeFilter() {
		return timeFilter;
	}

	/**
	 * 
	 * Return only the email in the form email@example.com
	 * 
	 * @param string
	 * @return
	 */
	private String getOnlyEmail(String string) {
		if (string.contains("<") && string.contains(">")) {
			String[] splitted1 = string.split("<");
			String[] splitted2 = splitted1[1].split(">");
			return splitted2[0];
		} else {
			return string;
		}
	}

	/**
	 * Return only the name of a email owner
	 * 
	 * @param string
	 * @return
	 */
	private String getOnlyName(String string) {
		if (string.contains("<") && string.contains(">")) {
			String[] splitted1 = string.split("<");
			return splitted1[0];
		} else {
			return "";
		}
	}

	/**
	 * Increment mLenght
	 */
	public void moreMails() {
		this.mLenght += 20;
	}

}
