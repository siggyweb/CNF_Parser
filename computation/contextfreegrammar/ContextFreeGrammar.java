package computation.contextfreegrammar;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/*q*
 * Represents a context free grammar.
 * 
 * @author Andrew Chinery
 */
public class ContextFreeGrammar {

	/** Set of variables. */
	private Set<Variable> variables;

	/** Set of terminals. */
	private Set<Terminal> terminals;


	/** 
	 * List of rules.
	 * Strictly speaking it should be a set of rules as well, but it's convenient to have
	 * them stay in a consistent order, e.g. in 
	 * {@link #ContextFreeGrammar(List<Rule> rules) the rules-only constructor}
	 * we assume the first rule contains the starting variable.
	 */
	private List<Rule> rules;

	/** The start variable. */
	private Variable startVariable;

	/**
	 * Instantiates a new context free grammar with all parts supplied.
	 *
	 * @param variables the variables
	 * @param terminals the terminals
	 * @param rules the rules
	 * @param startVariable the start variable
	 */
	public ContextFreeGrammar(Set<Variable> variables, Set<Terminal> terminals, List<Rule> rules,
			Variable startVariable) {
		this.variables = variables;
		this.terminals = terminals;
		this.rules = rules;
		this.startVariable = startVariable;
	}

	/**
	 * Instantiates a new context free grammar from rules alone.
	 * <p>
	 * This makes the normal assumptions, more or less.
	 * It assumes the first rule contains the start variable.
	 * It pulls all of the left hand side values for the set of variables.
	 * It filters the RHS based on the objects being Terminal objects for
	 * the terminals.
	 *
	 * @param rules the rules
	 */
	public ContextFreeGrammar(List<Rule> rules) {
		this.rules = rules;
		variables = rules.stream().map(Rule::getVariable).collect(Collectors.toSet());
		terminals = rules.stream()						//-a stream of all rules
				.map(Rule::getExpansion) 				//-this pulls out the expansion words
				.flatMap(word -> word.stream())			//-this pulls out the symbols from words
				.filter(n -> n instanceof Terminal)		//-filters for terminals – could use .isTerminal(), 
				// this possibly better for reasons too long for a comment
				.map(n -> (Terminal)n)					//-casts symbols to terminal objects
				.collect(Collectors.toSet()); 			//-converts to a set
		startVariable = rules.get(0).getVariable();
	}

	/**
	 * Gets the variables for this grammar.
	 *
	 * @return the variables
	 */
	public Set<Variable> getVariables() {
		return variables;
	}

	/**
	 * Gets the terminals for this grammar.
	 *
	 * @return the terminals
	 */
	public Set<Terminal> getTerminals() {
		return terminals;
	}

	/**
	 * Gets the rules for this grammar.
	 *
	 * @return the rules
	 */
	public List<Rule> getRules() {
		return rules;
	}

	/**
	 * Gets the start variable.
	 *
	 * @return the start variable
	 */
	public Variable getStartVariable() {
		return startVariable;
	}

	/**
	 * Checks if this grammar is is in Chomsky normal form (CNF).
	 * <p>
	 * You may wish to use this when you write your own grammar object to
	 * check you have done the conversion properly.
	 *
	 * @return true, if grammar is in Chomsky normal form
	 */
	public boolean isInChomskyNormalForm() {
		for(Rule r : rules) {
			// we allow the rule S → ε
			if(r.equals(new Rule(this.startVariable, Word.emptyWord))) {
				continue;
			}

			// otherwise if the rhs is a single terminal it is allowed
			if(r.getExpansion().isTerminal()) {
				continue;
			}

			// otherwise rules must be of the form A → BC where B and C are non-start variables

			// so if the length is > 2 this is not in CNF
			if(r.getExpansion().length() > 2) {
				return false;
			}
			else if(r.getExpansion().length() > 1) {
				// this tests symbols are variables and not the start variable
				if(r.getExpansion().get(0).isTerminal()
						|| r.getExpansion().get(1).isTerminal()
						|| r.getExpansion().get(0).equals(this.startVariable)
						|| r.getExpansion().get(1).equals(this.startVariable)) {
					return false;
				}
			}
			else {
				// otherwise we have ruled out 2-symbol expansions and terminal expansions
				// so this must be a unit variable expansion which is not allowed in CNF
				assert(r.getExpansion().length() == 1);
				assert(!r.getExpansion().isTerminal());
				return false;
			}
		}
		// if tests pass for all rules, we are in CNF
		return true;
	}

	/**
	 * This static method will generate an example context free grammar
	 * which is already in Chomsky Normal Form (CNF). You may wish to
	 * use this to help your understanding of how the code works, and to
	 * start writing your parser with a more simple grammar than the one
	 * in the coursework that you must convert to CNF yourself.
	 * <p>
	 * In particular, this grammar generates our normal example
	 * of a context free language: 0ⁿ1ⁿ, where n ≥ 0
	 * <p>
	 * The rules are: <br>
	 * A₀ → ε<br>
	 * A₀ → ZY<br>
	 * A₀ → ZB<br>
	 * A → ZY<br>
	 * A → ZB<br>
	 * B → AY<br>
	 * Z → 0<br>
	 * Y → 1<br>
	 * <p>
	 * Here is a sample derivation in this grammar: <br>
	 * A₀ → ZB → ZAY → ZZYY → 0ZYY → 00YY → 001Y → 0011 <br>
	 * notice this is 7 steps, which is 2*n - 1 where n is 4,
	 * the length of the final word.
	 *
	 * @return the context free grammar
	 */
	public static ContextFreeGrammar simpleCNF() {
		Variable A0 = new Variable("A0");
		Variable A = new Variable('A');
		Variable B = new Variable('B');
		Variable Z = new Variable('Z');
		Variable Y = new Variable('Y');

		HashSet<Variable> variables = new HashSet<>();
		variables.add(A0);
		variables.add(A);
		variables.add(B);
		variables.add(Z);
		variables.add(Y);

		Terminal zero = new Terminal('0');
		Terminal one = new Terminal('1');

		HashSet<Terminal> terminals = new HashSet<>();
		terminals.add(zero);
		terminals.add(one);

		ArrayList<Rule> rules = new ArrayList<>();
		rules.add(new Rule(A0, Word.emptyWord));
		rules.add(new Rule(A0, new Word(Z, Y)));
		rules.add(new Rule(A0, new Word(Z, B)));
		rules.add(new Rule(A, new Word(Z, Y)));
		rules.add(new Rule(A, new Word(Z, B)));
		rules.add(new Rule(B, new Word(A, Y)));
		rules.add(new Rule(Z, new Word(zero)));
		rules.add(new Rule(Y, new Word(one)));


		ContextFreeGrammar cfg = new ContextFreeGrammar(variables, terminals, rules, A0);
		return cfg;
	}

	/**
	 * Produces a human-readable string for this CFG.
	 * <p>
	 * We do this simply by listing the rules (much like we can 
	 * construct one this way). This is not strictly formal, but 
	 * it is sufficient. 
	 * <p>
	 * You will not need to ever convert a CFG into a string when 
	 * writing your parser, but if you are creating a new CFG (as
	 * you will for part 1) you might want to print it to check
	 * what it is currently doing, to make sure you didn't make
	 * any mistakes. There is an example of this in this class's
	 * {@link #main(String[] args) main method}.
	 * 
	 * @see java.lang.Object#toString()
	 * @see #main(String[] args)
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(rules.get(0));
		for(int i = 1; i < rules.size(); i++) {
			sb.append("\n").append(rules.get(i).toString());
		}
		return sb.toString();
	}

	/** 
	 * We must produce a hashCode when we override equals.
	 * <p>
	 * You do not need to worry about how this method works in any class.
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((rules == null) ? 0 : rules.hashCode());
		result = prime * result + ((startVariable == null) ? 0 : startVariable.hashCode());
		result = prime * result + ((terminals == null) ? 0 : terminals.hashCode());
		result = prime * result + ((variables == null) ? 0 : variables.hashCode());
		return result;
	}

	/** 
	 * Checks whether two CFGs are equal by comparing all their items.
	 * <p>
	 * You do not need to worry about how this method works in any class.
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ContextFreeGrammar other = (ContextFreeGrammar) obj;
		if (rules == null) {
			if (other.rules != null)
				return false;
		} else if (!rules.equals(other.rules))
			return false;
		if (startVariable == null) {
			if (other.startVariable != null)
				return false;
		} else if (!startVariable.equals(other.startVariable))
			return false;
		if (terminals == null) {
			if (other.terminals != null)
				return false;
		} else if (!terminals.equals(other.terminals))
			return false;
		if (variables == null) {
			if (other.variables != null)
				return false;
		} else if (!variables.equals(other.variables))
			return false;
		return true;
	}

	/**
	 * A main method which demonstrates a context free grammar.
	 * <p>
	 * The first part is a test that the code is working properly.
	 * First we generate the built-in grammar from
	 * {@link #simpleCNF() simpleCNF()}, then create a new
	 * grammar <i>just</i> from its rules, and assert they are
	 * equal. You must run with the -ea VM flag for assertions to work,
	 * but if you do and this assert fails, you have broken the class! 
	 * <p>
	 * Next, we show what happens when you print a grammar. (It prints
	 * the rules.)
	 * <p>
	 * Finally we demonstrate the {@link #isInChomskyNormalForm()} method,
	 * again make sure to turn assertions on, or switch this to a print
	 * statement. 
	 *
	 * @param args the arguments
	 */
	public static void main(String...args) {
		ContextFreeGrammar cfg = ContextFreeGrammar.simpleCNF();
		ContextFreeGrammar cfg2 = new ContextFreeGrammar(cfg.getRules());
		assert(cfg.equals(cfg2));

		System.out.println(cfg2);

		assert(cfg.isInChomskyNormalForm());
	}

}
