package math;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

/**
 * @author Sam Hooper
 *
 */
public class Fraction extends Number implements Comparable<Fraction> {
	/**
	 * The serial version UID
	 */
	private static final long serialVersionUID = 986583761682199657L;

	/**
	 * Lookup table for powers of ten. {@code POWERS_OF_10[i]} equals <i>10<sup>{@code i}</sup></i>
	 */
	private static final int[] POWERS_OF_10 =
		{1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000, 1000000000};
	
	/**
	 * A constant representing a fraction with value zero. It has a numerator of {@code 0}, a denominator of
	 * {@code 1}, and a positive sign.
	 */
	public static final Fraction ZERO = new Fraction(0, 1, 1);
	
	/**
	 * The numerator of this fraction; it will always be nonnegative
	 */
	private final int num;
	/**
	 * The numerator of this fraction; it will always be positive
	 */
	private final int denom;
	/**
	 * The sign of this fraction; it will always be either {@code -1} (negative) or {@code 1} (positive). A fraction
	 * with value zero will have a sign of {@code 1}.
	 */
	private final int sign;
	
	
	
	private static int gcd(int a, int b) {
		while(b != 0) {
			int t = b;
			b = a % b;
			a = t;
		}
		return a;
	}
	
	public static void divideByZero() {
		throw new ArithmeticException("A Fraction cannot be divided by zero.");
	}
	public static void zeroDenominator() {
		throw new ArithmeticException("A Fraction cannot have a denominator of zero.");
	}
	
	public static final Fraction of(final int numerator, final int denominator) {
		if(numerator == 0) return ZERO;
		if(denominator == 0) zeroDenominator();
		return new Fraction(numerator, denominator);
	}
	
	/**
	 * This method returns {@link #ZERO} if {@code numerator} is {@code 0}.
	 * @param numerator a <b>nonnegative</b> value representing the numerator of this fraction
	 * @param denominator a <b>nonnegative</b> value representing the denominator of this fraction
	 * @param signum the sign of this {@code Fraction}, either {@code 1} or {@code -1}.
	 * @return a fraction with the numerator of {@code numerator}, the denominator of {@code denominator} and a sign
	 * of positive if {@code signum} is {@code 1} or negative is {@code signum} is {@code 1}.
	 */
	private static final Fraction of(final int numerator, final int denominator, final int signum) {
		if(numerator == 0) return ZERO;
		if(denominator == 0) zeroDenominator();
		return new Fraction(numerator, denominator, signum);
	}
	
	/**
	 * Converts a real number, given in string form in radix 10, to a {@code Fraction}. {@code realNumberAsString}
	 * cannot have more than eight significant digits.
	 * @param realNumberAsString a real number given as a string. Some examples are "12.3", "-.7", ".0". "0.", or
	 *  "+182.49234"
	 * @return a {@code Fraction} with the same value as {@code realNumberAsString}. The fraction will be fully reduced.
	 */
	public static final Fraction from(final String realNumberAsString) {
		if(Objects.requireNonNull(realNumberAsString).isBlank()) {
			throw new IllegalArgumentException("Input String is empty or contains only whitespace.");
		}
		final char first = realNumberAsString.charAt(0);
		final int signum = first == '-' ? -1 : 1;
		
		String s = realNumberAsString.substring(first == '-' || first == '+' ? 1 : 0);
		if(s.indexOf('.') == -1) { //handles the no decimal case
			return Fraction.of(Integer.parseInt(s), 1, signum); //use Fraction.of so that it will return a premade value
			//for zero.
		}
		//cut trailing zeros
		for(int i = s.length() - 1; i >= 0; i--) {
			if(s.charAt(i) != '0') {
				s = s.substring(0, i + 1);
				break;
			}
		}
		//cut leading zeros
		for(int i = 0; i < s.length(); i++) {
			if(s.charAt(i) != '0') {
					s = s.substring(i);
					break;
			}
		}
		if(s.equals(".")) return ZERO; //they passed ".0" or "0.0" or "0."
		int pointIndex = s.indexOf('.');
//		System.out.printf("after trimming: s=%s, first=%c, pointIndex=%d, signum=%d, "
//				+ "realNumberAsString=%s%n", s, first, pointIndex, signum, realNumberAsString);
		final String digitsOnly = s.substring(0, pointIndex) + s.substring(pointIndex + 1);
		if(digitsOnly.length() > 8)
			throw new IllegalArgumentException("Input has too many significant digits. Input "
					+ "was: \"" + realNumberAsString + "\"; a maximum of 8 significant digits are allowed.");
//		System.out.printf("digitsOnly=%s%n", digitsOnly);
		return Fraction.of(Integer.parseInt(digitsOnly), POWERS_OF_10[digitsOnly.length() - pointIndex], signum);
	}
	
	/**
	 * A call to this method is equivalent to the call:
	 * <blockquote><pre>
	 * Fraction.from(Double.toString(value));
	 * </pre></blockquote>
	 * @param value the {@code double} value to be converted to a fraction
	 * @return a {@code Fraction} as determined by  {@link #from(String)}.
	 */
	public static final Fraction from(final double value) {
		return Fraction.from(Double.toString(value));
	}
	
	
	private Fraction(final int numerator, final int denominator) {
		this(numerator, denominator, (numerator < 0) ^ (denominator < 0) ? -1 : 1);
		
	}
	private Fraction(final int numerator, final int denominator, final int signum) {
		final int na = Math.abs(numerator), da = Math.abs(denominator);
		final int gcd = gcd(na, da);
		this.sign = signum;
		num = na / gcd;
		denom = da / gcd;
	}
	public Fraction add(final Fraction o) {
		return add(this, o);
	}
	
	public static final Fraction add(final Fraction a, final Fraction b) {
		if(a.num == 0) return b;
		if(b.num == 0) return a;
		final int denomGCD = gcd(a.denom, b.denom); // nonnegative
		int denomLCM = a.denom * b.denom / denomGCD; // nonnegative
		int numSum = a.sign * a.num * (denomLCM / a.denom) + b.sign * b.num * (denomLCM / b.denom); // possibly negative
		return Fraction.of(numSum, denomLCM);
	}
	
	/**
	 * 
	 * @param a the left operand.
	 * @param bnum a <b>non-negative</b> {@code int} representing the numerator of the fraction to sum with {@code a}.
	 * @param bdenom a <b>non-negative</b> {@code int} representing the denominator of the fraction to sum with
	 * {@code a}.
	 * @param bsign an {@code int} with value {@code -1} or {@code 1} representing the sign of the fraction to sum with
	 * {@code a}.
	 * @return a fully reduced {@code Fraction} representing the sum of {@code a} and a fraction with numerator
	 * {@code bnum}, denominator {@code bdenom} and a sign of {@code bsign}.
	 */
	public static final Fraction add(final Fraction a, final int bnum, final int bdenom, final int bsign) {
		if(a.num == 0) return Fraction.of(bnum, bdenom, bsign);
		if(bnum == 0) return a;
		final int denomGCD = gcd(a.denom, bdenom); // nonnegative
		int denomLCM = a.denom * bdenom / denomGCD; // nonnegative
		int numSum = a.sign * a.num * (denomLCM / a.denom) + bsign * bnum * (denomLCM / bdenom); // possibly negative
		return Fraction.of(numSum, denomLCM);
	}
	
	public Fraction multiply(final Fraction o) {
		return multiply(this, o);
	}
	
	public static final Fraction multiply(final Fraction a, final Fraction b) {
		if(a.num == 0 || b.num == 0) return ZERO;
		//to avoid overflow, first reduce:
		int gcd1 = gcd(a.num, b.denom);
		int gcd2 = gcd(a.denom, b.num);
		int an = a.num / gcd1, ad = a.denom / gcd2, bn = b.num / gcd2, bd = b.denom / gcd1;
		
		int resultNum = an * bn, resultDenom = ad * bd;
		return Fraction.of(resultNum, resultDenom, a.sign == b.sign ? 1 : -1);
	}
	
	public static final Fraction multiply(final Fraction a, final int bnum, final int bdenom, final int bsign) {
		if(a.num == 0 || bnum == 0) return ZERO;
		//to avoid overflow, first reduce:
		int gcd1 = gcd(a.num, bdenom);
		int gcd2 = gcd(a.denom, bnum);
		int an = a.num / gcd1, ad = a.denom / gcd2, bn = bnum / gcd2, bd = bdenom / gcd1;
		
		int resultNum = an * bn, resultDenom = ad * bd;
		return Fraction.of(resultNum, resultDenom, a.sign == bsign ? 1 : -1);
	}
	
	public Fraction subtract(final Fraction o) {
		return subtract(this, o);
	}
	public static final Fraction subtract(final Fraction a, final Fraction b) {
		return add(a, b.num, b.denom, -b.sign);
	}
	
	public Fraction divide(final Fraction o) {
		return divide(this, o);
	}
	
	public static final Fraction divide(final Fraction a, final Fraction b) {
		if(b == ZERO) divideByZero();
		return multiply(a, b.denom, b.num, b.sign);
	}
	
	public final BigDecimal toBigDecimal() {
		return BigDecimal.valueOf(num).divide(BigDecimal.valueOf(denom));
	}
	
	public boolean isNonnegative() {
		return sign == 1;
	}
	
	public boolean isNegative() {
		return sign == -1;
	}
	
	public int getNumerator() {
		return num;
	}
	
	public int getDenominator() {
		return denom;
	}
	@Override
	public int hashCode() {
		return Objects.hash(denom, num, sign);
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
		Fraction other = (Fraction) obj;
		return denom == other.denom && num == other.num && sign == other.sign;
	}

	@Override
	public String toString() {
		return String.format("%s%d/%d", sign == -1 ? "-" : "", num, denom);
	}

	@Override
	public int compareTo(Fraction o) {
		return compare(this, o);
	}
	
	public static int compare(final Fraction a, final Fraction b) {
		return Long.compare((long) a.num * b.denom * a.sign, (long) a.denom * b.num * b.sign);
	}
	
	/**
	 * Returns the smaller of the two given {@code Fraction}s. If {@code compare(a, b)} returns {@code 0},
	 * {@code a} is returned.
	 * @param a the first {@code Fraction}
	 * @param b the second {@code Fraction}
	 * @return the smaller of {@code a} and {@code b}
	 * @see #max(Fraction, Fraction)
	 */
	public static Fraction min(final Fraction a, final Fraction b) {
		return compare(a, b) <= 0 ? a : b;
	}
	
	/**
	 * Returns the greater of the two given {@code Fraction}s. If {@code compare(a, b)} returns {@code 0},
	 * {@code a} is returned.
	 * @param a the first {@code Fraction}
	 * @param b the second {@code Fraction}
	 * @return the greater of {@code a} and {@code b}
	 * @see #min(Fraction, Fraction)
	 */
	public static Fraction max(final Fraction a, final Fraction b) {
		return compare(a, b) >= 0 ? a : b;
	}

	@Override
	public int intValue() {
		return sign*num/denom;
	}

	@Override
	public long longValue() {
		return sign*num/denom;
	}

	@Override
	public float floatValue() {
		return 1f * sign*num/denom;
	}

	@Override
	public double doubleValue() {
		return 1d * sign*num/denom;
	}
}
