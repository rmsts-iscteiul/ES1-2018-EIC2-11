package tests;

import java.util.List;
import java.util.Scanner;

import javax.mail.Message;
import javax.mail.MessagingException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import apps.EmailApp;

class TestEmailApp {

	private static EmailApp email;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		email = new EmailApp(); /** instantiation EmailApp */
		Scanner keybord = new Scanner(System.in);
		System.out.println("Insert your user name");
		email.setUser(keybord.nextLine());
		// keybord.close();
		// Scanner keybord2 = new Scanner(System.in);
		System.out.println("Insert your user password");
		email.setPassword(keybord.nextLine());
		keybord.close();
	}

	@BeforeEach
	void setUp() throws Exception {
		System.out.println("\nA test is about to begin: \n");
	}

	@Test
	void testGetTimeLine() {
		System.out.println("-----------------------------------------------------------------------");
		System.out.println("Size of the email list:" + email.getTimeline().size()); /** Returns a list of email */

	}

	@Test
	void testWritePart() throws MessagingException, Exception {
		List<Message> messages = email.getTimeline();
		Message aux = messages.get(0); /** Get a message of an email in the position 1 on the emails folder */
		System.out.println("-----------------------------------------------------------------------");
		System.out.println("Get one specific email text:" + email.writePart(aux)); /** Returns the text of a message */
		System.out.println("From + Subjects:"
				+ email.writeEnvelope(aux)); /** Returns the Subject and email who sent the message */
	}

	@Test
	void testSendEmail() {
		System.out.println("-----------------------------------------------------------------------");
		System.out.println("Send an email to matiasfrazaocorreia@gmail.com with the text as 'test':");
		email.sendEmail("ljcra@iscte-iul.pt", "test"); /** Sends the email */
		System.out
				.println("Send an email to matiasfrazaocorreia@gmail.com with the text as 'test' and a file attached:");
		email.sendEmailWithAttachment("matiasfrazaocorreia@gmail.com", "test"); /** Sends the email */
	}

	@AfterAll
	static void tearDownAfterClass() {
		System.out.println("\n\n\n************************ FINISHED ALL TESTS ********************************+\n\n\n");
	}
}
