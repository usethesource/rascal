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

// Utility predicates on Symbols

public bool isTermSymbol(Symbol s){
   return t(_) := s;
}

public bool isNonTermSymbol(Symbol s){
   return nt(_) := s;
}

// Utilities on grammars

public set[Symbol] symbols(Grammar G){
   return { sym | Rule r <- G.rules, Symbol sym <- r.symbols};
}

public set[Symbol] terminals(Grammar G){
   return { sym | Rule r <- G.rules, Symbol sym <- r.symbols, isTermSymbol(sym)};
}

public set[Symbol] nonTerminals(Grammar G){
   return { sym | Rule r <- G.rules, Symbol sym <- r.symbols, isNonTermSymbol(sym)};
}

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

// Export a grammar to BNF
public BNF exportBNF(Grammar G){

}

BNFElement toElement(Symbol s){
   if(t(str name) := s)
      return Terminal `'<name>'`;
   if (nt(str name) := s)
      return NonTerminal `<name>`;
    throw IllegalArgument(s);  
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