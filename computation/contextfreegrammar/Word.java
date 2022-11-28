package computation.contextfreegrammar;

import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A class for a string in our context free grammar. We use the term
 * 'word' to refer to a string, since string is already taken in Java!
 */
public class Word implements Iterable<Symbol> {

	/**
	 * The empty word.
	 *
	 * Will print the symbol ε
	 * @see #toString()
	 * */
	public static Word emptyWord = new Word();

	/** The contents of this word. */
	private Symbol[] contents;

	/**
	 * Instantiates a new word with the given symbols.
	 *
	 * @param symbols the symbols
	 */
	public Word(Symbol... symbols) {
		contents = symbols;
	}

	/**
	 * This constructor parses a Java string into a word object.
	 * 
	 * Upper case symbols assumed to be variables.
	 * 
	 * Everything else assumed to be a terminal.
	 * 
	 * @param word the word
	 */
	public Word(String word) {
		this(convertStringToSymbols(word));
	}

	/**
	 * Converts a java char into a Symbol object.
	 * 
	 * Upper case symbols assumed to be variables.
	 * 
	 * Everything else assumed to be a terminal.
	 * 
	 * @param symbol the symbol
	 * @return the symbol
	 */
	private static Symbol convertCharToSymbol(char symbol) {
		if(symbol >= 'A' && symbol <= 'Z') {
			return new Variable(symbol);
		} else {
			return new Terminal(symbol);
		}
	}

	/**
	 * Convert string to symbols.
	 *
	 * @param word the word
	 * @return the symbol[]
	 */
	private static Symbol[] convertStringToSymbols(String word) {
		Symbol[] symbolArray = new Symbol[word.length()];
		int i = 0;
		for(char c : word.toCharArray()) {
			symbolArray[i++] = convertCharToSymbol(c);
		}
		return symbolArray;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if(contents.length == 0) {
			return "ε";
		}
		return Arrays.stream(contents).map(Symbol::toString).collect(Collectors.joining());
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(contents);
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
		Word other = (Word) obj;
		if (!Arrays.equals(contents, other.contents))
			return false;
		return true;
	}

	/**
	 * Returns the length of the word.
	 *
	 * @return the int
	 */
	public int length() {
		return contents.length;
	}

	/**
	 * Counts how many symbols in this word are equal to the target.
	 *
	 * @param target the symbol to count.
	 * @return the number of times this symbol appears
	 */
	public int count(Symbol target) {
		int count = 0;
		for(Symbol s : contents) {
			if(s.equals(target)) {
				count++;
			}
		}
		return count;
	}

	/**
	 * The index in the word of the first instance of this symbol.
	 *
	 * @param target the target
	 * @return the index if it exists, otherwise -1
	 */
	public int indexOfFirst(Symbol target) {
		return indexOfNth(target, 0);
	}

	/**
	 * The index in the word of the nth instance of this symbol.
	 *
	 * Notice this is zero indexed for n: 0 = first, 1 = second ...
	 *
	 * @param target the target
	 * @param n 0 is first, 1 is second, and so on
	 * @return the index if it exists, otherwise -1
	 */
	// 
	public int indexOfNth(Symbol target, int n) {
		int count = 0;
		for(int i = 0; i < contents.length; i++) {
			if(contents[i].equals(target)) {
				if(count == n) {
					return i;
				}
				count++;
			}
		}
		return -1;
	}

	/**
	 * Replace the symbol at the given index with the given word.
	 * 
	 * Useful for applying a rule to a given word, perhaps!
	 *
	 * @param index the index of the symbol to replace
	 * @param word the word to insert at this index
	 * @return a new word with the symbol replaced by the word
	 */
	public Word replace(int index, Word word) {
		if(index < 0 || index >= this.length()) {
			throw new ArrayIndexOutOfBoundsException("Word index " + index + " out of range for word " + word.toString());
		}

		Symbol[] newWord = new Symbol[contents.length + word.length() - 1];
		for(int i = 0; i < contents.length; i++) {
			if(i < index) {
				newWord[i] = contents[i];
			} 
			else if(i == index) {
				if (word.length() >= 0) {
					System.arraycopy(word.contents, 0, newWord, i, word.length());
				}
			} 
			else {
				newWord[i+word.length()-1] = contents[i];
			}
		}
		return new Word(newWord);
	}

	/**
	 * Checks if the word is terminal.
	 *
	 * @return true, if is terminal
	 */
	public boolean isTerminal() {
		if(this.length() != 1) {
			return false;
		}
		else {
			return this.contents[0].isTerminal();
		}
	}

	/**
	 * Gets the symbol at the given position.
	 *
	 * @param i position to get
	 * @return the symbol
	 */
	public Symbol get(int i) {
		return contents[i];
	}

	/**
	 * Returns a subword from index start to end-1 inclusive. New
	 * length will be end-start.
	 * 
	 * @param start the index to start
	 * @param end the index to end before
	 * @return the word
	 * @see java.lang.String#substring(int, int)
	 */
	public Word subword(int start, int end) {
		Symbol[] newWord = new Symbol[end-start];
		if (end > start) {
			System.arraycopy(contents, start, newWord, 0, end - start);
		}
		return new Word(newWord);
	}

	/**
	 * Returns a new Word with terminal concatenated at the end.
	 * @param t the Terminal to concatenate
	 * @return the word with a concatenated terminal
	 */
	public Word concatenate(Terminal t){
		final int length = contents.length;
		Symbol[] newContents = java.util.Arrays.copyOf(contents, length+1);
		newContents[length] = t;
		return new Word(newContents);
	}

	/**
	 * This main method demonstrates the use of subword and replace.
	 *
	 * @param args the arguments
	 */
	public static void main(String... args) {
		Word w = new Word("000111");
		w = w.subword(1, 5);
		w = w.replace(2, new Word("abc"));
		System.out.println(w); 
	}

	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<Symbol> iterator() {
		return Arrays.stream(contents).iterator();
	}

	/**
	 * Returns a stream. Only used internally.
	 *
	 * @return the stream
	 */
	public Stream<Symbol> stream() {
		return Stream.of(contents);
	}

}
