package tests;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.restfb.types.Post;

import src.apps.FacebookApp;

class TestFacebookApp {

	private static FacebookApp app;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		app = new FacebookApp(); // initiates facebookApp
	}
	
	@BeforeAll
	void setUp() throws Exception{
		System.out.println("\nA test is about to begin: \n");
	}

	@Test
	void testGetUserInfo() {
		System.out.println("-----------------------------------------------------------------------");
		System.out.println("The user of this app is " + app.getUser()); // This should return user's name, owner of the access token previously defined
											
	}
	
	@Test
	void testGetAllPosts() {
		int i = 0;
		List<Post> results = app.getTimeline();
		System.out.println("All post are going to be printed....\n");
		for (Post a : results) {
			System.out.println("\n-----------------------------------------------------------------------");
			System.out.println("Post number: " + i);
			System.out.println("Message: " + a.getMessage());
		}
	}
	
	@Test
	void testGetPostsbasedOnFilter() {
		int i =0;
		List<Post> results = app.getTimeline();
		System.out.println("Posts based on filter previous chosen are going to be printed....\n");
		
		for (Post a : results) {
			if(a.getMessage().contains("É")){ //filter
				System.out.println("\n-----------------------------------------------------------------------");
				System.out.println("Post number: " + i);
				System.out.println("Message: " + a.getMessage()); // Returns all the posts that contains the previous filter
			}
		}
	}
	
	@AfterAll
	static void tearDownAfterClass() {
		System.out.println("\n\n\n************************ FINISHED ALL TESTS ********************************+\n\n\n");
	}


}
