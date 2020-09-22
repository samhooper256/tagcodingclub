package typevisualizer;

import java.util.Objects;

/**
 * @author Sam Hooper
 *
 */
public class TypeArrow extends Arrow {
	private final TypeIcon source, dest;
	public TypeArrow(TypeIcon source, TypeIcon dest) {
		super();
		Objects.requireNonNull(source);
		Objects.requireNonNull(dest);
		this.source = source;
		this.dest = dest;
	}
	/** Removes this {@code TypeArrow} from the outgoingArrows list of {@code source} and the incomingArrows
	 * list of {@code dest}. Both lists <b>must</b> contain this arrow. Use {@link #severRemaining()} if one or
	 * both lists may not contain the arrow.
	 */
	public void sever() {
		System.out.printf(">>>Attempting to sever arrow.%n\tsource=%s, dest=%s%n\tsource.outgoingArrows=%s, dest.incomingArrows=%s%n",
				source, dest, source.outgoingArrows, dest.incomingArrows);
		boolean b = source.outgoingArrows.remove(this);
		b &= dest.incomingArrows.remove(this);
		if(!b) throw new IllegalArgumentException("This arrow is not properly connected. Either the source or dest does not have "
				+ "this arrow as an incoming/outgoing arrow. source=" + source + ", dest=" + dest);
	}
	void severRemaining() {
//		System.out.printf(">>>Attempting to severRemaining arrow.%n\tsource=%s, dest=%s%n\tsource.outgoingArrows=%s, dest.incomingArrows=%s%n",
//				source, dest, source.outgoingArrows, dest.incomingArrows);
		source.outgoingArrows.remove(this);
		dest.incomingArrows.remove(this);
	}
	public TypeIcon getSource() { return source; }
	public TypeIcon getDest() { return dest; }
	@Override
	public String toString() {
		return String.format("TypeArrow[src=%s, dest=%s]", source.getIconClass().getSimpleName(), dest.getIconClass().getSimpleName());
	}
}
