package tests;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.restfb.types.Post;

import apps.FacebookApp;
import apps.TimeFilter;

class TestFacebookApp {

	private static FacebookApp app;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		app = new FacebookApp(); // initiates facebookApp
	}

	@BeforeEach
	void setUp() throws Exception {
		System.out.println("\nA test is about to begin: \n");
	}

	@Test
	void testGetUserInfo() {
		System.out.println("-----------------------------------------------------------------------");
		System.out.println("The user of this app is " + app.getUserName()); // This should return user's name, owner of
																			// the access token previously defined

	}

	@Test
	void testGetAllPosts() {
		int i = 0;
		app.setTimeFilter(TimeFilter.ALL_TIME);
		List<Post> results = app.getTimeline();
		System.out.println("All post are going to be printed....\n");
		for (Post a : results) {
			System.out.println("\n-----------------------------------------------------------------------");
			System.out.println("Post number: " + i);
			System.out.println("Message: " + a.getMessage()); // Returns all the messages
			System.out.println("Likes: " + a.getLikesCount()); // Returns the post's likes count
			System.out.println("Comments: " + a.getCommentsCount()); // Returns the post's comments count
			System.out.println("Shares: " + a.getSharesCount()); // Returns the post's shares count
			i++;
		}
	}

	@Test
	void testGetAllPostsBasedOnFilter() {
		int i = 0;
		app.setTimeFilter(TimeFilter.ALL_TIME);
		List<Post> results = app.getTimeline("w");
		System.out.println("Posts based on filter previous chosen are going to be printed....\n");

		for (Post a : results) {
			System.out.println("\n-----------------------------------------------------------------------");
			System.out.println("Post number: " + i);
			System.out.println("Message: " + a.getMessage()); // Returns all the posts that contains the previous
																// filter
			System.out.println("Likes: " + a.getLikesCount()); // Returns the post's likes count
			System.out.println("Comments: " + a.getCommentsCount()); // Returns the post's comments count
			System.out.println("Shares: " + a.getSharesCount()); // Returns the post's shares count
			i++;
		}
	}

	//It will return that there are no posts because the user doesnt have posts within the timeFilter applied
	@Test
	void testGetPostsbasedOnTextAndTimeFilter() {
		int i = 0;
		app.setTimeFilter(TimeFilter.LAST_MONTH);
		List<Post> results = app.getTimeline("w");
		if (results.isEmpty()) {
			System.out.println("There are no posts for this text filter/time filter");
		} else {
			System.out.println("Posts based on filter previous chosen are going to be printed....\n");

			for (Post a : results) {
				System.out.println("\n-----------------------------------------------------------------------");
				System.out.println("Post number: " + i);
				System.out.println("Message: " + a.getMessage()); // Returns all the posts that contains the previous
																	// filter
				System.out.println("Likes: " + a.getLikesCount()); // Returns the post's likes count
				System.out.println("Comments: " + a.getCommentsCount()); // Returns the post's comments count
				System.out.println("Shares: " + a.getSharesCount()); // Returns the post's shares count
				i++;
			}
		}
	}
	
	//It will return that there are no posts because the user doesnt have posts within the timeFilter applied
	@Test
	void testGetPostsbasedOnTimeFilter() {
		int i = 0;
		app.setTimeFilter(TimeFilter.LAST_HOUR);
		List<Post> results = app.getTimeline();
		if (results.isEmpty()) {
			System.out.println("There are no posts for this text filter/time filter");
		} else {
			System.out.println("Posts based on filter previous chosen are going to be printed....\n");

			for (Post a : results) {
				System.out.println("\n-----------------------------------------------------------------------");
				System.out.println("Post number: " + i);
				System.out.println("Message: " + a.getMessage()); // Returns all the posts that contains the previous
																	// filter
				System.out.println("Likes: " + a.getLikesCount()); // Returns the post's likes count
				System.out.println("Comments: " + a.getCommentsCount()); // Returns the post's comments count
				System.out.println("Shares: " + a.getSharesCount()); // Returns the post's shares count
				i++;
			}
		}
	}

	@AfterAll
	static void tearDownAfterClass() {
		System.out.println("\n\n\n************************ FINISHED ALL TESTS ********************************+\n\n\n");
	}

}
