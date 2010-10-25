module experiments::GrammarTools::ImportBNF

import experiments::GrammarTools::BNF;
import experiments::GrammarTools::Grammar;

import experiments::GrammarTools::BNFGrammars;  // for testing
import experiments::GrammarTools::Grammars;     // for testing

import Set;
import IO;
import String;
import Exception;

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

Symbol toSymbol(BNFElement E){
   visit(E){
     case Terminal T:
          if(/'<text:[^']*>'/ := "<T>")
            return t(text);
     case NonTerminal NT:
          return nt("<NT>");
   }
   throw IllegalArgument(E);
}

// Tests
    
test importBNF(G1BNF) == G1;
test importBNF(G2BNF) == G2;
