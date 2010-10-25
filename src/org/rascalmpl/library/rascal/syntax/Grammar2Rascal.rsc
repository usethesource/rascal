module rascal::syntax::Grammar2Rascal

// Convert the Rascal internal grammar representation format (Grammar) to 
// a syntax definition in Rascal source code

// TODO:
// - done!

// Commands to test conversion of Pico or Rascal grammar:
// println(grammar2rascal(readTextValueFile(#Grammar, |stdlib:///org/rascalmpl/library/rascal/conversion/grammar/Pico.grammar|)));
// - Manually paste output to /org/rascalmpl/library/rascal/conversion/grammar/Pico.rsc

// println(grammar2rascal(readTextValueFile(#Grammar, |stdlib:///org/rascalmpl/library/rascal/conversion/grammar/Rascal.grammar|)));
// - Manually paste output to /org/rascalmpl/library/rascal/conversion/grammar/Rascal.rsc

import ParseTree;
import rascal::syntax::Grammar;
import rascal::syntax::Characters;
import rascal::syntax::Escape;
import IO;
import Set;
import List;
import String;
import ValueIO;

bool debug = false;

public str grammar2rascal(Grammar g, str name) {
  return "module <name> <grammar2rascal(g)>";
}

public str grammar2rascal(Grammar g) {
  if (grammar(set[Symbol] start, set[Production] productions) := g) {
    <normals,literals> = separateLiterals(productions);
    return ( "" | it + "\n" + topProd2rascal(p) | gr <- groupByNonTerminal(normals), Production p <- gr)
         + "\n\n"
         + ( "" | it + "\n" + topProd2rascal(p) | Production p <- literals);
  }
  else if (grammar(set[Symbol] start, map[Symbol,set[Production]] rules) := g) {
    return ("" | it + "\n" + topProd2rascal(choice(nont, rules[nont])) | nont <- rules, lit(_) !:= nont)
         + "\n\n"
         + ("" | it + "\n" + topProd2rascal(p) | Symbol nont:lit(_) <- rules, p <- rules[nont]);
  }
}

tuple[set[Production],set[Production]] separateLiterals(set[Production] prods) {
  literals = { p | p:prod(_,lit(_),_) <- prods } 
           + { p | p:restrict(lit(_),_,_) <- prods };
  return <prods - literals, literals>;
}

set[set[Production]] groupByNonTerminal(set[Production] productions) {
  solve (productions) {
    switch (productions) {
      case {set[Production] r, Production p:prod(_,Symbol rhs,_)} : 
        productions = {r, choice(rhs, {p})};
      case {set[Production] r, choice(Symbol s, set[Production] a1), choice(s, set[Production] a2)} :
        productions = {r, choice(s, a1 + a2)};
      case {set[Production] r, choice(Symbol s, set[Production] a1), restrict(s, others(s), set[list[Symbol]] restr)} :
        productions = {r, restrict(s,choice(s,a1), restr)};
    }
  }
   
  return group(productions, same);
}

bool same(Production p, Production q) {
  return p.rhs == q.rhs;
}

public str topProd2rascal(Production p) {
  if (regular(_,_) := p) return "";
  
  return "<(start(_) := p.rhs) ? "start ":""><(\layouts(_) := p.rhs) ? "layout <layoutname(p.rhs)>" : "syntax <symbol2rascal(p.rhs)>">\n\t= <prod2rascal(p)>;\n";
}

str layoutname(Symbol s) {
  if (\layouts(str name) := s)
    return name;
  throw "unexpected <s>";
}

public str prod2rascal(Production p) {
  if(debug) println("prod2rascal: <p>");
  switch (p) {
    case choice(s, alts) : 
        if (alts != {}) {
        	<fst, rest> = takeOneFrom(alts);
			return ( prod2rascal(fst) | "<it>\n\t| <prod2rascal(pr)>" | pr <- rest );
		}
		else {
		  return "...";
		}
    case first(s, alts) :
      	return ( prod2rascal(head(alts)) | "<it>\n\t\> <prod2rascal(pr)>" | pr <- tail(alts) );
      
    case \assoc(s, a, alts) : {
    		<fst, rest> = takeOneFrom(alts);
    		return ( "<attr2mod(\assoc(a))> (  <prod2rascal(fst)> " | "<it>\n\t\t| <prod2rascal(pr)>" | pr <- rest ) + "\n\t)";
 		}
    case diff(s,q,alts) : {
      <fst, rest> = takeOneFrom(alts);
      return ( "<prod2rascal(q)>\n\t- <prod2rascal(fst)>" | "<it>\n\t- <prod2rascal(pr)>" | pr <- rest );
    }
 
    case restrict(rhs, language, restrictions):
    	return "<prod2rascal(language)><for(r <- restrictions){>\n\t# <prod2rascal(r)><}>";
 
    case others(sym):
        return "...";
 
    // case prod(_,lit(_),_) : return "";
    
    case prod(list[Symbol] lhs,Symbol rhs,Attributes attrs) :
      	return "<attrs2mods(attrs)><for(s <- lhs){><symbol2rascal(s)> <}>";
 
    case regular(_,_) :
    	return "";
    
    default: throw "missed a case <p>";
  }
}

test prod2rascal(prod([sort("PICO-ID"),lit(":"),sort("TYPE")],sort("ID-TYPE"),\no-attrs()))
     == "PICO_ID \":\" TYPE ";

test prod2rascal(
     prod([sort("PICO-ID"), lit(":"), sort("TYPE")],
               sort("ID-TYPE"),
              attrs([term(cons("decl")),\assoc(left())]))) ==
               "left decl: PICO_ID \":\" TYPE ";
               
test prod2rascal(
	 prod([\char-class([range(9,9), range(10,10),range(13,13),range(32,32)])],sort("LAYOUT"),attrs([term(cons("whitespace"))]))) ==
	 "whitespace: [\\t\\n\\r\\ ] ";

test prod2rascal(
	first(sort("EXP"),[prod([sort("EXP"),lit("||"),sort("EXP")],sort("EXP"),\no-attrs()),
	                   prod([sort("EXP"),lit("-"),sort("EXP")],sort("EXP"),\no-attrs()),
	                   prod([sort("EXP"),lit("+"),sort("EXP")],sort("EXP"),\no-attrs())])) ==
	"EXP \"||\" EXP \n\t\> EXP \"-\" EXP \n\t\> EXP \"+\" EXP ";	

public str attrs2mods(Attributes as) {
  switch (as) {
    case \no-attrs(): 
      return "";
      
    case \attrs([list[Attr] a,term(node zz:"cons"(str c)),list[Attr] b]) : {
      return attrs2mods(\attrs([a,b])) + "<c>: ";
      }
      
    case \attrs([a,b*]): {
        if(size(b) == 0)
           return "<attr2mod(a)> ";
        return "<attr2mod(a)> <attrs2mods(\attrs(b))>"; 
      }
      
    case \attrs([]):
    	return "";  
    	 
    default:   throw "attrs2rascal: missing case <attrs>";
  }
}

test attrs2mods(\attrs([\assoc(\left())])) == "left ";
test attrs2mods(\attrs([\assoc(\left()), \assoc(\right())])) == "left right ";
test attrs2mods(\attrs([\assoc(\left()), term(cons("C")), \assoc(\right())])) == "left right C: ";
test attrs2mods(\attrs([term(cons("C"))])) == "C: ";
test attrs2mods(\attrs([term(cons("C")), term("lexical")])) == "lex C: ";

public str attr2mod(Attr a) {
  switch(a) {
    case \assoc(\left()): return "left";
    case \assoc(\right()): return "right";
    case \assoc(\non-assoc()): return "non-assoc";
    case \assoc(\assoc()): return "assoc";
    case term("lexical"): return "lex";
    case \bracket(): return "bracket";
    default : return "/*<a>*/";
  }
}

test attr2mod(\assoc(\left())) == "left";

public str symbol2rascal(Symbol sym) {
  switch (sym) {
    case label(str l, x) :
    	return "<symbol2rascal(x)> <l>";  
    case sort(x) :
    	return replaceAll(x, "-", "_");
    case \parameter(x) :
        return "&" + replaceAll(x, "-", "_");
    case lit(x) :
    	return "\"<escape(x)>\"";
    case cilit(x) :
    	return "\"<escape(x)>\"";
    case \lex(x):
    	return symbol2rascal(x);
    case \cf(x):
    	return symbol2rascal(x);
    case \parameterized-sort(str name, list[Symbol] parameters):
        return "<name>[<params2rascal(parameters)>]";
    case \char-class(x) : 
       if (\char-class(y) := complement(sym)) {
         str norm = cc2rascal(x);
         str comp = cc2rascal(y);
         return size(norm) > size(comp) ? "!<comp>" : norm;
       } 
       else throw "weird result of character class complement";
    case \seq(syms):
        return "( <for(s <- syms){> <symbol2rascal(s)> <}> )";
    case opt(x) : 
    	return "<symbol2rascal(x)>?";
    case iter(x) : 
    	return "<symbol2rascal(x)>+";
    case \iter-star(x) : 
    	return "<symbol2rascal(x)>*";
    case \iter-seps(x,seps) :
        return iterseps2rascal(x, seps, "+");
    case \iter-star-seps(x,seps) : 
    	return iterseps2rascal(x, seps, "*");
    case \layouts(str x): 
    	return "";
    case \start(x):
    	return symbol2rascal(x);
    case intersection(lhs, rhs):
        return "<symbol2rascal(lhs)> && <symbol2rascal(rhs)>";
    case union(lhs,rhs):
     	return "<symbol2rascal(lhs)> || <symbol2rascal(rhs)>";
    case difference(lhs,rhs):
     	return "<symbol2rascal(lhs)> -  <symbol2rascal(rhs)>";
    case complement(lhs):
     	return "!<symbol2rascal(lhs)>";
    case \at-column(int i) : 
        return "@<i>";
    case \start-of-line() :
        return "^";
    case \end-of-line():
        return "$";
  }
  throw "symbol2rascal: missing case <sym>";
}

test symbol2rascal(lit("abc")) == "\"abc\"";
test symbol2rascal(lit("\\\n")) == "\"\\\\\\n\"";
test symbol2rascal(sort("ABC")) == "ABC";
test symbol2rascal(cilit("abc")) == "\"abc\"";
test symbol2rascal(label("abc",sort("ABC"))) == "ABC abc";
test symbol2rascal(\parameterized-sort("A", [sort("B")])) == "A[[B]]";
test symbol2rascal(\parameterized-sort("A", [sort("B"), sort("C")])) == "A[[B, C]]";
test symbol2rascal(opt(sort("A"))) == "A?";
test symbol2rascal(\char-class([range(97,97)])) == "[a]";
test symbol2rascal(\iter-star-seps(sort("A"),[\layout()])) == "A*";
test symbol2rascal(\iter-seps(sort("A"),[\layout()])) == "A+";
test symbol2rascal(opt(\iter-star-seps(sort("A"),[\layout()]))) == "A*?";
test symbol2rascal(opt(\iter-seps(sort("A"),[\layout()]))) == "A+?";
test symbol2rascal(\iter-star-seps(sort("A"),[\layout(),lit("x"),\layout()])) == "{A \"x\"}*";
test symbol2rascal(\iter-seps(sort("A"),[\layout(),lit("x"),\layout()])) == "{A \"x\"}+";
test symbol2rascal(opt(\iter-star-seps(sort("A"),[\layout(),lit("x"),\layout()]))) == "{A \"x\"}*?";
test symbol2rascal(opt(\iter-seps(sort("A"),[\layout(),lit("x"),\layout()]))) == "{A \"x\"}+?";
test symbol2rascal(\iter-star(sort("A"))) == "A*";
test symbol2rascal(\iter(sort("A"))) == "A+";
test symbol2rascal(opt(\iter-star(sort("A")))) == "A*?";
test symbol2rascal(opt(\iter(sort("A")))) == "A+?";
test symbol2rascal(\iter-star-seps(sort("A"),[lit("x")])) == "{A \"x\"}*";
test symbol2rascal(\iter-seps(sort("A"),[lit("x")])) == "{A \"x\"}+";
test symbol2rascal(opt(\iter-star-seps(sort("A"),[lit("x")]))) == "{A \"x\"}*?";
test symbol2rascal(opt(\iter-seps(sort("A"),[lit("x")]))) == "{A \"x\"}+?";

public str iterseps2rascal(Symbol sym, list[Symbol] seps, str iter){
  separators = "<for(sp <- seps){><symbol2rascal(sp)><}>";
  if (separators != "")
     return "{<symbol2rascal(sym)> <separators>}<iter>";
  else
    return "<symbol2rascal(sym)><separators><iter>";
}

public str params2rascal(list[Symbol] params){
  len = size(params);
  if(len == 0)
  	return "";
  if(len == 1)
  	return symbol2rascal(params[0]);
  sep = "";
  res = "";
  for(Symbol p <- params){
      res += sep + symbol2rascal(p);
      sep = ", ";
  }
  return res;	
}

public str cc2rascal(list[CharRange] ranges) {
  return "[<range2rascal(head(ranges))><for (r <- tail(ranges)){> <range2rascal(r)><}>]";
}

public str range2rascal(CharRange r) {
  switch (r) {
    case range(c,c) : return makeCharClassChar(c);
    case range(c,d) : return "<makeCharClassChar(c)>-<makeCharClassChar(d)>";
    default: throw "range2rascal: missing case <r>";
  }
}

test range2rascal(range(97,97))  == "a";
test range2rascal(range(97,122)) == "a-z";
test range2rascal(range(10,10))  == "\\n";
test range2rascal(range(34,34))  == "\\\"";

