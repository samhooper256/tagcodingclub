package codetesting;

/**
 * @author Sam Hooper
 *
 */
public class Assertions {
	private static Boolean assertionsEnabled = null;
	
	public static boolean areEnabled() {
		x:
		if(assertionsEnabled == null) {
			try { assert false; }
			catch(AssertionError e) { assertionsEnabled = true; break x;}
			assertionsEnabled = false;
		}
		return assertionsEnabled;
	}
	
	public static <X extends Throwable> void assertThrows(Class<? extends X> throwableClass, Runnable r) {
		try { r.run(); }
		catch(Throwable e) { 
			if(throwableClass.isAssignableFrom(e.getClass()))
				return;
			throw new AssertionError("The given Runnable did not throw the correct exception type; instead it threw a(n) " + e.getClass());
		}
		throw new AssertionError("The given Runnable did not throw any exception");
	}
}
