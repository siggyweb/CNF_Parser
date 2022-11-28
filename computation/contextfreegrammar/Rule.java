package computation.contextfreegrammar;

/**
 * Represents a rule in a context free grammar.
 */
public class Rule {

	/** The variable (left hand side). */
	private Variable variable;

	/** The expansion (right hand side). */
	private Word expansion;

	/**
	 * Instantiates a new rule with the given values.
	 *
	 * @param variable the variable
	 * @param expansion the expansion
	 */
	public Rule(Variable variable, Word expansion) {
		this.variable = variable;
		this.expansion = expansion;
	}

	/**
	 * Gets the variable (left hand side).
	 *
	 * @return the variable
	 */
	public Variable getVariable() {
		return variable;
	}

	/**
	 * Gets the expansion (right hand side).
	 *
	 * @return the expansion
	 */
	public Word getExpansion() {
		return expansion;
	}

	/**
	 * Produces a nice readable version of the rule, e.g. A → BC
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return variable + " → " + expansion;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((expansion == null) ? 0 : expansion.hashCode());
		result = prime * result + ((variable == null) ? 0 : variable.hashCode());
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
		Rule other = (Rule) obj;
		if (expansion == null) {
			if (other.expansion != null)
				return false;
		} else if (!expansion.equals(other.expansion))
			return false;
		if (variable == null) {
			if (other.variable != null)
				return false;
		} else if (!variable.equals(other.variable))
			return false;
		return true;
	}

}
