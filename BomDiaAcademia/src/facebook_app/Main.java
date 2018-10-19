
package facebook_app;

import java.util.ArrayList;
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

	static String accessToken;
	static FacebookClient fbClient;
	static User me ;

	public static void main(String[] args) {

		// ljcra-iscteiul access token //
		
		accessToken = "EAAQSXbaizwwBAK2M96ZBGFaYFw7Ez8yaZAM6Ap9ZB1Lrou1kKi2PoGE1E96CUNE4pVokBfhAgZCIucDlb7r9J45O9mCkZAxZAZA9GIvWE27ZB8wtS6CUhIwjcoPVNGaChu87YH04taeksWTcmHuBKHI2M6oZAOR6EdYWTn1ka791HFwZDZD";
		fbClient = new DefaultFacebookClient(accessToken);
		me = fbClient.fetchObject("me", User.class);

		System.out.println(me.getName());
	}
	
	public List<Post> searchKeyword(String keyword){
		Connection<Post> result = fbClient.fetchConnection("me/feed", Post.class);
		List<Post> postsResult = new ArrayList<>();
		
		for (List<Post> page : result) {
			for (Post rPost : page) {
				if( rPost.getMessage()!= null && rPost.getMessage().contains(keyword)) {
					postsResult.add(rPost);
				}
			}	
		}
		return postsResult;
	}	

}
