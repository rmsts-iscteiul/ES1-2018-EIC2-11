package apps;

import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;

public class EmailApp {

	public class test {
		private String filterS;

		public void check(String host, String port, String storeType, String user, String password) {
			try {

				// create properties field
				Properties properties = new Properties();

				properties.put("mail.pop3.host", host);
				properties.put("mail.pop3.port", port);
				properties.put("mail.pop3.starttls.enable", "true");
				Session emailSession = Session.getDefaultInstance(properties);

				// create the POP3 store object and connect with the pop server
				Store store = emailSession.getStore("pop3s");

				store.connect(host, user, password);

				// create the folder object and open it
				Folder emailFolder = store.getFolder("INBOX");
				emailFolder.open(Folder.READ_ONLY);

				// retrieve the messages from the folder in an array and print it
				Message[] messages = emailFolder.getMessages();
				System.out.println("messages.length---" + messages.length);
				if (this.filterS != null) {
					System.out.println("PIMBA");
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
					System.out.println("WOWOWOW");
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

		public void filter(String filterS) {
			this.filterS = filterS;
		}

//		public static void main(String[] args) {
//			test email = new test();
//			String host = "pop-mail.outlook.com";// change accordingly
//			String port = "995"; // change accordingly
//			String mailStoreType = "pop3";
//			String username = "msjrr@iscte-iul.pt";// change accordingly
//			String password = // change accordingly
//			email.filter("estudantes");
//			email.check(host, port, mailStoreType, username, password);
//
//		}

	}
}
