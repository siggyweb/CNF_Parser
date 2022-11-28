package computation.derivation;

import computation.contextfreegrammar.*;

/**
 * Represents a step in a derivation. See {@link Derivation the derivation class}
 * for more details.
 * 
 * @see Derivation
 */
public class Step {

	/** The word. */
	private Word word;

	/** The rule. */
	private Rule rule;

	/** The index. */
	private int index;

	/**
	 * Instantiates a new step.
	 *
	 * @param word the word
	 * @param rule the rule
	 * @param index the index
	 */
	public Step(Word word, Rule rule, int index) {
		this.word = word;
		this.rule = rule;
		this.index = index;
	}

	/**
	 * Gets the word.
	 *
	 * @return the word
	 */
	public Word getWord() {
		return word;
	}

	/**
	 * Gets the rule.
	 *
	 * @return the rule
	 */
	public Rule getRule() {
		return rule;
	}

	/**
	 * Gets the index.
	 *
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * Checks if is start symbol.
	 *
	 * @return true, if is start symbol
	 */
	public boolean isStartSymbol() {
		return index == -1;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "(" + word + ", " + rule + ", " + index + ")"; 
	}

}
