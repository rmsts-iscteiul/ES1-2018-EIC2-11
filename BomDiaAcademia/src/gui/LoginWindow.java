package gui;

import apps.EmailApp;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LoginWindow {

	private Stage login_stage;
	private Scene login_scene;
	private BorderPane login_root_pane;
	private BorderPane login_window_root_pane;

	private EmailApp email_app;

	private static final int LOGIN_SHADOW_GAP = 10;
	private static final int LOGIN_ROOT_PANE_WIDTH = 400;
	private static final int LOGIN_ROOT_PANE_HEIGHT = 300;
	private static final int LOGIN_WINDOW_TOP_BAR_HEIGHT = 20;

	private double xOffset, yOffset;

	public LoginWindow(Stage main_stage, EmailApp email_app) {
		this.email_app = email_app;
		configureLoginStage(main_stage);
		createLoginRootPane();
		buildLoginWindowRootPane();
		buildLoginContent();
		buildLoginScene();
		startLoginStage();
	}

	private void configureLoginStage(Stage main_stage) {
		login_stage = new Stage();
		login_stage.initModality(Modality.APPLICATION_MODAL);
		login_stage.initOwner(main_stage);
		login_stage.initStyle(StageStyle.TRANSPARENT);
	}

	private void createLoginRootPane() {
		BorderPane pop_up_root_pane = new BorderPane();
		this.login_root_pane = pop_up_root_pane;
		pop_up_root_pane.setPrefSize(LOGIN_ROOT_PANE_WIDTH + LOGIN_SHADOW_GAP,
				LOGIN_ROOT_PANE_HEIGHT + LOGIN_SHADOW_GAP);
		pop_up_root_pane.setMaxSize(LOGIN_ROOT_PANE_WIDTH + LOGIN_SHADOW_GAP,
				LOGIN_ROOT_PANE_HEIGHT + LOGIN_SHADOW_GAP);
		pop_up_root_pane.setId("login_root_pane");
		HBox top_gap_pane = new HBox();
		top_gap_pane.setPrefSize(LOGIN_ROOT_PANE_WIDTH + LOGIN_SHADOW_GAP, LOGIN_SHADOW_GAP);
		top_gap_pane.setId("login_shadow_gap");
		HBox bottom_gap_pane = new HBox();
		bottom_gap_pane.setPrefSize(LOGIN_ROOT_PANE_WIDTH + LOGIN_SHADOW_GAP, LOGIN_SHADOW_GAP);
		bottom_gap_pane.setId("login_shadow_gap");
		VBox left_gap_pane = new VBox();
		left_gap_pane.setPrefSize(LOGIN_SHADOW_GAP, LOGIN_ROOT_PANE_HEIGHT + LOGIN_SHADOW_GAP);
		left_gap_pane.setId("login_shadow_gap");
		VBox right_gap_pane = new VBox();
		right_gap_pane.setPrefSize(LOGIN_SHADOW_GAP, LOGIN_ROOT_PANE_HEIGHT + LOGIN_SHADOW_GAP);
		right_gap_pane.setId("login_shadow_gap");
		pop_up_root_pane.setTop(top_gap_pane);
		pop_up_root_pane.setBottom(bottom_gap_pane);
		pop_up_root_pane.setLeft(left_gap_pane);
		pop_up_root_pane.setRight(right_gap_pane);
		pop_up_root_pane.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				xOffset = login_stage.getX() - event.getScreenX();
				yOffset = login_stage.getY() - event.getScreenY();
			}
		});
		pop_up_root_pane.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				login_stage.setX(event.getScreenX() + xOffset);
				login_stage.setY(event.getScreenY() + yOffset);
			}
		});
	}

	private void buildLoginScene() {
		login_scene = new Scene(login_root_pane, LOGIN_ROOT_PANE_WIDTH, LOGIN_ROOT_PANE_HEIGHT);
		login_scene.getStylesheets().add("/resources/css/login_window.css");
		login_scene.setFill(null);
	}

	private void buildLoginWindowRootPane() {
		login_window_root_pane = new BorderPane();
		login_window_root_pane.setId("login_window_root_pane");
		login_window_root_pane.setPrefSize(LOGIN_ROOT_PANE_WIDTH, LOGIN_ROOT_PANE_HEIGHT);
		login_window_root_pane.setMaxSize(LOGIN_ROOT_PANE_WIDTH, LOGIN_ROOT_PANE_HEIGHT);
		buildPopUpWindowTopBar();
		login_root_pane.setCenter(login_window_root_pane);
	}

	private void buildPopUpWindowTopBar() {
		HBox pop_up_window_top_bar = new HBox();
		pop_up_window_top_bar.setId("login_window_top_bar");
		pop_up_window_top_bar.setPrefSize(LOGIN_ROOT_PANE_WIDTH, LOGIN_WINDOW_TOP_BAR_HEIGHT);
		pop_up_window_top_bar.setMaxSize(LOGIN_ROOT_PANE_WIDTH, LOGIN_WINDOW_TOP_BAR_HEIGHT);
		FlowPane pop_up_window_top_bar_buttons_container = new FlowPane();
		pop_up_window_top_bar_buttons_container.setId("login_window_top_bar_buttons_container");
		Button close_button = new Button();
		close_button.setId("login_window_top_bar_close_button");
		close_button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				login_stage.close();
			}
		});
		pop_up_window_top_bar_buttons_container.getChildren().add(close_button);
		pop_up_window_top_bar.getChildren().addAll(pop_up_window_top_bar_buttons_container);
		login_window_root_pane.setTop(pop_up_window_top_bar);
	}

	private void buildLoginContent() {
		FlowPane login_container = new FlowPane(Orientation.VERTICAL);
		login_container.setId("login_container");

		// Email
		HBox email_text_field_container = new HBox();
		email_text_field_container.setId("email_text_field_container");
		Label email_icon = new Label();
		email_icon.setId("email_icon");
		TextField email_text_field = new TextField("email");
		email_text_field.setId("email_text_field");
		email_text_field.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (email_text_field.getText().equals("email")) {
					email_text_field.clear();
				}
			}
		});
		email_text_field_container.getChildren().addAll(email_icon, email_text_field);
		// Password
		HBox password_field_container = new HBox();
		password_field_container.setId("password_field_container");
		Label password_icon = new Label();
		password_icon.setId("password_icon");
		PasswordField password_field = new PasswordField();
		password_field.setId("password_field");
		password_field.setText("password");
		password_field.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (password_field.getText().equals("password")) {
					password_field.clear();
				}
			}
		});
		password_field_container.getChildren().addAll(password_icon, password_field);
		// Login
		HBox login_button_container = new HBox();
		login_button_container.setId("login_button_container");
		Button login_button = new Button("Login");
		login_button.setId("login_button");
		login_button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				email_app.setUser(email_text_field.getText());
				email_app.setPassword(password_field.getText());
				login_stage.close();
			}
		});
		login_button_container.getChildren().addAll(login_button);
		// Add all to login container
		login_container.getChildren().addAll(email_text_field_container, password_field_container,
				login_button_container);
		login_window_root_pane.setCenter(login_container);
	}

	private void startLoginStage() {
		login_stage.setScene(login_scene);
		login_stage.showAndWait();
	}
}
