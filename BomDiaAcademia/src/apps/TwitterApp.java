package apps;

import java.util.LinkedList;
import java.util.List;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterApp {

	private String user = "ISCTEIUL";
	private Twitter twitter;

	public TwitterApp() {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true).setOAuthConsumerKey("GahzkZi50ZuasuqqkRqckC2ln")
				.setOAuthConsumerSecret("Rr3Q7ivhL3HiEKVoP3d2aLwNbh6ez5rTdEy3MtNRd6yeC5vNVV")
				.setOAuthAccessToken("3362933517-swAMAbDiMufcuyxHUEpkSYUJ3JY9ANIrqT5yirP")
				.setOAuthAccessTokenSecret("onDIulYjcQCvb3rVk6N3cYp0DxytW0ew86fM2Kyp8JQOj");
		TwitterFactory tf = new TwitterFactory(cb.build());
		twitter = tf.getInstance();

	}

	public List<Status> getTimeline(String user) { // The argument need to be the @ of the user. DON'T
															// WRITE THE '@'
		try {
			this.user = user;
			return (List<Status>) twitter.getUserTimeline(user); 

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null; // when Twitter service or network is unavailable this will return a null LIST
	}

	public List<Status> getTimeline(String user, String filter) {
		List<Status> statuses = new LinkedList<>();
		this.user = user;
		try {
			for (Status status : twitter.getUserTimeline(user)) {
				if (status.getText().contains(filter)) {
					statuses.add(status);
				}
			}
		} catch (Exception e) { // when Twitter service or network is unavailable
			System.out.println(e.getMessage());
		}
		return statuses; 

	}
	
	public String getUser(){
		return user;
	}
	
	
	//-------------------------Test-------------------------------//
	
//	public static void main(String[] args) {
//		TwitterAPI t = new TwitterAPI();
//		List<Status> s= t.showTimeline("ISCTEIUL", "vid");
//		for (Status status : s) {
//			System.out.println(status.getText());
//		}
//	}

}