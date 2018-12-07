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
		app = new FacebookApp(
				"EAAD4C79u9UYBAMkgJAOuZAeFyWQarOmO9pBaLrRAfhA8G7DrJyWgJXZAWZAZCnUDhhI9z14AzQmGWLw38R3QFR5SZBZCl0hV0RtwQibKeFLHKOjUZA9gO6kq2N5Xzq0GEDuCi4neMZBrpX60Y8vtzMUfEi1CuVJdf9ZBBNQx6pNwPdVCF7akwPwmWxLvU1NZAqsk0ZD"); // initiates
																																																								// facebookApp
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
	void testGetFirstPagePosts() {
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
	void testGetAllPosts() {
		int i = 0;
		app.setTimeFilter(TimeFilter.ALL_TIME);
		List<Post> results = app.getTimeline();
		while (!results.isEmpty()) {
			System.out.println("All post are going to be printed....\n");
			for (Post a : results) {
				System.out.println("\n-----------------------------------------------------------------------");
				System.out.println("Post number: " + i);
				System.out.println("Message: " + a.getMessage()); // Returns all the messages
				System.out.println("Likes: " + a.getLikesCount()); // Returns the post's likes count
				System.out.println("Comments: " + a.getCommentsCount()); // Returns the post's comments count
				System.out.println("Shares: " + a.getSharesCount()); // Returns the post's shares count
				i++;
				app.incrementDesiredPage();
				results = app.getPostsByPage();
			}
		}

	}

	@Test
	void testGetFirstPagePostsBasedOnFilter() {
		int i = 0;
		app.setTimeFilter(TimeFilter.ALL_TIME);
		List<Post> results = app.getTimeline("a");
		System.out.println("Posts based on filter previous chosen are going to be printed....\n");
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

	// Will get posts from specific day(1 post for this user)
	@Test
	void testGetPostsBasedOnSpecificDayTimeFilter() {
		int i = 0;
		app.setTimeFilter(TimeFilter.SPECIFIC_DAY);
		app.getTimeFilter().setDate(2017, 10, 19);
		List<Post> results = app.getTimeline();
		System.out.println("Posts based on filter previous chosen are going to be printed....\n");
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
		List<Post> results = app.getTimeline("a");
		System.out.println("Posts based on filter previous chosen are going to be printed....\n");
		while (!results.isEmpty()) {
			System.out.println("All post are going to be printed....\n");
			for (Post a : results) {
				System.out.println("\n-----------------------------------------------------------------------");
				System.out.println("Post number: " + i);
				System.out.println("Message: " + a.getMessage()); // Returns all the messages
				System.out.println("Likes: " + a.getLikesCount()); // Returns the post's likes count
				System.out.println("Comments: " + a.getCommentsCount()); // Returns the post's comments count
				System.out.println("Shares: " + a.getSharesCount()); // Returns the post's shares count
				i++;
				app.incrementDesiredPage();
				results = app.getPostsByPage("a");
			}
		}
		System.out.println("Done!");

	}

	// It will return that there are no posts because the user doesnt have posts
	// within the timeFilter applied
	@Test
	void testGetFirstPageBasedOnTextAndTimeFilter() {
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

	// It will return that there are no posts because the user doesnt have posts
	// within the timeFilter applied
	@Test
	void testGetAllPostsBasedOnTimeFilter() {
		int i = 0;
		app.setTimeFilter(TimeFilter.LAST_HOUR);
		List<Post> results = app.getTimeline();
		if (results.isEmpty()) {
			System.out.println("There are no posts for this text filter/time filter");
		} else {
			while (!results.isEmpty()) {
				System.out.println("Posts based on filter previous chosen are going to be printed....\n");

				for (Post a : results) {
					System.out.println("\n-----------------------------------------------------------------------");
					System.out.println("Post number: " + i);
					System.out.println("Message: " + a.getMessage()); // Returns all the posts that contains the
																		// previous
																		// filter
					System.out.println("Likes: " + a.getLikesCount()); // Returns the post's likes count
					System.out.println("Comments: " + a.getCommentsCount()); // Returns the post's comments count
					System.out.println("Shares: " + a.getSharesCount()); // Returns the post's shares count
					i++;
					app.incrementDesiredPage();
					results = app.getPostsByPage();
				}
			}
		}
	}

	@AfterAll
	static void tearDownAfterClass() {
		System.out.println("\n\n\n************************ FINISHED ALL TESTS ********************************+\n\n\n");
	}

}
