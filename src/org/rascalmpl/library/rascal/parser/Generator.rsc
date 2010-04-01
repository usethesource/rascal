module rascal::parser::Generator

import rascal::parser::Grammar;
import String;
import List;

private int itemId = 0;

public str generateJava(str name, Grammar g) {
  itemId = 0;
  return 
"
package org.rascalmpl.rascal.generated;

import org.rascalmpl.gll.SGLL;
import org.rascalmpl.gll.stack.*;

public class <name> extends SGLL {
  public <name>(char[] input){
    super(input);
  }
	
  <generateSymbolMethods(g)>

  public static void main(String[] args){
    <name> parser = new <name>(args[1].toCharArray());
    System.out.println(parser.parse(args[0]));    
  }
}
";
}  

public str symbol2field(Symbol sym, int id) {
  return "ITEM_<sym2name(sym)>_<id>";
}

public str sym2name(Symbol sym) {
  switch (sym) {
    case sort(n) : return n;
    case lit(l) : return "LIT_<str2name(l)>";
    case cilit(l) : return "CILIT_<str2name(l)>";
    case iter(s) : return "ITER_<sym2name(s)>";
    case \iter-star(s) : return "ITERSTAR_<sym2name(s)>";
    case \iter-sep(Symbol s,list[Symbol] seps) : return "ITERSEP<sym2name(s)>_<(""|it+sym2name(sep)|sep<-seps)>";
    case \iter-sep-star(Symbol s,list[Symbol] seps) : return "ITERSEPSTAR_<sym2name(s)>_<(""|it+sym2name(sep)|sep<-seps)>";
    case \opt(s) : return "OPT_<sym2name(s)>";
    case \char-class(list[CharRange] ranges) : return "CLASS_<ranges2name(ranges)>";
    default: throw "not yet implemented <sym>";
  }
}

public str ranges2name(list[CharRange] ranges) {
  return ("" | it + "_" + range2name(range) | range <- ranges);
}

public str range2name(CharRange range) {
  return "<range.start>_TO_<range.end>";
}

public str str2name(str s) {
  return ("" | it + "_" + charAt(s,i) | i <- [0..size(s)-1]);
}

public str generateItemMethods(set[Production] productions) {
  return (""| it + generateItemMethods(p)| p:prod(_,_,_) <- g.productions);
}	

public str generateSymbolMethod(Production p) {
  if (prod(_,_,_) := p) {
    return 
"public void <sym2name(p.rhs)>() {
    expect(<generateSymbolItemExpects(p.lhs)>);  
  }
";
  }

  if (choice(ps) := p) {
    return
"public void <sym2name(p.rhs)>() {
    <for (Production q <- ps) {>
    expect(<generateSymbolItemExpects(q.lhs)>);
    <}>  
  }
";
  }

  throw "not implemented <p>";
}

public int nextItem() {
  int id = itemId;
  itemId += 1;
  return id;
}

public str generateSymbolItemExpects(list[Symbol] syms) {
   if (syms == []) {
     return "new Epsilon(<nextItem()>)";
   }
   
   return ("<sym2newitem(head(syms))>" | it + ", " + sym2newitem(sym) | sym <- tail(syms));
}

public str sym2newitem(Symbol sym) {
   int id = nextItem();

   switch (sym) {
    case \sort(n) : return "new NonTerminalParseStackNode(\"<sym>\", id)";
    case \lit(l) : return "new NonTerminalParseStackNode(\"<sym>\", id)";
    case \cilit(l) : return "new NonTerminalParseStackNode(\"<sym>\", id)";
    case \iter(\char-class(list[CharRange] ranges)) : return "new CharacterClassListParseStackNode(\"<sym>\", id, true)";
    case \iter-star(\char-class(list[CharRange] ranges)) : return "new CharacterClassListParseStackNode(\"<sym>\", id, false)";
    case \iter-sep(\char-class(list[CharRange] ranges),list[Symbol] seps) : return "new CharacterClassSeparatedListParseStackNode(\"<sym>\", id, true, <generateSymbolItemExpects(seps)>)";
    case \iter-sep-star(\char-class(list[CharRange] ranges),list[Symbol] seps) : return "new CharacterClassSeparatedListParseStackNode(\"<sym>\", id, false, <generateSymbolItemExpects(seps)>)";
    case \iter(s) : return "new NonTerminalListParseStackNode(\"<sym>\", id, true)";
    case \iter-star(s) : return "new NonTerminalListParseStackNode(\"<sym>\", id, false)";
    case \iter-sep(Symbol s,list[Symbol] seps) : return "new NonTerminalSeparatedListParseStackNode(\"<sym>\", id, true, <generateSymbolItemExpects(seps)>)";
    case \iter-sep-star(Symbol s,list[Symbol] seps) : return "new NonTerminalSeparatedListParseStackNode(\"<sym>\", id, false, <generateSymbolItemExpects(seps)>)";
    case \opt(s) : return "new NonTerminalOptionalParseStackNode(\"<sym>\", id)";
    case \char-class(list[CharRange] ranges) : "new CharacterClassParseStackNode(\"<sym>\", id)";
    default: throw "not yet implemented <sym>";
  }
}
