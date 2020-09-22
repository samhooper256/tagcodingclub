package primitivevisualizer;

import java.util.*;

import javafx.beans.property.DoubleProperty;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

/**
 * @author Sam Hooper
 *
 */
public enum MainPanel {
	INSTANCE;
	private final StackPane mainPanel;
	private final Map<String, NumericDisplay> map;
	private HBox topBar;
	private VBox vBox;
	private NumericDisplay currentDisplay;
	private HBox slidersBox;
	private Slider fontSizeSlider, segmentLabelSizeSlider, valueIndicatorSizeSlider;
	
	MainPanel() {
		map = new LinkedHashMap<>();
		mainPanel = new StackPane();
		mainPanel.setPadding(new Insets(10));
		slidersBox = new HBox(8);
		fontSizeSlider = new Slider(6, 100, 18);
		segmentLabelSizeSlider = new Slider(6, 100, 18);
		valueIndicatorSizeSlider = new Slider(6, 100, 18);
		slidersBox.getChildren().addAll(new Label("Font Size: "), fontSizeSlider, new Label("Segment Label Size: "), segmentLabelSizeSlider,
				new Label("Value Indicator Size: "), valueIndicatorSizeSlider);
		vBox = new VBox();
		topBar = new HBox(10);
		topBar.setPadding(new Insets(0, 0, 5, 0));
		vBox.getChildren().addAll(topBar, slidersBox);
		vBox.maxWidthProperty().bind(mainPanel.maxWidthProperty());
		mainPanel.setAlignment(Pos.CENTER);
		mainPanel.getChildren().add(vBox);
	}
	
	void fillMapAndTopBar() {
		map.put(ByteDisplay.NAME, new ByteDisplay());
		map.put(ShortDisplay.NAME, new ShortDisplay());
		map.put(CharDisplay.NAME, new CharDisplay());
		map.put(IntDisplay.NAME, new IntDisplay());
		map.put(LongDisplay.NAME, new LongDisplay());
		map.put(FloatDisplay.NAME, new FloatDisplay());
		map.put(DoubleDisplay.NAME, new DoubleDisplay());
		for(Map.Entry<String, NumericDisplay> entry : map.entrySet()) {
			Button b = new Button(entry.getKey());
			b.setOnMouseClicked(mouseEvent -> setDisplayTo(entry.getValue()));
			topBar.getChildren().add(b);
		}
	}
	void setDisplayTo(String displayName) {
		setDisplayTo(map.get(displayName));
	}
	
	private void setDisplayTo(NumericDisplay display) {
		int index = vBox.getChildren().indexOf(currentDisplay);
		if(index == -1)
			vBox.getChildren().add(display);
		else
			vBox.getChildren().set(index, display);
		VBox.setVgrow(display, Priority.ALWAYS);
		currentDisplay = display;
	}
	
	public DoubleProperty fontSizeProperty() {
		return fontSizeSlider.valueProperty();
	}
	
	public DoubleProperty segmentLabelSizeProperty() {
		return segmentLabelSizeSlider.valueProperty();
	}
	
	public DoubleProperty valueIndicatorSizeProperty() {
		return valueIndicatorSizeSlider.valueProperty();
	}
	
	public Pane getPane() {
		return mainPanel;
	}
}
