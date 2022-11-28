package computation.derivation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import computation.contextfreegrammar.*;

/**
 * A derivation is a list of steps, where a step contains
 * a word, a rule, and an index. So a derivation is a list
 * of these three things. Here is an example of a derivation
 * <p>
 * 	the words that make up the derivation 
 * <blockquote><pre>
 * 		e.g. [ A, AB, AAB, 0AB, 00B, 001 ]
 * </pre></blockquote>
 *	the rule used at each step, or null for the start symbol
 * <blockquote><pre>
 *  	e.g. [ null, A → AB, A → AB, A → 0, A → 0, B → 1 ]
 *  </pre></blockquote> 
 *  the index where the rule was applied (to avoid ambiguity), or
 *  -1 for the start symbol
 *  <blockquote><pre>
 *  	e.g. [ -1, 0, 0, 1, 2 ]
 *  </pre></blockquote>
 * <p>
 * This is a helper class that you may wish to use, but
 * it is by no means essential.
 * <p>
 * The reason it is helpful is because it is much easier
 * to create a parse tree backwards, i.e. from the final
 * word up to the start symbol. But if calculating all
 * possible derivations of length 2n-1, that means being able
 * to retrace our steps. This class simply tracks all of
 * the steps in a given derivation. It will be up to you
 * to retrace said steps and construct the parse tree!
 * <p>
 * The iterator for this class runs backwards, so if you
 * use a for each loop over a Derivation object, it will go
 * from the final element inserted backwards to the first
 * (to aid with the parsing).
 * <p>
 * <b>Again, use of this class is totally optional.</b>
 * <p>
 * It will probably be easier <i>not</i> to use it for
 * the second dynamic programming algorithm.
 */
public class Derivation implements Iterable<Step> {

	/** The derivation. */
	List<Step> derivation;

	/**
	 * Instantiates a new derivation with a single word as a start symbol.
	 *
	 * @param word the word
	 */
	public Derivation(Word word) {
		derivation = new ArrayList<>();
		this.addStartSymbol(word);
	}

	/**
	 * Instantiates a new derivation from an existing derivation. Also
	 * known as a copy constructor.
	 *
	 * @param d the d
	 */
	public Derivation(Derivation d) {
		derivation = new ArrayList<>(d.derivation);
	}

	/**
	 * Adds the start symbol.
	 *
	 * @param word the word
	 */
	private void addStartSymbol(Word word) {
		derivation.add(new Step(word, null, -1));
	}

	/**
	 * Adds a step.
	 *
	 * @param word the next word
	 * @param rule the rule used
	 * @param index the index modified
	 */
	public void addStep(Word word, Rule rule, Integer index) {
		derivation.add(new Step(word, rule, index));
	}

	/**
	 * Gets the latest word in the list.
	 *
	 * @return the latest word
	 */
	public Word getLatestWord() {
		return derivation.get(derivation.size()-1).getWord();
	}

	/**
	 * <b>Important note:</b> this iterator runs backwards!
	 * 
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<Step> iterator() {
		ArrayList<Step> reverse = new ArrayList<>(derivation);
		Collections.reverse(reverse);
		return reverse.iterator();
	}


}
