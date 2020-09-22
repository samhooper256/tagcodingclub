package primitivevisualizer;

import java.util.List;
import javafx.scene.paint.Color;

/**
 * @author Sam Hooper
 *
 */
public class FloatDisplay extends SignedNumericDisplay {
	public static final String NAME = "float";
	public static final int BITS = 32;
	private static final int EXPONENT_BITS = 8;
	private static final int FRACTION_BITS = 23;
	private static final List<SegmentInfo> segments;
	static {
		segments = List.of(new SegmentInfo("sign", 1, Color.GREEN), new SegmentInfo("exponent", EXPONENT_BITS, Color.BLUE), 
				new SegmentInfo("fraction", FRACTION_BITS, Color.RED));
	}
	
	private int intBits;
	private float floatValue;
	
	public FloatDisplay() {
		super(NAME, BITS, segments);
		intBits = 0;
		floatValue = 0;
	}
	
	@Override
	public void flipBitOnBinaryDisplay(int index) {
		bits[index].flip();
		intBits ^= (1 << (BITS - index - 1));
		floatValue = Float.intBitsToFloat(intBits);
	}
	
	@Override
	public void flipSignOnBinaryDisplay() {
		floatValue = -floatValue;
		intBits = Float.floatToRawIntBits(intBits);
		setDisplayBits(intBits);
	}

	@Override
	public void updateValueIndicator() {
		valueIndicator.setText("Decimal Value: " + floatValue);
	}
}


