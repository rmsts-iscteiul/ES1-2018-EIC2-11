package gui;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import javax.mail.Message;
import javax.mail.MessagingException;

import apps.EmailApp;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
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
	private HBox email_post_window_root_pane;
	private BorderPane view_email_post_window_root_pane;
	private BorderPane write_email_post_window_root_pane;

	private EmailApp email_app;
	private Message message;

	private TextField from_text_field;
	private TextField to_text_field;
	private TextField subject_text_field;
	private TextArea write_email_post_text_area;

	private static final int EMAIL_POST_SHADOW_GAP = 10;
	private static final int EMAIL_POST_ROOT_PANE_WIDTH = 900;
	private static final int EMAIL_POST_ROOT_PANE_HEIGHT = 600;
	private static final int VIEW_EMAIL_POST_ROOT_PANE_WIDTH = 400;
	private static final int VIEW_EMAIL_POST_ROOT_PANE_HEIGHT = 600;
	private static final int WRITE_EMAIL_POST_ROOT_PANE_WIDTH = 300;
	private static final int WRITE_EMAIL_POST_ROOT_PANE_HEIGHT = 420;
	private static final int EMAIL_POST_WINDOW_TOP_BAR_HEIGHT = 20;

	private final static String TEMP_DIRECTORY = "C:/temp";

	private double xOffset, yOffset;

	public EmailPostWindow(Stage main_stage, EmailApp email_app, Message message) throws MessagingException {
		this.email_app = email_app;
		this.message = message;
		configureEmailPostStage(main_stage);
		createEmailPostRootPane();
		buildViewEmailPostWindowRootPane();
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

		email_post_window_root_pane = new HBox();
		email_post_window_root_pane.setId("email_post_window_root_pane");
		email_post_root_pane.setCenter(email_post_window_root_pane);
	}

	private void buildEmailPostScene() {
		email_post_scene = new Scene(email_post_root_pane, EMAIL_POST_ROOT_PANE_WIDTH, EMAIL_POST_ROOT_PANE_HEIGHT);
		email_post_scene.getStylesheets().add("/resources/css/email_post_window.css");
		email_post_scene.setFill(null);
	}

	private void buildViewEmailPostWindowRootPane() throws MessagingException {
		view_email_post_window_root_pane = new BorderPane();
		view_email_post_window_root_pane.setId("view_email_post_window_root_pane");
		view_email_post_window_root_pane.setPrefSize(VIEW_EMAIL_POST_ROOT_PANE_WIDTH, VIEW_EMAIL_POST_ROOT_PANE_HEIGHT);
		view_email_post_window_root_pane.setMaxSize(VIEW_EMAIL_POST_ROOT_PANE_WIDTH, VIEW_EMAIL_POST_ROOT_PANE_HEIGHT);
		email_post_window_root_pane.getChildren().add(view_email_post_window_root_pane);
		buildViewEmailPostWindowTopBar();

	}

	private void buildWriteEmailPostWindowRootPane() throws MessagingException {
		write_email_post_window_root_pane = new BorderPane();
		write_email_post_window_root_pane.setId("write_email_post_window_root_pane");
		write_email_post_window_root_pane.setPrefSize(WRITE_EMAIL_POST_ROOT_PANE_WIDTH,
				WRITE_EMAIL_POST_ROOT_PANE_HEIGHT);
		write_email_post_window_root_pane.setMaxSize(WRITE_EMAIL_POST_ROOT_PANE_WIDTH,
				WRITE_EMAIL_POST_ROOT_PANE_HEIGHT);
		email_post_window_root_pane.getChildren().add(write_email_post_window_root_pane);
		buildWriteEmailPostWindowTopBar();
		buildWriteEmailPostContent();
	}

	private void buildViewEmailPostWindowTopBar() throws MessagingException {
		VBox email_post_window_top_bar = new VBox();
		email_post_window_top_bar.setId("view_email_post_window_top_bar");
		email_post_window_top_bar.setPrefSize(VIEW_EMAIL_POST_ROOT_PANE_WIDTH, EMAIL_POST_WINDOW_TOP_BAR_HEIGHT);
		email_post_window_top_bar.setMaxSize(VIEW_EMAIL_POST_ROOT_PANE_WIDTH, EMAIL_POST_WINDOW_TOP_BAR_HEIGHT);
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
		view_email_post_window_root_pane.setTop(email_post_window_top_bar);
	}

	private void buildWriteEmailPostWindowTopBar() throws MessagingException {
		VBox email_post_window_top_bar = new VBox();
		email_post_window_top_bar.setId("write_email_post_window_top_bar");
		email_post_window_top_bar.setPrefSize(WRITE_EMAIL_POST_ROOT_PANE_WIDTH, EMAIL_POST_WINDOW_TOP_BAR_HEIGHT);
		email_post_window_top_bar.setMaxSize(WRITE_EMAIL_POST_ROOT_PANE_WIDTH, EMAIL_POST_WINDOW_TOP_BAR_HEIGHT);
		HBox email_post_window_top_bar_buttons_container = new HBox();
		email_post_window_top_bar_buttons_container.setId("email_post_window_top_bar_buttons_container");
		// TOP
		HBox view_email_post_top_container = new HBox();
		view_email_post_top_container.setId("view_email_post_top_container");
		Button send_button = new Button("Send");
		send_button.setId("send_button");
		send_button.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (email_app.sendEmail(to_text_field.getText(), write_email_post_text_area.getText())) {
					write_email_post_window_root_pane.setVisible(false);
				}
			}
		});
		Button remove_button = new Button("Remove");
		remove_button.setId("remove_button");
		remove_button.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				email_post_window_root_pane.getChildren().remove(write_email_post_window_root_pane);
			}
		});
		view_email_post_top_container.getChildren().addAll(remove_button, send_button);
		final Pane spacer = new Pane();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		email_post_window_top_bar_buttons_container.getChildren().addAll(spacer, view_email_post_top_container);
		email_post_window_top_bar.getChildren().addAll(email_post_window_top_bar_buttons_container);
		write_email_post_window_root_pane.setTop(email_post_window_top_bar);
	}

	private void buildEmailPostContent() {
		BorderPane email_post_container = new BorderPane();
		email_post_container.setId("view_email_post_container");
		// TOP
		HBox view_email_post_top_container = new HBox();
		view_email_post_top_container.setId("view_email_post_top_container");
		Button reply_button = new Button("Reply");
		reply_button.setId("reply_button");
		reply_button.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				try {
					buildWriteEmailPostWindowRootPane();
				} catch (MessagingException e) {
					e.printStackTrace();
				}
			}
		});
		Button forward_button = new Button("Forward");
		forward_button.setId("forward_button");
		forward_button.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				try {
					buildWriteEmailPostWindowRootPane();
				} catch (MessagingException e) {
					e.printStackTrace();
				}
			}
		});
		view_email_post_top_container.getChildren().addAll(reply_button, forward_button);
		email_post_container.setTop(view_email_post_top_container);
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
		FlowPane view_fill_bottom = new FlowPane();
		view_fill_bottom.setId("view_fill_bottom");
		view_fill_bottom.setPrefHeight(EMAIL_POST_WINDOW_TOP_BAR_HEIGHT);
		view_fill_bottom.setMaxHeight(EMAIL_POST_WINDOW_TOP_BAR_HEIGHT);
		email_post_container.setBottom(view_fill_bottom);

		view_email_post_window_root_pane.setCenter(email_post_container);

	}

	private void buildWriteEmailPostContent() {
		FlowPane write_email_post_container = new FlowPane(Orientation.VERTICAL);
		write_email_post_container.setId("write_email_post_container");
		// From
		HBox from_write_email_post_top_container = new HBox();
		from_write_email_post_top_container.setId("write_email_top_field_container");
		Label from_label = new Label("From: ");
		from_text_field = new TextField("your_email@example.com");
		from_text_field.setId("name_text_field");
		from_write_email_post_top_container.getChildren().addAll(from_label, from_text_field);
		// To
		HBox to_write_email_post_top_container = new HBox();
		to_write_email_post_top_container.setId("write_email_top_field_container");
		Label to_label = new Label("To: ");
		to_text_field = new TextField("email@example.com");
		to_text_field.setId("name_text_field");
		to_text_field.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (to_text_field.getText().equals("email@example.com")) {
					to_text_field.clear();
				}
			}
		});
		to_write_email_post_top_container.getChildren().addAll(to_label, to_text_field);
		// Subject
		HBox subject_write_email_post_top_container = new HBox();
		subject_write_email_post_top_container.setId("write_email_top_field_container");
		Label subject_label = new Label("Subject: ");
		subject_text_field = new TextField("subject");
		subject_text_field.setId("name_text_field");
		subject_text_field.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (subject_text_field.getText().equals("subject")) {
					subject_text_field.clear();
				}
			}
		});
		subject_write_email_post_top_container.getChildren().addAll(subject_label, subject_text_field);
		write_email_post_container.getChildren().addAll(to_write_email_post_top_container,
				subject_write_email_post_top_container);
		// CENTER
		VBox write_email_post_text_area_container = new VBox();
		write_email_post_text_area_container.setId("write_email_post_text_area_container");
		write_email_post_text_area = new TextArea();
		write_email_post_text_area.setId("write_email_post_text_area");
		write_email_post_text_area_container.getChildren().add(write_email_post_text_area);
		write_email_post_container.getChildren().add(write_email_post_text_area_container);

		write_email_post_window_root_pane.setCenter(write_email_post_container);
	}

	private void startEmailPostStage() {
		email_post_stage.setScene(email_post_scene);
		email_post_stage.showAndWait();
	}
}
