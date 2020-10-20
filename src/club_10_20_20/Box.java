package club_10_20_20;

public class Box {
	
	private Object item;
	
	public Box() {
		this.item = null;
	}
	
	public Box(Object item) {
		this.item = item;
	}
	
	public Object getItem() {
		return item;
	}
	
	public void setItem(Object item) {
		this.item = item;
	}
	
}