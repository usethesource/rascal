@doc{
  This module provides functionality for merging the Rascal grammar and arbitrary user-defined grammars
}
module rascal::syntax::Assimilator

import ValueIO;
import List;
import rascal::syntax::Grammar;
import ParseTree;
import rascal::syntax::Normalization;
import rascal::syntax::Escape;

private Grammar rg = grammar({},{});
private Symbol  rl = layouts("LAYOUTLIST");

@doc{
  It is assumed both the Rascal grammar and the object grammar have been normalized
}
public Grammar assimilate(Grammar object) {
  set[Production] emptySet = {};
   
  if (rg == grammar({},{})) {
    rg = readBinaryValueFile(#Grammar, |stdlib:///org/rascalmpl/library/rascal/parser/Rascal.grammar|);
    
    // here we rename all sorts in the grammar to start with an underscore
    // and we wrap all literals to prevent interference with the object grammar's follow restrictions
    newLiterals = {};
    rg = visit(rg) {
      case sort(str n) => sort("_<n>")
      case lit(str l) : {
        newLiterals += prod([lit(l)],\parameterized-sort("_WrappedLiteral", [lit(l)]),\no-attrs());
        insert \parameterized-sort("_WrappedLiteral", [lit(l)]);
      }
      case cilit(str l) : {
        newLiterals += prod([lit(l)],\parameterized-sort("_WrappedLiteral", [cilit(l)]),\no-attrs());
        insert \parameterized-sort("_WrappedLiteral", [cilit(l)]);
      }
    }
    
    rg = compose(rg, grammar({}, newLiterals));
  }  
  
  bool isNonterminal(Symbol x) { 
    return lit(_) !:= x 
       && cilit(_) !:= x 
       && \char-class(_) !:= x 
       && \layouts(_) !:= x
       && \start(_) !:= x; 
  }
  
  set[Production] glue = 
      { prod([lit("`"),rl,nont,rl,lit("`")],sort("Expression"),attrs([term("cons"("ConcreteQuoted"))])),
        prod([lit("("),rl,symbolLiterals(nont),rl,lit(")"),rl,lit("`"),rl,nont,rl,lit("`")],sort("Expression"),attrs([term("cons"("ConcreteTypedQuoted"))])),
        prod([lit("`"),rl,nont,rl,lit("`")],sort("Pattern"),attrs([term("cons"("ConcreteQuoted"))])),
        prod([lit("("),rl,symbolLiterals(nont),rl,lit(")"),rl,lit("`"),rl,nont,rl,lit("`")],sort("Pattern"),attrs([term("cons"("ConcreteTypedQuoted"))])),
        prod([lit("\<"),rl,sort("Pattern"),rl,lit("\>")],nont,attrs([term("cons"("MetaVariable"))])) 
      | /Symbol nont <- object.rules, isNonterminal(nont) };
  
  return compose(rg, compose(object, grammar({}, glue)));
}

private list[Symbol] symbolLiterals(Symbol sym) {
  switch (sym) {
    case \sort(n) : return [lit(n)];
    case \opt(s) : return [symbolLiterals,rl,lit("?")];
    case \START() : return [lit("START")];
    case \start(s) : return [lit("start"),rl,lit("["),rl,symbolLiterals(s),rl,lit("]")];
    case \label(n,s) : return symbolLiterals(s); 
    case \lit(n) : return [lit(quote(n))];
    case \cilit(n) : return [lit(ciquote(n))]; 
    case \cf(s) : return symbolLiterals(s);
    case \lex(s) : return symbolLiterals(s);
    case \empty() : return "";
    case \layouts(n) : return [];
    case \iter(s) : return [symbolLiterals(s),rl,lit("+")];
    case \iter-star(s) : return [symbolLiterals(s),rl,lit("*")];
    case \iter-seps(s, seps) : return [lit("{"),rl,symbolLiterals(s),rl,tail([rl,symbolLiterals(t) | t <- seps]),rl,lit("}"),rl,lit("+")];
    case \iter-seps(s, seps) : return [lit("{"),rl,symbolLiterals(s),rl,tail([rl,symbolLiterals(t) | t <- seps]),rl,lit("}"),rl,lit("*")]; 
    case \parameterized-sort(n, params) : return [lit(n),rl,lit("["),rl,tail(tail(tail([rl,lit(","),rl,symbolLiterals(p) | p <- params]))),rl,lit("]")]; 
    case \parameter(n) : return [lit("&"),rl,lit(n)];
    case \char-class(list[CharRange] ranges) : return [lit("["),rl,tail([rl,rangeLiterals(r) | r <- ranges]),rl,lit("]")];
    case \at-column(c) : return [lit("@"),rl,lit("<c>")];
    case \start-of-line() : return [lit("^")];
    case \end-of-line() : return [lit("$")];
    default: throw "unsupported symbol <sym>";
  }
}

private list[Symbol] rangeLiterals(CharRange r) {
  switch (r) {
    case range(c,c) : return [lit(makeCharClassChar(c))];
    case range(c,d) : return [lit(makeCharClassChar(c)),lit("-"),lit(makeCharClassChar(d))];
  }
}

