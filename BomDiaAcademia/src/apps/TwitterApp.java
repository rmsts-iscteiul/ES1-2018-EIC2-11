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
	 * Deprecated list of a users tweets
	 */
	private List<Status> statuses;

	/**
	 * List containing the next 40 tweets (buffering)
	 */
	private List<Status> nextPageList;

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
	private static final int NUMBER_OF_TWEETS = 40;

	/**
	 * Page number of the tweets database. E.G.: |page 1 = 1st 40 tweets |page 2 =
	 * 2nd 40 tweets
	 */
	private int pagingNumber = 1;

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
		timeFilter = TimeFilter.ALL_TIME;
		try {
			owner = twitter.getScreenName();
		} catch (IllegalStateException | TwitterException e) {
			System.out.println("CHECK CONNECTION - Init");
		}
	}

	/**
	 * Gets the most recent 20 posts from a users timeline.
	 * 
	 * @param user(String) Owns the timeline which is being displayed.
	 * @return List(Status) - Contains the last 20 tweets of the user.
	 */
	@SuppressWarnings("finally")
	public List<Status> getTimeline(String user) throws TwitterException { // The argument need to be
		// the @ of the user. DON'T
		// WRITE THE '@'
		List<Status> list = new LinkedList<>();
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
	 * @param user(String) Owns the timeline which is being displayed.
	 * @param filter(String) Chosen by the User. Tweets of the user are filtered
	 *        using this argument.
	 * @return List(Status) - Contains the last 20 tweets based on the filter(2º
	 *         argument) of the user.
	 */
	@SuppressWarnings("finally")
	public List<Status> getTimeline(String user, String wordfilter) throws TwitterException {
		List<Status> listWithTimeFilter = new LinkedList<>();
		List<Status> listWithFilters = new LinkedList<>();
		setWordFilter(wordfilter);
		try {
			statuses = twitter.getUserTimeline(user, new Paging(1, NUMBER_OF_TWEETS));
			setUser(user);

		} catch (TwitterException e) { // when Twitter service or network is
										// unavailable
			System.out.println("CHECK NETWORK CONNECTION - using an un-updated feed");
			throw e;

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
	 */
	private void bufferingWithoutFilter() {
		bufferingThreadWithoutFilter = new Thread() {
			@Override
			public void run() {
				List<Status> list = new LinkedList<>();
				try {
					incrementPaging();
					list = twitter.getUserTimeline(user, new Paging(pagingNumber, NUMBER_OF_TWEETS));
					if (!timeFilter.equals(TimeFilter.ALL_TIME)) {
						for (Status s : list) {
							long date = ((timeFilter.getDate() / (24 * 60 * 60 * 1000))
									- (s.getCreatedAt().getTime() / (24 * 60 * 60 * 1000)));
							if (date >= 0 && date <= timeFilter.getDif()) {
								nextPageList.add(s);
							}
						}
					} else {
						nextPageList = list;
					}

				} catch (TwitterException e) {
					System.out.println("CHECK CONNECTION - Buffering Thread Stopped.");
					nextPageList = new LinkedList<Status>(); // so it does not send the page2 of another user
				}
			}
		};
		bufferingThreadWithoutFilter.start();
	}

	/**
	 * What the thread needs to do + start();
	 */
	private void bufferingWithFilter() {
		bufferingThreadWithFilter = new Thread() {
			@Override
			public void run() {
				List<Status> list = new LinkedList<>();
				List<Status> listWithTimeFilter = new LinkedList<>();
				try {
					incrementPaging();
					list = twitter.getUserTimeline(user, new Paging(pagingNumber, NUMBER_OF_TWEETS));
					if (!timeFilter.equals(TimeFilter.ALL_TIME)) { // Time Filter
						for (Status s : list) {
							long date = ((timeFilter.getDate() / (24 * 60 * 60 * 1000))
									- (s.getCreatedAt().getTime() / (24 * 60 * 60 * 1000)));
							if (date >= 0 && date <= timeFilter.getDif()) {
								listWithTimeFilter.add(s);
							}
						}
					} else {
						listWithTimeFilter = list;
					} // Word Filter
					for (Status status : listWithTimeFilter) {
						if (status.getText().contains(wordFilter)) {
							nextPageList.add(status);
						}
					}
				} catch (TwitterException e) {
					System.out.println("CHECK CONNECTION - Buffering Thread Stopped");
					nextPageList = new LinkedList<Status>();
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
	 */
	public List<Status> getMoreTweetsWithoutFilter() {
		try {
			bufferingThreadWithoutFilter.join();
			bufferingWithoutFilter();
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
	 */
	public List<Status> getMoreTweetsWithFilter() {
		try {
			bufferingThreadWithFilter.join();
			bufferingWithFilter();
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
	 * @return returns the owners timeline
	 */

	public void tweet(String text, File file) throws TwitterException {
		StatusUpdate newStatus = new StatusUpdate(text + WATERMARK);
		newStatus.media(file);
		twitter.updateStatus(newStatus.getStatus());
	}

	/**
	 * Replies to a status (comment)
	 * 
	 * @param text   - the text you want to comment
	 * @param postID - the id of the status you want to comment
	 * @throws TwitterException
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
	 * @param postID - the id of the status you want to retweet
	 * @throws TwitterException
	 */
	public void retweet(Status status) throws TwitterException { // Share a post
		if (!status.isRetweetedByMe())
			twitter.retweetStatus(status.getId());
		else
			twitter.unRetweetStatus(status.getId());
	}

	/**
	 * Retweets a certain status with a message (comment and share basically)
	 * 
	 * @param postID - the id of the status you want to retweet
	 * @param text   - the text you want to comment
	 * @throws TwitterException
	 */
	public void retweet(String text, Status status) throws TwitterException { // Basically It's to
		// comment and share a
		// post
		StatusUpdate newStatus = new StatusUpdate(text + WATERMARK);
		newStatus.setAttachmentUrl("https://twitter.com/" + user + "/status/" + status.getId());
		twitter.updateStatus(newStatus);
	}

	/**
	 * Favorites/un-favorites a certain users status
	 * 
	 * @param postID of the status you want to favorite
	 * @throws TwitterException
	 */
	public void favorite(Status status) throws TwitterException {
		if (!status.isFavorited())
			twitter.createFavorite(status.getId());
		else
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
	 * @param user
	 */
	public void setUser(String user) {
		this.user = user;
		resetPaging(); // Reset page when user changes
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
		resetPaging(); // resets page when word filter changes
	}

	/**
	 * Change time filter
	 * 
	 * @param timeFilter
	 */
	public void setTimeFilter(TimeFilter timeFilter) {
		this.timeFilter = timeFilter;
		resetPaging(); // Resets page when time filter changes
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

}