package primitivevisualizer;

import java.util.List;

import javafx.beans.binding.Bindings;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
/**
 * @author Sam Hooper
 *
 */
public abstract class NumericDisplay extends StackPane {
	public final String name;
	public final int bitCount;
	protected final Underscore[] underscores;
	protected final Bit[] bits;
	protected GridPane numbersGridPane;
	protected VBox vBox;
	protected Label valueIndicator;
	protected HBox buttons;
	protected Button flipAllBitsButton, setAllBitsButton, clearAllBitsButton;
	protected NumericDisplay(final String name, final int bitCount, List<SegmentInfo> segments) {
		this(name, bitCount, segments, "Decimal Value: 0");
	}
	
	protected NumericDisplay(final String name, final int bitCount, List<SegmentInfo> segments, String defaultValueText) {
		this.name = name;
		this.bitCount = bitCount;
		underscores = new Underscore[bitCount];
		bits = new Bit[bitCount];
		valueIndicator = new Label(defaultValueText);
		valueIndicator.styleProperty().bind(Bindings.concat("-fx-font-size: ", MainPanel.INSTANCE.valueIndicatorSizeProperty(), "; -fx-font-family: monospace"));
		vBox = new VBox();
		vBox.setMinSize(0, 0);
		numbersGridPane = new GridPane();
		numbersGridPane.setAlignment(Pos.CENTER);
		numbersGridPane.setMinSize(0, 0);
		for(int i = 0; i < bitCount; i++) {
			ColumnConstraints r = new ColumnConstraints();
			r.setPercentWidth(100d/bitCount);
			numbersGridPane.getColumnConstraints().add(r);
			bits[i] = new Bit();
			bits[i].label.styleProperty().bind(Bindings.concat("-fx-font-size: ", MainPanel.INSTANCE.fontSizeProperty(), "; -fx-font-family: serif"));
			final int index = i; //so that the lambda can use the value of it.
			bits[i].setOnMouseClicked(mouseEvent -> flipBitAndUpdateValueDisplay(index));
			GridPane.setConstraints(bits[i], i, 0);
			numbersGridPane.getChildren().add(bits[i]);
		}
		setupSegments(segments);
		VBox.setVgrow(numbersGridPane, Priority.ALWAYS);
		
		buttons = new HBox(10);
		flipAllBitsButton = new Button("Flip All Bits");
		flipAllBitsButton.setOnAction(actionEvent -> flipAllBitsAndUpdateValueIndicator());
		setAllBitsButton = new Button("Set All Bits");
		setAllBitsButton.setOnAction(actionEvent -> setAllBitsAndUpdateValueIndicator());
		clearAllBitsButton = new Button("Clear All Bits");
		clearAllBitsButton.setOnAction(actionEvent -> clearAllBitsAndUpdateValueIndicator());
		buttons.getChildren().addAll(setAllBitsButton, clearAllBitsButton, flipAllBitsButton);
		Pane valueDisplayPane = new Pane(valueIndicator);
		valueDisplayPane.setMinSize(0, 0);
		vBox.getChildren().addAll(numbersGridPane, valueDisplayPane, buttons);
		getChildren().add(vBox);
	}
	protected void setupSegments(List<SegmentInfo> segments) {
		int index = 0;
		for(SegmentInfo info : segments) {
			int length = info.length;
			HBox box = new HBox();
			box.setAlignment(Pos.TOP_CENTER);
			Label label = new Label(info.name);
			label.setTextFill(info.color);
			label.styleProperty().bind(Bindings.concat("-fx-font-size: ", MainPanel.INSTANCE.segmentLabelSizeProperty().divide(2), "; -fx-font-family: serif"));
			box.getChildren().add(label);
			GridPane.setConstraints(box, index, 2, length, 1, HPos.CENTER, VPos.TOP);
			numbersGridPane.getChildren().add(box);
			for(int i = 0; i < length; i++) {
				underscores[index] = new Underscore(Pos.TOP_CENTER, info.color);
				underscores[index].label.styleProperty().bind(Bindings.concat("-fx-font-size: ", MainPanel.INSTANCE.fontSizeProperty(), "; -fx-font-family: serif"));
				GridPane.setConstraints(underscores[index], index, 1, 1, 1, HPos.CENTER, VPos.TOP);
				numbersGridPane.getChildren().add(underscores[index]);
				index++;
			}
		}
	}
	
	public String getName() {
		return name;
	}
	
	public void flipAllBitsAndUpdateValueIndicator() {
		flipAllBitsOnBinaryDisplay();
		updateValueIndicator();
	}
	private void flipAllBitsOnBinaryDisplay() {
		for(int i = 0; i < bitCount; i++)
			flipBitOnBinaryDisplay(i);
	}
	
	public void setAllBitsOnBinaryDisplay() {
		for(int i = 0; i < bitCount; i++)
			if(!bits[i].isSet())
				flipBitOnBinaryDisplay(i);
	}
	
	public void setAllBitsAndUpdateValueIndicator() {
		setAllBitsOnBinaryDisplay();
		updateValueIndicator();
	}
	
	public void clearAllBitsOnBinaryDisplay() {
		for(int i = 0; i < bitCount; i++)
			if(bits[i].isSet())
				flipBitOnBinaryDisplay(i);
	}
	
	public void clearAllBitsAndUpdateValueIndicator() {
		clearAllBitsOnBinaryDisplay();
		updateValueIndicator();
	}
	
	public void flipBitAndUpdateValueDisplay(int index) {
		flipBitOnBinaryDisplay(index);
		updateValueIndicator();
	}
	
	/** Does NOT update the decimal value indicator */
	protected void setDisplayBits(long n) {
		for(int i = 0; i < bitCount; i++)
			if((n & 1L << bitCount - i - 1L) == 0)
				bits[i].clear();
			else 
				bits[i].set();
	}
	/** Index is from left to right (most significant to least significant). This method must NOT update {@link #valueIndicator}.
	 * That task is reserved for {@link #updateValueIndicator()}*/
	public abstract void flipBitOnBinaryDisplay(int index);
	public abstract void updateValueIndicator();
}
