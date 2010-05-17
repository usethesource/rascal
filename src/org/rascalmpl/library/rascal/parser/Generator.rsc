module rascal::parser::Generator

import rascal::parser::Grammar;
import ParseTree;
import String;
import List;
import Node;
import Set;
import IO;

private int itemId = 0;
private Grammar grammar = grammar({},{});

private int nextItem() {
  int id = itemId;
  itemId += 1;
  return id;
}

public str generate(str package, str name, Grammar g) {
  itemId = 0;
  grammar = g;
  return 
"
package <package>;

import org.rascalmpl.parser.sgll.SGLL;
import org.rascalmpl.parser.sgll.stack.*;
import org.rascalmpl.parser.sgll.result.INode; // TODO replace by PDB value
import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.io.StandardTextReader;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.rascalmpl.values.uptr.Factory;
import org.rascalmpl.values.uptr.SymbolAdapter;
import org.rascalmpl.values.ValueFactoryFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@SuppressWarnings(\"unused\")
public class <name> extends SGLL {
  private static IConstructor read(java.lang.String s, Type type) {
    try {
      return (IConstructor) new StandardTextReader().read(ValueFactoryFactory.getValueFactory(), org.rascalmpl.values.uptr.Factory.uptr, type, new ByteArrayInputStream(s.getBytes()));
    } catch (FactTypeUseException e) {
      throw new RuntimeException(\"unexpected exception in generated parser\", e);  
	} catch (IOException e) {
      throw new RuntimeException(\"unexpected exception in generated parser\", e);  
	}
  }
 
  // Symbols declarations
  <for (s <- { s | /Symbol s := g}) {>private static final IConstructor <sym2id(s)> = read(\"<esc("<s>")>\", Factory.Symbol);
  <}>

  // Production declarations
  <for (p <- { p | /Production p:prod(_,_,_) := g}) {>private static final IConstructor <value2id(p)> = read(\"<esc("<p>")>\", Factory.Production);
  <}>

  public <name>(){
    super();
  }

  // Parse methods	
  <for (Production p <- g.productions) {>
  <generateParseMethod(p)>
  <}>

  public INode parse(IConstructor start, java.lang.String input) {
    return parse(start, input.toCharArray());
  }

  public INode parse(IConstructor start, char[] sentence) {
      if (SymbolAdapter.isSort(start)) {
		  return parse(new NonTerminalStackNode(-1, SymbolAdapter.getName(start)), sentence);
	  }
	  else if (SymbolAdapter.isStartSort(start)) {
		 return parse(SymbolAdapter.getStart(start), sentence);  
	  }
	  throw new IllegalArgumentException(start.toString());
  }

  public static void main(java.lang.String[] args){
    <name> parser = new <name>();

    // TODO: this just takes an arbitrary start symbol, if there are more the result can be surprising ;-)
    System.out.println(parser.parse(read(\"<esc(getOneFrom(g.start))>\",Factory.Symbol), args[0].toCharArray())); 
  }
}
";
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

public str literals2ints(list[Symbol] chars) {
  if (chars == []) return "";
  
  str result = "<head(head(chars).ranges).start>";
  
  for (ch <- tail(chars)) {
    result += ",<head(ch.ranges).start>";
  }
  
  return result;
}

public str ciliterals2ints(list[Symbol] chars) {
  println("chars: ",chars);
  return "hoi";
}

public str sym2newitem(Symbol sym) {
   int id = nextItem();

   switch (sym) {
    case \label(_,s) : return sym2newitem(s); // ignore labels
    case \sort(n) : 
      return "new NonTerminalStackNode(<id>, \"<sym2name(sym)>\")";
    case \start(s) : 
      return "new NonTerminalStackNode(<id>, \"<sym2name(sym)>\")";
    case \lit(l) : {
      if (/p:prod(chars,\lit(l),_) := grammar)  
        return "new LiteralStackNode(<id>, <value2id(p)>, new char[] {<literals2ints(chars)>})";
      throw "literal not found in <grammar>??";
    }
    case \cilit(l) : {
       throw "ci lits not supported yet";
    }
    case \iter(s) : 
      return "new ListStackNode(<id>, <sym2id(sym)>, <sym2newitem(s)>, true)";
    case \iter-star(s) :
      return "new ListStackNode(<id>, <sym2id(sym)>, <sym2newitem(s)>, false)";
    case \iter-seps(Symbol s,list[Symbol] seps) : 
      return "new SeparatedListStackNode(<id>, <sym2id(sym)>, <sym2newitem(s)>, new StackNode[]{<generateSymbolItemExpects(seps)>}, true)";
    case \iter-star-seps(Symbol s,list[Symbol] seps) : 
      return "new SeparatedListStackNode(<id>, <sym2id(sym)>, <sym2newitem(s)>, new StackNode[]{<generateSymbolItemExpects(seps)>}, false)";
    case \opt(s) : 
      return "new OptionalStackNode(<id>, <sym2id(sym)>, <sym2newitem(s)>)";
    case \char-class(list[CharRange] ranges) : 
      return "new CharStackNode(<id>, new char[][]{<generateCharClassArrays(ranges)>})";
    case \layout() :
      return "new NonTerminalStackNode(<id>, \"<sym2name(sym)>\")";   
    default: 
      throw "not yet implemented <sym>";
  }
}

public str generateCharClassArrays(list[CharRange] ranges) {
  if (ranges == []) return "";
  result = "";
  if (range(from, to) := head(ranges)) 
    result += "{<from>,<to>}";
  for(range(from, to) <- tail(ranges))
    result += ",{<from>,<to>}";
  return result;
}

public str esc(Symbol s) {
  return esc("<s>");
}

private map[str,str] javaStringEscapes = ( "\n":"\\n", "\"":"\\\"", "\t":"\\t", "\r":"\\r","\\u":"\\\\u","\\":"\\\\");

public str esc(str s) {
  return escape(s, javaStringEscapes);
}

private map[str,str] javaIdEscapes = javaStringEscapes + ("-":"_");

public str escId(str s) {
  return escape(s, javaIdEscapes);
}

public str sym2name(Symbol s) {
  switch (s) {
    case sort(x) : return x;
    default      : return value2id(s);
  }
}

public str sym2id(Symbol s) {
  return "symbol_<value2id(s)>";
}

public str value2id(value v) {
  switch (v) {
    case label(_,v)    : return value2id(v);
    case sort(str s)   : return s;
    case lit(/<s:^[A-Za-z0-9]+$>/)    : return "lit_<s>";
    case cilit(str s)  : return "cilit_<s>";
    case int i         : return "<i>";
    case str s         : return ("" | it + "_<charAt(s,i)>" | i <- [0..size(s)-1]);
    case node n        : return "<escId(getName(n))>_<("" | it + "_" + value2id(c) | c <- getChildren(n))>";
    case list[value] l : return ("" | it + "_" + value2id(e) | e <- l);
    default            : throw "value not supported <v>";
  }
}
