package computation.contextfreegrammar;

import java.util.Arrays;

/**
 * Represents a variable, a.k.a. a nonterminal.
 * <p>
 * This class is slightly more complicated than the Terminal class.
 * To make your life easier, it is nice to have variables named<br>
 * S₀, S₁, S₂,...<br>
 * rather than having to use different letters:<br>
 * S, T, U,...<br>
 * this will become especially clear when you convert a grammar into
 * Chomsky normal form.
 * <p>
 * This class allows for variables with subscripts from 0 to 9, and
 * should handle them consistently as if they were different letters.
 * There is also a {@link #subscriptedVariables(char, int) helper method}
 * which will produce an array of variable objects, 
 * e.g. I want 3 S variables: S₀, S₁, S₂.
 */
public class Variable extends Symbol {

	/** All symbols have a character, variables can have an additional subscript */
	private char subscript = empty;

	/** This null character is used if you just want a single character variable. */
	private final static char empty = '\u0000';

	/** Here are the Unicode subscripts we're using. */
	private final static char[] subscripts = {'₀', '₁', '₂', '₃', '₄', '₅', '₆', '₇', '₈', '₉'}; 

	/**
	 * Instantiates a new variable with a single character symbol. Automatically
	 * converts to upper case.
	 *
	 * @param symbol the symbol for this variable
	 */
	public Variable(char symbol) {
		super(Character.toUpperCase(symbol));
	}

	/**
	 * Instantiates a new variable with a string, must be of length 1 or 2.
	 * <p>
	 * If length 1, will just initialise as if you called
	 * {@link #Variable(char) the other constructor}.
	 * <p>
	 * If length 2, will use the second character as a subscript.
	 * <p>
	 * e.g. this is valid:<br>
	 * {@code new Variable("A3");} <br>
	 * this is not: <br>
	 * {@code new Variable("BB");} <br>
	 * and will throw an exception.
	 * 
	 * @throws IllegalArgumentException if the string is invalid
	 * @param symbolSubscript the symbol subscript
	 */
	public Variable(String symbolSubscript) {
		super(Character.toUpperCase(symbolSubscript.charAt(0)));
		if(symbolSubscript.length() > 1) {
			if(symbolSubscript.length() > 2 || symbolSubscript.charAt(1) < '0' || symbolSubscript.charAt(1) > '9') {
				throw new IllegalArgumentException("Variables must be of the form 'A' or \"A1\"");
			}
			this.subscript = subscripts[symbolSubscript.charAt(1) - '0'];
		}
	}


	/* (non-Javadoc)
	 * @see computation.contextfreegrammar.Symbol#isTerminal()
	 */
	@Override
	public boolean isTerminal() {
		return false;
	}

	/* (non-Javadoc)
	 * @see computation.contextfreegrammar.Symbol#toString()
	 */
	@Override
	public String toString() {
		return this.subscript == empty ? super.toString() : super.toString() + this.subscript;
	}

	/**
	 * This helper method will give you an array of subscripted variables.
	 * <p>
	 * So the following:<br>
	 * {@code subscriptedVariables('S', 3)} <br>
	 * will return an array of 3 subscripted S variables: <br>
	 * {@literal [S₀, S₁, S₂]}
	 *
	 * @param letter the symbol to use for the variables
	 * @param n how many subscripted variables you want, between 1 to 10 inclusive
	 * @return an array of subscripted variables
	 * @throws IllegalArgumentException if not between 1 and 10 inclusive
	 */
	public static Variable[] subscriptedVariables(char letter, int n) {
		if(n < 1 || n > 10) {
			throw new IllegalArgumentException("Can only request between 1 and 10 variables");
		}
		Variable[] variables = new Variable[n];
		for(int i = 0; i<n; i++) {
			variables[i] = new Variable("" + letter + i);
		}
		return variables;
	}

	/* (non-Javadoc)
	 * @see computation.contextfreegrammar.Symbol#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + subscript;
		return result;
	}

	/* (non-Javadoc)
	 * @see computation.contextfreegrammar.Symbol#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Variable other = (Variable) obj;
		if (subscript != other.subscript)
			return false;
		return true;
	}

	/**
	 * A main method demonstrating the use of {@link #subscriptedVariables(char, int) subscriptedVariables}.
	 * <p>
	 * If this doesn't print then your terminal doesn't support Unicode. You can avoid
	 * this by simply not using subscripted variables.
	 *
	 * @param args the arguments
	 */
	public static void main(String... args) {
		System.out.println(Arrays.toString(subscriptedVariables('S', 10)));
	}

}
