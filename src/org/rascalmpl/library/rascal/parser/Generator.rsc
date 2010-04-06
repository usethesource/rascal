module rascal::parser::Generator

import rascal::parser::Grammar;
import ParseTree;
import String;
import List;
import Node;
import IO;

private int itemId = 0;

private int nextItem() {
  int id = itemId;
  itemId += 1;
  return id;
}

public str generate(str package, str name, Grammar g) {
  itemId = 0;
  return 
"
package org.rascalmpl.rascal.generated<package != "" ? ".<package>" : "">;

import org.rascalmpl.gll.SGLL;
import org.rascalmpl.gll.stack.*;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.values.uptr.Factory;
import org.rascalmpl.values.ValueFactoryFactory;

public class <name> extends SGLL {
  private static IConstructor read(String s, Type type) {
    return new StandardTextReader().read(ValueFactoryFactory.getValueFactory(), org.rascalmpl.values.uptr.Factory.uptr, type, new ByteArrayInputStream(s.getBytes())); 
  }
 
  // Symbols declarations
  <for (s <- { s | /Symbol s := g}) {>private static final IConstructor symbol_<value2id(s)> = read(\"<esc("<s>")>\", Factory.Symbol);
  <}>

  // Production declarations
  <for (p <- { p | /Production p:prod(_,_,_) := g}) {>private static final IConstructor <value2id(p)> = read(\"<esc("<p>")>\", Factory.Production);
  <}>

  public <name>(char[] input){
    super(input);
  }

  // Parse methods	
  <for (Production p <- g.productions) {>
  <generateParseMethod(p)>
  <}>

  public static void main(String[] args){
    <name> parser = new <name>(args[1].toCharArray());
    System.out.println(parser.parse(args[0]));    
  }
}
";
}  

public str sym2name(Symbol s) {
  switch (s) {
    case sort(x) : return x;
    default      : return value2id(s);
  }
}

public str generateParseMethod(Production p) {
  if (prod(_,Symbol rhs,_) := p) {
    return "public void <sym2name(rhs)>() {
      // <p>
      expect(<value2id(p)>, <generateSymbolItemExpects(p.lhs)>);  
    }";
  }

  if (choice(Symbol rhs, set[Production] ps) := p) {
    return "public void <sym2name(rhs)>() {
      <for (Production q:prod(_,_,_) <- ps) {>
      // <q>
      expect(<value2id(q)>, <generateSymbolItemExpects(q.lhs)>);
      <}>  
    }";
  }

  if (regular(_,_) := p) {
    // do not occur as defined symbols
    return "";
  }

  throw "not implemented <p>";
}

public str generateSymbolItemExpects(list[Symbol] syms) {
   if (syms == []) {
     return "new EpsilonParseStackNode(<nextItem()>)";
   }
   
   return ("<sym2newitem(head(syms))>" | it + ",\n\t\t" + sym2newitem(sym) | sym <- tail(syms));
}

public str sym2newitem(Symbol sym) {
   int id = nextItem();

   switch (sym) {
    case \label(_,s) : return sym2newitem(s); // ignore labels
    case \sort(n) : 
      return "new NonTerminalParseStackNode( \"<value2id(sym)>\" , <id>)";
    case \lit(l) : 
      return "new NonTerminalParseStackNode( \"<value2id(sym)>\" , <id>)";
    case \cilit(l) : 
      return "new NonTerminalParseStackNode( \"<value2id(sym)>\" , <id>)";
    case \iter(\char-class(list[CharRange] ranges)) : 
      return "new CharacterClassListParseStackNode(symbol_<value2id(sym)>, new char[][] { <("" | it + "{<from>,<to>}" | range(from,to) <- ranges)> }, <id>, true)";
    case \iter-star(\char-class(list[CharRange] ranges)) : 
      return "new CharacterClassListParseStackNode(symbol_<value2id(sym)> , new char[][] { <("" | it + "{<from>,<to>}" | range(from,to) <- ranges)> }, <id>, false)";
    case \iter-sep(\char-class(list[CharRange] ranges),list[Symbol] seps) : 
      return "new CharacterClassSeparatedListParseStackNode(symbol_<value2id(sym)> , new char[][] { <("" | it + "{<from>,<to>}" | range(from,to) <- ranges)> }, <id>, true, <generateSymbolItemExpects(seps)>)";
    case \iter-star-sep(\char-class(list[CharRange] ranges),list[Symbol] seps) :
      return "new CharacterClassSeparatedListParseStackNode(symbol_<value2id(sym)> , new char[][] { <("" | it + "{<from>,<to>}" | range(from,to) <- ranges)> }, <id>, false, <generateSymbolItemExpects(seps)>)";
    case \iter(s) : 
      return "new NonTerminalListParseStackNode( \"<value2id(sym)>\" , <id>, true)";
    case \iter-star(s) :
      return "new NonTerminalListParseStackNode( \"<value2id(sym)>\" , <id>, false)";
    case \iter-sep(Symbol s,list[Symbol] seps) : 
      return "new NonTerminalSeparatedListParseStackNode( \"<value2id(sym)>\" , <id>, true, <generateSymbolItemExpects(seps)>)";
    case \iter-star-sep(Symbol s,list[Symbol] seps) : 
      return "new NonTerminalSeparatedListParseStackNode( \"<value2id(sym)>\" , <id>, false, <generateSymbolItemExpects(seps)>)";
    case \opt(s) : 
      return "new NonTerminalOptionalParseStackNode( \"<value2id(sym)>\" , <id>)";
    case \char-class(list[CharRange] ranges) : 
      return "new CharacterClassParseStackNode(symbol_<value2id(sym)>, new char[][] { <("" | it + "{<from>,<to>}" | range(from,to) <- ranges)> }, <id>)";
    case \layout() :
      return "new NonTerminalParseStackNode(\"layout\", <id>)";
    default: 
      throw "not yet implemented <sym>";
  }
}

public str esc(str s) {
  return innermost visit(s) {
    case /\\/ => "\\\\"
    case /\"/ => "\\\""
    case /-/  => "_"
  }
}

public str value2id(value v) {
  switch (v) {
    case label(_,v)    : return value2id(v);
    case sort(str s)   : return s;
    case lit(/<s:^[A-Za-z0-9]+$>/)    : return "lit_<s>";
    case cilit(str s)  : return "cilit_<s>";
    case int i         : return "<i>";
    case str s         : return ("" | it + "_<charAt(s,i)>" | i <- [0..size(s)-1]);
    case node n        : return "<esc(getName(n))>_<("" | it + "_" + value2id(c) | c <- getChildren(n))>";
    case list[value] l : return ("" | it + "_" + value2id(e) | e <- l);
    default            : throw "value not supported <v>";
  }
}
