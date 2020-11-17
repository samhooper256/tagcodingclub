package club11_17_20;

import java.util.*;
import java.util.stream.*;

//This is my solution to the problem in class (an evaluator with only +, -, /, *, with no parenthesis or negation).
public class BasicEvaluator {
	
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		while(in.hasNextLine())
			System.out.println(evaluate(in.nextLine()));
	}
	
	public static double evaluate(String expression) {
		List<String> tokens = Arrays.stream(expression.split("(?<=[-+/*])|(?=[-+/*])")).collect(Collectors.toList());
		while(tokens.contains("*") || tokens.contains("/")) {
			int multIndex = tokens.indexOf("*"), divIndex = tokens.indexOf("/");
			int index;
			if(multIndex == -1)
				index = divIndex;
			else if(divIndex == -1)
				index = multIndex;
			else
				index = Math.min(divIndex, multIndex);
			double a = Double.parseDouble(tokens.get(index - 1)), b = Double.parseDouble(tokens.get(index + 1));
			double result;
			if("*".equals(tokens.get(index))) {
				result = a * b;
			}
			else {
				result = a / b;
			}
			tokens.set(index, String.valueOf(result));
			tokens.remove(index + 1);
			tokens.remove(index - 1);
		}
		
		while(tokens.contains("-") || tokens.contains("+")) {
			int addIndex = tokens.indexOf("+"), subtractIndex = tokens.indexOf("-");
			int index;
			if(addIndex == -1)
				index = subtractIndex;
			else if(subtractIndex == -1)
				index = addIndex;
			else
				index = Math.min(subtractIndex, addIndex);
			double a = Double.parseDouble(tokens.get(index - 1)), b = Double.parseDouble(tokens.get(index + 1));
			double result;
			if("+".equals(tokens.get(index))) {
				result = a + b;
			}
			else {
				result = a - b;
			}
			tokens.set(index, String.valueOf(result));
			tokens.remove(index + 1);
			tokens.remove(index - 1);
		}
		
		return Double.parseDouble(tokens.get(0));
	}
}
