package club03_23_21;

import java.util.regex.Pattern;

public class Regex {
	
	public static void main(String[] args) {
		
		String s = "\\"; //s holds 1 character: '\'
		System.out.println(s);
		System.out.printf("length = %d%n", s.length());
		
		String regex = "\\d"; //regex holds 2 characters: '\d'
		Pattern p = Pattern.compile(regex);
		
		System.out.println(p.matcher("3").matches()); //true
		
		System.out.println("hello123".matches("\\w+\\d*\\s*")); //true
		
		
		//The regular expression language also has ITS OWN escape sequences, which also
		//use '\'. For example, to match a literal '(', you must escape it using '\('.
		//However, since you write regex with Java Strings, you need to use a Java escape
		//sequence to escape the first '\', leaving you with the regex (as a Java String):
		//"\\("
		
		System.out.println("(".matches("\\(")); //true
		//If I hadn't used any backslashes, I would get an exception, since '(' is a special
		//character in the regex language.
		
		
	}
	
}
