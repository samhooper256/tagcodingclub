package primitivevisualizer;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.*;

/**
 * @author Sam Hooper
 *
 */
public class Main extends Application {
	private static MainPanel mainPanel;

	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Scene scene = new Scene(MainPanel.INSTANCE.getPane());
		MainPanel.INSTANCE.fillMapAndTopBar();
		MainPanel.INSTANCE.setDisplayTo(IntDisplay.NAME);
		primaryStage.setScene(scene);
		primaryStage.initStyle(StageStyle.DECORATED);
		primaryStage.show();
	}
	
	public static MainPanel getMainPanel() {
		return mainPanel;
	}
}
