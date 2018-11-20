package gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LoginWindow {

	private Stage pop_up_stage;
	private Scene pop_up_scene;
	private BorderPane pop_up_root_pane;
	private BorderPane pop_up_window_root_pane;

	private static final int POP_UP_SHADOW_GAP = 10;
	private static final int POP_UP_ROOT_PANE_WIDTH = 400;
	private static final int POP_UP_ROOT_PANE_HEIGHT = 300;
	private static final int POP_UP_WINDOW_TOP_BAR_HEIGHT = 20;

	private double xOffset, yOffset;

	public LoginWindow(Stage main_stage, PopUpType type) {
		configurePopUpStage(main_stage);
		createPopUpRootPane();
		buildPopUpWindowRootPane();
		buildPopUpContent(type);
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
		HBox pop_up_window_top_bar = new HBox();
		pop_up_window_top_bar.setId("pop_up_window_top_bar");
		pop_up_window_top_bar.setPrefSize(POP_UP_ROOT_PANE_WIDTH, POP_UP_WINDOW_TOP_BAR_HEIGHT);
		pop_up_window_top_bar.setMaxSize(POP_UP_ROOT_PANE_WIDTH, POP_UP_WINDOW_TOP_BAR_HEIGHT);
		FlowPane pop_up_window_top_bar_buttons_container = new FlowPane();
		pop_up_window_top_bar_buttons_container.setId("pop_up_window_top_bar_buttons_container");
		Button close_button = new Button();
		close_button.setId("pop_up_window_top_bar_close_button");
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

	private void buildPopUpContent(PopUpType type) {
		VBox pop_up_container = new VBox();
		pop_up_container.setId("pop_up_container");
		GridPane gridPane = new GridPane();
		gridPane.setId("grid_container");
		gridPane.setPadding(new Insets(20, 20, 20, 20));
		gridPane.setHgap(5);
		gridPane.setVgap(5);
		Label lblUserName = new Label("Username");
		final TextField txtUserName = new TextField();
		Label lblPassword = new Label("Password");
		final PasswordField pf = new PasswordField();
		Button btnLogin = new Button("Login");
		final Label lblMessage = new Label();
		gridPane.add(lblUserName, 0, 0);
		gridPane.add(txtUserName, 1, 0);
		gridPane.add(lblPassword, 0, 1);
		gridPane.add(pf, 1, 1);
		gridPane.add(btnLogin, 2, 1);
		gridPane.add(lblMessage, 1, 2);
		btnLogin.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				String checkUser = txtUserName.getText();
				String checkPw = pf.getText();
				pop_up_stage.close();
				
			}
		});
		pop_up_container.getChildren().addAll(gridPane);
		pop_up_window_root_pane.setCenter(pop_up_container);
	}

	private void startPopUpStage() {
		pop_up_stage.setScene(pop_up_scene);
		pop_up_stage.showAndWait();
	}
}

