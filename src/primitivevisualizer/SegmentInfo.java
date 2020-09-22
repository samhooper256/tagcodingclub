package primitivevisualizer;

import javafx.scene.paint.Color;

/**
 * @author Sam Hooper
 *
 */
public class SegmentInfo {
	public final String name;
	public final int length;
	public final Color color;
	public SegmentInfo(String name, int length, Color color) {
		this.name = name;
		this.length = length;
		this.color = color;
	}
}
