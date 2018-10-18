package gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainWindow extends Application {
	
	private Stage main_stage;
	private Scene scene;
	private BorderPane root_pane;
	private FlowPane window_root_pane;
	private BorderPane window_pane;
	
	private double xOffset, yOffset;
	
	private static final int SHADOW_GAP = 10;
	private static final int WINDOW_ROOT_PANE_WIDTH = 1000;
	private static final int WINDOW_ROOT_PANE_HEIGHT = 600;
	private static final int WINDOW_LEFT_MENU_WIDTH = 50;
	private static final int WINDOW_TOP_BAR_HEIGHT = 20;
	
	
	private static final String ICONS = "/resources/icons/";
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage main_stage) throws Exception {
		try {
			this.main_stage = main_stage;
			configureStage();
			createRootPane();
			buildWindowRootPane();
			buildScene();
			startStage();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void configureStage() {
		main_stage.setTitle("Bom dia Academia");
		main_stage.initStyle(StageStyle.TRANSPARENT);
	}

	private void createRootPane() {
		BorderPane root_pane = new BorderPane();
		this.root_pane = root_pane;
		root_pane.setPrefSize(WINDOW_ROOT_PANE_WIDTH + SHADOW_GAP, WINDOW_ROOT_PANE_HEIGHT + SHADOW_GAP);
		root_pane.setId("root_pane");
		createGapToShadowEffect();
	}

	private void buildScene() {
		final Scene scene = new Scene(root_pane);
		scene.getStylesheets().add("/resources/application.css");
		scene.setFill(null);
		this.scene = scene;
	}

	private void startStage() {
		main_stage.setScene(scene);
		main_stage.show();
	}
	
	private void createGapToShadowEffect() {
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
	
	private void buildWindowRootPane() {
		FlowPane window_root_pane = new FlowPane(Orientation.VERTICAL);
		this.window_root_pane = window_root_pane;
		window_root_pane.setId("window_root_pane");
		window_root_pane.setPrefSize(WINDOW_ROOT_PANE_WIDTH, WINDOW_ROOT_PANE_HEIGHT);
		buildWindowRootLeftMenu();
		buildWindowPane();
		root_pane.setCenter(window_root_pane);
	}
	
	private void buildWindowRootLeftMenu() {
		VBox window_left_menu = new VBox();
		window_left_menu.setPrefSize(WINDOW_LEFT_MENU_WIDTH, WINDOW_ROOT_PANE_HEIGHT);
		window_left_menu.setId("window_left_menu");
		Button facebook_icon = new Button(null,
				new ImageView(new Image(getClass().getResourceAsStream(ICONS + "facebook.png"))));
		facebook_icon.setId("window_left_menu_icon");
		Button twitter_icon = new Button(null,
				new ImageView(new Image(getClass().getResourceAsStream(ICONS + "twitter.png"))));
		twitter_icon.setId("window_left_menu_icon");
		Button email_icon = new Button(null, new ImageView(new Image(getClass().getResourceAsStream(ICONS + "email.png"))));
		email_icon.setId("window_left_menu_icon");
		window_left_menu.getChildren().addAll(facebook_icon, twitter_icon, email_icon);
		window_root_pane.getChildren().add(window_left_menu);
	}
	
	private void buildWindowPane() {
		BorderPane window_pane = new BorderPane();
		this.window_pane = window_pane;
		window_pane.setId("window_pane");
		window_pane.setPrefSize(WINDOW_ROOT_PANE_WIDTH - WINDOW_LEFT_MENU_WIDTH, WINDOW_ROOT_PANE_HEIGHT);
		buildWindowTopBar();
		window_root_pane.getChildren().add(window_pane);
	}
	
	private void buildWindowTopBar() {
		HBox window_top_bar = new HBox();
		window_top_bar.setId("window_top_bar");
		window_top_bar.setPrefSize(WINDOW_ROOT_PANE_WIDTH - WINDOW_LEFT_MENU_WIDTH, WINDOW_TOP_BAR_HEIGHT);
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
		Button close_button = new Button(null, new ImageView(new Image(getClass().getResourceAsStream(ICONS + "exit.png"))));
		close_button.setId("window_top_bar_icon");
		close_button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				Platform.exit();
			}
		});
		window_top_bar.getChildren().add(close_button);
		window_pane.setTop(window_top_bar);
	}
}
