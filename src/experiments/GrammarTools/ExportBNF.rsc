module experiments::GrammarTools::ExportBNF

import experiments::GrammarTools::BNF;
import experiments::GrammarTools::Grammar;

import experiments::GrammarTools::Grammars;     // for testing
import experiments::GrammarTools::BNFGrammars;  // for testing

import Set;
import IO;
import UnitTest;
import String;

// Export a grammar to BNF
public BNF exportBNF(Grammar G){
   BNFRule* bnfRules = BNFRule* ``;
  
   for(<Symbol A, list[Symbol] symbols> <- G.rules){
       NT = toElement(A);
       Elems = toElements(symbols);
       bnfRules = `<bnfRules> <NT> ::= <Elems>;`;
   }
   bnfStart = toElement(G.start);
   return `grammar <bnfStart> rules <bnfRules>`;
}

BNFElement toElement(Symbol s){
   if(t(str name) := s)
      return Terminal `'<name>'`;
   if (nt(str name) := s)
      return NonTerminal `<name>`;
    throw IllegalArgument(s);  
}

BNFElement* toElements(list[Symbol] symbols){
   BNFElement* result = ``;
   for(Symbol sym <- symbols){
       elm = toElement(sym);
       result = `<result> <sym>`;
   }
   return result;
}

public bool test(){
    println(exportBNF(G1));
    
	return report("GrammarTools::ExportBNF");
}

