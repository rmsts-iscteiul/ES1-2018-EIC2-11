package gui;

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

public class AddUserWindow {

	private Stage add_user_stage;
	private Scene add_user_scene;
	private BorderPane add_user_root_pane;
	private BorderPane add_user_window_root_pane;

	private static final int ADD_USER_SHADOW_GAP = 10;
	private static final int ADD_USER_ROOT_PANE_WIDTH = 400;
	private static final int ADD_USER_ROOT_PANE_HEIGHT = 500;
	private static final int ADD_USER_WINDOW_TOP_BAR_HEIGHT = 20;

	private double xOffset, yOffset;

	public AddUserWindow(Stage main_stage) {
		configureAddUserStage(main_stage);
		createAddUserRootPane();
		buildAddUserWindowRootPane();
		buildAddUserContent();
		buildAddUserScene();
		startAddUserStage();
	}

	private void configureAddUserStage(Stage main_stage) {
		add_user_stage = new Stage();
		add_user_stage.initModality(Modality.APPLICATION_MODAL);
		add_user_stage.initOwner(main_stage);
		add_user_stage.initStyle(StageStyle.TRANSPARENT);
	}

	private void createAddUserRootPane() {
		BorderPane add_user_root_pane = new BorderPane();
		this.add_user_root_pane = add_user_root_pane;
		add_user_root_pane.setPrefSize(ADD_USER_ROOT_PANE_WIDTH + ADD_USER_SHADOW_GAP,
				ADD_USER_ROOT_PANE_HEIGHT + ADD_USER_SHADOW_GAP);
		add_user_root_pane.setMaxSize(ADD_USER_ROOT_PANE_WIDTH + ADD_USER_SHADOW_GAP,
				ADD_USER_ROOT_PANE_HEIGHT + ADD_USER_SHADOW_GAP);
		add_user_root_pane.setId("add_user_root_pane");
		HBox top_gap_pane = new HBox();
		top_gap_pane.setPrefSize(ADD_USER_ROOT_PANE_WIDTH + ADD_USER_SHADOW_GAP, ADD_USER_SHADOW_GAP);
		top_gap_pane.setId("add_user_shadow_gap");
		HBox bottom_gap_pane = new HBox();
		bottom_gap_pane.setPrefSize(ADD_USER_ROOT_PANE_WIDTH + ADD_USER_SHADOW_GAP, ADD_USER_SHADOW_GAP);
		bottom_gap_pane.setId("add_user_shadow_gap");
		VBox left_gap_pane = new VBox();
		left_gap_pane.setPrefSize(ADD_USER_SHADOW_GAP, ADD_USER_ROOT_PANE_HEIGHT + ADD_USER_SHADOW_GAP);
		left_gap_pane.setId("add_user_shadow_gap");
		VBox right_gap_pane = new VBox();
		right_gap_pane.setPrefSize(ADD_USER_SHADOW_GAP, ADD_USER_ROOT_PANE_HEIGHT + ADD_USER_SHADOW_GAP);
		right_gap_pane.setId("add_user_shadow_gap");
		add_user_root_pane.setTop(top_gap_pane);
		add_user_root_pane.setBottom(bottom_gap_pane);
		add_user_root_pane.setLeft(left_gap_pane);
		add_user_root_pane.setRight(right_gap_pane);
		add_user_root_pane.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				xOffset = add_user_stage.getX() - event.getScreenX();
				yOffset = add_user_stage.getY() - event.getScreenY();
			}
		});
		add_user_root_pane.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				add_user_stage.setX(event.getScreenX() + xOffset);
				add_user_stage.setY(event.getScreenY() + yOffset);
			}
		});
	}

	private void buildAddUserScene() {
		add_user_scene = new Scene(add_user_root_pane, ADD_USER_ROOT_PANE_WIDTH, ADD_USER_ROOT_PANE_HEIGHT);
		add_user_scene.getStylesheets().add("/resources/css/add_user_window.css");
		add_user_scene.setFill(null);
	}

	private void buildAddUserWindowRootPane() {
		add_user_window_root_pane = new BorderPane();
		add_user_window_root_pane.setId("add_user_window_root_pane");
		add_user_window_root_pane.setPrefSize(ADD_USER_ROOT_PANE_WIDTH, ADD_USER_ROOT_PANE_HEIGHT);
		add_user_window_root_pane.setMaxSize(ADD_USER_ROOT_PANE_WIDTH, ADD_USER_ROOT_PANE_HEIGHT);
		buildPopUpWindowTopBar();
		add_user_root_pane.setCenter(add_user_window_root_pane);
	}

	private void buildPopUpWindowTopBar() {
		HBox add_user_window_top_bar = new HBox();
		add_user_window_top_bar.setId("add_user_window_top_bar");
		add_user_window_top_bar.setPrefSize(ADD_USER_ROOT_PANE_WIDTH, ADD_USER_WINDOW_TOP_BAR_HEIGHT);
		add_user_window_top_bar.setMaxSize(ADD_USER_ROOT_PANE_WIDTH, ADD_USER_WINDOW_TOP_BAR_HEIGHT);
		FlowPane add_user_window_top_bar_buttons_container = new FlowPane();
		add_user_window_top_bar_buttons_container.setId("add_user_window_top_bar_buttons_container");
		Button add_user_window_top_bar_close_button = new Button();
		add_user_window_top_bar_close_button.setId("add_user_window_top_bar_close_button");
		add_user_window_top_bar_close_button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				add_user_stage.close();
			}
		});
		add_user_window_top_bar_buttons_container.getChildren().add(add_user_window_top_bar_close_button);
		add_user_window_top_bar.getChildren().addAll(add_user_window_top_bar_buttons_container);
		add_user_window_root_pane.setTop(add_user_window_top_bar);
	}

	private void buildAddUserContent() {
		FlowPane add_user_container = new FlowPane(Orientation.VERTICAL);
		add_user_container.setId("add_user_container");

		VBox add_user_welcome_container = new VBox();
		add_user_welcome_container.setId("add_user_welcome_container");
		Label welcome_label = new Label("Welcome to \nBom dia Academia");

		add_user_welcome_container.getChildren().addAll(welcome_label);
		// First name
		HBox first_name_text_field_container = new HBox();
		first_name_text_field_container.setId("add_user_field_container");
		TextField first_name_text_field = new TextField("First name");
		first_name_text_field.setId("name_text_field");
		first_name_text_field.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (first_name_text_field.getText().equals("First name")) {
					first_name_text_field.clear();
				}
			}
		});
		first_name_text_field_container.getChildren().addAll(first_name_text_field);
		// Last name
		HBox last_name_text_field_container = new HBox();
		last_name_text_field_container.setId("add_user_field_container");
		TextField last_name_text_field = new TextField("Last name");
		last_name_text_field.setId("name_text_field");
		last_name_text_field.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (last_name_text_field.getText().equals("Last name")) {
					last_name_text_field.clear();
				}
			}
		});
		last_name_text_field_container.getChildren().addAll(last_name_text_field);
		// Password
		HBox password_field_container = new HBox();
		password_field_container.setId("add_user_password_field_container");
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
		// Repeat password
		HBox repeat_password_field_container = new HBox();
		repeat_password_field_container.setId("add_user_password_field_container");
		Label repeat_password_icon = new Label();
		repeat_password_icon.setId("password_icon");
		PasswordField repeat_password_field = new PasswordField();
		repeat_password_field.setId("password_field");
		repeat_password_field.setText("password");
		repeat_password_field.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (repeat_password_field.getText().equals("password")) {
					repeat_password_field.clear();
				}
			}
		});
		repeat_password_field_container.getChildren().addAll(repeat_password_icon, repeat_password_field);
		// Create account button
		HBox add_user_create_account_button_container = new HBox();
		add_user_create_account_button_container.setId("add_user_create_account_button_container");
		Button add_user_create_account_button = new Button("Create account");
		add_user_create_account_button.setId("add_user_create_account_button");
		add_user_create_account_button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				if (password_field.getText().equals(repeat_password_field.getText())) {
					auxClasses.User user = new auxClasses.User(first_name_text_field.getText(), last_name_text_field.getText(),
							password_field.getText(), null, null, null, last_name_text_field.getText(), "0");
					user.saveNewUser();
					new PopUpWindow(add_user_stage, PopUpType.SUCCESSFULLY, "Your account was added successfully!");
					add_user_stage.close();
				} else {
					password_field_container.setId("add_user_wrong_password_field_container");
					repeat_password_field_container.setId("add_user_wrong_password_field_container");
				}

			}
		});
		add_user_create_account_button_container.getChildren().addAll(add_user_create_account_button);
		// Add all to user container
		add_user_container.getChildren().addAll(add_user_welcome_container, first_name_text_field_container,
				last_name_text_field_container, password_field_container, repeat_password_field_container,
				add_user_create_account_button_container);
		add_user_window_root_pane.setCenter(add_user_container);
	}

	private void startAddUserStage() {
		add_user_stage.setScene(add_user_scene);
		add_user_stage.showAndWait();
	}
}
