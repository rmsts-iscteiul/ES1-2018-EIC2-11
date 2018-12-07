package gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class PopUpWindow {

	private Stage pop_up_stage;
	private Scene pop_up_scene;
	private BorderPane pop_up_root_pane;
	private BorderPane pop_up_window_root_pane;

	private PopUpType type;

	private static final int POP_UP_SHADOW_GAP = 10;
	private static final int POP_UP_ROOT_PANE_WIDTH = 400;
	private static final int POP_UP_ROOT_PANE_HEIGHT = 240;
	private static final int POP_UP_WINDOW_TOP_BAR_HEIGHT = 20;

	private double xOffset, yOffset;

	private boolean confirmation;

	private TextField facebook_access_token;
	private TextField consumerKey;
	private TextField consumerSecret;
	private TextField userAccessToken;
	private TextField accessTokenSecret;

	public PopUpWindow(Stage main_stage, PopUpType type, String text) {
		this.type = type;
		configurePopUpStage(main_stage);
		createPopUpRootPane();
		buildPopUpWindowRootPane();
		buildPopUpContent(text);
		buildPopUpScene();
		startPopUpStage();
	}

	private void configurePopUpStage(Stage main_stage) {
		pop_up_stage = new Stage();
		pop_up_stage.initModality(Modality.APPLICATION_MODAL);
		pop_up_stage.initOwner(main_stage);
		pop_up_stage.initStyle(StageStyle.TRANSPARENT);
	}

	private void createPopUpRootPane() {
		BorderPane pop_up_root_pane = new BorderPane();
		this.pop_up_root_pane = pop_up_root_pane;
		pop_up_root_pane.setPrefSize(POP_UP_ROOT_PANE_WIDTH + POP_UP_SHADOW_GAP,
				POP_UP_ROOT_PANE_HEIGHT + POP_UP_SHADOW_GAP);
		pop_up_root_pane.setMaxSize(POP_UP_ROOT_PANE_WIDTH + POP_UP_SHADOW_GAP,
				POP_UP_ROOT_PANE_HEIGHT + POP_UP_SHADOW_GAP);
		pop_up_root_pane.setId("pop_up_root_pane");
		HBox top_gap_pane = new HBox();
		top_gap_pane.setPrefSize(POP_UP_ROOT_PANE_WIDTH + POP_UP_SHADOW_GAP, POP_UP_SHADOW_GAP);
		top_gap_pane.setId("pop_up_shadow_gap");
		HBox bottom_gap_pane = new HBox();
		bottom_gap_pane.setPrefSize(POP_UP_ROOT_PANE_WIDTH + POP_UP_SHADOW_GAP, POP_UP_SHADOW_GAP);
		bottom_gap_pane.setId("pop_up_shadow_gap");
		VBox left_gap_pane = new VBox();
		left_gap_pane.setPrefSize(POP_UP_SHADOW_GAP, POP_UP_ROOT_PANE_HEIGHT + POP_UP_SHADOW_GAP);
		left_gap_pane.setId("pop_up_shadow_gap");
		VBox right_gap_pane = new VBox();
		right_gap_pane.setPrefSize(POP_UP_SHADOW_GAP, POP_UP_ROOT_PANE_HEIGHT + POP_UP_SHADOW_GAP);
		right_gap_pane.setId("pop_up_shadow_gap");
		pop_up_root_pane.setTop(top_gap_pane);
		pop_up_root_pane.setBottom(bottom_gap_pane);
		pop_up_root_pane.setLeft(left_gap_pane);
		pop_up_root_pane.setRight(right_gap_pane);
		pop_up_root_pane.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				xOffset = pop_up_stage.getX() - event.getScreenX();
				yOffset = pop_up_stage.getY() - event.getScreenY();
			}
		});
		pop_up_root_pane.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				pop_up_stage.setX(event.getScreenX() + xOffset);
				pop_up_stage.setY(event.getScreenY() + yOffset);
			}
		});
	}

	private void buildPopUpScene() {
		pop_up_scene = new Scene(pop_up_root_pane, POP_UP_ROOT_PANE_WIDTH, POP_UP_ROOT_PANE_HEIGHT);
		pop_up_scene.getStylesheets().add("/resources/css/pop_up_window.css");
		pop_up_scene.setFill(null);
	}

	private void buildPopUpWindowRootPane() {
		pop_up_window_root_pane = new BorderPane();
		pop_up_window_root_pane.setId("pop_up_window_root_pane");
		pop_up_window_root_pane.setPrefSize(POP_UP_ROOT_PANE_WIDTH, POP_UP_ROOT_PANE_HEIGHT);
		pop_up_window_root_pane.setMaxSize(POP_UP_ROOT_PANE_WIDTH, POP_UP_ROOT_PANE_HEIGHT);
		buildPopUpWindowTopBar();
		pop_up_root_pane.setCenter(pop_up_window_root_pane);
	}

	private void buildPopUpWindowTopBar() {
		VBox pop_up_window_top_bar = new VBox();
		if (type.equals(PopUpType.SUCCESSFULLY) || type.equals(PopUpType.TWITTERTOKEN)
				|| type.equals(PopUpType.FACEBOOKTOKEN)) {
			pop_up_window_top_bar.setId("pop_up_window_top_bar_successfully");
		} else {
			pop_up_window_top_bar.setId("pop_up_window_top_bar");
		}
		pop_up_window_top_bar.setPrefSize(POP_UP_ROOT_PANE_WIDTH, POP_UP_WINDOW_TOP_BAR_HEIGHT);
		pop_up_window_top_bar.setMaxSize(POP_UP_ROOT_PANE_WIDTH, POP_UP_WINDOW_TOP_BAR_HEIGHT);
		HBox pop_up_window_top_bar_buttons_container = new HBox();
		pop_up_window_top_bar_buttons_container.setId("pop_up_window_top_bar_buttons_container");
		Button close_button = new Button();
		close_button.setId("pop_up_window_top_bar_close_button");
		close_button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				confirmation = false;
				pop_up_stage.close();
			}
		});
		pop_up_window_top_bar_buttons_container.getChildren().addAll(close_button);
		pop_up_window_top_bar.getChildren().addAll(pop_up_window_top_bar_buttons_container);
		pop_up_window_root_pane.setTop(pop_up_window_top_bar);
	}

	private void buildPopUpContent(String text) {
		VBox pop_up_container = new VBox();
		pop_up_container.setId("pop_up_container");
		HBox pop_up_buttons_container = new HBox();
		pop_up_buttons_container.setId("pop_up_buttons_container");
		if (type.equals(PopUpType.WARNING)) {
			Label warning_label = new Label();
			warning_label.setId("warning_label");
			pop_up_container.getChildren().add(warning_label);
			pop_up_buttons_container.getChildren().addAll(buildOkButton());

		} else if (type.equals(PopUpType.CONFIRMATION)) {
			Label confirmation_label = new Label();
			confirmation_label.setId("confirmation_label");
			pop_up_container.getChildren().add(confirmation_label);
			pop_up_buttons_container.getChildren().addAll(buildOkButton(), buildCancelButton());
		} else if (type.equals(PopUpType.SUCCESSFULLY)) {
			Label successfully_label = new Label();
			successfully_label.setId("successfully_label");
			pop_up_container.getChildren().add(successfully_label);
			pop_up_buttons_container.getChildren().addAll(buildOkButton());
		} else if (type.equals(PopUpType.FACEBOOKTOKEN)) {
			Label input_label = new Label();
			input_label.setId("successfully_label");
			pop_up_container.getChildren().add(input_label);
			pop_up_buttons_container.getChildren().addAll(buildFacebookInputPane(), buildOkButton());
		} else if (type.equals(PopUpType.TWITTERTOKEN)) {
			Label input_label = new Label();
			input_label.setId("successfully_label");
			pop_up_container.getChildren().add(input_label);
			pop_up_buttons_container.getChildren().addAll(buildTwitterInputPane(), buildOkButton());
		}
		pop_up_container.getChildren().addAll(new Text(text), pop_up_buttons_container);
		pop_up_window_root_pane.setCenter(pop_up_container);
	}

	private Button buildOkButton() {
		Button ok_button = new Button();
		ok_button.setId("ok_button");
		ok_button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				confirmation = true;
				pop_up_stage.close();
			}
		});
		return ok_button;
	}

	private Button buildCancelButton() {
		Button cancel_button = new Button();
		cancel_button.setId("cancel_button");
		cancel_button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				confirmation = false;
				pop_up_stage.close();
			}
		});
		return cancel_button;
	}

	private VBox buildFacebookInputPane() {
		VBox facebook_access_token_pane = new VBox();
		facebook_access_token_pane.setId("input_pane");
		facebook_access_token = new TextField("Insert Facebook token");
		facebook_access_token.setId("input_text_field");
		facebook_access_token.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				if (facebook_access_token.getText().equals("Insert Facebook token")) {
					facebook_access_token.setText("");
				}
			}
		});
		facebook_access_token_pane.getChildren().add(facebook_access_token);
		return facebook_access_token_pane;
	}

	private VBox buildTwitterInputPane() {
		VBox input_pane_container = new VBox();

		HBox consumer_key_pane = new HBox();
		consumer_key_pane.setId("input_pane");
		consumerKey = new TextField("Insert consumer key");
		consumerKey.setId("input_text_field");
		consumerKey.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				if (consumerKey.getText().equals("Insert consumer key")) {
					consumerKey.clear();
				}
			}
		});
		consumer_key_pane.getChildren().add(consumerKey);

		HBox consumer_secret_pane = new HBox();
		consumer_key_pane.setId("input_pane");
		consumerSecret = new TextField("Insert consumer secret");
		consumerSecret.setId("input_text_field");
		consumerSecret.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				if (consumerSecret.getText().equals("Insert consumer secret")) {
					consumerSecret.clear();
				}
			}
		});
		consumer_key_pane.getChildren().add(consumerSecret);

		HBox user_access_token_pane = new HBox();
		user_access_token_pane.setId("input_pane");
		userAccessToken = new TextField("Insert access token");
		userAccessToken.setId("input_text_field");
		userAccessToken.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				if (userAccessToken.getText().equals("Insert access token")) {
					userAccessToken.clear();
				}
			}
		});
		user_access_token_pane.getChildren().add(userAccessToken);

		HBox access_token_secret_pane = new HBox();
		access_token_secret_pane.setId("input_pane");
		accessTokenSecret = new TextField("Insert access secret");
		accessTokenSecret.setId("input_text_field");
		accessTokenSecret.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				if (accessTokenSecret.getText().equals("Insert access secret")) {
					accessTokenSecret.clear();
				}
			}
		});
		access_token_secret_pane.getChildren().add(accessTokenSecret);
		input_pane_container.getChildren().addAll(consumer_key_pane, consumer_secret_pane, user_access_token_pane,
				access_token_secret_pane);
		return input_pane_container;
	}

	public boolean getConfirmation() {
		return confirmation;
	}

	public String getFacebookToken() {
		String facebook_input = facebook_access_token.getText();
		return facebook_input;
	}

	public String getTwitterToken() {
		String twitter_tokens = consumerKey.getText() + "," + consumerSecret.getText() + "," + userAccessToken.getText()
				+ "," + accessTokenSecret.getText();
		return twitter_tokens;
	}

	private void startPopUpStage() {
		pop_up_stage.setScene(pop_up_scene);
		pop_up_stage.showAndWait();
	}
}
