module experiments::GrammarTools::ImportBNF

import experiments::GrammarTools::BNF;
import experiments::GrammarTools::Grammar;

import experiments::GrammarTools::BNFGrammars;  // for testing
import experiments::GrammarTools::Grammars;     // for testing

import Set;
import IO;
import UnitTest;
import String;

// Import a grammar in BNF notation

public Grammar importBNF(BNF bnfGrammar){
    if(`grammar <NonTerminal Start> rules <BNFRule+ Rules>` := bnfGrammar){
       rules = {};
       for(/`<NonTerminal L> ::= <BNFElement* Elements> ; ` <- Rules){
            rules += <toSymbol(L), [toSymbol(E) | BNFElement E <- Elements]>;
       }
       return grammar(toSymbol(Start), rules); 
    }
}

// Convert a BNF Element to a Symbol value

Symbol toSymbol(Terminal E){
  if(/'<text:[^']*>'/ := "<E>")
      return t(text);
  else
      throw IllegalArgument(E);
}

Symbol toSymbol(NonTerminal E){
  return nt("<E>");
}

Symbol toSymbol(BNFElement E){
   visit(E){
     case Terminal T: return toSymbol(T);
     case NonTerminal NT: return toSymbol(NT);
   }
   throw IllegalArgument(E);
}

public bool test(){
    
    assertEqual(importBNF(G1BNF), G1);
    assertEqual(importBNF(G2BNF), G2);
	return report("GrammarTools::ImportBNF");
}
