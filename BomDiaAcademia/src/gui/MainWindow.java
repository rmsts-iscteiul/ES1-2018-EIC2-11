package gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainWindow extends Application {

	private Stage main_stage;
	private Scene scene;
	private Pane root_pane;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage main_stage) throws Exception {
		try {
			this.main_stage = main_stage;
			configureStage();
			createRootPane();
			buildScene(root_pane);
			startStage(scene);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void configureStage() {
		main_stage.setTitle("Bom dia Academia");
		main_stage.initStyle(StageStyle.TRANSPARENT);
	}

	private void createRootPane() {
		Pane root_pane = new WindowPane();
		this.root_pane = root_pane;
		root_pane.setId("root_pane");
	}

	private void buildScene(Pane root_pane) {
		final Scene scene = new Scene(root_pane, 1000, 600);
		this.scene = scene;
	}

	private void startStage(Scene scene) {
		main_stage.setScene(scene);
		main_stage.show();
	}

}
