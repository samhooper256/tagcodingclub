package club10_20_20;

public class GenericBox<E> {
	
	private E item;
	
	public GenericBox() {
		this.item = null;
	}
	
	public GenericBox(E item) {
		this.item = item;
	}
	
	public E getItem() {
		return item;
	}
	
	public void setItem(E item) {
		this.item = item;
	}
	
}
