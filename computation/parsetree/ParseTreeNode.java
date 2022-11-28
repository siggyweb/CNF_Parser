package computation.parsetree;

import computation.contextfreegrammar.Symbol;
import computation.contextfreegrammar.Terminal;
import computation.contextfreegrammar.Variable;
import thirdparty.treeprinter.tech.vanyo.treePrinter.TreePrinter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

/**
 * This class represents a node in a parse tree.
 * <p>
 * Every parse tree has a root node, and so it also represents the
 * entire parse tree.
 * <p>
 * For an example, see the code for the {@link #main(String[]) main method}.
 */
public class ParseTreeNode {

	/** The symbol of this node. */
	private Symbol symbol;

	/** A list of children. We assume this will only ever hold 2 items at most. */
	private List<ParseTreeNode> children;

	/**
	 * Only used internally for the unusual 'empty tree' parse tree.
	 */
	private ParseTreeNode() { this.children = new ArrayList<>(0); }

	/**
	 * Instantiates a new parse tree node with the given symbol and no children.
	 *
	 * @param symbol the symbol
	 */
	public ParseTreeNode(Symbol symbol) {
		this.symbol = symbol;
		this.children = new ArrayList<>(0);
	}

	/**
	 * Instantiates a new parse tree node with the given symbol and the
	 * given children. Throws an IllegalArgumentException if you enter more than
	 * 2 children, as this is not necessary for a grammar in Chomsky
	 * normal form, and the code to print the tree only works for binary trees!
	 *
	 * @param symbol the symbol
	 * @param children the children
	 * @throws IllegalArgumentException if you pass in more than 2 children
	 */
	public ParseTreeNode(Symbol symbol, ParseTreeNode... children) {
		if(children.length > 2) {
			throw new IllegalArgumentException("Only supports binary trees. ParseTreeNode should never have more than 2 children if grammar is in CNF.");
		}

		this.symbol = symbol;
		this.children = Arrays.asList(children);
	}

	/**
	 * Gets the symbol.
	 *
	 * @return the symbol
	 */
	public Symbol getSymbol() {
		return symbol;
	}

	/**
	 * Used internally when rendering the parse tree.
	 * @return the symbol as a string, or ε if this is the 'empty tree'
	 */
	private String getSymbolString() {
		if(symbol == null) {
			return "ε";
		} else {
			return symbol.toString();
		}
	}

	/**
	 * Gets the left child.
	 *
	 * @return the left
	 */
	private ParseTreeNode getLeft() {
		return children.size() > 0 ? children.get(0) : null;
	}

	/**
	 * Gets the right child.
	 *
	 * @return the right
	 */
	private ParseTreeNode getRight() {
		return children.size() > 1 ? children.get(1) : null;
	}

	/**
	 * Set up the TreePrinter
	 * @return a TreePrinter instance that can print ParseTreeNodes
	 */
	private TreePrinter<ParseTreeNode> getPrinter() {
		TreePrinter<ParseTreeNode> printer = new TreePrinter<>(n -> ""+n.getSymbolString(), ParseTreeNode::getLeft, ParseTreeNode::getRight);
		printer.setHspace(2);
		// use square branches
		printer.setSquareBranches(true);

		// option to render single left or right subtree as straight down branch (i.e. no indication of left or right)
		printer.setLrAgnostic(true);
		return printer;
	}

	/**
	 * Prints the entire tree including and below this node.
	 */
	public void print() {
		TreePrinter<ParseTreeNode> printer = getPrinter();
		printer.printTree(this);
		System.out.println();
	}

	/**
	 * The 3rd party TreePrinter only prints, but what if we want the tree as a string? How inconvenient!
	 * But luckily the library allows us to specify a custom PrintStream. So we make a print stream that writes to
	 * a byte array, print into that, then convert it into a string to return.
	 *
	 * @return A string containing what would be printed if you called {@link #print() .print()}.
	 */
	public String toString() {
		TreePrinter<ParseTreeNode> printer = getPrinter();

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(os);
		printer.setPrintStream(ps);

		printer.printTree(this);
		try {
			return os.toString("UTF8");
		} catch (UnsupportedEncodingException e) {
			System.err.println("Error using UTF8 encoding. Make sure you are using Java 8 or higher.");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * A grammar parsing the empty string is a special case that can
	 * break other code. This method will generate special tree just for this derivation.
	 *
	 * It looks like:
	 * <blockquote><pre>
	 * A
	 * |
	 * ε
	 * </pre></blockquote>
	 *
	 * @param variable the variable you want above the empty string
	 * @return the parse tree which just has one variable giving the empty string
	 */
	public static ParseTreeNode emptyParseTree(Variable variable) {
		return new ParseTreeNode(variable, new ParseTreeNode());
	}



	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((children == null) ? 0 : children.hashCode());
		result = prime * result + ((symbol == null) ? 0 : symbol.hashCode());
		return result;
	}

	/**
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
		ParseTreeNode other = (ParseTreeNode) obj;
		if (children == null) {
			if (other.children != null)
				return false;
		} else if (!children.equals(other.children))
			return false;
		if (symbol == null) {
			if (other.symbol != null)
				return false;
		} else if (!symbol.equals(other.symbol))
			return false;
		return true;
	}

	/**
	 * This main method gives an example of how to construct and print a
	 * parse tree using the classes provided. You should read the code
	 * to understand how it works.
	 *
	 * @param args the arguments
	 */
	public static void main(String... args) {
		// Here is an example of constructing a parse tree
		// The grammar is the same as ContextFreeGrammar.simpleCNF()

		// The string to parse is 0011
		/* The parse tree is:
		 *       A₀
		 *      / \
		 *     /   \
		 *    Z     B
		 *    |    / \
		 *    0   A   Y
		 *       / \  |
		 *      Z   Y 1
		 *      |   |
		 *      0   1
		 */

		/* For this example, we first create objects for all symbols.
		 * You should already have objects for them when you come to do this,
		 * but create more if you like – doesn't matter so long as symbols
		 * are correct.
		 */
		Terminal zero = new Terminal('0');
		Terminal one = new Terminal('1');

		Variable A0 = new Variable("A0");
		Variable A = new Variable('A');
		Variable B = new Variable('B');
		Variable Y = new Variable('Y');
		Variable Z = new Variable('Z');


		// Now work from the bottom of the tree upwards, starting with terminals
		// These nodes have no children
		ParseTreeNode node1 = new ParseTreeNode(zero);
		ParseTreeNode node2 = new ParseTreeNode(zero);
		ParseTreeNode node3 = new ParseTreeNode(one);
		ParseTreeNode node4 = new ParseTreeNode(one);

		// Create the layer directly above each terminal, from left to right
		// These nodes have exactly one child
		ParseTreeNode node5 = new ParseTreeNode(Z, node1);
		ParseTreeNode node6 = new ParseTreeNode(Z, node2);
		ParseTreeNode node7 = new ParseTreeNode(Y, node3);
		ParseTreeNode node8 = new ParseTreeNode(Y, node4);

		// Now create the variable expansions, from bottom to top
		// These nodes have exactly two children
		ParseTreeNode node9 = new ParseTreeNode(A, node6, node7);
		ParseTreeNode node10 = new ParseTreeNode(B, node9, node8);
		// Ending with the start symbol at the top
		ParseTreeNode node11 = new ParseTreeNode(A0, node5, node10);

		/* So if I rename the nodes with their object number:
		 *       11
		 *      /  \
		 *     /    \
		 *    5      10
		 *    |     /  \
		 *    1    9    8
		 *        / \   |
		 *       6   7  4
		 *       |   |
		 *       2   3
		 */

		// Finally use the print method on the root node to print a nice version to the console
		node11.print();
	}

}
