module rascal::parser::Generator

import rascal::parser::Grammar;
import rascal::parser::Priority;
import rascal::parser::Parameters;
import rascal::parser::Regular;
import rascal::parser::Normalization;
import ParseTree;
import String;
import List;
import Node;
import Set;
import IO;
import Exception;

private int itemId = 0;
private Grammar grammar = grammar({},{});

private int nextItem(){
    int id = itemId;
    itemId += 1;
    return id;
}

public str generate(str package, str name, Grammar gr){
    itemId = 0;
    println("expanding parameterized symbols");
    grammar = expandParameterizedSymbols(gr);
    println("generating stubs for regular");
    grammar = makeRegularStubs(grammar);
    println("factorizing priorities and associativity");
    grammar = factorize(grammar);
    println("printing the source code");
    g = grammar;
    
    uniqueProductions = {removePrimes(p) | /Production p := g, prod(_,_,_) := p || regular(_,_) := p};
    
    return 
"
package <package>;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.io.StandardTextReader;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.parser.sgll.SGLL;
import org.rascalmpl.parser.sgll.stack.*;
import org.rascalmpl.values.uptr.Factory;

public class <name> extends SGLL {
    private static IConstructor read(java.lang.String s, Type type) {
        try {
          return (IConstructor) new StandardTextReader().read(vf, org.rascalmpl.values.uptr.Factory.uptr, type, new ByteArrayInputStream(s.getBytes()));
        }
        catch(FactTypeUseException e){
          throw new RuntimeException(\"unexpected exception in generated parser\", e);  
        }
        catch(IOException e){
          throw new RuntimeException(\"unexpected exception in generated parser\", e);  
      }
    }
  
    // Production declarations
    <for (p <- uniqueProductions) {>private static final IConstructor <value2id(p)> = read(\"<esc("<p>")>\", Factory.Production);
    <}>
    
    public <name>(){
        super();
    }
    
    // Parse methods    
    <for (Symbol nont <- g.rules, isNonterminal(nont)) { >
        <generateParseMethod(choice(nont, g.rules[nont]))>
    <}>
}
";
}  

@doc{this function selects all symbols for which a parse method should be generated}
private bool isNonterminal(Symbol s) {
  switch (s) {
    case \sort(_) : return true;
    case \prime(sort(_),_,_) : return true;
    case \parameterized-sort(_,_) : return true;
    case \prime(\parameterized-sort(_,_)) : return true;
    case \start(_) : return true;
    case \layouts(_) : return true;
    default: return false;
  }
}

public str generateParseMethod(Production p){
    // note that this code heavily leans on the fact that production combinators are normalized 
    // (distribution and factoring laws have been applied to put a production expression in canonical form)
    
    if (prod(_,lit(_),_) := p) {
      return ""; // ignore literal productions
    }
    else if(prod(_,Symbol rhs,_) := p){
        return "public void <sym2name(rhs)>(){
            // <p>
            expect(<value2id(removePrimes(p))>,
            <generateSymbolItemExpects(p.lhs)>);  
        }";
    }
    else if(choice(Symbol rhs, set[Production] ps) := p){
        return "public void <sym2name(rhs)>(){
            <for (Production q:prod(_,_,_) <- ps){>
                // <q>
                expect(<value2id(removePrimes(q))>,
                <generateSymbolItemExpects(q.lhs)>);
            <}>  
        }";
    }
    else if (restrict(Symbol rhs, choice(rhs, set[Production] ps), set[Production] restrictions) := p) {
       str lookaheads = generateLookaheads(restrictions);
       return "public void <sym2name(rhs)>() {
             <for (Production q:prod(_,_,_) <- ps) {>
                // <q>
                expect(<value2id(removePrimes(q))>, <lookaheads>,
                <generateSymbolItemExpects(q.lhs)>);
            <}>         
       }";
    }
    else if (diff(Symbol rhs, choice(rhs, set[Production] choices), set[Production] rejects) := p) {
       return "public void <sym2name(rhs)>() {
            <for (Production q:prod(_,_,_) <- rejects){>
                // <q>
                expectReject(<value2id(removePrimes(q))>,
                <generateSymbolItemExpects(q.lhs)>);
            <}>
            <for (Production q:prod(_,_,_) <- choices){>
                // <q>
                expect(<value2id(removePrimes(q))>,
                <generateSymbolItemExpects(q.lhs)>);
            <}>
       }";
    }
    else if (diff(Symbol rhs, restrict(Symbol rhs, choice(rhs, set[Production] choices), set[Production] restrictions), set[Production] rejects) := p) {
       str lookaheads = generateLookaheads(restrictions);
       return "public void <sym2name(rhs)>() {
            <for (Production q:prod(_,_,_) <- rejects){>
                // <q>
                expectReject(<value2id(removePrimes(q))>, <lookaheads>, 
                <generateSymbolItemExpects(q.lhs)>);
            <}>
            <for (Production q:prod(_,_,_) <- choices){>
                // <q>
                expect(<value2id(removePrimes(q))>, <lookaheads>, 
                <generateSymbolItemExpects(q.lhs)>);
            <}>
       }";
    }
    else if (regular(_,_) := p) {
        // do not occur as defined symbols
        return "";
    }
    
    throw "not implemented <p>";
}

@doc{
  generate stack nodes for the restrictions. Note that although the abstract grammar for restrictions
  may seem pretty general (i.e. any symbol can be a restriction), we actually only allow finite languages
  defined as either sequences of character classes or literals.
}
public str generateLookaheads(set[Production] restrictions) {
  result = "new IReducableStackNode[] {"; 
  
  // not that only single symbol restrictions are allowed at the moment.
  // the run-time only supports character-classes and literals BTW, which should
  // be validated by a static checker for Rascal.  
  lookaheads = [ l | /prod([Symbol l],_,_) <- restrictions];
 
  if (lookaheads != []) {
    result += (sym2newitem(head(lookaheads)) | it + ", <sym2newitem(x)>" | x <- tail(lookaheads));
  }
  
  result += "}";
  return result;
}

public str generateSymbolItemExpects(list[Symbol] syms){
    if(syms == []){
        return "new EpsilonStackNode(<nextItem()>)";
    }
    
    return ("<sym2newitem(head(syms))>" | it + ",\n\t\t" + sym2newitem(sym) | sym <- tail(syms));
}

public str literals2ints(list[Symbol] chars){
    if(chars == []) return "";
    
    str result = "<head(head(chars).ranges).start>";
    
    for(ch <- tail(chars)){
        result += ",<head(ch.ranges).start>";
    }
    
    return result;
}

// TODO
public str ciliterals2ints(list[Symbol] chars){
    throw "case insensitive literals not yet implemented by parser generator";
}

public str sym2newitem(Symbol sym){
    int id = nextItem();
    switch(sym){
        case \label(_,s) : return sym2newitem(s); // ignore labels
        case \prime(_,_,_) : 
            return "new NonTerminalStackNode(<id>, \"<sym2name(sym)>\")";
        case \sort(n) : 
            return "new NonTerminalStackNode(<id>, \"<sym2name(sym)>\")";
        case \layouts(_) :
            return "new NonTerminalStackNode(<id>, \"<sym2name(sym)>\")";  
        case \parameterized-sort(n,args): 
            return "new NonTerminalStackNode(<id>, \"<sym2name(sym)>\")";
        case \parameter(n) :
            throw "all parameters should have been instantiated by now";
        case \start(s) : 
            return "new NonTerminalStackNode(<id>, \"<sym2name(sym)>\")";
        case \lit(l) : {
            if (/restrict(\lit(l), choice(\lit(l), {p:prod(chars,\lit(l),_)}), restrictions) := grammar.rules[sym]) { 
                return "new LiteralStackNode(<id>, <value2id(p)>, <generateLookaheads(restrictions)>, new char[] {<literals2ints(chars)>})";
            }
            else if (/p:prod(chars,\lit(l),_) := grammar.rules[sym]) {  
                return "new LiteralStackNode(<id>, <value2id(p)>, new char[] {<literals2ints(chars)>})";
            }
            throw "literal not found in <g>??";
        }
        case \cilit(l) : {
            throw "ci lits not supported yet";
        }
        case \iter(s) : 
            return "new ListStackNode(<id>, <value2id(regular(sym,\no-attrs()))>, <sym2newitem(s)>, true)";
        case \iter-star(s) :
            return "new ListStackNode(<id>, <value2id(regular(sym,\no-attrs()))>, <sym2newitem(s)>, false)";
        case \iter-seps(Symbol s,list[Symbol] seps) : 
            return "new SeparatedListStackNode(<id>, <value2id(regular(sym,\no-attrs()))>, <sym2newitem(s)>, new AbstractStackNode[]{<generateSymbolItemExpects(seps)>}, true)";
        case \iter-star-seps(Symbol s,list[Symbol] seps) : 
            return "new SeparatedListStackNode(<id>, <value2id(regular(sym,\no-attrs()))>, <sym2newitem(s)>, new AbstractStackNode[]{<generateSymbolItemExpects(seps)>}, false)";
        case \opt(s) : 
            return "new OptionalStackNode(<id>, <value2id(regular(sym,\no-attrs()))>, <sym2newitem(s)>)";
        case \char-class(list[CharRange] ranges) : 
            return "new CharStackNode(<id>, new char[][]{<generateCharClassArrays(ranges)>})";
       
        default: 
            throw "not yet implemented <sym>";
    }
}

public str generateCharClassArrays(list[CharRange] ranges){
    if(ranges == []) return "";
    result = "";
    if(range(from, to) := head(ranges)) 
        result += "{<from>,<to>}";
    for(range(from, to) <- tail(ranges))
        result += ",{<from>,<to>}";
    return result;
}

public str esc(Symbol s){
    return esc("<s>");
}

private map[str,str] javaStringEscapes = ( "\n":"\\n", "\"":"\\\"", "\t":"\\t", "\r":"\\r","\\u":"\\\\u","\\":"\\\\");

public str esc(str s){
    return escape(s, javaStringEscapes);
}

private map[str,str] javaIdEscapes = javaStringEscapes + ("-":"_");

public str escId(str s){
    return escape(s, javaIdEscapes);
}

public str sym2name(Symbol s){
    switch(s){
        case sort(x) : return x;
        default      : return value2id(s);
    }
}

public str sym2id(Symbol s){
    return "symbol_<value2id(s)>";
}

public Production removePrimes(Production p) {
  return visit(p) {
    case prime(Symbol s,_,_) => s
  }
}

map[value,str] idCache = ();

public str value2id(value v) {
  return idCache[v]? str () { idCache[v] = v2i(v); return idCache[v]; }();
}

str v2i(value v) {
    switch (v) {
        case label(str x,Symbol u) : return escId(x) + "_" + value2id(u);
        case prime(Symbol s, str reason, list[int] indexes) : return "<value2id(s)>_<reason>_<value2id(indexes)>";
        case sort(str s)   : return s;
        case \parameterized-sort(str s, list[Symbol] args) : return ("<s>_" | it + "_<value2id(arg)>" | arg <- args);
        case cilit(str s)  : return "cilit_<s>";
	    case lit(/<s:^[A-Za-z0-9]+$>/) : return "lit_<s>"; 
        case int i         : return "<i>";
        case str s         : return ("" | it + "_<charAt(s,i)>" | i <- [0..size(s)-1]);
        case str s()       : return escId(s);
        case node n        : return "<escId(getName(n))>_<("" | it + "_" + value2id(c) | c <- getChildren(n))>";
        case list[value] l : return ("" | it + "_" + value2id(e) | e <- l);
        default            : throw "value not supported <v>";
    }
}
