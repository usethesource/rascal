@bootstrapParser
module lang::rascalcore::compile::Examples::RascalMetrics

import ParseTree;
import lang::rascal::\syntax::Rascal;
import util::Benchmark;
import IO;
import ValueIO;
import util::Reflective;
import Set;

int cntAlt(Prod p){
    switch(p){
      case (Prod) `<Prod lhs> | <Prod rhs>`: return cntAlt(lhs) + cntAlt(rhs);
      case (Prod) `<Prod lhs> \> <Prod rhs>`: return cntAlt(lhs) + cntAlt(rhs);
    }
   return 1;
}

value main()
      = measure();

value measure(){

     //moduleLoc = |std:///lang/rascal/syntax/Rascal.rsc|;
    moduleLoc = |std:///experiments/Compiler/RVM/AST.rsc|;
    //moduleLoc = |std:///experiments/Compiler/RVM/Syntax.rsc|;
    //moduleLoc = |std:///experiments/Compiler/muRascal/Syntax.rsc|;
     //moduleLoc = |std:///experiments/Compiler/muRascal/AST.rsc|;
    //moduleLoc = |std:///demo/lang/Pico/Syntax.rsc|;
    //moduleLoc = |std:///demo/lang/Exp/Concrete/NoLayout/Syntax.rsc|;
    m = parse(#start[Module], moduleLoc).top;
    stats = ();
    nonterm = {};
    datadefs = {};
    
    visit(m){
      case (SyntaxDefinition) `<Visibility _> layout <Sym name> = <Prod p> ;`: {
            stats["layout"] ? 0 += 1;
            nonterm += "<name>";
            stats["layoutAlts"] ? 0 += cntAlt(p);
            }
      case (SyntaxDefinition) `lexical <Sym name> = <Prod p> ;` : {
            stats["lexical"] ? 0 += 1;
             nonterm += "<name>";
            stats["lexicalAlts"] ? 0 += cntAlt(p);
            }
      case (SyntaxDefinition) `keyword <Sym name> = <Prod p> ;` : {
            stats["keyword"] ? 0 += 1;
            nonterm += "<name>";
            stats["keywordAlts"] ? 0 += cntAlt(p);
            }
      case (SyntaxDefinition) `<Start _> syntax <Sym name> = <Prod p> ;` : {
            nonterm += "<name>";
            stats["syntax"] ? 0 += 1;
            stats["syntaxtAlts"] ? 0 += cntAlt(p);
        }
      case Declaration d:
            if(d is \data || d is \dataAbstract) datadefs += "<d.user>"; 
      case Variant v: 
          stats["variant"] ? 0 += 1;
    }
    stats["nonterminal"] = size(nonterm);
    stats["data"] = size(datadefs);
    return stats;
}