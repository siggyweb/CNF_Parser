package computation.contextfreegrammar;

/**
 * A superclass for variables and terminals.
 */
public abstract class Symbol {

	/** The symbol. */
	private char symbol;

	/**
	 * Instantiates a new symbol.
	 *
	 * @param symbol the symbol
	 */
	public Symbol(char symbol) {
		if(symbol == 'ε') {
			throw new IllegalArgumentException("ε is reserved for the empty word (see Word.emptyWord).");
		}
		this.symbol = symbol;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "" + symbol;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + symbol;
		return result;
	}

	/* (non-Javadoc)
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
		Symbol other = (Symbol) obj;
		if (symbol != other.symbol)
			return false;
		return true;
	}

	/**
	 * Checks if this symbol is a terminal (rather than variable).
	 * <p>
	 * This so we don't have to use instanceof Terminal, though
	 * we might choose to for specific reasons.
	 * 
	 * @return true if terminal, false if variable
	 */
	public abstract boolean isTerminal();

}
