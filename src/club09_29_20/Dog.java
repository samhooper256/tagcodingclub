package club09_29_20;

public class Dog extends Animal {
	private String name;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void speak() {
		System.out.println("Bark");
	}
	
	public Dog() {
		health = 100;
	}
}
