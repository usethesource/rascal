module experiments::GrammarTools::Basic

import experiments::GrammarTools::BNF;
import basic::Whitespace;
import Set;
import IO;
import UnitTest;

// Data structure for representing the grammar

public data Symbol      = t(str text) | nt(str name) | epsilon();
public alias Rule       = tuple[str name, list[Symbol] symbols];
public data Grammar     = grammar(str start, set[Rule] rules);

// Given a grammar in BNF notation, convert it to internal representation

/*
Grammar importGrammar(BNF bnfGrammar){
    if([|grammar <NonTerminal Start> rules <Rule+ Rules>|] := bnfGrammar){
       rules = {};
       for([|<NonTerminal L> ::= <Symbol* Symbols> ; |] <- Rules){
            rules += <L, [convert(S) | Symbol S <- Symbols]>;
       }
       return grammar(Start, rules); 
    }
}

// Convert a BNF Element to a Symbol value

Symbol convert(Element E){
   switch(E){
     case Terminal _: return t("<E>");
     case NonTerminal _: return n("<E>");
   }
}
*/

/*
BNF G1 = [|grammar E 
            rules
            	E ::= E "*" B;
           		E ::= E "+" B;
            	E ::= B;
            	B ::= "0";
            	B ::= "1";|];
*/
       	
public Grammar G1converted = grammar("E",
{
<"E", [nt("E"), t("*"), nt("B")]>,
<"E", [nt("E"), t("+"), nt("B")]>,
<"E", [nt("B")]>,
<"B", [t("0")]>,
<"B", [t("1")]>
});

/*
public bool test(){
    println(importGrammar(G1));
	return report("GrammarTools::Basic");
}
*/


