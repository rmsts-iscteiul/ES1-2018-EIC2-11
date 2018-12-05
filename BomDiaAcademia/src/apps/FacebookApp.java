
package apps;

import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
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
 */
public class FacebookApp {

	/**
	 * fbClient is to initiate communication with facebook trough accessToken
	 */
	private static FacebookClient fbClient;
	/**
	 * result is the list where posts are placed when fetching info from Facebook
	 */
	private Connection<Post> result;
	private List<Post> offlineList = new LinkedList<>();
	private TimeFilter timeFilter;
	private String userAccessToken;
//	private boolean textFilterChosen = false;

	/**
	 * Object fetched from facebook
	 */
	private static User me;

	/**
	 * Initilize app(fbClient) with given acessToken, and fetched "me" object from
	 * facebook.
	 */
	@SuppressWarnings("deprecation")
	public FacebookApp() {
		userAccessToken = "EAAD4C79u9UYBAKK5kALF2cxjaebseMLiSzyK6WIE3Y6iPfZBfo7164b0ZBLe9mHC1hKI7ZBjHcbr5Bef8DfzaizSZCpJBNK5SPRkTEHYGoGPSsZCVyhmGjTWELopC2c4LhLcFZAZBWia1rRJie2uR1WGgKKA301SaaZB2d3XpAW5ZAgZDZD\r\n";
		fbClient = new DefaultFacebookClient(userAccessToken);

		me = fbClient.fetchObject("me", User.class, Parameter.with("fields", "picture,name"));
	}

	/**
	 * Returns all non-null message posts from user feed
	 * 
	 * @throws FacebookNetworkException when system is offline, returning posts from
	 *                                  previous list
	 * @return List where all non-null message posts from user feed are included.
	 */
	Runnable updateOfflineList = new Runnable() {
		public void run() {
			for (List<Post> page : result) {
				for (Post rPost : page) {
					if (rPost.getMessage() != null) {
						offlineList.add(rPost);
					}
				}
			}
		}
	};

	@SuppressWarnings("finally")
	public List<Post> getTimeline() {
		try {
			result = fbClient.fetchConnection("me/feed", Post.class,
					Parameter.with("fields", "likes.summary(true),comments.summary(true),message,shares,created_time,picture"));
			Thread updater = new Thread(updateOfflineList);
			updater.start();
			updater.join();
		} catch (FacebookNetworkException e) {
			System.out.println("System is Offline, using backup data");
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			return getTimelineWithTime();
		}
	}

	public List<Post> getTimelineWithTime() {
		List<Post> posts = new LinkedList<>();
		Calendar calendar = Calendar.getInstance();
		Date date = calendar.getTime();
		if (!(timeFilter.equals(TimeFilter.ALL_TIME))) {
			for (Post rPost : offlineList) {
				if (rPost.getMessage() != null) {
					long timeDiff = (Math.abs(timeFilter.getDate() - rPost.getCreatedTime().getTime())
							/ (24 * 60 * 60 * 1000));
					if (timeDiff <= timeFilter.daysDif) {
						posts.add(rPost);
					}
				}
			}

		} else {
			for (Post rPost : offlineList) {
				if (rPost.getMessage() != null) {
					posts.add(rPost);
					System.out.println(rPost.getMessage());
					
				}
			}
		}
		return posts;
	}

	/**
	 * Returns all non-null message posts from user feed, based on a filter
	 * 
	 * @return List where all non-null message posts based on a filter are included,
	 *         from user feed.
	 * @throws FacebookNetworkException when system is offline, returning posts from
	 *                                  previous list(with filter)
	 * @param filter chosen by user to get specific posts.
	 */

	@SuppressWarnings("finally")
	public List<Post> getTimeline(String filter) {
		try {
			result = fbClient.fetchConnection("me/feed", Post.class,
					Parameter.with("fields", "likes.summary(true),comments.summary(true),message,shares,created_time,picture"));
			Thread updater = new Thread(updateOfflineList);
			updater.start();
			updater.join();
		} catch (FacebookNetworkException e) {
			System.out.println("System is Offline, using backup data");
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			return getTimelineWithTimeFilter(filter);
		}

	}

	public List<Post> getTimelineWithTimeFilter(String filter) {
		List<Post> posts = new LinkedList<>();
		Calendar calendar = Calendar.getInstance();
		Date date = calendar.getTime();
		if (!(timeFilter.equals(TimeFilter.ALL_TIME))) {
			for (Post rPost : offlineList) {
				if (rPost.getMessage() != null) {
					long timeDiff = (Math.abs(timeFilter.getDate() - rPost.getCreatedTime().getTime())
							/ (24 * 60 * 60 * 1000));
					if (timeDiff <= timeFilter.daysDif && rPost.getMessage().contains(filter)) {
						posts.add(rPost);
					}
				}
			}

		} else {
			for (Post rPost : offlineList) {
				if (rPost.getMessage() != null && rPost.getMessage().contains(filter)) {
					posts.add(rPost);
					System.out.println(rPost.getMessage());
				}
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
	public String getUsername() {
		return me.getName();
	}

	public void setTimeFilter(TimeFilter timeFilter) {
		this.timeFilter = timeFilter;
	}
}
