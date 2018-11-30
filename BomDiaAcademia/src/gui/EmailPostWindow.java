package gui;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import javax.mail.Message;
import javax.mail.MessagingException;

import apps.EmailApp;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class EmailPostWindow {

	private Stage email_post_stage;
	private Scene email_post_scene;
	private BorderPane email_post_root_pane;
	private BorderPane email_post_window_root_pane;

	private EmailApp email_app;
	private Message message;

	private static final int EMAIL_POST_SHADOW_GAP = 10;
	private static final int EMAIL_POST_ROOT_PANE_WIDTH = 400;
	private static final int EMAIL_POST_ROOT_PANE_HEIGHT = 600;
	private static final int EMAIL_POST_WINDOW_TOP_BAR_HEIGHT = 20;

	private final static String TEMP_DIRECTORY = "C:/temp";

	private double xOffset, yOffset;

	public EmailPostWindow(Stage main_stage, EmailApp email_app, Message message) throws MessagingException {
		this.email_app = email_app;
		this.message = message;
		configureEmailPostStage(main_stage);
		createEmailPostRootPane();
		buildEmailPostWindowRootPane();
		buildEmailPostContent();
		buildEmailPostScene();
		startEmailPostStage();
	}

	private void configureEmailPostStage(Stage main_stage) {
		email_post_stage = new Stage();
		email_post_stage.initModality(Modality.NONE);
		email_post_stage.initOwner(main_stage);
		email_post_stage.initStyle(StageStyle.TRANSPARENT);
	}

	private void createEmailPostRootPane() {
		BorderPane email_post_root_pane = new BorderPane();
		this.email_post_root_pane = email_post_root_pane;
		email_post_root_pane.setPrefSize(EMAIL_POST_ROOT_PANE_WIDTH + EMAIL_POST_SHADOW_GAP,
				EMAIL_POST_ROOT_PANE_HEIGHT + EMAIL_POST_SHADOW_GAP);
		email_post_root_pane.setMaxSize(EMAIL_POST_ROOT_PANE_WIDTH + EMAIL_POST_SHADOW_GAP,
				EMAIL_POST_ROOT_PANE_HEIGHT + EMAIL_POST_SHADOW_GAP);
		email_post_root_pane.setId("email_post_root_pane");
		HBox top_gap_pane = new HBox();
		top_gap_pane.setPrefSize(EMAIL_POST_ROOT_PANE_WIDTH + EMAIL_POST_SHADOW_GAP, EMAIL_POST_SHADOW_GAP);
		top_gap_pane.setId("email_post_shadow_gap");
		HBox bottom_gap_pane = new HBox();
		bottom_gap_pane.setPrefSize(EMAIL_POST_ROOT_PANE_WIDTH + EMAIL_POST_SHADOW_GAP, EMAIL_POST_SHADOW_GAP);
		bottom_gap_pane.setId("email_post_shadow_gap");
		VBox left_gap_pane = new VBox();
		left_gap_pane.setPrefSize(EMAIL_POST_SHADOW_GAP, EMAIL_POST_ROOT_PANE_HEIGHT + EMAIL_POST_SHADOW_GAP);
		left_gap_pane.setId("email_post_shadow_gap");
		VBox right_gap_pane = new VBox();
		right_gap_pane.setPrefSize(EMAIL_POST_SHADOW_GAP, EMAIL_POST_ROOT_PANE_HEIGHT + EMAIL_POST_SHADOW_GAP);
		right_gap_pane.setId("email_post_shadow_gap");
		email_post_root_pane.setTop(top_gap_pane);
		email_post_root_pane.setBottom(bottom_gap_pane);
		email_post_root_pane.setLeft(left_gap_pane);
		email_post_root_pane.setRight(right_gap_pane);
		email_post_root_pane.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				xOffset = email_post_stage.getX() - event.getScreenX();
				yOffset = email_post_stage.getY() - event.getScreenY();
			}
		});
		email_post_root_pane.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				email_post_stage.setX(event.getScreenX() + xOffset);
				email_post_stage.setY(event.getScreenY() + yOffset);
			}
		});
	}

	private void buildEmailPostScene() {
		email_post_scene = new Scene(email_post_root_pane, EMAIL_POST_ROOT_PANE_WIDTH, EMAIL_POST_ROOT_PANE_HEIGHT);
		email_post_scene.getStylesheets().add("/resources/css/email_post_window.css");
		email_post_scene.setFill(null);
	}

	private void buildEmailPostWindowRootPane() throws MessagingException {
		email_post_window_root_pane = new BorderPane();
		email_post_window_root_pane.setId("email_post_window_root_pane");
		email_post_window_root_pane.setPrefSize(EMAIL_POST_ROOT_PANE_WIDTH, EMAIL_POST_ROOT_PANE_HEIGHT);
		email_post_window_root_pane.setMaxSize(EMAIL_POST_ROOT_PANE_WIDTH, EMAIL_POST_ROOT_PANE_HEIGHT);
		buildEmailPostWindowTopBar();
		email_post_root_pane.setCenter(email_post_window_root_pane);
	}

	private void buildEmailPostWindowTopBar() throws MessagingException {
		VBox email_post_window_top_bar = new VBox();
		email_post_window_top_bar.setId("email_post_window_top_bar");
		email_post_window_top_bar.setPrefSize(EMAIL_POST_ROOT_PANE_WIDTH, EMAIL_POST_WINDOW_TOP_BAR_HEIGHT);
		email_post_window_top_bar.setMaxSize(EMAIL_POST_ROOT_PANE_WIDTH, EMAIL_POST_WINDOW_TOP_BAR_HEIGHT);
		HBox email_post_window_top_bar_buttons_container = new HBox();
		email_post_window_top_bar_buttons_container.setId("email_post_window_top_bar_buttons_container");
		Label subject_label = new Label(message.getSubject());
		subject_label.setId("subject_label");
		Button close_button = new Button();
		close_button.setId("email_post_window_top_bar_close_button");
		close_button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				email_post_stage.close();
			}
		});
		final Pane spacer = new Pane();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		email_post_window_top_bar_buttons_container.getChildren().addAll(subject_label, spacer, close_button);
		email_post_window_top_bar.getChildren().addAll(email_post_window_top_bar_buttons_container);
		email_post_window_root_pane.setTop(email_post_window_top_bar);
	}

	private void buildEmailPostContent() {
		BorderPane email_post_container = new BorderPane();
		email_post_container.setId("email_post_container");
		// TOP
		HBox email_post_top_container = new HBox();
		Button reply_button = new Button("Reply");
		reply_button.setId("reply_button");
		reply_button.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				// TO-DO
			}
		});
		email_post_top_container.getChildren().addAll(reply_button);
		email_post_container.setTop(email_post_top_container);
		// CENTER
		VBox email_post_center_container = new VBox();
		try {
			WebView webview = new WebView();
			WebEngine webEngine = webview.getEngine();
			webEngine.loadContent(email_app.writePart(message));
			email_post_center_container.getChildren().add(webview);
		} catch (Exception e) {
			e.printStackTrace();
		}
		File dir = new File(TEMP_DIRECTORY + File.separator + message.toString());
		if (dir.exists() && dir.list().length > 0) {
			HBox attachments_pane = new HBox();
			attachments_pane.setId("attachments_pane");
			for (File file : dir.listFiles()) {
				Button file_button = new Button(file.getName());
				file_button.setId("file_button");
				file_button.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent actionEvent) {
						Desktop desktop = Desktop.getDesktop();
						try {
							desktop.open(file);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				});
				attachments_pane.getChildren().addAll(file_button);
			}
			email_post_center_container.getChildren().add(attachments_pane);
		}
		email_post_container.setCenter(email_post_center_container);
		// BOTTOM
		email_post_container.setBottom(new HBox());

		email_post_window_root_pane.setCenter(email_post_container);
	}

	private void startEmailPostStage() {
		email_post_stage.setScene(email_post_scene);
		email_post_stage.showAndWait();
	}
}
