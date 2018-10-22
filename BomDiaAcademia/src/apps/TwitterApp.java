package apps;

import java.util.LinkedList;
import java.util.List;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * 
 * This is the Twitter Aplication. It uses twitter4j library. 
 * 
 * @author ES1-2018-EIC2-11
 *
 */

public class TwitterApp {
	/**
	 * The variable user refers to the last searched user. Initially it has the value
	 * of "ISCTEIUL" (default value).
	 */
	private String user = "ISCTEIUL";
	
	/**
	 * twitter refers to the twitter object from the twitterfactory that uses an ConsumerKey
	 * and a Access Token.
	 */
	private Twitter twitter;

	/**
	 * Constructor of the TwitterApp. It uses an Access token and an Consumer key
	 *  to build the twitter factory which uses to build the twitter instance.
	 */
	public TwitterApp() {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true).setOAuthConsumerKey("GahzkZi50ZuasuqqkRqckC2ln")
				.setOAuthConsumerSecret("Rr3Q7ivhL3HiEKVoP3d2aLwNbh6ez5rTdEy3MtNRd6yeC5vNVV")
				.setOAuthAccessToken("3362933517-swAMAbDiMufcuyxHUEpkSYUJ3JY9ANIrqT5yirP")
				.setOAuthAccessTokenSecret("onDIulYjcQCvb3rVk6N3cYp0DxytW0ew86fM2Kyp8JQOj");
		TwitterFactory tf = new TwitterFactory(cb.build());
		twitter = tf.getInstance();

	}

		/**
		 * Gets the most recent 20 posts from a users timeline. 
		 * 
		 * @param user(String) Owns the timeline which is being displayed.
		 * @return List(Status) - Contains the last 20 tweets of the user.
		 */
	public List<Status> getTimeline(String user) { // The argument need to be the @ of the user. DON'T
															// WRITE THE '@'
		try {
			this.user = user;
			return (List<Status>) twitter.getUserTimeline(user); 

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null; // when Twitter service or network is unavailable this will return a null LIST
	}

	/**
	 * Gets the timeline of the user filtered with the 2º argument of this method (filter)
	 * 
	 * @param user(String) Owns the timeline which is being displayed.
	 * @param filter(String) Chosen by the User. Tweets of the user are filtered using this argument.
	 * @return List(Status) - Contains the last 20 tweets based on the filter(2º argument) of the user.
	 */
	public List<Status> getTimeline(String user, String filter) {
		List<Status> statuses = new LinkedList<>();
		this.user = user;
		try {
			for (Status status : twitter.getUserTimeline(user)) {
				if (status.getText().contains(filter)) {
					statuses.add(status);
				}
			}
		} catch (Exception e) { // when Twitter service or network is unavailable
			System.out.println(e.getMessage());
		}
		return statuses; 

	}
	/**
	 * Returns the user that is being displayed.
	 * @return user(String) - (Getter) gets the user attribute.
	 */
	public String getUser(){
		return user;
	}
	
	
	//-------------------------Test-------------------------------//
	
//	public static void main(String[] args) {
//		TwitterAPI t = new TwitterAPI();
//		List<Status> s= t.showTimeline("ISCTEIUL", "vid");
//		for (Status status : s) {
//			System.out.println(status.getText());
//		}
//	}

}