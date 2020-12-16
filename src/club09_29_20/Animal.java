package club09_29_20;

import java.util.Objects;

public abstract class Animal implements CanSpeak {

	protected int health;

	@Override
	public abstract void speak();

	public Animal() {
		super();
	}

	public int getHealth() {
		return health;
	}

	@Override
	public int hashCode() {
		return Objects.hash(health);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Animal other = (Animal) obj;
		return health == other.health;
	}

	@Override
	public String toString() {
		return "Animal [health=" + health + "]";
	}
}