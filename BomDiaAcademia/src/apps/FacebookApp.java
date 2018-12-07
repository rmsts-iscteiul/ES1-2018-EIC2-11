
package apps;

import java.util.LinkedList;
import java.util.List;
import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.exception.FacebookNetworkException;
import com.restfb.types.Post;
import com.restfb.types.User;

/**
 * This is the FacebookApp that uses restfb library.
 * 
 * @author ES1-2018-EIC2-11
 * 
 */

public class FacebookApp {

	/**
	 * fbClient is to initiate communication with facebook trough accessToken
	 */
	private static FacebookClient fbClient;
	
	/**
	 * Desired page to appear in GUI(Paging mechanism)
	 */
	private int desiredPage = 0;

	/**
	 * result is the list where posts are placed when fetching info from Facebook
	 */
	private Connection<Post> result;

	/**
	 * Current time filter for the posts
	 */
	private TimeFilter timeFilter;

	/**
	 * User's acess token
	 */
	private String userAccessToken;

	/**
	 * Object fetched from facebook
	 */
	private static User me;
	
	/**
	 * List of the pages(List of posts) that, after getting timeline, will be the pages of posts fetched from facebook
	 */
	private List<List<Post>> pagesList = new LinkedList<List<Post>>();

	/**
	 * Initilize app(fbClient) with given acessToken, and fetched "me" object from
	 * facebook. Also sets time filter to TimeFilter.ALL_TIME
	 */
	@SuppressWarnings("deprecation")
	public FacebookApp() {
		userAccessToken = "EAAD4C79u9UYBAKK5kALF2cxjaebseMLiSzyK6WIE3Y6iPfZBfo7164b0ZBLe9mHC1hKI7ZBjHcbr5Bef8DfzaizSZCpJBNK5SPRkTEHYGoGPSsZCVyhmGjTWELopC2c4LhLcFZAZBWia1rRJie2uR1WGgKKA301SaaZB2d3XpAW5ZAgZDZD";
		fbClient = new DefaultFacebookClient(userAccessToken);

		me = fbClient.fetchObject("me", User.class, Parameter.with("fields", "picture,name"));
		timeFilter = TimeFilter.ALL_TIME;
	}

	/**
	 * Runnable that saves all pages of posts in a local list, with the result list fetched from facebook
	 */
	Runnable getPagesList = new Runnable() {
		public void run() {
			for (List<Post> page : result) {
				pagesList.add(page);
			}
		}
	};

	/**
	 * Returns all non-null message posts from user feed based on a time filter, and alwats fetches updated information
	 * 
	 * @throws FacebookNetworkException when system is offline, returning posts from
	 *                                  previous list
	 * 
	 * @return List where all non-null message posts from user feed are
	 *         included(with time filter).
	 */
	@SuppressWarnings("finally")
	public List<Post> getTimeline() {
		resetDesiredPage();
		try {
			result = fbClient.fetchConnection("me/feed", Post.class, Parameter.with("fields",
					"likes.summary(true),comments.summary(true),message,shares,created_time,picture"));
			Thread updater = new Thread(getPagesList);
			updater.start();
			updater.join();
		} catch (FacebookNetworkException e) {
			System.out.println("System is Offline, using backup data");
			throw e;
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			return getPostsByPage();
		}
	}

	/**
	 * Returns all non-null message posts from user feed based on a time filter, from pagesList. 
	 * 
	 * @return List where all non-null message posts from user feed are
	 *         included(with time filter).
	 */
	public List<Post> getPostsByPage() {
		List<Post> posts = new LinkedList<>();
		if (desiredPage <= pagesList.size()-1) {
			if (!(timeFilter.equals(TimeFilter.ALL_TIME))) {
				for (Post rPost : pagesList.get(desiredPage)) {
					if (rPost.getMessage() != null) {
						long timeDiff = ((timeFilter.getDate() / (24 * 60 * 60 * 1000))
								- (rPost.getCreatedTime().getTime() / (24 * 60 * 60 * 1000)));
						if (timeDiff >= 0 && timeDiff <= timeFilter.getDif()) {
							posts.add(rPost);
						}
					}
				}
				if (posts.isEmpty()) {
					incrementDesiredPage();
					getPostsByPage();
				}

			} else {
				for (Post rPost : pagesList.get(desiredPage)) {
					if (rPost.getMessage() != null) {
						posts.add(rPost);

					}
				}
			}
		}
		return posts;
	}

	/**
	 * Returns all non-null message posts from user feed, based on a text and time
	 * filter
	 * 
	 * @return List where all non-null message posts based on a text and time filter
	 *         are included, from user feed(also filtered with time filter).
	 * @throws FacebookNetworkException when system is offline, returning posts from
	 *                                  previous list(with text and time filter)
	 * @param filter chosen by user to get specific posts.
	 */

	@SuppressWarnings("finally")
	public List<Post> getTimeline(String filter) {
		resetDesiredPage();
		try {
			result = fbClient.fetchConnection("me/feed", Post.class, Parameter.with("fields",
					"likes.summary(true),comments.summary(true),message,shares,created_time,picture"));
			Thread updater = new Thread(getPagesList);
			updater.start();
			updater.join();
		} catch (FacebookNetworkException e) {
			System.out.println("System is Offline, using backup data");
			throw e;
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			return getPostsByPage(filter);
		}

	}

	
	/**
	 * Returns all non-null message posts from user feed based on a time filter and text filter, from pagesList. 
	 * 
	 * @return List where all non-null message posts from user feed are
	 *         included(with time and text filter).
	 * @param filter chosen by user        
	 */
	public List<Post> getPostsByPage(String filter) {
		List<Post> posts = new LinkedList<>();
		if (desiredPage <= pagesList.size()-1) {
			if (!(timeFilter.equals(TimeFilter.ALL_TIME))) {
				for (Post rPost : pagesList.get(desiredPage)) {
					if (rPost.getMessage() != null) {
						long timeDiff = ((timeFilter.getDate() / (24 * 60 * 60 * 1000))
								- (rPost.getCreatedTime().getTime() / (24 * 60 * 60 * 1000)));
						if (timeDiff >= 0 && timeDiff <= timeFilter.getDif() && rPost.getMessage().contains(filter)) {
							posts.add(rPost);
						}
					}
				}

			} else {
				for (Post rPost : pagesList.get(desiredPage)) {
					if (rPost.getMessage() != null && rPost.getMessage().contains(filter)) {
						posts.add(rPost);
					}
				}
			}
			if (posts.isEmpty()) {
				incrementDesiredPage();
				getPostsByPage(filter);
			}
		}
		return posts;
	}

	/**
	 * return user
	 * 
	 * @return String that equals user's name
	 */
	public User getUser() {
		return me;
	}
	
	/**
	 * return user's name
	 * 
	 * @return String that equals user's name
	 */
	public String getUserName() {
		return me.getName();
	}

	/**
	 * Sets specific time filter
	 * 
	 * @param timeFilter chosen by user.
	 */
	public void setTimeFilter(TimeFilter timeFilter) {
		this.timeFilter = timeFilter;
	}
	
	/**
	 * returns current time filter
	 * 
	 * @return current time filter
	 */
	public TimeFilter getTimeFilter() {
		return timeFilter;
	}

	/**
	 * Resets the desiredPage(Paging mechanism)
	 */
	private void resetDesiredPage() {
		desiredPage = 0;
	}

	/**
	 * Increments the desiredPage(Paging mechanism)
	 */
	public void incrementDesiredPage() {
		desiredPage++;
	}
}
