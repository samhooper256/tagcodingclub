package primitivevisualizer;

import java.util.List;
import javafx.scene.paint.Color;

/**
 * @author Sam Hooper
 *
 */
public class DoubleDisplay extends SignedNumericDisplay {
	public static final String NAME = "double";
	public static final int BITS = 64;
	private static final int EXPONENT_BITS = 11;
	private static final int FRACTION_BITS = 52;
	private static final List<SegmentInfo> segments;
	static {
		segments = List.of(new SegmentInfo("sign", 1, Color.GREEN), new SegmentInfo("exponent", EXPONENT_BITS, Color.BLUE), 
				new SegmentInfo("fraction", FRACTION_BITS, Color.RED));
	}
	
	private long longBits;
	private double doubleValue;
	
	public DoubleDisplay() {
		super(NAME, BITS, segments);
		longBits = 0;
		doubleValue = 0;
	}
	
	@Override
	public void flipBitOnBinaryDisplay(int index) {
		bits[index].flip();
		longBits ^= (1L << (BITS - index - 1L));
		doubleValue = Double.longBitsToDouble(longBits);
	}
	
	@Override
	public void flipSignOnBinaryDisplay() {
		doubleValue = -doubleValue;
		longBits = Double.doubleToRawLongBits(longBits);
		setDisplayBits(longBits);
	}

	@Override
	public void updateValueIndicator() {
		valueIndicator.setText("Decimal Value: " + doubleValue);
	}
}

