package primitivevisualizer;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

/**
 * @author Sam Hooper
 *
 */
public class Bit extends Pane {
	private static final String SET_STRING = "1";
	private static final String CLEARED_STRING = "0";
	public final Label label;
	private boolean state;
	public Bit() {
		super();
		label = new Label(CLEARED_STRING);
		label.setAlignment(Pos.CENTER);
		VBox box = new VBox();
		box.setAlignment(Pos.CENTER);
		box.getChildren().add(label);
		box.prefWidthProperty().bind(widthProperty());
		getChildren().add(box);
	}
	
	public boolean state() {
		return state;
	}
	
	public void flip() {
		if(state)
			label.setText(CLEARED_STRING);
		else
			label.setText(SET_STRING);
		state = !state;
	}
	
	public boolean isSet() {
		return state;
	}
	public void set() {
		label.setText(SET_STRING);
		state = true;
	}
	
	public void clear() {
		label.setText(CLEARED_STRING);
		state = false;
	}
}
