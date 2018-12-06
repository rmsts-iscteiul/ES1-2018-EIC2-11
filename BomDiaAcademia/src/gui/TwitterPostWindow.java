package gui;

import apps.TwitterApp;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import twitter4j.MediaEntity;
import twitter4j.Status;

public class TwitterPostWindow {
	private Stage twitter_post_stage;
	private Scene twitter_post_scene;
	private BorderPane twitter_post_root_pane;
	private HBox twitter_post_window_root_pane;
	private BorderPane view_twitter_post_window_root_pane = null;
	private BorderPane write_twitter_post_window_root_pane = null;

	private FlowPane write_twitter_post_container;
	private BorderPane view_twitter_post_container;

	private MediaPlayer twitter_post_media_player;

	private TwitterApp twitter_app;
	private Status status;

	private TextArea write_twitter_post_text_area;

	private static final int TWITTER_POST_SHADOW_GAP = 10;
	private static final int TWITTER_POST_ROOT_PANE_WIDTH = 900;
	private static final int TWITTER_POST_ROOT_PANE_HEIGHT = 600;
	private static final int VIEW_TWITTER_POST_ROOT_PANE_WIDTH = 400;
	private static final int VIEW_TWITTER_POST_ROOT_PANE_HEIGHT = 600;
	private static final int WRITE_TWITTER_POST_ROOT_PANE_WIDTH = 300;
	private static final int WRITE_TWITTER_POST_ROOT_PANE_HEIGHT = 420;
	private static final int EMAIL_TWITTER_WINDOW_TOP_BAR_HEIGHT = 20;

	private double xOffset, yOffset;

	public TwitterPostWindow(Stage main_stage, TwitterApp twitter_app, Status status) {
		this.twitter_app = twitter_app;
		this.status = status;
		configureTwitterPostStage(main_stage);
		createTwitterPostRootPane();
		buildViewTwitterPostWindowRootPane();
		buildViewTwitterPostContent();
		buildTwitterPostScene();
		startTwitterPostStage();
	}

	public TwitterPostWindow(Stage main_stage, TwitterApp twitter_app) {
		this.twitter_app = twitter_app;
		configureTwitterPostStage(main_stage);
		createTwitterPostRootPane();
		buildWriteTwitterPostWindowRootPane("");
		buildWriteTwitterPostContent("");
		buildTwitterPostScene();
		startTwitterPostStage();
	}

	private void configureTwitterPostStage(Stage main_stage) {
		twitter_post_stage = new Stage();
		twitter_post_stage.initModality(Modality.NONE);
		twitter_post_stage.initOwner(main_stage);
		twitter_post_stage.initStyle(StageStyle.TRANSPARENT);
	}

	private void createTwitterPostRootPane() {
		BorderPane twitter_post_root_pane = new BorderPane();
		this.twitter_post_root_pane = twitter_post_root_pane;
		twitter_post_root_pane.setPrefSize(TWITTER_POST_ROOT_PANE_WIDTH + TWITTER_POST_SHADOW_GAP,
				TWITTER_POST_ROOT_PANE_HEIGHT + TWITTER_POST_SHADOW_GAP);
		twitter_post_root_pane.setMaxSize(TWITTER_POST_ROOT_PANE_WIDTH + TWITTER_POST_SHADOW_GAP,
				TWITTER_POST_ROOT_PANE_HEIGHT + TWITTER_POST_SHADOW_GAP);
		twitter_post_root_pane.setId("twitter_post_root_pane");
		HBox top_gap_pane = new HBox();
		top_gap_pane.setPrefSize(TWITTER_POST_ROOT_PANE_WIDTH + TWITTER_POST_SHADOW_GAP, TWITTER_POST_SHADOW_GAP);
		top_gap_pane.setId("twitter_post_shadow_gap");
		HBox bottom_gap_pane = new HBox();
		bottom_gap_pane.setPrefSize(TWITTER_POST_ROOT_PANE_WIDTH + TWITTER_POST_SHADOW_GAP, TWITTER_POST_SHADOW_GAP);
		bottom_gap_pane.setId("email_post_shadow_gap");
		VBox left_gap_pane = new VBox();
		left_gap_pane.setPrefSize(TWITTER_POST_SHADOW_GAP, TWITTER_POST_ROOT_PANE_HEIGHT + TWITTER_POST_SHADOW_GAP);
		left_gap_pane.setId("email_post_shadow_gap");
		VBox right_gap_pane = new VBox();
		right_gap_pane.setPrefSize(TWITTER_POST_SHADOW_GAP, TWITTER_POST_ROOT_PANE_HEIGHT + TWITTER_POST_SHADOW_GAP);
		right_gap_pane.setId("email_post_shadow_gap");
		twitter_post_root_pane.setTop(top_gap_pane);
		twitter_post_root_pane.setBottom(bottom_gap_pane);
		twitter_post_root_pane.setLeft(left_gap_pane);
		twitter_post_root_pane.setRight(right_gap_pane);
		twitter_post_root_pane.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				xOffset = twitter_post_stage.getX() - event.getScreenX();
				yOffset = twitter_post_stage.getY() - event.getScreenY();
			}
		});
		twitter_post_root_pane.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				twitter_post_stage.setX(event.getScreenX() + xOffset);
				twitter_post_stage.setY(event.getScreenY() + yOffset);
			}
		});

		twitter_post_window_root_pane = new HBox();
		twitter_post_window_root_pane.setId("twitter_post_window_root_pane");
		twitter_post_root_pane.setCenter(twitter_post_window_root_pane);
	}

	private void buildTwitterPostScene() {
		twitter_post_scene = new Scene(twitter_post_root_pane, TWITTER_POST_ROOT_PANE_WIDTH,
				TWITTER_POST_ROOT_PANE_HEIGHT);
		twitter_post_scene.getStylesheets().add("/resources/css/twitter_post_window.css");
		twitter_post_scene.setFill(null);
	}

	private void buildViewTwitterPostWindowRootPane() {
		view_twitter_post_window_root_pane = new BorderPane();
		view_twitter_post_window_root_pane.setId("view_twitter_post_window_root_pane");
		view_twitter_post_window_root_pane.setPrefSize(VIEW_TWITTER_POST_ROOT_PANE_WIDTH,
				VIEW_TWITTER_POST_ROOT_PANE_HEIGHT);
		view_twitter_post_window_root_pane.setMaxSize(VIEW_TWITTER_POST_ROOT_PANE_WIDTH,
				VIEW_TWITTER_POST_ROOT_PANE_HEIGHT);
		twitter_post_window_root_pane.getChildren().add(view_twitter_post_window_root_pane);
		buildViewTwitterPostWindowTopBar();
	}

	private void buildWriteTwitterPostWindowRootPane(String what_called) {
		write_twitter_post_window_root_pane = new BorderPane();
		write_twitter_post_window_root_pane.setId("write_twitter_post_window_root_pane");
		write_twitter_post_window_root_pane.setPrefSize(WRITE_TWITTER_POST_ROOT_PANE_WIDTH,
				WRITE_TWITTER_POST_ROOT_PANE_HEIGHT);
		write_twitter_post_window_root_pane.setMaxSize(WRITE_TWITTER_POST_ROOT_PANE_WIDTH,
				WRITE_TWITTER_POST_ROOT_PANE_HEIGHT);
		twitter_post_window_root_pane.getChildren().add(write_twitter_post_window_root_pane);
		if (what_called.equals("R")) {
			buildWriteTwitterPostWindowTopBar(true, what_called);
			buildWriteTwitterPostContent("R");
		} else if (what_called.equals("RT")) {
			buildWriteTwitterPostWindowTopBar(true, what_called);
			buildWriteTwitterPostContent("RT");
		} else {
			buildWriteTwitterPostWindowTopBar(false, what_called);
			buildWriteTwitterPostContent("");
		}
	}

	private void buildViewTwitterPostWindowTopBar() {
		VBox view_twitter_post_window_top_bar = new VBox();
		view_twitter_post_window_top_bar.setId("view_twitter_post_window_top_bar");
		view_twitter_post_window_top_bar.setPrefSize(VIEW_TWITTER_POST_ROOT_PANE_WIDTH,
				EMAIL_TWITTER_WINDOW_TOP_BAR_HEIGHT);
		view_twitter_post_window_top_bar.setMaxSize(VIEW_TWITTER_POST_ROOT_PANE_WIDTH,
				EMAIL_TWITTER_WINDOW_TOP_BAR_HEIGHT);
		HBox twitter_post_window_top_bar_buttons_container = new HBox();
		twitter_post_window_top_bar_buttons_container.setId("twitter_post_window_top_bar_buttons_container");
		Label subject_label = new Label(status.getInReplyToScreenName());
		subject_label.setId("subject_label");
		Button close_button = new Button();
		close_button.setId("twitter_post_window_top_bar_close_button");
		close_button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				twitter_post_stage.close();
			}
		});
		final Pane spacer = new Pane();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		twitter_post_window_top_bar_buttons_container.getChildren().addAll(subject_label, spacer, close_button);
		view_twitter_post_window_top_bar.getChildren().addAll(twitter_post_window_top_bar_buttons_container);
		view_twitter_post_window_root_pane.setTop(view_twitter_post_window_top_bar);
	}

	private void buildWriteTwitterPostWindowTopBar(boolean can_return, String what_called) {
		VBox write_twitter_post_window_top_bar = new VBox();
		write_twitter_post_window_top_bar.setId("write_twitter_post_window_top_bar");
		write_twitter_post_window_top_bar.setPrefSize(WRITE_TWITTER_POST_ROOT_PANE_WIDTH,
				EMAIL_TWITTER_WINDOW_TOP_BAR_HEIGHT);
		write_twitter_post_window_top_bar.setMaxSize(WRITE_TWITTER_POST_ROOT_PANE_WIDTH,
				EMAIL_TWITTER_WINDOW_TOP_BAR_HEIGHT);
		HBox twitter_post_window_top_bar_buttons_container = new HBox();
		twitter_post_window_top_bar_buttons_container.setId("twitter_post_window_top_bar_buttons_container");
		// TOP
		HBox view_twitter_post_top_container = new HBox();
		view_twitter_post_top_container.setId("view_twitter_post_top_container");
		// Return
		if (can_return) {
			Button return_button = new Button("Return");
			return_button.setId("return_button");
			return_button.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent mouseEvent) {
					if (!view_twitter_post_window_root_pane.isVisible()) {
						if (new PopUpWindow(twitter_post_stage, PopUpType.CONFIRMATION,
								"Are you sure you want to discard this tweet?").getConfirmation()) {
							twitter_post_window_root_pane.getChildren().remove(write_twitter_post_window_root_pane);
							twitter_post_stage.close();
						}
					} else {
						write_twitter_post_window_root_pane.setVisible(false);
					}

				}
			});
			twitter_post_window_top_bar_buttons_container.getChildren().addAll(return_button);
		}
		// Tweet
		Button tweet_button;
		if (what_called.equals("R")) {
			tweet_button = new Button("Retweet");
			tweet_button.setId("retweet_button");
			tweet_button.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent mouseEvent) {
					if (write_twitter_post_text_area.getText().equals("Add a comment about this tweet...")
							|| write_twitter_post_text_area.getText().equals("")) {
						twitter_app.retweet(status);
					} else {
						twitter_app.retweet(write_twitter_post_text_area.getText(), status);
					}
				}
			});
		} else if (what_called.equals("RT")) {
			tweet_button = new Button("Reply");
			tweet_button.setId("reply_button");
			tweet_button.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent mouseEvent) {
					if (write_twitter_post_text_area.getText().equals("Add your answer to this tweet...")
							|| write_twitter_post_text_area.getText().equals("")) {
						new PopUpWindow(twitter_post_stage, PopUpType.WARNING,
								"Please add a comment to reply this tweet");
					} else {
						twitter_app.replyTo(write_twitter_post_text_area.getText(), status);
					}
				}
			});
		} else {
			tweet_button = new Button("Tweet");
			tweet_button.setId("tweet_button");
			tweet_button.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent mouseEvent) {
					if (write_twitter_post_text_area.getText().equals("Tweet about whats happening right now...")
							|| write_twitter_post_text_area.getText().equals("")) {
						new PopUpWindow(twitter_post_stage, PopUpType.WARNING,
								"You first need to write something to tweet");
					} else {
						twitter_app.tweet(write_twitter_post_text_area.getText());
					}
				}
			});
		}
		// Remove
		Button remove_button = new Button("Remove");
		remove_button.setId("remove_button");
		remove_button.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (new PopUpWindow(twitter_post_stage, PopUpType.CONFIRMATION,
						"Are you sure you want to discard this tweet?").getConfirmation()) {
					twitter_post_window_root_pane.getChildren().remove(write_twitter_post_window_root_pane);
					write_twitter_post_window_root_pane = null;
				}
			}
		});
		view_twitter_post_top_container.getChildren().addAll(remove_button, tweet_button);
		final Pane spacer = new Pane();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		twitter_post_window_top_bar_buttons_container.getChildren().addAll(spacer, view_twitter_post_top_container);
		write_twitter_post_window_top_bar.getChildren().addAll(twitter_post_window_top_bar_buttons_container);
		write_twitter_post_window_root_pane.setTop(write_twitter_post_window_top_bar);
	}

	private void buildViewTwitterPostContent() {
		view_twitter_post_container = new BorderPane();
		view_twitter_post_container.setId("view_twitter_post_container");
		ScrollPane view_twitter_post_scroll_pane = new ScrollPane();
		// TOP
		HBox view_twitter_post_top_container = new HBox();
		view_twitter_post_top_container.setId("view_twitter_post_top_container");
		// Return
		Button return_button = new Button("Return");
		return_button.setId("return_button");
		return_button.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (write_twitter_post_window_root_pane == null) {
					twitter_post_stage.close();
				} else {
					if (write_twitter_post_window_root_pane.isVisible()) {
						view_twitter_post_window_root_pane.setVisible(false);
					}
				}
			}
		});
		// Reply to
		Button reply_button = new Button("Reply");
		reply_button.setId("reply_button");
		reply_button.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (write_twitter_post_window_root_pane == null) {
					try {
						buildWriteTwitterPostWindowRootPane("RT");
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					write_twitter_post_window_root_pane.setVisible(true);
				}
			}
		});
		// Retweet
		Button retweet_button = new Button("Retweet");
		retweet_button.setId("retweet_button");
		retweet_button.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (write_twitter_post_window_root_pane == null) {
					buildWriteTwitterPostWindowRootPane("R");
				} else {
					write_twitter_post_window_root_pane.setVisible(true);
				}
			}
		});
		final Pane spacer = new Pane();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		view_twitter_post_top_container.getChildren().addAll(return_button, spacer, reply_button, retweet_button);
		view_twitter_post_container.setTop(view_twitter_post_top_container);
		// CENTER
		BorderPane twitter_post_container = new BorderPane();
		twitter_post_container.setId("twitter_post_container");

		// Top
		VBox twitter_post_left_container = new VBox();
		twitter_post_left_container.setId("twitter_post_top_container");
		ImageView cover_image = new ImageView(new Image(status.getUser().getProfileBanner300x100URL()));
		cover_image.setId("cover_image");
		ImageView profile_image = new ImageView(new Image(status.getUser().getProfileImageURL()));
		profile_image.setId("profile_image");
		profile_image.setFitWidth(80);
		profile_image.setFitHeight(80);
		Label profile_image_label = new Label(status.getUser().getName());
		profile_image_label.setId("profile_image_label");
		twitter_post_left_container.getChildren().addAll(cover_image, profile_image, profile_image_label);
		twitter_post_container.setTop(twitter_post_left_container);

		// Center
		VBox twitter_post_center_container = new VBox();
		twitter_post_center_container.setId("twitter_post_center_container");
		Text twitter_post_text = new Text(status.getText());
		twitter_post_text.setId("twitter_post_text");
		twitter_post_text.setWrappingWidth(VIEW_TWITTER_POST_ROOT_PANE_WIDTH * 0.6);
		twitter_post_center_container.getChildren().add(twitter_post_text);
		twitter_post_container.setCenter(twitter_post_center_container);

		// Right
		VBox twitter_post_right_container = new VBox();
		twitter_post_right_container.setId("twitter_post_right_container");
		Label favourites_label = new Label(status.getFavoriteCount() + "");
		if (status.isFavorited()) {
			favourites_label.setId("favourited_label");
		} else {
			favourites_label.setId("no_favourited_label");
		}
		favourites_label.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (status.isFavorited()) {
					twitter_app.favorite(status);
					favourites_label.setId("no_favourited_label");
				} else {
					favourites_label.setId("favourited_label");
					twitter_app.favorite(status);
				}
			}
		});
		Label retweets_label = new Label(status.getRetweetCount() + "");
		retweets_label.setId("retweets_label");
		retweets_label.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				// To-Do
			}
		});
		twitter_post_right_container.getChildren().addAll(favourites_label, retweets_label);
		twitter_post_container.setRight(twitter_post_right_container);

		// Bottom
		HBox twitter_post_bottom_container = new HBox();
		twitter_post_bottom_container.setId("twitter_post_bottom_container");
		MediaEntity[] media = status.getMediaEntities();
		for (MediaEntity m : media) {
			if (m.getVideoVariants().length != 0) {
				Media twitter_post_media = new Media(m.getVideoVariants()[0].getUrl());
				this.twitter_post_media_player = new MediaPlayer(twitter_post_media);
				this.twitter_post_media_player.setAutoPlay(true);
				MediaView twitter_post_media_view = new MediaView(twitter_post_media_player);
				twitter_post_bottom_container.getChildren().add(twitter_post_media_view);
			} else {
				ImageView img = new ImageView(new Image(m.getMediaURL()));
				twitter_post_bottom_container.getChildren().add(img);
			}
		}
		twitter_post_container.setBottom(twitter_post_bottom_container);
		view_twitter_post_container.setCenter(twitter_post_container);
		// BOTTOM
		FlowPane view_fill_bottom = new FlowPane();
		view_fill_bottom.setId("view_fill_bottom");
		view_fill_bottom.setMinHeight(EMAIL_TWITTER_WINDOW_TOP_BAR_HEIGHT);
		view_fill_bottom.setPrefHeight(EMAIL_TWITTER_WINDOW_TOP_BAR_HEIGHT);
		view_fill_bottom.setMaxHeight(EMAIL_TWITTER_WINDOW_TOP_BAR_HEIGHT);
		view_twitter_post_container.setBottom(view_fill_bottom);
		view_twitter_post_scroll_pane.setContent(view_twitter_post_container);
		view_twitter_post_window_root_pane.setCenter(view_twitter_post_scroll_pane);
	}

	private void buildWriteTwitterPostContent(String what_called) {
		write_twitter_post_container = new FlowPane(Orientation.VERTICAL);
		write_twitter_post_container.setId("write_twitter_post_container");
		if (what_called.equals("RT")) {
			HBox to_write_email_post_top_container = new HBox();
			to_write_email_post_top_container.setId("to_write_email_post_top_container");
			Label to_label = new Label("In response to " + status.getUser().getName());
			to_label.setId("to_label");
			to_write_email_post_top_container.getChildren().addAll(to_label);
			write_twitter_post_container.getChildren().add(to_write_email_post_top_container);
		}

		VBox write_twitter_post_text_area_container = new VBox();
		write_twitter_post_text_area_container.setId("write_twitter_post_text_area_container");
		if (what_called.equals("R")) {
			write_twitter_post_text_area = new TextArea("Add a comment about this tweet...");
			write_twitter_post_text_area.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					if (write_twitter_post_text_area.getText().equals("Add a comment about this tweet...")) {
						write_twitter_post_text_area.setText("");
					}
				}
			});
		} else if (what_called.equals("RT")) {
			write_twitter_post_text_area = new TextArea("Add your answer to this tweet...");
			write_twitter_post_text_area.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					if (write_twitter_post_text_area.getText().equals("Add your answer to this tweet...")) {
						write_twitter_post_text_area.setText("");
					}
				}
			});
		} else {
			write_twitter_post_text_area = new TextArea("Tweet about whats happening right now...");
			write_twitter_post_text_area.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					if (write_twitter_post_text_area.getText().equals("Tweet about whats happening right now...")) {
						write_twitter_post_text_area.setText("");
					}
				}
			});
		}
		write_twitter_post_text_area.setId("write_twitter_post_text_area");
		write_twitter_post_text_area_container.getChildren().add(write_twitter_post_text_area);
		write_twitter_post_container.getChildren().add(write_twitter_post_text_area_container);

		write_twitter_post_window_root_pane.setCenter(write_twitter_post_container);
	}

	private void startTwitterPostStage() {
		twitter_post_stage.setScene(twitter_post_scene);
		twitter_post_stage.showAndWait();
	}
}
