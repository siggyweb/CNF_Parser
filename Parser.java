import computation.contextfreegrammar.*;
import computation.parser.*;
import computation.parsetree.*;
import computation.derivation.*;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

public class Parser implements IParser {

  //Holding variable for the successful derivation to be used in parse tree.
  public Derivation toTree = null;

  /*Given a context-free grammar and a word, returns whether the word can be derived from the language in 2n-1 steps, as a boolean value*/
  public boolean isInLanguage(ContextFreeGrammar cfg, Word w){

    //Retrieve the start variable
    Word startVar = new Word(cfg.getStartVariable());
    Derivation start = new Derivation(startVar);

    //Retrieve rules from CFG
    List<Rule> rules = cfg.getRules();

    //Test if the string is the empty word and accept/reject accordingly.
    if (w.equals(Word.emptyWord)) {
      for (Rule r : rules) {
        //Only accept string if start variable can expand to \Epsilon.
        if (r.getExpansion().equals(Word.emptyWord) && r.getVariable().equals(cfg.getStartVariable())) {
          return true;
        }
      }
      return false;
    }

    //Metrics to define the limit to number of derivations performed.
    int n = w.length();
    int steps = (2 * n) - 1;

    //Hash map which will store an arrayList at each key, needed to store all possible         
    //derivations at each step. Each step of the derivation will be a separate list in the hashmap. Has a hardcoded limit of 41 lists.
    HashMap<Integer,ArrayList<Derivation>> derivations = new                     
    HashMap<Integer,ArrayList<Derivation>>(40);

    //Create the first ArrayList for start variable and add to the hashmap.
    ArrayList<Derivation> initial = new ArrayList<>();
    initial.add(start);
    Integer stepZero = Integer.valueOf(0);
    derivations.put(stepZero, initial);

    //Iterate through the 2n-1 steps of the word derivation.
    for (int stages = 1; stages <= steps; stages++ ){
      //At each stage, retrieve the list of all previous derivations
      ArrayList<Derivation> previousStep = derivations.get(Integer.valueOf(stages-1));
      //Create a new list for all derivations at that step
      ArrayList<Derivation> nextStep = new ArrayList<>();
      
    //Triple loop that takes every previous derivation and tries every rule at every index to create every possible word. These are added to the next list for further permutations.
      for (Derivation d : previousStep) { 
        for (int index = 0; index < d.getLatestWord().length(); index++) {
          for (Rule r : rules) {

            //ArrayList to store all possible words than can be created
            ArrayList<Word> nextWords = new ArrayList<>();

            //Word to be expanded upon
            Word baseWord = d.getLatestWord();
            if (baseWord.get(index) == r.getVariable()){
              Word newWord = baseWord.replace(index, r.getExpansion());
              nextWords.add(newWord);
            }

            //Turn all possible words into derivations and add to derivations.
            for (Word wrd : nextWords) {
              Derivation tempDeriv = new Derivation(d);
              tempDeriv.addStep(wrd, r, index);
              nextStep.add(tempDeriv);
            }
          }//end for rule loop
        }//end for index loop
      }//End for derivations loop
      
      //Add the new List to the Hashmap
      derivations.put(Integer.valueOf(stages),nextStep);
      
    }//end for (stages)

    //when number of steps is 2n-1, iterate through the final list for a match.
    ArrayList<Derivation> output = derivations.get(Integer.valueOf(steps));
    for (Derivation d : output) {
      //Test for presence of the input word w, store the relevant derivation in a field for generating the parsetree.
      if (d.getLatestWord().toString().equals(w.toString())) {
        toTree = d;
        //System.out.println(toTree.getLatestWord().toString());
        return true;
      }
    }

    //Explicit return false for no matches
    return false;
  }

  
  public ParseTreeNode generateParseTree(ContextFreeGrammar cfg, Word w){
    //If the string is not in the language there can be no valid parse tree.
    if (!this.isInLanguage(cfg, w)) {
      return null;
    }
    
    //Retrieve the variables, terminals and number of steps for the parseTreeNodes
    int jumps = (2 * w.length()) - 1;

    //Obtain the derivation from the most recent string
    Derivation toParseTree = toTree;

    //Create lists to hold the steps, Terminal nodes and Variable nodes
    ArrayList<ParseTreeNode> nodes = new ArrayList<>();
    ArrayList<ParseTreeNode> varNodes = new ArrayList<>();
    ArrayList<Step> steps = new ArrayList<>();

    //Create nodes for terminals and add to separate list for managing the index of substitution
    Word finalWord = toParseTree.getLatestWord();
    for (int index = 0; index < finalWord.length(); index++) {
      ParseTreeNode ptn = new ParseTreeNode(finalWord.get(index));
      nodes.add(ptn);
    }

    //Pull the steps of the derivation out of iterator into list
    Iterator<Step> itr = toParseTree.iterator();
    while (itr.hasNext()) {
      Step stp = itr.next();
      steps.add(stp);
    }

    //Iterate through the steps of the derivation to make a list of nodes that map to terminal
    for (int index = jumps-1; index >= 0; index--) {
      Step currentStep = steps.get(index);
      Symbol nodeSymbol = currentStep.getRule().getVariable();
      Word nodeExpansion = currentStep.getRule().getExpansion();
      //System.out.println(nodeSymbol);

      //find out which previous nodes are children for the terminals, add to the list of nodes for mapping variables.
      if(nodeExpansion.isTerminal()) {
        ParseTreeNode ptn = new ParseTreeNode(nodeSymbol, nodes.get(currentStep.getIndex()));
        varNodes.add(ptn);
      }
    }

    //Loop to do the opposite of the above loop, only deal with expansions into variables.
    for (int index = 0; index < jumps; index++) {
      Step currentStep = steps.get(index);
      Symbol nodeSymbol = currentStep.getRule().getVariable();
      Word nodeExpansion = currentStep.getRule().getExpansion();
      //Where the substitution was made
      int subAt = currentStep.getIndex();

      //Dealing with only variable to variable expansions
      if(!nodeExpansion.isTerminal()) {
        //Retrieve the 2 child nodes
        ParseTreeNode child1 = varNodes.get(currentStep.getIndex());
        ParseTreeNode child2 = varNodes.get(currentStep.getIndex()+1);
        //Create a new parent node
        ParseTreeNode ptn = new ParseTreeNode(nodeSymbol, child1, child2);
        //Replace the node and next node at the index of substitution
        varNodes.set(subAt, ptn);
      }
    }
    
    //Take the final node from the list nodes, and print.
    return varNodes.get(0);
  }
  
}