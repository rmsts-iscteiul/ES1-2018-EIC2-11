package gui;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import javax.mail.Message;
import javax.mail.MessagingException;

import com.restfb.types.Post;

import apps.EmailApp;
import apps.FacebookApp;
import apps.TimeFilter;
import apps.TwitterApp;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import twitter4j.Status;
import twitter4j.TwitterException;

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
	private TextField search_text_field;
	private ComboBox<TimeFilter> filter_combo_box;
	private LocalDate specific_date;

	private VBox twitter_app_pane = null;
	private VBox facebook_app_pane = null;
	private VBox email_app_pane = null;
	private VBox all_app_pane = null;

	private TwitterApp twitter_app;
	private FacebookApp facebook_app;
	private EmailApp email_app;

	private List<Object> all_posts;

	private utils.User user = null;

	private double xOffset, yOffset;

	private static final int SHADOW_GAP = 10;
	private static final int WINDOW_ROOT_PANE_WIDTH = 1000;
	private static final int WINDOW_ROOT_PANE_HEIGHT = 600;
	private static final int WINDOW_LEFT_MENU_WIDTH = 50;
	private static final int WINDOW_TOP_BAR_HEIGHT = 20;

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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method is used to configure the stage (main_stage).
	 */
	private void configureStage() {
		main_stage.setTitle("Bom dia Academia");
		main_stage.getIcons().add(new Image("/resources/images/logo_24.png"));
		main_stage.initStyle(StageStyle.TRANSPARENT);
	}

	/**
	 * This method is used to create and configure the root pane (root_pane). This
	 * pane is only used to configure the size of the window and to apply to it the
	 * shadow effect.
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
		scene.getStylesheets().add("/resources/css/all_app.css");
		scene.getStylesheets().add("/resources/css/options_pane.css");
		scene.setFill(null);
	}

	/**
	 * This method is used to start the stage (main_stage). It must be the be the
	 * last method to be called in start method.
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
				if (user == null) {
					new PopUpWindow(main_stage, PopUpType.WARNING, "Please log in first");
					left_menu_facebook_toggle_button.setSelected(false);
				} else {
					if (user.getFbToken() == null || user.getFbToken().equals("")) {
						String facebook_token = new PopUpWindow(main_stage, PopUpType.FACEBOOKTOKEN,
								"Please insert here your Facebook token").getFacebookToken();
						user.setFbToken(facebook_token);
						user.updateUsrInfo();
						facebook_app = new FacebookApp(facebook_token);
					} else {
						facebook_app = new FacebookApp(user.getFbToken());
					}
				}
				if (facebook_app_pane == null) {
					getFacebookTimeline();
				} else {
					if (left_menu_facebook_toggle_button.isSelected()) {
						facebook_app_pane.setVisible(true);
					} else {
						facebook_app_pane.setVisible(false);
					}
				}
			}
		});
		ToggleButton left_menu_twitter_toggle_button = new ToggleButton();
		left_menu_twitter_toggle_button.setId("left_menu_twitter_toggle_button");
		left_menu_twitter_toggle_button.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent actionEvent) {
				if (user == null) {
					new PopUpWindow(main_stage, PopUpType.WARNING, "Please log in first");
					left_menu_twitter_toggle_button.setSelected(false);
				} else {
					if (user.getTwToken() == null || user.getTwToken().equals("")) {
						String twitter_tokens = new PopUpWindow(main_stage, PopUpType.TWITTERTOKEN,
								"Please insert here your Twitter token").getTwitterToken();
						user.setTwToken(twitter_tokens);
						user.updateUsrInfo();
						try {
							twitter_app = new TwitterApp(twitter_tokens);
						} catch (TwitterException e) {
							new PopUpWindow(main_stage, PopUpType.WARNING,
									"Sorry but there is no Internet connection :(");
						}
					} else {
						try {
							twitter_app = new TwitterApp(user.getTwToken());
						} catch (TwitterException e) {
							new PopUpWindow(main_stage, PopUpType.WARNING,
									"Sorry but there is no Internet connection :(");
						}
					}
				}
				if (twitter_app_pane == null) {
					getTwitterTimeline();
				} else {
					if (left_menu_twitter_toggle_button.isSelected()) {
						twitter_app_pane.setVisible(true);
					} else {
						twitter_app_pane.setVisible(false);
					}
				}
			}
		});

		ToggleButton left_menu_email_toggle_button = new ToggleButton();
		left_menu_email_toggle_button.setId("left_menu_email_toggle_button");
		left_menu_email_toggle_button.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent actionEvent) {
				email_app = new EmailApp();
				if (user == null) {
					new PopUpWindow(main_stage, PopUpType.WARNING, "Please log in first");
					left_menu_email_toggle_button.setSelected(false);
				} else {
					if (user.getEmUsr() == null || user.getEmUsr().equals("") || user.getEmPwd() == null
							|| user.getEmPwd().equals("")) {
						new LoginWindow(main_stage, email_app);
						if (email_app.getUser().equals("") || email_app.getUser() == null
								|| email_app.getPassword().equals("") || email_app.getPassword() == null) {
							new PopUpWindow(main_stage, PopUpType.WARNING,
									"Sorry but something went wrong :( \n pls try again");
							return;
						} else {
							user.setEmUsr(email_app.getUser());
							user.setEmPwd(email_app.getPassword());
							user.updateUsrInfo();
						}
					} else {
						email_app.setUser(user.getEmUsr());
						email_app.setPassword(user.getEmPwd());
					}
				}
				if (email_app_pane == null) {

					getEmailTimeline();
				} else {
					if (left_menu_email_toggle_button.isSelected()) {
						email_app_pane.setVisible(true);
					} else {
						email_app_pane.setVisible(false);
					}
				}
			}
		});

		ToggleButton left_menu_combine_apps_toggle_button = new ToggleButton();
		left_menu_combine_apps_toggle_button.setId("left_menu_combine_apps_toggle_button");
		left_menu_combine_apps_toggle_button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				if (user == null) {
					new PopUpWindow(main_stage, PopUpType.WARNING, "Please log in first");
					left_menu_combine_apps_toggle_button.setSelected(false);
				} else {
					if (user.getFbToken() == null || user.getFbToken().equals("")) {
						new PopUpWindow(main_stage, PopUpType.WARNING,
								"Please add a Facebook account. \n Try to open Facebook App first");
						return;
					} else if (user.getTwToken() == null || user.getTwToken().equals("")) {
						new PopUpWindow(main_stage, PopUpType.WARNING,
								"Please add a Twitter account. \n Try to open Twitter App first");
						return;
					} else if (user.getEmUsr() == null || user.getEmUsr().equals("") || user.getEmPwd() == null
							|| user.getEmPwd().equals("")) {
						new PopUpWindow(main_stage, PopUpType.WARNING,
								"Please add a Email account. \n Try to open Email App first");
						return;
					}
				}
				if (facebook_app == null) {
					facebook_app = new FacebookApp(user.getFbToken());
				} else if (twitter_app == null) {
					try {
						twitter_app = new TwitterApp(user.getTwToken());
					} catch (TwitterException e) {
						new PopUpWindow(main_stage, PopUpType.WARNING, "Sorry but there is no Internet connection :(");
					}
				} else if (email_app == null) {
					email_app = new EmailApp();
					email_app.setUser(user.getEmUsr());
					email_app.setPassword(user.getEmPwd());

				}

				if (all_app_pane == null) {
					System.out.println("NULL");
					getAllTimeline();
				} else {
					if (left_menu_combine_apps_toggle_button.isSelected()) {
						email_app_pane.setVisible(true);
					} else {
						email_app_pane.setVisible(false);
					}
				}
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
		options_pane.setLayoutY(window_top_bar.getHeight() + 30);

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
				new AddUserWindow(main_stage);
				user = utils.User.getLast();
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
					user.setDarkTheme("1");
					user.updateUsrInfo();
				} else {
					window_pane.setId("window_pane");
					window_left_menu.setId("window_left_menu");
					user.setDarkTheme("0");
					user.updateUsrInfo();
				}
			}
		});
		Button log_in_button = new Button("Log in");
		log_in_button.setId("log_in_button");
		Button log_out_button = new Button("Log in");
		log_out_button.setId("log_out_button");
		// Login
		log_in_button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				user = new LoginWindow(main_stage).getUser();
				user.checkInfo();
				if (user.getDarkTheme().equals("1")) {
					window_pane.setId("window_pane_dt");
					window_left_menu.setId("window_left_menu_dt");
					dark_theme_toggle_button.setSelected(true);
				} else {
					window_pane.setId("window_pane");
					window_left_menu.setId("window_left_menu");
					dark_theme_toggle_button.setSelected(false);
				}
				log_in_button.setVisible(false);
				log_out_button.setVisible(true);
			}
		});
		// Logout
		log_out_button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				user = null;
				search_text_field.setText("Filter...");
				filter_combo_box.setValue(TimeFilter.ALL_TIME);
				apps_pane.getChildren().clear();
				log_in_button.setVisible(true);
				log_out_button.setVisible(false);
			}
		});
		log_out_button.setVisible(false);
		Button settings_button = new Button("Settings");
		settings_button.setId("settings_button");
		Button about_button = new Button("About");
		about_button.setId("about_button");
		bottom_options_container.getChildren().addAll(new Separator(), log_in_button, log_out_button, new Separator(),
				dark_theme_toggle_button, settings_button, about_button);
		options_content_pane.setBottom(bottom_options_container);

		options_pane.setCenter(options_content_pane);
		window_pane.getChildren().add(options_pane);
	}

	/**
	 * This method is used to return a search bar, on the top of the window.
	 * 
	 * @return window_top_bar_search_container
	 */
	private HBox createSearch() {
		HBox window_top_bar_search_container = new HBox();
		window_top_bar_search_container.setId("window_top_bar_search_container");
		HBox app_check_pane = new HBox();
		app_check_pane.setId("app_check_pane");
		ToggleButton search_facebook_toggle_button = new ToggleButton();
		search_facebook_toggle_button.setId("search_facebook_toggle_button");
		ToggleButton search_twitter_toggle_button = new ToggleButton();
		search_twitter_toggle_button.setId("search_twitter_toggle_button");
		ToggleButton search_email_toggle_button = new ToggleButton();
		search_email_toggle_button.setId("search_email_toggle_button");
		ToggleButton search_all_toggle_button = new ToggleButton();
		search_all_toggle_button.setId("search_all_toggle_button");
		HBox search_pane = new HBox();
		search_pane.setId("search_pane");
		filter_combo_box = new ComboBox<TimeFilter>();
		filter_combo_box.setId("filter_combo_box");
		filter_combo_box.getItems().addAll(TimeFilter.LAST_HOUR, TimeFilter.LAST_24H, TimeFilter.LAST_WEEK,
				TimeFilter.LAST_MONTH, TimeFilter.ALL_TIME, TimeFilter.SPECIFIC_DAY);
		filter_combo_box.setValue(TimeFilter.ALL_TIME);
		filter_combo_box.getSelectionModel().selectedItemProperty().addListener((options, old_value, new_value) -> {
			if (new_value.equals(TimeFilter.SPECIFIC_DAY)) {
				specific_date = new PopUpWindow(main_stage, PopUpType.DATEPICKER, "Select a day").getDate();
			}
		});
		app_check_pane.getChildren().addAll(search_facebook_toggle_button, search_twitter_toggle_button,
				search_email_toggle_button, search_all_toggle_button, filter_combo_box);
		search_text_field = new TextField("Filter...");
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
					twitter_app.setTimeFilter(filter_combo_box.getValue());
					if (filter_combo_box.getValue().equals(TimeFilter.SPECIFIC_DAY)) {
						twitter_app.setTimeFilter(TimeFilter.SPECIFIC_DAY);
						twitter_app.getTimeFilter().setDate(specific_date.getYear(), specific_date.getMonthValue(),
								specific_date.getDayOfMonth());
					}
					apps_pane.getChildren().remove(twitter_app_pane);
					if (search_text_field.getText().equals("Filter...") || search_text_field.getText().equals("")) {
						getTwitterTimeline();
					} else {
						getTwitterTimeline(search_text_field.getText());
						user.setWordFilter(search_text_field.getText());
						user.updateUsrInfo();
					}
				}
				if (search_facebook_toggle_button.isSelected()) {
					facebook_app.setTimeFilter(filter_combo_box.getValue());
					if (filter_combo_box.getValue().equals(TimeFilter.SPECIFIC_DAY)) {
						facebook_app.setTimeFilter(TimeFilter.SPECIFIC_DAY);
						facebook_app.getTimeFilter().setDate(specific_date.getYear(), specific_date.getMonthValue(),
								specific_date.getDayOfMonth());
					}
					apps_pane.getChildren().remove(facebook_app_pane);
					if (search_text_field.getText().equals("Filter...") || search_text_field.getText().equals("")) {
						getFacebookTimeline();
					} else {
						getFacebookTimeline(search_text_field.getText());
						user.setWordFilter(search_text_field.getText());
						user.updateUsrInfo();
					}
				}
				if (search_email_toggle_button.isSelected()) {
					email_app.setTimeFilter(filter_combo_box.getValue());
					if (filter_combo_box.getValue().equals(TimeFilter.SPECIFIC_DAY)) {
						email_app.setTimeFilter(TimeFilter.SPECIFIC_DAY);
						email_app.getTimeFilter().setDate(specific_date.getYear(), specific_date.getMonthValue(),
								specific_date.getDayOfMonth());
					}
					apps_pane.getChildren().remove(email_app_pane);
					if (search_text_field.getText().equals("Filter...") || search_text_field.getText().equals("")) {
						getEmailTimeline();
					} else {
						getEmailTimeline();
						user.setWordFilter(search_text_field.getText());
						user.updateUsrInfo();
					}
				}
				if (!search_facebook_toggle_button.isSelected() && !search_twitter_toggle_button.isSelected()
						&& !search_email_toggle_button.isSelected()) {
					new PopUpWindow(main_stage, PopUpType.WARNING, "Please select an App to search.");
				}
			}
		});
		search_pane.getChildren().addAll(filter_combo_box, new Separator(Orientation.VERTICAL), search_text_field,
				search_button);
		window_top_bar_search_container.getChildren().addAll(app_check_pane, search_pane);
		return window_top_bar_search_container;
	}

	/**
	 * This method is used to build the Apps pane (apps_pane). This pane is where
	 * the Facebook, Twitter and Email Apps is going to be added.
	 */
	private void buildAppsPane() {
		apps_pane = new HBox();
		apps_pane.setPrefSize(window_pane.getWidth(), (window_pane.getHeight() - window_top_bar.getHeight()));
		apps_pane.setMaxSize(window_pane.getMaxWidth(), (window_pane.getMaxHeight() - window_top_bar.getMaxHeight()));
		apps_pane.setId("apps_pane");
		window_pane.setCenter(apps_pane);
	}

	/**
	 * This method is used to build the Twitter App pane (twitter_app_pane), using a
	 * list of status. Note that this method doesn't set visible the App. It only
	 * build it back scene.
	 * 
	 * @param statuses
	 */
	private void buildTwitterApp(List<Status> statuses) {
		twitter_app_pane = new VBox();
		twitter_app_pane.setId("twitter_app_pane");
		twitter_app_pane.setPrefSize((getUsableSpace() * 0.9), (window_pane.getHeight() - window_top_bar.getHeight()));
		twitter_app_pane.setMaxSize((getUsableSpace() * 0.9),
				(window_pane.getMaxHeight() - window_top_bar.getMaxHeight()));

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

		Button twitter_app_top_bar_new_tweet_button = new Button();
		twitter_app_top_bar_new_tweet_button.setId("twitter_app_top_bar_new_tweet_button");
		twitter_app_top_bar_new_tweet_button.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				new TwitterPostWindow(main_stage, twitter_app);
			}
		});
		ToggleButton twitter_app_top_bar_change_user_button = new ToggleButton();
		twitter_app_top_bar_change_user_button.setId("twitter_app_top_bar_change_user_button");
		twitter_app_top_bar_change_user_button.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				twitter_app_tool_bar.getChildren().remove(twitter_app_top_bar_change_user_button);
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
		twitter_app_tool_bar.getChildren().addAll(twitter_app_top_bar_icon, spacer,
				twitter_app_top_bar_new_tweet_button, twitter_app_top_bar_refresh_button,
				twitter_app_top_bar_change_user_button, twitter_app_top_bar_minimize_button);

		ScrollPane twitter_app_scroll_pane = new ScrollPane();
		twitter_app_scroll_pane.setId("twitter_scroll_feed_pane");
		twitter_app_scroll_pane.setFitToWidth(true);
		VBox twitter_feed = new VBox();
		twitter_feed.setId("twitter_feed");

		boolean is_there_status = true;
		for (Status status : statuses) {
			if (is_there_status) {
				Image cover_image = new Image(status.getUser().getProfileBannerURL());
				double ratio = cover_image.getWidth() / cover_image.getHeight();
				ImageView cover_image_view = new ImageView();
				cover_image_view.setFitWidth(twitter_app_pane.getPrefWidth() * 0.9);
				cover_image_view.setFitHeight(cover_image_view.getFitWidth() / ratio);
				Rectangle clip = new Rectangle(cover_image_view.getFitWidth(), cover_image_view.getFitHeight());
				clip.setArcWidth(20);
				clip.setArcHeight(20);
				cover_image_view.setClip(clip);
				cover_image_view.setImage(cover_image);
				twitter_feed.getChildren().add(cover_image_view);
				is_there_status = false;
			}
			twitter_feed.getChildren().add(newTwitterPost(status, twitter_app_pane));
		}

		// More button
		Button more_button = new Button();
		more_button.setId("twitter_more_button");
		more_button.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				try {
					if (statuses.size() == 0) {
						twitter_feed.getChildren().remove(more_button);
						Label no_tweets_label = new Label("Sorry but there is no tweets");
						no_tweets_label.setId("no_tweets_label");
						twitter_feed.getChildren().add(no_tweets_label);
						twitter_feed.getChildren().add(more_button);

					} else {
						if (!search_text_field.getText().equals("Filter...")
								&& !search_text_field.getText().equals("")) {
							List<Status> statuses = twitter_app.getMoreTweetsWithFilter();
							if (statuses.isEmpty()) {
								new PopUpWindow(main_stage, PopUpType.WARNING, "Sorry but there is no more results");
							} else {
								twitter_feed.getChildren().remove(more_button);
								for (Status status : statuses) {
									twitter_feed.getChildren().add(newTwitterPost(status, twitter_app_pane));
								}
								twitter_app.bufferingWithFilter();
								twitter_feed.getChildren().add(more_button);
							}
						} else {
							List<Status> statuses = twitter_app.getMoreTweetsWithoutFilter();
							if (statuses.isEmpty()) {
								new PopUpWindow(main_stage, PopUpType.WARNING, "Sorry but there is no more results");
							} else {
								twitter_feed.getChildren().remove(more_button);
								for (Status status : statuses) {
									twitter_feed.getChildren().add(newTwitterPost(status, twitter_app_pane));
								}
								twitter_app.bufferingWithoutFilter();
								twitter_feed.getChildren().add(more_button);
							}
						}
					}

				} catch (TwitterException e) {
					new PopUpWindow(main_stage, PopUpType.WARNING, "Sorry but there is no Internet connection :(");
				}
			}
		});
		twitter_feed.getChildren().add(more_button);

		twitter_app_scroll_pane.setContent(twitter_feed);
		twitter_app_pane.getChildren().addAll(twitter_app_tool_bar, twitter_app_scroll_pane);

	}

	/**
	 * This method is used to build the Facebook App pane (facebook_app_pane), using
	 * a list of posts. Note that this method doesn't set visible the App. It only
	 * build it back scene.
	 * 
	 * @param posts
	 */
	private void buildFacebookApp(List<Post> posts) {
		facebook_app_pane = new VBox();
		facebook_app_pane.setId("facebook_app_pane");
		facebook_app_pane.setPrefSize((getUsableSpace() * 0.9), (window_pane.getHeight() - window_top_bar.getHeight()));
		facebook_app_pane.setMaxSize((getUsableSpace() * 0.9),
				(window_pane.getMaxHeight() - window_top_bar.getMaxHeight()));
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
		VBox facebook_feed = new VBox();
		facebook_feed.setId("facebook_feed");

		for (Post post : posts) {
			facebook_feed.getChildren().add(newFacebookPost(post, facebook_app_pane));
		}
		// More button
		Button more_button = new Button();
		more_button.setId("facebook_more_button");
		more_button.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				facebook_app.incrementDesiredPage();
				if (posts.size() == 0) {
					facebook_feed.getChildren().remove(more_button);
					Label no_posts_label = new Label("Sorry but there is no tweets");
					no_posts_label.setId("no_posts_label");
					facebook_feed.getChildren().add(no_posts_label);
					facebook_feed.getChildren().add(more_button);
				} else {
					if (!search_text_field.getText().equals("Filter...") && !search_text_field.getText().equals("")) {
						List<Post> posts = facebook_app.getPostsByPage(search_text_field.getText());
						if (posts.isEmpty()) {
							facebook_feed.getChildren().remove(more_button);
							new PopUpWindow(main_stage, PopUpType.WARNING, "Sorry but there is no more results");
						} else {
							facebook_feed.getChildren().remove(more_button);
							for (Post post : posts) {
								facebook_feed.getChildren().add(newFacebookPost(post, facebook_app_pane));
							}
							facebook_feed.getChildren().add(more_button);
						}
					} else {
						List<Post> posts = facebook_app.getPostsByPage();
						if (posts.isEmpty()) {
							facebook_feed.getChildren().remove(more_button);
							new PopUpWindow(main_stage, PopUpType.WARNING, "Sorry but there is no more results");
						} else {
							facebook_feed.getChildren().remove(more_button);
							for (Post post : posts) {
								facebook_feed.getChildren().add(newFacebookPost(post, facebook_app_pane));
							}
							facebook_feed.getChildren().add(more_button);
						}
					}
				}
			}
		});
		facebook_feed.getChildren().add(more_button);

		facebook_app_scroll_pane.setContent(facebook_feed);
		facebook_app_pane.getChildren().addAll(facebook_app_tool_bar, facebook_app_scroll_pane);

	}

	/**
	 * This method is used to build the Email App pane (email_app_pane), using a
	 * list of emails. Note that this method doesn't set visible the App. It only
	 * build it back scene.
	 * 
	 * @param emails
	 */
	private void buildEmailApp(List<Message> emails) {
		email_app_pane = new VBox();
		email_app_pane.setId("email_app_pane");
		email_app_pane.setPrefSize((getUsableSpace() * 0.9), (window_pane.getHeight() - window_top_bar.getHeight()));
		email_app_pane.setMaxSize((getUsableSpace() * 0.9),
				(window_pane.getMaxHeight() - window_top_bar.getMaxHeight()));
		HBox email_app_tool_bar = new HBox();
		email_app_tool_bar.setId("email_app_tool_bar");
		Label email_app_top_bar_icon = new Label(email_app.getUser());
		email_app_top_bar_icon.setId("email_app_top_bar_icon");
		Button email_app_top_bar_new_message_button = new Button();
		email_app_top_bar_new_message_button.setId("email_app_top_bar_new_message_button");
		email_app_top_bar_new_message_button.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				try {
					new EmailPostWindow(main_stage, email_app);
				} catch (MessagingException e) {
					e.printStackTrace();
				}
			}
		});
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
		email_app_tool_bar.getChildren().addAll(email_app_top_bar_icon, spacer, email_app_top_bar_new_message_button,
				email_app_top_bar_refresh_button, email_app_top_bar_minimize_button);
		ScrollPane email_app_scroll_pane = new ScrollPane();
		email_app_scroll_pane.setId("email_app_scroll_pane");
		VBox email_feed = new VBox();
		email_feed.setId("email_feed");

		// More button
		Button more_button = new Button();
		more_button.setId("email_more_button");
		more_button.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				// To-Do
			}
		});
		if (emails.size() == 0) {
			email_feed.getChildren().remove(more_button);
			Label no_posts_label = new Label("Sorry but there is no tweets");
			no_posts_label.setId("no_posts_label");
			email_feed.getChildren().add(no_posts_label);
			email_feed.getChildren().add(more_button);
		} else {
			for (Message message : emails) {
				try {
					email_feed.getChildren().add(newEmailPost(message, email_app_pane));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		email_feed.getChildren().add(more_button);

		email_app_scroll_pane.setContent(email_feed);
		email_app_pane.getChildren().addAll(email_app_tool_bar, email_app_scroll_pane);
	}

	/**
	 * This method is used to build the All App pane (all_app_pane), using a list of
	 * statuses, posts and emails. The All App contains every posts combined. Note
	 * that this method doesn't set visible the App. It only build it back scene.
	 * 
	 * @param emails
	 */
	private void buildAllApp(List<Status> statuses, List<Post> posts, List<Message> emails) {
		System.out.println("entrou");
		all_app_pane = new VBox();
		all_app_pane.setId("all_app_pane");
		all_app_pane.setPrefSize((getUsableSpace() * 0.9), (window_pane.getHeight() - window_top_bar.getHeight()));
		all_app_pane.setMaxSize((getUsableSpace() * 0.9), (window_pane.getMaxHeight() - window_top_bar.getMaxHeight()));
		HBox all_app_tool_bar = new HBox();
		all_app_tool_bar.setId("all_app_tool_bar");
		Label all_app_top_bar_icon = new Label();
		all_app_top_bar_icon.setId("all_app_top_bar_icon");
		Button all_app_top_bar_refresh_button = new Button();
		all_app_top_bar_refresh_button.setId("all_app_top_bar_refresh_button");
		all_app_top_bar_refresh_button.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				refreshAllApp();
			}
		});
		Button all_app_top_bar_minimize_button = new Button();
		all_app_top_bar_minimize_button.setId("all_app_top_bar_minimize_button");
		all_app_top_bar_minimize_button.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				all_app_pane.setVisible(false);
			}
		});
		final Pane spacer = new Pane();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		all_app_tool_bar.getChildren().addAll(all_app_top_bar_icon, spacer, all_app_top_bar_refresh_button,
				all_app_top_bar_minimize_button);
		ScrollPane all_app_scroll_pane = new ScrollPane();
		all_app_scroll_pane.setId("all_app_scroll_pane");
		all_app_scroll_pane.setPrefSize((window_pane.getWidth() * 0.8),
				(window_pane.getHeight() - window_top_bar.getHeight()));
		all_app_scroll_pane.setMaxSize((window_pane.getWidth() * 0.8),
				(window_pane.getMaxHeight() - window_top_bar.getMaxHeight()));

		VBox all_feed = new VBox();
		all_feed.setId("all_feed");

		System.out.println("BEFORE CLEAR");
		all_posts.clear();
		System.out.println("AFTER CLEAR");
		getAllPosts(statuses, posts, emails);
		for (Object o : all_posts) {
			if (o instanceof Status) {
				all_feed.getChildren().add(newTwitterPost((Status) o, all_app_pane));
			} else if (o instanceof Post) {
				all_feed.getChildren().add(newFacebookPost((Post) o, all_app_pane));
			} else if (o instanceof Message) {
				try {
					all_feed.getChildren().add(newEmailPost((Message) o, all_app_pane));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		// More button
		Button more_button = new Button();
		more_button.setId("all_more_button");
		more_button.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {

			}
		});
		all_feed.getChildren().add(more_button);

		all_app_scroll_pane.setContent(all_feed);
		all_app_pane.getChildren().addAll(all_app_tool_bar, all_app_scroll_pane);
	}

	/**
	 * This method is used to refresh the Twitter App pane (twitter_app_pane).
	 */
	private void refreshTwitterApp() {
		twitter_app.setTimeFilter(TimeFilter.ALL_TIME);
		apps_pane.getChildren().remove(twitter_app_pane);
		getTwitterTimeline();
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
		facebook_app.setTimeFilter(TimeFilter.ALL_TIME);
		apps_pane.getChildren().remove(facebook_app_pane);
		getFacebookTimeline();
	}

	/**
	 * This method is used to refresh the Email App pane (email_app_pane).
	 */
	private void refreshEmailApp() {
		apps_pane.getChildren().remove(email_app_pane);
		getEmailTimeline();
	}

	/**
	 * This method is used to refresh the All App pane (all_app_pane).
	 */
	private void refreshAllApp() {
		apps_pane.getChildren().remove(all_app_pane);
		getAllTimeline();
	}

	/**
	 * This method is used to return a Twitter post, given a status.
	 * 
	 * @param status
	 * @return twitter_post_pane
	 */
	private BorderPane newTwitterPost(Status status, VBox called_app_pane) {
		BorderPane twitter_post_pane = new BorderPane();
		twitter_post_pane.setId("twitter_post_pane");
		twitter_post_pane.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (mouseEvent.getClickCount() == 2 && !mouseEvent.isConsumed()) {
					mouseEvent.consume();
					new TwitterPostWindow(main_stage, twitter_app, status);
				}
			}
		});
		twitter_post_pane.setPrefSize(called_app_pane.getPrefWidth() * 0.9, 100);
		twitter_post_pane.setMaxSize(called_app_pane.getPrefWidth() * 0.9, 100);
		// Left
		VBox twitter_post_left_container = new VBox();
		twitter_post_left_container.setId("twitter_post_left_container");
		ImageView profile_image = new ImageView(new Image(status.getUser().getProfileImageURL()));
		profile_image.setId("profile_image");
		profile_image.setFitWidth(50);
		profile_image.setFitHeight(50);
		Label profile_image_label = new Label(status.getUser().getName());
		profile_image_label.setId("profile_image_label");
		twitter_post_left_container.getChildren().addAll(profile_image, profile_image_label);
		twitter_post_pane.setLeft(twitter_post_left_container);
		// Right
		VBox twitter_post_right_container = new VBox();
		twitter_post_right_container.setId("twitter_post_right_container");
		Label favourites_label = new Label(status.getFavoriteCount() + "");
		if (status.isFavorited()) {
			favourites_label.setId("favourited_label");
		} else {
			favourites_label.setId("no_favourited_label");
		}
		Label retweets_label = new Label(status.getRetweetCount() + "");
		retweets_label.setId("retweets_label");
		twitter_post_right_container.getChildren().addAll(favourites_label, retweets_label);
		twitter_post_pane.setRight(twitter_post_right_container);
		// Center
		VBox twitter_post_center_container = new VBox();
		twitter_post_center_container.setId("twitter_post_center_container");
		Text twitter_post_text = new Text(status.getText());
		twitter_post_text.setId("twitter_post_text");
		twitter_post_text.setWrappingWidth(twitter_post_pane.getPrefWidth() * 0.8);
		twitter_post_center_container.getChildren().add(twitter_post_text);
		twitter_post_pane.setCenter(twitter_post_center_container);
		// Bottom
		Label twitter_post_date = new Label(status.getCreatedAt() + "");
		twitter_post_pane.setBottom(twitter_post_date);

		return twitter_post_pane;
	}

	/**
	 * This method is used to return a Facebook post, given a post.
	 * 
	 * @param post
	 * @return facebook_post_pane
	 */
	private BorderPane newFacebookPost(Post post, VBox called_app_pane) {
		BorderPane facebook_post_pane = new BorderPane();
		facebook_post_pane.setId("facebook_post_pane");
		facebook_post_pane.setPrefSize(called_app_pane.getPrefWidth() * 0.9, 100);
		facebook_post_pane.setMaxSize(called_app_pane.getPrefWidth() * 0.9, 100);
		// LEFT BAR
		ImageView imgv = new ImageView(new Image(facebook_app.getUser().getPicture().getUrl()));
		imgv.resize(50, 20);
		HBox facebook_post_left_bar = new HBox(imgv);
		facebook_post_left_bar.setId("facebook_post_left_bar");
		facebook_post_pane.setLeft(facebook_post_left_bar);
		// CENTER PANE
		HBox facebook_post_center_container = new HBox();
		facebook_post_center_container.setId("facebook_post_center_container");
		Text post_text = new Text(post.getMessage());
		post_text.setId("post_texto");
		post_text.setWrappingWidth(facebook_post_pane.getPrefWidth() * 0.8);
		facebook_post_center_container.getChildren().add(post_text);
		facebook_post_pane.setCenter(facebook_post_center_container);
		// RIGHT BAR
		VBox facebook_post_right_bar = new VBox();
		facebook_post_right_bar.setId("facebook_post_right_bar");
		Label likes_label = new Label(post.getLikesCount().toString());
		likes_label.setId("likes_label");
		Label comments_label = new Label(post.getCommentsCount().toString());
		comments_label.setId("comments_label");
		Label shares_label = new Label(post.getSharesCount().toString());
		shares_label.setId("shares_label");
		facebook_post_right_bar.getChildren().addAll(likes_label, comments_label, shares_label);
		facebook_post_pane.setRight(facebook_post_right_bar);
		// Bottom
		Label facebook_post_date = new Label(post.getCreatedTime() + "");
		facebook_post_pane.setBottom(facebook_post_date);

		return facebook_post_pane;
	}

	/**
	 * This method is used to return a Email post, given a message.
	 * 
	 * @param message
	 * @return email_post_pane
	 * @throws Exception
	 */
	private BorderPane newEmailPost(Message message, VBox called_app_pane) throws Exception {
		BorderPane email_post_pane = new BorderPane();
		email_post_pane.setId("email_post_pane");
		email_post_pane.setPrefSize(called_app_pane.getPrefWidth() * 0.9, 100);
		email_post_pane.setMaxSize(called_app_pane.getPrefWidth() * 0.9, 100);
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

		// TOP
		String[] email_envelope = email_app.writeEnvelope(message);
		Label email_label = new Label(email_envelope[0]);
		email_label.setId("email_label");
		Label name_label = new Label(email_envelope[1] + "    ");
		name_label.setId("name_label");
		email_post_pane.setTop(new HBox(name_label, email_label));
		// BOTTOM
		email_post_pane.setBottom(new HBox(new Label(email_envelope[2]), new Label(message.getSentDate().toString())));
		return email_post_pane;
	}

	/**
	 * This method is used to initialize the Facebook, Twitter and Email Apps.
	 */
	private void initApps() {

	}

	/**
	 * This method is responsible for getting the the twitters timeline without a
	 * filter
	 */
	private void getTwitterTimeline() {
		/*
		 * The thread has a service for FACEBOOK that has only one task.
		 */
		Task<Void> task = new Task<Void>() {
			/*
			 * What the thread needs to do
			 */
			@Override
			protected Void call() throws Exception {
				buildTwitterApp(twitter_app.getTimeline(twitter_app.getUser()));
				return null;
			}
		};
		/*
		 * What the thread does after it did what is describred above
		 */
		task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent arg0) {
				apps_pane.getChildren().add(twitter_app_pane);
				if (!twitter_app_pane.isVisible())
					twitter_app_pane.setVisible(true);
			}
		});
		new Thread(task).start();
	}

	/**
	 * This method is responsible for getting the twitters timeline with a filter
	 * 
	 * @param filter
	 */
	private void getTwitterTimeline(String filter) {
		/*
		 * The thread has a service for FACEBOOK that has only one task.
		 */
		Task<Void> task = new Task<Void>() {
			/*
			 * What the thread needs to do
			 */
			@Override
			protected Void call() throws Exception {
				buildTwitterApp(twitter_app.getTimeline(twitter_app.getUser(), filter));
				return null;
			}
		};
		/*
		 * What the thread does after it did what is describred above
		 */
		task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent arg0) {
				apps_pane.getChildren().add(twitter_app_pane);
			}
		});
		new Thread(task).start();
	}

	/**
	 * This method is responsible for getting the the facebooks timeline without a
	 * filter.
	 */
	private void getFacebookTimeline() {
		/*
		 * The thread has a service for TWITTER that has only one task.
		 */
		Task<Void> task = new Task<Void>() {
			/*
			 * What the thread needs to do
			 */
			@Override
			protected Void call() throws Exception {
				buildFacebookApp(facebook_app.getTimeline());
				return null;
			}
		};

		/*
		 * What the thread does after it did what is describred above
		 */
		task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent arg0) {
				apps_pane.getChildren().add(facebook_app_pane);
				if (!facebook_app_pane.isVisible())
					facebook_app_pane.setVisible(true);
			}
		});
		new Thread(task).start();
	}

	/**
	 * This method is responsible for getting the the facebooks timeline with a
	 * filter
	 * 
	 * @param filter
	 */
	private void getFacebookTimeline(String filter) {

		Task<Void> task = new Task<Void>() {
			/*
			 * What the thread needs to do
			 */
			@Override
			protected Void call() throws Exception {
				buildFacebookApp(facebook_app.getTimeline(filter));
				return null;
			}
		};

		/*
		 * What the thread does after it did what is describred above
		 */
		task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent arg0) {
				apps_pane.getChildren().add(facebook_app_pane);
			}
		});
		new Thread(task).start();
	}

	private void getEmailTimeline() {
		/*
		 * The thread has a service for FACEBOOK that has only one task.
		 */
		Task<Void> task = new Task<Void>() {
			/*
			 * What the thread needs to do
			 */
			@Override
			protected Void call() throws Exception {
				buildEmailApp(email_app.getTimeline(""));
				return null;
			}
		};
		/*
		 * What the thread does after it did what is describred above
		 */
		task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent arg0) {
				apps_pane.getChildren().add(email_app_pane);
				if (!email_app_pane.isVisible())
					email_app_pane.setVisible(true);
			}
		});
		new Thread(task).start();
	}

	private void getAllTimeline() {
		/*
		 * The thread has a service for ALL APPS that has only one task.
		 */
		Task<Void> task = new Task<Void>() {
			/*
			 * What the thread needs to do
			 */
			@Override
			protected Void call() throws Exception {
				System.out.println("BEFORE BUILD");
				buildAllApp(twitter_app.getTimeline(twitter_app.getUser()), facebook_app.getTimeline(),
						email_app.getTimeline(""));
				System.out.println("AFTER BUILD");
				return null;
			}
		};

		/*
		 * What the thread does after it did what is describred above
		 */
		task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent arg0) {
				apps_pane.getChildren().add(all_app_pane);
				if (!all_app_pane.isVisible())
					all_app_pane.setVisible(true);
			}
		});
		new Thread(task).start();
	}

	private void getAllPosts(List<Status> statuses, List<Post> posts, List<Message> messages) {
		all_posts.addAll(posts);
		all_posts.addAll(messages);
		all_posts.addAll(statuses);
//		sortIt();
	}

	public void sortIt() {
		List<Object> objectList = new LinkedList<>();
		objectList.addAll(all_posts);
		List<Object> sortedList = new LinkedList<>();
		long mainTime = 0;
		long objTime = 0;
		int index = 0;
		while (objectList.size() != 1) {
			List<Object> objList = new LinkedList<>();
			objList.add(objectList.get(0));
			index = 0;
			for (int i = 0; i < objectList.size(); i++) {
				Object obj = objectList.get(i);
				if (obj instanceof Status) {
					objTime = (long) ((Status) obj).getCreatedAt().getTime();
				} else if (obj instanceof Post) {
					objTime = (long) ((Post) obj).getCreatedTime().getTime();
				} else if (obj instanceof Message) {
					try {
						objTime = (long) ((Message) obj).getSentDate().getTime();
					} catch (MessagingException e) {
						e.printStackTrace();
					}
				}
				if (objList.get(0) instanceof Status) {
					mainTime = (long) ((Status) objList.get(0)).getCreatedAt().getTime();
				} else if (objList.get(0) instanceof Post) {
					mainTime = (long) ((Post) objList.get(0)).getCreatedTime().getTime();
				} else if (objList.get(0) instanceof Message) {
					try {
						mainTime = (long) ((Message) objList.get(0)).getSentDate().getTime();
					} catch (MessagingException e) {
						e.printStackTrace();
					}
				}
				if (mainTime <= objTime) {
					objList.clear();
					objList.add(obj);
					index = i;
				}
			}
			sortedList.add(objList.get(0));
			objectList.remove(index);
		}
		sortedList.addAll(objectList);
		all_posts.clear();
		all_posts.addAll(sortedList);
	}

	private double getUsableSpace() {
		return apps_pane.getWidth();

	}

}
