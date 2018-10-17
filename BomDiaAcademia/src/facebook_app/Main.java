
package facebook_app;

import java.util.List;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.FacebookType;
import com.restfb.types.Group;
import com.restfb.types.Post;
import com.restfb.types.User;

public class Main {

	public static void main(String[] args) {

		// ljcra-iscteiul access token //

		String accessToken = "EAAQSXbaizwwBAK2M96ZBGFaYFw7Ez8yaZAM6Ap9ZB1Lrou1kKi2PoGE1E96CUNE4pVokBfhAgZCIucDlb7r9J45O9mCkZAxZAZA9GIvWE27ZB8wtS6CUhIwjcoPVNGaChu87YH04taeksWTcmHuBKHI2M6oZAOR6EdYWTn1ka791HFwZDZD";

		FacebookClient fbClient = new DefaultFacebookClient(accessToken);

		User me = fbClient.fetchObject("me", User.class);

		System.out.println(me.getName());

		Connection<Post> result = fbClient.fetchConnection("me/feed", Post.class);

		for (List<Post> page : result) {

			for (Post rPost : page) {
				
				// Will print the post's message //
				System.out.println(rPost.getMessage());
				// Will print post's url from facebook //
				System.out.println("fb.com/" + rPost.getId());
			}

		}
		
		
		//    this is for publish things in my feed (not working)//

		FacebookType response = fbClient.publish("me/feed", FacebookType.class,
				Parameter.with("message", "O Rodrigo chega tarde para não trabalhar"));
		System.out.println("fb.com/" + response.getId());
		
		

		// This is for group feed //

		Connection<Group> result1 = fbClient.fetchConnection("me/groups", Group.class);
		for (List<Group> page1 : result1) {

			for (Group rGroup : page1) {
				// Will print the names of all groups from user //
				System.out.println(rGroup.getName());
				
				if (rGroup.getName().contains("UniHUB")) {
					
					// Will access group's page//
					Connection<Post> result3 = fbClient.fetchConnection(rGroup.getId() + "/feed", Post.class);
					
					for (List<Post> page3 : result3) {

						for (Post rPost2 : page3) {
							// Will print posts from chosen group (not working)//
							System.out.println(rPost2.getMessage());
						}
					}
				}
			}
		}

	}

}
