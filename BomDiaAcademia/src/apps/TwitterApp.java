package apps;

import java.util.LinkedList;
import java.util.List;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
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
	 * The variable user refers to the last searched user. Initially it has the
	 * value of "ISCTEIUL" (default value).
	 */
	private String user = "ISCTEIUL";
	
	/**
	 * twitter refers to the twitter object from the twitterfactory that uses an
	 * ConsumerKey and a Access Token.
	 */
	private Twitter twitter;

	private String owner = "";
	
	/**
	 * Deprecated list of a users tweets
	 */
	private List<Status> statuses;

	/**
	 * Constructor of the TwitterApp. It uses an Access token and an Consumer key to
	 * build the twitter factory which uses to build the twitter instance.
	 */
	public TwitterApp() {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true).setOAuthConsumerKey("GahzkZi50ZuasuqqkRqckC2ln")
				.setOAuthConsumerSecret("Rr3Q7ivhL3HiEKVoP3d2aLwNbh6ez5rTdEy3MtNRd6yeC5vNVV")
				.setOAuthAccessToken("3362933517-swAMAbDiMufcuyxHUEpkSYUJ3JY9ANIrqT5yirP")
				.setOAuthAccessTokenSecret("onDIulYjcQCvb3rVk6N3cYp0DxytW0ew86fM2Kyp8JQOj");
		TwitterFactory tf = new TwitterFactory(cb.build());
		twitter = tf.getInstance();
		try {
			owner = twitter.getScreenName();
		} catch (IllegalStateException | TwitterException e) {
			e.printStackTrace();
		}
	}
	/**
	 * timeline of the user that is logged-in
	 * @return the timeline of the user that is logged-in
	 */
	public List<Status> homepage(){
		return getTimeline(owner);
	}
	
	/**
	 * Gets the most recent 20 posts from a users timeline.
	 * 
	 * @param user(String) Owns the timeline which is being displayed.
	 * @return List(Status) - Contains the last 20 tweets of the user.
	 */
	@SuppressWarnings("finally")
	public List<Status> getTimeline(String user) { // The argument need to be the @ of the user. DON'T
													// WRITE THE '@'
		try {
			statuses = twitter.getUserTimeline(user);
			this.user = user;

		} catch (TwitterException e) { // when Twitter service or network is unavailable this will return the
										// un-updated LIST
			System.out.println("CHECK NETWORK CONNECTION");
		}finally {
			return statuses;
		}
	}

	/**
	 * Gets the timeline of the user filtered with the 2º argument of this method
	 * (filter)
	 * 
	 * @param user(String) Owns the timeline which is being displayed.
	 * @param filter(String) Chosen by the User. Tweets of the user are filtered
	 *        using this argument.
	 * @return List(Status) - Contains the last 20 tweets based on the filter(2º
	 *         argument) of the user.
	 */
	@SuppressWarnings("finally")
	public List<Status> getTimeline(String user, String filter) {
		List<Status> list = new LinkedList<>();
		try {
			statuses = twitter.getUserTimeline(user);
			this.user = user;
			
		} catch (TwitterException e) { // when Twitter service or network is unavailable
			System.out.println("CHECK NETWORK CONNECTION");
		}finally{
			for (Status status : statuses) {
				if (status.getText().contains(filter)) {
					list.add(status);
				}
			}
			return list;
			
		}
	}

	/**
	 * Publishes a tweet on the logged users timeline and redirects its user to his homepage
	 * @param text - Tweets text
	 */
	public List<Status> tweet(Status text){
		try {
			twitter.updateStatus(text + "\n" + "\n" + "-Tweet sent through BomDiaAcademia Application <3");
			return homepage();
		} catch (TwitterException e) {
			System.out.println("CHECK CONNECTION - Tweet was possibly not published.");
			return statuses;
		}
	}
	
	/**
	 * Returns the user that is being displayed.
	 * 
	 * @return user(String) - (Getter) gets the user attribute.
	 */
	public String getUser() {
		return user;
	}
	
	/**
	 * Changes users page
	 * @param user
	 */
	public void setUser(String user) {
		this.user = user;
	}
	
	/**
	 * User that is logged-in
	 * @return the "@"name of the user that is logged-in
	 */
	public String getOwner(){
		return owner;
	}

}