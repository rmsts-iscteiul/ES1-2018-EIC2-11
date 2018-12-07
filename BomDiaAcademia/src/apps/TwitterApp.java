package apps;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import twitter4j.Paging;
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

	/**
	 * Time filter that is being used
	 */
	private TimeFilter timeFilter;

	/**
	 * word filter that is being used
	 */
	private String wordFilter;

	/**
	 * It's a simple watermark at the bottom of every tweet
	 */
	private final String WATERMARK = "\n" + "\n" + "-Tweet sent through BomDiaAcademia Application <3";

	/**
	 * twitter refers to the twitter object from the twitterfactory that uses an
	 * ConsumerKey and a Access Token.
	 */
	private Twitter twitter;

	/**
	 * logged in user
	 */
	private String owner = "";

	/**
	 * "Deprecated" (not updated) list of a users tweets
	 */
	private List<Status> statuses;

	/**
	 * List containing the next 40 tweets (buffering)
	 */
	private List<Status> nextPageList = new LinkedList<>();

	/**
	 * Thread is responsible to buffer the next 20 tweets with filter;
	 */
	private Thread bufferingThreadWithFilter;

	/**
	 * Thread is responsible to buffer the next 20 tweets without filter;
	 */
	private Thread bufferingThreadWithoutFilter;

	/**
	 * Number of tweets sent at a time
	 */
	private static final int NUMBER_OF_TWEETS = 20;

	/**
	 * Page number of the tweets database. E.G.: |page 1 = 1st 40 tweets |page 2 =
	 * 2nd 40 tweets
	 */
	private int pagingNumber = 1;

	/**
	 * Constructor of the TwitterApp. It uses an Access token and an Consumer key to
	 * build the twitter factory which uses to build the twitter instance.
	 * 
	 * @throws TwitterException - no connection
	 */
	public TwitterApp(String twitter_tokens) throws TwitterException {
		String[] twitter_tokens_array = twitter_tokens.split(",");
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true).setOAuthConsumerKey(twitter_tokens_array[0])
				.setOAuthConsumerSecret(twitter_tokens_array[1]).setOAuthAccessToken(twitter_tokens_array[2])
				.setOAuthAccessTokenSecret(twitter_tokens_array[3]);
		TwitterFactory tf = new TwitterFactory(cb.build());
		twitter = tf.getInstance();
		timeFilter = TimeFilter.ALL_TIME;
		try {
			owner = twitter.getScreenName();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (TwitterException e) {
			System.out.println("NETWORK CONNECTION - At starting twitterApp");
			throw e; // Warning the GUI that there is no connection
		}
	}

	/**
	 * Gets the most recent 20 posts from a users timeline.
	 * 
	 * @param user(String) Owns the timeline which is being displayed.
	 * @return List(Status) - Contains the last 20 tweets of the user.
	 * @throws TwitterException - no connection
	 */
	@SuppressWarnings("finally")
	public List<Status> getTimeline(String user) throws TwitterException { // The argument need to be
		// the @ of the user. DON'T
		// WRITE THE '@'
		List<Status> list = new LinkedList<>();
		resetPaging();
		try {
			statuses = twitter.getUserTimeline(user, new Paging(1, NUMBER_OF_TWEETS));
			setUser(user);
		} catch (TwitterException e) {
			System.out.println("CHECK NETWORK CONNECTION - using an un-updated feed");
			throw e; // So the GUI knows if it didnt go as planned.
		} finally {
			if (timeFilter.equals(TimeFilter.ALL_TIME)) {// If its all_time then there is no need to alterations.
				bufferingWithoutFilter();
				return statuses;
			}
			for (Status s : statuses) {
				long date = ((timeFilter.getDate() / (24 * 60 * 60 * 1000))
						- (s.getCreatedAt().getTime() / (24 * 60 * 60 * 1000)));
				if (date >= 0 && date <= timeFilter.getDif()) {
					list.add(s);
				}
			}
			bufferingWithoutFilter();
			return list;
		}
	}

	/**
	 * Gets the timeline of the user filtered with the 2º argument of this method
	 * (filter)
	 * 
	 * @param user       (String) Owns the timeline which is being displayed.
	 * @param wordfilter - (String) Chosen by the User. Tweets of the user are
	 *                   filtered using this argument.
	 * @return List (Status) - Contains the last 20 tweets based on the filter(2º
	 *         argument) of the user.
	 * @throws TwitterException - no connection
	 */
	@SuppressWarnings("finally")
	public List<Status> getTimeline(String user, String wordfilter) throws TwitterException {
		List<Status> listWithTimeFilter = new LinkedList<>();
		List<Status> listWithFilters = new LinkedList<>();
		setWordFilter(wordfilter);
		resetPaging();
		try {
			statuses = twitter.getUserTimeline(user, new Paging(1, NUMBER_OF_TWEETS));
			setUser(user);

		} catch (TwitterException e) { // when Twitter service or network is
										// unavailable
			System.out.println("CHECK NETWORK CONNECTION - using an un-updated feed");
			throw e; // Warning the GUI that there is no connection

		} finally {
			if (!timeFilter.equals(TimeFilter.ALL_TIME)) {
				for (Status s : statuses) {
					long date = ((timeFilter.getDate() / (24 * 60 * 60 * 1000))
							- (s.getCreatedAt().getTime() / (24 * 60 * 60 * 1000)));
					if (date >= 0 && date <= timeFilter.getDif()) {
						listWithTimeFilter.add(s);
					}
				}
			} else // If ALL_TIME
				listWithTimeFilter = statuses;

			for (Status status : listWithTimeFilter) {
				if (status.getText().contains(wordfilter)) {
					listWithFilters.add(status);
				}
			}
			bufferingWithFilter();
			return listWithFilters;

		}
	}

	/**
	 * What the thread needs to do + start();
	 * 
	 * @throws TwitterException - no connection
	 */
	public void bufferingWithoutFilter() throws TwitterException {
		bufferingThreadWithoutFilter = new Thread() {
			@SuppressWarnings("finally")
			@Override
			public void run() {
				List<Status> list = new LinkedList<>();
				try {
					incrementPaging();
					list = twitter.getUserTimeline(user, new Paging(pagingNumber, NUMBER_OF_TWEETS));
					nextPageList.clear();
					if (!timeFilter.equals(TimeFilter.ALL_TIME)) {
						for (Status s : list) {
							long date = ((timeFilter.getDate() / (24 * 60 * 60 * 1000))
									- (s.getCreatedAt().getTime() / (24 * 60 * 60 * 1000)));
							if (date >= 0 && date <= timeFilter.getDif()) {
								nextPageList.add(s);
							}
						}
					} else {
						nextPageList.addAll(list);
					}

				} catch (TwitterException e) {
					System.out.println("CHECK CONNECTION - Buffering Thread Stopped.");
					nextPageList = new LinkedList<Status>(); // so it does not send the page2 of another user
					throw e; // Warning the GUI that there is no connection
				} finally {
					System.out.println("");
					return;
				}
			}
		};
		bufferingThreadWithoutFilter.start();
	}

	/**
	 * What the thread needs to do + start();
	 * 
	 * @throws TwitterException - no connection
	 */
	public void bufferingWithFilter() throws TwitterException {
		bufferingThreadWithFilter = new Thread() {
			@SuppressWarnings("finally")
			@Override
			public void run() {
				List<Status> list = new LinkedList<>();
				List<Status> listWithTimeFilter = new LinkedList<>();
				try {
					incrementPaging();
					list = twitter.getUserTimeline(user, new Paging(pagingNumber, NUMBER_OF_TWEETS));
					if (!timeFilter.equals(TimeFilter.ALL_TIME)) { // Time
																	// Filter
						for (Status s : list) {
							long date = ((timeFilter.getDate() / (24 * 60 * 60 * 1000))
									- (s.getCreatedAt().getTime() / (24 * 60 * 60 * 1000)));
							if (date >= 0 && date <= timeFilter.getDif()) {
								listWithTimeFilter.add(s);
							}
						}
					} else {
						listWithTimeFilter.addAll(list);
					} // Word Filter
					nextPageList.clear();
					for (Status status : listWithTimeFilter) {
						if (status.getText().contains(wordFilter)) {
							nextPageList.add(status);
						}
					}
				} catch (TwitterException e) {
					System.out.println("CHECK CONNECTION - Buffering Thread Stopped");
					nextPageList = new LinkedList<Status>();
					throw e;
				} finally {
					return;
				}
			}
		};
		bufferingThreadWithFilter.start();

	}

	/**
	 * When you press a button "more..." it wait for the thread that is buffering
	 * the posterior info. to finish and it will begin its search again.
	 * 
	 * @return the list of the next 40 tweets (without word filter)
	 * @throws TwitterException - no connection
	 */
	public List<Status> getMoreTweetsWithoutFilter() throws TwitterException {
		try {
			bufferingThreadWithoutFilter.join();
			return nextPageList;
		} catch (InterruptedException e) {
			return nextPageList;
		}
	}

	/**
	 * When you press a button "more..." it wait for the thread that is buffering
	 * the posterior info. to finish and it will begin its search again.
	 * 
	 * @return the list of the next 40 tweets
	 * @throws TwitterException - no connection
	 */
	public List<Status> getMoreTweetsWithFilter() throws TwitterException {
		try {
			bufferingThreadWithFilter.join();
			return nextPageList;
		} catch (InterruptedException e) {
			return nextPageList;
		}
	}

	/**
	 * Publishes a tweet on the logged users timeline and redirects its user to his
	 * homepage
	 * 
	 * @param text - tweets this text
	 * @param file - file(image)
	 * @throws TwitterException - no connection
	 */

	public void tweet(String text, File file) throws TwitterException {
		StatusUpdate newStatus = new StatusUpdate(text + WATERMARK);
		if (file != null)
			newStatus.setMedia(file);
		twitter.updateStatus(newStatus.getStatus());
	}

	/**
	 * Replies to a status (comment)
	 * 
	 * @param text   - the text you want to comment
	 * @param status - the status you want to comment
	 * @throws TwitterException - no connection
	 */
	public void replyTo(String text, Status status) throws TwitterException { // Comment as a reply to
		// @user.
		StatusUpdate newStatus = new StatusUpdate("@" + user + "\n" + text + WATERMARK);
		newStatus.setInReplyToStatusId(status.getId());
		twitter.updateStatus(newStatus);

	}

	/**
	 * Retweets a certain status
	 * 
	 * @param status - the status you want to retweet
	 * @throws TwitterException - no connection
	 */
	public void retweet(Status status) throws TwitterException { // Share a post
		if (!status.isRetweetedByMe())
			twitter.retweetStatus(status.getId());
	}

	/**
	 * Unretweets a certain status
	 * 
	 * @param status - the status you want to unretweet
	 * @throws TwitterException - no connection
	 */
	public void unRetweet(Status status) throws TwitterException {
		if (status.isRetweetedByMe())
			twitter.unRetweetStatus(status.getId());
	}

	/**
	 * Retweets a certain status with a message (comment and share basically)
	 * 
	 * @param status - the status you want to retweet
	 * @param text   - the text you want to comment
	 * @throws TwitterException - no connection
	 */
	public void retweet(String text, Status status) throws TwitterException { // Basically It's to
		// comment and share a
		// post
		StatusUpdate newStatus = new StatusUpdate(text + WATERMARK);
		newStatus.setAttachmentUrl("https://twitter.com/" + user + "/status/" + status.getId());
		twitter.updateStatus(newStatus);
	}

	/**
	 * Favorites a certain users status
	 * 
	 * @param status the status you want to favorite
	 * @throws TwitterException - no connection
	 */
	public void favorite(Status status) throws TwitterException {
		if (!status.isFavorited())
			twitter.createFavorite(status.getId());
	}

	/**
	 * un-favorites a certain users status (needs to be favorited by you)
	 * 
	 * @param status the status you want to unfavorite
	 * @throws TwitterException - no connection
	 */
	public void unFavorite(Status status) throws TwitterException {
		System.out.println(status.isFavorited());
		if (status.isFavorited())
			twitter.destroyFavorite(status.getId());
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
	 * @param user - user searched
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

	/**
	 * timefilter getter
	 * 
	 * @return timefilter
	 */
	public TimeFilter getTimeFilter() {
		return timeFilter;
	}

	/**
	 * wordFilter getter
	 * 
	 * @return wordFilter
	 */
	public String getWordFilter() {
		return wordFilter;
	}

	/**
	 * wordFilter setter
	 * 
	 * @param wordFilter
	 */
	private void setWordFilter(String wordFilter) {
		this.wordFilter = wordFilter;
	}

	/**
	 * Change time filter
	 * 
	 * @param timeFilter time filter
	 */
	public void setTimeFilter(TimeFilter timeFilter) {
		this.timeFilter = timeFilter;
	}

	/**
	 * Increments the number of the page related with the timeline (more tweets)
	 */
	private void incrementPaging() {
		pagingNumber++;
	}

	/**
	 * Resets the number of the page related with the timeline (back to x tweets)
	 */
	private void resetPaging() {
		pagingNumber = 1;
	}

	/**
	 * Gets number of tweets per page.
	 * 
	 * @return Number of tweets
	 */
	public int getNumberOfTweetsPerPage() {
		return NUMBER_OF_TWEETS;
	}

}