package typevisualizer;

import java.lang.reflect.Modifier;
import java.util.*;

import fxutils.Backgrounds;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.paint.Color;

/**
 * @author Sam Hooper
 *
 */
public class TypeIcon{
	private final Class<?> clazz;
	public final ArrayList<TypeArrow> outgoingArrows, incomingArrows;
	@Override
	public int hashCode() {
		return Objects.hash(clazz);
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		}
		if(obj == null) {
			return false;
		}
		if(getClass() != obj.getClass()) {
			return false;
		}
		TypeIcon other = (TypeIcon) obj;
		return Objects.equals(clazz, other.clazz);
	}

	private final TypeIconPane iconPane;
	public TypeIcon(final Class<?> initClazz) {
		super();
		outgoingArrows = new ArrayList<>();
		incomingArrows = new ArrayList<>();
		iconPane = new TypeIconPane();
		this.clazz = initClazz;
		if(clazz.isInterface())
			iconPane.setBackground(Backgrounds.of(Color.AQUA));
		else if(clazz.isEnum())
			iconPane.setBackground(Backgrounds.of(Color.PINK));
		else if(Modifier.isAbstract(clazz.getModifiers()))
			iconPane.setBackground(Backgrounds.of(Color.BLUE));
		else
			iconPane.setBackground(Backgrounds.of(Color.LIGHTGREEN));
		iconPane.setPadding(new Insets(10));
		Label label = new Label(clazz.getSimpleName());
		iconPane.getChildren().add(label);
		iconPane.setOnDragDetected(mouseEvent -> {
			Dragboard db = iconPane.startDragAndDrop(TransferMode.MOVE);
			ClipboardContent content = new ClipboardContent();
			content.putString("");
			db.setContent(content);
			mouseEvent.consume();
		});
		ContextMenu contextMenu = new ContextMenu();
        MenuItem addDirectSupertypes = new MenuItem("Add direct supertypes");
        addDirectSupertypes.setOnAction(actionEvent -> {
        	TypeBoard.INSTANCE.addDirectSupertypesOfExistingType(clazz);
        });
        MenuItem addDirectSubtypes = new MenuItem("Add direct subtypes");
        addDirectSubtypes.setOnAction(actionEvent -> {
        	TypeBoard.INSTANCE.addDirectSubtypesOfExistingType(clazz);
        });
//        MenuItem addAllSubtypes = new MenuItem("Add all subtypes");
        MenuItem hide = new MenuItem("Hide");
        hide.setOnAction(actionEvent -> {
        	TypeBoard.INSTANCE.removeIcon(this);
        });
        contextMenu.getItems().addAll(addDirectSupertypes,addDirectSubtypes,/*addAllSubtypes,*/hide);
        
        iconPane.setOnMouseClicked(mouseEvent -> {
        	if(mouseEvent.getButton() == MouseButton.SECONDARY)
        		contextMenu.show(iconPane, mouseEvent.getScreenX(), mouseEvent.getScreenY());
        });
	}
	
	public TypeIconPane getPane() {
		return iconPane;
	}
	
	/** Returns the Class<?> this TypeIcon is representing */
	public Class<?> getIconClass() {
		return clazz;
	}
	
	public TypeArrow createSupertypeArrowTo(TypeIcon subtypeIcon) {
		TypeArrow a = new TypeArrow(this, subtypeIcon);
		a.startXProperty().bind(iconPane.centerXBinding());
		a.startYProperty().bind(iconPane.bottomYBinding());
		a.endXProperty().bind(subtypeIcon.iconPane.centerXBinding());
		a.endYProperty().bind(subtypeIcon.iconPane.layoutYProperty());
		outgoingArrows.add(a);
		subtypeIcon.incomingArrows.add(a);
		return a;
	}
	
	/** <b> {@code icon} must be connected to this {@code TypeIcon} by an outgoing arrow (outgoing from this {@code TypeIcon})*/
	public TypeArrow getOutgoingArrowWithDestOf(TypeIcon icon) {
		for(TypeArrow arrow : outgoingArrows)
			if(arrow.getDest().equals(icon))
				return arrow;
		throw new IllegalArgumentException(icon + " is not a destination of one of this TypeIcon's outgoing arrows");
	}
	
	public boolean isDestOfArrowFrom(TypeIcon icon) {
		for(TypeArrow arrow : icon.outgoingArrows)
			if(arrow.getDest().equals(this))
				return true;
		return false;
	}
	
	/** Returns the {@link TypeArrow} outgoing from {@code icon} whose destination is this {@code TypeIcon}.
	 * Throws an {@link IllegalArgumentException} if {@code icon} does not have an outgoing arrow that connects to this
	 * {@code TypeIcon}. */
	public TypeArrow arrowFrom(TypeIcon icon) {
		TypeArrow arrow = arrowFromNullable(icon);
		if(arrow == null) throw new IllegalArgumentException("there is no outgoing arrow from the given icon that connects to this TypeIcon");
		return arrow;
	}
	
	/** Returns the {@link TypeArrow} outgoing from {@code icon} whose destination is this {@code TypeIcon}.
	 * Returns {@code null} if {@code icon} does not have an outgoing arrow that connects to this
	 * {@code TypeIcon}. */
	public TypeArrow arrowFromNullable(TypeIcon icon) {
		for(TypeArrow arrow : icon.outgoingArrows)
			if(arrow.getDest().equals(this))
				return arrow;
		return null;
	}
	
	public TypeIcon getSuperclassIconNullable() {
		for(TypeArrow incomingArrow : incomingArrows)
			if(!incomingArrow.getSource().getIconClass().isInterface())
				return incomingArrow.getSource();
		return null;
	}
	
	public Set<TypeIcon> getSupertypeIcons() {
		Set<TypeIcon> supertypeIcons = new HashSet<>();
		for(TypeArrow a : incomingArrows)
			supertypeIcons.add(a.getSource());
		return supertypeIcons;
	}
	
	public Set<TypeIcon> getSubtypeIcons() {
		Set<TypeIcon> subtypeIcons = new HashSet<>();
		for(TypeArrow a : outgoingArrows)
			subtypeIcons.add(a.getDest());
		return subtypeIcons;
	}
	
	public TypeArrow getSuperclassArrowNullable() {
		for(TypeArrow incomingArrow : incomingArrows)
			if(!incomingArrow.getSource().getIconClass().isInterface())
				return incomingArrow;
		return null;
	}
	
	@Override
	public String toString() {
		return String.format("TypeIcon[clazz=%s]", clazz);
	}	
	
	
}
