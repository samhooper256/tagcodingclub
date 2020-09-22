package primitivevisualizer;

import java.util.List;
import javafx.scene.paint.Color;

/**
 * @author Sam Hooper
 *
 */
public class LongDisplay extends SignedNumericDisplay {
	public static final String NAME = "long";
	public static final int BITS = 64;
	private static final List<SegmentInfo> segments;
	static {
		segments = List.of(new SegmentInfo("sign", 1, Color.GREEN), new SegmentInfo("magnitude", BITS - 1, Color.RED));
	}
	
	private long value;
	
	public LongDisplay() {
		super(NAME, BITS, segments);
		value = 0;
	}
	
	@Override
	public void flipBitOnBinaryDisplay(int index) {
		bits[index].flip();
		long shiftDistance = BITS - index - 1L;
		value ^= (1L << shiftDistance);
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

