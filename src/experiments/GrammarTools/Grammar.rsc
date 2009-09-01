module experiments::GrammarTools::Grammar

import experiments::GrammarTools::BNF;
import basic::Whitespace;
import Set;
import IO;
import UnitTest;
import String;

// Data structure for representing the grammar

public data Symbol  = t(str text) | nt(str name) | epsilon();
public alias Rule   = tuple[Symbol name, list[Symbol] symbols];
public data Grammar = grammar(Symbol start, set[Rule] rules);

// Given a grammar in BNF notation, convert it to internal representation

public Grammar importGrammar(BNF bnfGrammar){
    if(`grammar <NonTerminal Start> rules <Rule+ Rules>` := bnfGrammar){
       rules = {};
       for(`<NonTerminal L> ::= <Element* Elements> ; ` <- Rules){
            rules += <toSymbol(L), [toSymbol(S) | Element S <- Elements]>;
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

Symbol toSymbol(Element E){
   visit(E){
     case Terminal T: return toSymbol(T);
     case NonTerminal NT: return toSymbol(NT);
   }
   throw IllegalArgument(E);
}

// Extract all symbols from a grammar

public set[Symbol] symbols(Grammar G){
   return { sym | Rule r <- G.rules, Symbol sym <- r.symbols};
}

BNF G1 = `grammar E 
            rules
            	E ::= E '*' B;
           		E ::= E '+' B;
            	E ::= B;
            	B ::= '0';
            	B ::= '1';`;
            	
/*	  THis list is too ambiguous ...          	
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
    println(importGrammar(G1));
	return report("GrammarTools::Basic");
}