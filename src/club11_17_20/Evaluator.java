package club11_17_20;

import java.math.*;
import java.util.*;

public class Evaluator {
	
	private static final MathContext CONTEXT = new MathContext(12); //We use a math context to round intermediate values to 12 decimal places.
	//12 is really an arbitrary amount to round to - the important thing here is that we have to use a MathContext. If we don't,
	//BigDecimal methods will throw an exception if you ask them to represent a nonterminating decimal (such as 1/3 = 0.33333...)
	
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		while(in.hasNextLine()) {
			System.out.println(evaluate(in.nextLine()));
		}
	}
	
	public static BigDecimal evaluate(final String initialExpression) {
		String cleanedExpression = makeExpressionSuitableForEvaluation(initialExpression);
		List<String> infixTokens = getTokensFrom(cleanedExpression);
		List<String> postfixTokens = convertInfixToPostfix(infixTokens);
		BigDecimal result = evaluatePostfix(postfixTokens);
		return result;
	}

	/** Cleans up the expression. Removes whitespace, and replaces all negation operators with '_' characters,
	 * so they are distinguishable from subtraction operators ('-')
	 */
	private static String makeExpressionSuitableForEvaluation(final String expression) {
		char[] resultChars; //I use a char array because it is mutable (unlike Strings). I'll convert it to a String at the end.
		
		//First, let's remove whitespace. I used a regular expression. You could do it with a loop if you wanted to as well.
		resultChars = expression.replaceAll("\\s+", "").toCharArray();
		
		//Then, let's replace negation operators with '_', so that they're distinguishable from subtraction operators:
		for(int i = 0; i < resultChars.length; i++) {
			if(resultChars[i] == '-') {
				if(i == 0 || "(_-+*/^".contains(Character.toString(resultChars[i - 1]))) {
					resultChars[i] = '_';
				}
			}
		}
		//Note that the above loop could be replaced with a single regex. I'll let you figure out what it is if you'd like to :)
		
		return new String(resultChars); //this String constructor just converts a char[] to a String.
	}
	
	/** Returns the tokens comprising the given expression, as a List<String>. 
	 * The tokens are:
	 * "_", "-", "+", "*", "/", "^", "(", ")", and numbers.
	 * */
	private static List<String> getTokensFrom(String cleanedExpression) {
		
		List<String> tokens = new ArrayList<>();
		
		for(int i = 0; i < cleanedExpression.length(); /* No increment */ ) {
			if("()_+-/*^".contains(cleanedExpression.substring(i, i + 1))) {
				tokens.add(cleanedExpression.substring(i, i + 1));
				i++;
			}
			else {
				//We know the token at this point in the String is a number.
				int endOfNumber = i + 1; //index right AFTER the last index of the number.
				while(endOfNumber < cleanedExpression.length() &&
						"123456789.".contains(cleanedExpression.substring(endOfNumber, endOfNumber + 1))) {
					endOfNumber++;
				}
				tokens.add(cleanedExpression.substring(i, endOfNumber));
				i = endOfNumber;
			}
		}
		
		return tokens;
	}
	
	/** Converts the given List of infix tokens to a List<String> of postfix tokens. */
	private static List<String> convertInfixToPostfix(List<String> infixTokens) {
		List<String> postfixTokens = new ArrayList<>();
		Stack<String> operatorStack = new Stack<>(); //temporary stack to store the operators.
		for(String token : infixTokens) {
			if(isOperator(token)) {
				//before we push 'token' onto the operator stack, we pop off (and add to the output list)
				//any operators already on the stack that have greater precedence than 'token' or have
				//equal precedence as 'token' and token is left associative.
				inner:
				while(!operatorStack.isEmpty()) {
					String peek = operatorStack.peek();
					if(isOperator(peek) &&
						(
							precedence(peek) > precedence(token) ||
							(precedence(peek) == precedence(token) && isLeftAssociative(token))
						)
					)
						postfixTokens.add(operatorStack.pop());
					else
						break inner;
				}
				operatorStack.push(token);
			}
			else if(isCloseParenthesis(token)) {
				//when we reach a close parenthesis, it's like we reached the end of the input - but the input is just
				//the stuff between the corresponding opening parenthesis and this parenthesis. Like when we get to the
				//end of the input, we pop off all the remaining operators on the operator stack and add them to the output:
				while(!isOpenParenthesis(operatorStack.peek()))
					postfixTokens.add(operatorStack.pop());
				operatorStack.pop(); //pop off the open parenthesis - there are no parenthesis in postfix!
			}
			else if(isOpenParenthesis(token)) {
				//we add the open parenthesis to the operator stack, and treat it as if it was "the bottom of the stack"
				//until we reach the corresponding closing parenthesis.
				operatorStack.add(token);
			}
			else { //it's a number
				//numbers immediately get added to the output stack as soon as we encounter them in the infix tokens.
				postfixTokens.add(token);
			}
		}
		//Before we're done, we pop off any remaining operators and add them to the output list. We have to do this - we
		//can't have operators that appear in the infix expression but not the postfix expression!
		while(!operatorStack.isEmpty()) {
			postfixTokens.add(operatorStack.pop());
		}
		return postfixTokens;
	}
	
	private static boolean isLeftAssociative(String operator) {
		if(!isOperator(operator))
			throw new IllegalArgumentException(operator + " is not an operator, so it does not have an associativity");
		if(operator.equals("^") || operator.equals("_")) //exponentiation and negation are the only right associative operators that can appear in our expression.
			return false;
		return true;
	}
	
	/**
	 * Returns the precedence of the given operator, as an {@code int}.
	 */
	private static int precedence(String operator) {
		if(!isOperator(operator))
			throw new IllegalArgumentException(operator + " is not an operator, so it does not have a precedence");
		//higher value = higher precedence
		if("+".equals(operator) || "-".equals(operator) || "_".equals(operator))
			return 1;
		if("*".equals(operator) || "/".equals(operator))
			return 2;
		return 3;
		
	}
	
	private static boolean isOperator(String token) {
		return 	"_".equals(token) || "-".equals(token) || "+".equals(token) || "*".equals(token) || "/".equals(token) ||"^".equals(token);
		//There are, of course, better ways to do the above line. One would be to put all the operator Strings into a set 
		//and then call Set.contains(...). That would be shorter and (theoretically) more efficient (O(1) as opposed to O(n)).
	}
	
	private static boolean isOpenParenthesis(String token) {
		return "(".equals(token);
	}
	
	private static boolean isCloseParenthesis(String token) {
		return ")".equals(token);
	}
	
	private static BigDecimal evaluatePostfix(List<String> postfixTokens) {
		Stack<BigDecimal> tempStack = new Stack<>();
		for(String token : postfixTokens) {
			if(isOperator(token)) {
				evaluateOperator(token, tempStack);
			}
			else {
				tempStack.push(new BigDecimal(token));
			}
		}
		if(tempStack.size() != 1)
			throw new IllegalArgumentException("Invalid expression");
		return tempStack.peek();
	}
	
	private static void evaluateOperator(String operator, Stack<BigDecimal> tempStack) {
		if("_".equals(operator)) {
			BigDecimal topOfStack = tempStack.pop();
			tempStack.push(topOfStack.negate(CONTEXT));
		}
		else {
			BigDecimal secondOperand = tempStack.pop(); //Note how the SECOND operand is the one on TOP of the stack,
			BigDecimal firstOperand = tempStack.pop(); 	//and the first operand is the one right BELOW the second on the stack.
														//This is because the order you pop from a stack is the reverse of order you pushed.
			tempStack.push(switch(operator) {
				case "+" -> firstOperand.add(secondOperand, CONTEXT);
				case "-" -> firstOperand.subtract(secondOperand, CONTEXT);
				case "*" -> firstOperand.multiply(secondOperand, CONTEXT);
				case "/" -> firstOperand.divide(secondOperand, CONTEXT);
				case "^" -> power(firstOperand, secondOperand);
				default -> throw new UnsupportedOperationException("Unsupported/unrecognized operator: \"" + operator + "\"");
			});
		}
	}

	private static BigDecimal power(BigDecimal a, BigDecimal b) {
		//Utilizes the property that X^(Y+Z) = X^Y*X^Z
		//BigDecimal has a pow(...) method, but it only accepts an int less than or equal to 999999999.
		//We could just convert our BigDecimals to doubles and use Math.pow(...), but that would loose accuracy,
		//so this method tries to avoid that if possible.
		BigDecimal[] divMod = b.divideAndRemainder(BigDecimal.ONE);
		int integerPartAsInt = divMod[0].intValueExact();
		BigDecimal remainder = divMod[1];
		if(integerPartAsInt <= 999999999) {
			//instead of doing a^b, we do a^i*a^r where 'i' is the integer part of b and 'r' is the remainder,
			//equivalent to 'b-i'. We know i+r = i+(b-i) = i-i+b = b, so we can utilize the exponent property mentioned above.
			BigDecimal result = a.pow(integerPartAsInt, CONTEXT)
					.multiply(BigDecimal.valueOf(Math.pow(a.doubleValue(), remainder.doubleValue())));
			return result;
		}
		else {
			return BigDecimal.valueOf(Math.pow(a.doubleValue(), b.doubleValue()));
		}
	}
}
