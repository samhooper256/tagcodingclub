package primitivevisualizer;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;

/**
 * @author Sam Hooper
 *
 */
public class Underscore extends Pane {
	public final Label label;
	private Paint color;
	public Underscore(Pos pos, Paint color) {
		super();
		label = new Label("¯");
		label.setAlignment(Pos.CENTER);
		VBox box = new VBox();
		box.setAlignment(pos);
		box.getChildren().add(label);
		box.prefWidthProperty().bind(widthProperty());
		getChildren().add(box);
		setColor(color);
	}
	
	public void setColor(Paint color) {
		label.setTextFill(color);
		this.color = color;
	}
	
	public Paint getColor() {
		return color;
	}
}
