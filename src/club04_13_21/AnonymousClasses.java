package club04_13_21;

public class AnonymousClasses {

	public static void main(String[] args) {
		
		Object o = new Object() {
			
			int x = 4;
			
			@Override
			public String toString() {
				return "Anonymous :D";
			}
			
		};
		
		System.out.println(o);
		
		Runnable r = new Runnable() {

			@Override
			public void run() {
				System.out.println("Action!");
			}
			
			
		};
		
		r.run();
		
	}
	
}
