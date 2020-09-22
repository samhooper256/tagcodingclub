package primitivevisualizer;

import java.util.List;
import javafx.scene.paint.Color;

/**
 * @author Sam Hooper
 *
 */
public class IntDisplay extends SignedNumericDisplay {
	public static final String NAME = "int";
	public static final int BITS = 32;
	private static final List<SegmentInfo> segments;
	static {
		segments = List.of(new SegmentInfo("sign", 1, Color.GREEN), new SegmentInfo("magnitude", BITS - 1, Color.RED));
	}
	
	private int value;
	
	public IntDisplay() {
		super(NAME, BITS, segments);
		value = 0;
	}
	
	@Override
	public void flipBitOnBinaryDisplay(int index) {
		bits[index].flip();
		value ^= (1 << (BITS - index - 1));
	}
	
	@Override
	public void flipSignOnBinaryDisplay() {
		value = -value;
		setDisplayBits(value);
	}

	@Override
	public void updateValueIndicator() {
		valueIndicator.setText("Decimal Value: " + value);
	}
}
