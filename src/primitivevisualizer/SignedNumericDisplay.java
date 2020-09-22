package primitivevisualizer;

import java.util.List;

import javafx.scene.control.Button;

/**
 * @author Sam Hooper
 *
 */
public abstract class SignedNumericDisplay extends NumericDisplay {
	protected Button flipSignButton;
	/**
	 * @param name
	 * @param bitCount
	 * @param segments
	 */
	protected SignedNumericDisplay(String name, int bitCount, List<SegmentInfo> segments) {
		super(name, bitCount, segments);
		flipSignButton = new Button("Flip Sign");
		flipSignButton.setOnAction(actionEvent -> flipSignAndUpdateValueDisplay());
		buttons.getChildren().add(flipSignButton); 
	}
	
	public void flipSignAndUpdateValueDisplay() {
		flipSignOnBinaryDisplay();
		updateValueIndicator();
	}
	
	public abstract void flipSignOnBinaryDisplay();

}
