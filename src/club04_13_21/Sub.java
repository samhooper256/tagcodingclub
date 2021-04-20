package club04_13_21;

public class Sub extends Super {
	
	//instance initializer
	{
		System.out.println("Hello");
	}
	
	public Sub() {
		//implicit super();
		System.out.printf("Sub()%n");
	}
	
}
