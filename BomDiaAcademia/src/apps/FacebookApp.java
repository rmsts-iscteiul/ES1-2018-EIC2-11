
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
	private TimeFilter timeFilter = TimeFilter.LAST_YEAR;
	private String textFilter;
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
		String accessToken = "EAAD4C79u9UYBAKK5kALF2cxjaebseMLiSzyK6WIE3Y6iPfZBfo7164b0ZBLe9mHC1hKI7ZBjHcbr5Bef8DfzaizSZCpJBNK5SPRkTEHYGoGPSsZCVyhmGjTWELopC2c4LhLcFZAZBWia1rRJie2uR1WGgKKA301SaaZB2d3XpAW5ZAgZDZD\r\n";
		fbClient = new DefaultFacebookClient(accessToken);

		me = fbClient.fetchObject("me", User.class, Parameter.with("fields", "picture"));
	}

	public enum TimeFilter {
		LAST_HOUR(1 / 24), LAST_24H(1), LAST_WEEK(7), LAST_MONTH(30), LAST_YEAR(365), ALL_TIME, SPECIFIC_DAY();

		@SuppressWarnings("unused")
		private int dif;

		TimeFilter(int dif) {
			this.dif = dif;

		}

		TimeFilter() {

		}
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
		List<Post> posts = new LinkedList<>();
		try {
			result = fbClient.fetchConnection("me/feed", Post.class,
					Parameter.with("fields", "likes.summary(true),comments.summary(true),message,shares,created_time"));
			new Thread(updateOfflineList).start();
		} catch (FacebookNetworkException e) {
			System.out.println("System is Offline, using backup data");
			return getTimelineWithTime(offlineList);
		}
		return getTimelineWithTime(posts);
	}

	public List<Post> getTimelineWithTime(List<Post> posts) {
		Calendar calendar = Calendar.getInstance();
		Date date = calendar.getTime();
		if (!(timeFilter.equals(TimeFilter.ALL_TIME))) {
			for (List<Post> page : result) {
				for (Post rPost : page) {
					if (rPost.getMessage() != null) {
						long timeDiff = (Math.abs(date.getTime() - rPost.getCreatedTime().getTime())
								/ (24 * 60 * 60 * 1000));
						if (timeDiff <= timeFilter.dif) {
							posts.add(rPost);
						}
					}
				}

			}
		} else {
			for (List<Post> page : result) {
				for (Post rPost : page) {
					if (rPost.getMessage() != null) {
						posts.add(rPost);
					}
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
		List<Post> posts = new LinkedList<>();
		try {
			result = fbClient.fetchConnection("me/feed", Post.class,
					Parameter.with("fields", "likes.summary(true),comments.summary(true),message,shares,created_time"));
			new Thread(updateOfflineList).start();
		} catch (FacebookNetworkException e) {
			System.out.println("System is Offline, using backup data");
			return getTimelineWithTimeFilter(offlineList, filter);
		}
		return getTimelineWithTimeFilter(posts, filter);
	}

	public List<Post> getTimelineWithTimeFilter(List<Post> posts, String filter) {
		Calendar calendar = Calendar.getInstance();
		Date date = calendar.getTime();
		if (!(timeFilter.equals(TimeFilter.ALL_TIME))) {
			for (List<Post> page : result) {
				for (Post rPost : page) {
					if (rPost.getMessage() != null) {
						long timeDiff = (Math.abs(date.getTime() - rPost.getCreatedTime().getTime())
								/ (24 * 60 * 60 * 1000));
						if (timeDiff <= timeFilter.dif && rPost.getMessage().contains(filter)) {
							posts.add(rPost);
						}
					}
				}

			}
		} else {
			for (List<Post> page : result) {
				for (Post rPost : page) {
					if (rPost.getMessage() != null && rPost.getMessage().contains(filter)) {
						posts.add(rPost);
					}
				}
			}
		}
		return posts;
	}

//	public List<Post> getOffline(List<Post> posts, String filter) {
//		for (Post rPost : offlineList) {
//			if (rPost.getMessage() != null && rPost.getMessage().contains(filter)) {
//				posts.add(rPost);
//			}
//		}
//		return posts;
//	}

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

	public static void main(String[] args) {
		FacebookApp a = new FacebookApp();
		a.getTimeline();
	}

}
