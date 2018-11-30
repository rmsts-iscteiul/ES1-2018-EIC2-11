package gui;

import apps.EmailApp;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

	private Stage pop_up_stage;
	private Scene pop_up_scene;
	private BorderPane pop_up_root_pane;
	private BorderPane pop_up_window_root_pane;
	
	private EmailApp email_app;

	private static final int POP_UP_SHADOW_GAP = 10;
	private static final int POP_UP_ROOT_PANE_WIDTH = 400;
	private static final int POP_UP_ROOT_PANE_HEIGHT = 300;
	private static final int POP_UP_WINDOW_TOP_BAR_HEIGHT = 20;

	private double xOffset, yOffset;

	public LoginWindow(Stage main_stage, EmailApp email_app) {
		this.email_app = email_app;
		configurePopUpStage(main_stage);
		createPopUpRootPane();
		buildPopUpWindowRootPane();
		buildPopUpContent();
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
		pop_up_root_pane.setId("login_root_pane");
		HBox top_gap_pane = new HBox();
		top_gap_pane.setPrefSize(POP_UP_ROOT_PANE_WIDTH + POP_UP_SHADOW_GAP, POP_UP_SHADOW_GAP);
		top_gap_pane.setId("login_shadow_gap");
		HBox bottom_gap_pane = new HBox();
		bottom_gap_pane.setPrefSize(POP_UP_ROOT_PANE_WIDTH + POP_UP_SHADOW_GAP, POP_UP_SHADOW_GAP);
		bottom_gap_pane.setId("login_shadow_gap");
		VBox left_gap_pane = new VBox();
		left_gap_pane.setPrefSize(POP_UP_SHADOW_GAP, POP_UP_ROOT_PANE_HEIGHT + POP_UP_SHADOW_GAP);
		left_gap_pane.setId("login_shadow_gap");
		VBox right_gap_pane = new VBox();
		right_gap_pane.setPrefSize(POP_UP_SHADOW_GAP, POP_UP_ROOT_PANE_HEIGHT + POP_UP_SHADOW_GAP);
		right_gap_pane.setId("login_shadow_gap");
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
		pop_up_scene.getStylesheets().add("/resources/css/login_window.css");
		pop_up_scene.setFill(null);
	}

	private void buildPopUpWindowRootPane() {
		pop_up_window_root_pane = new BorderPane();
		pop_up_window_root_pane.setId("login_window_root_pane");
		pop_up_window_root_pane.setPrefSize(POP_UP_ROOT_PANE_WIDTH, POP_UP_ROOT_PANE_HEIGHT);
		pop_up_window_root_pane.setMaxSize(POP_UP_ROOT_PANE_WIDTH, POP_UP_ROOT_PANE_HEIGHT);
		buildPopUpWindowTopBar();
		pop_up_root_pane.setCenter(pop_up_window_root_pane);
	}

	private void buildPopUpWindowTopBar() {
		HBox pop_up_window_top_bar = new HBox();
		pop_up_window_top_bar.setId("login_window_top_bar");
		pop_up_window_top_bar.setPrefSize(POP_UP_ROOT_PANE_WIDTH, POP_UP_WINDOW_TOP_BAR_HEIGHT);
		pop_up_window_top_bar.setMaxSize(POP_UP_ROOT_PANE_WIDTH, POP_UP_WINDOW_TOP_BAR_HEIGHT);
		FlowPane pop_up_window_top_bar_buttons_container = new FlowPane();
		pop_up_window_top_bar_buttons_container.setId("login_window_top_bar_buttons_container");
		Button close_button = new Button();
		close_button.setId("login_window_top_bar_close_button");
		close_button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				pop_up_stage.close();
			}
		});
		pop_up_window_top_bar_buttons_container.getChildren().add(close_button);
		pop_up_window_top_bar.getChildren().addAll(pop_up_window_top_bar_buttons_container);
		pop_up_window_root_pane.setTop(pop_up_window_top_bar);
	}

	private void buildPopUpContent() {
		FlowPane login_container = new FlowPane(Orientation.VERTICAL);
		login_container.setId("login_container");
		
		HBox email_text_field_container = new HBox();
		email_text_field_container.setId("email_text_field_container");
		Button email_icon = new Button();
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
		HBox password_field_container = new HBox();
		password_field_container.setId("password_field_container");
		Button password_icon = new Button();
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
		Button login_button = new Button("Login");
		login_button.setId("login_button");
		login_button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				email_app.setUser(email_text_field.getText());
				email_app.setPassword(password_field.getText());
				pop_up_stage.close();
				
			}
		});
		login_container.getChildren().addAll(email_text_field_container, password_field_container, login_button);
		pop_up_window_root_pane.setCenter(login_container);
	}

	private void startPopUpStage() {
		pop_up_stage.setScene(pop_up_scene);
		pop_up_stage.showAndWait();
	}
}

