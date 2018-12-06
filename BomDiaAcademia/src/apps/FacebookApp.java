
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
	
	/**
	 * offline list updated from result list fetched from facebook
	 */
	private List<Post> offlineList = new LinkedList<>();
	
	/**
	 * Current time filter for the posts
	 */
	private TimeFilter timeFilter;

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
		String accessToken = "EAAQSXbaizwwBAK2M96ZBGFaYFw7Ez8yaZAM6Ap9ZB1Lrou1kKi2PoGE1E96CUNE4pVokBfhAgZCIucDlb7r9J45O9mCkZAxZAZA9GIvWE27ZB8wtS6CUhIwjcoPVNGaChu87YH04taeksWTcmHuBKHI2M6oZAOR6EdYWTn1ka791HFwZDZD";
//		String accessToken = "EAAD4C79u9UYBAKK5kALF2cxjaebseMLiSzyK6WIE3Y6iPfZBfo7164b0ZBLe9mHC1hKI7ZBjHcbr5Bef8DfzaizSZCpJBNK5SPRkTEHYGoGPSsZCVyhmGjTWELopC2c4LhLcFZAZBWia1rRJie2uR1WGgKKA301SaaZB2d3XpAW5ZAgZDZD";
		fbClient = new DefaultFacebookClient(accessToken);

		
		me = fbClient.fetchObject("me", User.class, Parameter.with("fields", "picture,name"));
		timeFilter = TimeFilter.ALL_TIME;
	}

	/**
	 * Runnable that updates offlineList, with the result list fetched from facebook
	 */
	Runnable updateOfflineList = new Runnable() {
		public void run() {
			resetOfflineList();
			for (List<Post> page : result) {
				for (Post rPost : page) {
					if (rPost.getMessage() != null) {
						offlineList.add(rPost);
					}
				}
			}
		}
	};

	/**
	 * Returns all non-null message posts from user feed based on a time filter
	 * 
	 * @throws FacebookNetworkException when system is offline, returning posts from previous list
	 * 
	 * @throws InterruptedException when updater thread is interrupted
	 * 
	 * @return List where all non-null message posts from user feed are included(with time filter).
	 */
	@SuppressWarnings("finally")
	public List<Post> getTimeline() {
		List<Post> posts = new LinkedList<>();
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

	/**
	 * Returns all non-null message posts from user feed based on a time filter
	 * 
	 * @return List where all non-null message posts from user feed are included(with time filter).
	 */
	public List<Post> getTimelineWithTime() {
		List<Post> posts = new LinkedList<>();
		if (!(timeFilter.equals(TimeFilter.ALL_TIME))) {
			for (Post rPost : offlineList) {
				if (rPost.getMessage() != null) {
					long timeDiff = (Math.abs(timeFilter.getDate() - rPost.getCreatedTime().getTime())
							/ (24 * 60 * 60 * 1000));
					if (timeDiff <= timeFilter.getDif()) {
						posts.add(rPost);
					}
				}
			}

		} else {
			for (Post rPost : offlineList) {
				if (rPost.getMessage() != null) {
					posts.add(rPost);
					
				}
			}
		}
		return posts;
	}

	/**
	 * Returns all non-null message posts from user feed, based on a text and time filter
	 * 
	 * @return List where all non-null message posts based on a text and time filter are included,
	 *         from user feed(also filtered with time filter).
	 * @throws FacebookNetworkException when system is offline, returning posts from
	 *                                  previous list(with text and time filter)
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
			return getTimelineWithTime(filter);
		}

	}
	
	/**
	 * Returns all non-null message posts from user feed, based on a text and time filter
	 * 
	 * @return List where all non-null message posts based on a Text and time filter are included,
	 *         from user feed.
	 *         
	 * @param filter chosen by user to get specific posts.
	 */
	public List<Post> getTimelineWithTime(String filter) {
		List<Post> posts = new LinkedList<>();
		if (!(timeFilter.equals(TimeFilter.ALL_TIME))) {
			for (Post rPost : offlineList) {
				if (rPost.getMessage() != null) {
					long timeDiff = (Math.abs(timeFilter.getDate() - rPost.getCreatedTime().getTime())
							/ (24 * 60 * 60 * 1000));
					if (timeDiff <= timeFilter.getDif() && rPost.getMessage().contains(filter)) {
						posts.add(rPost);
					}
				}
			}

		} else {
			for (Post rPost : offlineList) {
				if (rPost.getMessage() != null && rPost.getMessage().contains(filter)) {
					posts.add(rPost);
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
	 * Resets offlineList
	 */
	public void resetOfflineList() {
		offlineList.clear();
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
	 * @param time filter chosen by user.
	 */
	public void setTimeFilter(TimeFilter timeFilter) {
		this.timeFilter = timeFilter;
	}
}
