package computation.contextfreegrammar;

/**
 * Represents a terminal symbol. Not much to see here!
 */
public class Terminal extends Symbol {

	/**
	 * Instantiates a new terminal.
	 *
	 * @param symbol the symbol
	 */
	public Terminal(char symbol) {
		super(Character.toLowerCase(symbol));
	}

	/* (non-Javadoc)
	 * @see computation.contextfreegrammar.Symbol#isTerminal()
	 */
	@Override
	public boolean isTerminal() {
		return true;
	}

}
