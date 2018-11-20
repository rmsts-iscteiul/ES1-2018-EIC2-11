package tests;


import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import src.apps.TwitterApp;
import twitter4j.Status;

class TestTwitterApp {

	static TwitterApp twitter;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		twitter = new TwitterApp();
	}

	@BeforeEach
	void setUp() throws Exception {
		System.out.println("\nA test is about to begin:\n");
	}

	
	
	@Test
	void testGetTimeLineWithFilter() {
		List<Status> list = twitter.getTimeline("ISCTEIUL", "YouTube"); // this could be another user but for test purposes
		// We chose ISCTEIUL
		System.out.println("Filter aplied is ''Youtube'' \n");
		
		int i= 0;
		for (Status status : list) {
			i++;
			System.out.println("Post number: " + i);
			System.out.println("User: " + status.getUser().getScreenName());
			System.out.println("Text: " + status.getText());
			System.out.println("Favorites: " + status.getFavoriteCount());
			System.out.println("Retweets: " + status.getRetweetCount());
			System.out.println("-------------------------------------------------------------");
		}
	}
	
	@Test
	void testGetTimeLine() {
		List<Status> list = twitter.getTimeline("ISCTEIUL"); // this could be another user but for test purposes
		// We chose ISCTEIUL
		int i= 0;
		for (Status status : list) {
			i++;
			System.out.println("Post number: " + i);
			System.out.println("User: " + status.getUser().getScreenName());
			System.out.println("Text: " + status.getText());
			System.out.println("Favorites: " + status.getFavoriteCount());
			System.out.println("Retweets: " + status.getRetweetCount());
			System.out.println("-------------------------------------------------------------");
		}
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		System.out.println("\n\n **************** FINISHED ALL TESTS **************** \n\n");
	}

}
