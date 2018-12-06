package apps;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import apps.TwitterApp;
import twitter4j.Status;
import twitter4j.TwitterException;

class TestTwitterApp {

	static TwitterApp twitter;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		twitter = new TwitterApp();
	}

	@BeforeEach
	void setUp() throws Exception {
		System.out.println("\n------------------A test is about to begin:------------------\n");
		System.out.println("The user for these tests is: " + twitter.getUser() + "\n");
	}

	@Test
	void testGetTimeLineWithFilter() {
		List<Status> list;
		try {
			list = twitter.getTimeline("ISCTEIUL", "YouTube");// this could be another user but for test purposes
			// We chose ISCTEIUL
			System.out.println("Filter aplied is ''Youtube'' \n");

			int i = 0;
			for (Status status : list) {
				i++;
				System.out.println("Post number: " + i);
				System.out.println("User: " + status.getUser().getScreenName());
				System.out.println("Text: " + status.getText());
				System.out.println("Favorites: " + status.getFavoriteCount());
				System.out.println("Retweets: " + status.getRetweetCount());
				System.out.println("-------------------------------------------------------------");
			}
		} catch (TwitterException e) {
			System.out.println("Warning to the GUI");
		}
	}

	@Test
	void testGetTimeLine() {
		List<Status> list;
		try {
			list = twitter.getTimeline("ISCTEIUL");  // this could be another user but for test purposes
		// We chose ISCTEIUL
		int i = 0;
		for (Status status : list) {
			i++;
			System.out.println("Post number: " + i);
			System.out.println("User: " + status.getUser().getScreenName());
			System.out.println("Text: " + status.getText());
			System.out.println("Favorites: " + status.getFavoriteCount());
			System.out.println("Retweets: " + status.getRetweetCount());
			System.out.println("-------------------------------------------------------------");
		}
		} catch (TwitterException e) {
			System.out.println("Warning to the GUI");
		}
	}

	@AfterAll
	static void tearDownAfterClass() {
		System.out.println("\n\n **************** FINISHED ALL TESTS **************** \n\n");
	}

}
