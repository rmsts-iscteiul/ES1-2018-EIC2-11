
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
		fbClient = new DefaultFacebookClient(accessToken);

		me = fbClient.fetchObject("me", User.class, Parameter.with("fields", "picture"));
	}

	/**
	 * Returns all non-null message posts from user feed
	 * 
	 * @throws FacebookNetworkException when system is offline, returning posts from
	 *                                  previous list
	 * @return List where all non-null message posts from user feed are included.
	 */

	@SuppressWarnings("finally")
	public List<Post> getTimeline() {
		List<Post> posts = new LinkedList<>();
		try {
			result = fbClient.fetchConnection("me/feed", Post.class,
					Parameter.with("fields", "likes.summary(true),comments.summary(true),message,shares"));
		} catch (FacebookNetworkException e) {
			System.out.println("System is Offline");
		} finally {
			for (List<Post> page : result) {
				for (Post rPost : page) {
					if (rPost.getMessage() != null) {
						posts.add(rPost);
					}
				}
			}
			return posts;
		}
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
					Parameter.with("fields", "likes.summary(true),comments.summary(true),message,shares"));
		} catch (FacebookNetworkException e) {
			System.out.println("System is Offline");
		} finally {
			for (List<Post> page : result) {
				for (Post rPost : page) {
					if (rPost.getMessage() != null && rPost.getMessage().contains(filter)) {
						posts.add(rPost);
					}
				}
			}
			return posts;
		}
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

}
