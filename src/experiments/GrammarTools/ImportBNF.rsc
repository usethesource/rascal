module experiments::GrammarTools::ImportBNF

import experiments::GrammarTools::BNF;
import experiments::GrammarTools::Grammar;
import basic::Whitespace;
import Set;
import IO;
import UnitTest;
import String;

// Import a grammar in BNF notation

public Grammar importBNF(BNF bnfGrammar){
    if(`grammar <NonTerminal Start> rules <BNFRule+ Rules>` := bnfGrammar){
       rules = {};
       for(`<NonTerminal L> ::= <BNFElement* Elements> ; ` <- Rules){
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

BNF G1 = `grammar E 
            rules
            	E ::= E '*' B;
           		E ::= E '+' B;
            	E ::= B;
            	B ::= '0';
            	B ::= '1';`;
            	
/*	  This list is too ambiguous ...          	
public Grammar G1converted = grammar("E",
{
<nt("E"), [nt("E"), t("*"), nt("B")]>,
<nt("E"), [nt("E"), t("+"), nt("B")]>,
<nt("E"), [nt("B")]>,
<nt("B"), [t("0")]>,
<nt("B"), [t("1")]>
});
*/

public bool test(){
    println(importBNF(G1));
    
	return report("GrammarTools::ImportBNF");
}
