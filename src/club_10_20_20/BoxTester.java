package club_10_20_20;

public class BoxTester {
	
	public static void main(String[] args) {
		Box box1 = new Box(3);
		Box box2 = new Box("goof");
		
		Object item1 = box1.getItem();
		if(box1.getItem() instanceof Integer) {
			
		}
		Integer i = (Integer) item1;
		Object item2 = box2.getItem();
		
		
	}
	
}
