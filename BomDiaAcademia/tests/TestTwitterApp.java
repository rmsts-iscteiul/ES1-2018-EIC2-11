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
	void testGetTimeLineWithWordFilter() {
		List<Status> list;
		try {
			twitter.setTimeFilter(TimeFilter.ALL_TIME);
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
			System.out.println("HERE ARE THE FIRST " + twitter.getNumberOfTweetsPerPage());
			System.out.println("LETS GET MORE!");
			list = twitter.getMoreTweetsWithFilter();
			i = 0;
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
	void testGetTimeLineWithoutWordFilter() {

		try {
			twitter.setTimeFilter(TimeFilter.ALL_TIME);
			List<Status> list = twitter.getTimeline("ISCTEIUL"); // this could be another user but for test purposes
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
			System.out.println("HERE ARE THE FIRST " + twitter.getNumberOfTweetsPerPage());
			System.out.println("LETS GET MORE!");
			list = twitter.getMoreTweetsWithoutFilter();
			i = 0;
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
	void testGetTimelineWithTimeFilter() {
		try {
			twitter.setTimeFilter(TimeFilter.LAST_MONTH);
			List<Status> list = twitter.getTimeline("ISCTEIUL");
			for (Status status : list) {
				System.out.println("User: " + status.getUser().getScreenName());
				System.out.println("Text: " + status.getText());
				System.out.println("Favorites: " + status.getFavoriteCount());
				System.out.println("Retweets: " + status.getRetweetCount());
				System.out.println("-------------------------------------------------------------");
			}
			twitter.setTimeFilter(TimeFilter.LAST_MONTH);
			twitter.getTimeline("ISCTEIUL");

			System.out.println("Now lets change the time filter and add a word filter");
			System.out.println("-------------------------------------------------------------");
			twitter.setTimeFilter(TimeFilter.SPECIFIC_DAY);
			twitter.getTimeFilter().setDate(2018, 11, 30);
			list = twitter.getTimeline("ISCTEIUL", "video");
			for (Status status : list) {
				System.out.println("User: " + status.getUser().getScreenName());
				System.out.println("Text: " + status.getText());
				System.out.println("Favorites: " + status.getFavoriteCount());
				System.out.println("Retweets: " + status.getRetweetCount());
				System.out.println("-------------------------------------------------------------");
			}
			twitter.setTimeFilter(TimeFilter.LAST_MONTH);
			twitter.getWordFilter();
			twitter.getTimeline("ISCTEIUL", "video");
		} catch (TwitterException e) {
			System.out.println("Warning to the GUI");
		}
	}

	@Test
	void homepage() {

		try {
			System.out.println("The users logged-in is " + twitter.getOwner());
			List<Status> list = twitter.getTimeline(twitter.getOwner());
			int i = 0;
			for (Status status : list) {
				i++;
				System.out.println("Post number: " + i);
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
	void tweet() {
		try {
			twitter.tweet("It's just a test from my new and awesome app!", null);
		} catch (TwitterException e) {
			System.out.println("Warning to the GUI");
		}
	}

	@Test
	void replyTo() {
		try {
			twitter.setTimeFilter(TimeFilter.LAST_MONTH);
			twitter.replyTo("Test of a reply from my new and awesome app!",
					twitter.getTimeline(twitter.getUser()).get(0)); // random status
			System.out.println("Done ! :)");
		} catch (TwitterException e) {
			System.out.println("Warning to the GUI");
		}
	}

	@Test
	void retweet() {
		try {
			twitter.setTimeFilter(TimeFilter.LAST_MONTH);
			Status s = twitter.getTimeline(twitter.getUser()).get(0);
			twitter.retweet(s);
			System.out.println("Now lets retweet with a message");
			twitter.retweet("Another test from my new and awesome app! (yes, I know it's another one, I'm sorry!)", s);
		} catch (TwitterException e) {
			System.out.println("Warning to the GUI");
		}
	}

	@Test
	void unRetweet() {
		try {
			twitter.setTimeFilter(TimeFilter.LAST_MONTH);
			Status s = twitter.getTimeline(twitter.getUser()).get(0);
			twitter.unRetweet(s);
		} catch (TwitterException e) {
			System.out.println("Warning to the GUI");
		}
	}

	@Test
	void favorite() {
		try {
			twitter.setTimeFilter(TimeFilter.LAST_MONTH);
			Status s = twitter.getTimeline("Slbenfica").get(0);
			System.out.println("Lets add a favorite to a random status");
			twitter.favorite(s);
			System.out.println("Favorite done if possible (not favorited yet)");
		} catch (TwitterException e) {
			System.out.println("Warning to the GUI");
		}
	}

	@Test
	void unFavorite() {
		try {
			twitter.setTimeFilter(TimeFilter.LAST_MONTH);
			Status s = twitter.getTimeline("Slbenfica").get(0);
			System.out.println("now lets remove the favorite that was just added !");
			twitter.unFavorite(s);
		} catch (TwitterException e) {
			System.out.println("Warning to the GUI");
		}
	}

	@AfterAll
	static void tearDownAfterClass() {
		System.out.println("\n\n **************** FINISHED ALL TESTS **************** \n\n");
	}

}
