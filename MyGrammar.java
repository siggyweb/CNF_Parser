import computation.contextfreegrammar.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class MyGrammar {
	public static ContextFreeGrammar makeGrammar() {
		// You can write your code here to make the context-free grammar from the assignment
    Variable S = new Variable('S');
		Variable E = new Variable('E');
		Variable T = new Variable('T');
		Variable F = new Variable('F');

		Variable A = new Variable('A');
		Variable B = new Variable('B');

		Variable P = new Variable('P');
		Variable M = new Variable('M');
		Variable N = new Variable('N');
		Variable C = new Variable('C');

		HashSet<Variable> variables = new HashSet<>();
		variables.add(S);
		variables.add(E);
		variables.add(T);
		variables.add(F);

		variables.add(A);
		variables.add(B);

		variables.add(P);
		variables.add(M);
		variables.add(N);
		variables.add(C);


		Terminal plus = new Terminal('+');
		Terminal mult = new Terminal('*');
		Terminal neg = new Terminal('-');
		Terminal one = new Terminal('1');
		Terminal zero = new Terminal('0');
		Terminal x = new Terminal('x');

		HashSet<Terminal> terminals = new HashSet<>();
		terminals.add(plus);
		terminals.add(mult);
		terminals.add(neg);
		terminals.add(one);
		terminals.add(zero);
		terminals.add(x);


		ArrayList<Rule> rules = new ArrayList<>();
		rules.add(new Rule(S, new Word(E, A)));
		rules.add(new Rule(S, new Word(T, B)));
		rules.add(new Rule(S, new Word(N, C)));
		rules.add(new Rule(S, new Word(one)));
		rules.add(new Rule(S, new Word(zero)));
		rules.add(new Rule(S, new Word(x)));

		rules.add(new Rule(E, new Word(E, A)));
		rules.add(new Rule(E, new Word(T, B)));
		rules.add(new Rule(E, new Word(N, C)));
		rules.add(new Rule(E, new Word(one)));
		rules.add(new Rule(E, new Word(zero)));
		rules.add(new Rule(E, new Word(x)));

		rules.add(new Rule(T, new Word(T, B)));
		rules.add(new Rule(T, new Word(N, C)));
		rules.add(new Rule(T, new Word(one)));
		rules.add(new Rule(T, new Word(zero)));
		rules.add(new Rule(T, new Word(x)));

		rules.add(new Rule(F, new Word(N, C)));
		rules.add(new Rule(F, new Word(one)));
		rules.add(new Rule(F, new Word(zero)));
		rules.add(new Rule(F, new Word(x)));

		rules.add(new Rule(A, new Word(P, T)));
		rules.add(new Rule(B, new Word(M, F)));

		rules.add(new Rule(N, new Word(neg)));
		rules.add(new Rule(P, new Word(plus)));
		rules.add(new Rule(M, new Word(mult)));
		rules.add(new Rule(C, new Word(one)));
		rules.add(new Rule(C, new Word(zero)));
		rules.add(new Rule(C, new Word(x)));


		ContextFreeGrammar cfg = new ContextFreeGrammar(variables, terminals, rules, S);
		return cfg;
	}
}
