package apps;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.Transport;

public class EmailApp {

	private String filterS;
	private String user;
	private String password;

	private void check(String user, String password) {
		this.user = user;
		this.password = password;
		try {

			// create properties field
			Properties properties = new Properties();

			properties.put("mail.pop3.host", "pop-mail.outlook.com");
			properties.put("mail.pop3.port", "995");
			properties.put("mail.pop3.starttls.enable", "true");
			Session emailSession = Session.getDefaultInstance(properties);

			// create the POP3 store object and connect with the pop server
			Store store = emailSession.getStore("pop3s");

			store.connect("pop-mail.outlook.com", user, password);

			// create the folder object and open it
			Folder emailFolder = store.getFolder("INBOX");
			emailFolder.open(Folder.READ_ONLY);

			// retrieve the messages from the folder in an array and print it
			Message[] messages = emailFolder.getMessages();
			System.out.println("messages.length---" + messages.length);
			if (this.filterS != null) {

				for (int i = 0, n = messages.length; i < n; i++) {
					Message message = messages[i];
					String aux = "" + message.getContent();
					if (aux.toLowerCase().indexOf(filterS.toLowerCase()) != -1) {
						System.out.println("---------------------------------");
						System.out.println("Email Number " + (i + 1));
						System.out.println("Subject: " + message.getSubject());
						System.out.println("From: " + message.getFrom()[0]);
						// System.out.println("Text: " + message.getContent().toString());
					} else {
						System.out.println("No results match the searching criteria");
					}
				}
			} else {

				for (int i = messages.length - 1, n = messages.length - 21; i > n; i--) {
					Message message = messages[i];
					System.out.println("---------------------------------");
					System.out.println("Email Number " + (i + 1));
					System.out.println("Subject: " + message.getSubject());
					System.out.println("From: " + message.getFrom()[0]);
					// System.out.println("Text: " + message.getContent().toString());

				}
			}

			// close the store and folder objects
			emailFolder.close(false);
			store.close();

		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void sendEmail(String to, String text) {
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

			// Now set the actual message
			message.setText(text);

			// Send message
			Transport.send(message);

			System.out.println("Sent message successfully....");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}

	}
	
	private void sendEmailWithAttachment(String to, String text) {
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
	         message.setRecipients(Message.RecipientType.TO,
	            InternetAddress.parse(to));

	         // Set Subject: header field
	         message.setSubject("Testing Subject");

	         // Create the message part
	         BodyPart messageBodyPart = new MimeBodyPart();

	         // Now set the actual message
	         messageBodyPart.setText("This is message body");

	         // Create a multipar message
	         Multipart multipart = new MimeMultipart();

	         // Set text message part
	         multipart.addBodyPart(messageBodyPart);

	         // Part two is attachment
	         messageBodyPart = new MimeBodyPart();
	         String filename = "/home/manisha/file.txt";
	         DataSource source = new FileDataSource(filename);
	         messageBodyPart.setDataHandler(new DataHandler(source));
	         messageBodyPart.setFileName(filename);
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

	public void filter(String filterS) {
		this.filterS = filterS;
	}

//	public static void main(String[] args) {
//		EmailApp email = new EmailApp();
//
//		String username = "msjrr@iscte-iul.pt";// change accordingly
//		String password = "*******";// change accordingly
//		//email.check(username, password);
//		email.sendEmail("matiasfrazaocorreia@gmail.com", "Bom dia academia teste!");
//	
//	 }

}
