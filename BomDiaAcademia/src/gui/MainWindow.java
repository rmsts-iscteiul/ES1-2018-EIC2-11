package gui;

import java.util.List;

import javax.mail.Message;
import javax.mail.MessagingException;

import com.restfb.types.Post;

import apps.EmailApp;
import apps.FacebookApp;
import apps.TwitterApp;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import twitter4j.MediaEntity;
import twitter4j.Status;

/**
 * 
 * @author ES1-2018-EIC2-11 This class runs the graphical user interface.
 *
 */
public class MainWindow extends Application {

	private Stage main_stage;
	private Scene scene;
	private BorderPane root_pane;
	private FlowPane window_root_pane;
	private VBox window_left_menu;
	private BorderPane window_pane;
	private HBox apps_pane;
	private HBox window_top_bar;
	private BorderPane options_pane = null;

	private VBox twitter_app_pane = null;
	private VBox facebook_app_pane = null;
	private VBox email_app_pane = null;

	private TwitterApp twitter_app;
	private FacebookApp facebook_app;
	private EmailApp email_app;

	private double xOffset, yOffset;

	private static final int SHADOW_GAP = 10;
	private static final int WINDOW_ROOT_PANE_WIDTH = 1000;
	private static final int WINDOW_ROOT_PANE_HEIGHT = 600;
	private static final int WINDOW_LEFT_MENU_WIDTH = 50;
	private static final int WINDOW_TOP_BAR_HEIGHT = 20;

	private static final int POST_WIDTH = 300;

	private Service<Void> twitterThread;
	private Service<Void> facebookThread;
	private Service<Void> emailThread;

	/**
	 * This is the main method.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * This is the method which starts the graphical user interface on, using a
	 * stage.
	 * 
	 * @param main_stage
	 */
	@Override
	public void start(Stage main_stage) throws Exception {
		this.main_stage = main_stage;
		try {
			configureStage();
			createRootPane();
			buildWindowRootPane();
			buildScene();
			initApps();
			startStage();

			System.out.println("W -> " + window_pane.getWidth() + "\n H -> " + window_pane.getHeight());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method is used to configure the stage (main_stage).
	 */
	private void configureStage() {
		main_stage.setTitle("Bom dia Academia");
		main_stage.initStyle(StageStyle.TRANSPARENT);
	}

	/**
	 * This method is used to create and configure the root pane (root_pane).
	 * This pane is only used to configure the size of the window and to apply
	 * to it the shadow effect.
	 */
	private void createRootPane() {
		BorderPane root_pane = new BorderPane();
		this.root_pane = root_pane;
		root_pane.setPrefSize(WINDOW_ROOT_PANE_WIDTH + SHADOW_GAP, WINDOW_ROOT_PANE_HEIGHT + SHADOW_GAP);
		root_pane.setMaxSize(WINDOW_ROOT_PANE_WIDTH + SHADOW_GAP, WINDOW_ROOT_PANE_HEIGHT + SHADOW_GAP);
		root_pane.setId("root_pane");
		HBox top_gap_pane = new HBox();
		top_gap_pane.setPrefSize(WINDOW_ROOT_PANE_WIDTH + SHADOW_GAP, SHADOW_GAP);
		top_gap_pane.setId("shadow_gap");
		HBox bottom_gap_pane = new HBox();
		bottom_gap_pane.setPrefSize(WINDOW_ROOT_PANE_WIDTH + SHADOW_GAP, SHADOW_GAP);
		bottom_gap_pane.setId("shadow_gap");
		VBox left_gap_pane = new VBox();
		left_gap_pane.setPrefSize(SHADOW_GAP, WINDOW_ROOT_PANE_HEIGHT + SHADOW_GAP);
		left_gap_pane.setId("shadow_gap");
		VBox right_gap_pane = new VBox();
		right_gap_pane.setPrefSize(SHADOW_GAP, WINDOW_ROOT_PANE_HEIGHT + SHADOW_GAP);
		right_gap_pane.setId("shadow_gap");
		root_pane.setTop(top_gap_pane);
		root_pane.setBottom(bottom_gap_pane);
		root_pane.setLeft(left_gap_pane);
		root_pane.setRight(right_gap_pane);
	}

	/**
	 * This method is used to build the scene and applies to it the CSS files.
	 */
	private void buildScene() {
		scene = new Scene(root_pane);
		scene.getStylesheets().add("/resources/css/main_window.css");
		scene.getStylesheets().add("/resources/css/twitter_app.css");
		scene.getStylesheets().add("/resources/css/facebook_app.css");
		scene.getStylesheets().add("/resources/css/email_app.css");
		scene.getStylesheets().add("/resources/css/options_pane.css");
		scene.setFill(null);
	}

	/**
	 * This method is used to start the stage (main_stage). It must be the be
	 * the last method to be called in start method.
	 */
	private void startStage() {
		main_stage.setScene(scene);
		main_stage.show();
	}

	/**
	 * This method is used to build the working base pane (window_root_pane).
	 */
	private void buildWindowRootPane() {
		FlowPane window_root_pane = new FlowPane(Orientation.VERTICAL);
		this.window_root_pane = window_root_pane;
		window_root_pane.setId("window_root_pane");
		window_root_pane.setPrefSize(WINDOW_ROOT_PANE_WIDTH, WINDOW_ROOT_PANE_HEIGHT);
		window_root_pane.setMaxSize(WINDOW_ROOT_PANE_WIDTH, WINDOW_ROOT_PANE_HEIGHT);
		buildWindowRootLeftMenu();
		buildWindowPane();
		root_pane.setCenter(window_root_pane);
	}

	/**
	 * This method is used to build the left menu (window_left_menu).
	 */
	private void buildWindowRootLeftMenu() {
		window_left_menu = new VBox();
		window_left_menu.setPrefSize(WINDOW_LEFT_MENU_WIDTH, WINDOW_ROOT_PANE_HEIGHT);
		window_left_menu.setMaxSize(WINDOW_LEFT_MENU_WIDTH, WINDOW_ROOT_PANE_HEIGHT);
		window_left_menu.setId("window_left_menu");
		ToggleButton left_menu_facebook_toggle_button = new ToggleButton();
		left_menu_facebook_toggle_button.setId("left_menu_facebook_toggle_button");
		left_menu_facebook_toggle_button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				if (facebook_app_pane == null) { // If it's == null, we need to
													// start the thread.
					getFacebookTimeline(); // thread.start()
				} else {
					if (left_menu_facebook_toggle_button.isSelected())
						facebook_app_pane.setVisible(true);
					else
						facebook_app_pane.setVisible(false);
				}
			}
		});
		ToggleButton left_menu_twitter_toggle_button = new ToggleButton();
		left_menu_twitter_toggle_button.setId("left_menu_twitter_toggle_button");
		left_menu_twitter_toggle_button.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent actionEvent) {
				if (twitter_app_pane == null) { // If it's == null, we need to
												// start the thread.
					getTwitterTimeline(); // thread.start()
				} else {
					if (left_menu_twitter_toggle_button.isSelected())
						twitter_app_pane.setVisible(true);
					else
						twitter_app_pane.setVisible(false);

				}
			}
		});

		ToggleButton left_menu_email_toggle_button = new ToggleButton();
		left_menu_email_toggle_button.setId("left_menu_email_toggle_button");
		left_menu_email_toggle_button.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent actionEvent) {
				if (email_app_pane == null) {
					new LoginWindow(main_stage, email_app);
					if (!(email_app.getUser() == null)) {
						getEmailTimeline();
					}
				} else {
					if (left_menu_email_toggle_button.isSelected())
						email_app_pane.setVisible(true);
					else
						email_app_pane.setVisible(false);
				}
			}
		});

		ToggleButton left_menu_combine_apps_toggle_button = new ToggleButton();
		left_menu_combine_apps_toggle_button.setId("left_menu_combine_apps_toggle_button");
		left_menu_combine_apps_toggle_button.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent actionEvent) {

			}
		});

		window_left_menu.getChildren().addAll(left_menu_facebook_toggle_button, left_menu_twitter_toggle_button,
				left_menu_email_toggle_button, left_menu_combine_apps_toggle_button);
		window_root_pane.getChildren().add(window_left_menu);
	}

	/**
	 * This method is used to build the window pane(window_pane). This window is
	 * where it'll be added the top bar and in the center the Apps pane.
	 */
	private void buildWindowPane() {
		BorderPane window_pane = new BorderPane();
		this.window_pane = window_pane;
		window_pane.setId("window_pane");
		window_pane.setPrefSize(WINDOW_ROOT_PANE_WIDTH - WINDOW_LEFT_MENU_WIDTH, WINDOW_ROOT_PANE_HEIGHT);
		window_pane.setMaxSize(WINDOW_ROOT_PANE_WIDTH - WINDOW_LEFT_MENU_WIDTH, WINDOW_ROOT_PANE_HEIGHT);
		buildWindowTopBar();
		buildAppsPane();
		window_root_pane.getChildren().add(window_pane);
	}

	/**
	 * This method is used to build the window top bar (window_top_bar) which
	 * contains at the left the search bar and at the right the windows buttons,
	 * like the exit and minimize button.
	 */
	private void buildWindowTopBar() {
		window_top_bar = new HBox();
		window_top_bar.setId("window_top_bar");
		window_top_bar.setPrefSize(WINDOW_ROOT_PANE_WIDTH - WINDOW_LEFT_MENU_WIDTH, WINDOW_TOP_BAR_HEIGHT);
		window_top_bar.setMaxSize(WINDOW_ROOT_PANE_WIDTH - WINDOW_LEFT_MENU_WIDTH, WINDOW_TOP_BAR_HEIGHT);
		window_top_bar.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				xOffset = main_stage.getX() - event.getScreenX();
				yOffset = main_stage.getY() - event.getScreenY();
			}
		});
		window_top_bar.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				main_stage.setX(event.getScreenX() + xOffset);
				main_stage.setY(event.getScreenY() + yOffset);
			}
		});
		FlowPane window_top_bar_buttons_container = new FlowPane();
		window_top_bar_buttons_container.setId("window_top_bar_buttons_container");
		Button close_button = new Button();
		close_button.setId("window_top_bar_close_button");
		close_button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				Platform.exit();
			}
		});
		Button minimize_button = new Button();
		minimize_button.setId("window_top_bar_minimize_button");
		minimize_button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				main_stage.setIconified(true);
			}
		});
		Button options_button = new Button();
		options_button.setId("window_top_bar_options_button");
		options_button.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (options_pane == null) {
					buildOptionsPane();
				} else {
					if (!options_pane.isVisible()) {
						options_pane.setVisible(true);
					} else {
						options_pane.setVisible(false);
					}
				}
			}
		});
		window_top_bar_buttons_container.getChildren().addAll(options_button, minimize_button, close_button);
		window_top_bar.getChildren().addAll(createSearch(), window_top_bar_buttons_container);
		window_pane.setTop(window_top_bar);
	}

	/**
	 * This method is used to build the options pane (options_root_pane) which
	 * contains the users info, settings and the about button
	 */
	private void buildOptionsPane() {

		options_pane = new BorderPane();
		options_pane.setId("options_pane");
		options_pane.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				options_pane.setVisible(false);
			}
		});
		options_pane.setPrefSize(300, 200);
		options_pane.setMinSize(300, 200);
		options_pane.setLayoutX(window_pane.getWidth() - 360);
		options_pane.setLayoutY(window_top_bar.getHeight());

		BorderPane options_content_pane = new BorderPane();
		options_content_pane.setId("options_content_pane");

		// Users pane
		TilePane users_pane = new TilePane();
		users_pane.setId("users_pane");
		users_pane.setPrefColumns(5);
		Button add_user_button = new Button("Add user");
		add_user_button.setId("add_user_button");
		add_user_button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {

			}
		});
		users_pane.getChildren().add(add_user_button);
		options_content_pane.setCenter(users_pane);

		VBox bottom_options_container = new VBox();
		bottom_options_container.setId("bottom_options_container");
		ToggleButton dark_theme_toggle_button = new ToggleButton("Dark Theme");
		dark_theme_toggle_button.setId("dark_theme_toggle_button");
		dark_theme_toggle_button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				if (dark_theme_toggle_button.isSelected()) {
					window_pane.setId("window_pane_dt");
					window_left_menu.setId("window_left_menu_dt");
				} else {
					window_pane.setId("window_pane");
					window_left_menu.setId("window_left_menu");
				}
			}
		});
		Button settings_button = new Button("Settings");
		settings_button.setId("settings_button");
		Button about_button = new Button("About");
		about_button.setId("about_button");
		bottom_options_container.getChildren().addAll(new Separator(), dark_theme_toggle_button, settings_button,
				about_button);
		options_content_pane.setBottom(bottom_options_container);

		options_pane.setCenter(options_content_pane);
		window_pane.getChildren().add(options_pane);
	}

	/**
	 * This method is used to return a search bar, on the top of the window.
	 * 
	 * @return window_top_bar_search_container
	 */
	private FlowPane createSearch() {
		FlowPane window_top_bar_search_container = new FlowPane();
		window_top_bar_search_container.setId("window_top_bar_search_container");
		HBox app_check_pane = new HBox();
		app_check_pane.setId("app_check_pane");
		ToggleButton search_facebook_toggle_button = new ToggleButton();
		search_facebook_toggle_button.setId("search_facebook_toggle_button");
		ToggleButton search_twitter_toggle_button = new ToggleButton();
		search_twitter_toggle_button.setId("search_twitter_toggle_button");
		ToggleButton search_email_toggle_button = new ToggleButton();
		search_email_toggle_button.setId("search_email_toggle_button");
		app_check_pane.getChildren().addAll(search_facebook_toggle_button, search_twitter_toggle_button,
				search_email_toggle_button);
		HBox search_pane = new HBox();
		search_pane.setId("search_pane");
		TextField search_text_field = new TextField("Filter...");
		search_text_field.setId("search_text_field");
		search_text_field.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (search_text_field.getText().equals("Filter...")) {
					search_text_field.clear();
				}
			}
		});
		Button search_button = new Button();
		search_button.setId("search_button");
		search_button.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (search_twitter_toggle_button.isSelected()) {
					refreshTwitterApp(search_text_field.getText());
				}
				if (search_facebook_toggle_button.isSelected()) {
					refreshFacebookApp(search_text_field.getText());
				}
				if (!search_facebook_toggle_button.isSelected() && !search_twitter_toggle_button.isSelected()
						&& !search_email_toggle_button.isSelected()) {
					new PopUpWindow(main_stage, PopUpType.WARNING, "Please select an App to search.");
				}
			}
		});
		search_pane.getChildren().addAll(search_text_field, search_button);
		window_top_bar_search_container.getChildren().addAll(app_check_pane, search_pane);
		return window_top_bar_search_container;
	}

	/**
	 * This method is used to build the Apps pane (apps_pane). This pane is
	 * where the Facebook, Twitter and Email Apps is going to be added.
	 */
	private void buildAppsPane() {
		apps_pane = new HBox();
		apps_pane.setPrefSize(window_pane.getWidth(), (window_pane.getHeight() - window_top_bar.getHeight()));
		apps_pane.setMaxSize(window_pane.getMaxWidth(), (window_pane.getMaxHeight() - window_top_bar.getMaxHeight()));
		apps_pane.setId("apps_pane");
		window_pane.setCenter(apps_pane);
	}

	/**
	 * This method is used to build the Twitter App pane (twitter_app_pane),
	 * using a list of status. Note that this method doesn't set visible the
	 * App. It only build it back scene.
	 * 
	 * @param statuses
	 */
	private void buildTwitterApp(List<Status> statuses) {
		twitter_app_pane = new VBox();
		twitter_app_pane.setId("twitter_app_pane");

		HBox twitter_app_tool_bar = new HBox();
		twitter_app_tool_bar.setId("twitter_app_tool_bar");
		Label twitter_app_top_bar_icon = new Label("@" + twitter_app.getUser());
		twitter_app_top_bar_icon.setId("twitter_app_top_bar_icon");
		Button twitter_app_top_bar_refresh_button = new Button();
		twitter_app_top_bar_refresh_button.setId("twitter_app_top_bar_refresh_button");
		twitter_app_top_bar_refresh_button.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				refreshTwitterApp();
			}
		});
		final Pane spacer = new Pane();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		Button twitter_app_top_bar_change_user_button = new Button();
		twitter_app_top_bar_change_user_button.setId("twitter_app_top_bar_change_user_button");
		twitter_app_top_bar_change_user_button.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				HBox change_user_container = new HBox();
				change_user_container.setId("change_user_container");
				TextField change_user_text_field = new TextField("New user");
				change_user_text_field.setId("change_user_text_field");
				change_user_text_field.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent mouseEvent) {
						if (change_user_text_field.getText().equals("New user")) {
							change_user_text_field.clear();
						}
					}
				});
				Button change_user_confirm_button = new Button();
				change_user_confirm_button.setId("change_user_confirm_button");
				change_user_confirm_button.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent mouseEvent) {
						changeTwitterAppUser(change_user_text_field.getText());
					}
				});
				change_user_container.getChildren().addAll(change_user_text_field, change_user_confirm_button);
				twitter_app_tool_bar.getChildren().add(change_user_container);
			}
		});
		Button twitter_app_top_bar_minimize_button = new Button();
		twitter_app_top_bar_minimize_button.setId("facebook_app_top_bar_minimize_button");
		twitter_app_top_bar_minimize_button.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				twitter_app_pane.setVisible(false);
			}
		});
		twitter_app_tool_bar.getChildren().addAll(twitter_app_top_bar_icon, spacer, twitter_app_top_bar_refresh_button,
				twitter_app_top_bar_change_user_button, twitter_app_top_bar_minimize_button);
		ScrollPane twitter_app_scroll_pane = new ScrollPane();
		twitter_app_scroll_pane.setId("twitter_scroll_feed_pane");
		twitter_app_scroll_pane.setPrefSize((window_pane.getWidth() * 0.4),
				(window_pane.getHeight() - window_top_bar.getHeight()));
		twitter_app_scroll_pane.setMaxSize((window_pane.getWidth() * 0.4),
				(window_pane.getMaxHeight() - window_top_bar.getMaxHeight()));

		VBox twitter_feed = new VBox();
		twitter_feed.setId("twitter_feed");

		for (Status status : statuses) {
			twitter_feed.getChildren().add(newTwitterPost(status));
		}

		twitter_app_scroll_pane.setContent(twitter_feed);
		twitter_app_pane.getChildren().addAll(twitter_app_tool_bar, twitter_app_scroll_pane);
	}

	/**
	 * This method is used to build the Facebook App pane (facebook_app_pane),
	 * using a list of posts. Note that this method doesn't set visible the App.
	 * It only build it back scene.
	 * 
	 * @param posts
	 */
	private void buildFacebookApp(List<Post> posts) {
		facebook_app_pane = new VBox();
		facebook_app_pane.setId("facebook_app_pane");
		HBox facebook_app_tool_bar = new HBox();
		facebook_app_tool_bar.setId("facebook_app_tool_bar");
		Label facebook_app_top_bar_icon = new Label(facebook_app.getUserName());
		facebook_app_top_bar_icon.setId("facebook_app_top_bar_icon");
		Button facebook_app_top_bar_refresh_button = new Button();
		facebook_app_top_bar_refresh_button.setId("facebook_app_top_bar_refresh_button");
		facebook_app_top_bar_refresh_button.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				refreshFacebookApp();
			}
		});
		Button facebook_app_top_bar_minimize_button = new Button();
		facebook_app_top_bar_minimize_button.setId("facebook_app_top_bar_minimize_button");
		facebook_app_top_bar_minimize_button.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				facebook_app_pane.setVisible(false);
			}
		});
		final Pane spacer = new Pane();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		facebook_app_tool_bar.getChildren().addAll(facebook_app_top_bar_icon, spacer,
				facebook_app_top_bar_refresh_button, facebook_app_top_bar_minimize_button);
		ScrollPane facebook_app_scroll_pane = new ScrollPane();
		facebook_app_scroll_pane.setId("facebook_app_scroll_pane");
		facebook_app_scroll_pane.setPrefSize((window_pane.getWidth() * 0.4),
				(window_pane.getHeight() - window_top_bar.getHeight()));
		facebook_app_scroll_pane.setMaxSize((window_pane.getWidth() * 0.4),
				(window_pane.getMaxHeight() - window_top_bar.getMaxHeight()));

		VBox facebook_feed = new VBox();
		facebook_feed.setId("facebook_feed");

		for (Post post : posts) {
			facebook_feed.getChildren().add(newFacebookPost(post));
		}

		facebook_app_scroll_pane.setContent(facebook_feed);
		facebook_app_pane.getChildren().addAll(facebook_app_tool_bar, facebook_app_scroll_pane);
	}

	/**
	 * This method is used to build the Email App pane (email_app_pane), using a
	 * list of emails. Note that this method doesn't set visible the App. It
	 * only build it back scene.
	 * 
	 * @param emails
	 */
	private void buildEmailApp(List<Message> emails) {
		email_app_pane = new VBox();
		email_app_pane.setId("email_app_pane");
		HBox email_app_tool_bar = new HBox();
		email_app_tool_bar.setId("email_app_tool_bar");
		Label email_app_top_bar_icon = new Label(email_app.getUser());
		email_app_top_bar_icon.setId("email_app_top_bar_icon");
		Button email_app_top_bar_refresh_button = new Button();
		email_app_top_bar_refresh_button.setId("email_app_top_bar_refresh_button");
		email_app_top_bar_refresh_button.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				refreshEmailApp();
			}
		});
		Button email_app_top_bar_minimize_button = new Button();
		email_app_top_bar_minimize_button.setId("email_app_top_bar_minimize_button");
		email_app_top_bar_minimize_button.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				email_app_pane.setVisible(false);
			}
		});
		final Pane spacer = new Pane();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		email_app_tool_bar.getChildren().addAll(email_app_top_bar_icon, spacer, email_app_top_bar_refresh_button,
				email_app_top_bar_minimize_button);
		ScrollPane email_app_scroll_pane = new ScrollPane();
		email_app_scroll_pane.setId("email_app_scroll_pane");
		email_app_scroll_pane.setPrefSize((window_pane.getWidth() * 0.4),
				(window_pane.getHeight() - window_top_bar.getHeight()));
		email_app_scroll_pane.setMaxSize((window_pane.getWidth() * 0.4),
				(window_pane.getMaxHeight() - window_top_bar.getMaxHeight()));

		VBox email_feed = new VBox();
		email_feed.setId("email_feed");

		for (Message message : emails) {
			try {
				email_feed.getChildren().add(newEmailPost(message));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		email_app_scroll_pane.setContent(email_feed);
		email_app_pane.getChildren().addAll(email_app_tool_bar, email_app_scroll_pane);
	}

	/**
	 * This method is used to refresh the Twitter App pane (twitter_app_pane).
	 */
	private void refreshTwitterApp() {
		apps_pane.getChildren().remove(twitter_app_pane);
		getTwitterTimeline();
	}

	/**
	 * This method is used to refresh the Twitter App pane (twitter_app_pane),
	 * using a keyword to filter the posts.
	 * 
	 * @param filter
	 */
	private void refreshTwitterApp(String filter) {
		apps_pane.getChildren().remove(twitter_app_pane);
		getTwitterTimeline(filter);
	}

	/**
	 * This method is used to change the user timeline to show on Twitter App
	 * (twitter_app_pane).
	 * 
	 * @param user
	 */
	private void changeTwitterAppUser(String user) {
		apps_pane.getChildren().remove(twitter_app_pane);
		twitter_app.setUser(user);
		getTwitterTimeline();
	}

	/**
	 * This method is used to refresh the Facebook App pane (facebook_app_pane).
	 */
	private void refreshFacebookApp() {
		apps_pane.getChildren().remove(facebook_app_pane);
		getFacebookTimeline();
	}

	/**
	 * This method is used to refresh the Facebook App pane (facebook_app_pane),
	 * using a keyword to filter the posts.
	 * 
	 * @param filter
	 */
	private void refreshFacebookApp(String filter) {
		apps_pane.getChildren().remove(facebook_app_pane);
		getFacebookTimeline(filter);
	}

	/**
	 * This method is used to refresh the Email App pane (email_app_pane).
	 */
	private void refreshEmailApp() {
		apps_pane.getChildren().remove(email_app_pane);
		getEmailTimeline();
		apps_pane.getChildren().add(email_app_pane);
	}

	/**
	 * This method is used to return a Twitter post, given a status.
	 * 
	 * @param status
	 * @return twitter_post_pane
	 */
	private BorderPane newTwitterPost(Status status) {
		BorderPane twitter_post_pane = new BorderPane();
		twitter_post_pane.setId("twitter_post_pane");
		twitter_post_pane.setPrefWidth(POST_WIDTH);

		HBox twitter_post_top_bar = new HBox(new ImageView(new Image(status.getUser().getProfileImageURL())),
				new Label(status.getUser().getName()));
		twitter_post_top_bar.setId("twitter_post_top_bar");

		// CENTER
		VBox twitter_post_center_container = new VBox();
		twitter_post_center_container.setId("twitter_post_center_container");
		Text post_text = new Text(status.getText());
		post_text.setId("post_texto");
		post_text.setWrappingWidth(POST_WIDTH);

		MediaEntity[] media = status.getMediaEntities(); // get the media
															// entities from the
															// status
		for (MediaEntity m : media) { // search trough your entities
			ImageView img = new ImageView(new Image(m.getMediaURL()));
			img.setFitWidth((window_pane.getWidth() * 0.38));
			img.setFitHeight((window_pane.getWidth() * 0.38));
			twitter_post_center_container.getChildren().add(img);
		}

		twitter_post_center_container.getChildren().addAll(post_text);
		// BOTTOM BAR
		HBox twitter_post_bottom_bar = new HBox();
		twitter_post_bottom_bar.setId("twitter_post_bottom_bar");

		Label favourites_label = new Label(status.getFavoriteCount() + "");
		favourites_label.setId("favourites_label");
		Label retweets_label = new Label(status.getRetweetCount() + "");
		retweets_label.setId("retweets_label");

		twitter_post_bottom_bar.getChildren().addAll(favourites_label, retweets_label);
		twitter_post_pane.setTop(twitter_post_top_bar);
		twitter_post_pane.setCenter(twitter_post_center_container);
		twitter_post_pane.setBottom(twitter_post_bottom_bar);
		return twitter_post_pane;
	}

	/**
	 * This method is used to return a Facebook post, given a post.
	 * 
	 * @param post
	 * @return facebook_post_pane
	 */
	private BorderPane newFacebookPost(Post post) {
		BorderPane facebook_post_pane = new BorderPane();
		facebook_post_pane.setId("facebook_post_pane");
		facebook_post_pane.setPrefWidth(POST_WIDTH);
		// TOP BAR
		ImageView imgv = new ImageView(new Image(facebook_app.getUser().getPicture().getUrl()));
		imgv.resize(50, 20);
		HBox facebook_post_top_bar = new HBox(imgv);
		facebook_post_top_bar.setId("post_top_bar");
		// CENTER PANE
		Text post_text = new Text(post.getMessage());
		post_text.setId("post_texto");
		post_text.setWrappingWidth(POST_WIDTH);
		// BOTTOM BAR
		HBox facebook_post_bottom_bar = new HBox();
		facebook_post_bottom_bar.setId("facebook_post_bottom_bar");

		Label likes_label = new Label(post.getLikesCount().toString());
		likes_label.setId("likes_label");
		Label comments_label = new Label(post.getCommentsCount().toString());
		comments_label.setId("comments_label");
		Label shares_label = new Label(post.getSharesCount().toString());
		shares_label.setId("shares_label");

		facebook_post_bottom_bar.getChildren().addAll(likes_label, comments_label, shares_label);
		facebook_post_pane.setTop(facebook_post_top_bar);
		facebook_post_pane.setCenter(post_text);
		facebook_post_pane.setBottom(facebook_post_bottom_bar);
		return facebook_post_pane;
	}

	/**
	 * This method is used to return a Email post, given a message.
	 * 
	 * @param message
	 * @return email_post_pane
	 * @throws Exception
	 */
	private BorderPane newEmailPost(Message message) throws Exception {
		BorderPane email_post_pane = new BorderPane();
		email_post_pane.setId("email_post_pane");
		email_post_pane.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (mouseEvent.getClickCount() == 2 && !mouseEvent.isConsumed()) {
					mouseEvent.consume();
					try {
						new EmailPostWindow(main_stage, email_app, message);
					} catch (MessagingException e) {
						e.printStackTrace();
					}
				}
			}
		});
		email_post_pane.setPrefWidth(POST_WIDTH);

		// TOP
		String[] email_envelope = email_app.writeEnvelope(message);
		email_post_pane.setTop(new HBox(new Label(email_envelope[0])));
		// BOTTOM
		email_post_pane.setBottom(new HBox(new Label(email_envelope[2]), new Label(message.getSentDate().toString())));
		return email_post_pane;
	}

	/**
	 * This method is used to initialize the Facebook, Twitter and Email Apps.
	 */
	private void initApps() {
		twitter_app = new TwitterApp();
		// facebook_app = new FacebookApp();
		email_app = new EmailApp();
	}

	/**
	 * This method is responsible for getting the the twitters timeline without
	 * a filter
	 */
	private void getTwitterTimeline() {
		/*
		 * The thread has a service for FACEBOOK that has only one task.
		 */
		twitterThread = new Service<Void>() {
			@Override
			protected Task<Void> createTask() {
				return new Task<Void>() {
					/*
					 * What the thread needs to do
					 */
					@Override
					protected Void call() throws Exception {
						buildTwitterApp(twitter_app.getTimeline(twitter_app.getUser()));
						return null;
					}
				};
			};
		};
		/*
		 * What the thread does after it did what is describred above
		 */
		twitterThread.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent arg0) {
				apps_pane.getChildren().add(twitter_app_pane);
				if (!twitter_app_pane.isVisible())
					twitter_app_pane.setVisible(true);
			}
		});
		twitterThread.restart();
	}

	/**
	 * This method is responsible for getting the twitters timeline with a
	 * filter
	 * 
	 * @param filter
	 */
	private void getTwitterTimeline(String filter) {
		/*
		 * The thread has a service for FACEBOOK that has only one task.
		 */
		twitterThread = new Service<Void>() {
			@Override
			protected Task<Void> createTask() {
				return new Task<Void>() {
					/*
					 * What the thread needs to do
					 */
					@Override
					protected Void call() throws Exception {
						buildTwitterApp(twitter_app.getTimeline(twitter_app.getUser(), filter));
						return null;
					}
				};
			};
		};
		/*
		 * What the thread does after it did what is describred above
		 */
		twitterThread.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent arg0) {
				apps_pane.getChildren().add(twitter_app_pane);
			}
		});
		twitterThread.restart();
	}

	/**
	 * This method is responsible for getting the the facebooks timeline without
	 * a filter.
	 */
	private void getFacebookTimeline() {
		/*
		 * The thread has a service for TWITTER that has only one task.
		 */
		facebookThread = new Service<Void>() {
			@Override
			protected Task<Void> createTask() {
				return new Task<Void>() {
					/*
					 * What the thread needs to do
					 */
					@Override
					protected Void call() throws Exception {
						buildFacebookApp(facebook_app.getTimeline());
						return null;
					}
				};
			};
		};

		/*
		 * What the thread does after it did what is describred above
		 */
		facebookThread.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent arg0) {
				apps_pane.getChildren().add(facebook_app_pane);
				if (!facebook_app_pane.isVisible())
					facebook_app_pane.setVisible(true);
			}
		});
		facebookThread.restart();
	}

	/**
	 * This method is responsible for getting the the facebooks timeline with a
	 * filter
	 * 
	 * @param filter
	 */
	private void getFacebookTimeline(String filter) {

		facebookThread = new Service<Void>() {
			@Override
			protected Task<Void> createTask() {
				return new Task<Void>() {
					/*
					 * What the thread needs to do
					 */
					@Override
					protected Void call() throws Exception {
						buildFacebookApp(facebook_app.getTimeline(filter));
						return null;
					}
				};
			};
		};
		/*
		 * What the thread does after it did what is describred above
		 */
		facebookThread.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent arg0) {
				apps_pane.getChildren().add(facebook_app_pane);
			}
		});
		facebookThread.restart();
	}

	private void getEmailTimeline() {
		/*
		 * The thread has a service for FACEBOOK that has only one task.
		 */
		emailThread = new Service<Void>() {
			@Override
			protected Task<Void> createTask() {
				return new Task<Void>() {
					/*
					 * What the thread needs to do
					 */
					@Override
					protected Void call() throws Exception {
						buildEmailApp(email_app.getTimeline());
						return null;
					}
				};
			};
		};
		/*
		 * What the thread does after it did what is describred above
		 */
		emailThread.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent arg0) {
				apps_pane.getChildren().add(email_app_pane);
				if (!email_app_pane.isVisible())
					email_app_pane.setVisible(true);
			}
		});
		emailThread.restart();
	}
}
