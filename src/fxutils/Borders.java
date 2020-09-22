package fxutils;

import javafx.scene.layout.*;
import javafx.scene.paint.Paint;

/**
 * @author Sam Hooper
 *
 */
public class Borders {
	public static Border of(Paint paint) {
		return new Border(new BorderStroke(paint, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1)));
	}
	public static Border of(Paint paint, BorderStrokeStyle style) {
		return new Border(new BorderStroke(paint, style, CornerRadii.EMPTY, new BorderWidths(1)));
	}
	public static Border of(Paint paint, BorderStrokeStyle style, int thickness) {
		return new Border(new BorderStroke(paint, style, CornerRadii.EMPTY, new BorderWidths(thickness)));
	}
}
