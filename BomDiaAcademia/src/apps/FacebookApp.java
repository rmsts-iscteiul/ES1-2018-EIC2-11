
package apps;

import java.util.LinkedList;
import java.util.List;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.types.Post;
import com.restfb.types.User;

public class FacebookApp {

	private static FacebookClient fbClient;
	private static User me;

	@SuppressWarnings("deprecation")
	public FacebookApp() {
		String accessToken = "EAAQSXbaizwwBAK2M96ZBGFaYFw7Ez8yaZAM6Ap9ZB1Lrou1kKi2PoGE1E96CUNE4pVokBfhAgZCIucDlb7r9J45O9mCkZAxZAZA9GIvWE27ZB8wtS6CUhIwjcoPVNGaChu87YH04taeksWTcmHuBKHI2M6oZAOR6EdYWTn1ka791HFwZDZD";
		fbClient = new DefaultFacebookClient(accessToken);

		me = fbClient.fetchObject("me", User.class);
	}

	public List<Post> getTimeline() {
		List<Post> posts = new LinkedList<>();
		try {
			Connection<Post> result = fbClient.fetchConnection("me/feed", Post.class);
			for (List<Post> page : result) {
				for (Post rPost : page) {
					if (rPost.getMessage() != null) {
						posts.add(rPost);
					}
				}
			}

		} catch (Exception e) { // When Facebook service or network is unavailable
			System.out.println(e.getMessage());
		}
		return posts;
	}

	public List<Post> getTimeline(String filter) {
		List<Post> posts = new LinkedList<>();
		try {
			Connection<Post> result = fbClient.fetchConnection("me/feed", Post.class);
			for (List<Post> page : result) {
				for (Post rPost : page) {
					if (rPost.getMessage() != null && rPost.getMessage().contains(filter)) {
						posts.add(rPost);
					}
				}
			}

		} catch (Exception e) { // When Facebook service or network is unavailable
			System.out.println(e.getMessage());
		}
		return posts;
	}

	public String getUser() {
		return me.getName();
	}

}
