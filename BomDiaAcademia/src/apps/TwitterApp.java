package apps;

import java.util.LinkedList;
import java.util.List;

import twitter4j.Status;
import twitter4j.StatusUpdate;
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

	private final String WATERMARK = "\n" + "\n" + "-Tweet sent through BomDiaAcademia Application <3";

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
	 * Constructor of the TwitterApp. It uses an Access token and an Consumer
	 * key to build the twitter factory which uses to build the twitter
	 * instance.
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @return timeline of the user that is logged-in
	 */
	public List<Status> homepage() {
		return getTimeline(owner);
	}

	/**
	 * Gets the most recent 20 posts from a users timeline.
	 * 
	 * @param user(String)
	 *            Owns the timeline which is being displayed.
	 * @return List(Status) - Contains the last 20 tweets of the user.
	 */
	@SuppressWarnings("finally")
	public List<Status> getTimeline(String user) { // The argument need to be
													// the @ of the user. DON'T
													// WRITE THE '@'
		try {
			statuses = twitter.getUserTimeline(user);
			this.user = user;

		} catch (TwitterException e) { // when Twitter service or network is
										// unavailable this will return the
										// un-updated LIST
			System.out.println("CHECK NETWORK CONNECTION");
		} finally {
			return statuses;
		}
	}

	/**
	 * Gets the timeline of the user filtered with the 2º argument of this
	 * method (filter)
	 * 
	 * @param user(String)
	 *            Owns the timeline which is being displayed.
	 * @param filter(String)
	 *            Chosen by the User. Tweets of the user are filtered using this
	 *            argument.
	 * @return List(Status) - Contains the last 20 tweets based on the filter(2º
	 *         argument) of the user.
	 */
	@SuppressWarnings("finally")
	public List<Status> getTimeline(String user, String filter) {
		List<Status> list = new LinkedList<>();
		try {
			statuses = twitter.getUserTimeline(user);
			this.user = user;

		} catch (TwitterException e) { // when Twitter service or network is
										// unavailable
			System.out.println("CHECK NETWORK CONNECTION");
		} finally {
			for (Status status : statuses) {
				if (status.getText().contains(filter)) {
					list.add(status);
				}
			}
			return list;

		}
	}

	/**
	 * Publishes a tweet on the logged users timeline and redirects its user to
	 * his homepage
	 * 
	 * @param text - tweets this text
	 * @return returns the owners timeline
	 */
	public List<Status> tweet(String text) {
		try {
			StatusUpdate newStatus = new StatusUpdate(text);
			twitter.updateStatus(newStatus.getStatus() + WATERMARK);
			return homepage();
		} catch (TwitterException e) {
			System.out.println("CHECK CONNECTION - Tweet was possibly not published.");
			return statuses;
		}
	}

	/**
	 * Replies to a status (comment)
	 * @param text - the text you want to comment
	 * @param postID - the id of the status you want to comment
	 */
	public void replyTo(String text, long postID) { // Comment as a reply to @user.
		try {
			StatusUpdate newStatus = new StatusUpdate("@" + user + "\n" + text);
			newStatus.setInReplyToStatusId(postID);
			twitter.updateStatus(newStatus + WATERMARK);
		} catch (TwitterException e) {
			e.printStackTrace();
		}

	}

	
	/**
	 * Retweets a certain status
	 * @param postID - the id of the status you want to retweet
	 */
	public void retweet(long postID) { //Share a post
		try {
			twitter.retweetStatus(postID);
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Retweets a certain status with a message (comment and share basically)
	 * @param postID - the id of the status you want to retweet
	 * @param text - the text you want to comment
	 */
	public void retweet(long postID, String text){ //Basically It's to comment and share a post
		try{
			StatusUpdate newStatus = new StatusUpdate(text);
			newStatus.setAttachmentUrl("https://twitter.com/" + user + "/status/" + postID);
			twitter.updateStatus(newStatus + WATERMARK);
		}catch (TwitterException e){
			e.printStackTrace();
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
	 * 
	 * @param user
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * User that is logged-in
	 * 
	 * @return the "@"name of the user that is logged-in
	 */
	public String getOwner() {
		return owner;
	}

	//BEFORE JUNIT
//	public static void main(String[] args) {
//		TwitterApp t = new TwitterApp();
//		System.out.println(t.getTimeline("Slbenfica").get(0).getText());
//		System.out.println(t.getTimeline("Slbenfica").get(0).getId());
//		long id = (long) t.getTimeline("Slbenfica").get(0).getId();
//		//t.replyTo("Random !", id);
//		t.retweet(id, "something something");
//	}

}