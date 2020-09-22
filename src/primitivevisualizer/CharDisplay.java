package primitivevisualizer;

import java.util.List;

import javafx.scene.paint.Color;

/**
 * @author Sam Hooper
 *
 */
public class CharDisplay extends NumericDisplay {
	public static final String NAME = "char";
	public static final int BITS = 16;
	private static final List<SegmentInfo> segments;
	static {
		segments = List.of(new SegmentInfo("magnitude", BITS, Color.RED));
	}
	
	private char value;
	
	public CharDisplay() {
		super(NAME, BITS, segments, getValueIndicatorTextFor('\u0000'));
		value = '\u0000';
	}
	
	@Override
	public void flipBitOnBinaryDisplay(int index) {
		bits[index].flip();
		value ^= (1 << (BITS - index - 1));
		
	}

	@Override
	public void updateValueIndicator() {
		valueIndicator.setText(getValueIndicatorTextFor(value));
	}
	
	private static String getValueIndicatorTextFor(char c) {
		return "Decimal Value: " + (int) c + ", Character: '" + c + "'";
	}
}
